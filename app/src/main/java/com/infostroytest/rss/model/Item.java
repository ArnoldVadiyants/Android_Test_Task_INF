package com.infostroytest.rss.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Item implements Serializable {

    private static final String JSON_PRICE = "price";
    private static final String JSON_IMAGE_URL = "picture";
    private static final String JSON_TITLE = "title";
    private static final String JSON_ID = "id";
    private static final String JSON_DESCRIPTION = "description";

    public static final Item NONE = new Item();

    private String title;
    private String description;
    private String imageUrl;
    private int price;
    private int id;
    public Item() {
        title = "Title";
        description = "Description";
        imageUrl = "imageUrl";
        price = 0;
        id = -999;

    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Item(JSONObject json) throws JSONException {

        id = json.getInt(JSON_ID);
        title = json.getString(JSON_TITLE);
        description = json.getString(JSON_DESCRIPTION);
        imageUrl = json.getString(JSON_IMAGE_URL);
        price = json.getInt(JSON_PRICE);

    }

    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();

        json.put(JSON_ID, id);
        json.put(JSON_TITLE, title);
        json.put(JSON_DESCRIPTION, description);
        json.put(JSON_IMAGE_URL, imageUrl);
        json.put(JSON_PRICE, price);

        return json;
    }

}
