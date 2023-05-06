package com.teamhike.teamhike.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.teamhike.teamhike.Models.Place;
import com.teamhike.teamhike.R;
import com.teamhike.teamhike.network.ApiClient;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfilePlacesAdapter extends RecyclerView.Adapter<ProfilePlacesAdapter.ItemViewHolder> {

    Activity activity;
    List<Place> places;

    public ProfilePlacesAdapter(Activity activity, List<Place> places) {
        this.activity = activity;
        this.places = places;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemViewHolder(LayoutInflater.from(activity).inflate(R.layout.profile_place_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
//        Picasso.get().load(ApiClient.TEAM_HIKER_URL + places.get(position).getImage()).into(holder.image);
//        holder.destination.setText(places.get(position).getDestination());
    }

    @Override
    public int getItemCount() {
        if (places != null) return places.size();
        else return 0;
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {

        CircleImageView image;
        TextView destination;

        ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.profilePlaceRecyclerView_image);
            destination = itemView.findViewById(R.id.profilePlaceRecyclerView_destination);
        }
    }
}
