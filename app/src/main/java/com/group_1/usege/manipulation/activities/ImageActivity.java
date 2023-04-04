package com.group_1.usege.manipulation.activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.group_1.usege.R;

public class ImageActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    int selectedItem = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setSelectedItemId(-1);
        bottomNavigationView.setLabelVisibilityMode(NavigationBarView.LABEL_VISIBILITY_LABELED);

//        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                switch (item.getItemId()) {
//                    case R.id.action_photoshop:
//                        // xử lý khi chọn item 1
//                        if (selectedItem != 0)
//                            selectedItem = 0; // lưu vị trí item đang được chọn
//                        return true;
//                    case R.id.action_favorite:
//                        // xử lý khi chọn item 2
//                        selectedItem = 1; // lưu vị trí item đang được chọn
//                        return true;
//                    case R.id.action_describe:
//                        // xử lý khi chọn item 3
//                        selectedItem = 2; // lưu vị trí item đang được chọn
//                        return true;
//                    case R.id.action_delete:
//                        selectedItem = 3;
//                        return true;
//                }
//                return false;
//            }
//        });
    }
}
