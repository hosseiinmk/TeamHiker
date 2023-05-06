package com.teamhike.teamhike.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
//import android.widget.LinearLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import com.teamhike.teamhike.Adapters.HomeSliderAdapter;
import com.teamhike.teamhike.CustomClasses.ReplaceFragment;
import com.teamhike.teamhike.CustomClasses.SharedPreferences;
import com.teamhike.teamhike.R;
import com.teamhike.teamhike.activities.MainActivity;
import com.teamhike.teamhike.activities.ShareExperienceActivity;
import com.teamhike.teamhike.fragments.PlacesFragment;
import com.teamhike.teamhike.fragments.GroupsFragment;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private Activity activity;
    private FragmentManager fragmentManager;
    private View view;
    private ViewPager2 viewPager2;
    private EditText searchBox;
    private RelativeLayout groupsBtn, chooseDestinationBtn;
    private LinearLayout shareExperienceBtn;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = getActivity();
        fragmentManager = getFragmentManager();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        setupViews();
        setupEvents();
        setSlider();
        return view;
    }

    private void setupViews() {
        viewPager2 = view.findViewById(R.id.homeFragment_viewPager);
        searchBox = view.findViewById(R.id.homeFragmentSearchBox);
        groupsBtn = view.findViewById(R.id.homeFragment_groupsBtn);
        chooseDestinationBtn = view.findViewById(R.id.homeFragment_chooseDestinationBtn);
        shareExperienceBtn = view.findViewById(R.id.homeFragment_shareExperiencesBtn);
    }

    private void setupEvents() {
        searchBox.setEnabled(false);
//        searchBox.setOnClickListener(v -> {
//            ((MainActivity) activity).disableDrawerMenu();
//            ((MainActivity) activity).backBtn.setVisibility(View.VISIBLE);
//            new ReplaceFragment().gotoFragment(new HomeSearchFragment(), fragmentManager);
//            new SharedPreferences().setSharedPreferences(activity, "fragmentName", "homeSearchFragment");
//        });

        groupsBtn.setOnClickListener(v -> {
            ((MainActivity) activity).backBtn.setVisibility(View.VISIBLE);
            new ReplaceFragment().gotoFragment(new GroupsFragment(), fragmentManager);
            new SharedPreferences().setSharedPreferences(activity, "fragmentName", "groupsFragment");
        });

        chooseDestinationBtn.setOnClickListener(v -> {
            ((MainActivity) activity).backBtn.setVisibility(View.VISIBLE);
            new ReplaceFragment().gotoFragment(new PlacesFragment(), fragmentManager);
            new SharedPreferences().setSharedPreferences(activity, "fragmentName", "chooseDestinationFragment");
        });

        shareExperienceBtn.setOnClickListener(v -> startActivity(new Intent(activity, ShareExperienceActivity.class)));
    }

    private void setSlider() {
        List<Bitmap> images = new ArrayList<>();
        images.add(BitmapFactory.decodeResource(activity.getResources(), R.drawable.slider_pic1));
        images.add(BitmapFactory.decodeResource(activity.getResources(), R.drawable.slider_pic2));
        images.add(BitmapFactory.decodeResource(activity.getResources(), R.drawable.slider_pic3));
        viewPager2.setAdapter(new HomeSliderAdapter(activity, images));
    }
}
