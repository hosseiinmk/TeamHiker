package com.teamhike.teamhike.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.teamhike.teamhike.Models.Group;
import com.teamhike.teamhike.Models.GroupAttraction;
import com.teamhike.teamhike.Models.GroupInformation;
import com.teamhike.teamhike.Models.GroupName;
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

public class   CreateGroupChooseLeaderActivity extends AppCompatActivity {

    private ImageView backBtn;
    private TextView nextStepBtn;
    private RecyclerView recyclerView;
    private GroupChooseLeaderAdapter adapter;

    private List<String> attractions;
    private List<String> targetDestinationsNames;

    static String selectedLeaderUniqueId = "";

    private String groupUniqueId;
    private String provinceName;
    private String mapLongitude;
    private String mapLatitude;
    private String ratingBar;
    private String minimumMemberNumber;
    private String maximumMemberNumber;
    private String startTravelDateText;
    private String endTravelDateText;
    private String startDestinationText;
    private String endDestinationText;
    private String neededOnWayText;
    private String mealsText;
    private String moreNotesText;
    private String startTravelTimeText;
    private String endTravelTimeText;
    private String groupName;
    private String imagePath;
    private String encodedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group_choose_leader);
        getIntentExtras();
        setupViews();
        setupEvents();
        setupRecyclerView();
    }

    private void getIntentExtras() {
        attractions = new ArrayList<>();
        targetDestinationsNames = new ArrayList<>();
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
        minimumMemberNumber = intent.getStringExtra("minimumMemberNumber");
        maximumMemberNumber = intent.getStringExtra("maximumMemberNumber");
        startTravelDateText = intent.getStringExtra("startTravelDateText");
        endTravelDateText = intent.getStringExtra("endTravelDateText");
        startDestinationText = intent.getStringExtra("startDestinationText");
        endDestinationText = intent.getStringExtra("endDestinationText");
        startTravelTimeText = intent.getStringExtra("startTravelTimeText");
        endTravelTimeText = intent.getStringExtra("endTravelTimeText");
        targetDestinationsNames.add(intent.getStringExtra("targetDestinationsName1"));
        targetDestinationsNames.add(intent.getStringExtra("targetDestinationsName2"));
        targetDestinationsNames.add(intent.getStringExtra("targetDestinationsName3"));
        targetDestinationsNames.add(intent.getStringExtra("targetDestinationsName4"));
        targetDestinationsNames.add(intent.getStringExtra("targetDestinationsName5"));
        neededOnWayText = intent.getStringExtra("neededOnWayText");
        mealsText = intent.getStringExtra("mealsText");
        moreNotesText = intent.getStringExtra("moreNotesText");
        groupName = intent.getStringExtra("groupName");
        imagePath = intent.getStringExtra("imagePath");
        encodedImage = intent.getStringExtra("encodedImage");
    }

    private void setupViews() {
        backBtn = findViewById(R.id.createGroupChooseLeader_backBtn);
        nextStepBtn = findViewById(R.id.createGroupChooseLeader_nextStepBtn);
        recyclerView = findViewById(R.id.createGroupLeaderChoose_recyclerView);
    }

    private void setupEvents() {
        backBtn.setOnClickListener(v -> onBackPressed());
        nextStepBtn.setOnClickListener(v -> registerGroup());
    }

    private void setupRecyclerView() {
        MainActivity.apiInterface.getAllLeaders().enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(@NonNull Call<List<User>> call, @NonNull Response<List<User>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<User> leaders = response.body();
                    recyclerView.setLayoutManager(new LinearLayoutManager(CreateGroupChooseLeaderActivity.this));
                    adapter = new GroupChooseLeaderAdapter(leaders);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<User>> call, @NonNull Throwable t) {
                Log.e("Network", "Get All Leaders Failure: " + t.getLocalizedMessage());
            }
        });
    }

    private void registerGroup() {
        String userUniqueId = new DataBase(this).getActiveLocalUser().getUniqueId();
        MainActivity.apiInterface.registerGroup(
                groupUniqueId,
                userUniqueId,
                "",
                provinceName,
                mapLongitude,
                mapLatitude,
                ratingBar
        ).enqueue(new Callback<Group>() {
            @Override
            public void onResponse(@NonNull Call<Group> call, @NonNull Response<Group> response) {}

            @Override
            public void onFailure(@NonNull Call<Group> call, @NonNull Throwable t) {
                Log.e("Network", "Register Group Failure: " + t.getLocalizedMessage());
            }
        });
        MainActivity.apiInterface.registerGroupAttractions(
                groupUniqueId,
                attractions.get(0),
                attractions.get(1),
                attractions.get(2),
                attractions.get(3),
                attractions.get(4),
                attractions.get(5),
                attractions.get(6),
                attractions.get(7),
                attractions.get(8)
        ).enqueue(new Callback<GroupAttraction>() {
            @Override
            public void onResponse(@NonNull Call<GroupAttraction> call, @NonNull Response<GroupAttraction> response) {}

            @Override
            public void onFailure(@NonNull Call<GroupAttraction> call, @NonNull Throwable t) {
                Log.e("Network", "Register Group Attractions Failure: " + t.getLocalizedMessage());
            }
        });
        MainActivity.apiInterface.registerGroupInformation(
                groupUniqueId,
                minimumMemberNumber,
                maximumMemberNumber,
                startTravelDateText,
                endTravelDateText,
                startDestinationText,
                endDestinationText,
                startTravelTimeText,
                endTravelTimeText,
                targetDestinationsNames.get(0),
                targetDestinationsNames.get(1),
                targetDestinationsNames.get(2),
                targetDestinationsNames.get(3),
                targetDestinationsNames.get(4),
                neededOnWayText,
                mealsText,
                moreNotesText
        ).enqueue(new Callback<GroupInformation>() {
            @Override
            public void onResponse(@NonNull Call<GroupInformation> call, @NonNull Response<GroupInformation> response) {}

            @Override
            public void onFailure(@NonNull Call<GroupInformation> call, @NonNull Throwable t) {
                Log.e("Network", "Register Group Information Failure: " + t.getLocalizedMessage());
            }
        });
        MainActivity.apiInterface.registerGroupName(
                groupUniqueId,
                groupName,
                imagePath,
                encodedImage
        ).enqueue(new Callback<GroupName>() {
            @Override
            public void onResponse(@NonNull Call<GroupName> call, @NonNull Response<GroupName> response) {}

            @Override
            public void onFailure(@NonNull Call<GroupName> call, @NonNull Throwable t) {
                Log.e("Network", "Register Group Name Failure: " + t.getLocalizedMessage());
            }
        });
        MainActivity.apiInterface.updateGroupLeader(
                selectedLeaderUniqueId,
                groupUniqueId
        ).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    Intent intent = new Intent(CreateGroupChooseLeaderActivity.this, GroupChatActivity.class);
                    intent.putExtra("createGroupFinalStep", 1);
                    startActivity(intent);
                    selectedLeaderUniqueId = null;
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Log.e("Network", "Update Group Leader Failure: " + t.getLocalizedMessage());
            }
        });
    }

    private class GroupChooseLeaderAdapter extends RecyclerView.Adapter<GroupChooseLeaderAdapter.ItemViewHolder> {

        List<User> leaders;
        ItemViewHolder itemHolder;

        public GroupChooseLeaderAdapter(List<User> leaders) {
            this.leaders = leaders;
        }

        @NonNull
        @Override
        public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ItemViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.choose_group_leader_item, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
            if (!leaders.get(position).getImage().isEmpty())
                Picasso.get().load(ApiClient.TEAM_HIKER_URL + leaders.get(position).getImage()).into(holder.image);
            else holder.image.setImageResource(R.drawable.personal_info_photo);
            holder.username.setText(leaders.get(position).getUsername());
            holder.userPost.setText(leaders.get(position).getPost());
            holder.checkBtn.setOnClickListener(v -> {
                if (itemHolder != null)
                    itemHolder.checkBtn.setImageResource(R.drawable.ic_empty_check);
                itemHolder = holder;
                holder.checkBtn.setImageResource(R.drawable.ic_filled_check);
                CreateGroupChooseLeaderActivity.selectedLeaderUniqueId = leaders.get(position).getUniqueId();
            });
        }

        @Override
        public int getItemCount() {
            return leaders.size();
        }

        public class ItemViewHolder extends RecyclerView.ViewHolder {

            CircleImageView image;
            TextView username, userPost;
            ImageView checkBtn;

            public ItemViewHolder(@NonNull View itemView) {
                super(itemView);
                image = itemView.findViewById(R.id.createGroupChooseLeader_image);
                username = itemView.findViewById(R.id.createGroupChooseLeader_username);
                userPost = itemView.findViewById(R.id.createGroupChooseLeader_userPost);
                checkBtn = itemView.findViewById(R.id.createGroupChooseLeader_checkBtn);
            }
        }
    }
}