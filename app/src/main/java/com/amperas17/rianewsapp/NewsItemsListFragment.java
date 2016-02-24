package com.amperas17.rianewsapp;


import android.database.Cursor;
import android.os.Bundle;
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

public class NewsItemsListFragment extends ListFragment implements LoaderManager.LoaderCallbacks{

    final static String SAVE_INST_STATE_CATEGORY_NAME = "categoryName";
    final static String SAVE_INST_STATE_CATEGORY_LINK = "categoryLink";
    final static Integer LOADER_ID = 2;


    String mCategoryName;
    String mCategoryLink;
    TextView mTvmCategoryLink;
    TextView mTvmCategoryName;
    ProgressBar progressBar;

    NewsListItemAdapter mNewsListItemAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(AppContract.LOG_TAG, "Frag[onCreate]: ");
        if (savedInstanceState!=null){
            mCategoryName = savedInstanceState.getString(SAVE_INST_STATE_CATEGORY_NAME);
            mCategoryLink = savedInstanceState.getString(SAVE_INST_STATE_CATEGORY_LINK);
        } else {
            mCategoryName = getArguments().getString(AppContract.NEWS_ITEMS_FRAG_BUNDLE_ARG_CATEGORY_NAME);
            mCategoryLink = getArguments().getString(AppContract.NEWS_ITEMS_FRAG_BUNDLE_ARG_CATEGORY_LINK);
        }

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //Log.d(AppContract.LOG_TAG, "Frag[onActivityCreated]");

        getActivity().getSupportLoaderManager().restartLoader(LOADER_ID, null, this);

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
        Log.d(AppContract.LOG_TAG,"Frag[onCreateView]");

        mTvmCategoryName = (TextView)view.findViewById(R.id.tvNewsItemCategoryName);
        mTvmCategoryName.setText(mCategoryName);

        mTvmCategoryLink = (TextView)view.findViewById(R.id.tvNewsItemCategoryLink);
        mTvmCategoryLink.setText(mCategoryLink);

        progressBar = (ProgressBar) view.findViewById(R.id.progressbar_news_fragment);

        return view;
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
