package com.infostroytest.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.infostroytest.R;
import com.infostroytest.rss.model.Item;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;


public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.ViewHolder> {
    static ImageLoader imageLoader= ImageLoader.getInstance();
    static DisplayImageOptions loaderOptions =  new DisplayImageOptions.Builder()
            .showImageOnLoading(R.drawable.loading_animation)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .build();



    private List<Item> items;
    private ItemClickListener itemClickListener;

    public FeedAdapter( List<Item> objects, @NonNull ItemClickListener itemClickListener) {
        this.items = objects;
        this.itemClickListener = itemClickListener;
        setHasStableIds(true);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        Context context = viewGroup.getContext();
        View parent = LayoutInflater.from(context).inflate(R.layout.feed_list_item, viewGroup, false);
        return ViewHolder.newInstance(parent);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        final Item item = items.get(position);
        viewHolder.setTitle(item.getTitle());
        viewHolder.setPrice("" + item.getPrice() + " " + "грн.");
        viewHolder.setImage(item.getImageUrl());

        viewHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.itemClicked(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public long getItemId(int position) {
        return items.get(position).getId();
    }

    public void moveItem(int start, int end) {
        int max = Math.max(start, end);
        int min = Math.min(start, end);
        if (min >= 0 && max < items.size()) {
            Item item = items.remove(min);
            items.add(max, item);
            notifyItemMoved(min, max);
        }
    }

    public int getPositionForId(long id) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getId() == id) {
                return i;
            }
        }
        return -1;
    }

    public static final class ViewHolder extends RecyclerView.ViewHolder {
        private final View parent;
        private final TextView title;
        private final TextView price;
        private final ImageView image;

        public static ViewHolder newInstance(View parent) {
            TextView title = (TextView) parent.findViewById(R.id.feed_item_name);
            TextView price = (TextView) parent.findViewById(R.id.feed_item_price);
            ImageView image = (ImageView) parent.findViewById(R.id.feed_item_picture);
            return new ViewHolder(parent, title, price, image);
        }

        private ViewHolder(View parent, TextView title, TextView price,  ImageView image) {
            super(parent);
            this.parent = parent;
            this.title = title;
            this.price = price;
            this.image = image;
        }

        public void setTitle(CharSequence text) {
            title.setText(text);
        }

        public void setPrice(CharSequence text) {price.setText(text);
        }

        public void setImage(String imageUrl) {

               imageLoader.displayImage(imageUrl,image, loaderOptions);
        }

        public void setOnClickListener(View.OnClickListener listener) {
            parent.setOnClickListener(listener);
        }


    }

    public interface ItemClickListener {
        void itemClicked(Item item);
    }
}
