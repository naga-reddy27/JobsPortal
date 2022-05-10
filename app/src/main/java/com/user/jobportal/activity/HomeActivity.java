package com.user.jobportal.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
    private FloatingActionButton fab;
    private RecyclerView recyclerView;
    private TextView txtNoData;
    private String adminId;
    private DBHelper db;
    private List<JobModel> jobList;
    private JobListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setTitle("Home");
        fab = findViewById(R.id.fab_button);
        recyclerView = findViewById(R.id.recycler_view);
        txtNoData = findViewById(R.id.nodata);
        db = new DBHelper(HomeActivity.this);

        getIntentData();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, AddJobActivity.class);
                intent.putExtra("adminId", adminId);
                startActivity(intent);

            }
        });

    }

    private void getIntentData() {
        if (getIntent().hasExtra("adminId")) {
            adminId = getIntent().getStringExtra("adminId");
        }
        getJobListFromDb();
    }

    void getJobListFromDb() {
        Cursor cursor = db.getJobList();
        if (cursor.getCount() == 0) {
            txtNoData.setVisibility(View.VISIBLE);
            // Toast.makeText(getApplicationContext(), "No Data", Toast.LENGTH_SHORT).show();
        } else {
            txtNoData.setVisibility(View.GONE);
            jobList = new ArrayList<>();
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
                        cursor.getString(9)
                );
                jobList.add(model);
                setAdapter();
            }
        }
    }

    public void setAdapter() {
        adapter = new JobListAdapter(HomeActivity.this, jobList, this);
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
            setAdapter();
            Toast.makeText(HomeActivity.this, "All Jobs Action clicked", Toast.LENGTH_LONG).show();
            return true;
        } else if (id == R.id.applied_jobs) {
            Toast.makeText(HomeActivity.this, "Applied Jobs Action clicked", Toast.LENGTH_LONG).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onItemClick(JobModel jobModel, int position) {
        startAddJobActivity(jobModel, position);
    }

    private void startAddJobActivity(JobModel jobModel, int pos) {
        Intent intent = new Intent(HomeActivity.this, AddJobActivity.class);
        intent.putExtra("adminId", adminId);
        startActivity(intent);
    }
}