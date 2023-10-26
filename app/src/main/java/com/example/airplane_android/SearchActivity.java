package com.example.airplane_android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.airplane_android.admin.model.Trip;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

  Toolbar toolbar;
  ImageButton btnBack;
  TextView txtToolbarTitle, txtToolbarSubTitle;
  ConstraintLayout mainHome;

  Intent getIntent;
  String fromDes, toDes, dateFlight, amount, typeChair;

  @SuppressLint("MissingInflatedId")
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_search);

    mainHome = findViewById(R.id.mainHome);
    toolbar = findViewById(R.id.toolbar);
    txtToolbarTitle = findViewById(R.id.toolbarTitle);
    txtToolbarSubTitle = findViewById(R.id.toolbarSubTitle);
    btnBack = findViewById(R.id.toolbarBack);

    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayShowTitleEnabled(false);

    getIntent = getIntent();
    fromDes = getIntent.getStringExtra("fromDes");
    toDes = getIntent.getStringExtra("toDes");
    dateFlight = getIntent.getStringExtra("date");
    amount = getIntent.getStringExtra("amount");
    typeChair = getIntent.getStringExtra("typeChair");

    txtToolbarTitle.setText(fromDes + " -> " + toDes);
    txtToolbarSubTitle.setText(dateFlight + " - " + amount + " hành khách - " + typeChair);
  }

  @Override
  protected void onStart() {
    super.onStart();

    FirebaseFirestore fireStore = FirebaseFirestore.getInstance();
    fireStore.collection("Trip").get()
        .addOnSuccessListener(res -> {
          List<Trip> tripList = new ArrayList<>();
          for (QueryDocumentSnapshot snapshot : res) {
            String convertDateStr = dateFlight.replace("/", "-");

            final String fromSnapshot = snapshot.getData().get("From").toString();
            final String toSnapshot = snapshot.getData().get("To").toString();
            final String startDateSnapshot = snapshot.getData().get("Start").toString();
            final String businessPriceSnapshot = snapshot.getData().get("BusinessPrice").toString();
            final String businessTicketSnapshot = snapshot.getData().get("BusinessTicket").toString();
            final String economyPriceSnapshot = snapshot.getData().get("EconomyPrice").toString();
            final String economyTicketSnapshot = snapshot.getData().get("EconomyTicket").toString();
            final String endDateSnapshot = snapshot.getData().get("End").toString();
            final String planeSnapshot = snapshot.getData().get("PlaneId").toString();
            final String idSnapshot = snapshot.getData().get("Id").toString();

            final String[] startDateSnapshotSplit = startDateSnapshot.split(" ");
            final String[] endDateSnapshotSplit = endDateSnapshot.split(" ");
            final Date convertDate = convertDateString(convertDateStr);
            final Date startDateSnapshotConvert = convertDateString(startDateSnapshotSplit[0]);
            final Date endDateSnapshotConvert = convertDateString(endDateSnapshotSplit[0]);

            if (fromSnapshot.toLowerCase().contains(fromDes.toLowerCase())
                && toSnapshot.toLowerCase().contains(toDes.toLowerCase())
                && convertDate.getTime() == startDateSnapshotConvert.getTime())
              tripList.add(new Trip(
                  fromSnapshot,
                  toSnapshot,
                  businessPriceSnapshot,
                  economyPriceSnapshot,
                  businessTicketSnapshot,
                  economyTicketSnapshot,
                  startDateSnapshotConvert,
                  endDateSnapshotConvert,
                  new Date(),
                  planeSnapshot
              ));
          }
        })
        .addOnFailureListener(e -> {
          Log.e("Load FireStore data", "Failed to query trip FireStore data, log: " + e);
          Snackbar.make(mainHome, "Có lỗi trong quá trình xử lý", Snackbar.LENGTH_SHORT).show();
        });

    btnBack.setOnClickListener(v -> finish());
  }

  private Date convertDateString(String dateStr) {
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    try {
      return dateFormat.parse(dateStr);
    } catch (ParseException e) {
      throw new RuntimeException(e);
    }
  }
}