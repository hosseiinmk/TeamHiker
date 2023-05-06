package com.teamhike.teamhike.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.squareup.picasso.Picasso;
import com.teamhike.teamhike.Models.User;
import com.teamhike.teamhike.fragments.PublicProfileExperiencesFragment;
import com.teamhike.teamhike.fragments.PublicProfileFriendsFragment;
import com.teamhike.teamhike.fragments.PublicProfilePersonalInfoFragment;
import com.teamhike.teamhike.fragments.PublicProfileToolsFragment;
import com.teamhike.teamhike.R;
import com.teamhike.teamhike.SqliteDataBase.DataBase;
import com.teamhike.teamhike.network.ApiClient;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PublicProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private List<String> tabLayoutTitles;

    private User user;

    private CircleImageView circleImageView;
    private TextView usernameTV;
    private TextView rankTV;
    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private ImageView addBtn, addedBtn;

    private String activeUserUniqueId;

    private String userUniqueId;
    private String imageURL;
    private String username;
    private String rank;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_profile);
        getIntentExtras();
        setupViews();
        setupEvents();
        checkIsMyFriend();
        getUserData();
    }

    private void getIntentExtras() {
        Intent intent = getIntent();
        userUniqueId = intent.getStringExtra("userUniqueId");
        imageURL = intent.getStringExtra("imageURL");
        username = intent.getStringExtra("username");
        rank = intent.getStringExtra("rank");
    }

    private void setupViews() {
        circleImageView = findViewById(R.id.publicProfile_Image);
        usernameTV = findViewById(R.id.publicProfile_username);
        rankTV = findViewById(R.id.publicProfile_userPost);
        tabLayout = findViewById(R.id.publicProfile_tabLayout);
        viewPager2 = findViewById(R.id.PublicProfile_viewPager);
        addBtn = findViewById(R.id.publicProfile_addBtn);
        addedBtn = findViewById(R.id.publicProfile_addedBtn);
    }

    private void setupEvents() {
        addBtn.setOnClickListener(this);
    }

    private void checkIsMyFriend() {
        activeUserUniqueId = new DataBase(this).getActiveLocalUser().getUniqueId();
        MainActivity.apiInterface.getFriends(activeUserUniqueId).enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(@NonNull Call<List<User>> call, @NonNull Response<List<User>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().get(0).getResponse() == null) {
                        List<User> friends = response.body();
                        for (User friend : friends) {
                            if (friend.getUniqueId().equals(userUniqueId)) {
                                myFriend();
                                break;
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<User>> call, @NonNull Throwable t) {
                Log.e("Network", "Get Friends Failure: " + t.getLocalizedMessage());
            }
        });
    }

    private void setupViewPager() {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new PublicProfilePersonalInfoFragment(user));
        fragments.add(new PublicProfileExperiencesFragment(user));
        fragments.add(new PublicProfileToolsFragment(user));
        fragments.add(new PublicProfileFriendsFragment(user));
        PublicProfileAdapter adapter = new PublicProfileAdapter(this, fragments);
        viewPager2.setAdapter(adapter);
    }

    private void getUserData() {
        MainActivity.apiInterface.getUserFromUniqueId(userUniqueId).enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    user = response.body();
                    setUserData();
                    setupViewPager();
                    setupTabs();
                }
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                Log.e("Network", "Get User Failure: " + t.getLocalizedMessage());
            }
        });
    }

    private void setUserData() {
        if (!imageURL.isEmpty()) Picasso.get().load(ApiClient.TEAM_HIKER_URL + imageURL).into(circleImageView);
        else circleImageView.setImageResource(R.drawable.personal_info_photo);
        usernameTV.setText(username);
        rankTV.setText(rank);
    }

    private void setupTabs() {
        tabLayoutTitles = new ArrayList<>();
        tabLayoutTitles.add("اطلاعات فردی");
        tabLayoutTitles.add("تجربیات سفر");
        tabLayoutTitles.add("تجهیزات");
        tabLayoutTitles.add("دوستان");
        new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> tab.setText(tabLayoutTitles.get(position))).attach();
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.publicProfile_addBtn) {
            addFriends();
        }
    }

    private void addFriends() {
        MainActivity.apiInterface.addFriend(activeUserUniqueId, user.getUniqueId()).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) myFriend();
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Log.e("Network", "Add Friend Failure: " + t.getMessage());
            }
        });
    }

    private void myFriend() {
        addBtn.setVisibility(View.GONE);
        addedBtn.setVisibility(View.VISIBLE);
    }

    private class PublicProfileAdapter extends FragmentStateAdapter {

        List<Fragment> fragments;

        public PublicProfileAdapter(@NonNull FragmentActivity fragmentActivity, List<Fragment> fragments) {
            super(fragmentActivity);
            this.fragments = fragments;
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return fragments.get(position);
        }

        @Override
        public int getItemCount() {
            return fragments.size();
        }
    }
}