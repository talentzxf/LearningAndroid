package com.example.vincentzhang.Sprite;

import android.content.res.Resources;
import android.graphics.Canvas;

/**
 * Created by VincentZhang on 5/20/2017.
 */

public interface SubSystem {
    boolean init(String level, Resources resources, Canvas canvas);

    void draw(Canvas canvas);

    void beforeCollision();

    AbstractSprite detectCollide(ImageSprite imgSprite);

    void preUpdate();
    void postUpdate();
}
