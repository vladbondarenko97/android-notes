package com.vladhq.mynewapp;


public class Paste {
    private String title, url, data;

    public Paste() {
    }

    public Paste(String title, String url, String data) {
        this.title = title;
        this.url = url;
        this.data = data;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String year) {
        this.url = url;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}