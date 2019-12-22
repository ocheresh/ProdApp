package com.example.prodapp.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.prodapp.R;
import com.example.prodapp.View.ChoiseMenu.ChoiseMenuView;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashActivity.this, ChoiseMenuView.class);
                startActivity(i);
                finish();
            }
        }, 1 * 1000);
    }
}
