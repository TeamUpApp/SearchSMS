package com.com.teamupapps.searchsms.models;

/**
 * Created by clazell on 8/01/2015.
 */
public class Contact {
    private String name;
    private String imageURI;

    public Contact(String name, String imageURI) {

        this.name = name;

        this.imageURI = imageURI;
    }

    public String getImageURI() {
        return imageURI;
    }

    public String getName() {
        return name;
    }

}
