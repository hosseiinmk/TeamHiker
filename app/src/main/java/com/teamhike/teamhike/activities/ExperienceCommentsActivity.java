package com.teamhike.teamhike.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.teamhike.teamhike.Models.ExperienceDetails;
import com.teamhike.teamhike.Models.User;
import com.teamhike.teamhike.R;
import com.teamhike.teamhike.SqliteDataBase.DataBase;
import com.teamhike.teamhike.network.ApiClient;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExperienceCommentsActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView backBtn;
    private CircleImageView userImage;
    private TextView username, sendCommentBtn;
    private RecyclerView recyclerView;
    private EditText comment;
    private String experienceOwnerUniqueId, experienceUniqueId;
    private List<ExperienceDetails> experienceDetails;
    private ExperienceCommentsAdapter adapter;
    private List<User> allUsers;
    private List<User> commentsOwners;
    private String senderUniqueId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experience_comments);
        senderUniqueId = new DataBase(this).getActiveLocalUser().getUniqueId();
        commentsOwners = new ArrayList<>();
        getIntentExtras();
        setupViews();
        setupEvents();
        getOwnerData();
        getExperienceDetails();
    }

    private void getIntentExtras() {
        Intent intent = getIntent();
        experienceOwnerUniqueId = intent.getStringExtra("experienceOwnerUniqueId");
        experienceUniqueId = intent.getStringExtra("experienceUniqueId");
    }

    private void setupViews() {
        backBtn = findViewById(R.id.experienceComments_backBtn);
        userImage = findViewById(R.id.experienceComments_image);
        username = findViewById(R.id.experienceComments_username);
        recyclerView = findViewById(R.id.experienceComments_recyclerView);
        comment = findViewById(R.id.experienceComments_comment);
        sendCommentBtn = findViewById(R.id.experienceComments_sendBtn);
    }

    private void setupEvents() {
        backBtn.setOnClickListener(this);
        sendCommentBtn.setOnClickListener(this);
    }

    private void getOwnerData() {
        MainActivity.apiInterface.getUserFromUniqueId(experienceOwnerUniqueId).enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    User user = response.body();
                    if (!user.getImage().isEmpty())
                        Picasso.get().load(ApiClient.TEAM_HIKER_URL + user.getImage()).into(userImage);
                    else userImage.setImageResource(R.drawable.personal_info_photo);
                    username.setText(user.getUsername());
                }
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                Log.e("Network", "Get User Data failure: " + t.getLocalizedMessage());
            }
        });
    }

    private void getExperienceDetails() {
        MainActivity.apiInterface.getExperienceDetailsByUniqueId(experienceUniqueId).enqueue(new Callback<List<ExperienceDetails>>() {
            @Override
            public void onResponse(@NonNull Call<List<ExperienceDetails>> call, @NonNull Response<List<ExperienceDetails>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().get(0).getResponse() == null) {
                        experienceDetails = response.body();
                        recyclerView.smoothScrollToPosition(experienceDetails.size() - 1);
                        if (allUsers == null) {
                            getAllUsers();
                        }
                        if (adapter != null) {
                            List<User> commentsOwners = findSenders();
                            adapter.updateExperienceDetails(experienceDetails, commentsOwners);
                        }
                        Log.d("DEBUG", "Get Experience Details Success");
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ExperienceDetails>> call, @NonNull Throwable t) {
                Log.e("Network", "Get Experience Details Failure: " + t.getLocalizedMessage());
            }
        });
    }

    private void getAllUsers() {
        MainActivity.apiInterface.getAllUsers().enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(@NonNull Call<List<User>> call, @NonNull Response<List<User>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    allUsers = response.body();
                    List<User> commentsOwners = findSenders();
                    setRecyclerView(experienceDetails, commentsOwners);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<User>> call, @NonNull Throwable t) {
                Log.e("Network", "Get All Users: " + t.getLocalizedMessage());
            }
        });
    }

    private List<User> findSenders() {
        if (commentsOwners != null) commentsOwners.clear();
        for (User user : allUsers) {
            for (ExperienceDetails details : experienceDetails) {
                if (user.getUniqueId().equals(details.getSenderUniqueId())) {
                    commentsOwners.add(user);
                }
            }
        }
        return commentsOwners;
    }

    private void setRecyclerView(List<ExperienceDetails> EDs, List<User> SDs) {
        recyclerView.setLayoutManager(new LinearLayoutManager(ExperienceCommentsActivity.this));
        adapter = new ExperienceCommentsAdapter(EDs, SDs);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == R.id.experienceComments_backBtn) {
            onBackPressed();
        } else if (viewId == R.id.experienceComments_sendBtn) {
            String commentText = comment.getText().toString().trim();
            if (!commentText.isEmpty()) {
                sendComment(commentText);
                comment.setText("");
                Log.d("DEBUG", "Comment Sent Step 1");
            }
            else Log.d("DEBUG", "onClick failure: ");
        }
    }

    private void sendComment(String comment) {
        Log.d("DEBUG", "Experience Owner UniqueId: " + experienceOwnerUniqueId);
        Log.d("DEBUG", "Experience UniqueId: " + experienceUniqueId);
        Log.d("DEBUG", "Sender UniqueId: " + senderUniqueId);
        Log.d("DEBUG", "Comment: " + comment);
        MainActivity.apiInterface.sendExperienceComment(
                experienceOwnerUniqueId,
                experienceUniqueId,
                senderUniqueId,
                comment
        ).enqueue(new Callback<ExperienceDetails>() {
            @Override
            public void onResponse(@NonNull Call<ExperienceDetails> call, @NonNull Response<ExperienceDetails> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("DEBUG", "Response: " + response.body().getResponse());
                    if (response.body().getResponse().equals("successfully")) {
                        getExperienceDetails();
                        Log.d("DEBUG", "Comment Sent Step 2");
                    } else {
                        Log.d("DEBUG", "Comment Sent Failure");
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ExperienceDetails> call, @NonNull Throwable t) {
                Log.e("Network", "Send Comment Failure: " + t.getLocalizedMessage());
            }
        });
    }

    private static class ExperienceCommentsAdapter extends RecyclerView.Adapter<ExperienceCommentsAdapter.ItemViewHolder> {

        List<ExperienceDetails> details;
        List<User> commentsOwners;

        public ExperienceCommentsAdapter(List<ExperienceDetails> details, List<User> commentsOwners) {
            this.details = details;
            this.commentsOwners = commentsOwners;
        }

        @NonNull
        @Override
        public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.experience_comment_item,
                    parent,
                    false
            ));
        }

        @Override
        public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
            if (!commentsOwners.get(position).getImage().isEmpty())
                Picasso.get().load(ApiClient.TEAM_HIKER_URL + commentsOwners.get(position).getImage()).into(holder.senderImage);
            else holder.senderImage.setImageResource(R.drawable.personal_info_photo);
            holder.senderUsername.setText(commentsOwners.get(position).getUsername());
            holder.senderComment.setText(details.get(position).getComment());
        }

        @Override
        public int getItemCount() {
            return details.size();
        }

        public void updateExperienceDetails(List<ExperienceDetails> details, List<User> commentsOwners) {
            this.details = details;
            this.commentsOwners = commentsOwners;
            notifyDataSetChanged();
        }

        public static class ItemViewHolder extends RecyclerView.ViewHolder {

            CircleImageView senderImage;
            TextView senderUsername, senderComment;

            public ItemViewHolder(@NonNull View itemView) {
                super(itemView);
                senderImage = itemView.findViewById(R.id.experienceCommentItem_image);
                senderUsername = itemView.findViewById(R.id.experienceCommentItem_username);
                senderComment = itemView.findViewById(R.id.experienceCommentItem_comment);
            }
        }
    }
}