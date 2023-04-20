package com.group_1.usege.userInfo.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.group_1.usege.R;
import com.group_1.usege.library.activities.LibraryActivity;
import com.group_1.usege.userInfo.model.UserStatistic;
import com.group_1.usege.userInfo.repository.UserInfoRepository;
import com.group_1.usege.userInfo.services.MasterServiceGenerator;
import com.group_1.usege.utilities.activities.ActivityUtilities;
import com.group_1.usege.utilities.activities.AuthApiCallerActivity;
import com.group_1.usege.utilities.math.MathUtilities;

import java.util.Locale;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;


@AndroidEntryPoint
public class UserStatisticActivity extends AuthApiCallerActivity<UserStatistic> {
    Context context = this;
    private TextView txtUsedSpace;
    private ProgressBar progressUsedSpace;
    private TextView txtCountImg;
    private TextView txtCountAlbums;
    @Inject
    public MasterServiceGenerator masterServiceGenerator;

    public UserStatisticActivity()
    {
        super(R.layout.activity_user_statistic);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // handle toggle Menu
        DrawerLayout drawerLayout = findViewById(R.id.root_drawer_layout);
        NavigationView rootNavigationView = findViewById(R.id.root_navigation_view);
        rootNavigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        // Do something when a menu item is clicked
                        Intent intentSettings;
                        switch (item.getItemId()) {
                            case R.id.nav_library:
                                // Handle menu item 1 click
                                ActivityUtilities.TransitActivity((Activity) context, LibraryActivity.class);
                                break;
                            case R.id.nav_external_library:
                                // Handle menu item 2 click
//                                intentSettings = new Intent(LibraryActivity.this, OnlineLibraryActivity.class);
//                                startActivity(intentSettings);
                                break;
                            case R.id.nav_plan:
                                // Handle menu item 2 click
                                ActivityUtilities.TransitActivity((Activity) context, UserPlanActivity.class);
                                break;
                            case R.id.nav_statistic:
                                // Handle menu item 2 click
                                ActivityUtilities.TransitActivity((Activity)context, UserStatisticActivity.class);
                                break;
                            // Add more cases for other menu items as needed
                        }
                        return false;
                    }
                });
        ImageView rootMenuImageView = findViewById(R.id.root_menu_image_view);
        rootMenuImageView.setOnClickListener(v -> {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        txtUsedSpace = findViewById(R.id.txt_used_space);
        progressUsedSpace = findViewById(R.id.progress_used_space);
        txtCountAlbums = findViewById(R.id.txt_total_albums);
        txtCountImg = findViewById(R.id.txt_total_imgs);
    }

    protected void onResume()
    {
        super.onResume();
//        handleCallSuccess(new UserStatistic(10000000, MathUtilities.gbToKb(15), 100, 100));
        startCallApi(masterServiceGenerator
                .getService(tokenRepository.getToken().getAccessToken())
                .getUserStatistic(tokenRepository.getToken().getUserId()));
    }

    private void setUsedSpace(long usedSpace, long maxSpace)
    {
        double usedSpaceInGb = MathUtilities.kbToGb(usedSpace);
        double maxSpaceInGb = MathUtilities.kbToGb(maxSpace);
        String usedSpaceRep = String.format(Locale.getDefault(),"%.2f/%d GB", usedSpaceInGb, (int)maxSpaceInGb);
        txtUsedSpace.setText(usedSpaceRep);
        progressUsedSpace.setMax((int)maxSpace);
        progressUsedSpace.setProgress((int)usedSpace);
    }

    @Override
    protected void handleCallSuccess(UserStatistic body) {
        setUsedSpace(body.getUsedSpaceInKb(), body.getMaxSpaceInKb());
        txtCountImg.setText(String.valueOf(body.getCountImg()));
        txtCountAlbums.setText(String.valueOf(body.getCountAlbum()));
    }
}