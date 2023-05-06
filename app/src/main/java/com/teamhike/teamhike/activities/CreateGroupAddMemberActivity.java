package com.teamhike.teamhike.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.teamhike.teamhike.Models.GroupMember;
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

public class CreateGroupAddMemberActivity extends AppCompatActivity {

    private List<User> friends;

    private ImageView backBtn;
    private TextView nextStepBtn;
    private RecyclerView recyclerView;
    private EditText minimumMemberNumber, maximumMemberNumber, searchBox;

    private String groupUniqueId;
    private String provinceName;
    private String mapLongitude;
    private String mapLatitude;
    private String ratingBar;

    private List<String> attractions;

    private FriendsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group_add_member);
        getIntentExtras();
        setupViews();
        setupEvents();
        showFriends();
    }

    private void getIntentExtras() {
        attractions = new ArrayList<>();
        Intent intent = getIntent();
        groupUniqueId = intent.getStringExtra("groupUniqueId");
        provinceName = intent.getStringExtra("provinceName");
        mapLongitude = intent.getStringExtra("mapLongitude");
        mapLatitude = intent.getStringExtra("mapLatitude");
        attractions.add(intent.getStringExtra("attraction1"));
        attractions.add(intent.getStringExtra("attraction2"));
        attractions.add(intent.getStringExtra("attraction3"));
        attractions.add(intent.getStringExtra("attraction4"));
        attractions.add(intent.getStringExtra("attraction5"));
        attractions.add(intent.getStringExtra("attraction6"));
        attractions.add(intent.getStringExtra("attraction7"));
        attractions.add(intent.getStringExtra("attraction8"));
        attractions.add(intent.getStringExtra("attraction9"));
        ratingBar = intent.getStringExtra("ratingBar");
    }

    private void setupViews() {
        backBtn = findViewById(R.id.createGroupAddMember_backBtn);
        nextStepBtn = findViewById(R.id.createGroupAddMember_nextStepBtn);
        recyclerView = findViewById(R.id.createGroupAddMember_recyclerView);
        minimumMemberNumber = findViewById(R.id.createGroupAddMember_minimumMemberNumber);
        maximumMemberNumber = findViewById(R.id.createGroupAddMember_maximumMemberNumber);
        searchBox = findViewById(R.id.createGroupAddMember_searchBox);
    }

    private void setupEvents() {
        backBtn.setOnClickListener(v -> onBackPressed());
        nextStepBtn.setOnClickListener(v -> {
            if (!minimumMemberNumber.getText().toString().isEmpty() && !maximumMemberNumber.getText().toString().isEmpty()) {
                Intent intent = new Intent(this, CreateGroupDescriptionActivity.class);
                intent.putExtra("groupUniqueId", groupUniqueId);
                intent.putExtra("provinceName", provinceName);
                intent.putExtra("mapLongitude", mapLongitude);
                intent.putExtra("mapLatitude", mapLatitude);
                intent.putExtra("attraction1", attractions.get(0));
                intent.putExtra("attraction2", attractions.get(1));
                intent.putExtra("attraction3", attractions.get(2));
                intent.putExtra("attraction4", attractions.get(3));
                intent.putExtra("attraction5", attractions.get(4));
                intent.putExtra("attraction6", attractions.get(5));
                intent.putExtra("attraction7", attractions.get(6));
                intent.putExtra("attraction8", attractions.get(7));
                intent.putExtra("attraction9", attractions.get(8));
                intent.putExtra("ratingBar", ratingBar);
                intent.putExtra("minimumMemberNumber", minimumMemberNumber.getText().toString());
                intent.putExtra("maximumMemberNumber", maximumMemberNumber.getText().toString());
                startActivity(intent);
            } else {
                Toast.makeText(this, "لطفا ظرفیت گروه را مشخص کنید", Toast.LENGTH_SHORT).show();
            }
        });
        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (friends != null) {
                    if (!editable.toString().isEmpty()) searchOnUsers(editable.toString());
                    else adapter.updateFriends(friends);
                }
            }
        });
    }

    private void showFriends() {
        String activeUserUniqueId = new DataBase(this).getActiveLocalUser().getUniqueId();
        MainActivity.apiInterface.getFriends(activeUserUniqueId).enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(@NonNull Call<List<User>> call, @NonNull Response<List<User>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().get(0).getResponse() == null) {
                        friends = response.body();
                        recyclerView.setLayoutManager(new LinearLayoutManager(CreateGroupAddMemberActivity.this));
                        adapter = new FriendsAdapter(CreateGroupAddMemberActivity.this, friends, groupUniqueId);
                        recyclerView.setAdapter(adapter);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<User>> call, @NonNull Throwable t) {
                Log.e("Network", "Get Friends Failure: " + t.getLocalizedMessage());
            }
        });
    }

    private void searchOnUsers(String targetUsername) {
        List<User> matchUsers = new ArrayList<>();
        for (User user : friends) {
            if (user.getUsername().toLowerCase().contains(targetUsername)) {
                matchUsers.add(user);
            }
        }
        adapter.updateFriends(matchUsers);
    }

    private class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.ItemViewHolder> {

        Activity activity;
        List<User> friends;
        String groupUniqueId;

        public FriendsAdapter(Activity activity, List<User> friends, String groupUniqueId) {
            this.activity = activity;
            this.friends = friends;
            this.groupUniqueId = groupUniqueId;
        }

        @NonNull
        @Override
        public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_add_friend, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
            if (!friends.get(position).getImage().isEmpty())
                Picasso.get().load(ApiClient.TEAM_HIKER_URL + friends.get(position).getImage()).into(holder.image);
            else holder.image.setImageResource(R.drawable.personal_info_photo);
            holder.username.setText(friends.get(position).getUsername());
            holder.userPost.setText(friends.get(position).getPost());
            holder.image.setOnClickListener(v -> {

            });
            holder.addBtn.setOnClickListener(v -> {
                holder.addGroupMember(position);
                holder.addBtn.setVisibility(View.GONE);
            });
        }

        @Override
        public int getItemCount() {
            return friends.size();
        }

        public void updateFriends(List<User> friends) {
            this.friends = friends;
            notifyDataSetChanged();
        }

        public class ItemViewHolder extends RecyclerView.ViewHolder {

            CircleImageView image;
            TextView username, userPost;
            ImageView addBtn;

            public ItemViewHolder(@NonNull View itemView) {
                super(itemView);
                image = itemView.findViewById(R.id.addFriendItem_image);
                username = itemView.findViewById(R.id.addFriendItem_username);
                userPost = itemView.findViewById(R.id.addFriendItem_userPost);
                addBtn = itemView.findViewById(R.id.addFriendItem_addBtn);
            }

            public void addGroupMember(int position) {
                MainActivity.apiInterface.addGroupMember(groupUniqueId, friends.get(position).getUniqueId()).enqueue(new Callback<GroupMember>() {
                    @Override
                    public void onResponse(@NonNull Call<GroupMember> call, @NonNull Response<GroupMember> response) {}

                    @Override
                    public void onFailure(@NonNull Call<GroupMember> call, @NonNull Throwable t) {
                        Log.e("Network", "Add Group Member Failure: " + t.getLocalizedMessage());
                    }
                });
            }
        }
    }
}