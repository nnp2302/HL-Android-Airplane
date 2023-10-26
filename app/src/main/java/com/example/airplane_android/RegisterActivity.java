package com.example.airplane_android;

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

import com.example.airplane_android.utils.ValidateUtils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

  TextView txtLoginLink;
  TextInputEditText emailInput, passInput, rePassInput;
  ImageButton btnBack;
  Button btnSubmit;
  FirebaseAuth auth;
  FirebaseFirestore fireStore;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_register);

    emailInput = findViewById(R.id.emailInput);
    passInput = findViewById(R.id.passInput);
    rePassInput = findViewById(R.id.rePassInput);
    txtLoginLink = findViewById(R.id.loginLink);
    btnBack = findViewById(R.id.back);
    btnSubmit = findViewById(R.id.submit);

    auth = FirebaseAuth.getInstance();
    fireStore = FirebaseFirestore.getInstance();

    // (17/10/2023 17:05) Hưng Hà - biến đổi màu text "Đăng nhập"
    final String registerTxt = txtLoginLink.getText().toString();
    final int registerTxtHighlightIndex = registerTxt.indexOf("Đăng nhập");
    final SpannableString spanRegisterTxt = new SpannableString(registerTxt);
    spanRegisterTxt.setSpan(new ForegroundColorSpan(Color.parseColor("#CCCCCC")), 0, registerTxtHighlightIndex - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    spanRegisterTxt.setSpan(new ForegroundColorSpan(Color.parseColor("#0194F3")), registerTxtHighlightIndex, txtLoginLink.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    txtLoginLink.setText(spanRegisterTxt);
  }

  @Override
  protected void onStart() {
    super.onStart();

    // (17/10/2023 16:15) Hưng Hà - navigate tới các activity tương ứng
    txtLoginLink.setOnClickListener(v -> startActivity(new Intent(this, LoginActivity.class)));

    // (17/10/2023 15:25) Hưng Hà - back về activity home
    btnBack.setOnClickListener(v -> finish());

    // (17/10/2023 17:05) Hưng Hà - validate tài khoản
    btnSubmit.setOnClickListener(v -> {
      final String emailValue = emailInput.getText().toString();
      final String passValue = passInput.getText().toString();
      final String rePassValue = rePassInput.getText().toString();

      String basicValidateRes = validateBasicField(emailValue, passValue, rePassValue);
      if (!basicValidateRes.equals("")) {
        Toast.makeText(this, basicValidateRes, Toast.LENGTH_SHORT).show();
        return;
      }

      auth.createUserWithEmailAndPassword(emailValue, passValue)
          .addOnSuccessListener(res -> {
            FirebaseUser currentUser = auth.getCurrentUser();
            String userUid = currentUser.getUid();

            Map<String, Object> payload = new HashMap<>();
            payload.put("email", emailValue);

            handleAddUserData(userUid, payload);
          })
          .addOnFailureListener(e -> {
            Log.e("SignUp Firebase", "Failed to register new account, log: " + e);
            Toast.makeText(this, "Tài khoản đã tồn tại", Toast.LENGTH_SHORT).show();
          });
    });
  }

  private boolean checkFieldIsEmpty(String emailValue, String passValue, String rePassValue) {
    return emailValue.equals("") || passValue.equals("") || rePassValue.equals("");
  }

  private String validateBasicField(String emailValue, String passValue, String rePassValue) {
    if (checkFieldIsEmpty(emailValue, passValue, rePassValue))
      return "Vui lòng điền đầy đủ thông tin";
    if (!ValidateUtils.checkEmail(emailValue)) return "Định dạng email không đúng";
    if (!ValidateUtils.checkPassword(passValue))
      return "Mật khẩu phải tối thiểu 8 ký tự và có ít nhất 1 ký tự viết hoa";
    if (!ValidateUtils.checkRegisterPasswordMatch(passValue, rePassValue))
      return "Mật khẩu không khớp nhau";

    return "";
  }

  private void handleAddUserData(String uid, Map<String, Object> payload) {
    fireStore.collection("Users").document(uid).set(payload)
        .addOnSuccessListener(documentReference -> {
          Toast.makeText(this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
          startActivity(new Intent(this, AddUserInfoActivity.class));
        }).addOnFailureListener(e -> {
          Log.e("Add data FireStore", "Failed to add new user data, log: " + e);
          Toast.makeText(this, "Đăng ký thất bại", Toast.LENGTH_SHORT).show();
        });
  }
}