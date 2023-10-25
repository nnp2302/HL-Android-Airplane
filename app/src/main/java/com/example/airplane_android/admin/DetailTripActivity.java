package com.example.airplane_android.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.airplane_android.R;
import com.example.airplane_android.admin.model.Trip;

public class DetailTripActivity extends AppCompatActivity {
    TextView textFrom,textTo,textStart,textEnd,textEconomyPrice,textBusinessPrice,textEconomyTicket,textBusinessTicket,textSelectedPlane;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_trip);
        textFrom = findViewById(R.id.textTripDetailFrom);
        textTo = findViewById(R.id.textTripDetailTo);
        textStart = findViewById(R.id.textTripDetailStart);
        textEnd = findViewById(R.id.textTripDetailEnd);
        textEconomyPrice = findViewById(R.id.textTripDetailEconomyPrice);
        textBusinessPrice = findViewById(R.id.textTripDetailBusinessPrice);
        textEconomyTicket = findViewById(R.id.textTripDetailEconomyTicket);
        textBusinessTicket = findViewById(R.id.textTripDetailBusinessTicket);
        textSelectedPlane = findViewById(R.id.textTripDetailPlaneSelect);


        Bundle bundle = getIntent().getExtras();
        Trip trip = (Trip) bundle.get("trip");
        textFrom.setText(trip.getFrom());
        textTo.setText(trip.getTo());
        textStart.setText(trip.getStart());
        textEnd.setText(trip.getEnd());
        textEconomyPrice.setText(trip.getEconomyPrice().toString());
        textBusinessPrice.setText(trip.getBusinessPrice().toString());
        textEconomyTicket.setText(trip.getEconomyTicket().toString());
        textBusinessTicket.setText(trip.getBusinessTicket().toString());
        textSelectedPlane.setText(trip.getPlaneId());
    }
}