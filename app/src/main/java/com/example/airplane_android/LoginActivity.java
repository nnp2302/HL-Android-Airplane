package com.example.airplane_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.airplane_android.admin.AdminActivity;
import com.example.airplane_android.utils.ValidateUtils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

  TextInputEditText emailInput, passInput;
  TextView txtRegisterLink, txtForgotLink;
  ImageButton btnBack;
  Button btnSubmit;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);

    emailInput = findViewById(R.id.emailInput);
    passInput = findViewById(R.id.passInput);
    txtRegisterLink = findViewById(R.id.registerLink);
    txtForgotLink = findViewById(R.id.forgotPass);
    btnBack = findViewById(R.id.back);
    btnSubmit = findViewById(R.id.submit);

    // (17/10/2023 16:03) Hưng Hà - biến đổi màu text "Đăng ký"
    final String registerTxt = txtRegisterLink.getText().toString();
    final int registerTxtHighlightIndex = registerTxt.indexOf("Đăng ký");
    final SpannableString spanRegisterTxt = new SpannableString(registerTxt);
    spanRegisterTxt.setSpan(new ForegroundColorSpan(Color.parseColor("#CCCCCC")), 0, registerTxtHighlightIndex - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    spanRegisterTxt.setSpan(new ForegroundColorSpan(Color.parseColor("#0194F3")), registerTxtHighlightIndex, txtRegisterLink.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    txtRegisterLink.setText(spanRegisterTxt);
  }

  @Override
  protected void onStart() {
    super.onStart();

    // (17/10/2023 16:15) Hưng Hà - navigate tới các activity tương ứng
    txtRegisterLink.setOnClickListener(v -> startActivity(new Intent(this, RegisterActivity.class)));

    txtForgotLink.setOnClickListener(v -> startActivity(new Intent(this, ForgotPassActivity.class)));

    btnBack.setOnClickListener(v -> startActivity(new Intent(this, MainActivity.class)));

    // (17/10/2023 16:20) Hưng Hà - validate tài khoản
    btnSubmit.setOnClickListener(v -> {
      final String emailValue = emailInput.getText().toString();
      final String passValue = passInput.getText().toString();

      final String basicValidateRes = validateBasicField(emailValue, passValue);
      if (!basicValidateRes.equals("")) {
        Toast.makeText(this, basicValidateRes, Toast.LENGTH_SHORT).show();
        return;
      }

      if (emailValue.equals("admin@gmail.com") && passValue.equals("AdminAdmin123456"))
        startActivity(new Intent(this, AdminActivity.class));
      else if (emailValue.equals("admin@gmail.com") && !passValue.equals("AdminAdmin123456"))
        Toast.makeText(this, "Email hoặc mật khẩu không đúng", Toast.LENGTH_SHORT).show();
      else {
        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithEmailAndPassword(emailValue, passValue)
            .addOnSuccessListener(res -> {
              Toast.makeText(this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
              startActivity(new Intent(this, MainActivity.class));
            })
            .addOnFailureListener(e -> {
              Log.e("SignIn Firebase", "Failed to login with Firebase Authentication!!!, log: " + e);
              Toast.makeText(this, "Email hoặc mật khẩu không đúng", Toast.LENGTH_SHORT).show();
            });
      }
    });
  }

  private boolean checkFieldIsEmpty(String emailInput, String passInput) {
    return emailInput.equals("") || passInput.equals("");
  }

  private String validateBasicField(String emailValue, String passValue) {
    if (checkFieldIsEmpty(emailValue, passValue)) return "Vui lòng nhập email và mật khẩu";
    if (!ValidateUtils.checkEmail(emailValue) || !ValidateUtils.checkPassword(passValue))
      return "Email hoặc mật khẩu không đúng";

    return "";
  }
}