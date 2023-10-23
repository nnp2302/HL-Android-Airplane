package com.example.airplane_android;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.example.airplane_android.utils.ValidateUtils;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddUserInfoActivity extends AppCompatActivity {

    TextInputEditText usernameInput, phoneInput, birthInput;
    AutoCompleteTextView genderInput;
    Button btnSkip, btnSubmit;
    CircleImageView imgAvatar;

    private Uri imgUri;
    private final String[] genderList = new String[] { "Nam", "Nữ", "Khác" };

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user_info);

        btnSkip = findViewById(R.id.skip);
        usernameInput = findViewById(R.id.usernameInput);
        phoneInput = findViewById(R.id.phoneInput);
        birthInput = findViewById(R.id.birthInput);
        genderInput = findViewById(R.id.genderInput);
        btnSubmit = findViewById(R.id.submit);
        imgAvatar = findViewById(R.id.avatar);

        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(this, R.layout.item_gender_list, genderList);
        genderInput.setAdapter(genderAdapter);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onStart() {
        super.onStart();

        // (19/10/2023 17:47) Hưng Hà - navigate đến activity Home
        btnSkip.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });

        //(19/10/2023 23:36) Hưng Hà - chọn ảnh
        imgAvatar.setOnClickListener(v -> ImagePicker.with(this)
            .crop()
            .compress(1024)
            .maxResultSize(1080, 1080)
            .start());

        birthInput.setOnClickListener(v -> {
            MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Chọn ngày")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .setTextInputFormat(new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH))
                .build();

            datePicker.show(getSupportFragmentManager(), "birthCalendar");

            datePicker.addOnPositiveButtonClickListener(selection -> {
                Date selectedDate = new Date(selection);
                SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
                formatDate.format(selectedDate);
                birthInput.setText(formatDate.format(selectedDate));
            });
        });

        // (19/10/2023 17:47) Hưng Hà - validate form
        btnSubmit.setOnClickListener(v -> {
            final String usernameValue = Objects.requireNonNull(usernameInput.getText()).toString();
            final String phoneValue = Objects.requireNonNull(phoneInput.getText()).toString();
            final String birthValue = Objects.requireNonNull(birthInput.getText()).toString();
            final String genderValue = genderInput.getText().toString();

            if (checkFieldIsEmpty(usernameValue, phoneValue, birthValue)) {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }

            if (!phoneValue.equals("") && !ValidateUtils.checkPhoneNumber(phoneValue)) {
                Toast.makeText(this, "Số điện thoại không đúng định dạng", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!birthValue.equals("") && ValidateUtils.checkBirthDay(birthValue)) {
                Toast.makeText(this, "Bạn chưa đủ tuổi đăng ký", Toast.LENGTH_SHORT).show();
                return;
            }

            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            if (currentUser == null) {
                Toast.makeText(this, "Có lỗi trong quá trình xử lý", Toast.LENGTH_SHORT).show();
                Log.e("Null", "Get null user");
                return;
            }
            final String userId = currentUser.getUid();

            if (imgUri != null) {
                final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.ENGLISH);
                final Date currentTime = new Date();
                final String fileName = dateFormat.format(currentTime) + "_" + userId;

                final StorageReference fireStorage = FirebaseStorage.getInstance().getReference("avatars/" + fileName);
                fireStorage.putFile(imgUri).addOnSuccessListener(taskSnapshot -> {
                    StorageReference dataRef = FirebaseStorage.getInstance().getReference("avatars/" + fileName);
                    dataRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        Map<String, Object> userPayload = new HashMap<>();
                        userPayload.put("name", usernameValue);
                        userPayload.put("gender", genderValue);
                        userPayload.put("phone", phoneValue);
                        userPayload.put("birth", birthValue);
                        userPayload.put("photoUrl", uri.toString());

                        FirebaseFirestore fireStore = FirebaseFirestore.getInstance();
                        fireStore.collection("Users").document(userId).update(userPayload)
                            .addOnSuccessListener(u -> {
                                Toast.makeText(this, "Thêm thông tin thành công", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(this, MainActivity.class);
                                startActivity(intent);
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(this, "Thêm thông tin thất bại", Toast.LENGTH_SHORT).show();
                                Log.e("Error Upload user info", e.toString());
                            });
                    });
                });
            } else {
                Map<String, Object> userPayload = new HashMap<>();
                userPayload.put("name", usernameValue);
                userPayload.put("gender", genderValue);
                userPayload.put("phone", phoneValue);
                userPayload.put("birth", birthValue);
                userPayload.put("photoUrl", "");

                FirebaseFirestore fireStore = FirebaseFirestore.getInstance();
                fireStore.collection("Users").document(userId).set(userPayload)
                    .addOnSuccessListener(u -> {
                        Intent intent = new Intent(this, MainActivity.class);
                        startActivity(intent);
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Thêm thông tin thất bại", Toast.LENGTH_SHORT).show();
                        Log.e("Error Upload user info", e.toString());
                    });
            }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            assert data != null;
            Uri uri = data.getData();
            imgUri = uri;
            imgAvatar.setImageURI(uri);
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkFieldIsEmpty(String usernameValue, String phoneValue, String birthValue) {
        return usernameValue.equals("") && phoneValue.equals("") && birthValue.equals("");
    }
}