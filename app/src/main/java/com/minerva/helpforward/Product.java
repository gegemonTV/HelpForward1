package com.minerva.helpforward;

import android.net.Uri;

public class Product {
    private String name, description;
    private long price;
    private Uri url;

    public Product() {

    }

    public Product(String name, String description, long price, String url) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.url = Uri.parse(url);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public void setUrl(String url) {
        this.url = Uri.parse(url);
    }

    public Uri getUrl() {
        return url;
    }
}
