package com.example.hp.edufun;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class splash_screen extends AppCompatActivity {
    private  static int screen_time_out = 4000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(splash_screen.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        },screen_time_out);


    }
}
