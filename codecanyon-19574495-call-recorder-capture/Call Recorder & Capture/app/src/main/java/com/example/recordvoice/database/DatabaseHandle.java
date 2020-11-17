package com.example.recordvoice.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vieta on 28/6/2016.
 */
public class DatabaseHandle extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "record_call";

    // Contacts table name
    private static final String TABLE_RECORD = "record1";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_PHONE_NUMBER = "phone_number";
    private static final String KEY_DATE = "date";
    private static final String KEY_FILE_NAME = "file_name";
    private static final String KEY_TYPE_CALL = "type_call";

    public DatabaseHandle(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_RECORD + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_PHONE_NUMBER + " TEXT, "
                + KEY_DATE + " TEXT, " + KEY_FILE_NAME + " TEXT, "+ KEY_TYPE_CALL + " INTEGER )";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     * Lop Database dung de khoi tao, add them, load cac record
     */

    // Adding new contact
    public void addHistory(RecordCall recordCall) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PHONE_NUMBER, recordCall.getPhoneNumber());
        values.put(KEY_DATE, recordCall.getDate());
        values.put(KEY_FILE_NAME, recordCall.getFileName());
        values.put(KEY_TYPE_CALL, recordCall.getTypeCall());
        // Inserting Row
        db.insert(TABLE_RECORD, null, values);
        db.close(); // Closing database connection
    }

    // Getting All Record
    public List<RecordCall> getAllRecord() {
        List<RecordCall> recordCalls = new ArrayList<RecordCall>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_RECORD + " ORDER BY id DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                RecordCall recordCall = new RecordCall(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getInt(4));
                recordCall.setId(cursor.getInt(0));
                // Adding contact to list
                recordCalls.add(recordCall);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        // return record list
        return recordCalls;
    }

    // Getting All Contacts incall or outcall
    public List<RecordCall> getAllRecordCondition(int type) {
        List<RecordCall> recordCalls = new ArrayList<RecordCall>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_RECORD + " WHERE "+KEY_TYPE_CALL +" = "+ type +" ORDER BY id DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                RecordCall recordCall = new RecordCall(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getInt(4));
                recordCall.setId(cursor.getInt(0));
                // Adding contact to list
                recordCalls.add(recordCall);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        // return contact list
        return recordCalls;
    }

    // Getting contacts Count
    public int getListCount() {
        String countQuery = "SELECT  * FROM " + TABLE_RECORD;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

    public void delete(RecordCall recordCall) {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("LOgMain",recordCall.getId()+"/m");
        db.delete(TABLE_RECORD, KEY_ID + " = ?",
                new String[]{String.valueOf(recordCall.getId())});
        db.close();
    }

}
