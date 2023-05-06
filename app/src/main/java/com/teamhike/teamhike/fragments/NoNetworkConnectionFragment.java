package com.teamhike.teamhike.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.teamhike.teamhike.R;

public class NoNetworkConnectionFragment extends Fragment {

    private ImageView noNetworkConnectionImage;
    private TextView noNetworkConnectionTitle;
    private RelativeLayout progressBarLayout;

    private boolean checkingNetworkConnection;

    public NoNetworkConnectionFragment(boolean checkingNetworkConnection) {
        this.checkingNetworkConnection = checkingNetworkConnection;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_no_network_connection, container, false);
        setupViews(view);
        checkingNetworkConnection();
        return view;
    }

    private void setupViews(View view) {
        noNetworkConnectionImage = view.findViewById(R.id.noNetworkConnection_image);
        noNetworkConnectionTitle = view.findViewById(R.id.noNetworkConnection_title);
        progressBarLayout = view.findViewById(R.id.noNetworkConnection_progressBarLayout);
    }

    private void checkingNetworkConnection() {
        if (checkingNetworkConnection) {
            showProgressLayout();
        } else {
            hideProgressLayout();
        }
    }

    private void hideProgressLayout() {
        noNetworkConnectionImage.setVisibility(View.VISIBLE);
        noNetworkConnectionTitle.setVisibility(View.VISIBLE);
        progressBarLayout.setVisibility(View.GONE);
    }

    private void showProgressLayout() {
        noNetworkConnectionImage.setVisibility(View.GONE);
        noNetworkConnectionTitle.setVisibility(View.GONE);
        progressBarLayout.setVisibility(View.VISIBLE);
    }
}
