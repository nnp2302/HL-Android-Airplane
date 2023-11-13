package com.example.airplane_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.airplane_android.models.UserTrip;
import com.example.airplane_android.utils.TicketConstants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class BookingActivity extends AppCompatActivity {
    UserTrip userTrip;
    TextView plane,from,to,start,end,price,estimate;
    Button submit;
    CheckBox isBusiness;
    EditText qty;
    FirebaseFirestore firestore;
    FirebaseAuth auth;
    Integer bPrice,ePrice;
    EditText fullname;
    TextInputEditText dob;
    ImageView backBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        plane = findViewById(R.id.booking_plane_id);
        from = findViewById(R.id.booking_from);
        to = findViewById(R.id.booking_to);
        start = findViewById(R.id.booking_start);
        end = findViewById(R.id.booking_end);
        price = findViewById(R.id.booking_price);
        estimate = findViewById(R.id.booking_estimate);
        submit = findViewById(R.id.booking_submit_button);
        isBusiness = findViewById(R.id.booking_is_business);
        qty = findViewById(R.id.booking_quantity);
        fullname = findViewById(R.id.booking_fullname);
        dob  = findViewById(R.id.booking_dob);
        backBtn = findViewById(R.id.booking_back_btn);
        //Khoi tao firebase
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        //Lay id
        String id = getIntent().getStringExtra("tripId");
        //Lay data
        firestore.collection("Trip").document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        bPrice = document.getLong("BusinessPrice").intValue();
                        ePrice = document.getLong("EconomyPrice").intValue();
                        userTrip = UserTrip.fromDocument(document);
                        plane.setText(userTrip.getPlaneId());
                        from.setText(userTrip.getFrom());
                        to.setText(userTrip.getTo());
                        start.setText(userTrip.getStart());
                        end.setText(userTrip.getEnd());
                        estimate.setText(userTrip.getEstimateTime());
                    } else {
                        // Xu li khi fail
                    }
                } else {

                }
            }
        });
        backBtn.setOnClickListener(v -> finish());
        dob.setOnClickListener(v -> {
            MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Chọn ngày")
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    .setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR)
                    .setTextInputFormat(new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH))
                    .build();

            datePicker.show(getSupportFragmentManager(), "birthCalendar");

            datePicker.addOnPositiveButtonClickListener(selection -> {
                long selectedTimestamp = selection;

                // Kiểm tra nếu ngày được chọn nhỏ hơn ngày hiện tại
                if (selectedTimestamp < MaterialDatePicker.todayInUtcMilliseconds()) {
                    Date selectedDate = new Date(selectedTimestamp);
                    SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
                    dob.setText(formatDate.format(selectedDate));
                } else {
                    // Ngày chọn lớn hơn hoặc bằng ngày hiện tại, thông báo cho người dùng
                    Toast.makeText(getApplicationContext(), "Vui lòng chọn ngày sinh hợp lệ.", Toast.LENGTH_SHORT).show();
                }
            });
        });


        //Xử lý các sự kiện
            //Sự kiện thay đổi số lượng vé
        qty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    Integer qty = Integer.parseInt(s.toString());
                    if(isBusiness.isChecked()){
                        price.setText(qty*bPrice+"");
                    }else{
                        price.setText(qty*ePrice+"");
                    }
                }catch (Exception e){
                    price.setText("0");
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //Sự kiện thay đổi loại vé
        isBusiness.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                try {
                    Integer quantity = Integer.parseInt(qty.getText().toString());
                    if(isBusiness.isChecked()){
                        price.setText(quantity*bPrice+"");
                    }else{
                        price.setText(quantity*ePrice+"");
                    }
                }catch (Exception e){
                    price.setText("0");
                }
            }
        });
        //Sự kiện đặt vé
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkNull()){
                    Toast.makeText(BookingActivity.this,"Vui lòng nhập đúng định dạng!",Toast.LENGTH_LONG).show();
                }else{
                    String uid = auth.getUid();
                    Map<String,Object> data = new HashMap<>();
                    data.put("isBusiness",isBusiness.isChecked());
                    data.putAll(userTrip.toMap());
                    int quantity = Integer.parseInt(qty.getText().toString());
                    data.put("quantity", quantity);
                    data.put("purchaseStatus",false);
                    data.put("ticketStatus", TicketConstants.ACTIVE);
                    data.put("isCheckIn",false);
                    int priceValue =Integer.parseInt(price.getText().toString());
                    data.put("Price",priceValue);
                    data.put("fullname",fullname.getText().toString());
                    data.put("dob",dob.getText().toString().replace("/","-"));
                    long ticketCode =System.currentTimeMillis();
                    DocumentReference reference = firestore.collection("Users").document(uid+"/tickets/"+ticketCode);
                    reference.set(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isComplete()){
                                DocumentReference ref = firestore.collection("Bill").document(""+ticketCode);
                                data.remove("ticketStatus");
                                data.remove("isCheckIn");
                                data.put("paymentMethod","");
                                data.put("uid",uid);
                                data.put("email",auth.getCurrentUser().getEmail());
                                ref.set(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isComplete()){
                                            Intent intent = new Intent(BookingActivity.this, BookingHistoryActivity.class);
                                            startActivity(intent);
                                        }
                                    }
                                });
                            }
                        }
                    });
                }


            }
        });
    }
    private boolean checkNull(){
        if(dob.getText().toString().equals(""))
            return false;
        if(fullname.getText().toString().equals(""))
            return false;
        if(qty.getText().toString().equals(""))
            return false;
        else if(Integer.parseInt(qty.getText().toString())<1)
            return false;
        return true;
    }
    @Override
    protected void onStart() {
        super.onStart();
    }
}