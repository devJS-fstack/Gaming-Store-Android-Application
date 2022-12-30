package com.example.middle_group2;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class NotificationActivity extends AppCompatActivity {

    private Button btnBackToHome;
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification);
        intent = new Intent(NotificationActivity.this, MainActivity.class);
        setControl();
        onBackToHome();
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                handleBackToHome();
            }
        };
        this.getOnBackPressedDispatcher().addCallback(this, callback);
    }

    private void onBackToHome() {
        btnBackToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleBackToHome();
            }
        });
    }
    private void handleBackToHome() {
        finish();
        startActivity(intent);
    }

    private void setControl() {
        btnBackToHome = findViewById(R.id.btnBackToHome);
    }
}