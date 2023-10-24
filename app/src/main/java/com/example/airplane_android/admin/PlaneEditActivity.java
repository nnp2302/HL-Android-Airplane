package com.example.airplane_android.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.airplane_android.R;
import com.example.airplane_android.admin.model.Plane;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

public class PlaneEditActivity extends AppCompatActivity {
    EditText textSuaHangBay, textSuaLoai,textSuaSucChua;
    RadioGroup radioGroupActive;
    Button submitEditPlane,backToList;
    FirebaseFirestore firestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plane_edit);
        firestore = FirebaseFirestore.getInstance();
        submitEditPlane = findViewById(R.id.submitEditPlane);
        backToList = findViewById(R.id.backEditToList);
        backToList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToList = new Intent(PlaneEditActivity.this,PlaneActivity.class);
                startActivity(intentToList);
            }
        });
        // lấy dữ liệu
        Bundle bundle = getIntent().getExtras();
        Plane plane = (Plane) bundle.get("plane");
        if(bundle !=null){

            textSuaHangBay = findViewById(R.id.textSuaHangBay);
            textSuaLoai = findViewById(R.id.textSuaLoai);
            textSuaSucChua = findViewById(R.id.textSuaSucChua);

            textSuaHangBay.setText(plane.getBrand());
            textSuaLoai.setText(plane.getType());
            textSuaSucChua.setText(plane.getCapacity().toString());
            // lấy value isActive
            boolean planeActive = plane.isActive();

            radioGroupActive = findViewById(R.id.radioEditGroupActive);

            if (planeActive) {
                radioGroupActive.check(R.id.radioEditActive);
            } else {
                radioGroupActive.check(R.id.radioEditNoActive);
            }
        }
        radioGroupActive = findViewById(R.id.radioEditGroupActive);
        final boolean[] isActive = {false};
        radioGroupActive.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton selectedRadioButton = findViewById(checkedId);

                if (selectedRadioButton != null) {
                    if (selectedRadioButton.getId() == R.id.radioEditActive) {
                        isActive[0] = true;
                        Log.d("IsActive", "true");
                    } else {
                        isActive[0] = false;
                        Log.d("IsActive", "false");
                    }
                }
            }
        });
        submitEditPlane.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = plane.getId().toString();
                String textHang = textSuaHangBay.getText().toString().trim();
                String textLoai = textSuaLoai.getText().toString().trim();
                String textSucChua = textSuaSucChua.getText().toString().trim();

                UpdateData(id,textHang,textLoai,textSucChua,isActive[0]);
            }
        });
    }

    private void UpdateData(String id, String textHang, String textLoai, String textSucChua, boolean b) {
        firestore.collection("Plane")
                .document(id)
                .update("Id",id,"brand",textHang,
                        "type",textLoai,"capacity",Integer.parseInt(textSucChua),
                        "active",b
                ).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(PlaneEditActivity.this,"Cập nhật thành công!!",Toast.LENGTH_SHORT).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }
}