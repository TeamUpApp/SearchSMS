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
        private Bitmap bitmap;

        public MMS(String id, String name, String content, String date, String imageURI, Bitmap bitmap) {
            this.id = id;
            this.name = name;
            this.content = content;
            this.date = date;
            this.imageURI = imageURI;
            this.bitmap = bitmap;
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

        public Bitmap getBitmap(){ return bitmap;}
    }
