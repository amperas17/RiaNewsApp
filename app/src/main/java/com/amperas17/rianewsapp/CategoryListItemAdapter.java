package com.amperas17.rianewsapp;


import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Adapter with mCategories for drawerList;
 */
public class CategoryListItemAdapter extends CursorAdapter {

    public CategoryListItemAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.drawer_category_list_item,parent,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView tvTitle = (TextView)view.findViewById(R.id.tv_category_name);
        tvTitle.setText(""+cursor.getString(cursor.getColumnIndex(RiaNewsDBContract.CategoryEntry.COLUMN_NAME)));

        TextView tvLink = (TextView)view.findViewById(R.id.tv_category_link);
        tvLink.setText(""+cursor.getString(cursor.getColumnIndex(RiaNewsDBContract.CategoryEntry.COLUMN_LINK)));

    }
}
