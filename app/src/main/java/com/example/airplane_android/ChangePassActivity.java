package com.example.airplane_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.airplane_android.utils.ValidateUtils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePassActivity extends AppCompatActivity {

  ImageButton btnBack;
  Button btnSubmit;
  TextInputEditText oldPassInput, newPassInput, newRePassInput;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_change_pass);

    btnBack = findViewById(R.id.back);
    btnSubmit = findViewById(R.id.submit);
    oldPassInput = findViewById(R.id.oldPassInput);
    newPassInput = findViewById(R.id.newPassInput);
    newRePassInput = findViewById(R.id.newRePassInput);
  }

  @Override
  protected void onStart() {
    super.onStart();

    btnBack.setOnClickListener(v -> finish());

    btnSubmit.setOnClickListener(v -> {
      final String oldPassValue = oldPassInput.getText().toString();
      final String newPassValue = newPassInput.getText().toString();
      final String newRePassValue = newRePassInput.getText().toString();

      final String validateBasicRes = validateBasicField(oldPassValue, newPassValue, newRePassValue);
      if (!validateBasicRes.equals("")) {
        Toast.makeText(this, validateBasicRes, Toast.LENGTH_SHORT).show();
        return;
      }

      FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
      currentUser.updatePassword(newPassValue)
          .addOnSuccessListener(res -> {
            Toast.makeText(this, "Thay đổi thành công", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, MainActivity.class));
          })
          .addOnFailureListener(e -> {
            Log.e("Change password Authentication", "Failed to change a new password, log: " + e);
            Toast.makeText(this, "Thay đổi thất bại", Toast.LENGTH_SHORT).show();
          });
    });
  }

  private boolean checkIsEmptyField(String oldPassValue, String newPassValue, String newRePassValue) {
    return oldPassValue.equals("") && newPassValue.equals("") && newRePassValue.equals("");
  }

  private String validateBasicField(String oldPassValue, String newPassValue, String newRePassValue) {
    if (checkIsEmptyField(oldPassValue, newPassValue, newRePassValue)) return "Không được để trống";
    if (!ValidateUtils.checkPassword(oldPassValue) || !ValidateUtils.checkPassword(newPassValue) || !ValidateUtils.checkPassword(newRePassValue))
      return "Mật khẩu phải có ít nhất 8 ký tự và phải có ít nhát 1 ký tự viết hoa";
    if (oldPassValue.equals(newPassValue)) return "Mật khẩu mới phải khác mật khẩu cũ";
    if (!newPassValue.equals(newRePassValue)) return "Mật khẩu không khớp nhau";

    return "";
  }
}