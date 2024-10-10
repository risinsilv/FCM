package com.example.fcm;

import android.graphics.Bitmap;
import android.net.Uri;

public class Image {
    private String imageKey;
    private Bitmap image;

    public String getImageKey() {
        return imageKey;
    }

    public void setImageKey(String imageKey) {
        this.imageKey = imageKey;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
