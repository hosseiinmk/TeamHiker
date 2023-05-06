package com.teamhike.teamhike.activities;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.teamhike.teamhike.Adapters.GroupDetailAdapter;
import com.teamhike.teamhike.R;
import com.teamhike.teamhike.fragments.GroupDetailMembersFragment;

import java.util.ArrayList;
import java.util.List;

public class GroupDetailActivity extends FragmentActivity {

    TabLayout tabLayout;
    ViewPager2 viewPager2;
    List<String> tabLayoutTitles = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_detail);
        tabLayout = findViewById(R.id.groupDetail_tabLayout);
        viewPager2 = findViewById(R.id.groupDetail_viewPager);
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new GroupDetailMembersFragment());
        fragments.add(new GroupDetailMembersFragment());
        fragments.add(new GroupDetailMembersFragment());
        fragments.add(new GroupDetailMembersFragment());
        fragments.add(new GroupDetailMembersFragment());
//        fragments.add(new GroupDetailMembersFragment());
        FragmentStateAdapter adapter = new GroupDetailAdapter(this, fragments);
        viewPager2.setAdapter(adapter);
        tabLayoutTitles.add("اعضا");
        tabLayoutTitles.add("تصاویر");
        tabLayoutTitles.add("وسایل الزامی");
        tabLayoutTitles.add("وظایف من");
        tabLayoutTitles.add("برنامه سفر");
        tabLayoutTitles.add("نظر سنجی");
        new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> tab.setText(tabLayoutTitles.get(position))).attach();
    }
}