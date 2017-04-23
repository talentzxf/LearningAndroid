package com.example.vincentzhang.Sprite;

import android.content.res.Resources;

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
}
