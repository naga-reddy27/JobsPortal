package com.user.jobportal.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.user.jobportal.R;
import com.user.jobportal.db.DBHelper;

public class RegisterActivity extends AppCompatActivity {
    private EditText edtUsername, edtMobile, edtEmail, edtAddress, edtPwd;
    private Button btnRegister;
    private DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setTitle("Register");
        TextView txtRegister = findViewById(R.id.txt_login);
        edtUsername = findViewById(R.id.edt_username);
        edtMobile = findViewById(R.id.edt_mobile);
        edtEmail = findViewById(R.id.edt_email);
        edtAddress = findViewById(R.id.edt_address);
        edtPwd = findViewById(R.id.edt_pwd);
        btnRegister = findViewById(R.id.btn_register);
        db = new DBHelper(RegisterActivity.this);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = edtUsername.getText().toString();
                String mobile = edtMobile.getText().toString();
                String email = edtEmail.getText().toString();
                String address = edtAddress.getText().toString();
                String pwd = edtPwd.getText().toString();
                if (validate(username, mobile, email, address, pwd)) {
                    long result = db.addUser(username, mobile, email, address, pwd);
                    if (result == -1) {
                        Toast.makeText(getApplicationContext(), "Registration Failed", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Enter Valid details", Toast.LENGTH_SHORT).show();
                }
            }
        });
        txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private boolean validate(String username, String mobile, String email, String address, String pwd) {
        if (username.isEmpty() || mobile.isEmpty() || mobile.length() < 10 || email.isEmpty() || address.isEmpty() || pwd.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }
}