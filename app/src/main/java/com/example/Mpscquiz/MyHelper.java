package com.example.Mpscquiz;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyHelper extends SQLiteOpenHelper {

    private static final String dbname = "Mpscquiz";
    private static final int version = 1;
    public MyHelper(Context context) {
        super(context, dbname, null, version);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        // subject table
        createSubjectsTable(db);
    }



    // creating table subject
    private void createSubjectsTable(SQLiteDatabase db) {

        String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + "subjects" + " (" +
                        "_id" + " INTEGER PRIMARY KEY," +
                        "subject_name" + " TEXT)";
        db.execSQL(SQL_CREATE_ENTRIES);

        // inserting intital  data into a subjects table
        insertDataSubjectTable("agriculture", db);
        insertDataSubjectTable("current_affair", db);
        insertDataSubjectTable("History", db);
        insertDataSubjectTable("economics", db);
        insertDataSubjectTable("english", db);
        insertDataSubjectTable("geography", db);
        insertDataSubjectTable("human_res", db);
        insertDataSubjectTable("logical", db);
        insertDataSubjectTable("politics", db);
        insertDataSubjectTable("science", db);

    }

    private void insertDataSubjectTable(String subject_name, SQLiteDatabase db) {

        ContentValues values = new ContentValues();
        values.put("subject_name", subject_name);
        db.insert("subjects", null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
