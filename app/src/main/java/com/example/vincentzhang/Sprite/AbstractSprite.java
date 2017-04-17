package com.example.vincentzhang.Sprite;

import android.graphics.Canvas;

/**
 * Created by VincentZhang on 4/15/2017.
 */

public interface AbstractSprite {
    boolean loaded();
    void draw(Canvas canvas);
    void update();
}
