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
import com.user.jobportal.model.JobModel;

public class AddJobActivity extends AppCompatActivity {
    private EditText edtJobName, edtOrg, edtMobile, edtEmail, edtRequiredSkills, edtPackage, edtCurrentAddress;
    private Button btnAddJob;
    private DBHelper db;
    private String adminId;
    private String action;
    private JobModel jobModel;

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
                    long result;
                    if (jobModel.getJobId() != null) {
                        result = db.updateJob(adminId, jobModel.getJobId(), jobName, org, mobile, email, requiredSkills, packageDetails, currentDetails, appliedStatus);
                    } else {
                        result = db.addJob(adminId, jobName, org, mobile, email, requiredSkills, packageDetails, currentDetails, appliedStatus);
                    }
                    if (result == -1) {
                        Toast.makeText(AddJobActivity.this, "FAILED ", Toast.LENGTH_SHORT).show();
                    } else {
                        setResult(RESULT_OK);
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
            action = getIntent().getStringExtra("action");
            if (!action.equals("new")) {
                jobModel = (JobModel) getIntent().getSerializableExtra("job");
                if (jobModel != null) {
                    edtJobName.setText(jobModel.getJobName());
                    edtOrg.setText(jobModel.getOrg());
                    edtMobile.setText(jobModel.getMobile());
                    edtEmail.setText(jobModel.getEmail());
                    edtPackage.setText(jobModel.getPackageDetails());
                    edtRequiredSkills.setText(jobModel.getSkillRequired());
                    edtCurrentAddress.setText(jobModel.getCurrentAddress());
                    btnAddJob.setText("Update Job");
                }
            }
            if (action.equals("view")) {
                btnAddJob.setVisibility(View.GONE);
                edtJobName.setEnabled(false);
                edtOrg.setEnabled(false);
                edtMobile.setEnabled(false);
                edtEmail.setEnabled(false);
                edtPackage.setEnabled(false);
                edtRequiredSkills.setEnabled(false);
                edtCurrentAddress.setEnabled(false);
                btnAddJob.setEnabled(false);
            } else {
                btnAddJob.setVisibility(View.VISIBLE);
                edtJobName.setEnabled(true);
                edtOrg.setEnabled(true);
                edtMobile.setEnabled(true);
                edtEmail.setEnabled(true);
                edtPackage.setEnabled(true);
                edtRequiredSkills.setEnabled(true);
                edtCurrentAddress.setEnabled(true);
                btnAddJob.setEnabled(true);
            }
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