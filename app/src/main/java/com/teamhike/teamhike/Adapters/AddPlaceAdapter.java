package com.teamhike.teamhike.Adapters;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.teamhike.teamhike.activities.MainActivity;
import com.teamhike.teamhike.Models.Place;
import com.teamhike.teamhike.R;
import com.teamhike.teamhike.SqliteDataBase.DataBase;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddPlaceAdapter extends RecyclerView.Adapter<AddPlaceAdapter.ItemViewHolder> {

    Activity activity;
    private List<Place> places;
    private List<Place> favoritePlaces;

    public AddPlaceAdapter(Activity activity, List<Place> places) {
        this.activity = activity;
        this.places = places;
        getFavoritePlaces();
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AddPlaceAdapter.ItemViewHolder(LayoutInflater.from(activity).inflate(R.layout.item_profile_place, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
//        if (!isExists(places.get(position).getDestination()))
//            holder.addBtn.setVisibility(View.VISIBLE);
//        else holder.addBtn.setVisibility(View.GONE);
//        Picasso.get().load(ApiClient.TEAM_HIKER_URL + places.get(position).getImage()).into(holder.image);
//        holder.destination.setText(places.get(position).getDestination());
//        holder.enthusiastsNum.setText(places.get(position).getEnthusiastsNum());
//        holder.addBtn.setOnClickListener(v -> addPlace(places.get(position).getDestination()));
    }

    @Override
    public int getItemCount() {
        return places.size();
    }

    private void addPlace(String destination) {
        String userUniqueId = new DataBase(activity).getActiveLocalUser().getUniqueId();
        MainActivity.apiInterface.addPlace(userUniqueId, destination).enqueue(new Callback<Place>() {
            @Override
            public void onResponse(@NonNull Call<Place> call, @NonNull Response<Place> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getResponse().equals("successfully")) {
                        Toast.makeText(activity, "مکان اضافه شد", Toast.LENGTH_SHORT).show();
                        getFavoritePlaces();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Place> call, @NonNull Throwable t) {
                Log.e("ADD_PLACE", t.getLocalizedMessage());
            }
        });
    }

    private void getFavoritePlaces() {
        String userUniqueId = new DataBase(activity).getActiveLocalUser().getUniqueId();
        MainActivity.apiInterface.getFavoritePlaces(userUniqueId).enqueue(new Callback<List<Place>>() {
            @Override
            public void onResponse(@NonNull Call<List<Place>> call, @NonNull Response<List<Place>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().get(0).getResponse() == null) {
                            updateFavoriteData(response.body());
//                            getAllPlaces();
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

    private void getAllPlaces() {
        MainActivity.apiInterface.getAllPlaces().enqueue(new Callback<List<Place>>() {
            @Override
            public void onResponse(@NonNull Call<List<Place>> call, @NonNull Response<List<Place>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    updatePlacesData(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Place>> call, @NonNull Throwable t) {
                Log.e("GET_ALL_PLACES", t.getLocalizedMessage());
            }
        });
    }

//    private boolean isExists(String destination) {
//        boolean exists = false;
//        if (favoritePlaces != null) {
//            for (Place favPlace : favoritePlaces) {
//                if (destination.equals(favPlace.getDestination())) {
//                    exists = true;
//                    break;
//                }
//            }
//        }
//        return exists;
//    }

    public void updatePlacesData(List<Place> places) {
        this.places = places;
        notifyDataSetChanged();
    }

    private void updateFavoriteData(List<Place> favoritePlaces) {
        this.favoritePlaces = favoritePlaces;
        notifyDataSetChanged();
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {

        CircleImageView image;
        TextView destination, enthusiastsNum;
        ImageView addBtn;

        ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.placeRecyclerView_image);
            destination = itemView.findViewById(R.id.placeRecyclerView_destination);
            enthusiastsNum = itemView.findViewById(R.id.placeRecyclerView_enthusiastsNum);
            addBtn = itemView.findViewById(R.id.placeRecyclerView_addBtn);
        }
    }
}
