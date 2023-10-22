package com.example.airplane_android.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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
    EditText txtHangBay,txtLoai,txtSucChua;
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

        final boolean[] isActive = {false};
        radioGroupActive = findViewById(R.id.radioGroupActive);
        radioGroupActive.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton selectedRadioButton = findViewById(checkedId);

                if (selectedRadioButton != null) {
                    if (selectedRadioButton.getId() == R.id.radioActive) {
                        isActive[0] = true;
                    } else {
                        isActive[0] = false;
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

                String textHang = txtHangBay.getText().toString().trim();
                String textLoai = txtLoai.getText().toString().trim();
                String textSucChua = txtSucChua.getText().toString().trim();
                uploadData(textHang,textLoai,textSucChua,isActive[0]);
            }

            private void uploadData(String textHang, String textLoai, String textSucChua, boolean b) {
                String id = UUID.randomUUID().toString();
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