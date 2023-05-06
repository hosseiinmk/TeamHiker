package com.teamhike.teamhike.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.teamhike.teamhike.Adapters.AddPlaceAdapter;
import com.teamhike.teamhike.Models.Place;
import com.teamhike.teamhike.R;
import com.teamhike.teamhike.activities.MainActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddPlacesFragment extends Fragment {

    private Activity activity;
    private List<Place> places;
    private View view;
    private EditText searchBox;
    private RecyclerView recyclerView;
    private AddPlaceAdapter adapter;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_places, container, false);
        setupViews();
        setupEvents();
//        getAllPlaces();
        return view;
    }

    private void setupViews() {
        searchBox = view.findViewById(R.id.addPlaces_searchBox);
        recyclerView = view.findViewById(R.id.addPlaces_recyclerView);
    }

    private void setupEvents() {
        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()) showAllPlaces();
//                else searchOnPlaces(s.toString());
            }
        });
    }

    private void getAllPlaces() {
        MainActivity.apiInterface.getAllPlaces().enqueue(new Callback<List<Place>>() {
            @Override
            public void onResponse(@NonNull Call<List<Place>> call, @NonNull Response<List<Place>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    places = response.body();
                    setRecyclerViewAdapter();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Place>> call, @NonNull Throwable t) {
                Log.e("GET_ALL_PLACES", t.getLocalizedMessage());
            }
        });
    }

    private void setRecyclerViewAdapter() {
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        adapter = new AddPlaceAdapter(activity, places);
        recyclerView.setAdapter(adapter);
    }

    private void showAllPlaces() {
        adapter.updatePlacesData(places);
    }

//    private void searchOnPlaces(String placeName) {
//        List<Place> foundPlaces = new ArrayList<>();
//        for (Place place : places) {
//            if (place.getDestination().contains(placeName))
//                foundPlaces.add(place);
//        }
//        adapter.updatePlacesData(foundPlaces);
//    }
}
