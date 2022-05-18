package com.user.jobportal.model;

import java.io.Serializable;
import java.util.List;

public class JobModel implements Serializable {
    private String adminId;
    private String jobId;
    private String jobName;
    private String org;
    private String mobile;
    private String email;
    private String skillRequired;
    private String packageDetails;
    private String currentAddress;
    private String appliedStatus;
    private String userIds;

    public JobModel(String adminId,String jobId, String jobName, String org,
                    String mobile, String email, String skillRequired,
                    String packageDetails, String currentAddress, String appliedStatus, String userIds) {
        this.adminId = adminId;
        this.jobId = jobId;
        this.jobName = jobName;
        this.org = org;
        this.mobile = mobile;
        this.email = email;
        this.skillRequired = skillRequired;
        this.packageDetails = packageDetails;
        this.currentAddress = currentAddress;
        this.appliedStatus = appliedStatus;
        this.userIds = userIds;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getOrg() {
        return org;
    }

    public void setOrg(String org) {
        this.org = org;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSkillRequired() {
        return skillRequired;
    }

    public void setSkillRequired(String skillRequired) {
        this.skillRequired = skillRequired;
    }

    public String getPackageDetails() {
        return packageDetails;
    }

    public void setPackageDetails(String packageDetails) {
        this.packageDetails = packageDetails;
    }

    public String getCurrentAddress() {
        return currentAddress;
    }

    public void setCurrentAddress(String currentAddress) {
        this.currentAddress = currentAddress;
    }

    public String getAppliedStatus() {
        return appliedStatus;
    }

    public void setAppliedStatus(String appliedStatus) {
        this.appliedStatus = appliedStatus;
    }

    public String getUserIds() {
        return userIds;
    }

    public void setUserIds(String userIds) {
        this.userIds = userIds;
    }
}
