package org.impactready.protea_io;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.view.Display;
import android.view.WindowManager;


public class PictureServices {

    public static Bitmap setPicture(Context context, Uri imageLocation) {
        Bitmap bitmap;
        // Get the dimensions of the View
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int targetW = size.x - 5;
        int targetH = 600;

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imageLocation.toString(), bmOptions);

        int scaleFactor = Math.min(bmOptions.outWidth/targetW, bmOptions.outHeight/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = 4;

        bitmap = BitmapFactory.decodeFile(imageLocation.getPath(), bmOptions);
        return bitmap;
    }
}
