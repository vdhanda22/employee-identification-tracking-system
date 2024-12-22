package com.karzansoft.fastvmi.DataBase;

/**
 * Created by Yasir on 4/13/2016.
 */
public class DBConstants {

    // Database Info
    public static final String DATABASE_NAME = "mCC_Dollar_Database";
    public static final int DATABASE_VERSION = 1;

    // Table Names
    public static final String TABLE_ACCESSORY = "accessory";
    public static final String TABLE_VEHICLE_LOCATION = "vehicle_location";
    public static final String TABLE_STAFF_MEMBER = "staff_member";
    public static final String TABLE_VEHICLE_STATUS = "vehicle_status";
    public static final String TABLE_MARK_IMAGE = "mark_image";


    // General Table Columns
    public static final String KEY_ID = "id";
    public static final String KEY_SERVER_ID = "server_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_CODE = "code";
    public static final String KEY_NATIONAL_ID = "nationalId";

    // MARK_IMAGE Table Columns
    public static final String KEY_GUID = "guid";
    public static final String KEY_PATH = "path";
    public static final String KEY_SYNC_STATUS = "sync_status";





    public static String getCreateAccessoryTableQuery()
    {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_ACCESSORY +
                "(" +
                KEY_ID + " INTEGER PRIMARY KEY," +
                KEY_SERVER_ID + " INTEGER UNIQUE," +
                KEY_NAME + " TEXT" +
                ")";
        return CREATE_TABLE;
    }

    public static String getCreateLocationTableQuery()
    {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_VEHICLE_LOCATION +
                "(" +
                KEY_ID + " INTEGER PRIMARY KEY," +
                KEY_SERVER_ID + " TEXT UNIQUE," +
                KEY_NAME + " TEXT" +
                ")";
        return CREATE_TABLE;
    }

    public static String getCreateStaffMemberTableQuery()
    {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_STAFF_MEMBER +
                "(" +
                KEY_ID + " INTEGER PRIMARY KEY," +
                KEY_SERVER_ID + " TEXT UNIQUE," +
                KEY_CODE + " TEXT," +
                KEY_NAME + " TEXT," +
                KEY_NATIONAL_ID + " TEXT UNIQUE" +
                ")";
        return CREATE_TABLE;
    }
    public static String getCreateVehicleStatusTableQuery()
    {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_VEHICLE_STATUS +
                "(" +
                KEY_ID + " INTEGER PRIMARY KEY," +
                KEY_SERVER_ID + " TEXT UNIQUE," +
                KEY_NAME + " TEXT" +
                ")";
        return CREATE_TABLE;
    }


    public static String getCreateMarkImageTableQuery()
    {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_MARK_IMAGE +
                "(" +
                KEY_ID + " INTEGER PRIMARY KEY," +
                KEY_GUID + " TEXT UNIQUE," +
                KEY_PATH + " TEXT," +
                KEY_SYNC_STATUS + " INTEGER" +
                ")";
        return CREATE_TABLE;
    }
}
