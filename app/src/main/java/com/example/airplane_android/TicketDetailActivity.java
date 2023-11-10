package com.example.airplane_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.airplane_android.models.TicketModel;
import com.example.airplane_android.utils.TicketConstants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class TicketDetailActivity extends AppCompatActivity {
    ImageView backBtn;
    TextView code,name,dob,from,to,start,end,price,type,payment_status,status;
    Button purchase,cancel;
    FirebaseFirestore firestore;
    FirebaseAuth auth;
    String ticketCode;
    static TicketModel ticket;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_detail);
        Init();
        getIntentValues();
        getTicketByTicketCode(ticketCode);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelTicketByCode(ticket.getTicketCode());
            }
        });
        backBtn.setOnClickListener(v -> finish());
    }
    private void getIntentValues(){
        Intent i = getIntent();
        ticketCode = i.getStringExtra("code");
    }

    private void updateData(){
        if(ticket!=null){
            code.setText(ticket.getTicketCode());
            name.setText(ticket.getFullname());
            dob.setText(ticket.getDob());
            from.setText(ticket.getFrom());
            to.setText(ticket.getTo());
            start.setText(ticket.getStart());
            end.setText(ticket.getEnd());
            price.setText(ticket.getPrice()+"");
            type.setText((ticket.isBusiness())? TicketConstants.BUSSINESS:TicketConstants.ECONOMIC);
            switch (ticket.getTicketStatus()){
                case TicketConstants.ACTIVE:
                    status.setText(TicketConstants.ACTIVE_STATUS);
                    break;
                case TicketConstants.CANCELED:
                    status.setText(TicketConstants.CANCELED_STATUS);
                    cancel.setEnabled(false);
                    purchase.setEnabled(false);
                    break;
                case TicketConstants.DELAY:
                    status.setText(TicketConstants.DELAY_STATUS);
                    break;
                default:
                    status.setText("");
                    break;
            }
            if(ticket.getPurchaseStatus()){
                payment_status.setText(TicketConstants.PURCHASED);
                purchase.setEnabled(false);
                cancel.setEnabled(false);
            }else{
                payment_status.setText(TicketConstants.UN_PURCHASED);
            }

        }
    }
    private void getTicketByTicketCode(String code){
        String uid = auth.getUid();
        DocumentReference reference = firestore.collection("Users/"+uid+"/tickets").document(code);
        reference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    ticket = TicketModel.fromDocument(document);
                    ticket.setTicketCode(document.getId());
                    updateData();
                }
            }
        });
    }
    private void cancelTicketByCode(String code){
        String uid = auth.getUid();
        DocumentReference reference = firestore.collection("Users/"+uid+"/tickets").document(code);

        ticket.setTicketStatus(TicketConstants.CANCELED);

        Map<String,Object> newData = ticket.toMap();

        reference.set(newData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Intent intent = new Intent(TicketDetailActivity.this, BookingHistoryActivity.class);
                startActivity(intent);
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
        code = findViewById(R.id.ticket_detail_code);
        name = findViewById(R.id.ticket_detail_name);
        dob = findViewById(R.id.ticket_detail_dob);
        from = findViewById(R.id.ticket_detail_from);
        to = findViewById(R.id.ticket_detail_to);
        start =findViewById(R.id.ticket_detail_start);
        end = findViewById(R.id.ticket_detail_end);
        price = findViewById(R.id.ticket_detail_price);
        type = findViewById(R.id.ticket_detail_type);
        payment_status = findViewById(R.id.ticket_detail_payment_status);
        status = findViewById(R.id.ticket_detail_status);
    }
    private void InitButton(){
        purchase = findViewById(R.id.ticket_detail_purchase_btn);
        cancel = findViewById(R.id.ticket_detail_cancel_btn);
        backBtn = findViewById(R.id.ticket_detail_back_btn);
    }
}