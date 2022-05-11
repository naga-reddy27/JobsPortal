package com.user.jobportal.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.user.jobportal.R;
import com.user.jobportal.model.JobModel;

import java.util.List;

public class JobListAdapter extends RecyclerView.Adapter<JobListAdapter.MyViewHolder> {
    private Context context;
    private List<JobModel> jobsList;
    private ItemClickListener itemClickListener;
    private String adminId;

    public JobListAdapter(Context context, List<JobModel> jobsList, String adminId, ItemClickListener itemClickListener) {
        this.context = context;
        this.jobsList = jobsList;
        this.adminId = adminId;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.job_list_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JobListAdapter.MyViewHolder holder, int position) {
        JobModel job = jobsList.get(position);
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
            holder.imgMore.setVisibility(View.VISIBLE);
        } else {
            holder.btnLayout.setVisibility(View.VISIBLE);
            holder.imgMore.setVisibility(View.GONE);
        }
        holder.imgMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.onItemClick(job, position, "more");
            }
        });

    }

    @Override
    public int getItemCount() {
        return jobsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView txtJobName, txtOrg, txtSkills, txtAddress;
        private ImageView btnEdit, btnDelete;
        private LinearLayout btnLayout;
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
        }
    }

    public interface ItemClickListener {
        public void onItemClick(JobModel jobModel, int position, String action);
    }
}
