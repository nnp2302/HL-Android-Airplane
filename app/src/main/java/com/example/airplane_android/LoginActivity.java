package com.example.airplane_android.auth_activities;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import com.example.airplane_android.R;

public class LoginActivity extends AppCompatActivity {

    TextView registerLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // (17/10/2023 16:03) Hưng Hà - biến đổi màu text "Đăng ký"
        registerLink = findViewById(R.id.link);
        String registerTxt = registerLink.getText().toString();
        int registerTxtHighlightIndex = registerTxt.indexOf("Đăng ký");
        SpannableString spanRegisterTxt = new SpannableString(registerTxt);
        spanRegisterTxt.setSpan(new ForegroundColorSpan(Color.parseColor("#CCCCCC")), 0, registerTxtHighlightIndex -1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spanRegisterTxt.setSpan(new ForegroundColorSpan(Color.parseColor("#0194F3")), registerTxtHighlightIndex, registerLink.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        registerLink.setText(spanRegisterTxt);

        // (17/10/2023 16:15) Hưng Hà - navigate tới các trang tương ứng
        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}