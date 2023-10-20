package com.example.airplane_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.airplane_android.utils.ValidateUtils;

public class LoginActivity extends AppCompatActivity {

    TextView txtRegisterLink, txtForgotLink;
    EditText emailInput, passInput;
    ImageButton btnTogglePass, btnBack;
    Button btnSubmit;

    private boolean showPass = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailInput = findViewById(R.id.emailInput);
        passInput = findViewById(R.id.passInput);
        txtRegisterLink = findViewById(R.id.registerLink);
        txtForgotLink = findViewById(R.id.forgotPass);
        btnTogglePass = findViewById(R.id.togglePass);
        btnBack = findViewById(R.id.back);
        btnSubmit = findViewById(R.id.submit);

        // (17/10/2023 16:03) Hưng Hà - biến đổi màu text "Đăng ký"
        final String registerTxt = txtRegisterLink.getText().toString();
        final int registerTxtHighlightIndex = registerTxt.indexOf("Đăng ký");
        final SpannableString spanRegisterTxt = new SpannableString(registerTxt);
        spanRegisterTxt.setSpan(new ForegroundColorSpan(Color.parseColor("#CCCCCC")), 0, registerTxtHighlightIndex -1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spanRegisterTxt.setSpan(new ForegroundColorSpan(Color.parseColor("#0194F3")), registerTxtHighlightIndex, txtRegisterLink.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        txtRegisterLink.setText(spanRegisterTxt);

        // (17/10/2023 16:15) Hưng Hà - navigate tới các activity tương ứng
        txtRegisterLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        txtForgotLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgotPassActivity.class);
                startActivity(intent);
            }
        });


        // (17/10/2023 16:44) Hưng Hà - toggle pass
        btnTogglePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(showPass) {
                    passInput.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    btnTogglePass.setImageResource(R.drawable.ic_eye);
                } else {
                    passInput.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    btnTogglePass.setImageResource(R.drawable.ic_eye_off);
                }

                showPass = !showPass;
            }
        });

        // (17/10/2023 15:23) Hưng Hà - back về activity home
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        // (17/10/2023 16:20) Hưng Hà - validate tài khoản
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkFieldIsEmpty(emailInput, passInput)) {
                    Toast.makeText(LoginActivity.this, "Vui lòng nhập email và mật khẩu", Toast.LENGTH_SHORT).show();
                    return;
                }

                final String emailValue = emailInput.getText().toString();
                final String passValue = passInput.getText().toString();
                if (!ValidateUtils.checkEmail(emailValue) || !ValidateUtils.checkPassword(passValue)) {
                    Toast.makeText(LoginActivity.this, "Email hoặc mật khẩu không đúng", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private boolean checkFieldIsEmpty(EditText emailInput, EditText passInput) {
        if (emailInput == null || passInput == null) return true;

        final String emailValue = emailInput.getText().toString();
        final String passValue = passInput.getText().toString();
        return emailValue.equals("") || passValue.equals("");
    }
}