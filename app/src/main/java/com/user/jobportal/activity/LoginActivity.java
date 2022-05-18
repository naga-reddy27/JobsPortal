package com.user.jobportal.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.user.jobportal.R;
import com.user.jobportal.db.DBHelper;

public class LoginActivity extends AppCompatActivity {
    private EditText edtUsername;
    private EditText edtPwd;
    private Button btnLogin;
    private DBHelper db;
    private SharedPreferences sp;
    public static final String MY_PREF = "user";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("Login");
        TextView txtRegister = findViewById(R.id.txt_register);
        edtUsername = findViewById(R.id.edt_username);
        edtPwd = findViewById(R.id.edt_pwd);
        btnLogin = findViewById(R.id.btn_login);
        db = new DBHelper(LoginActivity.this);
        sp = getSharedPreferences(MY_PREF,
                Context.MODE_PRIVATE);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = edtUsername.getText().toString();
                String pwd = edtPwd.getText().toString();
                if (!username.isEmpty() && !pwd.isEmpty()) {
                    if ((username.equals("admin1") && pwd.equals("12345"))) {
                        startHomeActivity("1", "", "admin2");
                    } else if ((username.equals("admin2") && pwd.equals("54321"))) {
                        startHomeActivity("2", "", "admin2");
                    } else {
                        String userId = db.loginWithDB(username, pwd);
                        if (userId != null) {
                            startHomeActivity("", userId, username);
                        } else {
                            Toast.makeText(getApplicationContext(), "Enter username and pwd", Toast.LENGTH_SHORT).show();

                        }
                    }
                }
            }
        });
        txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    void startHomeActivity(String adminId, String userId, String username) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("id", userId);
        editor.putString("adminId", adminId);
        editor.putString("username", username);
        editor.apply();
        editor.commit();
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        intent.putExtra("adminId", adminId);
        intent.putExtra("userId", userId);
        startActivity(intent);
        finish();
    }
}