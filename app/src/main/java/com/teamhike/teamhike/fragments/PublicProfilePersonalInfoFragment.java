package com.teamhike.teamhike.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.teamhike.teamhike.Models.User;
import com.teamhike.teamhike.R;

public class PublicProfilePersonalInfoFragment extends Fragment {

    private User user;
    private View view;
    private TextView aboutMe, city, travelsNum;

    public PublicProfilePersonalInfoFragment(User user) {
        this.user = user;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_public_profile_personal_info, container, false);
        setupViews();
        setUserData();
        return view;
    }

    private void setupViews() {
        aboutMe = view.findViewById(R.id.publicProfilePersonalInfo_aboutMe);
        city = view.findViewById(R.id.publicProfilePersonalInfo_city);
        travelsNum = view.findViewById(R.id.publicProfilePersonalInfo_travelsNum);
    }

    private void setUserData() {
        aboutMe.setText(user.getAboutMe());
        city.setText(user.getCity());
        travelsNum.setText("0");
    }
}
