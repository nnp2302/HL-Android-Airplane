package com.example.airplane_android.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.airplane_android.R;
import com.example.airplane_android.admin.adapter.TripAdapter;
import com.example.airplane_android.admin.model.Plane;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class TripAddActivity extends AppCompatActivity {
    final Calendar myCalendarStart = Calendar.getInstance();
    final Calendar myCalendarEnd = Calendar.getInstance();
    private TimePickerDialog.OnTimeSetListener time;
    private TimePickerDialog.OnTimeSetListener timeEnd;
    private EditText startDate,endDate,from,to,priceEconomy,priceBusiness,economyTicket,businessTicket;
    Spinner spinner;
    ArrayList<String> spinnerList;
    ArrayAdapter<String> adapter;
    Button btnSubmitTrip;
    String selectedPlane;
    FirebaseFirestore firestore;
    private TripAdapter tripAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_add);
        firestore = FirebaseFirestore.getInstance();
        CollectionReference planeRef = firestore.collection("Plane");
        spinner = findViewById(R.id.listPlane);
        //
        from = findViewById(R.id.textThemNoiDi);
        to = findViewById(R.id.textThemNoiDen);
        startDate = findViewById(R.id.textThemBatDau);
        endDate = findViewById(R.id.textThemKetThuc);
        priceBusiness = findViewById(R.id.textThemGiaBusiness);
        priceEconomy = findViewById(R.id.textThemGiaEconomy);
        businessTicket = findViewById(R.id.textSoLuongBusiness);
        economyTicket = findViewById(R.id.textSoLuongEconomy);
        //
        btnSubmitTrip = findViewById(R.id.submitAddTrip);
        // call calendar
        selectDataDate();
        // dropdown items select plane
        spinnerList = new ArrayList<>();
        planeRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    boolean planeActive = document.getBoolean("active");
                    if(planeActive){
                        String planeName = document.getString("Id");
                        spinnerList.add(planeName);
                    }
                }
                populateSpinner(spinnerList);
            } else {
            }
        });

        // selected value in spinner
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedPlane = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btnSubmitTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textBatDau = startDate.getText().toString().trim();
                String textKetThuc = endDate.getText().toString().trim();
                String textNoiDi = from.getText().toString().trim();
                String textNoiDen = to.getText().toString().trim();
                String textGiaVeEconomy = priceEconomy.getText().toString().trim();
                String textGiaVeBusiness = priceBusiness.getText().toString().trim();
                String textSLeconomy = economyTicket.getText().toString().trim();
                String textSLbusiness = businessTicket.getText().toString().trim();
                uploadData(textBatDau,textKetThuc,textNoiDi,textNoiDen,textGiaVeEconomy,textGiaVeBusiness,textSLeconomy,textSLbusiness,selectedPlane);
            }
        });
    }

    private void uploadData(String textBatDau,
                            String textKetThuc,
                            String textNoiDi,
                            String textNoiDen,
                            String textGiaVeEconomy,
                            String textGiaVeBusiness,
                            String textSLeconomy,
                            String textSLbusiness,
                            String selectedPlane) {

        Map<String, Object> doc = new HashMap<>();
        String id = UUID.randomUUID().toString();
        doc.put("Id",id);
        doc.put("From",textNoiDi);
        doc.put("To",textNoiDen);
        doc.put("Start",textBatDau);
        doc.put("End",textKetThuc);
        doc.put("EconomyPrice",Integer.parseInt(textGiaVeEconomy));
        doc.put("BusinessPrice",Integer.parseInt(textGiaVeBusiness));
        doc.put("EconomyTicket",Integer.parseInt(textSLeconomy));
        doc.put("BusinessTicket",Integer.parseInt(textSLbusiness));
        doc.put("PlaneId",selectedPlane);
        firestore.collection("Trip").document(id)
                .set(doc)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(TripAddActivity.this,"Tạo Mới Thành Công",Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void populateSpinner(ArrayList<String> spinnerList) {
        Spinner spinner = findViewById(R.id.listPlane);

        // Create an ArrayAdapter with the data
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerList);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
    }
    private void selectDataDate(){
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendarStart.set(Calendar.YEAR,year);
                myCalendarStart.set(Calendar.MONTH,month);
                myCalendarStart.set(Calendar.DAY_OF_MONTH,day);

                showTimePickerStart();
            }
        };

        DatePickerDialog.OnDateSetListener dateEnd = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendarEnd.set(Calendar.YEAR,year);
                myCalendarEnd.set(Calendar.MONTH,month);
                myCalendarEnd.set(Calendar.DAY_OF_MONTH,day);

                showTimePickerEnd();
            }
        };
        time = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                myCalendarStart.set(Calendar.HOUR_OF_DAY, hourOfDay);
                myCalendarStart.set(Calendar.MINUTE, minute);
                startDate.setText(displayDataDate(myCalendarStart));
            }
        };

        // dateEnd
        timeEnd = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                myCalendarEnd.set(Calendar.HOUR_OF_DAY, hourOfDay);
                myCalendarEnd.set(Calendar.MINUTE, minute);
                endDate.setText(displayDataDate(myCalendarEnd));
            }
        };
        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  new DatePickerDialog(
                        TripAddActivity.this,date,myCalendarStart.get(Calendar.YEAR),myCalendarStart.get(Calendar.MONTH),myCalendarStart.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(
                        TripAddActivity.this,dateEnd,myCalendarEnd.get(Calendar.YEAR),myCalendarEnd.get(Calendar.MONTH),myCalendarEnd.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }



    private void showTimePickerStart() {
        new TimePickerDialog(
                TripAddActivity.this, time, myCalendarStart.get(Calendar.HOUR_OF_DAY), myCalendarStart.get(Calendar.MINUTE), true
        ).show();
    }

    private void showTimePickerEnd() {
        new TimePickerDialog(
                TripAddActivity.this, timeEnd, myCalendarEnd.get(Calendar.HOUR_OF_DAY), myCalendarEnd.get(Calendar.MINUTE), true
        ).show();
    }


    private String displayDataDate(Calendar value){
        String myFormat = "dd-MM-yyyy HH:mm";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
        return dateFormat.format(value.getTime());
    }

}