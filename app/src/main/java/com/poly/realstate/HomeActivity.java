package com.poly.realstate;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;

    private ViewPager2 bannerViewPager;
    private Handler bannerHandler = new Handler();
    private int[] bannerImages = {R.drawable.onboarding1, R.drawable.onboarding2, R.drawable.onboarding3};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Drawer & Toolbar
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Placeholder header info
        TextView headerName = navigationView.getHeaderView(0).findViewById(R.id.header_name);
        TextView headerEmail = navigationView.getHeaderView(0).findViewById(R.id.header_email);
        headerName.setText("Utilisateur");
        headerEmail.setText("user@email.com");

        // Navigation drawer item click
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                Toast.makeText(HomeActivity.this, "Home clicked", Toast.LENGTH_SHORT).show();
            } else if (id == R.id.nav_profile) {
                startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
            } else if (id == R.id.nav_creehome) {
                startActivity(new Intent(HomeActivity.this, CreationHomeActivity.class));
            } else if (id == R.id.nav_signout) {
                // Logout → Go to SignInActivity
                startActivity(new Intent(HomeActivity.this, SignInActivity.class));
                finish();
            }
            drawerLayout.closeDrawers();
            return true;
        });

        // Banner ViewPager2
        bannerViewPager = findViewById(R.id.bannerViewPager);
        BannerAdapter bannerAdapter = new BannerAdapter(this, bannerImages);
        bannerViewPager.setAdapter(bannerAdapter);
        startBannerAutoScroll();

        // Houses and Best Offers RecyclerViews
        setupHousesAndOffers();
    }

    private void setupHousesAndOffers() {
        RecyclerView recyclerViewHouses = findViewById(R.id.recyclerViewHouses);
        RecyclerView recyclerViewBestOffers = findViewById(R.id.recyclerViewBestOffers);
        TextView viewAllHouses = findViewById(R.id.textViewViewAllHouses);

        // Sample data
        List<House> houses = new ArrayList<>();
        houses.add(new House(
                R.drawable.ic_home,
                "Villa Green",
                "120000 DT",
                "Tunis",
                "150 m²",
                "3 rooms",
                "A beautiful villa located in the heart of Tunis with a large garden and modern facilities."
        ));

        houses.add(new House(
                R.drawable.ic_home,
                "Modern House",
                "95000 DT",
                "Sousse",
                "120 m²",
                "2 rooms",
                "A modern house in a quiet neighborhood of Sousse, perfect for small families."
        ));

        houses.add(new House(
                R.drawable.ic_home,
                "Luxury Home",
                "200000 DT",
                "Hammamet",
                "250 m²",
                "5 rooms",
                "A luxurious home near the beach of Hammamet with private pool and garden."
        ));

        houses.add(new House(
                R.drawable.ic_home,
                "Small Apartment",
                "50000 DT",
                "La Marsa",
                "80 m²",
                "2 rooms",
                "A cozy small apartment located in La Marsa, ideal for singles or couples."
        ));

        houses.add(new House(
                R.drawable.ic_home,
                "Studio Flat",
                "35000 DT",
                "Monastir",
                "50 m²",
                "1 room",
                "A compact studio flat in Monastir, perfect for students or short-term stays."
        ));

        List<House> bestOffers = new ArrayList<>();
        houses.add(new House(
                R.drawable.ic_home,
                "Small Apartment",
                "50000 DT",
                "La Marsa",
                "80 m²",
                "2 rooms",
                "A cozy small apartment located in La Marsa, ideal for singles or couples."
        ));

        houses.add(new House(
                R.drawable.ic_home,
                "Studio Flat",
                "35000 DT",
                "Monastir",
                "50 m²",
                "1 room",
                "A compact studio flat in Monastir, perfect for students or short-term stays."
        ));

        // Setup RecyclerViews
        HouseAdapter housesAdapter = new HouseAdapter(this, houses);
        recyclerViewHouses.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewHouses.setAdapter(housesAdapter);

        HouseAdapter bestOffersAdapter = new HouseAdapter(this, bestOffers);
        recyclerViewBestOffers.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewBestOffers.setAdapter(bestOffersAdapter);

        // View All button click
        viewAllHouses.setOnClickListener(v ->
                startActivity(new Intent(HomeActivity.this, AllHousesActivity.class)));

    }

    private void startBannerAutoScroll() {
        final Runnable runnable = new Runnable() {
            int current = 0;

            @Override
            public void run() {
                if (current == bannerImages.length) current = 0;
                bannerViewPager.setCurrentItem(current++, true);
                bannerHandler.postDelayed(this, 3000);
            }
        };
        bannerHandler.postDelayed(runnable, 3000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bannerHandler.removeCallbacksAndMessages(null);
    }
}
