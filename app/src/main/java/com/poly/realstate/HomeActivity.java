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
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class HomeActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;

    private ViewPager2 bannerViewPager;
    private Handler bannerHandler = new Handler();
    private int[] bannerImages = {R.drawable.onboarding1, R.drawable.onboarding2, R.drawable.onboarding3};

    FirebaseFirestore db;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        // Drawer & Toolbar
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Fetch drawer header
        TextView headerName = navigationView.getHeaderView(0).findViewById(R.id.header_name);
        TextView headerEmail = navigationView.getHeaderView(0).findViewById(R.id.header_email);

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            headerEmail.setText(currentUser.getEmail());

            // Fetch full name from Firestore
            db.collection("users").document(currentUser.getUid())
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if(documentSnapshot.exists()) {
                            String fullName = documentSnapshot.getString("fullName");
                            if(fullName != null) headerName.setText(fullName);
                        }
                    });
        }

        // Navigation drawer item click
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                Toast.makeText(HomeActivity.this, "Home clicked", Toast.LENGTH_SHORT).show();
            } else if (id == R.id.nav_profile) {
                Toast.makeText(HomeActivity.this, "Profile clicked", Toast.LENGTH_SHORT).show();
            } else if (id == R.id.nav_creehome) {
                startActivity(new Intent(HomeActivity.this, CreationHomeActivity.class));
            } else if (id == R.id.nav_signout) {
                // Logout
                mAuth.signOut();
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

        // Auto-scroll banner
        startBannerAutoScroll();
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
