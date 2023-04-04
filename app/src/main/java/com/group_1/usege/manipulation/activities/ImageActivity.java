package com.group_1.usege.manipulation.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.group_1.usege.R;
import com.group_1.usege.modle.Image;
import com.group_1.usege.syncing.activities.LibraryActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ImageActivity extends AppCompatActivity {

    TextView tvDatetime, tvSize;
    ImageView ivImage, ivBack;
    EditText etDescription;

    Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            return;
        }

        Image image = (Image) bundle.getParcelable("object_image");
        Log.d("Size", "I: " + image.getDate());
        // Ánh xạ các widgets
        init();
        setValueToLayout(image);

    }

    public void init() {
        tvDatetime = findViewById(R.id.text_view_datetime);
        tvSize = findViewById(R.id.text_view_size);
        ivImage = findViewById(R.id.image_view_image);
        ivBack = findViewById(R.id.image_view_backward);
        etDescription = findViewById(R.id.edit_text_description);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void setValueToLayout(Image image) {
        // Hiển thị dung lượng
        tvSize.setText(image.getSize() + " KB");

        // Format Date
        String date = convertToFormatMDY(image.getDate());
        tvDatetime.setText(date);

        // Hiển thị hình ảnh
        Log.d("URI", "I: " + image.getUri());
        Glide.with(this)
                .load(image.getUri())
                .into(ivImage);

        // Hiển thị mô tả
        etDescription.setText(image.getDescription());
    }

    public String convertToFormatMDY(String datetime) {
        String formattedDate = "";

        if (datetime != null && datetime.length() > 0) {
            Date date = new Date();
            // Chuyển String thành Date
            SimpleDateFormat format = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
            try {
                date = format.parse(datetime);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            // Chuyển thành dạng Month Day, Year
            SimpleDateFormat sdf = new SimpleDateFormat("MMMM d, yyyy", Locale.US);
            formattedDate = sdf.format(date);
        }

        return formattedDate;
    }
}
