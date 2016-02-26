package com.amperas17.rianewsapp;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class NewsItemsListFragment extends ListFragment
        implements LoaderManager.LoaderCallbacks, DataReceiver.Receiver{

    final static String SAVE_INST_STATE_CATEGORY_NAME = "categoryName";
    final static String SAVE_INST_STATE_CATEGORY_LINK = "categoryLink";
    final static Integer LOADER_ID = 2;


    String mCategoryName;
    String mCategoryLink;
    ProgressBar progressBar;

    NewsListItemAdapter mNewsListItemAdapter;

    DataReceiver mDataReceiver;
    Boolean mIsServiceRunning = false;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Log.d(AppContract.LOG_TAG, "Frag[onCreate]: ");
        if (savedInstanceState!=null){
            mCategoryName = savedInstanceState.getString(SAVE_INST_STATE_CATEGORY_NAME);
            mCategoryLink = savedInstanceState.getString(SAVE_INST_STATE_CATEGORY_LINK);
        } else {
            mCategoryName = getArguments().getString(AppContract.NEWS_ITEMS_FRAG_BUNDLE_ARG_CATEGORY_NAME);
            mCategoryLink = getArguments().getString(AppContract.NEWS_ITEMS_FRAG_BUNDLE_ARG_CATEGORY_LINK);
        }

    }



    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(SAVE_INST_STATE_CATEGORY_NAME, mCategoryName);
        outState.putString(SAVE_INST_STATE_CATEGORY_LINK, mCategoryLink);

        //Log.d(AppContract.LOG_TAG, "NewsFragment[onSaveInstanceState]: " + outState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_items_list, container, false);
        //Log.d(AppContract.LOG_TAG,"Frag[onCreateView]");


        progressBar = (ProgressBar) view.findViewById(R.id.progressbar_news_fragment);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //Log.d(AppContract.LOG_TAG, "Frag[onActivityCreated]");

        getActivity().getSupportLoaderManager().restartLoader(LOADER_ID, null, this);
        refreshData();
    }

    private void refreshData(){
        if (isNetworkConnected()) {
            if (!mIsServiceRunning) {
                mDataReceiver = new DataReceiver(new Handler());

                mDataReceiver.setReceiver(this);

                Intent intent = new Intent(getActivity(), GettingDataService.class);
                intent.putExtra(RiaNewsDBContract.CategoryEntry.COLUMN_NAME, mCategoryName);
                intent.putExtra(RiaNewsDBContract.CategoryEntry.COLUMN_LINK, mCategoryLink);
                Log.d(AppContract.LOG_TAG, "Frag[refreshData]: ");

                intent.putExtra(AppContract.RECEIVER_TAG, mDataReceiver);
                getActivity().startService(intent);
                mIsServiceRunning = true;

            } else {
                Toast.makeText(getActivity(), R.string.service_running_message, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getActivity(), R.string.no_internet_message, Toast.LENGTH_SHORT).show();
        }
    }





    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        mIsServiceRunning = false;
        Log.d(AppContract.LOG_TAG,"Frag[onReceiveResult]: ");
    }



    private boolean isNetworkConnected() {
        ConnectivityManager manager = (ConnectivityManager)
                getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        return (networkInfo==null?false:true);
    }


    /**----CursorLoader gives newsItems from DB NewsItemsEntry table to Fragment`s list----*/
    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        //Log.d(AppContract.LOG_TAG,"Frag[onCreateLoader]");

        String selection = RiaNewsDBContract.CategoryEntry.COLUMN_NAME + "=\""+ mCategoryName+"\"";

        CursorLoader cursorLoader = new CursorLoader(getActivity(), RiaNewsDBContract.NEWS_ITEMS_URI, null,
                selection, null, null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {
        mNewsListItemAdapter = new NewsListItemAdapter(getActivity(),(Cursor) data,0);
        //Log.d(AppContract.LOG_TAG, "Frag[onLoadFinished] " + ((Cursor) data).getCount());

        getListView().setAdapter(mNewsListItemAdapter);
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onLoaderReset(Loader loader) {
        mNewsListItemAdapter.swapCursor(null);
    }

    /**----------------------------------------------------------------------------*/

}
