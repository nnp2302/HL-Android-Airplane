package com.example.airplane_android;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.airplane_android.utils.ValidateUtils;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddUserInfoActivity extends AppCompatActivity {

    EditText usernameInput, phoneInput, birthInput;
    RadioGroup genderInput;
    ImageButton btnToggleBirthCalendar;
    Button btnSkip, btnSubmit;
    CircleImageView imgAvatar;

    FirebaseFirestore fireStore;

    private int[] cYear, cMonth, cDay;


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
        btnToggleBirthCalendar = findViewById(R.id.birthCalendar);
        btnSubmit = findViewById(R.id.submit);
        imgAvatar = findViewById(R.id.avatar);

        fireStore = FirebaseFirestore.getInstance();

        final Calendar calendar = Calendar.getInstance();
        cYear = new int[]{calendar.get(Calendar.YEAR)};
        cMonth = new int[]{calendar.get(Calendar.MONTH)};
        cDay = new int[]{calendar.get(Calendar.DAY_OF_MONTH)};
    }

    @Override
    protected void onStart() {
        super.onStart();

        // (19/10/2023 17:47) Hưng Hà - navigate đến activity Home
        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddUserInfoActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        //(19/10/2023 23:36) Hưng Hà - chọn ảnh
        imgAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.with(AddUserInfoActivity.this)
                        .crop()
                        .compress(1024)
                        .maxResultSize(1080, 1080)
                        .start();
            }
        });

        //(19/10/2023 20:19) Hưng Hà - mở calendar view
        btnToggleBirthCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String birthValue = birthInput.getText().toString();
                if (!birthValue.equals("")) {
                    String[] brithSplit = birthValue.split("/");

                    cDay[0] = Integer.parseInt(brithSplit[0]);
                    cMonth[0] = Integer.parseInt(brithSplit[1]);
                    cYear[0] = Integer.parseInt(brithSplit[2]);
                }

                DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        birthInput.setText(dayOfMonth + "/" + (month + 1) + "/" + year);

                        cYear[0] = year;
                        cMonth[0] = month;
                        cDay[0] = dayOfMonth;
                    }
                };

                DatePickerDialog datePickerDialog = new DatePickerDialog(AddUserInfoActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                        dateSetListener,
                        cYear[0],
                        cMonth[0],
                        cDay[0]);
                datePickerDialog.show();
            }
        });

        // (19/10/2023 17:47) Hưng Hà - validate form
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkFieldIsEmpty(usernameInput, phoneInput, birthInput, genderInput)) {
                    Intent intent = new Intent(AddUserInfoActivity.this, MainActivity.class);
                    startActivity(intent);
                    return;
                }

                final String phoneValue = phoneInput.getText().toString();
                if (!phoneValue.equals("") && !ValidateUtils.checkPhoneNumber(phoneValue)) {
                    Toast.makeText(AddUserInfoActivity.this, "Số điện thoại không đúng định dạng", Toast.LENGTH_SHORT).show();
                    return;
                }

                final String birthValue = birthInput.getText().toString();
                if (!birthValue.equals("") && ValidateUtils.checkBirthDay(birthValue)) {
                    Toast.makeText(AddUserInfoActivity.this, "Bạn chưa đủ tuổi đăng ký", Toast.LENGTH_SHORT).show();
                    return;
                }

                String genderValue = "";
                int selectedGenderId = genderInput.getCheckedRadioButtonId();
                if (selectedGenderId != -1) {
                    RadioButton selectedGender = findViewById(selectedGenderId);
                    genderValue = selectedGender.getText().toString();
                } else {
                    Toast.makeText(AddUserInfoActivity.this, "Có lỗi trong quá trình chọn giới tính", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent registerIntent = getIntent();
                String userId = registerIntent.getStringExtra("userId");

                final String usernameValue = usernameInput.getText().toString();
                Map<String, Object> userPayload = new HashMap<>();
                userPayload.put("name", usernameValue);
                userPayload.put("gender", genderValue);
                userPayload.put("phone", phoneValue);
                userPayload.put("birth", birthValue);

                fireStore.collection("Users").document(userId)
                        .update(userPayload)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(AddUserInfoActivity.this, "Thêm thông tin thành công", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(AddUserInfoActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AddUserInfoActivity.this, "Thêm thông tin thất bại", Toast.LENGTH_SHORT).show();
                            }
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
            imgAvatar.setImageURI(uri);
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(AddUserInfoActivity.this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(AddUserInfoActivity.this, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkFieldIsEmpty(EditText usernameInput, EditText phoneInput, EditText birthInput, RadioGroup genderInput) {
        return usernameInput == null && phoneInput == null && birthInput == null && genderInput == null;
    }
}