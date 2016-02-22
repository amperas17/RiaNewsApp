package com.amperas17.rianewsapp;

import android.content.ContentValues;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
    final static String ACTIONBAR_TITLE = "Новости";

    final static Integer LOADER_ID = 1;
    final static Integer INITIAL_DRAWER_POSITION = 0;


    private DrawerLayout mDrawerLayout;
    private ListView mDrawerListView;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;

    private Boolean mIsJustLaunched;


    CategoryListItemAdapter mCategoriesAdapter;


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        getSupportActionBar().setTitle(savedInstanceState.getString(SAVE_INST_STATE_ACTIONBAR_TITLE));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(LOG_TAG, "onCreate");

        mIsJustLaunched = true;

        //Set StatusBarColor
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#323232"));
        }

        /**--------Drawer functionality-------*/
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

        if (savedInstanceState == null) {
            //Set initial NewsListFragment after the launching
            selectItem(INITIAL_DRAWER_POSITION,AppContract.INITIAL_CATEGORY_NAME,
                    AppContract.INITIAL_CATEGORY_LINK);
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(SAVE_INST_STATE_ACTIONBAR_TITLE, getSupportActionBar().getTitle().toString());
    }


    public void onClickInsert(View v) {
        ContentValues cv = new ContentValues();
        cv.put(RiaNewsDBContract.CategoryEntry.COLUMN_NAME, "header _1");
        cv.put(RiaNewsDBContract.CategoryEntry.COLUMN_LINK, "link _1");

        getContentResolver().insert(RiaNewsDBContract.CATEGORY_URI, cv);
        //Log.d(LOG_TAG, "insert, result Uri : " + newUri.toString());
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
            TextView tvCategoryTitle = (TextView)view.findViewById(R.id.tvCategoryTitle);
            TextView tvCategoryLink = (TextView)view.findViewById(R.id.tvCategoryLink);

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

            //Log.d(AppContract.LOG_TAG, "MainActivity[selectItem]: " + fragment.getArguments());

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_for_drawer, fragment)
                    .commit();

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
        mTitle = title;
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




    /**----CursorLoader gives categories from DB CategoryEntry table to drawerList----*/

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        CursorLoader cursorLoader = new CursorLoader(this, RiaNewsDBContract.CATEGORY_URI, null,
                null, null, null);
        Log.d(LOG_TAG,"onCreateLoader");
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {
        mCategoriesAdapter = new CategoryListItemAdapter(this,(Cursor) data,0);
        mDrawerListView.setAdapter(mCategoriesAdapter);
        Log.d(LOG_TAG, "onLoadFinished " + ((Cursor) data));

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
