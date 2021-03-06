package com.com.teamupapps.searchsms.models;

/**
 * Created by nrobatmeily on 27/10/2014.
 */
public class SMS {

    private String id;
    private String name;
    private String content;
    private String date;
    private String imageURI;
    private String url;

    public SMS(String id, String name, String content, String date, String imageURI) {
        this.id = id;
        this.name = name;
        this.content = content;
        this.date = date;
        this.imageURI = imageURI;
    }

    public String getImageURI() {
        return imageURI;
    }

    public String getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
