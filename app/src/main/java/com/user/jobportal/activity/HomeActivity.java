package com.user.jobportal.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Shader;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.user.jobportal.R;
import com.user.jobportal.adapter.JobListAdapter;
import com.user.jobportal.db.DBHelper;
import com.user.jobportal.model.JobModel;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements JobListAdapter.ItemClickListener {
    private final String TAG = HomeActivity.this.getClass().getSimpleName();
    private FloatingActionButton fab;
    private EditText edtSearchJob;
    private RecyclerView recyclerView;
    private TextView txtNoData;
    private String adminId;
    private String userId;
    private String username;
    private DBHelper db;
    private List<JobModel> jobList;
    private JobListAdapter adapter;
    private static final int REQUEST_CODE = 1;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        fab = findViewById(R.id.fab_button);
        recyclerView = findViewById(R.id.recycler_view);
        edtSearchJob = findViewById(R.id.search_view);
        txtNoData = findViewById(R.id.nodata);
        db = new DBHelper(HomeActivity.this);

        sp = getSharedPreferences("user",
                MODE_PRIVATE);

        if (sp.contains("adminId") || sp.contains("id")) {
            adminId = sp.getString("adminId", "");
            userId = sp.getString("id", "");
            username = sp.getString("username", "");
            Log.v("ADMIN ", "ADMIN ID :: " + adminId);
            if (!adminId.equals("")) {
                setTitle("ADMIN" + " \n" + username);
                fab.setVisibility(View.VISIBLE);
                getJobListByAdminIdFromDb();
            } else if (!userId.equals("")) {
                setTitle(username);
                fab.setVisibility(View.GONE);
                getAllJobsList();
            }
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startAddJobActivity(null, null, "new");
            }
        });
        edtSearchJob.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filterList(editable.toString());
            }
        });

    }

    private void filterList(String str) {
        List<JobModel> filterList = new ArrayList<>();
        if (jobList != null && jobList.size() > 0) {
            for (JobModel job : jobList) {
                if (job.getJobName().toLowerCase().contains(str.toLowerCase()) || job.getOrg().toLowerCase().contains(str.toLowerCase())) {
                    filterList.add(job);
                }
            }
        }
        adapter.filteredList(filterList);
    }

    private void getAllJobsList() {
        Cursor cursor = db.getAllJobList();
        jobList = new ArrayList<>();
        if (cursor != null && cursor.getCount() == 0) {
            if (jobList.size() == 0) {
                txtNoData.setVisibility(View.VISIBLE);
            }
        } else {
            txtNoData.setVisibility(View.GONE);

            while (cursor.moveToNext()) {
                JobModel model = new JobModel(cursor.getString(1),
                        cursor.getString(0),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(8),
                        cursor.getString(5),
                        cursor.getString(6),
                        cursor.getString(7),
                        cursor.getString(10),
                        cursor.getString(9)
                );
                jobList.add(model);
                Log.v(TAG, "JOBS LIST :: ");

            }
        }
        if (jobList != null) {
            setAdapter("all");
        }
    }

    void getJobListByAdminIdFromDb() {
        Cursor cursor = db.getJobListByAdminId(adminId);
        jobList = new ArrayList<>();
        if (cursor.getCount() == 0) {
            if (jobList.size() == 0) {
                txtNoData.setVisibility(View.VISIBLE);
            }
        } else {
            txtNoData.setVisibility(View.GONE);

            while (cursor.moveToNext()) {
                JobModel model = new JobModel(cursor.getString(1),
                        cursor.getString(0),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(8),
                        cursor.getString(5),
                        cursor.getString(6),
                        cursor.getString(7),
                        cursor.getString(10),
                        cursor.getString(9)
                );
                jobList.add(model);
                Log.v(TAG, "JOBS LIST :: ");

            }

        }
        if (jobList != null) {
            setAdapter("jobByAdmin");
        }
    }

    public void setAdapter(String listType) {
        adapter = new JobListAdapter(HomeActivity.this, jobList, adminId, userId, listType, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(HomeActivity.this));
        //    recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.all_jobs) {
            jobList.clear();
            if (!adminId.equals("")) {
                getJobListByAdminIdFromDb();
            } else if (!userId.equals("")) {
                getAllJobsList();
            }
            return true;
        } else if (id == R.id.applied_jobs) {
            jobList.clear();
            if (!adminId.equals("")) {
                getAppliedJobListByAdminId();
            } else if (!userId.equals("")) {
                getAppliedJobListByUserId();
            }
            return true;
        } else if (id == R.id.logout) {
            Intent intent = new Intent(HomeActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void getAppliedJobListByUserId() {
        Cursor cursor = db.getAppliedJobListByUserId("Applied");
        jobList = new ArrayList<>();
        if (cursor.getCount() == 0) {
            if (jobList.size() == 0) {
                txtNoData.setVisibility(View.VISIBLE);
            }
        } else {
            while (cursor.moveToNext()) {
                JobModel model = new JobModel(cursor.getString(1),
                        cursor.getString(0),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(8),
                        cursor.getString(5),
                        cursor.getString(6),
                        cursor.getString(7),
                        cursor.getString(10),
                        cursor.getString(9)
                );
                Log.v(TAG, "JOBS LIST :: " + model.getUserIds().contains(userId));

                if (model.getUserIds().contains(userId)) {
                    jobList.add(model);
                }
                if (jobList.size() == 0) {
                    txtNoData.setVisibility(View.VISIBLE);
                } else {
                    txtNoData.setVisibility(View.GONE);
                }

            }

        }
        if (jobList != null) {
            setAdapter("appliedJobsByUser");
        }
    }

    private void getAppliedJobListByAdminId() {
        Cursor cursor = db.getAppliedJobListByAdminId(adminId, "Applied");
        jobList = new ArrayList<>();
        if (cursor.getCount() == 0) {
            if (jobList.size() == 0) {
                txtNoData.setVisibility(View.VISIBLE);
            }
        } else {
            txtNoData.setVisibility(View.GONE);
            while (cursor.moveToNext()) {
                JobModel model = new JobModel(cursor.getString(1),
                        cursor.getString(0),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(8),
                        cursor.getString(5),
                        cursor.getString(6),
                        cursor.getString(7),
                        cursor.getString(10),
                        cursor.getString(9)
                );
                jobList.add(model);
                Log.v(TAG, "JOBS LIST :: ");

            }
        }
        if (jobList != null) {
            setAdapter("appliedJobsByAdmin");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            Log.v(TAG, "HOME RECREATED :: ");

            if (!adminId.equals("")) {
                getJobListByAdminIdFromDb();
            } else if (!userId.equals("")) {
                getAllJobsList();
            }
        }
    }

    @Override
    public void onItemClick(JobModel jobModel, int position, String action) {
        if (action.equals("delete")) {
            confirmation(jobModel.getJobId());
        } else {
            startAddJobActivity(jobModel, position, action);
        }
    }

    private void startAddJobActivity(JobModel jobModel, Integer pos, String action) {
        Intent intent = new Intent(HomeActivity.this, AddJobActivity.class);
        intent.putExtra("adminId", adminId);
        intent.putExtra("job", jobModel);
        intent.putExtra("action", action);
        intent.putExtra("pos", pos);
        startActivityForResult(intent, REQUEST_CODE);
    }

    private void confirmation(String id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
        builder.setTitle("Delete ");
        builder.setMessage("Are you sure, You want to delete this Job ?");
        builder.setPositiveButton("Delete ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                long result = db.deleteJob(id);
                if (result == -1) {
                    Toast.makeText(getApplicationContext(), "Data not Deleted", Toast.LENGTH_SHORT).show();
                } else {
                    dialogInterface.dismiss();
                    getJobListByAdminIdFromDb();
                }
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.create().show();

    }
}