package com.group_1.usege.utilities.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.group_1.usege.R;
import com.group_1.usege.library.activities.LibraryActivity;
import com.group_1.usege.library.activities.OnlineLibraryActivity;
import com.group_1.usege.userInfo.activities.UserPlanActivity;
import com.group_1.usege.userInfo.activities.UserStatisticActivity;

import java.util.HashMap;
import java.util.Map;

public abstract class NavigatedAuthApiCallerActivity<S> extends AuthApiCallerActivity<S> {
    public NavigatedAuthApiCallerActivity(int contentLayoutId) {
        super(contentLayoutId);
    }

    public abstract int navigateId();

    private static final Map<Integer, Class> navIdToClass = new HashMap<Integer, Class>() {
        {
            put(R.id.nav_library, LibraryActivity.class);
            put(R.id.nav_plan, UserPlanActivity.class);
            put(R.id.nav_statistic, UserStatisticActivity.class);
            put(R.id.nav_external_library, OnlineLibraryActivity.class);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // handle toggle Menu
        DrawerLayout drawerLayout = findViewById(R.id.root_drawer_layout);
        NavigationView rootNavigationView = findViewById(R.id.root_navigation_view);
        Activity current = this;
        rootNavigationView.setNavigationItemSelectedListener(
                item -> {
                    int id = item.getItemId();
                    if (id == navigateId())
                        return false;
                    Class tranAct = navIdToClass.getOrDefault(id, null);
                    if (tranAct != null)
                        ActivityUtilities.TransitActivity(current, tranAct);
                    return false;
                });
        ImageView rootMenuImageView = findViewById(R.id.root_menu_image_view);
        rootMenuImageView.setOnClickListener(v -> {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }
}
