package com.example.vincentzhang.Sprite;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Rect;
import android.support.v4.app.ActivityCompat;

import com.example.vincentzhang.learnandroid.R;

import org.xml.sax.InputSource;

import java.lang.reflect.Field;

/**
 * Created by VincentZhang on 4/23/2017.
 */

public class Utilities {
    public static int getId(String resourceName, Class<?> c) {
        try {
            Field idField = c.getDeclaredField(resourceName);
            return idField.getInt(idField);
        } catch (Exception e) {
            throw new RuntimeException("No resource ID found for: "
                    + resourceName + " / " + c, e);
        }
    }


    public static InputSource getXmlSource(Resources resources, String level) {
        return new InputSource(resources.openRawResource(getId(level, R.raw.class)));
    }

    public static boolean detectCollide(Rect rect1, Rect rect2) {
        int minx = Math.max(rect1.left, rect2.left);
        int miny = Math.max(rect1.top, rect2.top);
        int maxx = Math.min(rect1.right, rect2.right);
        int maxy = Math.min(rect1.bottom, rect2.bottom);

        if (minx > maxx || miny > maxy) {
            return false;
        }
        return true;
    }

    public static Rect mapRect(Rect parentRect, Rect curRect, Rect targetParentRect) {
        float widthRatio = (float) targetParentRect.width() / (float) parentRect.width();
        float heightRatio = (float) targetParentRect.height() / (float) parentRect.height();

        // Result target width and height
        int width = (int) (widthRatio * (float) curRect.width());
        int height = (int) (heightRatio * (float) curRect.height());

        int curLeft = (int) (targetParentRect.left + (widthRatio * (curRect.left - parentRect.left)));
        int curTop = (int) (targetParentRect.top + (widthRatio * (curRect.top - parentRect.top)));

        return new Rect(curLeft, curTop, curLeft + width, curTop + height);
    }

    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    /**
     * Checks if the app has permission to write to device storage
     * <p>
     * If the app does not has permission then the user will be prompted to
     * grant permissions
     *
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }
    }
}
