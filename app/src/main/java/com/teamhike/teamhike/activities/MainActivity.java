package com.teamhike.teamhike.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;
import com.teamhike.teamhike.CustomClasses.ReplaceFragment;
import com.teamhike.teamhike.CustomClasses.SharedPreferences;
import com.teamhike.teamhike.fragments.FriendsFragment;
import com.teamhike.teamhike.fragments.HomeFragment;
import com.teamhike.teamhike.Models.User;
import com.teamhike.teamhike.fragments.ProfileFavoritePlacesFragment;
import com.teamhike.teamhike.fragments.ProfileFragment;
import com.teamhike.teamhike.R;
import com.teamhike.teamhike.fragments.ToolsFragment;
import com.teamhike.teamhike.fragments.LetsTravelFragment;
import com.teamhike.teamhike.fragments.NoNetworkConnectionFragment;
import com.teamhike.teamhike.network.ApiClient;
import com.teamhike.teamhike.network.ApiInterface;
import com.teamhike.teamhike.SqliteDataBase.DataBase;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        NavigationView.OnNavigationItemSelectedListener, BottomNavigationView.OnNavigationItemSelectedListener {

    public static ApiInterface apiInterface;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private CircleImageView navigationHeaderCircleImageView;
    private TextView navigationHeaderTextView;
    public ImageView backBtn;
    private ImageView menuBtn;
    private ReplaceFragment replaceFragment;
    private SharedPreferences sharedPreferences;
    private User user;
    private BottomNavigationView bottomNavigationView;

    private boolean drawerIsOpen = false;
    private boolean exitReady;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createObjects();
        setupViews();
        setupEvents();
        setNoNetworkFragment(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        String sharedPreferencesValue = sharedPreferences.getSharedPreferences(this, "fragmentName");
        if (user == null || sharedPreferencesValue.equals("editPersonalInformationFragment")) {
            setUserData(sharedPreferencesValue);
        }
    }

    private void createObjects() {
        sharedPreferences = new SharedPreferences();
        replaceFragment = new ReplaceFragment();
    }

    private void setupViews() {
        apiInterface = ApiClient.getTeamHikerRetrofit().create(ApiInterface.class);
        drawerLayout = findViewById(R.id.mainActivity_drawerLayout);
        navigationView = findViewById(R.id.mainActivity_drawerNavigation);
        View navigationHeader = navigationView.getHeaderView(0);
        navigationHeaderCircleImageView = navigationHeader.findViewById(R.id.mainActivityDrawerHeader_image);
        navigationHeaderTextView = navigationHeader.findViewById(R.id.mainActivityDrawerHeader_username);
        backBtn = findViewById(R.id.mainActivityToolbar_backBtn);
        menuBtn = findViewById(R.id.mainActivityToolbar_menuBtn);
        bottomNavigationView = findViewById(R.id.mainActivity_bottomNavigation);
    }

    private void setupEvents() {
        backBtn.setVisibility(View.GONE);
        backBtn.setOnClickListener(this);
        menuBtn.setOnClickListener(this);
        navigationView.setNavigationItemSelectedListener(this);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
    }

    public void setUserData(String sharedPreferencesValue) {
        String userUniqueId = new DataBase(this).getActiveLocalUser().getUniqueId();
        apiInterface.getUserFromUniqueId(userUniqueId).enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getResponse() == null) {
                        user = response.body();
                        navigationHeaderCircleImageView.setPadding(0, 0, 0, 0);
                        if (!user.getImage().isEmpty()) {
                            Picasso.get().load(ApiClient.TEAM_HIKER_URL + user.getImage()).into(navigationHeaderCircleImageView);
                        } else {
                            navigationHeaderCircleImageView.setImageResource(R.drawable.personal_info_photo);
                        }
                        navigationHeaderTextView.setText(user.getUsername());
                    }
                    setHomeFragment(sharedPreferencesValue);
                }
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                setNoNetworkFragment(false);
                Log.e("Network", "Get User Failure: " + t.getLocalizedMessage());
            }
        });
    }

    private void setNoNetworkFragment(boolean checkingNetworkConnection) {
        replaceFragment.gotoFragment(new NoNetworkConnectionFragment(checkingNetworkConnection), getSupportFragmentManager());
    }

    private void setHomeFragment(String sharedPreferencesValue) {
        if (sharedPreferencesValue.equals("editPersonalInformationFragment")) {
            replaceFragment.gotoFragment(new ProfileFragment(user), getSupportFragmentManager());
            sharedPreferences.setSharedPreferences(this, "fragmentName", "profileFragment");
        } else {
            replaceFragment.gotoFragment(new HomeFragment(), getSupportFragmentManager());
            sharedPreferences.setSharedPreferences(this, "fragmentName", "homeFragment");
        }
    }

    @Override
    public void onBackPressed() {
        backPressed();
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.mainActivityToolbar_menuBtn) {
            openDrawerMenu();
        } else if (viewId == R.id.mainActivityToolbar_backBtn) {
            onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemViewId = item.getItemId();
        if (itemViewId == R.id.mainActivityDrawer_profile) {
            if (user != null) {
                closeDrawerMenu();
                disableDrawerMenu();
                backBtn.setVisibility(View.VISIBLE);
                replaceFragment.gotoFragment(new ProfileFragment(user), getSupportFragmentManager());
                sharedPreferences.setSharedPreferences(this, "fragmentName", "profileFragment");
            } else {
                Toast.makeText(this, "به اینترنت متصل نیستید", Toast.LENGTH_SHORT).show();
            }
        } else if (itemViewId == R.id.mainActivityDrawer_friends) {
            if (user != null) {
                backBtn.setVisibility(View.VISIBLE);
                closeDrawerMenu();
                disableDrawerMenu();
                replaceFragment.gotoFragment(new FriendsFragment(), getSupportFragmentManager());
                sharedPreferences.setSharedPreferences(this, "fragmentName", "mainDrawerFriends");
            } else {
                Toast.makeText(this, "به اینترنت متصل نیستید", Toast.LENGTH_SHORT).show();
            }
        } else if (itemViewId == R.id.mainActivityDrawer_tools) {
            if (user != null) {
                backBtn.setVisibility(View.VISIBLE);
                closeDrawerMenu();
                disableDrawerMenu();
                replaceFragment.gotoFragment(new ToolsFragment(), getSupportFragmentManager());
                sharedPreferences.setSharedPreferences(this, "fragmentName", "mainDrawerTools");
            } else {
                Toast.makeText(this, "به اینترنت متصل نیستید", Toast.LENGTH_SHORT).show();
            }
        } else if (itemViewId == R.id.mainActivityDrawer_exit) {
            if (user != null) {
                apiInterface.updateUser(
                        user.getUniqueId(),
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "no"
                ).enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                        if (response.isSuccessful()) {
                            new DataBase(MainActivity.this)
                                    .updateLocalUser(
                                            DataBase.DB_COLUMN_LOGGED,
                                            "no",
                                            DataBase.DB_COLUMN_USER_UNIQUE_ID,
                                            user.getUniqueId()
                                    );
                            startActivity(new Intent(MainActivity.this, RegisterLoginActivity.class));
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                        Log.e("Network", "Update Exit User Status Failure: " + t.getLocalizedMessage());
                    }
                });
            } else {
                Toast.makeText(this, "به اینترنت متصل نیستید", Toast.LENGTH_SHORT).show();
            }
        } else if (itemViewId == R.id.mainActivityBottomNavigation_home) {
            if (user != null) {
                backBtn.setVisibility(View.GONE);
                enableDrawerMenu();
                replaceFragment.gotoFragment(new HomeFragment(), getSupportFragmentManager());
                sharedPreferences.setSharedPreferences(this, "fragmentName", "homeFragment");
            } else {
                Toast.makeText(this, "به اینترنت متصل نیستید", Toast.LENGTH_SHORT).show();
            }
        } else if (itemViewId == R.id.mainActivityBottomNavigation_travel) {
            if (user != null) {
                backBtn.setVisibility(View.VISIBLE);
                replaceFragment.gotoFragment(new LetsTravelFragment(), getSupportFragmentManager());
                sharedPreferences.setSharedPreferences(this, "fragmentName", "travelFragment");
            } else {
                Toast.makeText(this, "به اینترنت متصل نیستید", Toast.LENGTH_SHORT).show();
            }
        }
        return true;
    }

    public void backPressed() {
        String sharedPreferencesValue = sharedPreferences.getSharedPreferences(this, "fragmentName");
        if (sharedPreferencesValue != null) {
            switch (sharedPreferencesValue) {
                case "homeFragment":
                    if (drawerIsOpen) {
                        closeDrawerMenu();
                    } else if (!exitReady) {
                        Toast.makeText(this, "برای خروج برگشت را مجدد فشار دهید", Toast.LENGTH_SHORT).show();
                        exitReady = true;
                        new Handler().postDelayed(() -> exitReady = false, 1000);
                    } else finish();
                    break;
                case "groupsFragment":
                case "chooseDestinationFragment":
                    gotoHomeFragment();
                    break;
                case "mainDrawerFriends":
                case "mainDrawerTools":
                    backBtn.setVisibility(View.GONE);
                    enableDrawerMenu();
                    gotoHomeFragment();
                    break;
                case "homeSearchFragment":
                case "profileFragment":
                    enableDrawerMenu();
                    gotoHomeFragment();
                    break;
                case "editFragment":
                    replaceFragment.gotoFragment(new ProfileFragment(user), getSupportFragmentManager());
                    sharedPreferences.setSharedPreferences(this, "fragmentName", "profileFragment");
                    break;
                case "addToolsFragment":
                    replaceFragment.gotoFragment(new ToolsFragment(), getSupportFragmentManager());
                    sharedPreferences.setSharedPreferences(this, "fragmentName", "editFragment");
                    break;
                case "addFriendsFragment":
                    replaceFragment.gotoFragment(new FriendsFragment(), getSupportFragmentManager());
                    sharedPreferences.setSharedPreferences(this, "fragmentName", "editFragment");
                    break;
                case "profileChooseDestinationFragment":
                    replaceFragment.gotoFragment(new ProfileFavoritePlacesFragment(), getSupportFragmentManager());
                    sharedPreferences.setSharedPreferences(this, "fragmentName", "editFragment");
                    break;
//                case "addPlacesFragment":
//                    replaceFragment.gotoFragment(new ProfileFavoritePlacesFragment(), getSupportFragmentManager());
//                    sharedPreferences.setSharedPreferences(this, "fragmentName", "editFragment");
//                    break;
                case "profileFriendsPublicProfileFragment":
                    replaceFragment.gotoFragment(new ProfileFragment(user), getSupportFragmentManager());
                    sharedPreferences.setSharedPreferences(this, "fragmentName", "editFragment");
                    break;
                case "travelFragment":
                    backBtn.setVisibility(View.GONE);
                    enableDrawerMenu();
                    replaceFragment.gotoFragment(new HomeFragment(), getSupportFragmentManager());
                    sharedPreferences.setSharedPreferences(this, "fragmentName", "homeFragment");
                    break;
            }
        }
    }

    private void openDrawerMenu() {
        drawerLayout.openDrawer(GravityCompat.START);
        drawerIsOpen = true;
    }

    private void closeDrawerMenu() {
        drawerLayout.closeDrawer(GravityCompat.START);
        drawerIsOpen = false;
    }

    public void disableDrawerMenu() {
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        menuBtn.setVisibility(View.GONE);
    }

    private void enableDrawerMenu() {
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        menuBtn.setVisibility(View.VISIBLE);
    }

    private void gotoHomeFragment() {
        backBtn.setVisibility(View.GONE);
        replaceFragment.gotoFragment(new HomeFragment(), getSupportFragmentManager());
        sharedPreferences.setSharedPreferences(this, "fragmentName", "homeFragment");
    }
}
