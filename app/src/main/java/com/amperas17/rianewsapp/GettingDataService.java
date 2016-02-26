package com.amperas17.rianewsapp;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

/**
 * Created by Вова on 24.02.2016.
 */
public class GettingDataService extends IntentService {
    public GettingDataService() {
        super("GettingDataService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(AppContract.LOG_TAG,"GettingDataService[onHandleIntent]: " );

        if (intent != null) {
            ResultReceiver receiver = intent.getParcelableExtra(AppContract.RECEIVER_TAG);
            String categoryName = intent.getStringExtra(RiaNewsDBContract.CategoryEntry.COLUMN_NAME);
            String categoryLink = intent.getStringExtra(RiaNewsDBContract.CategoryEntry.COLUMN_LINK);
            Log.d(AppContract.LOG_TAG,"GettingDataService[onHandleIntent]: "+categoryName+" "+categoryLink);
            //-------------------------------------------------------//

            getData(categoryName,categoryLink);

            //-------------------------------------------------------//
            Bundle bundle = new Bundle();
            //bundle.putString(AppContract.HTML_CONTENT_TAG,content);
            receiver.send(0, bundle);
        }
    }



    private void getData(String categoryName,String categoryLink){


    }



}
