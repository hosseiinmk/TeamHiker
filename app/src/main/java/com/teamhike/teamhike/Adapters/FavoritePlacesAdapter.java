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

public class FavoritePlacesAdapter extends RecyclerView.Adapter<FavoritePlacesAdapter.ItemViewHolder> {

    Activity activity;
    private List<Place> favoritePlaces;

    public FavoritePlacesAdapter(Activity activity, List<Place> favoritePlaces) {
        this.activity = activity;
        this.favoritePlaces = favoritePlaces;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemViewHolder(LayoutInflater.from(activity).inflate(R.layout.item_profile_place, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
//        if (favoritePlaces != null) {
//            holder.deleteBtn.setVisibility(View.VISIBLE);
//            Picasso.get().load(ApiClient.TEAM_HIKER_URL + favoritePlaces.get(position).getImage()).into(holder.image);
//            holder.destination.setText(favoritePlaces.get(position).getDestination());
//            holder.enthusiastsNum.setText(favoritePlaces.get(position).getEnthusiastsNum());
//            holder.deleteBtn.setOnClickListener(v -> {
//                deletePlace(favoritePlaces.get(position).getDestination());
//            });
//        }
    }

    @Override
    public int getItemCount() {
        if (favoritePlaces != null) return favoritePlaces.size();
        else return 0;
    }

    public void updateFavoritePlacesData(List<Place> favoritePlaces) {
        this.favoritePlaces = favoritePlaces;
        notifyDataSetChanged();
    }

    private void deletePlace(String destination) {
        String userUniqueId = new DataBase(activity).getActiveLocalUser().getUniqueId();
        Call<Place> call = MainActivity.apiInterface.deletePlace(userUniqueId, destination);
        call.enqueue(new Callback<Place>() {
            @Override
            public void onResponse(@NonNull Call<Place> call, @NonNull Response<Place> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getResponse().equals("successfully")) {
                        Toast.makeText(activity, "مکان حذف شد", Toast.LENGTH_SHORT).show();
                        getFavoritePlaces();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Place> call, @NonNull Throwable t) {
                Log.e("DELETE_PLACE", t.getMessage());
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
                            updateFavoritePlacesData(response.body());
                        } else updateFavoritePlacesData(null);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Place>> call, @NonNull Throwable t) {
                Log.e("GET_FAV_PLACES", t.getLocalizedMessage());
            }
        });
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {

        CircleImageView image;
        TextView destination, enthusiastsNum;
        ImageView deleteBtn;

        ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.placeRecyclerView_image);
            destination = itemView.findViewById(R.id.placeRecyclerView_destination);
            enthusiastsNum = itemView.findViewById(R.id.placeRecyclerView_enthusiastsNum);
            deleteBtn = itemView.findViewById(R.id.placeRecyclerView_deleteBtn);
        }
    }
}
