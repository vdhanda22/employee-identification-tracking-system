package com.karzansoft.fastvmi.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import com.karzansoft.fastvmi.Models.AccessoryItem;
import com.karzansoft.fastvmi.Models.Contact;
import com.karzansoft.fastvmi.Models.MarkImage;
import com.karzansoft.fastvmi.Models.VehicleLocation;
import com.karzansoft.fastvmi.Models.VehicleStatus;
import com.karzansoft.fastvmi.Utils.Util;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Yasir on 4/13/2016.
 */
public class DatabaseManager {

    private AtomicInteger mOpenCounter = new AtomicInteger();
    private static DatabaseManager instance;
    private static DatabaseHelper mDatabaseHelper;
    private SQLiteDatabase mDatabase;
    private Context context;

    private DatabaseManager(Context context)
    {
        mDatabaseHelper=new DatabaseHelper(context);
        this.context=context;
    }

    public static synchronized DatabaseManager getInstance(Context context) {
        if (instance == null) {
           instance=new DatabaseManager(context);
        }
        return instance;
    }

    public synchronized SQLiteDatabase openDatabase() {
        if(mOpenCounter.incrementAndGet() == 1) {
            ////////Opening new database/////////
            mDatabase = mDatabaseHelper.getWritableDatabase();
        }
        return mDatabase;
    }

    public synchronized void closeDatabase() {
        if(mOpenCounter.decrementAndGet() == 0) {
            ////// Closing database////////
            mDatabase.close();

        }
    }

/////////////////////// Accessory Table Operations//////////////////////////////////////////////

    public void addAccessories(ArrayList<AccessoryItem> items)
    {

        SQLiteDatabase db=openDatabase();

        try {
            db.beginTransaction();

            for (int i=0;i<items.size();i++)
            {
                ContentValues values = new ContentValues();
                values.put(DBConstants.KEY_SERVER_ID, items.get(i).getId());
                values.put(DBConstants.KEY_NAME, items.get(i).getName());
                db.insertWithOnConflict(DBConstants.TABLE_ACCESSORY, null, values, SQLiteDatabase.CONFLICT_IGNORE);

            }

            db.setTransactionSuccessful();
        }catch (Exception ex){ex.printStackTrace();}
        finally {
            db.endTransaction();
        }
        closeDatabase();

    }

    public ArrayList<AccessoryItem> getAccessories()
    {
        ArrayList<AccessoryItem> items=new ArrayList<AccessoryItem>();
        String selectQuery = "SELECT  * FROM " + DBConstants.TABLE_ACCESSORY;
        SQLiteDatabase db=openDatabase();
        try {
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor!=null && cursor.moveToFirst()) {
                do {
                    AccessoryItem item=new AccessoryItem();

                    item.setId(cursor.getInt(cursor.getColumnIndex(DBConstants.KEY_SERVER_ID)));
                    item.setName(cursor.getString(cursor.getColumnIndex(DBConstants.KEY_NAME)));
                    items.add(item);


                } while(cursor.moveToNext());
            }

        }catch (Exception ex){ex.printStackTrace();}

        closeDatabase();
        return items;
    }

    public void deleteAllAccessory()
    {
       deleteTable(DBConstants.TABLE_ACCESSORY);
    }
    public int getAccessoryCount() {
        return getRecordCount(DBConstants.TABLE_ACCESSORY);
    }



   /////////////////////////////// Location Table Operations////////////////////////////////////////

    public void addVehicleLocation(ArrayList<VehicleLocation> items) {
        SQLiteDatabase db = openDatabase();
        try {
            db.beginTransaction();
            for (int i = 0; i < items.size(); i++) {
                ContentValues values = new ContentValues();
                values.put(DBConstants.KEY_SERVER_ID, ""+items.get(i).getId());
                values.put(DBConstants.KEY_NAME, items.get(i).getName());
                db.insertWithOnConflict(DBConstants.TABLE_VEHICLE_LOCATION, null, values, SQLiteDatabase.CONFLICT_IGNORE);
            }
            db.setTransactionSuccessful();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            db.endTransaction();
        }
        closeDatabase();
    }

    public ArrayList<VehicleLocation> searchLocation(String keyWord)
    {
        ArrayList<VehicleLocation> items=new ArrayList<VehicleLocation>();
        String selectQuery = "SELECT  * FROM " + DBConstants.TABLE_VEHICLE_LOCATION+" WHERE LOWER ("+DBConstants.KEY_NAME+") LIKE LOWER ( '%"+keyWord+"%' ) OR  LOWER ("
                +DBConstants.KEY_SERVER_ID+") LIKE LOWER ( '%"+keyWord+"%' )"+ " LIMIT 0, 20";
        SQLiteDatabase db=openDatabase();
        try {
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor!=null && cursor.moveToFirst()) {
                do {
                    VehicleLocation item=new VehicleLocation();
                    item.setId(cursor.getString(cursor.getColumnIndex(DBConstants.KEY_SERVER_ID)));
                    item.setName(cursor.getString(cursor.getColumnIndex(DBConstants.KEY_NAME)));
                    items.add(item);


                } while(cursor.moveToNext());
            }

        }catch (Exception ex){ex.printStackTrace();}

        closeDatabase();
        return items;
    }

    public ArrayList<VehicleLocation>  getVehiclLocations()
    {
        ArrayList<VehicleLocation> items=new ArrayList<VehicleLocation>();

        String selectQuery = "SELECT  * FROM " + DBConstants.TABLE_VEHICLE_LOCATION;
        SQLiteDatabase db=openDatabase();
        try {
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor!=null && cursor.moveToFirst()) {
                do {
                    VehicleLocation item=new VehicleLocation();
                    item.setId(cursor.getString(cursor.getColumnIndex(DBConstants.KEY_SERVER_ID)));
                    item.setName(cursor.getString(cursor.getColumnIndex(DBConstants.KEY_NAME)));
                    items.add(item);


                } while(cursor.moveToNext());
            }


        }catch (Exception ex){ex.printStackTrace();}

        closeDatabase();
        return items;
    }

    public VehicleLocation getLocationByID(String id)
    {
        VehicleLocation loc=null;
        if(id==null || id.length()<1)
            return loc;
        String selectQuery = "SELECT  * FROM " + DBConstants.TABLE_VEHICLE_LOCATION+" WHERE "+DBConstants.KEY_SERVER_ID+" ='"+id+"'";

        SQLiteDatabase db=openDatabase();
        try {
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor!=null && cursor.moveToFirst()) {

                loc = new VehicleLocation();
                loc.setId(cursor.getString(cursor.getColumnIndex(DBConstants.KEY_SERVER_ID)));
                loc.setName(cursor.getString(cursor.getColumnIndex(DBConstants.KEY_NAME)));

            }

        }catch (Exception ex){ex.printStackTrace();}

        closeDatabase();
        return loc;
    }

    public void deleteAllLocations()
    {
        deleteTable(DBConstants.TABLE_VEHICLE_LOCATION);
    }

    /////////////////////////////////////// Staff Member Table Operations///////////////////////////

    public void addStaffMember(ArrayList<Contact> items)
    {
        SQLiteDatabase db=openDatabase();
        try {
            db.beginTransaction();
            for (int i=0;i<items.size();i++)
            {
                ContentValues values = new ContentValues();
                Contact staff=items.get(i);
                values.put(DBConstants.KEY_SERVER_ID, ""+staff.getId());
                values.put(DBConstants.KEY_NAME, staff.getName());
                if(staff.getCode()!=null && staff.getCode().length()>0)
                values.put(DBConstants.KEY_CODE, staff.getCode());
               /*if(staff.getNationalId()!=null && staff.getNationalId().length()>0)
                values.put(DBConstants.KEY_NATIONAL_ID, ""+staff.getNationalId());
*/                db.insertWithOnConflict(DBConstants.TABLE_STAFF_MEMBER, null, values, SQLiteDatabase.CONFLICT_IGNORE);
            }
            db.setTransactionSuccessful();
        }catch (Exception ex){ex.printStackTrace();}
        finally {
            db.endTransaction();
        }
        closeDatabase();
    }

    /*
     public ArrayList<VehicleLocation>  getVehiclLocations()
    {
        ArrayList<VehicleLocation> items=new ArrayList<VehicleLocation>();

        String selectQuery = "SELECT  * FROM " + DBConstants.TABLE_VEHICLE_LOCATION;
        SQLiteDatabase db=openDatabase();
        try {
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor!=null && cursor.moveToFirst()) {
                do {
                    VehicleLocation item=new VehicleLocation();
                    item.setId(cursor.getString(cursor.getColumnIndex(DBConstants.KEY_SERVER_ID)));
                    item.setName(cursor.getString(cursor.getColumnIndex(DBConstants.KEY_NAME)));
                    items.add(item);


                } while(cursor.moveToNext());
            }


        }catch (Exception ex){ex.printStackTrace();}

        closeDatabase();
        return items;
    }
    */
    public ArrayList<Contact> getDrivers(String limit)
    {
        ArrayList<Contact> items=new ArrayList<Contact>();
        String selectQuery = "SELECT  * FROM " + DBConstants.TABLE_STAFF_MEMBER+ " LIMIT 0, "+limit;
        SQLiteDatabase db=openDatabase();
        try {
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor!=null && cursor.moveToFirst()) {
                do {
                    Contact item=new Contact();
                    item.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBConstants.KEY_SERVER_ID))));
                    item.setName(cursor.getString(cursor.getColumnIndex(DBConstants.KEY_NAME)));
                    item.setFirstName(cursor.getString(cursor.getColumnIndex(DBConstants.KEY_NAME)));
                    // item.setNationalId(cursor.getString(cursor.getColumnIndex(DBConstants.KEY_NATIONAL_ID)));
                    items.add(item);
                   Util.LogE("Searhing",""+item.getName());

                } while(cursor.moveToNext());
            }

        }catch (Exception ex){ex.printStackTrace();}

        closeDatabase();
        return items;
    }

    public ArrayList<Contact> searchDriver(String keyWord)
    {
        ArrayList<Contact> items=new ArrayList<Contact>();
        String selectQuery = "SELECT  * FROM " + DBConstants.TABLE_STAFF_MEMBER+" WHERE LOWER ("+DBConstants.KEY_NAME+") LIKE LOWER ( '%"+keyWord+"%' ) OR  LOWER ("
                +DBConstants.KEY_SERVER_ID+") LIKE LOWER ( '%"+keyWord+"%' )"+ " LIMIT 0, 20";
        SQLiteDatabase db=openDatabase();
        try {
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor!=null && cursor.moveToFirst()) {
                do {
                    Contact item=new Contact();
                    item.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBConstants.KEY_SERVER_ID))));
                    item.setName(cursor.getString(cursor.getColumnIndex(DBConstants.KEY_NAME)));
                    item.setFirstName(cursor.getString(cursor.getColumnIndex(DBConstants.KEY_NAME)));
                   // item.setNationalId(cursor.getString(cursor.getColumnIndex(DBConstants.KEY_NATIONAL_ID)));
                    items.add(item);


                } while(cursor.moveToNext());
            }

        }catch (Exception ex){ex.printStackTrace();}

        closeDatabase();
        return items;
    }

    public Contact getDriverByID(int id)
    {
        Contact item=null;
        String selectQuery = "SELECT  * FROM " + DBConstants.TABLE_STAFF_MEMBER+" WHERE "+DBConstants.KEY_SERVER_ID+" = ?";
        SQLiteDatabase db=openDatabase();
        try {
            String[] args={""+id};
            Cursor cursor = db.rawQuery(selectQuery, args);
            if (cursor!=null && cursor.moveToFirst()) {
                do {
                     item=new Contact();
                    item.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBConstants.KEY_SERVER_ID))));
                    item.setName(cursor.getString(cursor.getColumnIndex(DBConstants.KEY_NAME)));
                    item.setFirstName(cursor.getString(cursor.getColumnIndex(DBConstants.KEY_NAME)));
                   // item.setNationalId(cursor.getString(cursor.getColumnIndex(DBConstants.KEY_NATIONAL_ID)));



                } while(cursor.moveToNext());
            }

        }catch (Exception ex){ex.printStackTrace();}

        closeDatabase();
        return item;
    }

    public void deleteAllStaff()
    {
        deleteTable(DBConstants.TABLE_STAFF_MEMBER);
    }

    ////////////////////////////////////// Vehicle statusCode Table Operations//////////////////////////

    public void addVehicleStatus(ArrayList<VehicleStatus> items)
    {
        SQLiteDatabase db=openDatabase();
        try {
            db.beginTransaction();
            for (int i=0;i<items.size();i++)
            {
                ContentValues values = new ContentValues();
                values.put(DBConstants.KEY_SERVER_ID, items.get(i).getId());
                values.put(DBConstants.KEY_NAME, items.get(i).getName());
                db.insertWithOnConflict(DBConstants.TABLE_VEHICLE_STATUS, null, values, SQLiteDatabase.CONFLICT_IGNORE);
            }
            db.setTransactionSuccessful();
        }catch (Exception ex){ex.printStackTrace();}
        finally {
            db.endTransaction();
        }
        closeDatabase();
    }

    public ArrayList<VehicleStatus> getVehiclStatuses()
    {
        ArrayList<VehicleStatus> items=new ArrayList<VehicleStatus>();
        String selectQuery = "SELECT  * FROM " + DBConstants.TABLE_VEHICLE_STATUS;
        SQLiteDatabase db=openDatabase();
        try {
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor!=null &&cursor.moveToFirst()) {
                do {
                    VehicleStatus item=new VehicleStatus();

                    item.setId(cursor.getString(cursor.getColumnIndex(DBConstants.KEY_SERVER_ID)));
                    item.setName(cursor.getString(cursor.getColumnIndex(DBConstants.KEY_NAME)));
                    items.add(item);

                } while(cursor.moveToNext());
            }

        }catch (Exception ex){ex.printStackTrace();}

        closeDatabase();
        return items;
    }
    public void deleteAllStatuses()
    {
        deleteTable(DBConstants.TABLE_VEHICLE_STATUS);
    }


    /////////////////////////////// MarkImage Table Operations////////////////////////////////////////

    public synchronized void addMarkImage(ArrayList<MarkImage> items)
    {
        SQLiteDatabase db=openDatabase();
        try {
            db.beginTransaction();
            for (int i=0;i<items.size();i++)
            {
                ContentValues values = new ContentValues();
                values.put(DBConstants.KEY_GUID, ""+items.get(i).getGuid());
                values.put(DBConstants.KEY_PATH, items.get(i).getImagePath());
                values.put(DBConstants.KEY_SYNC_STATUS, 0);
                db.insertWithOnConflict(DBConstants.TABLE_MARK_IMAGE, null, values, SQLiteDatabase.CONFLICT_IGNORE);
            }
            db.setTransactionSuccessful();
        }catch (Exception ex){ex.printStackTrace();}
        finally {
            db.endTransaction();
        }
        closeDatabase();
    }

    public ArrayList<MarkImage> getImagesBySyncStatus(int status)
    {
        ArrayList<MarkImage> images=new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + DBConstants.TABLE_MARK_IMAGE+" WHERE "+DBConstants.KEY_SYNC_STATUS+" = ?";
        SQLiteDatabase db=openDatabase();
        try {
            String[] args={""+status};
            Cursor cursor = db.rawQuery(selectQuery, args);
            if (cursor!=null && cursor.moveToFirst()) {
                do {
                    MarkImage item=new MarkImage();
                    item.setId(cursor.getInt(cursor.getColumnIndex(DBConstants.KEY_ID)));
                    item.setImagePath(cursor.getString(cursor.getColumnIndex(DBConstants.KEY_PATH)));
                    item.setGuid(cursor.getString(cursor.getColumnIndex(DBConstants.KEY_GUID)));
                    item.setSyncStatus(cursor.getInt(cursor.getColumnIndex(DBConstants.KEY_SYNC_STATUS)));
                    images.add(item);
                    Util.LogE("path", item.getImagePath());

                } while(cursor.moveToNext());
            }

        }catch (Exception ex){ex.printStackTrace();}

        closeDatabase();

        return images;
    }

    public synchronized int updateImageStatusByID(int id,int status)
    {
        int updatedRowCount=0;

        SQLiteDatabase db=openDatabase();
        ContentValues values = new ContentValues();
        values.put(DBConstants.KEY_SYNC_STATUS, status);
       try {

           updatedRowCount = db.update(DBConstants.TABLE_MARK_IMAGE, values, DBConstants.KEY_ID + " = ?",
                   new String[]{String.valueOf(id)});

       }catch (Exception ex){ex.printStackTrace();}
        closeDatabase();

        return updatedRowCount;
    }


    public void deleteImageByStatusID(int status)
    {
        SQLiteDatabase db=openDatabase();
        db.delete(DBConstants.TABLE_MARK_IMAGE, DBConstants.KEY_SYNC_STATUS+" = ?", new String[]{String.valueOf(status)});
        closeDatabase();
    }

    ///////////////////////////// General Operations////////////////////////////////////////////////

    public void deleteTable(String tableName)
    {
        SQLiteDatabase db=openDatabase();
        db.delete(tableName, null, null);
        closeDatabase();
    }

    public int getRecordCount(String tableName)//select count(*) from
    {
        int count = 0;
        SQLiteDatabase db = openDatabase();
        try {
            count = (int) DatabaseUtils.queryNumEntries(db, tableName);
            Util.LogE("Count", "" + count);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        closeDatabase();

        return count;
    }

    public int getRecordCount(String tableName, int syncStatus)//select count(*) from
    {
        int count = 0;
        SQLiteDatabase db = openDatabase();

        try {
            count = (int) DatabaseUtils.queryNumEntries(db, tableName, DBConstants.KEY_SYNC_STATUS + " = ?", new String[]{"" + syncStatus});
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        closeDatabase();
        Util.LogE("Count", "" + count);
        return count;
    }


}
