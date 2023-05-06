package com.teamhike.teamhike.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.teamhike.teamhike.R;

import java.util.ArrayList;
import java.util.List;

public class CreateGroupAttractionsActivity extends AppCompatActivity implements View.OnClickListener {

    final int MAX_BUTTONS = 9;

    private ImageView backBtn;
    private TextView nextStepBtn;
    private RatingBar ratingBar;

    private String provinceName, mapLongitude, mapLatitude;
    private Boolean attractionSelected = false;

    private List<Button> buttons;
    private List<Boolean> isClicked;
    private List<String> attractions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group_attractions);
        getIntentExtras();
        createObjects();
        setupViews();
        initializing();
        setupEvents();
    }

    private void getIntentExtras() {
        Intent intent = getIntent();
        provinceName = intent.getStringExtra("provinceName");
        mapLongitude = intent.getStringExtra("mapLongitude");
        mapLatitude = intent.getStringExtra("mapLatitude");
    }

    private void createObjects() {
        buttons = new ArrayList<>();
        isClicked = new ArrayList<>();
        attractions = new ArrayList<>();
    }

    private void setupViews() {
        backBtn = findViewById(R.id.createGroupAttractions_backBtn);
        nextStepBtn = findViewById(R.id.createGroupAttractions_nextStepBtn);
        ratingBar = findViewById(R.id.createGroupAttractions_hardnessLevelRatingBar);
        buttons.add(findViewById(R.id.createGroupAttractions_jungleBtn));
        buttons.add(findViewById(R.id.createGroupAttractions_mountainBtn));
        buttons.add(findViewById(R.id.createGroupAttractions_seaBtn));
        buttons.add(findViewById(R.id.createGroupAttractions_lakeBtn));
        buttons.add(findViewById(R.id.createGroupAttractions_waterfallBtn));
        buttons.add(findViewById(R.id.createGroupAttractions_historicalAttractionsBtn));
        buttons.add(findViewById(R.id.createGroupAttractions_beachBtn));
        buttons.add(findViewById(R.id.createGroupAttractions_caveBtn));
        buttons.add(findViewById(R.id.createGroupAttractions_moreBtn));
    }

    private void initializing() {
        for (int i = 0; i < MAX_BUTTONS; i++) {
            isClicked.add(i, false);
        }
        for (int i = 0; i < MAX_BUTTONS; i++) {
            attractions.add(i, "");
        }
    }

    private void setupEvents() {
        backBtn.setOnClickListener(v -> onBackPressed());
        nextStepBtn.setOnClickListener(v -> {
            for (int i = 0 ; i < isClicked.size() ; i++) {
                if (isClicked.get(i)) {
                    attractionSelected = true;
                    break;
                } else {
                    attractionSelected = false;
                }
            }
            if (attractionSelected) {
                if (ratingBar.getRating() != 0.0) {
                    goToNextStep();
                } else Toast.makeText(this, "میزان سختی سفر را مشخص کنید", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "لطفا یکی از جاذبه ها را انتخاب کنید", Toast.LENGTH_SHORT).show();
            }
        });
        for (Button btn : buttons) {
            btn.setOnClickListener(this);
        }
    }

    private void goToNextStep() {
        String groupUniqueId = generateGroupUniqueId();
        Intent intent = new Intent(CreateGroupAttractionsActivity.this, CreateGroupAddMemberActivity.class);
        intent.putExtra("groupUniqueId", groupUniqueId);
        intent.putExtra("provinceName", provinceName);
        intent.putExtra("mapLongitude", mapLongitude);
        intent.putExtra("mapLatitude", mapLatitude);
        intent.putExtra("attraction1", attractions.get(0));
        intent.putExtra("attraction2", attractions.get(1));
        intent.putExtra("attraction3", attractions.get(2));
        intent.putExtra("attraction4", attractions.get(3));
        intent.putExtra("attraction5", attractions.get(4));
        intent.putExtra("attraction6", attractions.get(5));
        intent.putExtra("attraction7", attractions.get(6));
        intent.putExtra("attraction8", attractions.get(7));
        intent.putExtra("attraction9", attractions.get(8));
        intent.putExtra("ratingBar", ratingBar.getRating());
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        for (int i = 0 ; i < buttons.size() ; i++) {
            if (viewId == buttons.get(i).getId()) {
                if (isClicked.get(i)) {
                    buttons.get(i).setBackgroundResource(R.drawable.simple_background);
                    attractions.set(i, "");
                    isClicked.set(i, false);
                } else {
                    buttons.get(i).setBackgroundResource(R.drawable.simple_selected_background);
                    attractions.set(i, buttons.get(i).getText().toString());
                    isClicked.set(i, true);
                }
                break;
            }
        }
    }

    private String generateGroupUniqueId() {
        int min = 1000, max = 9999;
        double uniqueId = Math.random() * (max - min + 1) + min;
        return "group_" + (int) uniqueId;
    }
}