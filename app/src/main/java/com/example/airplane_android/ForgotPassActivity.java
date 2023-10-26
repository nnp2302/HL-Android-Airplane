package com.example.airplane_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.airplane_android.utils.ValidateUtils;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;

public class ForgotPassActivity extends AppCompatActivity {

  TextInputEditText emailInput;
  ImageButton btnBack;
  Button btnSubmit;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_forgot_pass);

    emailInput = findViewById(R.id.emailInput);
    btnBack = findViewById(R.id.back);
    btnSubmit = findViewById(R.id.submit);
  }

  @Override
  protected void onStart() {
    super.onStart();

    btnBack.setOnClickListener(v -> finish());

    btnSubmit.setOnClickListener(v -> {
      final String emailValue = Objects.requireNonNull(emailInput.getText()).toString();

      final String validateBasicRes = validateBasicField(emailValue);
      if (!validateBasicRes.equals("")) {
        Toast.makeText(this, validateBasicRes, Toast.LENGTH_SHORT).show();
        return;
      }

      final FirebaseFirestore fireStore = FirebaseFirestore.getInstance();
      fireStore.collection("Users").whereEqualTo("email", emailValue).get().addOnCompleteListener(t -> {
        if (handleCheckAuthExist(t)) {
          FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
          firebaseAuth.sendPasswordResetEmail(emailValue).addOnCompleteListener(rt -> handleResetAuth());
        } else Toast.makeText(this, "Email không tồn tại", Toast.LENGTH_SHORT).show();
      });
    });
  }

  private boolean checkIsEmpty(String emailValue) {
    return emailValue.equals("");
  }

  private String validateBasicField(String emailValue) {
    if (checkIsEmpty(emailValue)) return "Vui lòng điền email";
    if (!ValidateUtils.checkEmail(emailValue)) return "Vui lòng nhập đúng định dạng email";

    return "";
  }

  private boolean handleCheckAuthExist(Task<QuerySnapshot> t) {
    return t.isSuccessful() && !t.getResult().isEmpty();
  }

  private void handleResetAuth() {
    Toast.makeText(this, "Vui lòng kiểm tra email để được reset", Toast.LENGTH_SHORT).show();
    startActivity(new Intent(this, LoginActivity.class));
  }
}