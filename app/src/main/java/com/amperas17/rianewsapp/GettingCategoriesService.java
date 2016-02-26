package com.amperas17.rianewsapp;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;

/**
 * Created by Вова on 25.02.2016.
 */
public class GettingCategoriesService  extends IntentService {

    String mMainUrl ="http://ria.ru/";
    Document mDocument;
    Elements mCategories;
    ArrayList<CategoryItem> mDownloadedCategoryItems = new ArrayList<>();
    ArrayList<CategoryItem> dbCategoryItems = new ArrayList<>();


    public GettingCategoriesService() {
        super("GettingCategoriesService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(AppContract.LOG_TAG, "GettingCategoriesService[onHandleIntent]: ");
        getCategories();
    }


    private void getCategories(){
        Cursor cursor;
        ContentValues cv;
        try {

            /**Download mCategories from http://ria.ru/ using Jsoup and put them into mDownloadedCategoryItems list */
            mDocument = Jsoup.connect(mMainUrl).get();
            mCategories = mDocument
                    .select(".navigator_top_item:not(.navigator_top_item_selected_default)>.navigator_top_item_title");

            for (int i=0;i< mCategories.size();i++) {
                String name = mCategories.get(i).text();
                String link = mCategories.get(i).attr("href");
                if (link.startsWith("/")){
                    link = mMainUrl + link.substring(1);
                }
                mDownloadedCategoryItems.add(new CategoryItem(name, link));
            }
            /**-------------------------------------------------------------------------------------------------*/

            /**Get cursor with categories from local DB and put them into dbCategoryItems list*/
            cursor = getContentResolver().query(RiaNewsDBContract.CATEGORIES_URI,
                    null,null,null,null);

            cursor.moveToFirst();
            for (int i=1;i<cursor.getCount();i++){
                cursor.moveToNext();
                String dbName = cursor.getString(cursor.getColumnIndex(RiaNewsDBContract.CategoryEntry.COLUMN_NAME));
                String dbLink = cursor.getString(cursor.getColumnIndex(RiaNewsDBContract.CategoryEntry.COLUMN_LINK));
                dbCategoryItems.add(new CategoryItem(dbName, dbLink));

                //Log.d(AppContract.LOG_TAG, "GettingCategoriesService[onHandleIntent]: for "+dbName);
            }
            cursor.close();
            /**-------------------------------------------------------------------------------------------------*/

            /**Compare two ArrayLists and if mDownloadedCategoryItems has new category
             * then this category is inserting into DB CategoriesTable
             * And if dbCategoryItems has deprecated category
             * then this old category is deleting from DB CategoriesTable
             * */
            cv = new ContentValues();
            for (CategoryItem item: mDownloadedCategoryItems){
                if (!dbCategoryItems.contains(item)){
                    cv.clear();
                    cv.put(RiaNewsDBContract.CategoryEntry.COLUMN_NAME, item.mName);
                    cv.put(RiaNewsDBContract.CategoryEntry.COLUMN_LINK, item.mLink);
                    getContentResolver().insert(RiaNewsDBContract.CATEGORIES_URI, cv);
                }
            }

            for (CategoryItem item:dbCategoryItems){
                Log.d(AppContract.LOG_TAG,"GettingCategoriesService: for"+item.toString());

                if (!mDownloadedCategoryItems.contains(item)){
                    Log.d(AppContract.LOG_TAG,"GettingCategoriesService: if "+item.toString());
                    getContentResolver().delete(RiaNewsDBContract.CATEGORIES_URI,
                            RiaNewsDBContract.CategoryEntry.COLUMN_NAME + "=" + "\"" + item.mName + "\"", null);

                    /**TODO: NewsItems Deleting
                     getContentResolver().delete(RiaNewsDBContract.NEWS_ITEMS_URI,
                     RiaNewsDBContract.NewsItemEntry.COLUMN_CATEGORY + "=" + "\"" + item.mName + "\"", null);
                     *
                     * */
                }
            }
            /**-------------------------------------------------------------------------------------------------*/


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
