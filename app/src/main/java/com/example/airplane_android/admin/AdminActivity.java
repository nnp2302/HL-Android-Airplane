package com.example.airplane_android.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.airplane_android.LoginActivity;
import com.example.airplane_android.MainActivity;
import com.example.airplane_android.R;
import com.google.firebase.auth.FirebaseAuth;

public class AdminActivity extends AppCompatActivity implements View.OnClickListener {
    CardView cardPlane,cardTrip,cardBill;
    LinearLayout logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        cardPlane = findViewById(R.id.cardplane);
        cardTrip = findViewById(R.id.cardtrip);
        cardBill = findViewById(R.id.cardbill);
        cardBill.setOnClickListener(this);
        cardPlane.setOnClickListener(this);
        cardTrip.setOnClickListener(this);
        logout = findViewById(R.id.admin_logout_btn);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(AdminActivity.this, LoginActivity.class));
            }
        });

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
            case R.id.cardbill:
                Intent intentBill = new Intent(AdminActivity.this,BillActivity.class);
                startActivity(intentBill);
                break;
            case R.id.carduser:
                Toast.makeText(AdminActivity.this,"Comming soon...",Toast.LENGTH_LONG);
                break;
        }
    }
}