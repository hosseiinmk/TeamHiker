package com.teamhike.teamhike.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.teamhike.teamhike.Models.User;
import com.teamhike.teamhike.R;
import com.teamhike.teamhike.activities.MainActivity;
import com.teamhike.teamhike.network.ApiClient;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PublicProfileFriendsFragment extends Fragment {

    private Activity activity;
    private User user;
    private View view;
    private RecyclerView recyclerView;

    public PublicProfileFriendsFragment(User user) {
        this.user = user;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_public_profle_friends, container, false);
        setupViews();
        showFriends();
        return view;
    }

    private void setupViews() {
        recyclerView = view.findViewById(R.id.publicProfileFriends_recyclerView);
    }

    private void showFriends() {
        MainActivity.apiInterface.getFriends(user.getUniqueId()).enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(@NonNull Call<List<User>> call, @NonNull Response<List<User>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().get(0).getResponse() == null) {
                        List<User> friends = response.body();
                        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
                        recyclerView.setAdapter(new PublicFriendsAdapter(activity, friends));
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<User>> call, @NonNull Throwable t) {
                Log.e("Network", "Get Friends Failure: " + t.getLocalizedMessage());
            }
        });
    }

    private class PublicFriendsAdapter extends RecyclerView.Adapter<PublicFriendsAdapter.ItemViewHolder> {

        Activity activity;
        List<User> users;

        public PublicFriendsAdapter(Activity activity, List<User> users) {
            this.activity = activity;
            this.users = users;
        }

        @NonNull
        @Override
        public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ItemViewHolder(LayoutInflater.from(activity).inflate(R.layout.item_friend, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
            if (!users.get(position).getImage().isEmpty())
                Picasso.get().load(ApiClient.TEAM_HIKER_URL + users.get(position).getImage()).into(holder.image);
            else holder.image.setImageResource(R.drawable.personal_info_photo);
            holder.username.setText(users.get(position).getUsername());
            holder.userPost.setText(users.get(position).getPost());
        }

        @Override
        public int getItemCount() {
            return users.size();
        }

        class ItemViewHolder extends RecyclerView.ViewHolder {

            CircleImageView image;
            TextView username, userPost;

            ItemViewHolder(@NonNull View itemView) {
                super(itemView);
                image = itemView.findViewById(R.id.friendRecyclerView_image);
                username = itemView.findViewById(R.id.friendRecyclerView_username);
                userPost = itemView.findViewById(R.id.friendRecyclerView_post);
            }
        }
    }
}
