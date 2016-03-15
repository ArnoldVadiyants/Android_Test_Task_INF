package com.infostroytest;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.infostroytest.rss.model.Item;
import com.nostra13.universalimageloader.core.ImageLoader;

public class FeedDetailActivity extends ActionBarActivity {
    public static final String ARG_ITEM = "ARG_ITEM";

   // private DateFormat dateFormat = SimpleDateFormat.getDateInstance(SimpleDateFormat.MEDIUM, Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feed_detail);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Item item = (Item) getIntent().getSerializableExtra(ARG_ITEM);

        TextView title = (TextView) findViewById(R.id.feed_detail_title);
        TextView price = (TextView) findViewById(R.id.feed_detail_price);
        TextView detail = (TextView) findViewById(R.id.feed_detail_body);
        ImageView picture = (ImageView) findViewById(R.id.feed_detail_picture);

        title.setText(item.getTitle());
        price.setText(item.getPrice() + " " + "грн.");
        detail.setText(item.getDescription());
        ImageLoader imageLoader= ImageLoader.getInstance();
        imageLoader.displayImage(item.getImageUrl(), picture);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int id = menuItem.getItemId();
        if (id == android.R.id.home) {
           finish();
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }
}
