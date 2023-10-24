package com.example.airplane_android.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.airplane_android.R;
import com.example.airplane_android.admin.model.Trip;

public class DetailTripActivity extends AppCompatActivity {
    TextView textIdTripDetails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_trip);
        textIdTripDetails = findViewById(R.id.textIdTripDetail);
        Bundle bundle = getIntent().getExtras();
        Trip trip = (Trip) bundle.get("trip");
        textIdTripDetails.setText(trip.getId());
    }
}