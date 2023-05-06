package com.teamhike.teamhike.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.teamhike.teamhike.CustomClasses.Image;
import com.teamhike.teamhike.CustomClasses.RotateImage;
import com.teamhike.teamhike.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CreateGroupInformationActivity extends AppCompatActivity {

    private final int REQUEST_CODE = 0;

    private ImageView backBtn;
    private TextView nextStepBtn;
    private CircleImageView circleImageView;
    private EditText groupName;
    private Uri imageUri;
    private Bitmap bitmap;

    private List<String> attractions;
    private List<String> targetDestinationsNames;

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
    private String startTravelTimeText;
    private String endTravelTimeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group_information);
        getIntentExtras();
        setupViews();
        setupEvents();
    }

    private void getIntentExtras() {
        attractions = new ArrayList<>();
        targetDestinationsNames = new ArrayList<>();
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
        startTravelDateText = intent.getStringExtra("startTravelDateText");
        endTravelDateText = intent.getStringExtra("endTravelDateText");
        startDestinationText = intent.getStringExtra("startDestinationText");
        endDestinationText = intent.getStringExtra("endDestinationText");
        startTravelTimeText = intent.getStringExtra("startTravelTimeText");
        endTravelTimeText = intent.getStringExtra("endTravelTimeText");
        targetDestinationsNames.add(intent.getStringExtra("targetDestinationsName1"));
        targetDestinationsNames.add(intent.getStringExtra("targetDestinationsName2"));
        targetDestinationsNames.add(intent.getStringExtra("targetDestinationsName3"));
        targetDestinationsNames.add(intent.getStringExtra("targetDestinationsName4"));
        targetDestinationsNames.add(intent.getStringExtra("targetDestinationsName5"));
        neededOnWayText = intent.getStringExtra("neededOnWayText");
        mealsText = intent.getStringExtra("mealsText");
        moreNotesText = intent.getStringExtra("moreNotesText");
    }

    private void setupViews() {
        backBtn = findViewById(R.id.createGroupInformation_backBtn);
        nextStepBtn = findViewById(R.id.createGroupInformation_nextStepBtn);
        circleImageView = findViewById(R.id.createGroupInformation_circleImage);
        groupName = findViewById(R.id.createGroupInformation_groupName);
    }

    private void setupEvents() {
        backBtn.setOnClickListener(v -> onBackPressed());
        nextStepBtn.setOnClickListener(v -> {
            if (!groupName.getText().toString().isEmpty()) {
                goToNextStep();
            } else {
                Toast.makeText(this, "برای گروه خود نام انتخاب کنید", Toast.LENGTH_SHORT).show();
            }
        });
        circleImageView.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, REQUEST_CODE);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            try {
                imageUri = data.getData();
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                InputStream inputStream = getContentResolver().openInputStream(imageUri);
                bitmap = new RotateImage().getRotatedImageBitmap(inputStream, bitmap);
                bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() / 3, bitmap.getHeight() / 3, true);
                circleImageView.setPadding(0, 0, 0, 0);
                circleImageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void goToNextStep() {
        String encodedImage = "";
        String imagePath = "";
        if (imageUri != null && bitmap != null) {
            Log.d("", "imageUri & bitmap is not null");
            Image image = new Image();
            String imageName = image.createImageName();
            encodedImage = image.getEncodedImage(bitmap);
            imagePath = "uploads/" + imageName;
        }
        Intent intent = new Intent(CreateGroupInformationActivity.this, CreateGroupChooseLeaderActivity.class);
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
        intent.putExtra("groupName", groupName.getText().toString().trim());
        intent.putExtra("imagePath", imagePath);
        intent.putExtra("encodedImage", encodedImage);
        startActivity(intent);
    }
}