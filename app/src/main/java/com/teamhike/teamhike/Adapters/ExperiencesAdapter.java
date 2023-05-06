package com.teamhike.teamhike.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.teamhike.teamhike.activities.MainActivity;
import com.teamhike.teamhike.Models.Experience;
import com.teamhike.teamhike.Models.FavoriteExperience;
import com.teamhike.teamhike.R;
import com.teamhike.teamhike.activities.ExperienceCommentsActivity;
import com.teamhike.teamhike.network.ApiClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExperiencesAdapter extends RecyclerView.Adapter<ExperiencesAdapter.ItemViewHolder> {

    Activity activity;
    List<Experience> experiences;
    String userUniqueId;
    List<FavoriteExperience> favoriteExperiences;
    List<String> likes = new ArrayList<>();
    List<Boolean> isLiked = new ArrayList<>();

    public ExperiencesAdapter(Activity activity, List<Experience> experiences, String userUniqueId, List<FavoriteExperience> favoriteExperiences) {
        this.activity = activity;
        this.experiences = experiences;
        this.userUniqueId = userUniqueId;
        this.favoriteExperiences = favoriteExperiences;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemViewHolder(LayoutInflater.from(activity).inflate(R.layout.item_experience, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        isLiked.add(position, false);
        likes.add(experiences.get(position).getLikes());
        holder.location.setText(experiences.get(position).getLocation());
        holder.viewNumber.setText(experiences.get(position).getViews());
        holder.likesNumber.setText(experiences.get(position).getLikes());
        holder.description.setText(experiences.get(position).getDescription());
        holder.goodNotes.setText(experiences.get(position).getGoodNotes());
        holder.badNotes.setText(experiences.get(position).getBadNotes());
        Picasso.get().load(ApiClient.TEAM_HIKER_URL + experiences.get(position).getImage()).into(holder.image);
        if (favoriteExperiences != null) {
            boolean checkIsLiked = holder.checkIsLiked(favoriteExperiences, experiences.get(position).getExperienceUniqueId());
            if (checkIsLiked) {
                holder.setLike(position);
            } else {
                holder.setDislike(position);
            }
        }
        holder.btnLike.setOnClickListener(v -> {
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
        holder.comment.setOnClickListener(v -> {
            Intent intent = new Intent(activity, ExperienceCommentsActivity.class);
            intent.putExtra("experienceOwnerUniqueId", userUniqueId);
            intent.putExtra("experienceUniqueId", experiences.get(position).getExperienceUniqueId());
            activity.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return experiences.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        ImageView btnLike;
        ImageView comment;
        TextView location;
        TextView viewNumber;
        TextView likesNumber;
        TextView description;
        TextView goodNotes;
        TextView badNotes;

        ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            location = itemView.findViewById(R.id.experienceItem_location);
            image = itemView.findViewById(R.id.experienceItem_image);
            location = itemView.findViewById(R.id.experienceItem_location);
            viewNumber = itemView.findViewById(R.id.experienceItem_viewNumber);
            btnLike = itemView.findViewById(R.id.experienceItem_btnLike);
            likesNumber = itemView.findViewById(R.id.experienceItem_likesNumber);
            description = itemView.findViewById(R.id.experienceItem_description);
            comment = itemView.findViewById(R.id.experienceItem_commentBtn);
            goodNotes = itemView.findViewById(R.id.experienceItem_goodNotes);
            badNotes = itemView.findViewById(R.id.experienceItem_badNotes);
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
            btnLike.setImageResource(R.drawable.ic_filled_favorite);
            isLiked.set(position, true);
        }

        public void setDislike(int position) {
            btnLike.setImageResource(R.drawable.ic_favorite_black);
            isLiked.set(position, false);
        }
    }
}
