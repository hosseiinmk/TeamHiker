package com.teamhike.teamhike.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.teamhike.teamhike.Models.Experience;
import com.teamhike.teamhike.Models.FavoriteExperience;
import com.teamhike.teamhike.Models.User;
import com.teamhike.teamhike.R;
import com.teamhike.teamhike.SqliteDataBase.DataBase;
import com.teamhike.teamhike.network.ApiClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShareExperienceActivity extends AppCompatActivity {

    private ImageView backBtn;
    private RecyclerView recyclerView;
    private List<FavoriteExperience> favoriteExperiences;
    private String userUniqueId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_experience);
        setupViews();
        setupEvents();
        getExperiences();
    }

    private void setupViews() {
        backBtn = findViewById(R.id.shareExperience_backBtn);
        recyclerView = findViewById(R.id.shareExperience_RecyclerView);
    }

    private void setupEvents() {
        backBtn.setOnClickListener(v -> onBackPressed());
    }

    private void getExperiences() {
        MainActivity.apiInterface.getAllExperience().enqueue(new Callback<List<Experience>>() {
            @Override
            public void onResponse(@NonNull Call<List<Experience>> call, @NonNull Response<List<Experience>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().get(0).getResponse() == null) {
                        List<Experience> experiences = response.body();
                        getExperiencesOwnersUsername(experiences);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Experience>> call, @NonNull Throwable t) {
                Log.e("Network", "Get All Experiences Failure: " + t.getLocalizedMessage());
            }
        });
    }

    private void getExperiencesOwnersUsername(List<Experience> experiences) {
        MainActivity.apiInterface.getAllUsers().enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(@NonNull Call<List<User>> call, @NonNull Response<List<User>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<User> users = response.body();
                    findOwners(experiences, users);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<User>> call, @NonNull Throwable t) {
                Log.e("Network", "Get All Users Failure: " + t.getLocalizedMessage());
            }
        });
    }

    private void findOwners(List<Experience> experiences, List<User> users) {
        List<User> owners = new ArrayList<>();
        for (Experience experience : experiences) {
            for (User user : users) {
                if (user.getUniqueId().equals(experience.getUserUniqueId())) {
                    owners.add(user);
                }
            }
        }
        getUserFavoriteExperiences(experiences, owners);
    }

    private void getUserFavoriteExperiences(List<Experience> experiences, List<User> owners) {
        userUniqueId = new DataBase(this).getActiveLocalUser().getUniqueId();
        MainActivity.apiInterface.getUserFavoriteExperiences(userUniqueId).enqueue(new Callback<List<FavoriteExperience>>() {
            @Override
            public void onResponse(@NonNull Call<List<FavoriteExperience>> call, @NonNull Response<List<FavoriteExperience>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().get(0).getResponse() == null) {
                        favoriteExperiences = response.body();
                    }
                    initializingRecyclerView(experiences, owners);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<FavoriteExperience>> call, @NonNull Throwable t) {
                Log.e("Network", "Get User Favorite Experiences: " + t.getLocalizedMessage());
            }
        });
    }

    private void initializingRecyclerView(List<Experience> experiences, List<User> owners) {
        if (experiences != null && owners != null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            ShareExperiencesAdapter adapter = new ShareExperiencesAdapter(this, experiences, owners, favoriteExperiences, userUniqueId);
            recyclerView.setAdapter(adapter);
        }
    }

    private class ShareExperiencesAdapter extends RecyclerView.Adapter<ShareExperiencesAdapter.ItemViewHolder> {

        Activity activity;
        List<Experience> experiences;
        List<User> ownerData;
        List<FavoriteExperience> favoriteExperiences;
        String userUniqueId;
        List<String> likes = new ArrayList<>();
        List<Boolean> isLiked = new ArrayList<>();

        public ShareExperiencesAdapter(
                Activity activity,
                List<Experience> experiences,
                List<User> ownerData,
                List<FavoriteExperience> favoriteExperiences,
                String userUniqueId) {
            this.activity = activity;
            this.experiences = experiences;
            this.ownerData = ownerData;
            this.favoriteExperiences = favoriteExperiences;
            this.userUniqueId = userUniqueId;
        }

        @NonNull
        @Override
        public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ShareExperiencesAdapter.ItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.item_share_experience,
                    parent,
                    false
            ));
        }

        @Override
        public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
            isLiked.add(position, false);
            likes.add(experiences.get(position).getLikes());
            holder.username.setText(ownerData.get(position).getUsername());
            holder.location.setText(experiences.get(position).getLocation());
            Picasso.get().load(ApiClient.TEAM_HIKER_URL + experiences.get(position).getImage()).into(holder.image);
            holder.viewNumber.setText(experiences.get(position).getViews());
            holder.likesNumber.setText(likes.get(position));
            holder.goodNotes.setText(experiences.get(position).getGoodNotes());
            holder.badNotes.setText(experiences.get(position).getBadNotes());
            if (favoriteExperiences != null) {
                boolean checkIsLiked = holder.checkIsLiked(favoriteExperiences, experiences.get(position).getExperienceUniqueId());
                if (checkIsLiked) {
                    holder.setLike(position);
                } else {
                    holder.setDislike(position);
                }
            }
            holder.likeBtn.setOnClickListener(v -> {
                int mLikesNumber = Integer.parseInt(likes.get(position));
                if (isLiked.get(position)) {
                    holder.setDislike(position);
                    mLikesNumber -= 1;
                    likes.set(position, String.valueOf(Integer.parseInt(likes.get(position)) - 1));
                    holder.deleteFavoriteExperience(experiences.get(position).getExperienceUniqueId(), userUniqueId);
                } else {
                    holder.setLike(position);
                    mLikesNumber += 1;
                    likes.set(position, String.valueOf(Integer.parseInt(likes.get(position)) + 1));
                    holder.saveFavoriteExperience(experiences.get(position).getExperienceUniqueId(), userUniqueId);
                }
                holder.likesNumber.setText(String.valueOf(mLikesNumber));
                holder.updateExperience(experiences.get(position).getExperienceUniqueId(), String.valueOf(mLikesNumber));
            });
            holder.commentBtn.setOnClickListener(v -> {
                Intent intent = new Intent(activity, ExperienceCommentsActivity.class);
                intent.putExtra("experienceOwnerUniqueId", ownerData.get(position).getUniqueId());
                intent.putExtra("experienceUniqueId", experiences.get(position).getExperienceUniqueId());
                activity.startActivity(intent);
            });
        }

        @Override
        public int getItemCount() {
            return experiences.size();
        }

        public class ItemViewHolder extends RecyclerView.ViewHolder {

            TextView location, username, likesNumber, goodNotes, badNotes, viewNumber;
            ImageView image, likeBtn, commentBtn;

            public ItemViewHolder(@NonNull View itemView) {
                super(itemView);
                location = itemView.findViewById(R.id.shareExperienceItem_location);
                username = itemView.findViewById(R.id.shareExperienceItem_username);
                image = itemView.findViewById(R.id.shareExperienceItem_image);
                viewNumber = itemView.findViewById(R.id.shareExperienceItem_viewsNumber);
                likeBtn = itemView.findViewById(R.id.shareExperienceItem_btnLike);
                likesNumber = itemView.findViewById(R.id.shareExperienceItem_LikesNumber);
                commentBtn = itemView.findViewById(R.id.shareExperienceItem_btnComment);
                goodNotes = itemView.findViewById(R.id.shareExperienceItem_goodNotes);
                badNotes = itemView.findViewById(R.id.shareExperienceItem_badNotes);
            }

            public void saveFavoriteExperience(String experienceUniqueId, String userUniqueId) {
                MainActivity.apiInterface.saveFavoriteExperience(
                        experienceUniqueId,
                        userUniqueId
                ).enqueue(new Callback<FavoriteExperience>() {
                    @Override
                    public void onResponse(@NonNull Call<FavoriteExperience> call, @NonNull Response<FavoriteExperience> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getResponse().equals("failed")) {
                                Log.d("DEBUG", "save failed");
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<FavoriteExperience> call, @NonNull Throwable t) {
                        Log.e("Network", "Save Favorite Experience Failure: " + t.getLocalizedMessage());
                    }
                });
            }

            public void deleteFavoriteExperience(String experienceUniqueId, String userUniqueId) {
                MainActivity.apiInterface.deleteFavoriteExperience(
                        experienceUniqueId,
                        userUniqueId
                ).enqueue(new Callback<FavoriteExperience>() {
                    @Override
                    public void onResponse(@NonNull Call<FavoriteExperience> call, @NonNull Response<FavoriteExperience> response) {
                    }

                    @Override
                    public void onFailure(@NonNull Call<FavoriteExperience> call, @NonNull Throwable t) {
                        Log.e("Network", "Delete Favorite Experience Failure: " + t.getLocalizedMessage());
                    }
                });
            }

            public void updateExperience(String experienceUniqueId, String likes) {
                MainActivity.apiInterface.updateExperience(
                        experienceUniqueId,
                        likes
                ).enqueue(new Callback<Experience>() {
                    @Override
                    public void onResponse(@NonNull Call<Experience> call, @NonNull Response<Experience> response) {
                    }

                    @Override
                    public void onFailure(@NonNull Call<Experience> call, @NonNull Throwable t) {
                        Log.e("Network", "Delete Favorite Experience Failure: " + t.getLocalizedMessage());
                    }
                });
            }

            public boolean checkIsLiked(List<FavoriteExperience> favoriteExperiences, String experienceUniqueId) {
                boolean isLiked = false;
                for (FavoriteExperience fe : favoriteExperiences) {
                    if (fe.getExperienceUniqueId().equals(experienceUniqueId)) {
                        isLiked = true;
                        break;
                    }
                }
                return isLiked;
            }

            public void setLike(int position) {
                likeBtn.setImageResource(R.drawable.ic_filled_favorite);
                isLiked.set(position, true);
            }

            public void setDislike(int position) {
                likeBtn.setImageResource(R.drawable.ic_favorite_black);
                isLiked.set(position, false);
            }
        }
    }
}