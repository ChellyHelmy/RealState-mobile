package com.poly.realstate;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class HouseDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_details);

        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbarHouseDetails);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        // Views
        ImageView imgHouse = findViewById(R.id.imgHouseDetails);
        TextView txtTitle = findViewById(R.id.txtHouseTitle);
        TextView txtPrice = findViewById(R.id.txtHousePrice);
        TextView txtAddress = findViewById(R.id.txtHouseAddress);
        TextView txtArea = findViewById(R.id.txtHouseArea);
        TextView txtRooms = findViewById(R.id.txtHouseRooms);
        TextView txtDescription = findViewById(R.id.txtHouseDescription);
        Button btnReservation = findViewById(R.id.btnReservation);

        // Get data from Intent extras
        imgHouse.setImageResource(getIntent().getIntExtra("imageRes", R.drawable.ic_home));
        txtTitle.setText(getIntent().getStringExtra("title"));
        txtPrice.setText(getIntent().getStringExtra("price"));
        txtAddress.setText(getIntent().getStringExtra("address"));
        txtArea.setText(getIntent().getStringExtra("area"));
        txtRooms.setText(getIntent().getStringExtra("rooms"));
        txtDescription.setText(getIntent().getStringExtra("description"));

        // Reservation button click
        btnReservation.setOnClickListener(v ->
                Toast.makeText(HouseDetailsActivity.this, "Reservation clicked", Toast.LENGTH_SHORT).show()
        );
    }
}
