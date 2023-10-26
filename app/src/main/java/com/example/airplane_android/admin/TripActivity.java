package com.example.airplane_android.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.airplane_android.R;
import com.example.airplane_android.admin.adapter.TripAdapter;
import com.example.airplane_android.admin.model.Trip;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TripActivity extends AppCompatActivity {
    FirebaseFirestore firestore;
    private RecyclerView rcvTripView;
    private TripAdapter tripAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip);
        firestore = FirebaseFirestore.getInstance();
        rcvTripView = findViewById(R.id.idTripItem);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvTripView.setLayoutManager(linearLayoutManager);

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rcvTripView.addItemDecoration(itemDecoration);

        tripAdapter = new TripAdapter(GetAllTrip(),this);
        rcvTripView.setAdapter(tripAdapter);

        FloatingActionButton fab = findViewById(R.id.floatingActionButtonTrip);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Context context = view.getContext();
                Intent intent = new Intent(context, TripAddActivity.class);
                context.startActivity(intent);
            }
        });
    }

    private List<Trip> GetAllTrip() {
        List<Trip> trip = new ArrayList<>();
        firestore.collection("Trip").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()) {
                    Trip viewAllModel = documentSnapshot.toObject(Trip.class);
                    trip.add(viewAllModel);
                    tripAdapter.notifyDataSetChanged();
                    Log.d("Firestore", "Document data: " + documentSnapshot.getData().toString());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("Firestore", "Error retrieving document: " + e.getMessage());

            }
        });
        return trip;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(tripAdapter != null){
            tripAdapter.release();
        }
    }
}