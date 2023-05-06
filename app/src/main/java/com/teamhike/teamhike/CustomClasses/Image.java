package com.teamhike.teamhike.CustomClasses;

import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

public class Image {
    public String createImageName() {
        int min = 1000, max = 9999;
        double random_number = Math.random() * (max - min + 1) + min;
//        String imagePath = uri.getLastPathSegment();
//        int cutPoint;
//        String imageName = null;
//        if (imagePath != null) {
//            cutPoint = imagePath.lastIndexOf("/");
//            if (cutPoint != -1) imageName = imagePath.substring(cutPoint + 1);
//        }
        return (int) random_number + ".png";
    }

    public String getEncodedImage(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] imageByteArr = stream.toByteArray();
        return Base64.encodeToString(imageByteArr, Base64.DEFAULT);
    }
}
