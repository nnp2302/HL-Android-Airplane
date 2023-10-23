package com.example.airplane_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.airplane_android.utils.ValidateUtils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

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
            String emailValue = Objects.requireNonNull(emailInput.getText()).toString();

            if (checkIsEmpty(emailValue)) {
                Toast.makeText(this, "Vui lòng điền email", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!ValidateUtils.checkEmail(emailValue)) {
                Toast.makeText(this, "Vui lòng nhập đúng email", Toast.LENGTH_SHORT).show();
                return;
            }

            FirebaseFirestore fireStore = FirebaseFirestore.getInstance();
            fireStore.collection("Users").whereEqualTo("email", emailValue).get().addOnCompleteListener(task -> {
                if (task.isSuccessful() && !task.getResult().isEmpty()) {
                    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                    firebaseAuth.sendPasswordResetEmail(emailValue)
                        .addOnCompleteListener(resetTask -> {
                            Toast.makeText(this, "Vui lòng kiểm tra email để được reset", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(this, LoginActivity.class);
                            startActivity(intent);
                        });
                } else {
                    Toast.makeText(this, "Email không tồn tại", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private boolean checkIsEmpty(String emailValue) {
        return emailValue.equals("");
    }
}