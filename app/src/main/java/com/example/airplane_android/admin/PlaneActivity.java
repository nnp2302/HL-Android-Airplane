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
import android.view.View;

import com.example.airplane_android.R;
import com.example.airplane_android.admin.model.adapter.PlaneAdapter;
import com.example.airplane_android.admin.model.Plane;
import com.example.airplane_android.models.TicketModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class PlaneActivity extends AppCompatActivity {
    FirebaseFirestore firestore;
    private RecyclerView rcvPlaneView;
    private PlaneAdapter planeAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plane);
        firestore = FirebaseFirestore.getInstance();
        rcvPlaneView = findViewById(R.id.idPlaneItem);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvPlaneView.setLayoutManager(linearLayoutManager);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rcvPlaneView.addItemDecoration(itemDecoration);

        planeAdapter = new PlaneAdapter(GetAllPlane(),this);
        rcvPlaneView.setAdapter(planeAdapter);

        FloatingActionButton fab = findViewById(R.id.floatingActionButtonPlane);

        // Set an OnClickListener for the floating action button
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create an Intent to start the new activity
                Context context = view.getContext();
                Intent intent = new Intent(context, PlaneAddActivity.class);
                // Start the new activity
                context.startActivity(intent);
            }
        });
    }

    private List<Plane> GetAllPlane() {
        List<Plane> plane = new ArrayList<>();
        firestore.collection("Plane").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()) {
                    Plane viewAllModel = documentSnapshot.toObject(Plane.class);
                    plane.add(viewAllModel);
                    planeAdapter.notifyDataSetChanged();

                }
            }
        });
        return plane;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(planeAdapter != null){
            planeAdapter.release();
        }
    }

}