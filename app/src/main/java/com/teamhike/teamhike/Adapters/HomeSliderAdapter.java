package com.teamhike.teamhike.Adapters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.teamhike.teamhike.R;

import java.util.List;

public class HomeSliderAdapter extends RecyclerView.Adapter<HomeSliderAdapter.ItemViewHolder> {

    private Activity activity;
    private List<Bitmap> images;

    public HomeSliderAdapter(Activity activity, List<Bitmap> images) {
        this.activity = activity;
        this.images = images;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemViewHolder(LayoutInflater.from(activity).inflate(
                R.layout.slider_item,
                parent,
                false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Bitmap bitmap = images.get(position);
        holder.image.setImageBitmap (
                Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() / 5, bitmap.getHeight() / 5, true)
        );
        holder.image.setImageBitmap(images.get(position));
//        Picasso.get().load(ApiClient.BASE_URL + images.get(position)).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        private ImageView image;

        ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.slider_image);
        }
    }
}
