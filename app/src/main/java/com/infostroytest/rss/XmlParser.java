package com.infostroytest.rss;

import android.content.Context;
import android.util.Log;

import com.infostroytest.rss.model.Feed;
import com.infostroytest.rss.model.Item;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

public final class XmlParser {
    private static final  String TAG = "XmlParser";
    private static final String DEFAULT_CHARSET = "UTF-8";
    private static final  String OFFERS =  "offers";
    private static final  String OFFER = "offer";
    private static final  String OFFER_PRICE = "price";
    private static final String OFFER_TITLE = "name";
    private static final  String OFFER_ID = "id";
    private static final  String OFFER_IMAGE_URL = "picture";
    private static final String DESCRIPTION = "description";

    private final XmlPullParser parser;
    private ParseState state = ParseState.NONE;
    private Context context;
    private Feed feed;
    private Item currentItem = Item.NONE;

   private ArrayList<Item>items = new ArrayList<>();

    private XmlParser(Context context, XmlPullParser parser) {
        this.parser = parser;
        this.context = context;
        feed = Feed.get(context);
    }

    public static XmlParser newInstance(Context context,InputStream inputStream) {
        XmlPullParserFactory factory;
        XmlPullParser parser = null;
        try {
            factory = XmlPullParserFactory.newInstance();
            parser = factory.newPullParser();
            parser.setInput(inputStream, DEFAULT_CHARSET);
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        return new XmlParser(context, parser);
    }

    public void parse() throws Exception {
        int eventType = parser.getEventType();

        while (eventType != XmlPullParser.END_DOCUMENT && items.size()<=100) {
            parseEvent(eventType);
            eventType = parser.next();
        }
        feed.setItems(items);
        feed.saveItems();

    }

    private void parseEvent(int eventType) {
        switch (eventType) {
            case XmlPullParser.START_TAG:
                startTag(parser.getName());
                break;
            case XmlPullParser.END_TAG:
                endTag(parser.getName());
                break;
            case XmlPullParser.TEXT:
                text(parser.getText());
                break;
            default:
        }
    }

    private void startTag(String name) {
        if (OFFERS.equals(name)) {
            items.clear();
            Log.d(TAG, "offers");

        } else if (OFFER.equals(name)) {
            currentItem = new Item();
            Log.d(TAG, "offer");
            int id;
            try
            {
                id =Integer.parseInt(parser.getAttributeValue(null, OFFER_ID));
                Log.d(TAG, "" +id);
            }
            catch (NumberFormatException e) {
               id =  new Random().nextInt();
            }
            currentItem.setId(id);
        }else if (OFFER_PRICE.equals(name)) {
            if (currentItem != Item.NONE) {
                state = ParseState.ITEM_PRICE;
            }
        } else if (DESCRIPTION.equals(name)) {
            if (currentItem != Item.NONE) {
                state = ParseState.ITEM_DESCRIPTION;
            }
        } else if (OFFER_TITLE.equals(name)) {
            if (currentItem != Item.NONE) {
                state = ParseState.ITEM_TITLE;
            }
        } else if (OFFER_IMAGE_URL.equals(name)) {
            if (currentItem != Item.NONE) {
                state = ParseState.ITEM_IMAGE_URL;
            }
        }
    }

    private void text(String text) {
        switch (state) {
            case ITEM_TITLE:
                currentItem.setTitle(text);
                break;
            case ITEM_DESCRIPTION:
                currentItem.setDescription(text);
                break;
            case ITEM_PRICE:
                int price = 0;
                try
                {
                    price = Integer.parseInt(text);
                    Log.d(TAG, "" +price);
                }
                catch (NumberFormatException e) {}
                currentItem.setPrice(price);
                break;
            case ITEM_IMAGE_URL:

                currentItem.setImageUrl(text);
                break;
            default:
                break;
        }
        Log.d(TAG, ""+text);
    }

    private void endTag(String name) {
        if (OFFER.equals(name)) {
            items.add(currentItem);
            currentItem = Item.NONE;
        }
        state = ParseState.NONE;
    }

    private enum ParseState {
        NONE,
        ITEM_PRICE,
        ITEM_DESCRIPTION,
        ITEM_TITLE,
        ITEM_IMAGE_URL
    }
}
