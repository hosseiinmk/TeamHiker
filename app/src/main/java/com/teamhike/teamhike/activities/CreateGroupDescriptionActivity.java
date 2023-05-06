package com.teamhike.teamhike.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.teamhike.teamhike.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CreateGroupDescriptionActivity extends AppCompatActivity {

    final int MAX_TARGET_DESTINATIONS = 5;

    private ImageView backBtn;
    private TextView nextStepBtn, addTargetDestinationBtn, travelMoveTimeBtn, arriveTimeBtn;
    private DatePicker startTravelDate, endTravelDate;
    private EditText startTravel, endTravel, targetDestination, needOnWayTools, meals, moreNotes;
    private RecyclerView recyclerView;
    private TargetDestinationsAdapter adapter;

    private List<String> targetDestinationsNames;
    private List<String> addedTargetDestinationsNames;
    private List<String> attractions;

    private String groupUniqueId;
    private String provinceName;
    private String mapLongitude;
    private String mapLatitude;
    private String ratingBar;
    private String minimumMemberNumber;
    private String maximumMemberNumber;
    private String startTravelDateText;
    private String endTravelDateText;
    private String startDestinationText;
    private String endDestinationText;
    private String neededOnWayText;
    private String mealsText;
    private String moreNotesText;
    private String startTravelTimeText = "";
    private String endTravelTimeText = "";

    private int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group_description);
        getIntentExtras();
        setupViews();
        setupEvents();
        setupRecyclerView();
    }

    private void getIntentExtras() {
        attractions = new ArrayList<>();
        Intent intent = getIntent();
        groupUniqueId = intent.getStringExtra("groupUniqueId");
        provinceName = intent.getStringExtra("provinceName");
        mapLongitude = intent.getStringExtra("mapLongitude");
        mapLatitude = intent.getStringExtra("mapLatitude");
        attractions.add(intent.getStringExtra("attraction1"));
        attractions.add(intent.getStringExtra("attraction2"));
        attractions.add(intent.getStringExtra("attraction3"));
        attractions.add(intent.getStringExtra("attraction4"));
        attractions.add(intent.getStringExtra("attraction5"));
        attractions.add(intent.getStringExtra("attraction6"));
        attractions.add(intent.getStringExtra("attraction7"));
        attractions.add(intent.getStringExtra("attraction8"));
        attractions.add(intent.getStringExtra("attraction9"));
        ratingBar = intent.getStringExtra("ratingBar");
        minimumMemberNumber = intent.getStringExtra("minimumMemberNumber");
        maximumMemberNumber = intent.getStringExtra("maximumMemberNumber");
    }

    private void setupRecyclerView() {
        targetDestinationsNames = new ArrayList<>();
        addedTargetDestinationsNames = new ArrayList<>();
        for (int i = 0; i < MAX_TARGET_DESTINATIONS; i++) {
            targetDestinationsNames.add(i, "");
            addedTargetDestinationsNames.add(i, "");
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TargetDestinationsAdapter(addedTargetDestinationsNames);
        recyclerView.setAdapter(adapter);
        addedTargetDestinationsNames.clear();
    }

    private void setupViews() {
        backBtn = findViewById(R.id.createGroupDescription_backBtn);
        nextStepBtn = findViewById(R.id.createGroupDescription_nextStepBtn);
        addTargetDestinationBtn = findViewById(R.id.createGroupDescription_addTargetDestinationBtn);
        travelMoveTimeBtn = findViewById(R.id.createGroupDescription_travelMoveTimeBtn);
        arriveTimeBtn = findViewById(R.id.createGroupDescription_arriveTimeBtn);
        startTravelDate = findViewById(R.id.createGroupDescription_startTravelDate);
        endTravelDate = findViewById(R.id.createGroupDescription_endTravelDate);
        startTravel = findViewById(R.id.createGroupDescription_startTravel);
        endTravel = findViewById(R.id.createGroupDescription_endTravel);
        targetDestination = findViewById(R.id.createGroupDescription_targetDestination);
        needOnWayTools = findViewById(R.id.createGroupDescription_needOnWayTools);
        meals = findViewById(R.id.createGroupDescription_meals);
        moreNotes = findViewById(R.id.createGroupDescription_moreNotes);
        recyclerView = findViewById(R.id.createGroupDescription_targetDestinationsRecyclerView);
    }

    private void setupEvents() {
        backBtn.setOnClickListener(v -> onBackPressed());
        nextStepBtn.setOnClickListener(v -> goToNextStep());
        addTargetDestinationBtn.setOnClickListener(v -> {
            if (counter < MAX_TARGET_DESTINATIONS) {
                if (!targetDestination.getText().toString().isEmpty()) {
                    addedTargetDestinationsNames.add(targetDestination.getText().toString());
                    adapter.updateTargetDestinationsNames(addedTargetDestinationsNames);
                    counter++;
                } else {
                    Toast.makeText(this, "مکان مورد نظر را وارد کنید", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "حداکثر پنج مقصد میتوانید انتخاب کنید", Toast.LENGTH_SHORT).show();
            }
        });
        travelMoveTimeBtn.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);
            TimePickerDialog timePickerDialog = new TimePickerDialog(this, (timePicker, i, i1) -> {
                if (i < 10 && i1 < 10) startTravelTimeText = "0" + i + " : 0" + i1;
                else if (i < 10 && i1 > 10) startTravelTimeText = "0" + i + " : " + i1;
                else if (i > 10 && i1 < 10) startTravelTimeText = i + " : 0" + i1;
                else startTravelTimeText = i + " : " + i1;
                travelMoveTimeBtn.setText(startTravelTimeText);
            }, hour, minute, true);
            timePickerDialog.show();
        });
        arriveTimeBtn.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);
            TimePickerDialog timePickerDialog = new TimePickerDialog(this, (timePicker, i, i1) -> {
                if (i < 10 && i1 < 10) endTravelTimeText = "0" + i + " : 0" + i1;
                else if (i < 10 && i1 > 10) endTravelTimeText = "0" + i + " : " + i1;
                else if (i > 10 && i1 < 10) endTravelTimeText = i + " : 0" + i1;
                else endTravelTimeText = i + " : " + i1;
                arriveTimeBtn.setText(endTravelTimeText);
            }, hour, minute, true);
            timePickerDialog.show();
        });
    }

    private void goToNextStep() {
        startTravelDateText = startTravelDate.getYear() + "/" + startTravelDate.getMonth() + "/" + startTravelDate.getDayOfMonth();
        endTravelDateText = endTravelDate.getYear() + "/" + endTravelDate.getMonth() + "/" + endTravelDate.getDayOfMonth();
        startDestinationText = startTravel.getText().toString();
        endDestinationText = endTravel.getText().toString();
        neededOnWayText = needOnWayTools.getText().toString();
        mealsText = meals.getText().toString();
        moreNotesText = moreNotes.getText().toString();
        if (startDestinationText.isEmpty()) {
            Toast.makeText(this, "مبدا سفر را مشخص کنید", Toast.LENGTH_SHORT).show();
        } else if (endDestinationText.isEmpty()) {
            Toast.makeText(this, "پایان سفر را مشخص کنید", Toast.LENGTH_SHORT).show();
        } else if (startTravelTimeText.isEmpty()) {
            Toast.makeText(this, "زمان حرکت سفر را مشخص کنید", Toast.LENGTH_SHORT).show();
        } else if (endTravelTimeText.isEmpty()) {
            Toast.makeText(this, "زمان پایان سفر را مشخص کنید", Toast.LENGTH_SHORT).show();
        } else {
            if (addedTargetDestinationsNames != null) {
                for (int i = 0; i < addedTargetDestinationsNames.size(); i++) {
                    if (!addedTargetDestinationsNames.get(i).isEmpty()) {
                        targetDestinationsNames.set(i, addedTargetDestinationsNames.get(i));
                    }
                }
            }
            registerGroupInformation();
        }
    }

    private void registerGroupInformation() {
        Intent intent = new Intent(CreateGroupDescriptionActivity.this, CreateGroupMemberNecessaryToolsActivity.class);
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
        intent.putExtra("ratingBar", ratingBar);
        intent.putExtra("minimumMemberNumber", minimumMemberNumber);
        intent.putExtra("maximumMemberNumber", maximumMemberNumber);
        intent.putExtra("startTravelDateText", startTravelDateText);
        intent.putExtra("endTravelDateText", endTravelDateText);
        intent.putExtra("startDestinationText", startDestinationText);
        intent.putExtra("endDestinationText", endDestinationText);
        intent.putExtra("startTravelTimeText", startTravelTimeText);
        intent.putExtra("endTravelTimeText", endTravelTimeText);
        intent.putExtra("targetDestinationsName1", targetDestinationsNames.get(0));
        intent.putExtra("targetDestinationsName2", targetDestinationsNames.get(1));
        intent.putExtra("targetDestinationsName3", targetDestinationsNames.get(2));
        intent.putExtra("targetDestinationsName4", targetDestinationsNames.get(3));
        intent.putExtra("targetDestinationsName5", targetDestinationsNames.get(4));
        intent.putExtra("neededOnWayText", neededOnWayText);
        intent.putExtra("mealsText", mealsText);
        intent.putExtra("moreNotesText", moreNotesText);
        startActivity(intent);
    }

    private class TargetDestinationsAdapter extends RecyclerView.Adapter<TargetDestinationsAdapter.ItemViewHolder> {

        List<String> targetDestinationsNames;

        public TargetDestinationsAdapter(List<String> targetPlacesNames) {
            this.targetDestinationsNames = targetPlacesNames;
        }

        @NonNull
        @Override
        public TargetDestinationsAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return
                    new TargetDestinationsAdapter.ItemViewHolder(
                            LayoutInflater
                                    .from(parent.getContext())
                                    .inflate(R.layout.item_group_target_place, parent, false)
                    );
        }

        @Override
        public void onBindViewHolder(@NonNull TargetDestinationsAdapter.ItemViewHolder holder, int position) {
            holder.targetPlaceName.setText(targetDestinationsNames.get(position));
//            holder.deleteBtn.setOnClickListener(v -> {
//                targetDestinationsNames.remove(position);
//                updateTargetDestinationsNames(targetDestinationsNames);
//            });
        }

        @Override
        public int getItemCount() {
            return targetDestinationsNames.size();
        }

        public void updateTargetDestinationsNames(List<String> targetDestinationsNames) {
            this.targetDestinationsNames = targetDestinationsNames;
            notifyDataSetChanged();
        }

        public class ItemViewHolder extends RecyclerView.ViewHolder {

            TextView targetPlaceName;
            ImageView deleteBtn;

            public ItemViewHolder(@NonNull View itemView) {
                super(itemView);
                targetPlaceName = itemView.findViewById(R.id.targetPlaces_name);
                deleteBtn = itemView.findViewById(R.id.targetPlaces_deleteBtn);
            }
        }
    }
}