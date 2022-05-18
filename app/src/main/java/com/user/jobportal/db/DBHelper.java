package com.user.jobportal.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.provider.BaseColumns;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DBHelper extends SQLiteOpenHelper {
    private Context context;

    // database name
    private static final String DATABASE_NAME = "Jobs.db";
    private static final int DATABASE_VERSION = 1;
    // table name to store jobs
    private static final String TABLE_NAME_JOB = "job_table";
    private static final String COLUMN_JOB_ID = BaseColumns._ID;
    private static final String COLUMN_ADMIN_ID = "admin_id";
    // column names to store job details
    private static final String COLUMN_JOB_NAME = "job_name";
    private static final String COLUMN_ORG = "organisation";
    private static final String COLUMN_NUMBER = "phone_number";
    private static final String COLUMN_EMAIL = "email_address";
    private static final String COLUMN_CURRENT_ADDRESS = "current_address";
    private static final String COLUMN_REQUIRED_SKILLS = "required_skills";
    private static final String COLUMN_PACKAGE_DETAILS = "package_details";
    private static final String COLUMN_APPLIED_STATUS = "status";
    private static final String COLUMN_APPLIED_USER_ID = "applied_user_id";

    private static final String TABLE_USER = "user_table";
    private static final String COLUMN_USER_NAME = "user_name";
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_USER_MOBILE = "user_mobile";
    private static final String COLUMN_USER_EMAIL = "user_email";
    private static final String COLUMN_USER_PWD = "user_pwd";
    private static final String COLUMN_USER_ADDRESS = "user_address";


    public DBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String jobs = "CREATE TABLE " + TABLE_NAME_JOB +
                // 0
                "(" + COLUMN_JOB_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                //1
                COLUMN_ADMIN_ID + " TEXT, " +
                //2
                COLUMN_JOB_NAME + " TEXT, " +
                //3
                COLUMN_ORG + " TEXT, " +
                //4
                COLUMN_NUMBER + " TEXT, " +
                //5
                COLUMN_REQUIRED_SKILLS + " TEXT, " +
                //6
                COLUMN_PACKAGE_DETAILS + " TEXT, " +
                // 7
                COLUMN_CURRENT_ADDRESS + " TEXT, " +
                // 8
                COLUMN_EMAIL + " TEXT, " +
                //9
                COLUMN_APPLIED_USER_ID + " TEXT, " +
                //10
                COLUMN_APPLIED_STATUS + " TEXT); ";

        String user = "CREATE TABLE " + TABLE_USER +
                // 0
                "(" + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                //1
                COLUMN_USER_NAME + " TEXT, " +
                //2
                COLUMN_USER_MOBILE + " TEXT, " +
                //3
                COLUMN_USER_EMAIL + " TEXT, " +
                //4
                COLUMN_USER_ADDRESS + " TEXT, " +
                //5
                COLUMN_USER_PWD + " TEXT); ";

        sqLiteDatabase.execSQL(user);
        sqLiteDatabase.execSQL(jobs);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_JOB);
        onCreate(sqLiteDatabase);
    }

    public long addJob(String adminId, String jobName, String org, String mobile, String email, String skillsRequired, String packageDetails, String currentAddress, String appliedStatus) {
        Log.v("TAG", "JOBS INSERT :: " + adminId);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ADMIN_ID, adminId);
        contentValues.put(COLUMN_JOB_NAME, jobName);
        contentValues.put(COLUMN_ORG, org);
        contentValues.put(COLUMN_NUMBER, mobile);
        contentValues.put(COLUMN_EMAIL, email);
        contentValues.put(COLUMN_REQUIRED_SKILLS, skillsRequired);
        contentValues.put(COLUMN_PACKAGE_DETAILS, packageDetails);
        contentValues.put(COLUMN_CURRENT_ADDRESS, currentAddress);
        contentValues.put(COLUMN_APPLIED_STATUS, appliedStatus);
        contentValues.put(COLUMN_APPLIED_USER_ID, "");
        long result = db.insert(TABLE_NAME_JOB, null, contentValues);

        return result;
    }

    public Cursor getJobListByAdminId(String adminId) {
        String query = " select * from " + TABLE_NAME_JOB + " WHERE " + COLUMN_ADMIN_ID + "=" + adminId;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public String getUserNameById(String userId) {
        //    String query = " select * from " + TABLE_USER + " WHERE " + COLUMN_USER_ID + "=" + userId;
        String query = "Select * FROM " + TABLE_USER + " WHERE " + COLUMN_USER_ID + " LIKE " + "'" + userId + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        String userName = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                userName = cursor.getString(1);
            }
        }
        return userName;
    }

    public Cursor getAppliedJobListByAdminId(String adminId, String status) {
        String query = "Select * FROM " + TABLE_NAME_JOB + " WHERE " + COLUMN_ADMIN_ID + " LIKE " + "'" + adminId + "'" + " AND " + COLUMN_APPLIED_STATUS + " LIKE " + "'" + status + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public Cursor getAppliedJobListByUserId(String status) {
        String query = "Select * FROM " + TABLE_NAME_JOB + " WHERE " + COLUMN_APPLIED_STATUS + " LIKE " + "'" + status + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public Cursor getAllJobList() {
        String query = "select * from " + TABLE_NAME_JOB;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public long updateJob(String adminId, String jobId, String jobName, String org, String mobile, String email, String skillsRequired, String packageDetails, String currentAddress, String appliedStatus, String appliedUserId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_JOB_ID, jobId);
        contentValues.put(COLUMN_ADMIN_ID, adminId);
        contentValues.put(COLUMN_JOB_NAME, jobName);
        contentValues.put(COLUMN_ORG, org);
        contentValues.put(COLUMN_NUMBER, mobile);
        contentValues.put(COLUMN_EMAIL, email);
        contentValues.put(COLUMN_REQUIRED_SKILLS, skillsRequired);
        contentValues.put(COLUMN_PACKAGE_DETAILS, packageDetails);
        contentValues.put(COLUMN_CURRENT_ADDRESS, currentAddress);
        contentValues.put(COLUMN_APPLIED_STATUS, appliedStatus);
        contentValues.put(COLUMN_APPLIED_USER_ID, appliedUserId);
        // long result = db.update(TABLE_NAME_JOB, contentValues, BaseColumns._ID + "=?", new String[]{jobId});
        String whereClause = BaseColumns._ID + " = ? AND " + "admin_id" + " = ?"; // HERE ARE OUR CONDITONS STARTS
        String[] whereArgs = {jobId, adminId};
        return db.update(TABLE_NAME_JOB,
                contentValues,
                whereClause,
                whereArgs);
    }

    /* @RequiresApi(api = Build.VERSION_CODES.N)*/
    public ArrayList getAppliedJobUserIdList(String jobId) {
        String query = " select * from " + TABLE_NAME_JOB + " WHERE " + COLUMN_JOB_ID + "=" + jobId;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        ArrayList aList = new ArrayList<>();
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        if (cursor.getCount() == 0) {
            Toast.makeText(context, "No Data", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                String str = "" + cursor.getString(9);
                aList = new ArrayList(Arrays.asList(str.trim().split(",")));
                for (int i = 0; i < aList.size(); i++) {
                    System.out.println(" -->" + aList.get(i));
                }
                Log.v("DB", "DATA FOUND :: " + aList.toString() + "\n");
            }
        }
        return aList;
    }

    public long deleteJob(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME_JOB, BaseColumns._ID + "=?", new String[]{id});
    }

    public long addUser(String username, String mobile, String email, String address, String pwd) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_USER_NAME, username);
        contentValues.put(COLUMN_USER_MOBILE, mobile);
        contentValues.put(COLUMN_USER_EMAIL, email);
        contentValues.put(COLUMN_USER_ADDRESS, address);
        contentValues.put(COLUMN_USER_PWD, pwd);

        return db.insert(TABLE_USER, null, contentValues);
    }

    public String loginWithDB(String username, String pwd) {
        //  String query = "SELECT * FROM " + TABLE_USER + " WHERE " + COLUMN_USER_NAME + "=" + username + " AND " + COLUMN_USER_PWD + "=" + pwd;
        String query = "Select * FROM " + TABLE_USER + " WHERE " + COLUMN_USER_NAME + " LIKE " + "'" + username + "'" + " AND " + COLUMN_USER_PWD + " LIKE " + "'" + pwd + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        String id = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        Log.v("DATABASE", "LOGIN DETAILS COUNT ::" + cursor.getCount());
        if ((cursor != null ? cursor.getCount() : 0) > 0) {
            while (cursor.moveToNext()) {
                id = cursor.getString(0);
            }
            Log.v("DATABASE", "LOGIN DETAILS ::" + id);
            return id;
        } else {
            return null;
        }
    }

    public boolean checkUsernameAndPwd(String userName, String pwd) {
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM user_table WHERE user_name = ? and user_pwd = ?", new String[]{userName, pwd});
        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }
}
