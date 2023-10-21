package com.example.airplane_android;

import androidx.annotation.NonNull;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    TextView txtLoginLink;
    EditText emailInput, passInput, rePassInput;
    ImageButton btnTogglePass, btnToggleRePass, btnBack;
    Button btnSubmit;
    FirebaseAuth auth;
    FirebaseFirestore fireStore;

    private boolean showPass = false, showRePass = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        emailInput = findViewById(R.id.emailInput);
        passInput = findViewById(R.id.passInput);
        rePassInput = findViewById(R.id.rePassInput);
        txtLoginLink = findViewById(R.id.loginLink);
        btnTogglePass = findViewById(R.id.togglePass);
        btnToggleRePass = findViewById(R.id.toggleRePass);
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

        // (17/10/2023 16:15) Hưng Hà - navigate tới các activity tương ứng
        txtLoginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        // (17/10/2023 17:05) Hưng Hà - toggle pass
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

        btnToggleRePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(showRePass) {
                    rePassInput.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    btnToggleRePass.setImageResource(R.drawable.ic_eye);
                } else {
                    rePassInput.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    btnToggleRePass.setImageResource(R.drawable.ic_eye_off);
                }

                showRePass = !showRePass;
            }
        });

        // (17/10/2023 15:25) Hưng Hà - back về activity home
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        // (17/10/2023 17:05) Hưng Hà - validate tài khoản
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkFieldIsEmpty(emailInput, passInput, rePassInput)) {
                    Toast.makeText(RegisterActivity.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }

                final String emailValue = emailInput.getText().toString();
                if (!ValidateUtils.checkEmail(emailValue)) {
                    Toast.makeText(RegisterActivity.this, "Định dạng email không đúng", Toast.LENGTH_SHORT).show();
                    return;
                }

                final String passValue = passInput.getText().toString();
                if (!ValidateUtils.checkPassword(passValue)) {
                    Toast.makeText(RegisterActivity.this, "Mật khẩu phải tối thiểu 8 ký tự và có ít nhất 1 ký tự viết hoa", Toast.LENGTH_SHORT).show();
                    return;
                }

                final String rePassValue = rePassInput.getText().toString();
                if (!ValidateUtils.checkRegisterPasswordMatch(passValue, rePassValue)) {
                    Toast.makeText(RegisterActivity.this, "Mật khẩu không khớp nhau", Toast.LENGTH_SHORT).show();
                    return;
                }

                auth.createUserWithEmailAndPassword(emailValue, passValue).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Map<String, Object> userData = new HashMap<>();
                            userData.put("email", emailValue);

                            fireStore.collection("Users").add(userData)
                                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                @Override
                                                public void onSuccess(DocumentReference documentReference) {
                                                    Toast.makeText(RegisterActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();

                                                    Intent intent = new Intent(RegisterActivity.this, AddUserInfoActivity.class);
                                                    intent.putExtra("userId", documentReference.getId());
                                                    startActivity(intent);
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(RegisterActivity.this, "Có lỗi trong quá trình xử lý", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                        } else {
                            Toast.makeText(RegisterActivity.this, "Tài khoản đã tồn tại", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    private boolean checkFieldIsEmpty(EditText emailInput, EditText passInput, EditText rePassInput) {
        if (emailInput == null || passInput == null || rePassInput == null) return true;

        final String emailValue = emailInput.getText().toString();
        final String passValue = passInput.getText().toString();
        final String rePassValue = rePassInput.getText().toString();
        return emailValue.equals("") || passValue.equals("") || rePassValue.equals("");
    }
}