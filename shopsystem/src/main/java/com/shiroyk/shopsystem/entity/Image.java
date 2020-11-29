package com.shiroyk.shopsystem.entity;

public class Image {
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "{" + "\"url:\"" + url + "}";
    }
}
