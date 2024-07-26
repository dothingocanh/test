package com.example.tytb1.activity;

import android.os.Bundle;

import com.example.tytb1.R;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.tytb1.fragment.Add;
import com.example.tytb1.fragment.Home;
import com.example.tytb1.fragment.Notification;
import com.example.tytb1.fragment.Profile;
import com.example.tytb1.fragment.Search;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import android.view.MenuItem;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                int itemId = item.getItemId();
                if (itemId == R.id.navigation_home) {
                    selectedFragment = new Home();
                } else if (itemId == R.id.navigation_search) {
                    selectedFragment = new Search();
                } else if (itemId == R.id.navigation_add) {
                    selectedFragment = new Add();
                } else if (itemId == R.id.navigation_favorite) {
                    selectedFragment = new Profile();
                } else if (itemId == R.id.navigation_star) {
                    selectedFragment = new Notification();
                }

                if (selectedFragment != null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                }
                return true;
            }
        });

        // Mặc định chọn HomeFragment khi khởi động ứng dụng
        if (savedInstanceState == null) {
            bottomNavigationView.setSelectedItemId(R.id.navigation_home);
        }
    }
}