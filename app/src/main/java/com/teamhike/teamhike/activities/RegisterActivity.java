package com.teamhike.teamhike.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Toast;

import com.teamhike.teamhike.CustomClasses.Image;
import com.teamhike.teamhike.CustomClasses.RotateImage;
import com.teamhike.teamhike.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterActivity extends AppCompatActivity {

    private final String[] citySuggestions = new String[]{
            "تهران", "البرز", "رشت", "اصفهان"
    };

    List<EditText> editTexts = new ArrayList<>();

    private final int REQUEST_CODE = 999;

    private ScrollView scrollView;
    private CircleImageView circleImageView;
    private AutoCompleteTextView editTextCity;
    private RadioGroup genderRadioGroup, experienceRadioGroup;
    private RadioButton genderMaleBtn, experienceYesBtn;
    private ImageView backBtn;
    private Button submitBtn;
    private DatePicker datePicker;
    private Uri imageUri;
    private Bitmap bitmap;
    private String username, name, email, birthday, city, gender, experience, aboutMe, phoneNumber;
//    private RelativeLayout progressBarLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setupViews();
        setupEvents();
    }

    private void setupViews() {
        scrollView = findViewById(R.id.scrollview);
        circleImageView = findViewById(R.id.register_circleImage);
        editTexts.add(findViewById(R.id.register_editTextUsername));
        editTexts.add(findViewById(R.id.register_editTextName));
        editTexts.add(findViewById(R.id.register_editTextEmail));
        editTextCity = findViewById(R.id.register_editTextCity);
        datePicker = findViewById(R.id.register_datePicker);
        genderRadioGroup = findViewById(R.id.register_genderRadioGroup);
        experienceRadioGroup = findViewById(R.id.register_experienceRadioGroup);
        genderMaleBtn = findViewById(R.id.register_maleRadioGroup);
        experienceYesBtn = findViewById(R.id.registerFormExperienceYes);
        backBtn = findViewById(R.id.register_backBtn);
        submitBtn = findViewById(R.id.register_submitBtn);
//        progressBarLayout = findViewById(R.id.register_progressBarLayout);
    }

    private void setupEvents() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, citySuggestions
        );
        editTextCity.setAdapter(adapter);
        scrollView.setVerticalScrollBarEnabled(false);
        circleImageView.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, REQUEST_CODE);
        });
        backBtn.setOnClickListener(v -> onBackPressed());
        submitBtn.setOnClickListener(v -> registerUser());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
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

    private void registerUser() {
//        showProgressLayout();
        Intent intent = getIntent();
        phoneNumber = intent.getStringExtra("phoneNumber");
        username = editTexts.get(0).getText().toString().trim();
        name = editTexts.get(1).getText().toString().trim();
        email = editTexts.get(2).getText().toString().trim();
        birthday = datePicker.getYear() + "/" + datePicker.getMonth() + "/" + datePicker.getDayOfMonth();
        city = editTextCity.getText().toString().trim();
        aboutMe = "";
        int genderRadioID = genderRadioGroup.getCheckedRadioButtonId();
        if (genderRadioID > 0) {
            RadioButton genderBtn = findViewById(genderRadioID);
            if (genderBtn == genderMaleBtn) gender = "male";
            else gender = "female";
        } else gender = "";
        int experienceRadioID = experienceRadioGroup.getCheckedRadioButtonId();
        if (experienceRadioID > 0) {
            RadioButton experienceBtn = findViewById(experienceRadioID);
            if (experienceBtn == experienceYesBtn) experience = "yes";
            else experience = "no";
        } else experience = "";
        if (username.isEmpty()) {
//            hideProgressLayout();
            Toast.makeText(this, "نام کاربری را وارد کنید", Toast.LENGTH_LONG).show();
        } else if (name.isEmpty()) {
//            hideProgressLayout();
            Toast.makeText(this, "نام و نام خانوادگی را وارد کنید", Toast.LENGTH_LONG).show();
        } else if (email.isEmpty()) {
//            hideProgressLayout();
            Toast.makeText(this, "ایمیل را وارد کنید", Toast.LENGTH_LONG).show();
        } else if (city.isEmpty()) {
//            hideProgressLayout();
            Toast.makeText(this, "شهر خود را وارد کنید", Toast.LENGTH_LONG).show();
        } else if (gender.isEmpty()) {
//            hideProgressLayout();
            Toast.makeText(this, "جنست خود را مشخص کنید", Toast.LENGTH_LONG).show();
        } else if (experience.isEmpty()) {
//            hideProgressLayout();
            Toast.makeText(this, "تجربه خود را با ما به اشتراک بگذارید", Toast.LENGTH_LONG).show();
        } else {
            goToSurvey();
        }
    }

    private void goToSurvey() {
        String userUniqueId = generateUniqueId();
        String encodedImage = "";
        String imagePath = "";
        if (imageUri != null && bitmap != null) {
            Image image = new Image();
            String imageName = image.createImageName();
            encodedImage = image.getEncodedImage(bitmap);
            imagePath = "uploads/" + imageName;
        }
        Intent intent = new Intent(RegisterActivity.this, SurveyCatchupActivity.class);
        intent.putExtra("username", username);
        intent.putExtra("name", name);
        intent.putExtra("email", email);
        intent.putExtra("birthday", birthday);
        intent.putExtra("city", city);
        intent.putExtra("gender", gender);
        intent.putExtra("experience", experience);
        intent.putExtra("phoneNumber", phoneNumber);
        intent.putExtra("post", "نوپا");
        intent.putExtra("aboutMe", aboutMe);
        intent.putExtra("encodedImage", encodedImage);
        intent.putExtra("imagePath", imagePath);
        intent.putExtra("userUniqueId", userUniqueId);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(RegisterActivity.this, RegisterLoginActivity.class));
        finish();
    }

    private String generateUniqueId() {
        int min = 1000, max = 9999;
        double uniqueId = Math.random() * (max - min + 1) + min;
        return "user_" + (int) uniqueId;
    }

//    private void hideProgressLayout() {
//        progressBarLayout.setVisibility(View.GONE);
//        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
//    }
//
//    private void showProgressLayout() {
//        progressBarLayout.setVisibility(View.VISIBLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
//    }
}
