package com.teamhike.teamhike.activities;

//import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;

/*
import android.content.Intent;
import android.graphics.BitmapFactory;
*/

import android.os.Bundle;

/*
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
*/

/*
import com.teamhike.teamhike.Models.Items;
import com.teamhike.teamhike.Models.MapSearch;
import com.teamhike.teamhike.Models.Province;
*/

import com.teamhike.teamhike.R;

/*
import com.teamhike.teamhike.network.ApiClient;
import com.teamhike.teamhike.network.ApiInterface;
*/

/*
import org.neshan.core.LngLat;
import org.neshan.core.Range;
import org.neshan.layers.VectorElementLayer;
import org.neshan.services.NeshanMapStyle;
import org.neshan.services.NeshanServices;
import org.neshan.styles.AnimationStyle;
import org.neshan.styles.AnimationStyleBuilder;
import org.neshan.styles.AnimationType;
import org.neshan.styles.MarkerStyle;
import org.neshan.styles.MarkerStyleCreator;
import org.neshan.ui.ClickData;
import org.neshan.ui.ClickType;
import org.neshan.ui.MapEventListener;
import org.neshan.ui.MapView;
import org.neshan.utils.BitmapUtils;
import org.neshan.vectorelements.Marker;
*/

/*
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
*/

public class CreateGroupChooseDestinationActivity extends AppCompatActivity {

    /* private ImageView backBtn;
    private TextView nextStepBtn;
    private Spinner destinationSpinner;
    private MapView mapView;
    private String selectedProvinceName;
    private List<Province> provinces;
    private List<String> provincesName;
    private List<Items> items;

    private double longitude, latitude;

    ApiInterface teamHikerApi, mapApi;
    VectorElementLayer markerLayer; */

    @Override
    protected void onStart() {
        super.onStart();
//        initLayoutReferences();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group_choose_destination);
        /* createObjects();
        setupViews();
        setupEvents(); */
    }

    /* private void createObjects() {
        teamHikerApi = ApiClient.getTeamHikerRetrofit().create(ApiInterface.class);
        mapApi = ApiClient.getMapRetrofit().create(ApiInterface.class);
        teamHikerApi.getProvinceName().enqueue(new Callback<List<Province>>() {
            @Override
            public void onResponse(@NonNull Call<List<Province>> call, @NonNull Response<List<Province>> response) {
                provinces = response.body();
                provincesName = new ArrayList<>();
                for (Province province : provinces) {
                    provincesName.add(province.getProvinceName());
                }
                provincesName.add(0, "استان مورد نظر");
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                        CreateGroupChooseDestinationActivity.this,
                        android.R.layout.simple_spinner_dropdown_item,
                        provincesName
                ) {
                    @Override
                    public boolean isEnabled(int position) {
                        return position != 0;
                    }
                };
                destinationSpinner.setAdapter(adapter);
            }

            @Override
            public void onFailure(@NonNull Call<List<Province>> call, @NonNull Throwable t) {
                Log.e("Network", "Get Provinces Name Failure: " + t.getLocalizedMessage());
            }
        });
    } */

    /* private void setupViews() {
        destinationSpinner = findViewById(R.id.createGroupChooseDestination_destinationSpinner);
        backBtn = findViewById(R.id.createGroupChooseDestination_backBtn);
        nextStepBtn = findViewById(R.id.createGroupChooseDestination_nextStepBtn);
        mapView = findViewById(R.id.createGroupChooseDestination_mapView);
    } */

    /* private void setupEvents() {
        backBtn.setOnClickListener(v -> onBackPressed());
        nextStepBtn.setOnClickListener(v -> {
            if (selectedProvinceName != null && !selectedProvinceName.isEmpty() && longitude != 0.0 && latitude != 0.0) {
                goToNextStep();
            } else {
                Toast.makeText(this, "لطفا استان مورد نظر را انتخاب کنید", Toast.LENGTH_SHORT).show();
            }
        });
        destinationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0) {
                    selectedProvinceName = provincesName.get(i);
                    gotoPlace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    } */

    /* private void goToNextStep() {
        Intent intent = new Intent(this, CreateGroupAttractionsActivity.class);
        intent.putExtra("provinceName", selectedProvinceName);
        intent.putExtra("mapLongitude", String.valueOf(longitude));
        intent.putExtra("mapLatitude", String.valueOf(latitude));
        startActivity(intent);
    } */

    /* private void gotoPlace() {
        final double a = 0.0;
        final double b = 0.0;
        final String url = "https://api.neshan.org/v1/search?term=" + selectedProvinceName + "&lat=" + a + "&lng=" + b;
        mapApi.getSearch(url).enqueue(new Callback<MapSearch>() {
            @Override
            public void onResponse(@NonNull Call<MapSearch> call, @NonNull Response<MapSearch> response) {
                if (response.isSuccessful() && response.body() != null) {
                    MapSearch mapSearch = response.body();
                    items = mapSearch.getItems();
                    if (items == null) Log.d("Status", "items is null");
                    else {
                        mapView.setFocalPointPosition(new LngLat(items.get(0).getLocation().getX(), items.get(0).getLocation().getY()), 0f);
                        mapView.setZoom(15f, 0f);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<MapSearch> call, @NonNull Throwable t) {
                Log.e("Network", "Map Search Failure: " + t.getLocalizedMessage());
                Toast.makeText(CreateGroupChooseDestinationActivity.this, "به اینترنت متصل نیستید", Toast.LENGTH_SHORT).show();
            }
        });
    } */

    /* private void initLayoutReferences() {
        initMap();
        mapView.setMapEventListener(new MapEventListener() {
            @Override
            public void onMapClicked(ClickData mapClickInfo) {
                if (mapClickInfo.getClickType() == ClickType.CLICK_TYPE_LONG) {
                    if (selectedProvinceName != null) {
                        LngLat clickedLocation = mapClickInfo.getClickPos();
                        addMarker(clickedLocation);
                    }
                }
            }
        });
    } */

    /* private void initMap() {
        int BASE_MAP_INDEX = 0;
        markerLayer = NeshanServices.createVectorElementLayer();
        mapView.getLayers().add(markerLayer);
        mapView.getOptions().setZoomRange(new Range(4.5f, 18f));
        mapView.getLayers().insert(BASE_MAP_INDEX, NeshanServices.createBaseMap(NeshanMapStyle.STANDARD_DAY));
        mapView.setFocalPointPosition(new LngLat(51.33820, 35.69992), 0f);
        mapView.setZoom(15f, 0f);
    } */

    /* private void addMarker(LngLat loc) {
        markerLayer.clear();
        AnimationStyleBuilder animStBl = new AnimationStyleBuilder();
        animStBl.setFadeAnimationType(AnimationType.ANIMATION_TYPE_SMOOTHSTEP);
        animStBl.setSizeAnimationType(AnimationType.ANIMATION_TYPE_SPRING);
        animStBl.setPhaseInDuration(0.5f);
        animStBl.setPhaseOutDuration(0.5f);
        AnimationStyle animSt = animStBl.buildStyle();
        MarkerStyleCreator markStCr = new MarkerStyleCreator();
        markStCr.setSize(20f);
        markStCr.setBitmap(
                BitmapUtils.createBitmapFromAndroidBitmap(
                        BitmapFactory.decodeResource(
                                getResources(), R.drawable.ic_marker
                        )
                )
        );
        markStCr.setAnimationStyle(animSt);
        MarkerStyle markSt = markStCr.buildStyle();
        Marker marker = new Marker(loc, markSt);
        Log.i("Longitude", String.valueOf(loc.getX()));
        Log.i("Latitude", String.valueOf(loc.getY()));
        markerLayer.add(marker);
        longitude = loc.getX();
        latitude = loc.getY();
    } */
}