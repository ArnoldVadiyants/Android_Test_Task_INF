package com.infostroytest;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.infostroytest.adapter.FeedAdapter;
import com.infostroytest.common.DragController;
import com.infostroytest.rss.model.Feed;
import com.infostroytest.rss.model.Item;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;

public class FeedListActivity extends AppCompatActivity
        implements FeedConsumer, FeedAdapter.ItemClickListener {
    private static final String TAG = "FeedListActivity";
    private static final String DATA_FRAGMENT_TAG = DataFragment.class.getCanonicalName();
    private MenuItem mRefresh = null;
    private Handler mHandler = new Handler();
    private RecyclerView recyclerView;
    private DataFragment dataFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feed_list);
        recyclerView = (RecyclerView) findViewById(R.id.list);
        ImageView overlay = (ImageView) findViewById(R.id.overlay);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
       layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnItemTouchListener(new DragController(recyclerView, overlay));

         dataFragment = (DataFragment) getFragmentManager().findFragmentByTag(DATA_FRAGMENT_TAG);
        if (dataFragment == null) {
            Log.d(TAG, "dataFragmentInitialized");
            dataFragment = (DataFragment) Fragment.instantiate(this, DataFragment.class.getName());
            dataFragment.setRetainInstance(true);
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.add(dataFragment, DATA_FRAGMENT_TAG);
            transaction.commit();

            Log.d(TAG, "dataFragment is initialized");
        }
       /* else
        {
            Log.d(TAG, "dataFragment has already initialized");
            initializeFeed();
        }*/
        ImageLoader imageLoader = ImageLoader.getInstance();
        File cacheDir = StorageUtils.getCacheDirectory(getApplicationContext());
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())

                .diskCacheExtraOptions(480, 800, null)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                .memoryCacheSize(2 * 1024 * 1024)
                .diskCache(new UnlimitedDiskCache(cacheDir))
                .diskCacheSize(100 * 1024 * 1024)
                .writeDebugLogs()
                .build();
        imageLoader.init(config);
    }


    @Override
    public void showProgressBar() {
        Log.d(TAG, "showProgressBar");

        if(mRefresh!=null) {
            mRefresh.setActionView(R.layout.layout_progress);
        }
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.toolbar_progress_bar);
        progressBar.setVisibility(View.VISIBLE);
        Log.d(TAG, "mRefresh = null");
     // recyclerView.setVisibility(View.GONE);
    }

    @Override
    public void hideProgressBar() {
        Log.d(TAG, "hideProgressBar");
        if(mRefresh!=null) {
            mRefresh.setActionView(null);
        }

                ProgressBar progressBar = (ProgressBar) findViewById(R.id.toolbar_progress_bar);
                progressBar.setVisibility(View.GONE);
                Log.d(TAG, "mRefresh = null");


    //    recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void initializeFeed() {
        Log.d(TAG, "initialize feed");
        FeedAdapter adapter = new FeedAdapter(Feed.get(getApplicationContext()).getItems(), this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void handleError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void itemClicked(Item item) {
        Intent detailIntent = new Intent(FeedListActivity.this, FeedDetailActivity.class);
        detailIntent.putExtra(FeedDetailActivity.ARG_ITEM, item);
        startActivity(detailIntent);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu");
                getMenuInflater().inflate(R.menu.activity_main, menu);
        mRefresh = menu.findItem(R.id.menu_refresh);
        return true;
    }

   @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_refresh) {
         //   item.setActionView(R.layout.layout_progress);

                        dataFragment.update();
            Log.d(TAG, "onOptionsItemSelected");
            }


        return super.onOptionsItemSelected(item);
    }

 /*   private void refreshComplete() {
        if(dataFragment!=null)
        {
            dataFragment.update();
            while (dataFragment.isLoading());
        }
        mRefresh.setActionView(null);
    }*/
}
