package com.teamhike.teamhike.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.teamhike.teamhike.Models.Group;
import com.teamhike.teamhike.Models.GroupInformation;
import com.teamhike.teamhike.Models.GroupName;
import com.teamhike.teamhike.R;
import com.teamhike.teamhike.network.ApiClient;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminPanelActivity extends AppCompatActivity implements View.OnClickListener {

    private Button checkGroupsBtn;
    private Button addBlogBtn;
    private List<Group> allPendingGroups;
    private List<GroupInformation> allPendingGroupsInformation;
    private List<GroupName> allPendingGroupsNames;

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);
        setupViews();
        setupEvents();
        allPendingGroupsInformation = new ArrayList<>();
        allPendingGroupsNames = new ArrayList<>();
        getAllPendingGroups();
    }

    private void setupViews() {
        recyclerView = findViewById(R.id.adminPanel_recyclerView);
        checkGroupsBtn = findViewById(R.id.adminPanel_checkGroupsBtn);
        addBlogBtn = findViewById(R.id.adminPanel_addBlogBtn);
    }

    private void setupEvents() {
        checkGroupsBtn.setOnClickListener(this);
        addBlogBtn.setOnClickListener(this);
    }

    private void getAllPendingGroups() {
        MainActivity.apiInterface.getAllPendingGroups().enqueue(new Callback<List<Group>>() {
            @Override
            public void onResponse(@NonNull Call<List<Group>> call, @NonNull Response<List<Group>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().get(0).getResponse() == null) {
                        allPendingGroups = response.body();
                        getGroupsInformation();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Group>> call, @NonNull Throwable t) {
                Log.e("Network", "Get All Pending Groups Failure: " + t.getLocalizedMessage());
            }
        });
    }

    private void getGroupsInformation() {
        for (int i = 0; i < allPendingGroups.size(); i++) {
            Log.d("Debug", "allPendingGroupsUniqueId: " + allPendingGroups.get(i).getGroupUniqueId());
            MainActivity.apiInterface.getGroupInformation(allPendingGroups.get(i).getGroupUniqueId()).enqueue(new Callback<GroupInformation>() {
                @Override
                public void onResponse(@NonNull Call<GroupInformation> call, @NonNull Response<GroupInformation> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        if (response.body().getResponse() == null) {
                            allPendingGroupsInformation.add(response.body());
                            Log.d("Debug", "allPendingGroupsInformation: " + allPendingGroupsInformation.size());
                            getGroupsName(response.body().getGroupUniqueId());
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<GroupInformation> call, @NonNull Throwable t) {
                    Log.e("Network", "Get Group Information Failure: " + t.getLocalizedMessage());
                }
            });
        }
    }

    private void getGroupsName(String groupUniqueId) {
        Log.d("Debug", "groupUniqueId: " + groupUniqueId);
        MainActivity.apiInterface.getGroupName(groupUniqueId).enqueue(new Callback<GroupName>() {
            @Override
            public void onResponse(@NonNull Call<GroupName> call, @NonNull Response<GroupName> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getResponse() == null) {
                        allPendingGroupsNames.add(response.body());
                        Log.d("Debug", "allPendingGroupsNames: " + allPendingGroupsNames.size());
                        if (allPendingGroupsNames.size() == allPendingGroups.size()) {
                            setupRecyclerView();
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<GroupName> call, @NonNull Throwable t) {
                Log.e("Network", "Get Group Name Failure: " + t.getLocalizedMessage());
            }
        });
    }

    private void setupRecyclerView() {
        if (allPendingGroups != null && allPendingGroupsInformation != null && allPendingGroupsNames != null) {
//            Log.d("Debug", "Set Recycler View is ready");
//            Log.d("Debug", "allPendingGroupsSize: " + allPendingGroups.size());
//            Log.d("Debug", "allPendingGroupsInformation: " + allPendingGroupsInformation.size());
//            Log.d("Debug", "allPendingGroupsNames: " + allPendingGroupsNames.size());
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            PendGroupAdapter adapter = new PendGroupAdapter(this, allPendingGroups, allPendingGroupsInformation, allPendingGroupsNames);
            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == R.id.adminPanel_addBlogBtn) {
            startActivity(new Intent(this, AddBlogActivity.class));
        }
    }

    private class PendGroupAdapter extends RecyclerView.Adapter<PendGroupAdapter.ItemViewHolder> {

        Activity activity;
        List<Group> groups;
        List<GroupInformation> groupInformation;
        List<GroupName> groupNames;

        public PendGroupAdapter(Activity activity, List<Group> groups, List<GroupInformation> groupInformation, List<GroupName> groupNames) {
            this.activity = activity;
            this.groups = groups;
            this.groupInformation = groupInformation;
            this.groupNames = groupNames;
        }

        @NonNull
        @Override
        public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pend_group, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
//            holder.mainLayout.setOnClickListener(v -> activity.startActivity(new Intent(activity, GroupDetailActivity.class)));
            if (!groupNames.get(position).getGroupImage().isEmpty()) {
                Picasso.get().load(ApiClient.TEAM_HIKER_URL + groupNames.get(position).getGroupImage()).into(holder.image);
            } else holder.image.setImageResource(R.drawable.personal_info_photo);
            holder.destination.setText(groups.get(position).getDestinationProvince());
            holder.travelStartTime.setText(groupInformation.get(position).getStartDate());
            holder.travelFinishTime.setText(groupInformation.get(position).getFinishDate());
            holder.acceptBtn.setOnClickListener(v -> {
                int situation = 1;
                holder.setGroupSituation(groups.get(position).getGroupUniqueId(), situation);
                groups.remove(position);
                groupInformation.remove(position);
                groupNames.remove(position);
                notifyDataSetChanged();
            });
            holder.rejectBtn.setOnClickListener(v -> {
                int situation = -1;
                holder.setGroupSituation(groups.get(position).getGroupUniqueId(), situation);
                groups.remove(position);
                groupInformation.remove(position);
                groupNames.remove(position);
                notifyDataSetChanged();
            });
        }

        @Override
        public int getItemCount() {
            return groups.size();
        }

        class ItemViewHolder extends RecyclerView.ViewHolder {

            LinearLayout mainLayout;
            TextView destination, travelStartTime, travelFinishTime;
            CircleImageView image;
            Button acceptBtn, rejectBtn;

            public ItemViewHolder(@NonNull View itemView) {
                super(itemView);
                mainLayout = itemView.findViewById(R.id.pendGroupItem_mainLayout);
                image = itemView.findViewById(R.id.pendGroupItem_image);
                destination = itemView.findViewById(R.id.pendGroupItem_destination);
                travelStartTime = itemView.findViewById(R.id.pendGroupItem_travelStartTime);
                travelFinishTime = itemView.findViewById(R.id.pendGroupItem_travelFinishTime);
                acceptBtn = itemView.findViewById(R.id.pendGroupItem_acceptBtn);
                rejectBtn = itemView.findViewById(R.id.pendGroupItem_rejectBtn);
            }

            public void setGroupSituation(String groupUniqueId ,int situation) {
                MainActivity.apiInterface.setGroupSituation(groupUniqueId, situation).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {

                    }

                    @Override
                    public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {

                    }
                });
            }
        }
    }
}