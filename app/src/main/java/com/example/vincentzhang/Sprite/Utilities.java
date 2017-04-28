package com.example.vincentzhang.Sprite;

import android.content.res.Resources;
import android.graphics.Rect;

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


    public static InputSource getXmlSource(Resources resources, String level){
        return new InputSource(resources.openRawResource(getId(level, R.raw.class)));
    }

    public static boolean detectCollide(Rect rect1,Rect rect2){
        int minx = Math.max(rect1.left, rect2.left);
        int miny = Math.max(rect1.top, rect2.top);
        int maxx = Math.min(rect1.right, rect2.right);
        int maxy = Math.min(rect1.bottom, rect2.bottom);

        if(minx > maxx || miny > maxy){
            return false;
        }
        return true;
    }
}
