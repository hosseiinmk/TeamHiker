package com.teamhike.teamhike.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.teamhike.teamhike.Adapters.GroupDetailMemberAdapter;
import com.teamhike.teamhike.activities.MainActivity;
import com.teamhike.teamhike.Models.User;
import com.teamhike.teamhike.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GroupDetailMembersFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group_detail_member, container, false);
        setupViews(view);
        return view;
    }

    private void setupViews(View view) {
        MainActivity.apiInterface.getAllGroupMembers().enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(@NonNull Call<List<User>> call, @NonNull Response<List<User>> response) {
                if (response.isSuccessful() && response.body() != null) {
//                    Log.i("test", "onResponse: " + response.body().get(0).getUsername());
                    RecyclerView recyclerView = view.findViewById(R.id.fragmentGroupDetailMembers_RecyclerView);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    GroupDetailMemberAdapter adapter = new GroupDetailMemberAdapter(response.body());
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<User>> call, @NonNull Throwable t) {
                Log.e("Network", "Get All Group Members Failure: " + t.getLocalizedMessage());
            }
        });
    }
}
