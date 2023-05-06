package com.teamhike.teamhike.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.teamhike.teamhike.R;
import com.teamhike.teamhike.network.ApiClient;

public class PlaceDetailsAttractionPlaceDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView backBtn, image, staticLocationImage;
    private TextView description;
    private String placeDetailImage, placeDetailDescription, placeDetailStaticLocationImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_details_attraction_place_details);
        setupViews();
        setUpEvents();
        getIntentExtras();
        initializingDetails();
    }

    private void setupViews() {
        backBtn = findViewById(R.id.placeDetailsAttractionPlaceDetails_backBtn);
        image = findViewById(R.id.placeDetailsAttractionPlaceDetails_image);
        staticLocationImage = findViewById(R.id.placeDetailsAttractionPlaceDetails_staticLocationImage);
        description = findViewById(R.id.placeDetailsAttractionPlacesItem_description);
    }

    private void setUpEvents() {
        backBtn.setOnClickListener(this);
    }

    private void getIntentExtras() {
        Intent intent = getIntent();
        placeDetailImage = intent.getStringExtra("image");
        placeDetailDescription = intent.getStringExtra("description");
        placeDetailStaticLocationImage = intent.getStringExtra("staticLocationImage");
    }

    private void initializingDetails() {
        if (!placeDetailImage.isEmpty()) {
            Picasso.get().load(ApiClient.TEAM_HIKER_URL + placeDetailImage).into(image);
        }
        description.setText(placeDetailDescription);
        if (!placeDetailStaticLocationImage.isEmpty()) {
            Picasso.get().load(ApiClient.TEAM_HIKER_URL + placeDetailStaticLocationImage).into(staticLocationImage);
        }
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == R.id.placeDetailsAttractionPlaceDetails_backBtn) {
            finish();
        }
    }
}