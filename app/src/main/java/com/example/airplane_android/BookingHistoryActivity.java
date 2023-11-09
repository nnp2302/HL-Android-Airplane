package com.example.airplane_android;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.airplane_android.adapters.TicketHistoryAdapter;
import com.example.airplane_android.models.TicketModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BookingHistoryActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    FirebaseFirestore firestore;
    FirebaseAuth auth;
    private List<TicketModel> ticketModelList;
    private TicketHistoryAdapter adapter;
    QuerySnapshot a;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_history);
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView = findViewById(R.id.history_ticket_list);
        ticketModelList = new ArrayList<>();
        String uid = auth.getUid();
        firestore.collection("Users/"+uid+"/tickets")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            TicketModel ticket = TicketModel.fromDocument(document);
                            ticket.setTicketCode(document.getId());
                            ticketModelList.add(ticket);
                        }

                        recyclerView.setLayoutManager(linearLayoutManager);
                        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
                        recyclerView.addItemDecoration(itemDecoration);
                        adapter = new TicketHistoryAdapter(ticketModelList);
                        recyclerView.setAdapter(adapter);
                    } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());

                    }
                });


    }
}