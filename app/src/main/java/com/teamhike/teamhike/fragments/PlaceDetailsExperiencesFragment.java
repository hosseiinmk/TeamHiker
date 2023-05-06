package com.teamhike.teamhike.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.teamhike.teamhike.activities.MainActivity;
import com.teamhike.teamhike.Models.Experience;
import com.teamhike.teamhike.Models.User;
import com.teamhike.teamhike.R;
import com.teamhike.teamhike.activities.ExperienceCommentsActivity;
import com.teamhike.teamhike.network.ApiClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlaceDetailsExperiencesFragment extends Fragment {

    private Activity activity;
    private RecyclerView recyclerView;
    private String provinceName;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = getActivity();
    }

    public PlaceDetailsExperiencesFragment(String provinceName) {
        this.provinceName = provinceName;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_place_details_experiences, container, false);
        setupViews(view);
        setupEvents();
        getExperiences();
        return view;
    }

    private void setupViews(View view) {
        recyclerView = view.findViewById(R.id.placeDetailsExperiences_recyclerView);
    }

    private void setupEvents() {

    }

    private void getExperiences() {
        Log.d("Debug", "Province: " + provinceName);
        MainActivity.apiInterface.getExperiencesByProvinceName(provinceName).enqueue(new Callback<List<Experience>>() {
            @Override
            public void onResponse(@NonNull Call<List<Experience>> call, @NonNull Response<List<Experience>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().get(0).getResponse() == null) {
                        List<Experience> experiences = response.body();
                        Log.d("Debug", "Experiences Size: " + experiences.size());
                        getAllUsers(experiences);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Experience>> call, @NonNull Throwable t) {
                Log.e("Network", "Get Experience By Province Name Failure: " + t.getLocalizedMessage());
            }
        });
    }

    private void getAllUsers(List<Experience> experiences) {
        MainActivity.apiInterface.getAllUsers().enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(@NonNull Call<List<User>> call, @NonNull Response<List<User>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<User> users = response.body();
                    List<User> owners = new ArrayList<>();
                    for (Experience experience : experiences) {
                        for (User user : users) {
                            if (user.getUniqueId().equals(experience.getUserUniqueId())) {
                                owners.add(user);
                            }
                        }
                    }
                    initializingRecyclerView(experiences, owners);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<User>> call, @NonNull Throwable t) {
                Log.e("Network", "Get All Users: " + t.getLocalizedMessage());
            }
        });
    }

    private void initializingRecyclerView(List<Experience> experiences, List<User> owners) {
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        PlaceDetailsExperiencesAdapter adapter = new PlaceDetailsExperiencesAdapter(activity, experiences, owners);
        recyclerView.setAdapter(adapter);
    }

    private class PlaceDetailsExperiencesAdapter extends RecyclerView.Adapter<PlaceDetailsExperiencesAdapter.ItemViewHolder> {

        Activity activity;
        List<Experience> experiences;
        List<User> owners;

        public PlaceDetailsExperiencesAdapter(Activity activity, List<Experience> experiences, List<User> owners) {
            this.activity = activity;
            this.experiences = experiences;
            this.owners = owners;
        }

        @NonNull
        @Override
        public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ItemViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_share_experience, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
            holder.location.setText(experiences.get(position).getLocation());
            holder.username.setText(owners.get(position).getUsername());
            if (!experiences.get(position).getImage().isEmpty()) {
                Picasso.get().load(ApiClient.TEAM_HIKER_URL + experiences.get(position).getImage()).into(holder.image);
            }
            holder.likesNumber.setText(experiences.get(position).getLikes());
            holder.goodNotes.setText(experiences.get(position).getGoodNotes());
            holder.badNotes.setText(experiences.get(position).getBadNotes());
            holder.btnLike.setOnClickListener(v -> {

            });
            holder.btnComment.setOnClickListener(v -> {
                Intent intent = new Intent(activity, ExperienceCommentsActivity.class);
                intent.putExtra("experienceOwnerUniqueId", owners.get(position).getUniqueId());
                intent.putExtra("experienceUniqueId", experiences.get(position).getExperienceUniqueId());
                activity.startActivity(intent);
            });
        }

        @Override
        public int getItemCount() {
            return experiences.size();
        }

        public class ItemViewHolder extends RecyclerView.ViewHolder {

            TextView location, username, views, likesNumber, goodNotes, badNotes;
            ImageView image, btnLike, btnComment;

            public ItemViewHolder(@NonNull View itemView) {
                super(itemView);
                location = itemView.findViewById(R.id.shareExperienceItem_location);
                username = itemView.findViewById(R.id.shareExperienceItem_username);
                image = itemView.findViewById(R.id.shareExperienceItem_image);
                views = itemView.findViewById(R.id.shareExperienceItem_viewsNumber);
                btnLike = itemView.findViewById(R.id.shareExperienceItem_btnLike);
                likesNumber = itemView.findViewById(R.id.shareExperienceItem_LikesNumber);
                btnComment = itemView.findViewById(R.id.shareExperienceItem_btnComment);
                goodNotes = itemView.findViewById(R.id.shareExperienceItem_goodNotes);
                badNotes = itemView.findViewById(R.id.shareExperienceItem_badNotes);
            }
        }
    }
}
