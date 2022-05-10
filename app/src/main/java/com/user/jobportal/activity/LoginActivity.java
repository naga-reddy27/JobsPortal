package com.user.jobportal.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.user.jobportal.R;

public class LoginActivity extends AppCompatActivity {
    EditText edtUsername;
    EditText edtPwd;
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("Login");
        TextView txtRegister = findViewById(R.id.txt_register);
        edtUsername = findViewById(R.id.edt_username);
        edtPwd = findViewById(R.id.edt_pwd);
        btnLogin = findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = edtUsername.getText().toString();
                String pwd = edtPwd.getText().toString();
                if (!username.isEmpty() && !pwd.isEmpty()) {
                    if ((username.equals("admin1") && pwd.equals("12345"))) {
                        startHomeActivity("1");
                    } else if ( (username.equals("admin2") && pwd.equals("54321"))){
                        startHomeActivity("2");
                    }else {

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
    void startHomeActivity(String adminId){
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        intent.putExtra("adminId", adminId);
        startActivity(intent);
    }
}