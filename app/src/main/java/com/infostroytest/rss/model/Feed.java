package com.infostroytest.rss.model;

import android.content.Context;
import android.util.Log;

import com.infostroytest.common.ItemIntentJSONSerializer;

import java.io.Serializable;
import java.util.ArrayList;

public class Feed implements Serializable {
    private static final String TAG = "Feed";
    private static final String ITEMS_FILENAME = "items.json";
    private ArrayList<Item> mItems = new ArrayList<>();
    private ItemIntentJSONSerializer mSerializer;
    private static Feed sFeed;
    private Context mAppContext;



    private Feed(Context appContext) {
        mAppContext = appContext;
        mSerializer = new ItemIntentJSONSerializer(mAppContext, ITEMS_FILENAME);
        try {
            mItems = mSerializer.loadItems();
            Log.d(TAG, "loading items: ");
        } catch (Exception e) {
            //Log.e(TAG, "Error loading items: ", e);
        }

    }
    public static Feed get(Context c) {
        if (sFeed == null) {
            sFeed = new Feed(c.getApplicationContext());
        }
        //	else newConstructor = false;
        return sFeed;
    }

    public void addItem(Item item) {
        mItems.add(item);
    }

    public  ArrayList<Item> getItems() {
        return mItems;
    }
    public void setItems(ArrayList<Item>items) {
        this.mItems = items;
    }


    public boolean saveItems() {
        try {

            mSerializer.saveItems(mItems);
            Log.d(TAG, "Items saved to file");
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Error saving items: ", e);
            return false;
        }
    }
}
