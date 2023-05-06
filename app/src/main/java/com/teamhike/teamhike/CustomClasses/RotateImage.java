package com.teamhike.teamhike.CustomClasses;

import android.graphics.Bitmap;
import android.graphics.Matrix;

import androidx.exifinterface.media.ExifInterface;

import java.io.IOException;
import java.io.InputStream;

public class RotateImage {
    public Bitmap getRotatedImageBitmap(InputStream inputStream, Bitmap bitmap) {
        try {
            ExifInterface exif = new ExifInterface(inputStream);
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    bitmap = rotateImage(bitmap, (float) 90);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    bitmap = rotateImage(bitmap, (float) 180);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    bitmap = rotateImage(bitmap, (float) 270);
                    break;
                case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                    bitmap = flipHorizontalImage(bitmap);
                    break;
                case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                    bitmap = flipVerticalImage(bitmap);
                    break;
                default:
                    bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return bitmap;
    }

    private Bitmap rotateImage(Bitmap bitmap, Float degree) {
        Matrix matrix = new Matrix();
        matrix.preRotate(degree);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    private Bitmap flipVerticalImage(Bitmap bitmap) {
        Matrix matrix = new Matrix();
        matrix.preScale(1.0f, -1.0f);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    private Bitmap flipHorizontalImage(Bitmap bitmap) {
        Matrix matrix = new Matrix();
        matrix.preScale(-1.0f, 1.0f);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public interface toggleMainDrawer {
        public void setDrawerState(boolean enabled);
    }
}
