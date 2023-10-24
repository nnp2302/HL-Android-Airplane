package com.example.airplane_android.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
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
import com.example.airplane_android.admin.model.Trip;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class TripEditActivity extends AppCompatActivity {
    final Calendar myCalendarEditStart = Calendar.getInstance();
    final Calendar myCalendarEditEnd = Calendar.getInstance();
    private TimePickerDialog.OnTimeSetListener timeEdit;
    private TimePickerDialog.OnTimeSetListener timeEndEdit;
    EditText fromEdit,toEdit,startDateEdit,endDateEdit
            ,priceBusinessEdit,priceEconomyEdit,businessTicketEdit,economyTicketEdit;
    Spinner spinnerEditPlane;
    ArrayList<String> spinnerEditList;
    FirebaseFirestore firestore;
    String selectedEditPlane;
    Button backEditTripToList,submitEditTrip;
    private TripAdapter tripAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_edit);
        startDateEdit= findViewById(R.id.textSuaBatDau);
        endDateEdit= findViewById(R.id.textSuaKetThuc);
        selectDataDate();
        backEditTripToList = findViewById(R.id.backEditTripToList);
        submitEditTrip = findViewById(R.id.submitEditTrip);
        backEditTripToList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToList = new Intent(TripEditActivity.this,TripActivity.class);
                startActivity(intentToList);
            }
        });

        firestore = FirebaseFirestore.getInstance();
        CollectionReference planeRef = firestore.collection("Plane");

        Bundle bundle = getIntent().getExtras();
        Trip trip = (Trip) bundle.get("trip");
        if(bundle !=null){
            fromEdit= findViewById(R.id.textSuaNoiDi);
            toEdit= findViewById(R.id.textSuaNoiDen);

            priceBusinessEdit= findViewById(R.id.textSuaGiaBusiness);
            priceEconomyEdit= findViewById(R.id.textSuaGiaEconomy);
            businessTicketEdit= findViewById(R.id.textSuaSoLuongBusiness);
            economyTicketEdit= findViewById(R.id.textSuaSoLuongEconomy);

            spinnerEditPlane = findViewById(R.id.listEditPlane);

            fromEdit.setText(trip.getFrom());
            toEdit.setText(trip.getTo());
            startDateEdit.setText(trip.getStart());
            endDateEdit.setText(trip.getEnd());
            priceBusinessEdit.setText(trip.getBusinessPrice().toString());
            priceEconomyEdit.setText(trip.getEconomyPrice().toString());
            businessTicketEdit.setText(trip.getBusinessTicket().toString());
            economyTicketEdit.setText(trip.getEconomyTicket().toString());

            // handle spinner edit

            ArrayList<String> planeIds = new ArrayList<>();


            // Create an ArrayAdapter for the spinner
            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, planeIds);
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerEditPlane.setAdapter(spinnerAdapter);


            String idPlane = trip.getPlaneId();
            int selectedPosition = planeIds.indexOf(idPlane);
            if (idPlane != null) {

                if (selectedPosition != -1) {
                    spinnerEditPlane.setSelection(selectedPosition);
                } else {
                    // Handle the case where the idPlane doesn't match any item in the spinner

                }
            }

            spinnerEditList = new ArrayList<>();
            planeRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String planeName = document.getString("Id");
                        spinnerEditList.add(planeName);
                    }

                    populateEditSpinner(spinnerEditList,trip);
                } else {
                    // Handle the error
                }
            });

            // selected value in spinner
            spinnerEditPlane.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    selectedEditPlane = (String) parent.getItemAtPosition(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            submitEditTrip.setOnClickListener(new View.OnClickListener() {
//                fromEdit= findViewById(R.id.textSuaNoiDi);
//                toEdit= findViewById(R.id.textSuaNoiDen);
//                startDateEdit= findViewById(R.id.textSuaBatDau);
//                endDateEdit= findViewById(R.id.textSuaKetThuc);
//                priceBusinessEdit= findViewById(R.id.textSuaGiaBusiness);
//                priceEconomyEdit= findViewById(R.id.textSuaGiaEconomy);
//                businessTicketEdit= findViewById(R.id.textSuaSoLuongBusiness);
//                economyTicketEdit= findViewById(R.id.textSuaSoLuongEconomy);
                @Override
                public void onClick(View v) {
                    String id = trip.getId().toString();
                    String fromEditSubmit = fromEdit.getText().toString().trim();
                    String toEditSubmit = toEdit.getText().toString().trim();
                    String startDateEditSubmit = startDateEdit.getText().toString().trim();
                    String endDateEditSubmit = endDateEdit.getText().toString().trim();
                    String priceBusinessEditSubmit = priceBusinessEdit.getText().toString().trim();
                    String priceEconomyEditSubmit = priceEconomyEdit.getText().toString().trim();
                    String businessTicketEditSubmit = businessTicketEdit.getText().toString().trim();
                    String economyTicketEditSubmit = economyTicketEdit.getText().toString().trim();

                    UpdateData(id,fromEditSubmit,toEditSubmit,startDateEditSubmit,endDateEditSubmit
                        ,priceBusinessEditSubmit,priceEconomyEditSubmit,businessTicketEditSubmit,economyTicketEditSubmit,
                            selectedEditPlane
                    );
                }
            });
        }
    }

    private void selectDataDate() {
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendarEditStart.set(Calendar.YEAR,year);
                myCalendarEditStart.set(Calendar.MONTH,month);
                myCalendarEditStart.set(Calendar.DAY_OF_MONTH,day);

                showTimePickerStartEdit();
            }
        };

        DatePickerDialog.OnDateSetListener dateEnd = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendarEditEnd.set(Calendar.YEAR,year);
                myCalendarEditEnd.set(Calendar.MONTH,month);
                myCalendarEditEnd.set(Calendar.DAY_OF_MONTH,day);

                showTimePickerEndEdit();
            }
        };
        timeEdit = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                myCalendarEditStart.set(Calendar.HOUR_OF_DAY, hourOfDay);
                myCalendarEditStart.set(Calendar.MINUTE, minute);
                startDateEdit.setText(displayDataDate(myCalendarEditStart));
            }
        };

        // dateEnd
        timeEndEdit = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                myCalendarEditEnd.set(Calendar.HOUR_OF_DAY, hourOfDay);
                myCalendarEditEnd.set(Calendar.MINUTE, minute);
                endDateEdit.setText(displayDataDate(myCalendarEditEnd));
            }
        };
        startDateEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(
                        TripEditActivity.this,date,myCalendarEditStart.get(Calendar.YEAR),myCalendarEditStart.get(Calendar.MONTH),myCalendarEditStart.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        endDateEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(
                        TripEditActivity.this,dateEnd,myCalendarEditEnd.get(Calendar.YEAR),myCalendarEditEnd.get(Calendar.MONTH),myCalendarEditEnd.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void showTimePickerStartEdit() {
        new TimePickerDialog(
                TripEditActivity.this, timeEdit, myCalendarEditStart.get(Calendar.HOUR_OF_DAY), myCalendarEditStart.get(Calendar.MINUTE), true
        ).show();
    }

    private void showTimePickerEndEdit() {
        new TimePickerDialog(
                TripEditActivity.this, timeEndEdit, myCalendarEditEnd.get(Calendar.HOUR_OF_DAY), myCalendarEditEnd.get(Calendar.MINUTE), true
        ).show();
    }


    private String displayDataDate(Calendar value){
        String myFormat = "dd-MM-yyyy HH:mm";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
        return dateFormat.format(value.getTime());
    }

    private void UpdateData(String id, String fromEditSubmit, String toEditSubmit, String startDateEditSubmit, String endDateEditSubmit,
                            String priceBusinessEditSubmit, String priceEconomyEditSubmit, String businessTicketEditSubmit, String economyTicketEditSubmit,
                            String selectedEditPlane
    ) {
        firestore.collection("Trip")
                .document(id)
                .update("Id",id,
                        "From",fromEditSubmit,
                        "To",toEditSubmit,
                        "Start",startDateEditSubmit,
                        "End",endDateEditSubmit,
                        "BusinessPrice",Integer.parseInt(priceBusinessEditSubmit),
                        "EconomyPrice",Integer.parseInt(priceEconomyEditSubmit),
                        "BusinessTicket",Integer.parseInt(businessTicketEditSubmit),
                        "EconomyTicket",Integer.parseInt(economyTicketEditSubmit),
                        "PlaneId",selectedEditPlane
                ).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(TripEditActivity.this,"Cập nhật thành công!!",Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }
    private void populateEditSpinner(ArrayList<String> spinnerEditList,Trip trip) {
        Spinner spinner = findViewById(R.id.listEditPlane);

        // Create an ArrayAdapter with the data
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerEditList);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        String idPlane = trip.getPlaneId();
        if (idPlane != null) {
            int selectedPosition = spinnerEditList.indexOf(idPlane);
            if (selectedPosition != -1) {
                spinnerEditPlane.setSelection(selectedPosition);
            } else {
                // Handle the case where the idPlane doesn't match any item in the spinner
            }
        }
    }
}