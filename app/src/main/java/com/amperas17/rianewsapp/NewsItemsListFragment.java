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

        if (savedInstanceState!=null){
            mCategoryName = savedInstanceState.getString(SAVE_INST_STATE_CATEGORY_NAME);
            mCategoryLink = savedInstanceState.getString(SAVE_INST_STATE_CATEGORY_LINK);
        } else {
            mCategoryName = getArguments().getString(AppContract.NEWS_ITEMS_FRAG_BUNDLE_ARG_CATEGORY_NAME);
            mCategoryLink = getArguments().getString(AppContract.NEWS_ITEMS_FRAG_BUNDLE_ARG_CATEGORY_LINK);
        }

        getActivity().getSupportLoaderManager().initLoader(LOADER_ID, null, this);




    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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

        mTvmCategoryName = (TextView)view.findViewById(R.id.tvNewsItemCategoryName);
        mTvmCategoryName.setText(mCategoryName);

        mTvmCategoryLink = (TextView)view.findViewById(R.id.tvNewsItemCategoryLink);
        mTvmCategoryLink.setText(mCategoryLink);

        progressBar = (ProgressBar) view.findViewById(R.id.progressbar_news_fragment);


        return view;
    }


    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        CursorLoader cursorLoader = new CursorLoader(getActivity(), RiaNewsDBContract.NEWS_ITEMS_URI, null,
                null, null, null);
        Log.d(AppContract.LOG_TAG, "NewsFrag[onCreateLoader]");
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {
        mNewsListItemAdapter = new NewsListItemAdapter(getActivity(),(Cursor) data,0);
        getListView().setAdapter(mNewsListItemAdapter);
        Log.d(AppContract.LOG_TAG, "onLoadFinished " + data);
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onLoaderReset(Loader loader) {
        mNewsListItemAdapter.swapCursor(null);
    }
}
