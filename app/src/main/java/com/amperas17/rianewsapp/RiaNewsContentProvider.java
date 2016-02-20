package com.amperas17.rianewsapp;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

public class RiaNewsContentProvider extends ContentProvider {
    final String LOG_TAG = "myLogs";

    private static final int CATEGORY = 100;
    private static final int CATEGORY_ID = 101;
    private static final int NEWS_ITEM = 200;
    private static final int NEWS_ITEM_ID = 201;


    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private RiaNewsDBHelper mOpenHelper;

    @Override
    public boolean onCreate() {
        Log.d(LOG_TAG, "onCreate RiaProvider");
        mOpenHelper = new RiaNewsDBHelper(getContext());
        return true;
    }

    public static UriMatcher buildUriMatcher(){
        String authority = RiaNewsDBContract.AUTHORITY;

        // All paths to the UriMatcher have a corresponding code to return
        // when a match is found (the ints above).
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(authority, RiaNewsDBContract.PATH_NEWS_CATEGORY, CATEGORY);
        matcher.addURI(authority, RiaNewsDBContract.PATH_NEWS_CATEGORY + "/#", CATEGORY_ID);
        matcher.addURI(authority, RiaNewsDBContract.PATH_NEWS_ITEM, NEWS_ITEM);
        matcher.addURI(authority, RiaNewsDBContract.PATH_NEWS_ITEM + "/#", NEWS_ITEM_ID);

        return matcher;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int rows; // Number of rows affected

        switch(sUriMatcher.match(uri)){
            case CATEGORY:
                rows = db.delete(RiaNewsDBContract.CategoryEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case NEWS_ITEM:
                rows = db.delete(RiaNewsDBContract.NewsItemEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Because null could delete all rows:
        if(selection == null || rows != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rows;
    }

    @Override
    public String getType(Uri uri) {
        switch(sUriMatcher.match(uri)){
            case CATEGORY:
                return RiaNewsDBContract.CategoryEntry.CONTENT_TYPE;
            case CATEGORY_ID:
                return RiaNewsDBContract.CategoryEntry.CONTENT_ITEM_TYPE;
            case NEWS_ITEM:
                return RiaNewsDBContract.NewsItemEntry.CONTENT_TYPE;
            case NEWS_ITEM_ID:
                return RiaNewsDBContract.NewsItemEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("[getType]Unknown uri: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        long _id;
        Uri returnUri;

        switch(sUriMatcher.match(uri)){
            case CATEGORY:
                _id = db.insert(RiaNewsDBContract.CategoryEntry.TABLE_NAME, null, values);
                Log.d(LOG_TAG,"Insert - id= "+_id);
                if(_id > 0){
                    returnUri =  RiaNewsDBContract.CategoryEntry.buildCategoryUri(_id);
                } else{
                    returnUri = Uri.parse("Error");
                    //throw new UnsupportedOperationException("[insert(CATEGORY)]Unable to insert rows into: " + uri);
                }
                break;
            case NEWS_ITEM:
                _id = db.insert(RiaNewsDBContract.NewsItemEntry.TABLE_NAME, null, values);
                if(_id > 0){
                    returnUri = RiaNewsDBContract.NewsItemEntry.buildNewsItemUri(_id);
                } else{
                    throw new UnsupportedOperationException("[insert(NEWS_ITEM)]Unable to insert rows into: " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("[insert]Unknown uri: " + uri);
        }

        // Use this on the URI passed into the function to notify any observers that the uri has
        // changed.
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }


    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        Cursor cursor;
        switch(sUriMatcher.match(uri)) {
            case CATEGORY:
                cursor = db.query(
                        RiaNewsDBContract.CategoryEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case CATEGORY_ID:
                long _id = ContentUris.parseId(uri);
                cursor = db.query(
                        RiaNewsDBContract.CategoryEntry.TABLE_NAME,
                        projection,
                        RiaNewsDBContract.CategoryEntry._ID + " = ?",
                        new String[]{String.valueOf(_id)},
                        null,
                        null,
                        sortOrder
                );
                break;
            case NEWS_ITEM:
                cursor = db.query(
                        RiaNewsDBContract.NewsItemEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case NEWS_ITEM_ID:
                _id = ContentUris.parseId(uri);
                cursor = db.query(
                        RiaNewsDBContract.NewsItemEntry.TABLE_NAME,
                        projection,
                        RiaNewsDBContract.NewsItemEntry._ID + " = ?",
                        new String[]{String.valueOf(_id)},
                        null,
                        null,
                        sortOrder
                );
                break;
            default:
                throw new UnsupportedOperationException("[query]Unknown uri: " + uri);
        }
        // Set the notification URI for the cursor to the one passed into the function. This
        // causes the cursor to register a content observer to watch for changes that happen to
        // this URI and any of it's descendants. By descendants, is meant any URI that begins
        // with this path.
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int rows;

        switch(sUriMatcher.match(uri)){
            case CATEGORY:
                rows = db.update(RiaNewsDBContract.CategoryEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case NEWS_ITEM:
                rows = db.update(RiaNewsDBContract.NewsItemEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("[update]Unknown uri: " + uri);
        }

        if(rows != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rows;
    }
}
