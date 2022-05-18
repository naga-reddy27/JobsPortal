package com.user.jobportal.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.user.jobportal.R;
import com.user.jobportal.db.DBHelper;
import com.user.jobportal.model.JobModel;

import java.util.ArrayList;
import java.util.Arrays;

public class AddJobActivity extends AppCompatActivity {
    private EditText edtJobName, edtOrg, edtMobile, edtEmail, edtRequiredSkills, edtPackage, edtCurrentAddress;
    private Button btnAddJob;
    private DBHelper db;
    private String adminId;
    private String userId;
    private String action;
    private JobModel jobModel;
    private SharedPreferences sp;

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
        sp = getSharedPreferences("user",
                MODE_PRIVATE);

        if (sp.contains("adminId") || sp.contains("id")) {
            adminId = sp.getString("adminId", "");
            userId = sp.getString("id", "");
            Log.v("ADMIN ", "ADMIN ID :: " + adminId);
        }
        db = new DBHelper(AddJobActivity.this);
        btnAddJob.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
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
                if (action.equals("more")) {
                    appliedStatus = "Applied";
                    ArrayList userIdList = db.getAppliedJobUserIdList(jobModel.getJobId());
                    String[] Total_Score = new String[0];
                    for (int i = 0; i < userIdList.size(); i++) {
                        if (!userIdList.contains(userId.trim())) {
                            if (userIdList.contains("")) {
                                Total_Score = new String[]{userId};
                            } else {
                                Total_Score = new String[]{String.valueOf(userIdList.get(i)), userId};
                            }
                            // Converting it into a single string
                            String result_ScoreP1 = ("" + Arrays.asList(Total_Score)).
                                    replaceAll("(^.|.$)", "  ").replace(", ", ",");
                            long result = db.updateJob(jobModel.getAdminId(), jobModel.getJobId(), jobName, org, mobile, email, requiredSkills, packageDetails, currentDetails, appliedStatus, result_ScoreP1);

                            if (result == -1) {
                                Toast.makeText(AddJobActivity.this, "Failed to apply", Toast.LENGTH_SHORT).show();
                            } else {
                                db.getAppliedJobUserIdList(jobModel.getJobId());
                                setResult(RESULT_OK);
                                finish();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Already Applied", Toast.LENGTH_SHORT).show();
                        }
                    }

                } else {
                    if (validateFields(jobName, org, mobile, email, requiredSkills, packageDetails, currentDetails, appliedStatus)) {
                        long result;
                        if (jobModel != null && jobModel.getJobId() != null) {
                            result = db.updateJob(adminId, jobModel.getJobId(), jobName, org, mobile, email, requiredSkills, packageDetails, currentDetails, appliedStatus, "");
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
            }
        });

    }

    private void getIntentData() {
        if (getIntent().hasExtra("adminId")) {
            adminId = getIntent().getStringExtra("adminId");
            userId = getIntent().getStringExtra("id");
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
                setFieldsDisable();
            } else {
                setFieldsEnable();
            }
            if (action.equals("more")) {
                btnAddJob.setText("Apply");
                setFieldsDisable();
                btnAddJob.setVisibility(View.VISIBLE);
                btnAddJob.setEnabled(true);
            }
        }
    }

    public void setFieldsEnable() {
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

    public void setFieldsDisable() {
        btnAddJob.setVisibility(View.GONE);
        edtJobName.setEnabled(false);
        edtOrg.setEnabled(false);
        edtMobile.setEnabled(false);
        edtEmail.setEnabled(false);
        edtPackage.setEnabled(false);
        edtRequiredSkills.setEnabled(false);
        edtCurrentAddress.setEnabled(false);
        btnAddJob.setEnabled(false);
    }

    private boolean validateFields(String jobName, String org, String mobile, String email, String requiredSkills, String packageDetails, String currentDetails, String appliedStatus) {
        if (jobName.isEmpty() || org.isEmpty() || mobile.isEmpty() || mobile.length() < 10 || email.isEmpty() || requiredSkills.isEmpty() || packageDetails.isEmpty() || currentDetails.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //  moveTaskToBack(true);
    }
}