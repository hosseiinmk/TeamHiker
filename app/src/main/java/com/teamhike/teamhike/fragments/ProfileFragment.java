package com.teamhike.teamhike.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.squareup.picasso.Picasso;
import com.teamhike.teamhike.Adapters.ProfilePlacesAdapter;
import com.teamhike.teamhike.Adapters.ProfileToolsAdapter;
import com.teamhike.teamhike.Adapters.SliderAdapter;
import com.teamhike.teamhike.CustomClasses.ReplaceFragment;
import com.teamhike.teamhike.CustomClasses.SharedPreferences;
import com.teamhike.teamhike.Models.Experience;
import com.teamhike.teamhike.Models.Place;
import com.teamhike.teamhike.Models.Tool;
import com.teamhike.teamhike.Models.User;
import com.teamhike.teamhike.R;
import com.teamhike.teamhike.activities.AdminPanelActivity;
import com.teamhike.teamhike.activities.EditPersonalInformationActivity;
import com.teamhike.teamhike.activities.MainActivity;
import com.teamhike.teamhike.activities.PublicProfileActivity;
import com.teamhike.teamhike.network.ApiClient;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment implements View.OnClickListener {

    private Activity activity;
    private List<Experience> experiences;
    private FragmentManager fragmentManager;
    private ReplaceFragment replaceFragment;
    private SharedPreferences sharedPreferences;
    private View view;
    private TextView username, userRank, aboutMe;
    private CircleImageView circleImageView;
    private Button adminPanelBtn, personalInfo_editBtn, experience_editBtn, tools_editBtn, friends_editBtn, places_editBtn;
    private ViewPager2 viewPager2;
    private RecyclerView toolsRecyclerView, friendsRecyclerView, placesRecyclerView;

    private User user;

    public ProfileFragment(User user) {
        this.user = user;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = getActivity();
        fragmentManager = getFragmentManager();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        createObjects();
        setupViews();
        setupEvents();
        checkIsUserAdmin();
        setData();
        return view;
    }

    private void createObjects() {
        sharedPreferences = new SharedPreferences();
        replaceFragment = new ReplaceFragment();
    }

    private void setupViews() {
        // Admin Panel Button
        adminPanelBtn = view.findViewById(R.id.profileAdminPanelBtn);
        //Personal Info Views
        circleImageView = view.findViewById(R.id.profilePersonalInfo_circleImage);
        username = view.findViewById(R.id.profilePersonalInfo_username);
        userRank = view.findViewById(R.id.profilePersonalInfo_userRank);
        aboutMe = view.findViewById(R.id.profilePersonalInfo_aboutMe);
        //Experiences View
        viewPager2 = view.findViewById(R.id.profileExperience_viewPager);
        //Tools RecyclerView
        toolsRecyclerView = view.findViewById(R.id.profileTools_recyclerView);
        //Friends RecyclerView
        friendsRecyclerView = view.findViewById(R.id.profileFriends_recyclerView);
        // Places RecyclerView
        placesRecyclerView = view.findViewById(R.id.profilePlaces_recyclerView);
        //Edit Buttons
        personalInfo_editBtn = view.findViewById(R.id.profilePersonalInfo_editBtn);
        experience_editBtn = view.findViewById(R.id.profileExperience_editBtn);
        tools_editBtn = view.findViewById(R.id.profileTools_editBtn);
        friends_editBtn = view.findViewById(R.id.profileFriends_editBtn);
        places_editBtn = view.findViewById(R.id.profilePlaces_editBtn);
    }

    private void setupEvents() {
        adminPanelBtn.setOnClickListener(this);
        personalInfo_editBtn.setOnClickListener(this);
        experience_editBtn.setOnClickListener(this);
        tools_editBtn.setOnClickListener(this);
        friends_editBtn.setOnClickListener(this);
        places_editBtn.setOnClickListener(this);
    }

    private void checkIsUserAdmin() {
        if (user.getAdmin() == 1) {
            adminPanelBtn.setVisibility(View.VISIBLE);
        }
    }

    private void setData() {
        setUserData();
        setExperienceSlider();
        showTools();
        showFriends();
        showPlaces();
    }

    private void setUserData() {
        username.setText(user.getUsername());
        userRank.setText(user.getPost());
        aboutMe.setText(user.getAboutMe());
        if (!user.getImage().isEmpty())
            Picasso.get().load(ApiClient.TEAM_HIKER_URL + user.getImage()).into(circleImageView);
        else circleImageView.setImageResource(R.drawable.personal_info_photo);
    }

    private void setExperienceSlider() {
        MainActivity.apiInterface.getUserExperiencesByUniqueId(user.getUniqueId()).enqueue(new Callback<List<Experience>>() {
            @Override
            public void onResponse(@NonNull Call<List<Experience>> call, @NonNull Response<List<Experience>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().get(0).getResponse() == null) {
                        List<String> images = new ArrayList<>();
                        experiences = response.body();
                        for (Experience experience : experiences) {
                            images.add(experience.getImage());
                        }
                        viewPager2.setAdapter(new SliderAdapter(images));
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Experience>> call, @NonNull Throwable t) {
                Log.e("Network", "Get Experience Failure: " + t.getLocalizedMessage());
            }
        });
    }

    private void showTools() {
        MainActivity.apiInterface.getTools(user.getUniqueId()).enqueue(new Callback<List<Tool>>() {
            @Override
            public void onResponse(@NonNull Call<List<Tool>> call, @NonNull Response<List<Tool>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().get(0).getResponse() == null) {
                        List<Tool> tools = response.body();
                        toolsRecyclerView.setLayoutManager(new GridLayoutManager(activity, 2));
                        toolsRecyclerView.setAdapter(new ProfileToolsAdapter(tools));
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Tool>> call, @NonNull Throwable t) {
                Log.e("Network", "Get Tools Failure: " + t.getLocalizedMessage());
            }
        });
    }

    private void showFriends() {
        MainActivity.apiInterface.getFriends(user.getUniqueId()).enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(@NonNull Call<List<User>> call, @NonNull Response<List<User>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().get(0).getResponse() == null) {
                        List<User> friends = response.body();
                        friendsRecyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
                        friendsRecyclerView.setAdapter(new ProfileFriendsAdapter(activity, friends));
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<User>> call, @NonNull Throwable t) {
                Log.e("Network", "Get Friend Failure: " + t.getLocalizedMessage());
            }
        });
    }

    private void showPlaces() {
        MainActivity.apiInterface.getFavoritePlaces(user.getUniqueId()).enqueue(new Callback<List<Place>>() {
            @Override
            public void onResponse(@NonNull Call<List<Place>> call, @NonNull Response<List<Place>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().get(0).getResponse() == null) {
                        List<Place> places = response.body();
                        placesRecyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
                        placesRecyclerView.setAdapter(new ProfilePlacesAdapter(activity, places));
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Place>> call, @NonNull Throwable t) {
                Log.e("Network", "Get Favorite Places Failure: " + t.getLocalizedMessage());
            }
        });
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.profileAdminPanelBtn) {
            Intent intent = new Intent(activity, AdminPanelActivity.class);
            startActivity(intent);
        } else if (viewId == R.id.profilePersonalInfo_editBtn) {
            Intent intent = new Intent(activity, EditPersonalInformationActivity.class);
            startActivity(intent);
            sharedPreferences.setSharedPreferences(activity, "fragmentName", "editPersonalInformationFragment");
        } else if (viewId == R.id.profileExperience_editBtn) {
            replaceFragment.gotoFragment(new ExperiencesFragment(), fragmentManager);
            sharedPreferences.setSharedPreferences(activity, "fragmentName", "editFragment");
        } else if (viewId == R.id.profileTools_editBtn) {
            replaceFragment.gotoFragment(new ToolsFragment(), fragmentManager);
            sharedPreferences.setSharedPreferences(activity, "fragmentName", "editFragment");
        } else if (viewId == R.id.profileFriends_editBtn) {
            replaceFragment.gotoFragment(new FriendsFragment(), fragmentManager);
            sharedPreferences.setSharedPreferences(activity, "fragmentName", "editFragment");
        } else if (viewId == R.id.profilePlaces_editBtn) {
            replaceFragment.gotoFragment(new ProfileFavoritePlacesFragment(), fragmentManager);
            sharedPreferences.setSharedPreferences(activity, "fragmentName", "editFragment");
        }
    }

    private class ProfileFriendsAdapter extends RecyclerView.Adapter<ProfileFriendsAdapter.ItemViewHolder> {

        Context context;
        List<User> friends;

        public ProfileFriendsAdapter(Context context, List<User> friends) {
            this.context = context;
            this.friends = friends;
        }

        @NonNull
        @Override
        public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_friend_item, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
            if (!friends.get(position).getImage().isEmpty())
                Picasso.get().load(ApiClient.TEAM_HIKER_URL + friends.get(position).getImage()).into(holder.image);
            else holder.image.setImageResource(R.drawable.personal_info_photo);
            holder.image.setOnClickListener(v -> {
                Intent intent = new Intent(context, PublicProfileActivity.class);
                intent.putExtra("userUniqueId", friends.get(position).getUniqueId());
                intent.putExtra("imageURL", friends.get(position).getImage());
                intent.putExtra("username", friends.get(position).getUsername());
                intent.putExtra("rank", friends.get(position).getPost());
                startActivity(intent);
            });
        }

        @Override
        public int getItemCount() {
            return friends.size();
        }

        class ItemViewHolder extends RecyclerView.ViewHolder {

            CircleImageView image;

            ItemViewHolder(@NonNull View itemView) {
                super(itemView);
                image = itemView.findViewById(R.id.profileFriend_image);
            }
        }
    }
}
