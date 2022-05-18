package com.user.jobportal.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.user.jobportal.R;

public class MainActivity extends AppCompatActivity {
    private SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btnLogin = findViewById(R.id.btn_login);
        Button btnRegister = findViewById(R.id.btn_register);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

    }
    private void checkUserAuth() {
        sp = getSharedPreferences("user",
                MODE_PRIVATE);
        if (sp.contains("adminId") || sp.contains("id")) {
            String adminId = sp.getString("adminId", "");
            String userId = sp.getString("id", "");
            String username = sp.getString("username", "");
            Log.v("ADMIN ", "ADMIN ID :: " + adminId);
            if (!adminId.equals("")) {
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            } else if (!userId.equals("")) {
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        checkUserAuth();
    }
}