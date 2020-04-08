package com.example.groupproject1.NASAImgDay;

import android.graphics.Bitmap;

public class ObjectHolder {
    String date, title, Query;
    Bitmap img;
    public ObjectHolder() {

    }

    public void setQuery(String query) {
        Query = query;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setImg(Bitmap img) {
        this.img = img;
    }

    public String getQuery() {
        return Query;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public Bitmap getImg() {
        return img;
    }
}
