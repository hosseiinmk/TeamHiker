package com.teamhike.teamhike.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.teamhike.teamhike.Models.GroupMessage;
import com.teamhike.teamhike.R;
import com.teamhike.teamhike.SqliteDataBase.DataBase;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GroupChatActivity extends AppCompatActivity {

    ImageView sendMessageBtn, refreshBtn;
    EditText messageBox;
    private RelativeLayout overLayout;
    private Button returnToHomeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);
        setupViews();
        setupEvents();
        getIntentExtra();
    }

    private void setupViews() {
        sendMessageBtn = findViewById(R.id.groupChat_sendMessageBtn);
        messageBox = findViewById(R.id.groupChat_messageBox);
        refreshBtn = findViewById(R.id.groupChat_refreshBtn);
        overLayout = findViewById(R.id.groupChat_overLayout);
        returnToHomeBtn = findViewById(R.id.groupChat_returnToHomeBtn);
    }

    private void setupEvents() {
        String senderUniqueId = new DataBase(this).getActiveLocalUser().getUniqueId();
        String groupUniqueId = "group_1234";
        sendMessageBtn.setOnClickListener(v -> {
            String message = messageBox.getText().toString().trim();
            MainActivity.apiInterface.sendGroupMessage(
                    groupUniqueId,
                    senderUniqueId,
                    message
            ).enqueue(new Callback<GroupMessage>() {
                @Override
                public void onResponse(@NonNull Call<GroupMessage> call, @NonNull Response<GroupMessage> response) {

                }

                @Override
                public void onFailure(@NonNull Call<GroupMessage> call, @NonNull Throwable t) {
                    Log.e("Network", "Send Group Chat Failure: " + t.getLocalizedMessage());
                }
            });
        });
        refreshBtn.setOnClickListener(v -> {
            MainActivity.apiInterface.getGroupChats(groupUniqueId).enqueue(new Callback<List<GroupMessage>>() {
                @Override
                public void onResponse(@NonNull Call<List<GroupMessage>> call, @NonNull Response<List<GroupMessage>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        List<GroupMessage> messages = response.body();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<List<GroupMessage>> call, @NonNull Throwable t) {
                    Log.e("Network", "Get Group Chats Failure: " + t.getLocalizedMessage());
                }
            });
        });
        returnToHomeBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });
    }

    private void getIntentExtra() {
        if (getIntent() != null) {
            Intent intent = getIntent();
            int createGroupFinalStep;
            createGroupFinalStep = intent.getIntExtra("createGroupFinalStep", 0);
            if (createGroupFinalStep == 1) {
                showOverLayout();
            }
        }
    }

    private void showOverLayout() {
        overLayout.setVisibility(View.VISIBLE);
        messageBox.setEnabled(false);
        sendMessageBtn.setEnabled(false);
        refreshBtn.setEnabled(false);
    }
}