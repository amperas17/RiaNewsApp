package com.amperas17.rianewsapp;


import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Adapter with newsItems data for NewsItemsFragment list;
 */
public class NewsListItemAdapter extends CursorAdapter {
    public NewsListItemAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View rootView = LayoutInflater.from(context)
                .inflate(R.layout.news_items_list_item, parent, false);

        ViewHolder holder = new ViewHolder();

        ImageView ivImage = (ImageView)rootView.findViewById(R.id.iv_news_list_item_image);
        holder.ivImage = ivImage;

        TextView tvHeader = (TextView)rootView.findViewById(R.id.tv_news_list_item_header);
        holder.tvHeader = tvHeader;

        TextView tvDescription = (TextView)rootView.findViewById(R.id.tv_news_list_item_description);
        holder.tvDescription = tvDescription;

        TextView tvLink = (TextView)rootView.findViewById(R.id.tv_news_list_item_link);
        holder.tvLink = tvLink;

        rootView.setTag(holder);

        return rootView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        /*ImageView ivImage = (ImageView)view.findViewById(R.id.iv_news_list_item_image);

        TextView tvHeader = (TextView)view.findViewById(R.id.tv_news_list_item_header);
        tvHeader.setText(cursor.getString(cursor
                .getColumnIndex(RiaNewsDBContract.NewsItemEntry.COLUMN_HEADER)));

        TextView tvDescription = (TextView)view.findViewById(R.id.tv_news_list_item_description);
        tvDescription.setText(cursor.getString(cursor
                .getColumnIndex(RiaNewsDBContract.NewsItemEntry.COLUMN_DESCRIPTION)));

        TextView tvLink = (TextView)view.findViewById(R.id.tv_news_list_item_link);
        tvLink.setText(cursor.getString(cursor
                .getColumnIndex(RiaNewsDBContract.NewsItemEntry.COLUMN_LINK)));*/

        ViewHolder holder = (ViewHolder)view.getTag();
        if (holder != null){
            //holder.ivImage ....
            holder.tvHeader.setText(cursor.getString(cursor
                    .getColumnIndex(RiaNewsDBContract.NewsItemEntry.COLUMN_HEADER)));
            holder.tvDescription.setText(cursor.getString(cursor
                    .getColumnIndex(RiaNewsDBContract.NewsItemEntry.COLUMN_DESCRIPTION)));
            holder.tvLink.setText(cursor.getString(cursor
                    .getColumnIndex(RiaNewsDBContract.NewsItemEntry.COLUMN_LINK)));
        }


    }

    public static class ViewHolder {
        public ImageView ivImage;
        public TextView tvHeader;
        public TextView tvDescription;
        public TextView tvLink;
    }
}
