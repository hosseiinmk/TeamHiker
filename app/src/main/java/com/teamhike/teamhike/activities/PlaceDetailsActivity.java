package com.teamhike.teamhike.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.teamhike.teamhike.R;
import com.teamhike.teamhike.fragments.PlaceDetailsAttractionPlacesFragment;
import com.teamhike.teamhike.fragments.PlaceDetailsEnthusiastsFragment;
import com.teamhike.teamhike.fragments.PlaceDetailsExperiencesFragment;

import java.util.ArrayList;
import java.util.List;

public class PlaceDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView btnBack;
    private String placeProvinceName, placeAttractionPlaceNumber, placeEnthusiastNumber;
    private TextView provinceName, attractionPlacesNumber, enthusiastsNumber;
    private TabLayout tabLayout;
    private ViewPager2 viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_details);
        setupViews();
        setupEvents();
        getIntentExtras();
        initializingDetails();
        initializingFragments();
        initializingTabLayout();
    }

    private void setupViews() {
        btnBack = findViewById(R.id.placeDetails_backBtn);
        provinceName = findViewById(R.id.placeDetails_provinceName);
        attractionPlacesNumber = findViewById(R.id.placeDetails_attractionPlacesNumber);
        enthusiastsNumber = findViewById(R.id.placeDetails_enthusiastsNumber);
        tabLayout = findViewById(R.id.placeDetails_tabLayout);
        viewPager = findViewById(R.id.placeDetails_viewPager);
    }

    private void setupEvents() {
        btnBack.setOnClickListener(this);
    }

    private void getIntentExtras() {
        placeProvinceName = getIntent().getStringExtra("provinceName");
        placeAttractionPlaceNumber = getIntent().getStringExtra("attractionPlaceNumber");
        placeEnthusiastNumber = getIntent().getStringExtra("enthusiastNumber");
    }

    private void initializingDetails() {
        provinceName.setText(placeProvinceName);
        attractionPlacesNumber.setText(placeAttractionPlaceNumber);
        enthusiastsNumber.setText(placeEnthusiastNumber);
    }

    private void initializingFragments() {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new PlaceDetailsAttractionPlacesFragment(placeProvinceName));
//        fragments.add(new PlaceDetailsAttractionPlacesFragment(placeProvinceName));
        fragments.add(new PlaceDetailsExperiencesFragment(placeProvinceName));
        fragments.add(new PlaceDetailsEnthusiastsFragment());
        ChooseDestinationDetailsAdapter adapter = new ChooseDestinationDetailsAdapter(this, fragments);
        viewPager.setAdapter(adapter);
    }

    private void initializingTabLayout() {
        List<String> tabLayoutTitles = new ArrayList<>();
        tabLayoutTitles.add("جاذبه های دیدنی");
//        tabLayoutTitles.add("گروه ها و تورها");
        tabLayoutTitles.add("تجربیات کاربران");
        tabLayoutTitles.add("افراد علاقه مند");
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> tab.setText(tabLayoutTitles.get(position))).attach();
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == R.id.placeDetails_backBtn) {
            finish();
        }
    }

    private class ChooseDestinationDetailsAdapter extends FragmentStateAdapter {

        List<Fragment> fragments;

        public ChooseDestinationDetailsAdapter(@NonNull FragmentActivity fragmentActivity, List<Fragment> fragments) {
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