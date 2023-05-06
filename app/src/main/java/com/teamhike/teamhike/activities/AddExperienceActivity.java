package com.teamhike.teamhike.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.teamhike.teamhike.CustomClasses.Image;
import com.teamhike.teamhike.CustomClasses.RotateImage;
import com.teamhike.teamhike.Models.Experience;
import com.teamhike.teamhike.R;
import com.teamhike.teamhike.SqliteDataBase.DataBase;

import java.io.IOException;
import java.io.InputStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddExperienceActivity extends AppCompatActivity implements View.OnClickListener {

    private final int REQUEST_CODE = 999;

    private Button submitBtn, cancelBtn;
    private ImageView image, backBtn;
    private TextView addLocation, goodNotesDropDown, badNotesDropDown;
    private EditText description, goodNotes, badNotes;
    private Uri imageUri;
    private Bitmap bitmap;
    private RelativeLayout progressBarLayout;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_experience);
        setupViews();
        setupEvents();
    }

    private void setupViews() {
        backBtn = findViewById(R.id.addExperience_backBtn);
        image = findViewById(R.id.addExperience_image);
        addLocation = findViewById(R.id.addExperience_addLocation);
        description = findViewById(R.id.addExperience_description);
        goodNotes = findViewById(R.id.addExperience_goodNotes);
        badNotes = findViewById(R.id.addExperience_badNotes);
        goodNotesDropDown = findViewById(R.id.addExperience_goodNotesDropDownTitle);
        badNotesDropDown = findViewById(R.id.addExperience_badNotesDropDownTitle);
        submitBtn = findViewById(R.id.addExperience_submitBtn);
        cancelBtn = findViewById(R.id.addExperience_cancelBtn);
        progressBarLayout = findViewById(R.id.addExperience_progressBarLayout);
    }

    private void setupEvents() {
        backBtn.setOnClickListener(this);
        image.setOnClickListener(this);
        addLocation.setOnClickListener(this);
        submitBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);
        goodNotesDropDown.setOnClickListener(this);
        badNotesDropDown.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == R.id.addExperience_backBtn) {
            onBackPressed();
        } else if (viewId == R.id.addExperience_image) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, REQUEST_CODE);
        } else if (viewId == R.id.addExperience_addLocation) {
            startActivity(new Intent(this, AddExperienceLocationActivity.class));
        } else if (viewId == R.id.addExperience_submitBtn) {
            saveExperience(this);
        } else if (viewId == R.id.addExperience_cancelBtn) {
            onBackPressed();
        } else if (viewId == R.id.addExperience_goodNotesDropDownTitle) {
            goodNotesDropDown.setVisibility(View.GONE);
            goodNotes.setVisibility(View.VISIBLE);
        } else if (viewId == R.id.addExperience_badNotesDropDownTitle) {
            badNotesDropDown.setVisibility(View.GONE);
            badNotes.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            try {
                imageUri = data.getData();
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                InputStream inputStream = getContentResolver().openInputStream(imageUri);
                bitmap = new RotateImage().getRotatedImageBitmap(inputStream, bitmap);
//                bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() / 3, bitmap.getHeight() / 3, true);
                image.setScaleType(ImageView.ScaleType.CENTER_CROP);
                image.setPadding(0, 0, 0, 0);
                image.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveExperience(Context context) {
        showProgressbar();
        if (imageUri == null) {
            Toast.makeText(this, "عکس انتخاب نشده", Toast.LENGTH_SHORT).show();
            hideProgressbar();
        } else if (AddExperienceLocationActivity.selectedLocation.isEmpty()) {
            Toast.makeText(this, "مکان تجربه خود را مشخص کنید", Toast.LENGTH_SHORT).show();
            hideProgressbar();
        } else if (description.getText().toString().isEmpty()) {
            Toast.makeText(this, "توضیحات وارد نشده", Toast.LENGTH_SHORT).show();
            hideProgressbar();
        }  else {
            String location = AddExperienceLocationActivity.selectedLocation;
            String province = AddExperienceLocationActivity.selectedLocationProvince;
            AddExperienceLocationActivity.selectedLocation = "";
            AddExperienceLocationActivity.selectedLocationProvince = "";
            String views = "0";
            String likes = "0";
            String descriptionText = description.getText().toString().trim();
            String encodedImage = "";
            String imagePath = "";
            String positiveNotes = goodNotes.getText().toString().trim();
            String negativeNotes = badNotes.getText().toString().trim();
            if (bitmap != null) {
                Image image = new Image();
                String imageName = image.createImageName();
                encodedImage = image.getEncodedImage(bitmap);
                imagePath = "experiences/" + imageName;
            }
            String experienceUniqueId = generateUniqueId();
            String userUniqueId = new DataBase(this).getActiveLocalUser().getUniqueId();
            MainActivity.apiInterface.registerExperience(
                    userUniqueId,
                    experienceUniqueId,
                    location,
                    province,
                    descriptionText,
                    positiveNotes,
                    negativeNotes,
                    imagePath,
                    encodedImage,
                    likes,
                    views
            ).enqueue(new Callback<Experience>() {
                @Override
                public void onResponse(@NonNull Call<Experience> call, @NonNull Response<Experience> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        if (response.body().getResponse().equals("successfully")) {
                            Toast.makeText(context, "تجربه شما ثبت شد", Toast.LENGTH_LONG).show();
                            onBackPressed();
                        } else {
                            Toast.makeText(context, "تجربه ثبت نشد!", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(context, "متاسفانه خطایی رخ داده!", Toast.LENGTH_LONG).show();
                    }
                    hideProgressbar();
                }

                @Override
                public void onFailure(@NonNull Call<Experience> call, @NonNull Throwable t) {
                    Log.e("Network", "Register Experience Failure: " + t.getLocalizedMessage());
                    Toast.makeText(context, "به اینترنت متصل نیستید", Toast.LENGTH_SHORT).show();
                    hideProgressbar();
                }
            });
        }
    }

    private void showProgressbar() {
        progressBarLayout.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    private void hideProgressbar() {
        progressBarLayout.setVisibility(View.GONE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    private String generateUniqueId() {
        int min = 10000, max = 99999;
        double uniqueId = Math.random() * (max - min + 1) + min;
        return "experience_" + (int) uniqueId;
    }
}