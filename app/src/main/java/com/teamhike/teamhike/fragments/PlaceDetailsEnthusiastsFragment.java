package com.teamhike.teamhike.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.teamhike.teamhike.R;

public class PlaceDetailsEnthusiastsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_place_details_enthusiasts, container, false);
        setupViews(view);
        setupEvents();
        return view;
    }

    private void setupViews(View view) {

    }

    private void setupEvents() {

    }

}
