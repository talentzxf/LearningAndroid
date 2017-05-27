package com.example.vincentzhang.Sprite.Controller;

import com.example.vincentzhang.Sprite.AbstractSprite;

/**
 * Created by VincentZhang on 5/20/2017.
 */

public interface Controller {
    void update();
    void onCollide(AbstractSprite target);
}
