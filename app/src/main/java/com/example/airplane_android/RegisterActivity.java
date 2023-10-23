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
import java.util.Objects;

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
        spanRegisterTxt.setSpan(new ForegroundColorSpan(Color.parseColor("#CCCCCC")), 0, registerTxtHighlightIndex -1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spanRegisterTxt.setSpan(new ForegroundColorSpan(Color.parseColor("#0194F3")), registerTxtHighlightIndex, txtLoginLink.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        txtLoginLink.setText(spanRegisterTxt);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // (17/10/2023 16:15) Hưng Hà - navigate tới các activity tương ứng
        txtLoginLink.setOnClickListener(v -> {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        });

        // (17/10/2023 15:25) Hưng Hà - back về activity home
        btnBack.setOnClickListener(v -> finish());

        // (17/10/2023 17:05) Hưng Hà - validate tài khoản
        btnSubmit.setOnClickListener(v -> {
            final String emailValue = Objects.requireNonNull(emailInput.getText()).toString();
            final String passValue = Objects.requireNonNull(passInput.getText()).toString();
            final String rePassValue = Objects.requireNonNull(rePassInput.getText()).toString();

            if (checkFieldIsEmpty(emailValue, passValue, rePassValue)) {
                Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!ValidateUtils.checkEmail(emailValue)) {
                Toast.makeText(this, "Định dạng email không đúng", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!ValidateUtils.checkPassword(passValue)) {
                Toast.makeText(this, "Mật khẩu phải tối thiểu 8 ký tự và có ít nhất 1 ký tự viết hoa", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!ValidateUtils.checkRegisterPasswordMatch(passValue, rePassValue)) {
                Toast.makeText(this, "Mật khẩu không khớp nhau", Toast.LENGTH_SHORT).show();
                return;
            }

            auth.createUserWithEmailAndPassword(emailValue, passValue).addOnCompleteListener(this, task -> {
                if (task.isSuccessful()) {
                    FirebaseUser currentUser = auth.getCurrentUser();
                    assert currentUser != null;

                    Map<String, Object> userData = new HashMap<>();
                    userData.put("email", emailValue);

                    FirebaseFirestore fireStore = FirebaseFirestore.getInstance();
                    fireStore.collection("Users")
                        .document(currentUser.getUid())
                        .set(userData)
                        .addOnSuccessListener(documentReference -> {
                            Toast.makeText(this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(this, AddUserInfoActivity.class);
                            startActivity(intent);
                        }).addOnFailureListener(e -> {
                            Toast.makeText(this, "Đăng ký thất bại", Toast.LENGTH_SHORT).show();
                            Log.e("FireStore", e.toString());
                        });
                } else {
                    Toast.makeText(this, "Tài khoản đã tồn tại", Toast.LENGTH_SHORT).show();
                    Log.e("Authentication", "Email is already registered");
                }
            });
        });
    }

    private boolean checkFieldIsEmpty(String emailValue, String passValue, String rePassValue) {
        return emailValue.equals("") || passValue.equals("") || rePassValue.equals("");
    }
}