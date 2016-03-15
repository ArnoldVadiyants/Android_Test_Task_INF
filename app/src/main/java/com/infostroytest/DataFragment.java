package com.infostroytest;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.infostroytest.net.VolleySingleton;
import com.infostroytest.rss.XmlRequest;
import com.infostroytest.rss.model.Feed;


public class DataFragment extends Fragment implements Response.Listener<Feed>, Response.ErrorListener {
    private String URL;
    private static final String TAG = "DataFragment";
    private Feed feed;
    private Context context;
    private VolleySingleton volley;

    private FeedConsumer feedConsumer;
    private boolean isLoading = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = inflater.getContext();
        URL = getString(R.string.feed_url);
        initVolley(context);
        postData();
        Log.d(TAG, "onCreateView");

        return null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        volley.stop();
    }
    @Override
    public void onAttach(Context context) {
         //  super.onAttach(context);
        Log.d(TAG, "onAttach");
        Activity a;
        if(context instanceof Activity)
        {
            a = (Activity)context;
            onAttach(a);
        }
     /*   if (context instanceof  com.materialdesign.FeedConsumer){
        // // Activity  activity=(Activity) context;
         //   if (activity instanceof com.materialdesign.FeedConsumer) {
            //    feedConsumer = (com.materialdesign.FeedConsumer) context;
            onAttach(hostActivity);
           }
       // }*/
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof FeedConsumer) {
            Log.d(TAG, "onAttachAct");
            feedConsumer = (FeedConsumer) activity;
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "onDetach");
        feedConsumer = null;
    }



    @Override
    public void onResponse(Feed feed) {
        this.feed = feed;
        if (feedConsumer != null) {
            feedConsumer.initializeFeed();
            feedConsumer.hideProgressBar();
        }
        Log.d(TAG, "onResponse");
        isLoading = false;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        if (feedConsumer != null)
            feedConsumer.initializeFeed(); {
            feedConsumer.handleError("Check your internet connection");
            feedConsumer.hideProgressBar();
        }
        Log.d(TAG, "onErrorResponse");
        isLoading = false;
    }

    private void initVolley(Context context) {
        if (volley == null) {
            volley = VolleySingleton.getInstance(context);
        }
    }
    private void postData()
    {
        Log.d(TAG, "postData");
        if(feed==null &&!isLoading()) {
            Log.d(TAG, "isLoading");
            volley.addToRequestQueue(new XmlRequest(context, Request.Method.GET, URL, this, this));
            if (feedConsumer != null) {
                Log.d(TAG, "postData");
                feedConsumer.showProgressBar();
            }
            isLoading = true;
        }else {
            if (feedConsumer != null) {
                feedConsumer.hideProgressBar();
                feedConsumer.initializeFeed();
            }
        }
    }
    public void update() {
        Log.d(TAG, "update");
    //    if (!isLoading()) {
            feed = null;
            Log.d(TAG, "isLoading");
            volley.addToRequestQueue(new XmlRequest(context, Request.Method.GET, URL, this, this));
            if (feedConsumer != null) {
                feedConsumer.showProgressBar();
        //    }

            isLoading = true;
        }
    }

    public boolean isLoading() {
        return isLoading;
    }
}
