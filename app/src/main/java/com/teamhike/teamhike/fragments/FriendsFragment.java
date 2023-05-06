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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.teamhike.teamhike.Models.User;
import com.teamhike.teamhike.R;
import com.teamhike.teamhike.SqliteDataBase.DataBase;
import com.teamhike.teamhike.activities.AddFriendActivity;
import com.teamhike.teamhike.activities.MainActivity;
import com.teamhike.teamhike.activities.PublicProfileActivity;
import com.teamhike.teamhike.network.ApiClient;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FriendsFragment extends Fragment {

    private Activity activity;
    private View view;

    private ImageView addBtn;
    private RecyclerView recyclerView;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_friends, container, false);
        setupViews();
        setupEvents();
        showFriends();
        return view;
    }

    private void setupViews() {
        recyclerView = view.findViewById(R.id.friends_recyclerView);
        addBtn = view.findViewById(R.id.friends_addBtn);
    }

    private void setupEvents() {
        addBtn.setOnClickListener(v -> activity.startActivity(new Intent(activity, AddFriendActivity.class)));
    }

    private void showFriends() {
        String activeUserUniqueId = new DataBase(activity).getActiveLocalUser().getUniqueId();
        MainActivity.apiInterface.getFriends(activeUserUniqueId).enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(@NonNull Call<List<User>> call, @NonNull Response<List<User>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().get(0).getResponse() == null) {
                        List<User> friends = response.body();
                        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
                        recyclerView.setAdapter(new FriendsAdapter(activity, friends, activeUserUniqueId));
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<User>> call, @NonNull Throwable t) {
                Log.e("Network", "Get Friends Failure: " + t.getLocalizedMessage());
            }
        });
    }

    private class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.ItemViewHolder> {

        Context context;
        List<User> friends;
        String userUniqueId;

        public FriendsAdapter(Context context, List<User> friends, String userUniqueId) {
            this.context = context;
            this.friends = friends;
            this.userUniqueId = userUniqueId;
        }

        @NonNull
        @Override
        public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
            if (!friends.get(position).getImage().isEmpty())
                Picasso.get().load(ApiClient.TEAM_HIKER_URL + friends.get(position).getImage()).into(holder.image);
            else holder.image.setImageResource(R.drawable.personal_info_photo);
            holder.username.setText(friends.get(position).getUsername());
            holder.userPost.setText(friends.get(position).getPost());
            holder.image.setOnClickListener(v -> {
                Intent intent = new Intent(context, PublicProfileActivity.class);
                intent.putExtra("userUniqueId", friends.get(position).getUniqueId());
                intent.putExtra("imageURL", friends.get(position).getImage());
                intent.putExtra("username", friends.get(position).getUsername());
                intent.putExtra("rank", friends.get(position).getPost());
                startActivity(intent);
            });
            holder.deleteBtn.setOnClickListener(v -> deleteFriend(position));
        }

        @Override
        public int getItemCount() {
            if (friends == null) {
                return 0;
            } else {
                return friends.size();
            }
        }

        private void deleteFriend(int position) {
            MainActivity.apiInterface.deleteFriend(userUniqueId, friends.get(position).getUniqueId()).enqueue(new Callback<User>() {
                @Override
                public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        if (response.body().getResponse().equals("successfully")) {
                            Toast.makeText(activity, "کاربر حذف شد", Toast.LENGTH_SHORT).show();
                            updateFriends();
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                    Log.e("Network", "Delete Friend Failure: " + t.getLocalizedMessage());
                }
            });
        }

        private void updateFriends() {
            MainActivity.apiInterface.getFriends(userUniqueId).enqueue(new Callback<List<User>>() {
                @Override
                public void onResponse(@NonNull Call<List<User>> call, @NonNull Response<List<User>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        if (response.body().get(0).getResponse() == null) {
                            friends = response.body();
                        } else {
                            friends = null;
                        }
                        notifyDataSetChanged();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<List<User>> call, @NonNull Throwable t) {
                    Log.e("Network", "Get Friend Failure: " + t.getLocalizedMessage());
                }
            });
        }

        class ItemViewHolder extends RecyclerView.ViewHolder {

            CircleImageView image;
            TextView username, userPost;
            ImageView deleteBtn;

            ItemViewHolder(@NonNull View itemView) {
                super(itemView);
                image = itemView.findViewById(R.id.friendRecyclerView_image);
                username = itemView.findViewById(R.id.friendRecyclerView_username);
                userPost = itemView.findViewById(R.id.friendRecyclerView_post);
                deleteBtn = itemView.findViewById(R.id.friendRecyclerView_deleteBtn);
                deleteBtn.setVisibility(View.VISIBLE);
            }
        }
    }
}
