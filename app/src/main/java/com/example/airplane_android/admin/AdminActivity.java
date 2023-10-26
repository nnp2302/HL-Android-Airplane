package com.example.airplane_android.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.example.airplane_android.MainActivity;
import com.example.airplane_android.R;

public class AdminActivity extends AppCompatActivity implements View.OnClickListener {
    CardView cardPlane,cardTrip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        cardPlane = findViewById(R.id.cardplane);
        cardTrip = findViewById(R.id.cardtrip);
        cardPlane.setOnClickListener(this);
        cardTrip.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cardplane:
                Intent intent = new Intent(AdminActivity.this,PlaneActivity.class);
                startActivity(intent);
                break;
            case R.id.cardtrip:
                Intent intentTrip = new Intent(AdminActivity.this,TripActivity.class);
                startActivity(intentTrip);
                break;
        }
    }
}