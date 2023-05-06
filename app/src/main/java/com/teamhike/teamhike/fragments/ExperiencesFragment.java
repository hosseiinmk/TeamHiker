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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.teamhike.teamhike.Adapters.ExperiencesAdapter;
import com.teamhike.teamhike.Models.Experience;
import com.teamhike.teamhike.R;
import com.teamhike.teamhike.SqliteDataBase.DataBase;
import com.teamhike.teamhike.activities.AddExperienceActivity;
import com.teamhike.teamhike.activities.MainActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExperiencesFragment extends Fragment {

    private Activity activity;
    private View view;
    private ImageView addBtn;
    private RecyclerView recyclerView;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_experiences, container, false);
        setupViews();
        setupEvents();
        getExperiences();
        return view;
    }

    private void setupViews() {
        recyclerView = view.findViewById(R.id.experiences_recyclerView);
        addBtn = view.findViewById(R.id.experiences_addBtn);
    }

    private void setupEvents() {
        addBtn.setOnClickListener(v -> {
            Intent intent = new Intent(activity, AddExperienceActivity.class);
            startActivity(intent);
        });
    }

    private void getExperiences() {
        String userUniqueId = new DataBase(activity).getActiveLocalUser().getUniqueId();
        MainActivity.apiInterface.getUserExperiencesByUniqueId(userUniqueId).enqueue(new Callback<List<Experience>>() {
            @Override
            public void onResponse(@NonNull Call<List<Experience>> call, @NonNull Response<List<Experience>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().get(0).getResponse() == null) {
                        List<Experience> experiences = response.body();
                        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
                        recyclerView.setAdapter(new ExperiencesAdapter(activity, experiences, userUniqueId, null));
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Experience>> call, @NonNull Throwable t) {
                Log.e("Network", "Get User Experiences Failure: " + t.getLocalizedMessage());
            }
        });
    }
}
