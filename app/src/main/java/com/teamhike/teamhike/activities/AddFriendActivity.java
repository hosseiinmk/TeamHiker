package com.teamhike.teamhike.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.teamhike.teamhike.Models.User;
import com.teamhike.teamhike.R;
import com.teamhike.teamhike.SqliteDataBase.DataBase;
import com.teamhike.teamhike.network.ApiClient;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddFriendActivity extends AppCompatActivity {

    private List<User> allUsersExceptActiveUser;

    private EditText searchBox;
    private RecyclerView recyclerView;
    private AddFriendsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        setupViews();
        setupEvents();
        showUsers();
    }

    private void setupViews() {
        allUsersExceptActiveUser = new ArrayList<>();
        recyclerView = findViewById(R.id.addFriend_recyclerView);
        searchBox = findViewById(R.id.addFriend_searchBox);
    }

    private void setupEvents() {
        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().isEmpty()) searchOnUsers(s.toString());
                else adapter.filterUsers(allUsersExceptActiveUser);
            }
        });
    }

    private void showUsers() {
        String activeUserUniqueId = new DataBase(this).getActiveLocalUser().getUniqueId();
        MainActivity.apiInterface.getAllUsers().enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(@NonNull Call<List<User>> call, @NonNull Response<List<User>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().get(0).getResponse() == null) {
                        List<User> allUsers = response.body();
                        for (User user : allUsers) {
                            if (!user.getUniqueId().equals(activeUserUniqueId)) {
                                allUsersExceptActiveUser.add(user);
                            }
                        }
                        recyclerView.setLayoutManager(new LinearLayoutManager(AddFriendActivity.this));
                        adapter = new AddFriendsAdapter(AddFriendActivity.this, allUsersExceptActiveUser);
                        recyclerView.setAdapter(adapter);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<User>> call, @NonNull Throwable t) {
                Log.e("Network", "Get All Users Failure: " + t.getLocalizedMessage());
            }
        });
    }

    private void searchOnUsers(String targetUsername) {
        List<User> matchUsers = new ArrayList<>();
        for (User user : allUsersExceptActiveUser) {
            if (user.getUsername().toLowerCase().contains(targetUsername)) {
                matchUsers.add(user);
            }
        }
        adapter.filterUsers(matchUsers);
    }

    private class AddFriendsAdapter extends RecyclerView.Adapter<AddFriendsAdapter.ItemViewHolder> {

        Context context;
        List<User> users;

        public AddFriendsAdapter(Context context, List<User> users) {
            this.context = context;
            this.users = users;
        }

        @NonNull
        @Override
        public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
            if (!users.get(position).getImage().isEmpty())
                Picasso.get().load(ApiClient.TEAM_HIKER_URL + users.get(position).getImage()).into(holder.image);
            else holder.image.setImageResource(R.drawable.personal_info_photo);
            holder.username.setText(users.get(position).getUsername());
            holder.userPost.setText(users.get(position).getPost());
            holder.mainLayout.setOnClickListener(v -> {
                Intent intent = new Intent(context, PublicProfileActivity.class);
                intent.putExtra("userUniqueId", users.get(position).getUniqueId());
                intent.putExtra("imageURL", users.get(position).getImage());
                intent.putExtra("username", users.get(position).getUsername());
                intent.putExtra("rank", users.get(position).getPost());
                startActivity(intent);
            });
        }

        @Override
        public int getItemCount() {
            return users.size();
        }

        public void filterUsers(List<User> users) {
            this.users = users;
            notifyDataSetChanged();
        }

        private class ItemViewHolder extends RecyclerView.ViewHolder {

            CircleImageView image;
            TextView username, userPost;
            ConstraintLayout mainLayout;

            ItemViewHolder(@NonNull View itemView) {
                super(itemView);
                image = itemView.findViewById(R.id.friendRecyclerView_image);
                username = itemView.findViewById(R.id.friendRecyclerView_username);
                userPost = itemView.findViewById(R.id.friendRecyclerView_post);
                mainLayout = itemView.findViewById(R.id.friendRecyclerView_mainLayout);
            }
        }
    }
}