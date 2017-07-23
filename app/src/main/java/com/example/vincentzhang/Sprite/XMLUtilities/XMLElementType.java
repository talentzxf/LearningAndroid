package com.example.vincentzhang.Sprite.XMLUtilities;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by VincentZhang on 7/23/2017.
 */

@Retention(RetentionPolicy.RUNTIME)
public @interface XMLElementType {
    Class value();
}
