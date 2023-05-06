package com.teamhike.teamhike.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.teamhike.teamhike.Models.User;
import com.teamhike.teamhike.R;
import com.teamhike.teamhike.network.ApiClient;
import com.teamhike.teamhike.network.ApiInterface;
import com.teamhike.teamhike.SqliteDataBase.DataBase;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SurveyCatchupActivity extends AppCompatActivity {

    List<Button> buttons = new ArrayList<>();
    List<Button> buttonSelected = new ArrayList<>();
    List<Boolean> isClicked = new ArrayList<>();

    private Button btnSubmit;
    private ImageView btnBack;
    private RelativeLayout progressBarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_catchup);
        setupViews();
        initializing();
        setupEvents();
    }

    private void setupViews() {
        buttons.add(findViewById(R.id.surveyCatchup_btnTourismAttractions));
        buttons.add(findViewById(R.id.surveyCatchup_btnNaturalAttractions));
        buttons.add(findViewById(R.id.surveyCatchup_btnEvents));
        buttons.add(findViewById(R.id.surveyCatchup_btnJungle));
        buttons.add(findViewById(R.id.surveyCatchup_btnMountain));
        buttons.add(findViewById(R.id.surveyCatchup_btnSea));
        buttons.add(findViewById(R.id.surveyCatchup_btnLake));
        buttons.add(findViewById(R.id.surveyCatchup_btnWatterFall));
        buttons.add(findViewById(R.id.surveyCatchup_btnFamily));
        buttons.add(findViewById(R.id.surveyCatchup_btnFriends));
        buttons.add(findViewById(R.id.surveyCatchup_btnAlone));
        buttons.add(findViewById(R.id.surveyCatchup_btnEveryThingReady));
        buttons.add(findViewById(R.id.surveyCatchup_btnChallengeTravels));
        btnSubmit = findViewById(R.id.surveyCatchup_btnSubmit);
        btnBack = findViewById(R.id.surveyCatchup_btnBack);
        progressBarLayout = findViewById(R.id.surveyCatchup_progressBarLayout);
    }

    private void initializing() {
        buttonSelected.add(buttons.get(0));
        buttonSelected.add(buttons.get(3));
        buttonSelected.add(buttons.get(8));
        buttonSelected.add(buttons.get(11));
        while (isClicked.size() < 4) {
            isClicked.add(false);
        }
    }

    private void setupEvents() {
        for (int i = 0; i < buttons.size(); i++) {
            buttons.get(i).setOnClickListener(new MyOnClickListener(buttons.get(i), i));
        }
        btnSubmit.setOnClickListener(v -> saveInformation());
        btnBack.setOnClickListener(v -> onBackPressed());
    }

    private void saveInformation() {
        showProgressLayout();
        boolean isReady = true;
        for (int i = 0; i < isClicked.size(); i++) {
            if (!isClicked.get(i)) {
                isReady = false;
                Toast.makeText(this, "لطفا علایق خود را انتخاب کنید", Toast.LENGTH_SHORT).show();
                break;
            }
        }
        if (isReady) {
            register();
        } else hideProgressLayout();
    }

    private void register() {
        Intent intent = getIntent();
        String username,
                name,
                email,
                birthday,
                city,
                gender,
                experience,
                aboutMe,
                phoneNumber,
                encodedImage,
                imagePath,
                userUniqueId,
                post;
        username = intent.getStringExtra("username");
        name = intent.getStringExtra("name");
        email = intent.getStringExtra("email");
        birthday = intent.getStringExtra("birthday");
        city = intent.getStringExtra("city");
        gender = intent.getStringExtra("gender");
        experience = intent.getStringExtra("experience");
        aboutMe = intent.getStringExtra("aboutMe");
        phoneNumber = intent.getStringExtra("phoneNumber");
        post = intent.getStringExtra("post");
        encodedImage = intent.getStringExtra("encodedImage");
        imagePath = intent.getStringExtra("imagePath");
        userUniqueId = intent.getStringExtra("userUniqueId");
        ApiInterface apiInterface = ApiClient.getTeamHikerRetrofit().create(ApiInterface.class);
        apiInterface.registerUser(
                userUniqueId,
                username,
                name,
                email,
                birthday,
                city,
                gender,
                experience,
                phoneNumber,
                aboutMe,
                encodedImage,
                imagePath,
                post
        ).enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getResponse().equals("successfully")) {
                        new DataBase(SurveyCatchupActivity.this).registerLocalUser(userUniqueId, phoneNumber, "yes");
                        Toast.makeText(SurveyCatchupActivity.this, "حساب کاربری شما ثبت شد", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                Toast.makeText(SurveyCatchupActivity.this, "به اینترنت متصل نیستید", Toast.LENGTH_SHORT).show();
                Log.e("Network", "Register User Failure: " + t.getLocalizedMessage());
            }
        });
        apiInterface.registerSurveyCatchup(
                userUniqueId,
                buttonSelected.get(0).getText().toString(),
                buttonSelected.get(1).getText().toString(),
                buttonSelected.get(2).getText().toString(),
                buttonSelected.get(3).getText().toString()
        ).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    startActivity(new Intent(SurveyCatchupActivity.this, MainActivity.class));
                    finish();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                hideProgressLayout();
                Toast.makeText(SurveyCatchupActivity.this, "به اینترنت متصل نیستید", Toast.LENGTH_SHORT).show();
                Log.e("Network", "Register Survey Failure: " + t.getMessage());
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(SurveyCatchupActivity.this, RegisterLoginActivity.class));
        finish();
    }

    protected class MyOnClickListener implements View.OnClickListener {

        Button currentButton;
        int index;

        MyOnClickListener(Button currentButton, int index) {
            this.currentButton = currentButton;
            this.index = index;
        }

        @Override
        public void onClick(View v) {
            if (index <= 2) {
                buttonSelected.get(0).setBackgroundResource(R.drawable.simple_background);
                buttonSelected.set(0, currentButton);
                isClicked.set(0, true);
            } else if (index <= 7) {
                buttonSelected.get(1).setBackgroundResource(R.drawable.simple_background);
                buttonSelected.set(1, currentButton);
                isClicked.set(1, true);
            } else if (index <= 10) {
                buttonSelected.get(2).setBackgroundResource(R.drawable.simple_background);
                buttonSelected.set(2, currentButton);
                isClicked.set(2, true);
            } else {
                buttonSelected.get(3).setBackgroundResource(R.drawable.simple_background);
                buttonSelected.set(3, currentButton);
                isClicked.set(3, true);
            }
            currentButton.setBackgroundResource(R.drawable.simple_selected_background);
        }
    }

    private void hideProgressLayout() {
        progressBarLayout.setVisibility(View.GONE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    private void showProgressLayout() {
        progressBarLayout.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }
}
