package com.teamhike.teamhike.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.teamhike.teamhike.Models.User;
import com.teamhike.teamhike.R;
import com.teamhike.teamhike.SqliteDataBase.DataBase;

public class RegisterLoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button registerBtn, loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_login);
        checkLogged();
        setupViews();
        setupEvents();
    }

    private void checkLogged() {
        User user = new DataBase(this).getActiveLocalUser();
        if (user != null) {
            startActivity(new Intent(RegisterLoginActivity.this, MainActivity.class));
            finish();
        }
    }

    private void setupViews() {
        registerBtn = findViewById(R.id.registerLogin_registerBtn);
        loginBtn = findViewById(R.id.registerLogin_loginBtn);
    }

    private void setupEvents() {
        registerBtn.setOnClickListener(this);
        loginBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        startActivity(new Intent(this, PhoneNumberActivity.class));
        finish();
    }
}
