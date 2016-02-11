package com.amperas17.rianewsapp;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * RiaNewsDBContract describes static fields of riaNewsDatabase.
 *
 * CategoryEntry consists of news categories columns.
 * NewsItemEntry consists of news items columns.
 */

public class RiaNewsDBContract {
    public static final String AUTHORITY = "com.amperas17.providers.rianews";
    public static final String CONTENT_AUTHORITY ="content://" + AUTHORITY;

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String PATH_NEWS_CATEGORY = "categoryPath";
    public static final String PATH_NEWS_ITEM = "newsItemPath";


    public static final class CategoryEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_NEWS_CATEGORY).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_URI + "/" + PATH_NEWS_CATEGORY;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_URI + "/" + PATH_NEWS_CATEGORY;

        public static final String TABLE_NAME = "categoriesTable";

        public static final String COLUMN_NAME = "categoryName";
        public static final String COLUMN_LINK = "categoryLink";


        public static Uri buildCategoryUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class NewsItemEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_NEWS_ITEM).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_URI + "/" + PATH_NEWS_ITEM;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_URI + "/" + PATH_NEWS_ITEM;

        public static final String TABLE_NAME = "newsItemsTable";

        public static final String COLUMN_HEADER = "newsItemHeader";
        public static final String COLUMN_CATEGORY = "newsItemCategory";
        public static final String COLUMN_LINK = "newsItemLink";
        public static final String COLUMN_DESCRIPTION = "newsItemDescription";
        public static final String COLUMN_NEWS_TEXT = "newsItemText";
        public static final String COLUMN_NEWS_DATE = "newsItemDate";
        public static final String COLUMN_IMAGE_SRC = "newsItemImageSrc";


        public static Uri buildNewsItemUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
