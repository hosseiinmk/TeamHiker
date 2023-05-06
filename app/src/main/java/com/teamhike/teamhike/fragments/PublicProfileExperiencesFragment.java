package com.teamhike.teamhike.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.teamhike.teamhike.Adapters.ExperiencesAdapter;
import com.teamhike.teamhike.Models.Experience;
import com.teamhike.teamhike.Models.FavoriteExperience;
import com.teamhike.teamhike.Models.User;
import com.teamhike.teamhike.R;
import com.teamhike.teamhike.SqliteDataBase.DataBase;
import com.teamhike.teamhike.activities.MainActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PublicProfileExperiencesFragment extends Fragment {

    private Activity activity;
    private User user;
    private View view;
    private RecyclerView recyclerView;
    private List<FavoriteExperience> favoriteExperiences;

    public PublicProfileExperiencesFragment(User user) {
        this.user = user;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_public_profile_experiences, container, false);
        setupViews();
        getExperiencesData();
        return view;
    }

    private void setupViews() {
        recyclerView = view.findViewById(R.id.publicProfileExperiences_recyclerView);
    }


    private void getExperiencesData() {
        MainActivity.apiInterface.getUserExperiencesByUniqueId(user.getUniqueId()).enqueue(new Callback<List<Experience>>() {
            @Override
            public void onResponse(@NonNull Call<List<Experience>> call, @NonNull Response<List<Experience>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().get(0).getResponse() == null) {
                        List<Experience> experiences = response.body();
                        getUserFavoriteExperiences(experiences);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Experience>> call, @NonNull Throwable t) {
                Log.e("Network", "Get All Experiences Failure: " + t.getLocalizedMessage());
            }
        });
    }

    private void getUserFavoriteExperiences(List<Experience> experiences) {
        String userUniqueId = new DataBase(activity).getActiveLocalUser().getUniqueId();
        MainActivity.apiInterface.getUserFavoriteExperiences(userUniqueId).enqueue(new Callback<List<FavoriteExperience>>() {
            @Override
            public void onResponse(@NonNull Call<List<FavoriteExperience>> call, @NonNull Response<List<FavoriteExperience>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().get(0).getResponse() == null) {
                        favoriteExperiences = response.body();
                    }
                    setupRecyclerView(experiences);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<FavoriteExperience>> call, @NonNull Throwable t) {
                Log.e("Network", "Get User Favorite Experiences: " + t.getLocalizedMessage());
            }
        });
    }

    private void setupRecyclerView(List<Experience> experiences) {
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.setAdapter(new ExperiencesAdapter(activity, experiences, user.getUniqueId(), favoriteExperiences));
    }
}
