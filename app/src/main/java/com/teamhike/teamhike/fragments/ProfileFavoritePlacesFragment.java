package com.teamhike.teamhike.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.teamhike.teamhike.Adapters.FavoritePlacesAdapter;
import com.teamhike.teamhike.CustomClasses.ReplaceFragment;
import com.teamhike.teamhike.CustomClasses.SharedPreferences;
import com.teamhike.teamhike.Models.Place;
import com.teamhike.teamhike.R;
import com.teamhike.teamhike.SqliteDataBase.DataBase;
import com.teamhike.teamhike.activities.MainActivity;
import com.teamhike.teamhike.fragments.PlacesFragment;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFavoritePlacesFragment extends Fragment {

    private Activity activity;
    private FragmentManager fragmentManager;
    private View view;
    private ImageView addBtn;
    private RecyclerView recyclerView;
    private List<Place> favoritePlaces;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = getActivity();
        fragmentManager = getFragmentManager();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile_favorite_places, container, false);
        setupViews();
        setupEvents();
        showFavoritePlaces();
        return view;
    }

    private void setupViews() {
        addBtn = view.findViewById(R.id.places_addBtn);
        recyclerView = view.findViewById(R.id.places_recyclerView);
    }

    private void setupEvents() {
        addBtn.setOnClickListener(v -> {
            new ReplaceFragment().gotoFragment(new PlacesFragment(), fragmentManager);
            new SharedPreferences().setSharedPreferences(activity, "fragmentName", "profileChooseDestinationFragment");
        });
    }

    private void showFavoritePlaces() {
        String userUniqueId = new DataBase(activity).getActiveLocalUser().getUniqueId();
        MainActivity.apiInterface.getFavoritePlaces(userUniqueId).enqueue(new Callback<List<Place>>() {
            @Override
            public void onResponse(@NonNull Call<List<Place>> call, @NonNull Response<List<Place>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().get(0).getResponse() == null) {
                            favoritePlaces = response.body();
                            recyclerView.setLayoutManager(new LinearLayoutManager(activity));
                            recyclerView.setAdapter(new FavoritePlacesAdapter(activity, favoritePlaces));
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Place>> call, @NonNull Throwable t) {
                Log.e("GET_FAV_PLACES", t.getLocalizedMessage());
            }
        });
    }
}
