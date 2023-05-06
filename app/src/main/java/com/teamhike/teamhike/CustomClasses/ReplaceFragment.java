package com.teamhike.teamhike.CustomClasses;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.teamhike.teamhike.R;

public class ReplaceFragment {
    public void gotoFragment(Fragment fragment, FragmentManager fragmentManager) {
        fragmentManager.beginTransaction().replace(R.id.mainActivity_frameLayout, fragment).commit();
    }
}
