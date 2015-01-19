package com.com.teamupapps.searchsms.models;

import android.graphics.Bitmap;

/**
 * Created by clazell on 7/01/2015.
 */
public class MMS {


        private String id;
        private String name;
        private String content;
        private String date;
        private String imageURI;
        private String bitmapID;

        public MMS(String id, String name, String content, String date, String imageURI, String bitmap) {
            this.id = id;
            this.name = name;
            this.content = content;
            this.date = date;
            this.imageURI = imageURI;
            this.bitmapID = bitmap;
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

        public String getBitmap(){ return bitmapID;}
    }
