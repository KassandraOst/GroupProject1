package com.example.groupproject1.NASAImageDay;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DataBase extends SQLiteOpenHelper {
    static final String ACTIVITY_NAME = "DataBaseAdapter";
    static final String KEY_ID = "_id";
    static final String KEY_DATE = "date";
    static final String KEY_TITLE = "title";
    static final String KEY_QUERY = "image";

    static final String TAG = "DBAdapter";
    static final String DATABASE_NAME = "DatabaseNASA";
    static final String DATABASE_TABLE = "NASAImgoftheDay";
    static final int DATABASE_VERSION = 1;
    static final String DATABASE_CREATE =
            "create table " + DATABASE_TABLE + " (" + KEY_ID + " integer primary key autoincrement, "
                    + KEY_DATE + " TEXT not null, "+ KEY_TITLE + " TEXT not null, " + KEY_QUERY + " TEXT not null);";

    SQLiteDatabase db = this.getReadableDatabase();

    public DataBase(Context ctx) {
        super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
        onCreate(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        try {
            db.execSQL(DATABASE_CREATE);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
        onCreate(db);
    }

    public boolean insertMessage(String date, String query, String title) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_DATE, date);
        initialValues.put(KEY_TITLE, title);
            initialValues.put(KEY_QUERY, query);
        long result = db.insert(DATABASE_TABLE, null, initialValues);

        return result != -1;
    }


    public Cursor getAllEntries() {

        String query = "Select * from " + DATABASE_TABLE;
        Cursor cursor = db.rawQuery(query, null);
        printCursor(cursor);
        return cursor;
    }

    public void printCursor(Cursor cursor) {
        Log.v("Database Version:", Integer.toString(db.getVersion()));
        Log.v("Number of columns: ", Integer.toString(cursor.getColumnCount()));
        for (int i = 0; i < cursor.getColumnCount(); i++) {
            Log.v("Column " + (i + 1) + ": ", cursor.getColumnName(i));
        }
        Log.v("Number of rows:", cursor.getCount()+"");
        Log.v("Cursor Object", DatabaseUtils.dumpCursorToString(cursor));


    }

    public void deleteMessage(long id) {

        String query = "DELETE FROM " + DATABASE_TABLE + " WHERE " + KEY_ID + " LIKE " + id;
        db.execSQL(query);

    }

    public void dropTable() {

        String query = "DROP TABLE " + DATABASE_TABLE;
        db.execSQL(query);
    }

    public String getQuery(long id) {
        String query = null;
        String q = "SELECT * FROM " + DATABASE_TABLE +" WHERE " + KEY_ID +"="+ id;
        Cursor cursor = db.rawQuery(q, null);
        if( cursor != null && cursor.moveToFirst() ){
            query = cursor.getString(cursor.getColumnIndex(KEY_QUERY));
        }
        printCursor(cursor);
        return query;
    }

    public int getCount() {

        String query = "SELECT * FROM " + DATABASE_TABLE;
        Cursor cursor = db.rawQuery(query, null);

        printCursor(cursor);
        return cursor.getCount();
    }

    public int getId(int pos) {
        String query;
        int num = 0;
        if (pos == 0)
        {query = "SELECT * FROM " + DATABASE_TABLE +" ORDER BY "+ KEY_ID +" LIMIT 1 ";}
        else {
            query = "SELECT * FROM " + DATABASE_TABLE +" ORDER BY "+ KEY_ID +" LIMIT 1 OFFSET "+ pos;}
        Cursor cursor = db.rawQuery(query, null);
        if( cursor != null && cursor.moveToFirst() ){
            num = cursor.getInt(cursor.getColumnIndex(KEY_ID));
        }
        printCursor(cursor);

        return num;
    }

    public String getDate(long id) {

        String date = null;
        String q = "SELECT * FROM " + DATABASE_TABLE +" WHERE " + KEY_ID +" LIKE "+ id;
        Cursor cursor = db.rawQuery(q, null);
        if( cursor != null && cursor.moveToFirst() ){
            date = cursor.getString(cursor.getColumnIndex(KEY_DATE));
        }
        printCursor(cursor);
        return date;
    }
    public String getTitle(long id) {
        String title = null;
        String q = "SELECT * FROM " + DATABASE_TABLE +" WHERE " + KEY_ID +" LIKE "+ id;
        Cursor cursor = db.rawQuery(q, null);
        if( cursor != null && cursor.moveToFirst() ){
            title = cursor.getString(cursor.getColumnIndex(KEY_TITLE));
        }
        printCursor(cursor);
        return title;
    }
}