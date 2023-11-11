package com.example.airplane_android.razorpay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.airplane_android.BookingHistoryActivity;
import com.example.airplane_android.MainActivity;
import com.example.airplane_android.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PaymentActivity extends Activity  implements PaymentResultListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    FirebaseAuth auth;
    FirebaseFirestore firestore;
    String amount,code;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        Checkout.preload(getApplicationContext());
        Intent intent = getIntent();
        amount = intent.getStringExtra("amount");
        code = intent.getStringExtra("code");
        startPayment();
    }
    private void startPayment() {
        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_0DNFzpuVbuUWhA");

        checkout.setImage(R.drawable.ic_logo);

        final Activity paymentActivity = this;

        try {
            JSONObject options = new JSONObject();

            options.put("name", "Airplane Service");
            options.put("description", "Thanh toan ve: #"+code);
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.jpg");
//            options.put("order_id", "order_"+code);//from response of step 3.
            options.put("theme.color", "#3399cc");
            options.put("currency", "USD");
            options.put("amount", amount);//pass amount in currency subunits
            options.put("prefill.email", auth.getCurrentUser().getEmail());
            options.put("prefill.contact","0123456789");
            JSONObject retryObj = new JSONObject();
            retryObj.put("enabled", true);
            retryObj.put("max_count", 4);
            options.put("retry", retryObj);

            checkout.open(paymentActivity, options);

        } catch(Exception e) {
            Log.e(TAG, "Lỗi khi tạo giao dịch Razorpay: ", e);
        }
    }

    @Override
    public void onPaymentSuccess(String s) {
        String uid = auth.getUid();
        DocumentReference doc = firestore.document("Users/"+uid+"/tickets/"+code);
        Map<String,Object> paystatus  = new HashMap<>();
        paystatus.put("purchaseStatus",true);
        doc.update(paystatus).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Intent intent = new Intent(PaymentActivity.this, BookingHistoryActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(this, "Thanh toán thất bại", Toast.LENGTH_LONG).show();
    }
}