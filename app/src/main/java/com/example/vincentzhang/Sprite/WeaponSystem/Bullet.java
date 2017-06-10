package com.example.vincentzhang.Sprite.WeaponSystem;

import com.example.vincentzhang.Sprite.SequentialAbstractCollidableSprite;

/**
 * Created by VincentZhang on 6/9/2017.
 */

public class Bullet extends SequentialAbstractCollidableSprite {

    private boolean inited = false;

    public Bullet(int imgId) {
        super(imgId);
    }

    public boolean isInited() {
        return inited;
    }

    public void setInited(boolean inited) {
        this.inited = inited;
    }
}
