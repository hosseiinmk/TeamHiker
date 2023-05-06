package com.teamhike.teamhike.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.teamhike.teamhike.R;
import com.teamhike.teamhike.network.ApiClient;

import java.util.List;

public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.ItemViewHolder> {

    List<String> images;

    public SliderAdapter(List<String> images) {
        this.images = images;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemViewHolder(LayoutInflater.from(parent.getContext()).inflate (
                R.layout.slider_item, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Picasso.get().load(ApiClient.TEAM_HIKER_URL + images.get(position)).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {

        ImageView image;

        ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.slider_image);
        }
    }
}
