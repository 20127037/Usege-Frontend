package com.group_1.usege.library.activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.group_1.usege.R;

public class OnlineLibraryActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_library);

        NavigationView rootNavigationView = findViewById(R.id.root_navigation_view);
        rootNavigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        return false;
                    }
                });
        ImageView rootMenuImageView = findViewById(R.id.root_menu_image_view);
        rootMenuImageView.setOnClickListener(v -> {
            DrawerLayout drawerLayout = findViewById(R.id.root_drawer_layout);
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
