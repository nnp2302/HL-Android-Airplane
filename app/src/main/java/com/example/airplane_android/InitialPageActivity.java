package com.example.airplane_android;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class InitialPage extends AppCompatActivity {

    private ImageView imgLogo;
    private TextView textLogo;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial_page);

        imgLogo = (ImageView) findViewById(R.id.imgLogo);
        textLogo = (TextView) findViewById(R.id.textLogo);

        imgLogo.setVisibility(View.INVISIBLE);
        textLogo.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onStart() {
        super.onStart();

        imgLogo = (ImageView) findViewById(R.id.imgLogo);
        textLogo = (TextView) findViewById(R.id.textLogo);

        handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                imgLogo.setVisibility(View.VISIBLE);
                textLogo.setVisibility(View.VISIBLE);
            }
        }, 2000);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(InitialPage.this, MainActivity.class);
                startActivity(intent);
            }
        }, 5000);

    }
}