package com.teamhike.teamhike.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.teamhike.teamhike.CustomClasses.Image;
import com.teamhike.teamhike.CustomClasses.RotateImage;
import com.teamhike.teamhike.Models.Blog;
import com.teamhike.teamhike.Models.Province;
import com.teamhike.teamhike.R;
import com.teamhike.teamhike.network.ApiClient;
import com.teamhike.teamhike.network.ApiInterface;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddBlogActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private final int BLOG_REQUEST_CODE = 999;
    private final int STATIC_LOCATION_REQUEST_CODE = 998;

    private ImageView backBtn, blogImage, staticLocation;
    private TextView saveBtn;
    private Spinner provinceSpinner;
    private List<String> provincesNames;
    private String selectedProvinceName = "";
    private Uri blogImageUri = null, staticLocationImageUri = null;
    private Bitmap blogImageBitmap = null, staticLocationImageBitmap = null;
    private EditText destinationName, blogDescription;
    private RelativeLayout progressBarLayout;
    private Image image;
    private RotateImage rotateImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_blog);
        setupViews();
        setupEvents();
        initSpinner();
    }

    private void setupViews() {
        backBtn = findViewById(R.id.addBlog_backBtn);
        saveBtn = findViewById(R.id.addBlog_saveBtn);
        provinceSpinner = findViewById(R.id.addBlog_provincesSpinner);
        blogImage = findViewById(R.id.addBlog_blogImage);
        destinationName = findViewById(R.id.addBlog_destinationName);
        blogDescription = findViewById(R.id.addBlog_description);
        staticLocation = findViewById(R.id.addBlog_staticLocationImage);
        progressBarLayout = findViewById(R.id.addBlog_progressBarLayout);
    }

    private void setupEvents() {
        backBtn.setOnClickListener(this);
        saveBtn.setOnClickListener(this);
        blogImage.setOnClickListener(this);
        staticLocation.setOnClickListener(this);
        provinceSpinner.setOnItemSelectedListener(this);
    }

    private void initSpinner() {
        image = new Image();
        rotateImage = new RotateImage();
        ApiInterface teamHikerApi = ApiClient.getTeamHikerRetrofit().create(ApiInterface.class);
        teamHikerApi.getProvinceName().enqueue(new Callback<List<Province>>() {
            @Override
            public void onResponse(@NonNull Call<List<Province>> call, @NonNull Response<List<Province>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Province> provinces = response.body();
                    provincesNames = new ArrayList<>();
                    for (Province province : provinces) {
                        provincesNames.add(province.getProvinceName());
                    }
                    provincesNames.add(0, "انتخاب استان");
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                            AddBlogActivity.this,
                            android.R.layout.simple_spinner_dropdown_item,
                            provincesNames
                    ) {
                        @Override
                        public boolean isEnabled(int position) {
                            return position != 0;
                        }
                    };
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    provinceSpinner.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Province>> call, @NonNull Throwable t) {

            }
        });
    }

    private void saveBlog() {
        showProgressbar();
        if (!selectedProvinceName.equals("")) {
            if (!destinationName.getText().toString().trim().equals("")) {
                if (!blogDescription.getText().toString().trim().equals("")) {
                    if (blogImageUri != null && staticLocationImageUri != null) {
                        String blogImagePath = "";
                        String encodedBlogImage = "";
                        String staticLocationImagePath = "";
                        String encoded_static_location_image = "";
                        if (blogImageBitmap != null && staticLocationImageBitmap != null) {
                            blogImagePath = image.createImageName();
                            encodedBlogImage = image.getEncodedImage(blogImageBitmap);
                            blogImagePath = "blog_images/" + blogImagePath;

                            staticLocationImagePath = image.createImageName();
                            encoded_static_location_image = image.getEncodedImage(staticLocationImageBitmap);
                            staticLocationImagePath = "blog_images/" + staticLocationImagePath;
                        }
                        String blogUniqueId = generateUniqueId();
                        MainActivity.apiInterface.registerBlog(
                                blogUniqueId,
                                selectedProvinceName,
                                destinationName.getText().toString().trim(),
                                blogImagePath,
                                encodedBlogImage,
                                blogDescription.getText().toString().trim(),
                                staticLocationImagePath,
                                encoded_static_location_image
                        ).enqueue(new Callback<Blog>() {
                            @Override
                            public void onResponse(@NonNull Call<Blog> call, @NonNull Response<Blog> response) {
                                if (response.isSuccessful() && response.body() != null) {
                                    if (response.body().getResponse().equals("successfully")) {
                                        hideProgressbar();
                                        Toast.makeText(AddBlogActivity.this, "بلاگ ثبت شد", Toast.LENGTH_LONG).show();
                                    } else {
                                        hideProgressbar();
                                        Toast.makeText(AddBlogActivity.this, "بلاگ ثبت نشد!", Toast.LENGTH_LONG).show();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<Blog> call, @NonNull Throwable t) {
                                hideProgressbar();
                                Log.e("Network", "Register Blog Failure: " + t.getLocalizedMessage());
                            }
                        });
                    } else {
                        hideProgressbar();
                        Toast.makeText(this, "تصاویر انتخاب نشده اند!", Toast.LENGTH_LONG).show();
                    }
                } else {
                    hideProgressbar();
                    Toast.makeText(this, "توضیحات وارد نشده", Toast.LENGTH_LONG).show();
                }
            } else {
                hideProgressbar();
                Toast.makeText(this, "مقصد انتخاب نشده", Toast.LENGTH_LONG).show();
            }
        } else {
            hideProgressbar();
            Toast.makeText(this, "استان انتخاب نشده", Toast.LENGTH_LONG).show();
        }
    }

    private void showProgressbar() {
        progressBarLayout.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    private void hideProgressbar() {
        progressBarLayout.setVisibility(View.GONE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    private String generateUniqueId() {
        int min = 10000, max = 99999;
        double uniqueId = Math.random() * (max - min + 1) + min;
        return "blog_" + (int) uniqueId;
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == R.id.addBlog_backBtn) {
            onBackPressed();
        } else if (viewId == R.id.addBlog_saveBtn) {
            saveBlog();
        } else if (viewId == R.id.addBlog_blogImage) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, BLOG_REQUEST_CODE);
        } else if (viewId == R.id.addBlog_staticLocationImage) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, STATIC_LOCATION_REQUEST_CODE);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (i != 0) {
            Log.d("DEBUG", "ProvinceName: " + provincesNames.get(i));
            selectedProvinceName = provincesNames.get(i);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null && data.getData() != null) {
            try {
                if (requestCode == BLOG_REQUEST_CODE) {
                    blogImageUri = data.getData();
                    blogImageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), blogImageUri);
                    InputStream inputStream = getContentResolver().openInputStream(blogImageUri);
                    blogImageBitmap = rotateImage.getRotatedImageBitmap(inputStream, blogImageBitmap);
//                    blogImageBitmap = Bitmap.createScaledBitmap(blogImageBitmap, blogImageBitmap.getWidth() / 3, blogImageBitmap.getHeight() / 3, true);
                    blogImage.setImageBitmap(blogImageBitmap);
                } else if (requestCode == STATIC_LOCATION_REQUEST_CODE) {
                    staticLocationImageUri = data.getData();
                    staticLocationImageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), staticLocationImageUri);
                    InputStream inputStream = getContentResolver().openInputStream(staticLocationImageUri);
                    staticLocationImageBitmap = rotateImage.getRotatedImageBitmap(inputStream, staticLocationImageBitmap);
//                    staticLocationImageBitmap = Bitmap.createScaledBitmap(staticLocationImageBitmap, staticLocationImageBitmap.getWidth() / 3, staticLocationImageBitmap.getHeight() / 3, true);
                    staticLocation.setImageBitmap(staticLocationImageBitmap);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}