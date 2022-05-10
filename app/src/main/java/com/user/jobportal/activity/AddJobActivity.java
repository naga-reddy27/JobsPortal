package com.user.jobportal.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.user.jobportal.R;
import com.user.jobportal.db.DBHelper;

public class AddJobActivity extends AppCompatActivity {
    private EditText edtJobName, edtOrg, edtMobile, edtEmail, edtRequiredSkills, edtPackage, edtCurrentAddress;
    private Button btnAddJob;
    private DBHelper db;
    private String adminId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_job);
        edtJobName = findViewById(R.id.edt_job_name);
        edtOrg = findViewById(R.id.edt_org);
        edtMobile = findViewById(R.id.edt_mobile);
        edtEmail = findViewById(R.id.edt_email);
        edtRequiredSkills = findViewById(R.id.edt_skills);
        edtPackage = findViewById(R.id.edt_package);
        edtCurrentAddress = findViewById(R.id.edt_address);
        btnAddJob = findViewById(R.id.btn_add_job);
        getIntentData();
        db = new DBHelper(AddJobActivity.this);
        btnAddJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String jobName = edtJobName.getText().toString();
                String org = edtOrg.getText().toString();
                String mobile = edtMobile.getText().toString();
                String email = edtEmail.getText().toString();
                String requiredSkills = edtRequiredSkills.getText().toString();
                String packageDetails = edtPackage.getText().toString();
                String currentDetails = edtCurrentAddress.getText().toString();
                String appliedStatus = "NOT APPLIED";
                if (validateFields(jobName, org, mobile, email, requiredSkills, packageDetails, currentDetails, appliedStatus)) {
                    long result = db.addJob(adminId, jobName, org, mobile, email, requiredSkills, packageDetails, currentDetails, appliedStatus);
                    if (result == -1) {
                        Toast.makeText(AddJobActivity.this, "FAILED ", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(AddJobActivity.this, HomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("adminId", adminId);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    Toast.makeText(AddJobActivity.this, "Invalid Details, Please enter job details ", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void getIntentData() {
        if (getIntent().hasExtra("adminId")) {
            adminId = getIntent().getStringExtra("adminId");
        }
    }

    private boolean validateFields(String jobName, String org, String mobile, String email, String requiredSkills, String packageDetails, String currentDetails, String appliedStatus) {
        if (jobName.isEmpty() || org.isEmpty() || mobile.isEmpty() || mobile.length() < 10 || email.isEmpty() || requiredSkills.isEmpty() || packageDetails.isEmpty() || currentDetails.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }
}