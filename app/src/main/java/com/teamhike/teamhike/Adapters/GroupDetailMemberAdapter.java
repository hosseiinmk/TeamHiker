package com.teamhike.teamhike.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.teamhike.teamhike.Models.User;
import com.teamhike.teamhike.R;
import com.teamhike.teamhike.network.ApiClient;

import java.util.List;

public class GroupDetailMemberAdapter extends RecyclerView.Adapter<GroupDetailMemberAdapter.ItemViewHolder> {

    private List<User> users;

    public GroupDetailMemberAdapter(List<User> users) {
        this.users = users;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        if (!users.get(position).getImage().isEmpty())
            Picasso.get().load(ApiClient.TEAM_HIKER_URL + users.get(position).getImage()).into(holder.image);
        else holder.image.setImageResource(R.drawable.personal_info_photo);
        holder.username.setText(users.get(position).getUsername());
        holder.post.setText(users.get(position).getPost());
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        public ImageView image;
        public TextView username, post;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.friendRecyclerView_image);
            username = itemView.findViewById(R.id.friendRecyclerView_username);
            post = itemView.findViewById(R.id.friendRecyclerView_post);
        }
    }
}
