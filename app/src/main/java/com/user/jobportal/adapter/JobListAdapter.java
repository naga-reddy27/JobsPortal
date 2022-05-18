package com.user.jobportal.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.user.jobportal.R;
import com.user.jobportal.activity.HomeActivity;
import com.user.jobportal.db.DBHelper;
import com.user.jobportal.model.JobModel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class JobListAdapter extends RecyclerView.Adapter<JobListAdapter.MyViewHolder> {
    private Context context;
    private List<JobModel> jobsList;
    private ItemClickListener itemClickListener;
    private String adminId;
    private String userId;
    private String listType;
    private DBHelper db;

    public JobListAdapter(Context context, List<JobModel> jobsList, String adminId, String userId, String listType, ItemClickListener itemClickListener) {
        this.context = context;
        this.jobsList = jobsList;
        this.adminId = adminId;
        this.userId = userId;
        this.listType = listType;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.job_list_item, parent, false);
        return new MyViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull JobListAdapter.MyViewHolder holder, int position) {
        JobModel job = jobsList.get(position);
        db = new DBHelper(context);
        holder.txtJobName.setText(job.getJobName());
        holder.txtOrg.setText(job.getOrg());
        holder.txtSkills.setText(job.getSkillRequired());
        holder.txtAddress.setText(job.getCurrentAddress());

        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.onItemClick(job, position, "edit");
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.onItemClick(job, position, "view");
            }
        });
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.onItemClick(job, position, "delete");
            }
        });
        if (adminId.equals("")) {
            holder.btnLayout.setVisibility(View.GONE);
            holder.layoutMore.setVisibility(View.VISIBLE);
            holder.imgMore.setVisibility(View.VISIBLE);
        } else {
            holder.layoutMore.setVisibility(View.GONE);
            holder.btnLayout.setVisibility(View.VISIBLE);
            holder.imgMore.setVisibility(View.GONE);
        }
        holder.imgMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.onItemClick(job, position, "more");
            }
        });
        Log.v("ADAPTER ", "USER IDS :: " + job.getUserIds().toString());
        if (job.getUserIds().contains(userId)) {
            holder.txtStatus.setText("Applied");
            holder.txtStatus.setTextColor(ContextCompat.getColor(context, R.color.teal_200));
        } else {
            holder.txtStatus.setText("Not Applied");
            holder.txtStatus.setTextColor(ContextCompat.getColor(context, R.color.black));
        }
        ArrayList userIdList = db.getAppliedJobUserIdList(job.getJobId());
        Log.v("ADAPTER ", " USER LIST " + userIdList.toString());
        if (userIdList != null && userIdList.size() > 0) {
            StringBuilder username = new StringBuilder();
            for (int i = 0; i < userIdList.size(); i++) {
                String usernameStr = db.getUserNameById("" + userIdList.get(i));
                System.out.println(" -->" + userIdList.get(i) + "\n" + username);
                if (!username.equals(usernameStr)) {
                    username.append(" " + usernameStr);
                }
                if (listType.equals("appliedJobsByAdmin")) {
                    holder.layoutMore.setVisibility(View.VISIBLE);
                    holder.txtStatus.setText("Applied by " + username);
                    holder.btnLayout.setVisibility(View.GONE);
                }
            }
        }

    }
    public void filteredList(List<JobModel> jobsList){
        this.jobsList = jobsList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return jobsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView txtJobName, txtOrg, txtSkills, txtAddress, txtStatus;
        private ImageView btnEdit, btnDelete;
        private LinearLayout btnLayout, layoutMore;
        private ImageView imgMore;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtJobName = itemView.findViewById(R.id.txt_job_name);
            txtOrg = itemView.findViewById(R.id.txt_job_org);
            txtSkills = itemView.findViewById(R.id.txt_job_skills);
            txtAddress = itemView.findViewById(R.id.txt_address);
            btnDelete = itemView.findViewById(R.id.txt_delete_job);
            btnEdit = itemView.findViewById(R.id.txt_edit_job);
            btnLayout = itemView.findViewById(R.id.btn_layout);
            imgMore = itemView.findViewById(R.id.img_more);
            txtStatus = itemView.findViewById(R.id.txt_status);
            layoutMore = itemView.findViewById(R.id.layout_more);
        }
    }

    public interface ItemClickListener {
        public void onItemClick(JobModel jobModel, int position, String action);
    }
}
