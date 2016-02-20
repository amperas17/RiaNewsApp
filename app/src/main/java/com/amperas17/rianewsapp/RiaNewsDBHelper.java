package com.amperas17.rianewsapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * RiaNewsDBHelper provides SQLiteOpenHelper callbacks for work with riaNewsDatabase.
 */

public class RiaNewsDBHelper extends SQLiteOpenHelper {
    final String LOG_TAG = "myLogs";

    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "riaNews.db";



    public RiaNewsDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(LOG_TAG, "onCreate DBHelper");

        db.execSQL(
                "CREATE TABLE " + RiaNewsDBContract.CategoryEntry.TABLE_NAME + " (" +
                        RiaNewsDBContract.CategoryEntry._ID + " INTEGER PRIMARY KEY, " +
                        RiaNewsDBContract.CategoryEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                        RiaNewsDBContract.CategoryEntry.COLUMN_LINK + " TEXT NOT NULL);"
        );

        db.execSQL(
                "CREATE TABLE " + RiaNewsDBContract.NewsItemEntry.TABLE_NAME + " (" +
                        RiaNewsDBContract.NewsItemEntry._ID + " INTEGER PRIMARY KEY, " +
                        RiaNewsDBContract.NewsItemEntry.COLUMN_HEADER + " TEXT NOT NULL, " +
                        RiaNewsDBContract.NewsItemEntry.COLUMN_CATEGORY + " INTEGER NOT NULL, " +
                        RiaNewsDBContract.NewsItemEntry.COLUMN_LINK + " TEXT NOT NULL, " +
                        RiaNewsDBContract.NewsItemEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL, " +
                        RiaNewsDBContract.NewsItemEntry.COLUMN_NEWS_TEXT + " TEXT NOT NULL, " +
                        RiaNewsDBContract.NewsItemEntry.COLUMN_NEWS_DATE + " TEXT NOT NULL, " +
                        RiaNewsDBContract.NewsItemEntry.COLUMN_IMAGE_SRC + " TEXT NOT NULL, " +

                        "FOREIGN KEY (" + RiaNewsDBContract.NewsItemEntry.COLUMN_CATEGORY + ") " +
                        "REFERENCES " + RiaNewsDBContract.CategoryEntry.TABLE_NAME + " (" + RiaNewsDBContract.CategoryEntry._ID + "));"
        );

        ContentValues cv = new ContentValues();

        cv.put(RiaNewsDBContract.CategoryEntry.COLUMN_NAME, AppContract.INITIAL_CATEGORY_NAME);
        cv.put(RiaNewsDBContract.CategoryEntry.COLUMN_LINK, AppContract.INITIAL_CATEGORY_LINK);
        db.insert(RiaNewsDBContract.CategoryEntry.TABLE_NAME, null, cv);
        cv.clear();

        for (int i = 1; i <= 3; i++) {
            cv.put(RiaNewsDBContract.CategoryEntry.COLUMN_NAME, "name " + i);
            cv.put(RiaNewsDBContract.CategoryEntry.COLUMN_LINK, "link " + i);
            db.insert(RiaNewsDBContract.CategoryEntry.TABLE_NAME, null, cv);
            Log.d(LOG_TAG, "insert" + cv.toString());
        }
        cv.clear();
        Log.d(LOG_TAG, "insert" + cv.toString());
        for (int i = 1; i <= 4; i++) {
            cv.put(RiaNewsDBContract.NewsItemEntry.COLUMN_HEADER, "header " + i);
            cv.put(RiaNewsDBContract.NewsItemEntry.COLUMN_LINK, "link " + i);
            cv.put(RiaNewsDBContract.NewsItemEntry.COLUMN_CATEGORY, 2);
            cv.put(RiaNewsDBContract.NewsItemEntry.COLUMN_DESCRIPTION, "desc " + i);
            cv.put(RiaNewsDBContract.NewsItemEntry.COLUMN_NEWS_TEXT, "text " + i);
            cv.put(RiaNewsDBContract.NewsItemEntry.COLUMN_NEWS_DATE, "date " + i);
            cv.put(RiaNewsDBContract.NewsItemEntry.COLUMN_IMAGE_SRC, "link " + i);


            Log.d(LOG_TAG,"insert"+cv.toString());
            db.insert(RiaNewsDBContract.NewsItemEntry.TABLE_NAME, null, cv);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(LOG_TAG, " --- onUpgrade database from " + oldVersion
                + " to " + newVersion + " version --- ");
        db.beginTransaction();
        try {
            db.execSQL("DROP TABLE IF EXISTS " + RiaNewsDBContract.CategoryEntry.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + RiaNewsDBContract.NewsItemEntry.TABLE_NAME);
            Log.d(LOG_TAG, " deleting db... ");
            onCreate(db);

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        Log.d(LOG_TAG, " --- onDowngrade database from " + oldVersion
                + " to " + newVersion + " version --- ");
        db.beginTransaction();
        try {
            db.execSQL("DROP TABLE IF EXISTS " + RiaNewsDBContract.CategoryEntry.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + RiaNewsDBContract.NewsItemEntry.TABLE_NAME);
            Log.d(LOG_TAG, " deleting db... ");
            onCreate(db);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }
}