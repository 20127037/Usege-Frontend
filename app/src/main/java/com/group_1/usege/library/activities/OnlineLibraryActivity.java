package com.group_1.usege.library.activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.group_1.usege.R;

public class OnlineLibraryActivity extends AppCompatActivity {

    private SearchView searchView;
    private RecyclerView recyclerView;

    public OnlineLibraryActivity()
    {
        super(R.layout.activity_online_library);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        searchView = findViewById(R.id.view_search);
        recyclerView = findViewById(R.id.lst_img);
        recyclerView.setAdapter();

        ViewModelProvider viewModel = new ViewModelProvider(this)
                .get(ExampleViewModel.class);

        UserAdapter pagingAdapter = new UserAdapter(new UserComparator());
        recyclerView.adapter = pagingAdapter
        viewModel.flowable
                // Using AutoDispose to handle subscription lifecycle.
                // See: https://github.com/uber/AutoDispose
                .to(autoDisposable(AndroidLifecycleScopeProvider.from(this)))
                .subscribe(pagingData -> pagingAdapter.submitData(lifecycle, pagingData));
    }
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView();
//
//        NavigationView rootNavigationView = findViewById(R.id.root_navigation_view);
//        rootNavigationView.setNavigationItemSelectedListener(
//                new NavigationView.OnNavigationItemSelectedListener() {
//                    @Override
//                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                        return false;
//                    }
//                });
//        ImageView rootMenuImageView = findViewById(R.id.root_menu_image_view);
//        rootMenuImageView.setOnClickListener(v -> {
//            DrawerLayout drawerLayout = findViewById(R.id.root_drawer_layout);
//            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
//                drawerLayout.closeDrawer(GravityCompat.START);
//            } else {
//                drawerLayout.openDrawer(GravityCompat.START);
//            }
//        });
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//    }
}
