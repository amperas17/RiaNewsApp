package com.amperas17.rianewsapp;

/**
 * Created by Вова on 16.02.2016.
 */
public final class AppContract {

    final static String LOG_TAG = "myLogs";

    /** MainActivity send NEWS_ITEMS_FRAG_BUNDLE_ARG_CATEGORY_NAME
     *  to NewsItemsListFragment in bundle;
     */
    final static String NEWS_ITEMS_FRAG_BUNDLE_ARG_CATEGORY_NAME = "categoryName";
    final static String NEWS_ITEMS_FRAG_BUNDLE_ARG_CATEGORY_LINK = "categoryLink";

    /** MainActivity instantiate initial default NewsItemsListFragment
     *  with INITIAL_CATEGORY_NAME and INITIAL_CATEGORY_LINK;
     *
     *  RiaNewsDBHelper insert such record
     *  with INITIAL_CATEGORY_NAME and INITIAL_CATEGORY_LINK
     *  to CategoryEntry table in it`s onCreate() ;
     */
    final static String INITIAL_CATEGORY_NAME = "Главное";
    final static String INITIAL_CATEGORY_LINK = "todoLink";


    /*------------------------------------------------*/


    static final String RECEIVER_TAG = "receiverTag";


}
