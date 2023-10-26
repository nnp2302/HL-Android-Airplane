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

import de.hdodenhof.circleimageview.CircleImageView;

public class AddUserInfoActivity extends AppCompatActivity {

  TextInputEditText usernameInput, phoneInput, birthInput;
  AutoCompleteTextView genderInput;
  Button btnSkip, btnSubmit;
  CircleImageView imgAvatar;

  FirebaseFirestore fireStore;
  FirebaseStorage firebaseStorage;

  private Uri imgUri;
  private final String[] genderList = new String[]{"Nam", "Nữ", "Khác"};

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

    fireStore = FirebaseFirestore.getInstance();
    firebaseStorage = FirebaseStorage.getInstance();

    ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(this, R.layout.item_gender_list, genderList);
    genderInput.setAdapter(genderAdapter);
  }

  @SuppressLint("SetTextI18n")
  @Override
  protected void onStart() {
    super.onStart();

    // (19/10/2023 17:47) Hưng Hà - navigate đến activity Home
    btnSkip.setOnClickListener(v -> startActivity(new Intent(this, MainActivity.class)));

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
        birthInput.setText(formatDate.format(selectedDate));
      });
    });

    // (19/10/2023 17:47) Hưng Hà - validate form
    btnSubmit.setOnClickListener(v -> {
      final String usernameValue = usernameInput.getText().toString();
      final String phoneValue = phoneInput.getText().toString();
      final String birthValue = birthInput.getText().toString();
      final String genderValue = genderInput.getText().toString();

      if (checkFieldIsEmpty(usernameValue, phoneValue, birthValue))
        startActivity(new Intent(this, MainActivity.class));

      final String validateBasicRes = validateBasicField(phoneValue, birthValue);
      if (!validateBasicRes.equals("")) {
        Toast.makeText(this, validateBasicRes, Toast.LENGTH_SHORT).show();
        return;
      }

      final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
      if (currentUser == null) {
        Log.e("Firebase Authentication", "Can't find user");
        Toast.makeText(this, "Có lỗi trong quá trình xử lý", Toast.LENGTH_SHORT).show();
        return;
      }

      final String userUid = currentUser.getUid();

      if (imgUri != null) {
        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.ENGLISH);
        final Date currentTime = new Date();
        final String fileName = dateFormat.format(currentTime) + "_" + userUid;

        final StorageReference storageRef = firebaseStorage.getReference("avatars/" + fileName);
        storageRef.putFile(imgUri)
            .addOnSuccessListener(ts -> {
              final StorageReference dataRef = firebaseStorage.getReference("avatars/" + fileName);
              dataRef.getDownloadUrl()
                  .addOnSuccessListener(uri -> handleAddUserInfo(userUid, usernameValue, genderValue, phoneValue, birthValue, "avatars/" + fileName, uri.toString()))
                  .addOnFailureListener(e -> {
                    Toast.makeText(this, "Thêm thông tin thất bại", Toast.LENGTH_SHORT).show();
                    Log.e("Get photo FirebaseStorage", "Failed to get user's avatar uri in FirebaseStorage, log: " + e);
                  });
            })
            .addOnFailureListener(e -> {
              Toast.makeText(this, "Thêm thông tin thất bại", Toast.LENGTH_SHORT).show();
              Log.e("Add photo FirebaseStorage", "Failed to add user's avatar to FirebaseStorage, log: " + e);
            });
      } else handleAddUserInfo(userUid, usernameValue, genderValue, phoneValue, birthValue, "", "");
    });
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == Activity.RESULT_OK) {
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

  private String validateBasicField(String phoneValue, String birthValue) {
    if (!phoneValue.equals("") && !ValidateUtils.checkPhoneNumber(phoneValue))
      return "Số điện thoại không đúng định dạng";
    if (!birthValue.equals("") && ValidateUtils.checkBirthDay(birthValue))
      return "Bạn chưa đủ tuổi đăng ký";

    return "";
  }

  private void handleAddUserInfo(String uid, String username, String gender, String phone, String birth, String photoPath, String photoUrl) {
    Map<String, Object> payload = new HashMap<>();
    payload.put("name", username);
    payload.put("gender", gender);
    payload.put("phone", phone);
    payload.put("birth", birth);
    payload.put("photoFilePath", photoPath);
    payload.put("photoUrl", photoUrl);

    fireStore.collection("Users").document(uid).set(payload)
        .addOnSuccessListener(t -> {
          Toast.makeText(this, "Thêm thông tin thành công", Toast.LENGTH_SHORT).show();
          startActivity(new Intent(this, MainActivity.class));
        })
        .addOnFailureListener(e -> {
          Toast.makeText(this, "Thêm thông tin thất bại", Toast.LENGTH_SHORT).show();
          Log.e("Add data FireStore", "Failed to add user data's document to FireStore, log: " + e);
        });
  }
}