package com.amperas17.rianewsapp;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks {

    final String LOG_TAG = "myLogs";
    final static String SAVE_INST_STATE_ACTIONBAR_TITLE = "title";
    final static String SAVE_INST_STATE_DRAWER_TITLE = "drawerTitle";

    final static String SAVE_INST_STATE_IS_JUST_LAUNCHED = "isJustLaunched";
    final static String SAVE_INST_STATE_BACK_PRESSURE_COUNT = "backPressureCount";


    final static String ACTIONBAR_TITLE = "Новости";

    final static Integer LOADER_ID = 1;
    final static Integer INITIAL_DRAWER_POSITION = 0;

    final static Integer MAX_COUNT_OF_BACK_PRESSURE = 5;



    private DrawerLayout mDrawerLayout;
    private ListView mDrawerListView;
    private ActionBarDrawerToggle mDrawerToggle;
    private String mDrawerTitle;
    private String mTitle;

    private Boolean mIsJustLaunched;

    private CategoryListItemAdapter mCategoriesAdapter;
    private FragmentManager mFragmentManager;

    private int mBackPressureCount;


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        getSupportActionBar().setTitle(savedInstanceState.getString(SAVE_INST_STATE_ACTIONBAR_TITLE));
        mDrawerTitle = savedInstanceState.getString(SAVE_INST_STATE_DRAWER_TITLE);
        mBackPressureCount = savedInstanceState.getInt(SAVE_INST_STATE_BACK_PRESSURE_COUNT);
        mIsJustLaunched = savedInstanceState.getBoolean(SAVE_INST_STATE_IS_JUST_LAUNCHED);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(LOG_TAG, "Main[onCreate]: ");

        //Set StatusBarColor
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#323232"));
        }

        mBackPressureCount = MAX_COUNT_OF_BACK_PRESSURE;

        /**--------Drawer functionality-------*/
        mIsJustLaunched = true;
        mTitle = mDrawerTitle = ACTIONBAR_TITLE;

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerListView = (ListView) findViewById(R.id.drawer_list);
        mDrawerListView.setOnItemClickListener(new DrawerItemClickListener());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#424242")));

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.drawer_open, R.string.drawer_close){

            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(mDrawerTitle);
                supportInvalidateOptionsMenu();
            }

            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(mTitle);
                supportInvalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        /**-----------------------------------*/

        getSupportLoaderManager().initLoader(LOADER_ID, null, this);
        mFragmentManager = getSupportFragmentManager();

        if (savedInstanceState == null) {
            //Set initial NewsListFragment after the launching
            selectItem(INITIAL_DRAWER_POSITION,AppContract.INITIAL_CATEGORY_NAME,
                    AppContract.INITIAL_CATEGORY_LINK);
        }

        //start service to refresh database categoriesTable and categoriesList
        if (isNetworkConnected()) {
            Intent intent = new Intent(this, GettingCategoriesService.class);
            startService(intent);
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(SAVE_INST_STATE_ACTIONBAR_TITLE, getSupportActionBar().getTitle().toString());
        outState.putString(SAVE_INST_STATE_DRAWER_TITLE, mDrawerTitle);

        outState.putBoolean(SAVE_INST_STATE_IS_JUST_LAUNCHED, mIsJustLaunched);
        outState.putInt(SAVE_INST_STATE_BACK_PRESSURE_COUNT, mBackPressureCount);

    }


    public void onClickInsert(View v) {
        ContentValues cv = new ContentValues();
        //cv.put(RiaNewsDBContract.CategoryEntry.COLUMN_NAME, "header _1");
        //cv.put(RiaNewsDBContract.CategoryEntry.COLUMN_LINK, "link _1");

        //getContentResolver().insert(RiaNewsDBContract.CATEGORIES_URI, cv);

        cv.clear();
        cv.put(RiaNewsDBContract.NewsItemEntry.COLUMN_HEADER, "header __");
        cv.put(RiaNewsDBContract.NewsItemEntry.COLUMN_LINK, "link __");
        cv.put(RiaNewsDBContract.NewsItemEntry.COLUMN_CATEGORY, "3");
        cv.put(RiaNewsDBContract.NewsItemEntry.COLUMN_DESCRIPTION, "desc __");
        cv.put(RiaNewsDBContract.NewsItemEntry.COLUMN_NEWS_TEXT, "text __");
        cv.put(RiaNewsDBContract.NewsItemEntry.COLUMN_NEWS_DATE, "date __");
        cv.put(RiaNewsDBContract.NewsItemEntry.COLUMN_IMAGE_SRC, "link __");

        getContentResolver().insert(RiaNewsDBContract.NEWS_ITEMS_URI, cv);
        //Log.d(LOG_TAG, "insert, result Uri : " + newUri.toString());
    }


    //attempt to make ability to pop last 5 news fragments and only then close app
    @Override
    public void onBackPressed() {
        int count = mFragmentManager.getBackStackEntryCount();
        //Log.d(AppContract.LOG_TAG, "MainActivity[onBackPressed]: " + count);

        if (count > 1 && mBackPressureCount > 0) {
            getSupportFragmentManager().popBackStack();
            String title = getSupportFragmentManager().getFragments().get(count-2)
                    .getArguments().getString(RiaNewsDBContract.CategoryEntry.COLUMN_NAME);
            setTitle(title);
            mBackPressureCount--;

            for (int i=0;i<mDrawerListView.getChildCount();i++){
                TextView tv = (TextView)mDrawerListView.getChildAt(i).findViewById(R.id.tv_category_name);
                String s = tv.getText().toString();
                if (s.equals(title)){
                    mDrawerListView.setItemChecked(i, true);
                    break;
                }
            }

        } else {
            finish();
        }

    }


    private boolean isNetworkConnected() {
        ConnectivityManager manager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        return (networkInfo==null?false:true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**----------Methods provide drawer functionality------*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            TextView tvCategoryTitle = (TextView)view.findViewById(R.id.tv_category_name);
            TextView tvCategoryLink = (TextView)view.findViewById(R.id.tv_category_link);

            Log.d(AppContract.LOG_TAG, "MainActivity[onItemClick]: " +
                    tvCategoryTitle.getText() + " : " +
                    tvCategoryLink.getText() );

            selectItem(position,tvCategoryTitle.getText().toString(),tvCategoryLink.getText().toString());
        }
    }

    private void selectItem(int position,String categoryName,String categoryLink) {
        Fragment fragment;
        fragment = new NewsItemsListFragment();

        if (fragment != null) {

            Bundle bundle = new Bundle();
            bundle.putString(AppContract.NEWS_ITEMS_FRAG_BUNDLE_ARG_CATEGORY_NAME, categoryName);
            bundle.putString(AppContract.NEWS_ITEMS_FRAG_BUNDLE_ARG_CATEGORY_LINK, categoryLink);
            fragment.setArguments(bundle);

            Log.d(AppContract.LOG_TAG, "MainActivity[selectItem]: " + fragment.getArguments());

            mFragmentManager.beginTransaction()
                    .addToBackStack(categoryName)
                    .replace(R.id.frame_for_drawer, fragment)
                    .commit();

            if (mBackPressureCount <MAX_COUNT_OF_BACK_PRESSURE){
                mBackPressureCount++;
            }

            //Log.d(AppContract.LOG_TAG, "MainActivity[selectItem]: " + mFragmentManager.getBackStackEntryCount());
            mDrawerListView.setItemChecked(position, true);
            setTitle(categoryName);
            mDrawerLayout.closeDrawer(mDrawerListView);

        } else {
            throw new ExceptionInInitializerError("MainActivity[selectItem]: fragment is null!");
            //Toast.makeText(this,"Something goes wrong! Restart app!",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title.toString();
        getSupportActionBar().setTitle(mTitle);
    }

    //Toggle animation
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
    /**-----------------------------------------------------------*/




    /**----CursorLoader gives mCategories from DB CategoryEntry table to drawerList----*/

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        CursorLoader cursorLoader = new CursorLoader(this, RiaNewsDBContract.CATEGORIES_URI, null,
                null, null, null);
        Log.d(LOG_TAG,"Main[onCreateLoader]: ");
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {
        mCategoriesAdapter = new CategoryListItemAdapter(this,(Cursor) data,0);
        mDrawerListView.setAdapter(mCategoriesAdapter);
        Log.d(LOG_TAG, "Main[onLoadFinished]: ");

        //I think it is crutch, but it works
        if (mIsJustLaunched){
            mDrawerListView.setItemChecked(0, true);
            mIsJustLaunched = false;
        }
    }

    @Override
    public void onLoaderReset(Loader loader) {
        mCategoriesAdapter.swapCursor(null);
    }
    /**----------------------------------------------------------------------------*/

}
