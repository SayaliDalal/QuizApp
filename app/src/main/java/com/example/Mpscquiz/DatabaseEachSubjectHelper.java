package com.example.Mpscquiz;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

class  DatabaseOpenHelper extends SQLiteAssetHelper {

    private static final int DATABASE_VERSION = 1;

    public DatabaseOpenHelper(Context context, String DATABASE_NAME) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
}


class DatabaseAccess {
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase database;
    private static DatabaseAccess instance;

    private DatabaseAccess(Context context, String DATABASE_NAME) {
        this.openHelper = new DatabaseOpenHelper(context, DATABASE_NAME);
    }
    public static DatabaseAccess getInstance(Context context, String DATABASE_NAME) {
        if (instance == null) {
            instance = new DatabaseAccess(context, DATABASE_NAME);
        }
        return instance;
    }
    public void open() {

        this.database = openHelper.getWritableDatabase();
    }
    public void close() {
        if (database != null) {
            this.database.close();
        }
    }
    public String getSubjectDetails(int columnNo, String subject, int pos) {
        String res = "";
        String query = "SELECT * FROM "+ subject;
        Cursor cursor = database.rawQuery(query, null);
        cursor.moveToPosition(pos);
        res = cursor.getString(columnNo);
        cursor.close();
        return res;
    }
}

