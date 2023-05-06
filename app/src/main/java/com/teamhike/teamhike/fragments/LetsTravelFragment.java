package com.teamhike.teamhike.fragments;

import android.app.Activity;
import android.content.Context;
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
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.teamhike.teamhike.CustomClasses.ReplaceFragment;
import com.teamhike.teamhike.CustomClasses.SharedPreferences;
import com.teamhike.teamhike.activities.MainActivity;
import com.teamhike.teamhike.Models.Group;
import com.teamhike.teamhike.Models.GroupInformation;
import com.teamhike.teamhike.Models.GroupName;
import com.teamhike.teamhike.R;
import com.teamhike.teamhike.SqliteDataBase.DataBase;
import com.teamhike.teamhike.network.ApiClient;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LetsTravelFragment extends Fragment implements View.OnClickListener {

    private Activity activity;
    private RecyclerView recyclerView;
    private ImageView addBtn;
    private FragmentManager fragmentManager;
    private List<Group> allGroups;
    private List<GroupInformation> allGroupsInformation;
    private List<GroupName> allGroupsNames;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = getActivity();
        fragmentManager = getFragmentManager();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lets_travel, container, false);
        setupViews(view);
        setupEvents();
        allGroupsInformation = new ArrayList<>();
        allGroupsNames = new ArrayList<>();
        getAllGroups();
        return view;
    }

    private void setupViews(View view) {
        recyclerView = view.findViewById(R.id.fragmentLetsTravel_recyclerView);
        addBtn = view.findViewById(R.id.fragmentLetsTravel_addBtn);
    }

    private void setupEvents() {
        addBtn.setOnClickListener(this);
    }

    private void getAllGroups() {
        String userUniqueId = new DataBase(activity).getActiveLocalUser().getUniqueId();
        MainActivity.apiInterface.getAllGroupsByCreatorUniqueId(userUniqueId).enqueue(new Callback<List<Group>>() {
            @Override
            public void onResponse(@NonNull Call<List<Group>> call, @NonNull Response<List<Group>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().get(0).getResponse() == null) {
                        allGroups = response.body();
                        getGroupsInformation();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Group>> call, @NonNull Throwable t) {
                Log.e("Network", "Get All Groups Failure: " + t.getLocalizedMessage());
            }
        });
    }

    private void getGroupsInformation() {
        for (int i = 0; i < allGroups.size(); i++) {
            MainActivity.apiInterface.getGroupInformation(allGroups.get(i).getGroupUniqueId()).enqueue(new Callback<GroupInformation>() {
                @Override
                public void onResponse(@NonNull Call<GroupInformation> call, @NonNull Response<GroupInformation> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        if (response.body().getResponse() == null) {
                            allGroupsInformation.add(response.body());
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
                        allGroupsNames.add(response.body());
                        if (allGroupsNames.size() == allGroups.size()) {
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
        if (allGroups != null && allGroupsInformation != null && allGroupsNames != null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(activity));
            GroupAdapter adapter = new GroupAdapter(activity, allGroups, allGroupsInformation, allGroupsNames);
            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == R.id.fragmentLetsTravel_addBtn) {
            new ReplaceFragment().gotoFragment(new GroupsFragment(), fragmentManager);
            new SharedPreferences().setSharedPreferences(activity, "fragmentName", "groupsFragment");
        }
    }

    private class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.ItemViewHolder> {

        Activity activity;
        List<Group> groups;
        List<GroupInformation> groupInformation;
        List<GroupName> groupNames;

        public GroupAdapter(Activity activity, List<Group> groups, List<GroupInformation> groupInformation, List<GroupName> groupNames) {
            this.activity = activity;
            this.groups = groups;
            this.groupInformation = groupInformation;
            this.groupNames = groupNames;
        }

        @NonNull
        @Override
        public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lets_travel_group, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
            if (!groupNames.get(position).getGroupImage().isEmpty()) {
                Picasso.get().load(ApiClient.TEAM_HIKER_URL + groupNames.get(position).getGroupImage()).into(holder.image);
            } else holder.image.setImageResource(R.drawable.personal_info_photo);
            holder.destination.setText(groups.get(position).getDestinationProvince());
            holder.travelStartTime.setText(groupInformation.get(position).getStartDate());
            holder.travelFinishTime.setText(groupInformation.get(position).getFinishDate());
            holder.membersNumbers.setText("0");
        }

        @Override
        public int getItemCount() {
            return groups.size();
        }

        class ItemViewHolder extends RecyclerView.ViewHolder {

            LinearLayout mainLayout;
            TextView destination, travelStartTime, travelFinishTime, membersNumbers;
            CircleImageView image;

            public ItemViewHolder(@NonNull View itemView) {
                super(itemView);
                mainLayout = itemView.findViewById(R.id.letsTravelGroupItem_mainLayout);
                image = itemView.findViewById(R.id.letsTravelGroupItem_image);
                destination = itemView.findViewById(R.id.letsTravelGroupItem_destination);
                travelStartTime = itemView.findViewById(R.id.letsTravelGroupItem_travelStartTime);
                travelFinishTime = itemView.findViewById(R.id.letsTravelGroupItem_travelFinishTime);
                membersNumbers = itemView.findViewById(R.id.letsTravelGroupItem_membersNumber);
            }
        }
    }
}
