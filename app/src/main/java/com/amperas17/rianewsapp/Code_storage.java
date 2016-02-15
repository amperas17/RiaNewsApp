package com.amperas17.rianewsapp;


/**
 * Was in previous main_activity
 */
public final class Code_storage {

    /*final Uri Category_URI = Uri.parse(RiaNewsDBContract.CONTENT_AUTHORITY+"/"+RiaNewsDBContract.PATH_NEWS_CATEGORY);
    final Uri News_Item_URI = Uri.parse(RiaNewsDBContract.CONTENT_AUTHORITY+"/"+RiaNewsDBContract.PATH_NEWS_ITEM);

    final String NEWS_ITEM_HEADER = RiaNewsDBContract.NewsItemEntry.COLUMN_HEADER;
    final String NEWS_ITEM_LINK = RiaNewsDBContract.NewsItemEntry.COLUMN_LINK;*/

    //private String[] mScreenTitles={"dssdg","sdgdg"};

    /*@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    /*Uri uri = ContentUris.withAppendedId(News_Item_URI, 2);
        Cursor cursor = getContentResolver().query(uri, null, null,
                null, null);
        startManagingCursor(cursor);

        //String from[] = { NewsDBContract.CategoryEntry.COLUMN_NAME, NewsDBContract.CategoryEntry.COLUMN_LINK };
        String from[] = { NEWS_ITEM_HEADER, NEWS_ITEM_LINK };

        int to[] = { android.R.id.text1, android.R.id.text2 };
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_2, cursor, from, to);

        ListView lvContact = (ListView) findViewById(R.id.lvContact);
        lvContact.setAdapter(adapter);
    }*/
    /*public void onClickInsert(View v) {
        ContentValues cv = new ContentValues();
        cv.put(NEWS_ITEM_HEADER, "header _");
        cv.put(NEWS_ITEM_LINK, "link _");
        cv.put(RiaNewsDBContract.NewsItemEntry.COLUMN_CATEGORY, 1);
        cv.put(RiaNewsDBContract.NewsItemEntry.COLUMN_DESCRIPTION, "desc _");
        cv.put(RiaNewsDBContract.NewsItemEntry.COLUMN_NEWS_TEXT, "text _");
        cv.put(RiaNewsDBContract.NewsItemEntry.COLUMN_NEWS_DATE, "date _");
        cv.put(RiaNewsDBContract.NewsItemEntry.COLUMN_IMAGE_SRC, "link _");
        Uri newUri = getContentResolver().insert(News_Item_URI, cv);
        Log.d(LOG_TAG, "insert, result Uri : " + newUri.toString());
    }



    public void onClickUpdate(View v) {
        ContentValues cv = new ContentValues();
        cv.put(NEWS_ITEM_HEADER, "header _5");
        cv.put(NEWS_ITEM_LINK, "link _5");
        cv.put(RiaNewsDBContract.NewsItemEntry.COLUMN_CATEGORY, 1);
        cv.put(RiaNewsDBContract.NewsItemEntry.COLUMN_DESCRIPTION, "desc _");
        cv.put(RiaNewsDBContract.NewsItemEntry.COLUMN_NEWS_TEXT, "text _");
        cv.put(RiaNewsDBContract.NewsItemEntry.COLUMN_NEWS_DATE, "date _");
        cv.put(RiaNewsDBContract.NewsItemEntry.COLUMN_IMAGE_SRC, "link _");

        int cnt = getContentResolver().update(News_Item_URI, cv, "_id = 2", null);
        Log.d(LOG_TAG, "update, count = " + cnt);
    }

    public void onClickDelete(View v) {
        //Uri uri = ContentUris.withAppendedId(News_Item_URI, 3);
        int cnt = getContentResolver().delete(News_Item_URI, "_id = 3", null);
        Log.d(LOG_TAG, "delete, count = " + cnt);
    }

    public void onClickError(View v) {
        Uri uri = ContentUris.withAppendedId(News_Item_URI, 1);
        Cursor cursor = getContentResolver().query(uri, null, null,
                null, null);
        cursor.moveToFirst();
        int i=0;
        while (i<cursor.getColumnCount()){
            Log.d(LOG_TAG, "cursor " + cursor.getString(i));
            i++;
        }
        if (cursor.moveToNext()) {
            i = 0;
            while (i < cursor.getColumnCount()) {
                Log.d(LOG_TAG, "cursor " + cursor.getString(i));
                i++;
            }
        } else Log.d(LOG_TAG, "cursor is in the end ");

        String s = cursor.getExtras().getString(RiaNewsDBContract.NewsItemEntry.COLUMN_HEADER);
        //Log.d(LOG_TAG, "query = " + cursor.getExtras().toString());


    }

    */
}
