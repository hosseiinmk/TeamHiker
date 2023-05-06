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
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.teamhike.teamhike.activities.MainActivity;
import com.teamhike.teamhike.Models.Group;
import com.teamhike.teamhike.Models.GroupInformation;
import com.teamhike.teamhike.Models.GroupName;
import com.teamhike.teamhike.R;
import com.teamhike.teamhike.activities.CreateGroupChooseDestinationActivity;
import com.teamhike.teamhike.network.ApiClient;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GroupsFragment extends Fragment {

    private Activity activity;
    private View view;
    private ImageView addBtn;
    private RecyclerView recyclerView;
    private List<Group> allAcceptedGroups;
    private List<GroupInformation> allAcceptedGroupsInformation;
    private List<GroupName> allAcceptedGroupsNames;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_groups, container, false);
        setupViews();
        setupEvents();
        allAcceptedGroupsInformation = new ArrayList<>();
        allAcceptedGroupsNames = new ArrayList<>();
        getAllAcceptedGroups();
        return view;
    }

    private void setupViews() {
        addBtn = view.findViewById(R.id.groupsFragment_addBtn);
        recyclerView = view.findViewById(R.id.groupsFragment_recyclerView);
    }

    private void setupEvents() {
        addBtn.setOnClickListener(v ->
                startActivity(new Intent(activity, CreateGroupChooseDestinationActivity.class)));
    }

    private void getAllAcceptedGroups() {
        MainActivity.apiInterface.getAllAcceptedGroups().enqueue(new Callback<List<Group>>() {
            @Override
            public void onResponse(@NonNull Call<List<Group>> call, @NonNull Response<List<Group>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().get(0).getResponse() == null) {
                        allAcceptedGroups = response.body();
                        getGroupsInformation();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Group>> call, @NonNull Throwable t) {
                Log.e("Network", "Get All Accepted Groups Failure: " + t.getLocalizedMessage());
            }
        });
    }

    private void getGroupsInformation() {
        for (int i = 0; i < allAcceptedGroups.size(); i++) {
            MainActivity.apiInterface.getGroupInformation(allAcceptedGroups.get(i).getGroupUniqueId()).enqueue(new Callback<GroupInformation>() {
                @Override
                public void onResponse(@NonNull Call<GroupInformation> call, @NonNull Response<GroupInformation> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        if (response.body().getResponse() == null) {
                            allAcceptedGroupsInformation.add(response.body());
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
        MainActivity.apiInterface.getGroupName(groupUniqueId).enqueue(new Callback<GroupName>() {
            @Override
            public void onResponse(@NonNull Call<GroupName> call, @NonNull Response<GroupName> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getResponse() == null) {
                        allAcceptedGroupsNames.add(response.body());
                        if (allAcceptedGroupsNames.size() == allAcceptedGroups.size()) {
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
        if (allAcceptedGroups != null && allAcceptedGroupsInformation != null && allAcceptedGroupsNames != null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(activity));
            AcceptedGroupsAdapter adapter = new AcceptedGroupsAdapter(activity, allAcceptedGroups, allAcceptedGroupsInformation, allAcceptedGroupsNames);
            recyclerView.setAdapter(adapter);
        }
    }

    class AcceptedGroupsAdapter extends RecyclerView.Adapter<AcceptedGroupsAdapter.ItemViewHolder> {

        Activity activity;
        List<Group> groups;
        List<GroupInformation> groupInformation;
        List<GroupName> groupNames;

        public AcceptedGroupsAdapter(Activity activity, List<Group> groups, List<GroupInformation> groupInformation, List<GroupName> groupNames) {
            this.activity = activity;
            this.groups = groups;
            this.groupInformation = groupInformation;
            this.groupNames = groupNames;
        }

        @NonNull
        @Override
        public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ItemViewHolder(
                    LayoutInflater.from(parent.getContext()).inflate(
                            R.layout.item_accepted_group, parent, false
                    )
            );
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
            holder.membersNumbers.setText("0");
            holder.registerPrice.setText("رایگان");
        }

        @Override
        public int getItemCount() {
            return groups.size();
        }

        public class ItemViewHolder extends RecyclerView.ViewHolder {

            LinearLayout mainLayout;
            TextView destination, travelStartTime, travelFinishTime, membersNumbers, registerPrice;
            CircleImageView image;
            ImageView favoriteBtn;

            public ItemViewHolder(@NonNull View itemView) {
                super(itemView);
                mainLayout = itemView.findViewById(R.id.acceptedGroupItem_mainLayout);
                image = itemView.findViewById(R.id.acceptedGroupItem_image);
                destination = itemView.findViewById(R.id.acceptedGroupItem_destination);
                travelStartTime = itemView.findViewById(R.id.acceptedGroupItem_travelStartTime);
                travelFinishTime = itemView.findViewById(R.id.acceptedGroupItem_travelFinishTime);
                membersNumbers = itemView.findViewById(R.id.acceptedGroupItem_membersNumber);
                registerPrice = itemView.findViewById(R.id.acceptedGroupItem_registerPrice);
                favoriteBtn = itemView.findViewById(R.id.acceptedGroupItem_favoriteBtn);
            }
        }
    }
}
