package com.example.airplane_android.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.example.airplane_android.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class TripAddActivity extends AppCompatActivity {
    final Calendar myCalendarStart = Calendar.getInstance();
    final Calendar myCalendarEnd = Calendar.getInstance();
    private TimePickerDialog.OnTimeSetListener time;
    private TimePickerDialog.OnTimeSetListener timeEnd;
    private EditText startDate,endDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_add);

        startDate = findViewById(R.id.textThemBatDau);
        endDate = findViewById(R.id.textThemKetThuc);
        selectDataDate();
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
        String myFormat = "dd-MM-yyyy HH:mm:ss";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
        return dateFormat.format(value.getTime());
    }

}