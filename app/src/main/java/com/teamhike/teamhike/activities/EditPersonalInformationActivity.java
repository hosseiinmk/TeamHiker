package com.teamhike.teamhike.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.teamhike.teamhike.CustomClasses.Image;
import com.teamhike.teamhike.CustomClasses.RotateImage;
import com.teamhike.teamhike.Models.User;
import com.teamhike.teamhike.R;
import com.teamhike.teamhike.SqliteDataBase.DataBase;
import com.teamhike.teamhike.network.ApiClient;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditPersonalInformationActivity extends AppCompatActivity implements View.OnClickListener {

    private final int REQUEST_CODE = 999;
    private final int MAX_EDIT_BUTTONS = 7;

    private ImageView backBtn;
    private CircleImageView circleImageView;
    private Bitmap bitmap;
    private Uri imageUri;
    private List<LinearLayout> textViewLinearLayouts;
    private List<LinearLayout> editTextLinearLayouts;
    private List<EditText> editTexts;
    private List<TextView> textViews;
    private List<ImageView> editButtons;
    private List<ImageView> doneButtons;
    private List<ImageView> cancelButtons;
    private List<String> data;
    private Button submitBtn, cancelBtn;
    private RelativeLayout progressBarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_personal_information);
        createArraysObjects();
        initialization();
        setupViews();
        setupEvents();
        getUserData();
    }

    private void createArraysObjects() {
        textViewLinearLayouts = new ArrayList<>();
        editTextLinearLayouts = new ArrayList<>();
        editTexts = new ArrayList<>();
        textViews = new ArrayList<>();
        editButtons = new ArrayList<>();
        doneButtons = new ArrayList<>();
        cancelButtons = new ArrayList<>();
        data = new ArrayList<>();
    }

    private void initialization() {
        for (int i = 0; i < MAX_EDIT_BUTTONS; i++) data.add("");
    }

    private void setupViews() {
        // Back Button
        backBtn = findViewById(R.id.editPersonalInformation_backBtn);
        // Image
        circleImageView = findViewById(R.id.editPersonalInformation_circleImage);
        // TextView LinearLayouts
        textViewLinearLayouts.add(findViewById(R.id.editPersonalInformation_usernameLayout));
        textViewLinearLayouts.add(findViewById(R.id.editPersonalInformation_nameLayout));
        textViewLinearLayouts.add(findViewById(R.id.editPersonalInformation_emailLayout));
        textViewLinearLayouts.add(findViewById(R.id.editPersonalInformation_cityLayout));
        textViewLinearLayouts.add(findViewById(R.id.editPersonalInformation_phoneNumberLayout));
        textViewLinearLayouts.add(findViewById(R.id.editPersonalInformation_aboutMeLayout));
        // EditText LinearLayouts
        editTextLinearLayouts.add(findViewById(R.id.editPersonalInformation_editUsernameLayout));
        editTextLinearLayouts.add(findViewById(R.id.editPersonalInformation_editNameLayout));
        editTextLinearLayouts.add(findViewById(R.id.editPersonalInformation_editEmailLayout));
        editTextLinearLayouts.add(findViewById(R.id.editPersonalInformation_editCityLayout));
        editTextLinearLayouts.add(findViewById(R.id.editPersonalInformation_editPhoneNumberLayout));
        editTextLinearLayouts.add(findViewById(R.id.editPersonalInformation_editAboutMeLayout));
        // TextViews
        textViews.add(findViewById(R.id.editPersonalInformation_username));
        textViews.add(findViewById(R.id.editPersonalInformation_name));
        textViews.add(findViewById(R.id.editPersonalInformation_email));
        textViews.add(findViewById(R.id.editPersonalInformation_birthday));
        textViews.add(findViewById(R.id.editPersonalInformation_city));
        textViews.add(findViewById(R.id.editPersonalInformation_phoneNumber));
        textViews.add(findViewById(R.id.editPersonalInformation_aboutMe));
        // EditTexts
        editTexts.add(findViewById(R.id.editPersonalInformation_editUsername));
        editTexts.add(findViewById(R.id.editPersonalInformation_editName));
        editTexts.add(findViewById(R.id.editPersonalInformation_editEmail));
        editTexts.add(findViewById(R.id.editPersonalInformation_editCity));
        editTexts.add(findViewById(R.id.editPersonalInformation_editPhoneNumber));
        editTexts.add(findViewById(R.id.editPersonalInformation_editAboutMe));
        // Edit Buttons
        editButtons.add(findViewById(R.id.editPersonalInformation_username_editBtn));
        editButtons.add(findViewById(R.id.editPersonalInformation_name_editBtn));
        editButtons.add(findViewById(R.id.editPersonalInformation_email_editBtn));
        editButtons.add(findViewById(R.id.editPersonalInformation_birthday_editBtn));
        editButtons.add(findViewById(R.id.editPersonalInformation_city_editBtn));
        editButtons.add(findViewById(R.id.editPersonalInformation_phoneNumber_editBtn));
        editButtons.add(findViewById(R.id.editPersonalInformation_aboutMe_editBtn));
        // Done Buttons
        doneButtons.add(findViewById(R.id.editPersonalInformation_editUsername_doneBtn));
        doneButtons.add(findViewById(R.id.editPersonalInformation_editName_doneBtn));
        doneButtons.add(findViewById(R.id.editPersonalInformation_editEmail_doneBtn));
        doneButtons.add(findViewById(R.id.editPersonalInformation_editCity_doneBtn));
        doneButtons.add(findViewById(R.id.editPersonalInformation_editPhoneNumber_doneBtn));
        doneButtons.add(findViewById(R.id.editPersonalInformation_editAboutMe_doneBtn));
        // Cancel Buttons
        cancelButtons.add(findViewById(R.id.editPersonalInformation_editUsername_cancelBtn));
        cancelButtons.add(findViewById(R.id.editPersonalInformation_editName_cancelBtn));
        cancelButtons.add(findViewById(R.id.editPersonalInformation_editEmail_cancelBtn));
        cancelButtons.add(findViewById(R.id.editPersonalInformation_editCity_cancelBtn));
        cancelButtons.add(findViewById(R.id.editPersonalInformation_editPhoneNumber_cancelBtn));
        cancelButtons.add(findViewById(R.id.editPersonalInformation_editAboutMe_cancelBtn));
        // Submit And Cancel Buttons
        submitBtn = findViewById(R.id.editPersonalInformation_submitBtn);
        cancelBtn = findViewById(R.id.editPersonalInformation_cancelBtn);
        // ProgressBar
        progressBarLayout = findViewById(R.id.editPersonalInformation_progressBarLayout);
    }

    private void setupEvents() {
        final int MAX_DONE_BUTTONS = 6;
        circleImageView.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, REQUEST_CODE);
        });
        for (int i = 0; i < MAX_EDIT_BUTTONS; i++) editButtons.get(i).setOnClickListener(this);
        for (int i = 0; i < MAX_DONE_BUTTONS; i++) {
            doneButtons.get(i).setOnClickListener(this);
            cancelButtons.get(i).setOnClickListener(this);
        }
        backBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);
        submitBtn.setOnClickListener(this);
    }

    private void getUserData() {
        String userUniqueId = new DataBase(this).getActiveLocalUser().getUniqueId();
        MainActivity.apiInterface.getUserFromUniqueId(userUniqueId).enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    User user = response.body();
                    if (!user.getImage().isEmpty()) {
                        circleImageView.setPadding(0, 0, 0, 0);
                        Picasso.get().load(ApiClient.TEAM_HIKER_URL + user.getImage()).into(circleImageView);
                    }
                    textViews.get(0).setText(user.getUsername());
                    textViews.get(1).setText(user.getName());
                    textViews.get(2).setText(user.getEmail());
                    textViews.get(3).setText(user.getBirthday());
                    textViews.get(4).setText(user.getCity());
                    textViews.get(5).setText(user.getPhoneNumber());
                    textViews.get(6).setText(user.getAboutMe());
                }
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                Log.e("Network", "Get User Failure: " + t.getLocalizedMessage());
            }
        });
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
                bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() / 3, bitmap.getHeight() / 3, true);
                circleImageView.setPadding(0, 0, 0, 0);
                circleImageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.editPersonalInformation_backBtn) {
          onBackPressed();
        } else if (viewId == R.id.editPersonalInformation_submitBtn) {
            updateUserData(this);
            for (int i = 0; i < editTextLinearLayouts.size(); i++) disableEdit(i);
        } else if (viewId == R.id.editPersonalInformation_cancelBtn) {
            onBackPressed();
        } else if (viewId == R.id.editPersonalInformation_birthday_editBtn) {
            DialogFragment fragment = new DatePickerFragment(this, textViews.get(3), data);
            fragment.show(getSupportFragmentManager(), "date picker");
        } else {
            for (int i = 0; i < MAX_EDIT_BUTTONS; i++) {
                if (viewId == editButtons.get(i).getId()) {
                    if (i > 3) enableEdit(i - 1);
                    else enableEdit(i);
                    break;
                } else if (viewId == doneButtons.get(i).getId()) {
                    if (!editTexts.get(i).getText().toString().trim().isEmpty()) {
                        if (i >= 3) {
                            data.set(i + 1, editTexts.get(i).getText().toString().trim());
                            textViews.get(i + 1).setText(data.get(i + 1));
                        } else {
                            data.set(i, editTexts.get(i).getText().toString().trim());
                            textViews.get(i).setText(data.get(i));
                        }
                    }
                    disableEdit(i);
                    break;
                } else if (viewId == cancelButtons.get(i).getId()) {
                    disableEdit(i);
                    break;
                }
            }
        }
    }

    private void updateUserData(Context context) {
        showProgressLayout();
        boolean isReady = false;
        for (int x = 0; x < data.size(); x++) {
            if (!data.get(x).isEmpty() || (imageUri != null && bitmap != null)) {
                isReady = true;
                break;
            }
        }
        if (isReady) {
            DataBase db = new DataBase(this);
            String userUniqueId = db.getActiveLocalUser().getUniqueId();
            // Check PhoneNumber
            if (!data.get(5).isEmpty()) {
                db.updateLocalUser(DataBase.DB_COLUMN_PHONE_NUMBER, data.get(5), DataBase.DB_COLUMN_LOGGED, "yes");
            }
            String encodedImage = "";
            String imagePath = "";
            if (imageUri != null && bitmap != null) {
                Image image = new Image();
                String imageName = image.createImageName();
                encodedImage = image.getEncodedImage(bitmap);
                imagePath = "uploads/" + imageName;
            }
            MainActivity.apiInterface.updateUser (
                    userUniqueId,
                    data.get(0),
                    data.get(1),
                    data.get(2),
                    data.get(3),
                    data.get(4),
                    data.get(5),
                    imagePath,
                    encodedImage,
                    data.get(6),
                    ""
            ).enqueue(new Callback<User>() {
                @Override
                public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        if (response.body().getResponse().equals("successfully")) {
                            hideProgressLayout();
                            Toast.makeText(context, "اطلاعات بروزرسانی شدند", Toast.LENGTH_SHORT).show();
                            onBackPressed();
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                    hideProgressLayout();
                    Log.e("Network", "Update User Failure: " + t.getLocalizedMessage());
                }
            });
        } else {
            hideProgressLayout();
            Toast.makeText(this, "موردی برای بروزرسانی وجود ندارد", Toast.LENGTH_SHORT).show();
        }
    }

    private void enableEdit(int index) {
        editTextLinearLayouts.get(index).setVisibility(View.VISIBLE);
        textViewLinearLayouts.get(index).setVisibility(View.GONE);
    }

    private void disableEdit(int index) {
        editTextLinearLayouts.get(index).setVisibility(View.GONE);
        textViewLinearLayouts.get(index).setVisibility(View.VISIBLE);
    }

    private void hideProgressLayout() {
        progressBarLayout.setVisibility(View.GONE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    private void showProgressLayout() {
        progressBarLayout.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    private class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        Context context;
        TextView dateTextView;
        List<String> data;

        DatePickerFragment(Context context, TextView dateTextView, List<String> data) {
            this.context = context;
            this.dateTextView = dateTextView;
            this.data = data;
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(context, R.style.DatePickerStyle, this, year, month, day);
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            String dateFormat = year + "/" + month + "/" + dayOfMonth;
            dateTextView.setText(dateFormat);
            data.set(3, dateFormat);
        }
    }
}