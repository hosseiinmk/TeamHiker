package com.teamhike.teamhike.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.teamhike.teamhike.Models.Items;
import com.teamhike.teamhike.Models.MapSearch;
import com.teamhike.teamhike.Models.Province;
import com.teamhike.teamhike.R;
import com.teamhike.teamhike.network.ApiClient;
import com.teamhike.teamhike.network.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddExperienceLocationActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    private TextView saveBtn;
    private ImageView backBtn;
    public EditText searchBox;
    private RecyclerView recyclerView;
    private ExperienceLocationAdapter adapter;
    private ApiInterface mapApi;
    public static String selectedLocation = "";
    public static String selectedLocationProvince = "";
    private List<Items> items;

    @Override
    protected void onStart() {
        super.onStart();
        mapApi = ApiClient.getMapRetrofit().create(ApiInterface.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_experience_location);
        setupViews();
        setupEvents();
        getProvincesName();
    }

    private void setupViews() {
        saveBtn = findViewById(R.id.addExperienceLocation_saveBtn);
        backBtn = findViewById(R.id.addExperienceLocation_backBtn);
        searchBox = findViewById(R.id.addExperienceLocation_searchBox);
        recyclerView = findViewById(R.id.addExperienceLocation_recyclerView);
    }

    private void setupEvents() {
        saveBtn.setOnClickListener(this);
        backBtn.setOnClickListener(this);
        searchBox.addTextChangedListener(this);
    }

    private void getProvincesName() {
        MainActivity.apiInterface.getProvinceName().enqueue(new Callback<List<Province>>() {
            @Override
            public void onResponse(@NonNull Call<List<Province>> call, @NonNull Response<List<Province>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Province> provinces = response.body();
                    setupRecyclerView(provinces);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Province>> call, @NonNull Throwable t) {
                Log.e("Network", "Get Provinces Name Failure: " + t.getLocalizedMessage());
            }
        });
    }

    private void setupRecyclerView(List<Province> provinces) {
        items = new ArrayList<>();
        Items item = new Items();
        items.add(item);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ExperienceLocationAdapter(items, provinces, searchBox);
        recyclerView.setAdapter(adapter);
        items.clear();
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == R.id.addExperienceLocation_saveBtn) {
            if (!selectedLocation.equals("")) {
                finish();
            } else {
                Toast.makeText(this, "مکان تجربه رو انتخاب نکردید", Toast.LENGTH_LONG).show();
            }
        } else if (viewId == R.id.addExperienceLocation_backBtn) {
            selectedLocation = "";
            finish();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (editable.toString().isEmpty()) {
            adapter.updateItems(items);
        } else {
            searchLocation(editable.toString());
        }
    }

    private void searchLocation(String locationSearched) {
        final String url = "https://api.neshan.org/v1/search?term=" + locationSearched + "&lat=51.33820" + "&lng=35.69992";
        mapApi.getSearch(url).enqueue(new Callback<MapSearch>() {
            @Override
            public void onResponse(@NonNull Call<MapSearch> call, @NonNull Response<MapSearch> response) {
                if (response.isSuccessful() && response.body() != null) {
                    MapSearch mapSearch = response.body();
                    List<Items> items = mapSearch.getItems();
                    adapter.updateItems(items);
                }
            }

            @Override
            public void onFailure(@NonNull Call<MapSearch> call, @NonNull Throwable t) {
                Log.e("Network", "Search Location Failure: " + t.getLocalizedMessage());
            }
        });
    }

    private class ExperienceLocationAdapter extends RecyclerView.Adapter<ExperienceLocationAdapter.ItemViewHolder> {

        List<Items> items;
        List<Province> provinces;
        EditText searchBox;

        public ExperienceLocationAdapter(List<Items> items, List<Province> provinces, EditText searchBox) {
            this.items = items;
            this.provinces = provinces;
            this.searchBox = searchBox;
        }

        @NonNull
        @Override
        public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ItemViewHolder(
                    LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.experience_location_item, parent, false)
            );
        }

        @Override
        public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
            holder.locationName.setText(items.get(position).getTitle());
            holder.constraintLayout.setOnClickListener(v -> {
                if (items.get(position).getRegion() != null) {
                    String region = items.get(position).getRegion();
                    String regionName = region.substring(region.lastIndexOf(" ") + 1);
//                Log.d("Debug", "regionName: " + regionName);
                    AddExperienceLocationActivity.selectedLocation = items.get(position).getTitle();
                    for (Province province : provinces) {
//                    Log.d("Debug", "province name: " + province.getProvinceName());
                        if (province.getProvinceName().equals(regionName)) {
//                        Log.d("Debug", "Selected Location Province: " + province.getProvinceName());
                            AddExperienceLocationActivity.selectedLocationProvince = province.getProvinceName();
                            break;
                        }
                    }
                    searchBox.setText(items.get(position).getTitle());
                }
            });
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        public void updateItems(List<Items> items) {
            this.items = items;
            notifyDataSetChanged();
        }

//        public void updateProvincesName(List<String> provincesName) {
//            this.provincesName = provincesName;
//            notifyDataSetChanged();
//        }

        public class ItemViewHolder extends RecyclerView.ViewHolder {

            ConstraintLayout constraintLayout;
            TextView locationName;

            public ItemViewHolder(@NonNull View itemView) {
                super(itemView);
                constraintLayout = itemView.findViewById(R.id.experienceLocationItem_nameLayout);
                locationName = itemView.findViewById(R.id.experienceLocationItem_name);
            }
        }
    }
}