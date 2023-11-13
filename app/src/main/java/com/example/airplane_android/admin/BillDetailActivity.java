package com.example.airplane_android.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.airplane_android.R;
import com.example.airplane_android.admin.model.BillModel;
import com.example.airplane_android.models.TicketModel;
import com.example.airplane_android.razorpay.PaymentActivity;
import com.example.airplane_android.utils.TicketConstants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.util.Map;

public class BillDetailActivity extends AppCompatActivity{
    ImageView backBtn;
    TextView code,name,dob,from,to,start,end,price,type,payment_status,payment_method;
    Button purchase,cancel;
    FirebaseFirestore firestore;
    FirebaseAuth auth;
    String billCode;
    static BillModel bill;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_detail);
        Init();
        getIntentValues();
        getBillByCode(billCode);
        backBtn.setOnClickListener(v -> finish());
    }
    private void getIntentValues(){
        Intent i = getIntent();
        billCode = i.getStringExtra("code");
    }

    private void updateData(){
        if(bill!=null){
            code.setText(bill.getTicketCode());
            name.setText(bill.getFullname());
            dob.setText(bill.getDob());
            from.setText(bill.getFrom());
            to.setText(bill.getTo());
            start.setText(bill.getStart());
            end.setText(bill.getEnd());
            price.setText(bill.getPrice()+"");
            type.setText((bill.isBusiness())? TicketConstants.BUSSINESS:TicketConstants.ECONOMIC);
            if(bill.getPurchaseStatus()){
                payment_status.setText(TicketConstants.PURCHASED);
            }else{
                payment_status.setText(TicketConstants.UN_PURCHASED);
            }
            payment_method.setText(bill.getPaymentMethod());

        }
    }
    private void getBillByCode(String code){
        DocumentReference reference = firestore.collection("Bill").document(code);
        reference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    bill = BillModel.fromDocument(document);
                    bill.setTicketCode(document.getId());
                    updateData();
                }
            }
        });
    }
























    //______________________________________INITIALIZE________________________________________
    private void Init(){
        InitTextView();
        InitButton();
        InitFirebase();
    }
    private void InitFirebase(){
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
    }
    private void InitTextView(){
        code = findViewById(R.id.bill_detail_code);
        name = findViewById(R.id.bill_detail_name);
        dob = findViewById(R.id.bill_detail_dob);
        from = findViewById(R.id.bill_detail_from);
        to = findViewById(R.id.bill_detail_to);
        start =findViewById(R.id.bill_detail_start);
        end = findViewById(R.id.bill_detail_end);
        price = findViewById(R.id.bill_detail_price);
        type = findViewById(R.id.bill_detail_type);
        payment_status = findViewById(R.id.bill_detail_payment_status);
        payment_method = findViewById(R.id.bill_detail_payment_method);
    }
    private void InitButton(){
        backBtn = findViewById(R.id.bill_detail_back_btn);
    }

    //______________________________________DESTROY________________________________________
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}