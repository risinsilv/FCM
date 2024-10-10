package com.example.fcm;

import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;

public class ImageStorage {

    private static ImageStorage instance;
    private HashMap<String, Bitmap> imageMap;

    private ImageStorage() {
        imageMap = new HashMap<>(); // Initialize the HashMap here
    }

    public static ImageStorage getInstance() {
        if (instance == null) {
            instance = new ImageStorage();

        }
        return instance;
    }
    public HashMap<String, Bitmap> getImageMap() {
        return imageMap;
    }

    // Method to add an image to the map
    public void addImage(String key, Bitmap bitmap) {
        imageMap.put(key, bitmap);
    }

    public Bitmap getImage(String key) {
        return imageMap.get(key);
    }

    public void setImageMap(HashMap<String, Bitmap> imageMap) {
        this.imageMap = imageMap;
    }
}
