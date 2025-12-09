package com.poly.realstate;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AllHousesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_houses);

        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbarAllHouses);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        // RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerViewAllHouses);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2)); // 2 columns

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

        HouseAdapter adapter = new HouseAdapter(this, houses);
        recyclerView.setAdapter(adapter);
    }
}
