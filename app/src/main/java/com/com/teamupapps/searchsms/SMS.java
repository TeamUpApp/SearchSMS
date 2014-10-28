package com.com.teamupapps.searchsms;

/**
 * Created by nrobatmeily on 27/10/2014.
 */
public class SMS {

    private String id;
    private String name;
    private String content;
    private String date;

    public SMS(String id, String name, String content, String date) {
        this.id = id;
        this.name = name;
        this.content = content;
        this.date = date;
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
}
