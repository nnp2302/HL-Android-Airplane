package com.example.airplane_android.admin;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.airplane_android.R;
import com.example.airplane_android.adapters.TicketHistoryAdapter;
import com.example.airplane_android.admin.adapter.BillAdapter;
import com.example.airplane_android.admin.model.BillModel;
import com.example.airplane_android.models.TicketModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class BillActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    FirebaseFirestore firestore;
    FirebaseAuth auth;
    private List<BillModel> billModelList;
    private BillAdapter adapter;
    ImageView backBtn;
    QuerySnapshot a;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView = findViewById(R.id.bill_list);
        billModelList = new ArrayList<>();
        backBtn = findViewById(R.id.bill_back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        firestore.collection("Bill")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot data = task.getResult();
                        if(data!=null){
                            for (QueryDocumentSnapshot document : data) {
                                BillModel bill = BillModel.fromDocument(document);
                                bill.setTicketCode(document.getId());
                                billModelList.add(bill);
                            }

                            recyclerView.setLayoutManager(linearLayoutManager);
                            RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
                            recyclerView.addItemDecoration(itemDecoration);
                            adapter = new BillAdapter(billModelList, this);
                            recyclerView.setAdapter(adapter);
                        }

                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());

                    }
                });


    }
}