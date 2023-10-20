package com.example.airplane_android.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.airplane_android.R;
import com.example.airplane_android.admin.model.Plane;

public class PlaneEditActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plane_edit);

        Bundle bundle = getIntent().getExtras();
        if(bundle ==null) return;

        Plane plane = (Plane) bundle.get("plane");
        TextView txtview = findViewById(R.id.textViewEdit);
        txtview.setText(plane.getId());

    }
}