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
import com.teamhike.teamhike.Models.Blog;
import com.teamhike.teamhike.R;
import com.teamhike.teamhike.activities.PlaceDetailsAttractionPlaceDetailsActivity;
import com.teamhike.teamhike.network.ApiClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlaceDetailsAttractionPlacesFragment extends Fragment {

    private Activity activity;
    private RecyclerView recyclerView;
    private String placeProvinceName;

    public PlaceDetailsAttractionPlacesFragment(String placeProvinceName) {
        this.placeProvinceName = placeProvinceName;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_place_details_attraction_places, container, false);
        setupViews(view);
        setupEvents();
        getPlaceDetails();
        return view;
    }

    private void setupViews(View view) {
        recyclerView = view.findViewById(R.id.placeDetailsAttractionPlaces_recyclerView);
    }

    private void setupEvents() {

    }

    private void getPlaceDetails() {
        MainActivity.apiInterface.getPlaceDetailsByProvinceName(placeProvinceName).enqueue(new Callback<List<Blog>>() {
            @Override
            public void onResponse(@NonNull Call<List<Blog>> call, @NonNull Response<List<Blog>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Blog> placeDetails = response.body();
                    initializingRecyclerView(placeDetails);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Blog>> call, @NonNull Throwable t) {
                Log.e("Network", "Get Place Details Failure: " + t.getLocalizedMessage());
            }
        });
    }

    private void initializingRecyclerView(List<Blog> placeDetails) {
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        PlaceDetailsAttractionPlacesAdapter adapter = new PlaceDetailsAttractionPlacesAdapter(placeDetails);
        recyclerView.setAdapter(adapter);

    }

    private class PlaceDetailsAttractionPlacesAdapter extends RecyclerView.Adapter<PlaceDetailsAttractionPlacesAdapter.ItemViewHolder> {

        List<Blog> placeDetails;

        public PlaceDetailsAttractionPlacesAdapter(List<Blog> placeDetails) {
            this.placeDetails = placeDetails;
        }

        @NonNull
        @Override
        public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ItemViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_place_detail_attraction_places, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
            holder.location.setText(placeDetails.get(position).getDestination());
            if (!placeDetails.get(position).getImage().isEmpty()) {
                Picasso.get().load(ApiClient.TEAM_HIKER_URL + placeDetails.get(position).getImage()).into(holder.image);
            } else {
                holder.image.setImageResource(R.drawable.slider_pic1);
            }
            holder.description.setText(placeDetails.get(position).getDescription());
            holder.btnMore.setOnClickListener(v -> {
                Intent intent = new Intent(activity, PlaceDetailsAttractionPlaceDetailsActivity.class);
                intent.putExtra("image", placeDetails.get(position).getImage());
                intent.putExtra("description", placeDetails.get(position).getDescription());
                intent.putExtra("staticLocationImage", placeDetails.get(position).getStaticLocationImage());
                activity.startActivity(intent);
            });
        }

        @Override
        public int getItemCount() {
            return placeDetails.size();
        }

        public class ItemViewHolder extends RecyclerView.ViewHolder {

            TextView location, likesNumber, description, btnMore;
            ImageView image;

            public ItemViewHolder(@NonNull View itemView) {
                super(itemView);
                location = itemView.findViewById(R.id.placeDetailsAttractionPlacesItem_location);
                image = itemView.findViewById(R.id.placeDetailsAttractionPlacesItem_image);
                likesNumber = itemView.findViewById(R.id.placeDetailsAttractionPlacesItem_likesNumber);
                description = itemView.findViewById(R.id.placeDetailsAttractionPlacesItem_description);
                btnMore = itemView.findViewById(R.id.placeDetailsAttractionPlacesItem_btnMore);
            }
        }
    }
}
