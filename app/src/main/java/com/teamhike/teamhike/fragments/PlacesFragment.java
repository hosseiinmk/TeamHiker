package com.teamhike.teamhike.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.teamhike.teamhike.activities.MainActivity;
import com.teamhike.teamhike.Models.Blog;
import com.teamhike.teamhike.Models.Place;
import com.teamhike.teamhike.R;
import com.teamhike.teamhike.activities.PlaceDetailsActivity;
import com.teamhike.teamhike.network.ApiClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlacesFragment extends Fragment implements View.OnClickListener, TextWatcher {

    private Activity activity;
    private EditText searchBox;
    private TextView btnJungles, btnLakeAndWaterfall, btnHistoricalPlaces;
    private RecyclerView recyclerView;
    private ImageView addBtn;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_places, container, false);
        setupViews(view);
        setupEvents();
        getAllPlaces();
        return view;
    }

    private void setupViews(View view) {
        recyclerView = view.findViewById(R.id.placesFragment_recyclerView);
        searchBox = view.findViewById(R.id.placesFragment_searchBox);
        btnJungles = view.findViewById(R.id.placesFragment_btnEvents);
        btnLakeAndWaterfall = view.findViewById(R.id.placesFragment_btnNaturalAttractionsPlaces);
        btnHistoricalPlaces = view.findViewById(R.id.placesFragment_btnAttractionPlaces);
    }

    private void setupEvents() {
        searchBox.addTextChangedListener(this);
        btnJungles.setOnClickListener(this);
        btnLakeAndWaterfall.setOnClickListener(this);
        btnHistoricalPlaces.setOnClickListener(this);
    }

    private void getAllPlaces() {
        MainActivity.apiInterface.getAllPlaces().enqueue(new Callback<List<Place>>() {
            @Override
            public void onResponse(@NonNull Call<List<Place>> call, @NonNull Response<List<Place>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Place> places = response.body();
                    setupRecyclerView(places);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Place>> call, @NonNull Throwable t) {
                Log.e("Network", "Get All Places Failure: "+ t.getLocalizedMessage());
            }
        });
    }

    private void getAllBlog() {
        MainActivity.apiInterface.getAllBlog().enqueue(new Callback<List<Blog>>() {
            @Override
            public void onResponse(@NonNull Call<List<Blog>> call, @NonNull Response<List<Blog>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Blog> allBlog = response.body();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Blog>> call, @NonNull Throwable t) {
                Log.e("Network", "Get All Blog Failure: " + t.getLocalizedMessage());
            }
        });
    }

    private void setupRecyclerView(List<Place> places) {
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        PlacesAdapter adapter = new PlacesAdapter(activity, places);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    private class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.ItemViewHolder> {

        Activity activity;
        List<Place> places;

        public PlacesAdapter(Activity activity, List<Place> places) {
            this.activity = activity;
            this.places = places;
        }

        @NonNull
        @Override
        public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ItemViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_place, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
            holder.provinceName.setText(places.get(position).getProvince());
            holder.attractionPlaceNumber.setText(places.get(position).getAttractionPlaceNumber());
            holder.enthusiastNumber.setText(places.get(position).getEnthusiastsNumber());
            if (!places.get(position).getImage().isEmpty()) {
                Picasso.get().load(ApiClient.TEAM_HIKER_URL + places.get(position).getImage()).into(holder.image);
            } else holder.image.setImageResource(R.drawable.slider_pic1);
            holder.constraintLayout.setOnClickListener(v -> {
                        Intent intent = new Intent(activity, PlaceDetailsActivity.class);
                        intent.putExtra("provinceName", places.get(position).getProvince());
                        intent.putExtra("attractionPlaceNumber", places.get(position).getAttractionPlaceNumber());
                        intent.putExtra("enthusiastNumber", places.get(position).getEnthusiastsNumber());
                        activity.startActivity(intent);
                    }
            );
            holder.btnLike.setOnClickListener(v -> {

            });
            holder.btnFilledLike.setOnClickListener(v -> {

            });
        }

        @Override
        public int getItemCount() {
            return places.size();
        }

        public class ItemViewHolder extends RecyclerView.ViewHolder {
            ConstraintLayout constraintLayout;
            ImageView image, btnLike, btnFilledLike;
            TextView provinceName, attractionPlaceNumber, enthusiastNumber;

            public ItemViewHolder(@NonNull View itemView) {
                super(itemView);
                constraintLayout = itemView.findViewById(R.id.placeItem_constraintLayout);
                image = itemView.findViewById(R.id.placeItem_image);
                provinceName = itemView.findViewById(R.id.placeItem_provinceName);
                attractionPlaceNumber = itemView.findViewById(R.id.placeItem_provinceAttractionsNumber);
                enthusiastNumber = itemView.findViewById(R.id.placeItem_enthusiastNumber);
                btnLike = itemView.findViewById(R.id.placeItem_btnLike);
                btnFilledLike = itemView.findViewById(R.id.placeItem_btnFilledLike);
            }
        }
    }

}