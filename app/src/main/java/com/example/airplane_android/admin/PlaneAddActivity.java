package com.example.airplane_android.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.airplane_android.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class PlaneAddActivity extends AppCompatActivity {
    Button btnReturnListView,btnSave;
    EditText txtHangBay,txtLoai,txtSucChua,txtMaMayBay;
    FirebaseFirestore firestore;
    RadioGroup radioGroupActive;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plane_add);
        firestore = FirebaseFirestore.getInstance();
        btnReturnListView = findViewById(R.id.backToList);
        btnSave = findViewById(R.id.submitAddPlane);
        txtHangBay = findViewById(R.id.textThemHangBay);
        txtLoai = findViewById(R.id.textThemLoai);
        txtSucChua = findViewById(R.id.textThemSucChua);
        txtMaMayBay = findViewById(R.id.textThemMaMayBay);
        final boolean[] isActive = {false};
        radioGroupActive = findViewById(R.id.radioGroupActive);
        radioGroupActive.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton selectedRadioButton = findViewById(checkedId);

                if (selectedRadioButton != null) {
                    if (selectedRadioButton.getId() == R.id.radioActive) {
                        isActive[0] = true;
                        Toast.makeText(PlaneAddActivity.this,"Kích Hoạt",Toast.LENGTH_SHORT).show();
                    } else {
                        isActive[0] = false;
                        Toast.makeText(PlaneAddActivity.this,"Không Kích Hoạt",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        btnReturnListView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToList = new Intent(PlaneAddActivity.this,PlaneActivity.class);
                startActivity(intentToList);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textId = txtMaMayBay.getText().toString().trim();
                String textHang = txtHangBay.getText().toString().trim();
                String textLoai = txtLoai.getText().toString().trim();
                String textSucChua = txtSucChua.getText().toString().trim();
                if(textId.isEmpty() || textHang.isEmpty() || textLoai.isEmpty() || textSucChua.isEmpty()){
                    Toast.makeText(PlaneAddActivity.this,"Các Ô Không Được Phép Bỏ Trống",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!textSucChua.isEmpty() && !TextUtils.isDigitsOnly(textSucChua)){
                    Toast.makeText(PlaneAddActivity.this,"Vui Lòng Nhập Chữ Số",Toast.LENGTH_SHORT).show();
                    return;
                }
                int selectedRadioButtonId = radioGroupActive.getCheckedRadioButtonId();

                if (selectedRadioButtonId == -1) {
                    Toast.makeText(PlaneAddActivity.this, "Vui Lòng Chọn Trạng Thái", Toast.LENGTH_SHORT).show();
                    return;
                }
                uploadData(textId,textHang,textLoai,textSucChua,isActive[0]);
            }

            private void uploadData(String id,String textHang, String textLoai, String textSucChua, boolean b) {
                Map<String, Object> doc = new HashMap<>();
                doc.put("Id",id);
                doc.put("brand",textHang);
                doc.put("type",textLoai);
                doc.put("capacity",Integer.parseInt(textSucChua));
                doc.put("active",b);

                firestore.collection("Plane").document(id)
                        .set(doc)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(PlaneAddActivity.this,"Tạo Mới Thành Công",Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }
}