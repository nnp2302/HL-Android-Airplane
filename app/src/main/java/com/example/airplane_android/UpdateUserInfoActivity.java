package com.example.airplane_android;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
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
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class UpdateUserInfoActivity extends AppCompatActivity {

  TextInputEditText emailInput, usernameInput, phoneInput, birthInput, idInput;
  AutoCompleteTextView genderInput;
  CircleImageView imgAvatar;
  Button btnSubmit;

  FirebaseAuth firebaseAuth;
  FirebaseUser currentUser;
  FirebaseFirestore fireStore;
  FirebaseStorage firebaseStorage;

  private Uri imgUri;
  private final String[] genderList = new String[]{"Nam", "Nữ", "Khác"};

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_update_user_info);

    ActionBar actionBar = getSupportActionBar();
    assert actionBar != null;
    actionBar.setDisplayHomeAsUpEnabled(true);

    emailInput = findViewById(R.id.emailInput);
    usernameInput = findViewById(R.id.usernameInput);
    phoneInput = findViewById(R.id.phoneInput);
    birthInput = findViewById(R.id.birthInput);
    idInput = findViewById(R.id.idInput);
    genderInput = findViewById(R.id.genderInput);
    imgAvatar = findViewById(R.id.avatar);
    btnSubmit = findViewById(R.id.submit);

    firebaseAuth = FirebaseAuth.getInstance();
    fireStore = FirebaseFirestore.getInstance();
    firebaseStorage = FirebaseStorage.getInstance();

    ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(this, R.layout.item_gender_list, genderList);
    genderInput.setAdapter(genderAdapter);
  }

  @Override
  protected void onStart() {
    super.onStart();

    loadUserData();

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

    btnSubmit.setOnClickListener(v -> {
      String emailValue = emailInput.getText().toString();
      String usernameValue = usernameInput.getText().toString();
      String genderValue = genderInput.getText().toString();
      String phoneValue = phoneInput.getText().toString();
      String birthValue = birthInput.getText().toString();
      String idValue = idInput.getText().toString();

      Map<String, Object> rawData = new HashMap<>();
      rawData.put("email", emailValue);
      rawData.put("username", usernameValue);
      rawData.put("gender", genderValue);
      rawData.put("phone", phoneValue);
      rawData.put("birth", birthValue);
      rawData.put("idCard", idValue);

      if (checkIsEmpty(emailValue, usernameValue, genderValue, phoneValue, birthValue, idValue)) {
        Toast.makeText(this, "Thông tin không được để trống", Toast.LENGTH_SHORT).show();
        return;
      }

      currentUser = firebaseAuth.getCurrentUser();
      String userUid = currentUser.getUid();

      fireStore.collection("Users").document(userUid).get()
          .addOnSuccessListener(res -> {
            String getEmail = res.getString("email");
            String getUsername = res.getString("name");
            String getGender = res.getString("gender");
            String getPhone = res.getString("phone");
            String getBirth = res.getString("birth");
            String getIdCard = res.getString("idCard");
            String getPhotoFilePath = res.getString("photoFilePath");

            Map<String, Object> resRawData = new HashMap<>();
            resRawData.put("email", getEmail);
            resRawData.put("username", getUsername);
            resRawData.put("gender", getGender);
            resRawData.put("phone", getPhone);
            resRawData.put("birth", getBirth);
            resRawData.put("idCard", getIdCard);
            resRawData.put("photoPath", getPhotoFilePath);

//            String validateRes = validateDataField(rawData, resRawData);
//            if (!validateRes.equals("")) {
//              Toast.makeText(this, validateRes, Toast.LENGTH_SHORT).show();
//              return;
//            }
            try {
                if (!getEmail.equals(emailValue)) currentUser.updateEmail(emailValue);
            }catch(Exception e){

            }


            if (imgUri != null) {
              final StorageReference storageRef = firebaseStorage.getReference(getPhotoFilePath);
              storageRef.delete();

              final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.ENGLISH);
              final Date currentTime = new Date();
              final String fileName = dateFormat.format(currentTime) + "_" + userUid;

              final StorageReference fireStorage = firebaseStorage.getReference("avatars/" + fileName);
              fireStorage.putFile(imgUri)
                  .addOnSuccessListener(taskSnapshot -> {
                    StorageReference dataRef = firebaseStorage.getReference("avatars/" + fileName);
                    dataRef.getDownloadUrl()
                        .addOnSuccessListener(uri -> handleUpdateUserInfo(userUid, emailValue, usernameValue, genderValue, phoneValue, birthValue, idValue, "avatars/" + fileName, uri.toString()))
                        .addOnFailureListener(e -> {
                          Toast.makeText(this, "Cập nhật thông tin thất bại", Toast.LENGTH_SHORT).show();
                          Log.e("Update data FireStore", "Failed to update user data's document to FireStore, log: " + e);
                        });
                  })
                  .addOnFailureListener(e -> {
                    Toast.makeText(this, "Cập nhật thông tin thất bại", Toast.LENGTH_SHORT).show();
                    Log.e("Update photo FirebaseStorage", "Failed to update user's avatar to FirebaseStorage, log: " + e);
                  });
            } else
              handleUpdateUserInfo(userUid, emailValue, usernameValue, genderValue, phoneValue, birthValue, idValue, "", "");
          })
          .addOnFailureListener(e -> {
            Toast.makeText(this, "Có lỗi trong quá trình xử lý", Toast.LENGTH_SHORT).show();
            Log.e("Query data FireStore", "Failed to query user's data in FireStore, log: " + e);
          });
    });
  }

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    if (item.getItemId() == android.R.id.home) {
      this.finish();
      return true;
    }

    return super.onOptionsItemSelected(item);
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

  private boolean checkIsEmpty(String email, String username, String gender, String phone, String birth, String id) {
    return email.equals("") && username.equals("") && gender.equals("") && phone.equals("") && birth.equals("") && id.equals("");
  }

  private void loadUserData() {
    currentUser = firebaseAuth.getCurrentUser();
    fireStore.collection("Users").document(currentUser.getUid()).get()
        .addOnSuccessListener(res -> {
          String avatarUrl = res.getString("photoUrl");
          if (!avatarUrl.equals("")) Picasso.get().load(avatarUrl).into(imgAvatar);

          emailInput.setText(res.getString("email"));
          usernameInput.setText(res.getString("name"));
          genderInput.setText(res.getString("gender"));
          phoneInput.setText(res.getString("phone"));
          birthInput.setText(res.getString("birth"));
          idInput.setText(res.getString("idCard"));
        })
        .addOnFailureListener(e -> {
          Log.e("Load data FireStore", "Failed to load user data's document in FireStore, log: " + e);
          Toast.makeText(this, "Có lỗi trong quá trình xử lý", Toast.LENGTH_SHORT).show();
          finish();
        });
  }

  private String validateDataField(Map<String, Object> rawData, Map<String, Object> resRawData) {
    if (resRawData.get("email").equals(rawData.get("email")) &&
        resRawData.get("username").equals(rawData.get("username")) &&
        resRawData.get("gender").equals(rawData.get("username")) &&
        resRawData.get("phone").equals(rawData.get("phone")) &&
        resRawData.get("birth").equals(rawData.get("birth")) &&
        (resRawData.get("idCard") != null && resRawData.get("idCard").equals(rawData.get("idCard"))) &&
        imgUri == null)
      return "Không có sự thay đổi";

    if (!rawData.get("email").equals("") && !ValidateUtils.checkEmail(rawData.get("email").toString()))
      return "Định dạng email không đúng";

    if (!rawData.get("phone").equals("") && !ValidateUtils.checkPhoneNumber(rawData.get("phone").toString()))
      return "Số điện thoại không đúng định dạng";

    if (!rawData.get("birth").equals("") && ValidateUtils.checkBirthDay(rawData.get("birth").toString()))
      return "Bạn chưa đủ tuổi đăng ký";

    if (!rawData.get("idCard").equals("") && ValidateUtils.checkIdCard(rawData.get("idCard").toString()))
      return "CCCD/ CMND không đúng";

    return "";
  }

  private void handleUpdateUserInfo(String uid, String email, String username, String gender, String phone, String birth, String id, String photoPath, String photoUrl) {
    Map<String, Object> payload = new HashMap<>();
    payload.put("email", email);
    payload.put("name", username);
    payload.put("gender", gender);
    payload.put("phone", phone);
    payload.put("birth", birth);
    payload.put("idCard", id);
    payload.put("photoFilePath", photoPath);
    payload.put("photoUrl", photoUrl);

    fireStore.collection("Users").document(uid).update(payload)
        .addOnSuccessListener(t -> {
          Toast.makeText(this, "Cập nhật thông tin thành công", Toast.LENGTH_SHORT).show();
          startActivity(new Intent(this, MainActivity.class));
        })
        .addOnFailureListener(e -> {
          Toast.makeText(this, "Cập nhật thông tin thất bại", Toast.LENGTH_SHORT).show();
          Log.e("Add data FireStore", "Failed to add user data's document to FireStore, log: " + e);
        });
  }
}