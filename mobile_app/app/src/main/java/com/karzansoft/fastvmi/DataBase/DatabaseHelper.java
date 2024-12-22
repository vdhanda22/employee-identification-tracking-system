package com.karzansoft.fastvmi.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Yasir on 4/13/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {


    public DatabaseHelper(Context context) {
        super(context, DBConstants.DATABASE_NAME, null, DBConstants.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(DBConstants.getCreateAccessoryTableQuery());
        db.execSQL(DBConstants.getCreateLocationTableQuery());
        db.execSQL(DBConstants.getCreateStaffMemberTableQuery());
        db.execSQL(DBConstants.getCreateVehicleStatusTableQuery());
        db.execSQL(DBConstants.getCreateMarkImageTableQuery());

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            // Simplest implementation is to drop all old tables and recreate them

            //onCreate(db);
        }

    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        //db.setForeignKeyConstraintsEnabled(true);
    }
}
