package com.teamhike.teamhike.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
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

public class VerificationCodeActivity extends AppCompatActivity {

    List<EditText> editTexts = new ArrayList<>();

    private ApiInterface apiInterface;
    private ImageView backBtn;
    private TextView submitBtn;
    private String phoneNumber;
    private RelativeLayout progressBarLayout;
    private String userUniqueId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_code);
        setupViews();
        setupEvents();
    }

    private void setupViews() {
        editTexts.add(findViewById(R.id.verificationCode_numOne));
        editTexts.add(findViewById(R.id.verificationCode_numTwo));
        editTexts.add(findViewById(R.id.verificationCode_numThree));
        editTexts.add(findViewById(R.id.verificationCode_numFour));
        backBtn = findViewById(R.id.verificationCode_backBtn);
        submitBtn = findViewById(R.id.verificationCode_submitBtn);
        progressBarLayout = findViewById(R.id.verificationCode_progressBarLayout);
    }

    private void setupEvents() {
        for (int i = 0; i < editTexts.size(); i++) {
            if (i != 0) {
                editTexts.get(i).setOnKeyListener(new PhoneNumberActivity.MyOnKeyListener(editTexts.get(i - 1), editTexts.get(i)));
                if (i == editTexts.size() - 1)
                    editTexts.get(i).addTextChangedListener(new PhoneNumberActivity.MyTextWatcher(editTexts.get(i - 1), editTexts.get(i), editTexts.get(i)));
                else
                    editTexts.get(i).addTextChangedListener(new PhoneNumberActivity.MyTextWatcher(editTexts.get(i - 1), editTexts.get(i), editTexts.get(i + 1)));
                editTexts.get(i).setEnabled(false);
            } else {
                editTexts.get(i).requestFocus();
                editTexts.get(i).addTextChangedListener(new PhoneNumberActivity.MyTextWatcher(editTexts.get(i), editTexts.get(i), editTexts.get(i + 1)));
            }
        }
        editTexts.get(editTexts.size() - 1).setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL) {
                if (editTexts.get(editTexts.size() - 1).length() == 0) {
                    editTexts.get(editTexts.size() - 2).requestFocus();
                    editTexts.get(editTexts.size() - 2).setText("");
                }
            } else if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                checkVerificationCode();
            }
            return false;
        });
        backBtn.setOnClickListener(v -> onBackPressed());
        submitBtn.setOnClickListener(v -> checkVerificationCode());
    }

    private void checkVerificationCode() {
        showProgressLayout();
        String code1 = editTexts.get(0).getText().toString();
        String code2 = editTexts.get(1).getText().toString();
        String code3 = editTexts.get(2).getText().toString();
        String code4 = editTexts.get(3).getText().toString();
        String verificationCode = code1 + code2 + code3 + code4;
        if (verificationCode.isEmpty() || verificationCode.length() < 4) {
            hideProgressLayout();
            Toast.makeText(this, "کد تایید هویت کامل وارد نشده است", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = getIntent();
            phoneNumber = intent.getStringExtra("phoneNumber");
            int randomCode = intent.getIntExtra("randomCode", 0);
            if (randomCode != Integer.parseInt(verificationCode)) {
                hideProgressLayout();
                Toast.makeText(this, "کد تایید هویت اشتباه است!", Toast.LENGTH_SHORT).show();
            } else {
                apiInterface = ApiClient.getTeamHikerRetrofit().create(ApiInterface.class);
                apiInterface.getUserFromPhoneNumber(phoneNumber).enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getResponse() != null) {
                                Intent intent = new Intent(VerificationCodeActivity.this, RegisterActivity.class);
                                intent.putExtra("phoneNumber", phoneNumber);
                                startActivity(intent);
                                finish();
                            } else {
                                userUniqueId = response.body().getUniqueId();
                                Log.d("Debug", "Get User => Unique ID: " + userUniqueId);
                                checkLocalUser();
                                updateUser();
                            }
                            hideProgressLayout();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                        hideProgressLayout();
                        Toast.makeText(VerificationCodeActivity.this, "به اینترنت متصل نیستید", Toast.LENGTH_SHORT).show();
                        Log.e("Network", "Get User Failure: " + t.getLocalizedMessage());
                    }
                });
            }
        }
    }

    private void updateUser() {
        apiInterface.updateUser(
                userUniqueId,
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "yes"
        ).enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                if (response.isSuccessful()) {
                    startActivity(new Intent(VerificationCodeActivity.this, MainActivity.class));
                    finish();
                }
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                Toast.makeText(VerificationCodeActivity.this, "به اینترنت متصل نیستید", Toast.LENGTH_SHORT).show();
                Log.e("Network", "Update User Failure: " + t.getLocalizedMessage());
            }
        });
    }

    private void checkLocalUser() {
        boolean exist = false;
        DataBase db = new DataBase(this);
        List<User> users = db.getLocalUsers();
        if (users == null) {
            db.registerLocalUser(userUniqueId, phoneNumber, "yes");
        } else {
            for (User user : users) {
                if (user.getPhoneNumber().equals(phoneNumber)) {
                    exist = true;
                    break;
                }
            }
            if (exist) {
                db.updateLocalUser(DataBase.DB_COLUMN_LOGGED, "yes", DataBase.DB_COLUMN_PHONE_NUMBER, phoneNumber);
            } else {
                db.registerLocalUser(userUniqueId, phoneNumber, "yes");
            }
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(VerificationCodeActivity.this, RegisterLoginActivity.class));
        finish();
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
