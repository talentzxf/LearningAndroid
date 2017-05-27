package com.example.vincentzhang.Sprite.WeaponSystem;

import android.graphics.Canvas;
import android.graphics.Rect;

import com.example.vincentzhang.Sprite.AbstractSprite;
import com.example.vincentzhang.Sprite.ActorSprite;

/**
 * Created by VincentZhang on 5/17/2017.
 */

public class Explosion extends AbstractSprite {
    private int curFrame = 0;

    private int spriteWidth = -1;
    private int spriteHeight = -1;

    private long lastUpdateTime = -1;

    // TODO: Don't hard code here
    private int rowCount = 2;
    private int colCount = 8;
    private long explodeTime = 0;

    private ActorSprite owner;

    public ActorSprite getOwner() {
        return owner;
    }

    public void setOwner(ActorSprite owner) {
        this.owner = owner;
    }

    public Explosion(int imgId) {
        super(imgId);
        spriteWidth = getBm().getWidth() / colCount;
        spriteHeight = getBm().getHeight() / rowCount;
        explodeTime = System.currentTimeMillis();
        lastUpdateTime = explodeTime;
    }

    public Explosion(int imgId, int delayMilliSecs) {
        this(imgId);
        this.explodeTime = System.currentTimeMillis() + delayMilliSecs;
        lastUpdateTime = explodeTime;
    }

    @Override
    public Rect getSrcRect() {
        if (System.currentTimeMillis() > this.explodeTime) {
            int row = 0;
            if (curFrame > 8) {
                row = 1;
            }

            int col = curFrame % 8;

            if (System.currentTimeMillis() - lastUpdateTime > 100) {
                curFrame = (curFrame + 1) % (rowCount * colCount);
                lastUpdateTime = System.currentTimeMillis();
            }

            return new Rect(col * spriteWidth, row * spriteHeight, (col + 1) * spriteWidth, (row + 1) * spriteHeight);
        }
        return null;
    }

    public boolean isAlive() {
        return curFrame != (rowCount * colCount - 1);
    }

    @Override
    public void postUpdate() {
        if (System.currentTimeMillis() > this.explodeTime) {
            super.postUpdate();
        }
    }

    @Override
    protected void onCollide(AbstractSprite target) {
        if (System.currentTimeMillis() > this.explodeTime) {
            super.onCollide(target);
            if (target instanceof ActorSprite) {
                ActorSprite actorSprite = (ActorSprite) target;
                actorSprite.reduceHP(1, this.owner);
            }
        }
    }

    @Override
    public Rect draw(Canvas canvas) {
        if (System.currentTimeMillis() > this.explodeTime) {
            super.draw(canvas);
        }
        return null;
    }
}
