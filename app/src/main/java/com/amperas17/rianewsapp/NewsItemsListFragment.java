package com.amperas17.rianewsapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class NewsItemsListFragment extends Fragment {

    final static String SAVE_INST_STATE_CATEGORY_NAME = "categoryName";
    final static String SAVE_INST_STATE_CATEGORY_LINK = "categoryLink";

    String mCategoryName;
    String mCategoryLink;
    TextView mTvmCategoryLink;
    TextView mTvmCategoryName;

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

        return view;
    }


}
