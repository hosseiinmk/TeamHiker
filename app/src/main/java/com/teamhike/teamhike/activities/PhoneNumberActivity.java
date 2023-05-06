package com.teamhike.teamhike.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.teamhike.teamhike.R;

import java.util.ArrayList;
import java.util.List;

public class PhoneNumberActivity extends AppCompatActivity {

    private final String CHANNEL_ID = "verification code channel";

    List<EditText> editTexts = new ArrayList<>();

    private ImageView backBtn;
    private TextView submitBtn;
    private NotificationManager notificationManager;

    private double random_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_number);
        setupViews();
        setupEvents();
    }

    private void setupViews() {
        editTexts.add(findViewById(R.id.phoneNumber_numOne));
        editTexts.add(findViewById(R.id.phoneNumber_numTwo));
        editTexts.add(findViewById(R.id.phoneNumber_numThree));
        editTexts.add(findViewById(R.id.phoneNumber_numFour));
        editTexts.add(findViewById(R.id.phoneNumber_numFive));
        editTexts.add(findViewById(R.id.phoneNumber_numSix));
        editTexts.add(findViewById(R.id.phoneNumber_numSeven));
        editTexts.add(findViewById(R.id.phoneNumber_numEight));
        editTexts.add(findViewById(R.id.phoneNumber_numNine));
        editTexts.add(findViewById(R.id.phoneNumber_numTen));
        editTexts.add(findViewById(R.id.phoneNumber_numEleven));
        backBtn = findViewById(R.id.phoneNumber_backBtn);
        submitBtn = findViewById(R.id.phoneNumber_submitBtn);
    }

    private void setupEvents() {
        for (int i = 0; i < editTexts.size(); i++) {
            if (i != 0) {
                editTexts.get(i).setOnKeyListener(new MyOnKeyListener(editTexts.get(i-1), editTexts.get(i)));
                if (i == editTexts.size() - 1) editTexts.get(i).addTextChangedListener(new MyTextWatcher(editTexts.get(i-1), editTexts.get(i), editTexts.get(i)));
                else editTexts.get(i).addTextChangedListener(new MyTextWatcher(editTexts.get(i-1), editTexts.get(i), editTexts.get(i+1)));
                editTexts.get(i).setEnabled(false);
            } else {
                editTexts.get(i).requestFocus();
                editTexts.get(i).addTextChangedListener(new MyTextWatcher(editTexts.get(i), editTexts.get(i), editTexts.get(i+1)));
            }
        }
        editTexts.get(editTexts.size() - 1).setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL) {
                if (editTexts.get(editTexts.size() - 1).length() == 0) {
                    editTexts.get(editTexts.size() - 2).requestFocus();
                    editTexts.get(editTexts.size() - 2).setText("");
                }
            } else if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                checkPhoneNumber();
            }
            return false;
        });
        backBtn.setOnClickListener(v -> onBackPressed());
        submitBtn.setOnClickListener(v -> checkPhoneNumber());
    }

    private void checkPhoneNumber() {
        String fNumber = editTexts.get(0).getText().toString();
        String sNumber = editTexts.get(1).getText().toString();
        String phoneNumber = fNumber + sNumber;
        for (int i = 2; i < editTexts.size(); i++)
            phoneNumber = phoneNumber.concat(editTexts.get(i).getText().toString());
        if (phoneNumber.isEmpty() || (phoneNumber.length() < editTexts.size()))
            Toast.makeText(this, "شماره همراه کامل وارد نشده است!", Toast.LENGTH_LONG).show();
        else if (Integer.parseInt(fNumber) != 0 || Integer.parseInt(sNumber) != 9)
            Toast.makeText(this, "شماره همراه اشتباه است!", Toast.LENGTH_LONG).show();
        else {
            sendVerificationCode();
            Intent intent = new Intent(PhoneNumberActivity.this, VerificationCodeActivity.class);
            intent.putExtra("phoneNumber", phoneNumber);
            intent.putExtra("randomCode", (int) random_code);
            startActivity(intent);
            finish();
        }
    }

    private void sendVerificationCode() {
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelName = "Verification Code";
            String channelDescription = "The verification code for user";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, channelName, importance);
            channel.setDescription(channelDescription);
            notificationManager.createNotificationChannel(channel);
        }
        sendNotification();
    }

    private void sendNotification() {
        int min = 1000, max = 9999;
        random_code = Math.random() * (max - min + 1) + min;
        NotificationCompat.Builder notifyBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_message)
                .setContentTitle("کد تایید هویت")
                .setContentText((int) random_code + "")
                .setPriority(NotificationCompat.PRIORITY_HIGH);
        notificationManager.notify(0, notifyBuilder.build());
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(PhoneNumberActivity.this, RegisterLoginActivity.class));
        finish();
    }

    public static class MyTextWatcher implements TextWatcher {

        EditText previousEditText, currentEditText, nextEditText;

        MyTextWatcher(EditText twPrevious, EditText twCurrentEditText, EditText twNextEditText) {
            this.previousEditText = twPrevious;
            this.currentEditText = twCurrentEditText;
            this.nextEditText = twNextEditText;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (currentEditText.length() == 1 && nextEditText.length() == 0) {
                nextEditText.setEnabled(true);
                nextEditText.requestFocus();
            }
        }
    }

    public static class MyOnKeyListener implements View.OnKeyListener {

        EditText previousEditText, currentEditText;

        MyOnKeyListener(EditText previousEditText, EditText currentEditText) {
            this.previousEditText = previousEditText;
            this.currentEditText = currentEditText;
        }

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL) {
                if (currentEditText.length() == 0) {
                    previousEditText.requestFocus();
                    previousEditText.setText("");
                }
            }
            return false;
        }
    }
}
