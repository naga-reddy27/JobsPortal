package com.user.jobportal.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import androidx.annotation.Nullable;

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
                COLUMN_APPLIED_STATUS + " TEXT); ";
        sqLiteDatabase.execSQL(jobs);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
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
        long result = db.insert(TABLE_NAME_JOB, null, contentValues);

        return result;
    }

    public Cursor getJobList(String adminId) {
        String query = "select * from " + TABLE_NAME_JOB + " WHERE "  + COLUMN_ADMIN_ID + "=" + adminId;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public long updateJob(String adminId, String jobId, String jobName, String org, String mobile, String email, String skillsRequired, String packageDetails, String currentAddress, String appliedStatus) {
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

        // long result = db.update(TABLE_NAME_JOB, contentValues, BaseColumns._ID + "=?", new String[]{jobId});
        String whereClause = BaseColumns._ID + " = ? AND " + "admin_id" + " = ?"; // HERE ARE OUR CONDITONS STARTS
        String[] whereArgs = {jobId, adminId};
        return db.update(TABLE_NAME_JOB,
                contentValues,
                whereClause,
                whereArgs);
    }

    public long deleteJob(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME_JOB, BaseColumns._ID + "=?", new String[]{id});
    }


}
