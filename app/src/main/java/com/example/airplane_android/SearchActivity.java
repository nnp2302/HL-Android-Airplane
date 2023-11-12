package com.example.airplane_android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.airplane_android.adapters.TripAdapter;
import com.example.airplane_android.admin.model.Trip;
import com.example.airplane_android.models.UserTrip;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class SearchActivity extends AppCompatActivity {

  Toolbar toolbar;
  ImageButton btnBack;
  TextView txtToolbarTitle, txtToolbarSubTitle, txtNotFound;
  ListView lstSearchView;
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
    txtNotFound = findViewById(R.id.serach_not_found);
    lstSearchView = findViewById(R.id.lstSearch);
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
              List<UserTrip> tripList = new ArrayList<>();
              for (QueryDocumentSnapshot snapshot : res) {
                String convertDateStr = dateFlight.replace("/", "-");

                final String idSnapshot = snapshot.getData().get("Id").toString();
                final String planeSnapshot = snapshot.getData().get("PlaneId").toString();

                final String fromSnapshot = snapshot.getData().get("From").toString();
                final String toSnapshot = snapshot.getData().get("To").toString();
                final String startDateSnapshot = snapshot.getData().get("Start").toString();
                final String endDateSnapshot = snapshot.getData().get("End").toString();

                final String priceSnapshot;
                if (typeChair.equals("Economy"))
                  priceSnapshot = snapshot.getData().get("EconomyPrice").toString();
                else priceSnapshot = snapshot.getData().get("BusinessPrice").toString();

                final String amountSnapshot;
                if (typeChair.equals("Economy"))
                  amountSnapshot = snapshot.getData().get("EconomyTicket").toString();
                else amountSnapshot = snapshot.getData().get("BusinessTicket").toString();

                String estimateTime = calculateEstimateTime(startDateSnapshot, endDateSnapshot);

                final String[] startDateSnapshotSplit = startDateSnapshot.split(" ");
                final String[] endDateSnapshotSplit = endDateSnapshot.split(" ");
                final Date convertDate = convertDateString(convertDateStr);
                final Date startDateSnapshotConvert = convertDateString(startDateSnapshotSplit[0]);
                final Date endDateSnapshotConvert = convertDateString(endDateSnapshotSplit[0]);

                if (fromSnapshot.toLowerCase().contains(fromDes.toLowerCase())
                        && toSnapshot.toLowerCase().contains(toDes.toLowerCase())
                        && convertDate.getTime() == startDateSnapshotConvert.getTime()
                        && (Integer.parseInt(amountSnapshot) - Integer.parseInt(amount)) > 0)
                  tripList.add(new UserTrip(
                          idSnapshot,
                          fromSnapshot,
                          toSnapshot,
                          Integer.parseInt(priceSnapshot),
                          startDateSnapshot,
                          endDateSnapshot,
                          estimateTime,
                          planeSnapshot
                  ));
              }


              if (tripList.size() > 0) {
                txtNotFound.setVisibility(View.INVISIBLE);
                TripAdapter adapter = new TripAdapter(this, tripList);
                lstSearchView.setAdapter(adapter);
              } else
                lstSearchView.setVisibility(View.INVISIBLE);
            })
            .addOnFailureListener(e -> {
              Log.e("Load FireStore data", "Failed to query trip FireStore data, log: " + e);
              Snackbar.make(mainHome, "Có lỗi trong quá trình xử lý", Snackbar.LENGTH_SHORT).show();
            });
    lstSearchView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(FirebaseAuth.getInstance().getCurrentUser()==null){
          Toast.makeText(SearchActivity.this, "Đăng nhập để đặt vé", Toast.LENGTH_SHORT).show();
        }else{
          UserTrip trip = (UserTrip) parent.getItemAtPosition(position);
          Intent intent = new Intent(SearchActivity.this, BookingActivity.class);
          intent.putExtra("tripId",trip.getId());
          startActivity(intent);
        }

      }
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
  private String calculateEstimateTime(String startTime, String endTime) {
    final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.ENGLISH);

    Date convertTimeStart = null, convertTimeEnd = null;
    try {
      convertTimeStart = dateFormat.parse(startTime);
      convertTimeEnd = dateFormat.parse(endTime);
    } catch (Exception e) {
      Log.e("Convert date time", "Failed to convert string time to date, log: " + e);
    }

    long estimateTime = convertTimeEnd.getTime() - convertTimeStart.getTime();
    long hours = TimeUnit.MILLISECONDS.toHours(estimateTime);
    long minutes = TimeUnit.MILLISECONDS.toMinutes(estimateTime - TimeUnit.HOURS.toMillis(hours));
    long seconds = TimeUnit.MILLISECONDS.toSeconds(estimateTime - TimeUnit.HOURS.toMillis(hours) - TimeUnit.MINUTES.toMillis(minutes));

    String formattedTime = "";
    if (hours > 0)
      formattedTime = hours + "h" + minutes + "m";
    else if (minutes > 0)
      formattedTime = minutes + "m" + seconds + "s";
    else
      formattedTime = seconds + "s";
    return formattedTime;
  }
}