package com.example.vincentzhang.Sprite.WeaponSystem;

import android.graphics.Canvas;
import android.graphics.Rect;

import com.example.vincentzhang.Sprite.AbstractSprite;

/**
 * Created by VincentZhang on 5/17/2017.
 */

public class Explosion extends AbstractSprite {
    private int curFrame = 0;

    private int spriteWidth = -1;
    private int spriteHeight = -1;

    private long lastUpdateTime = System.currentTimeMillis();

    // TODO: Don't hard code here
    private int rowCount = 2;
    private int colCount = 8;

    public Explosion(int imgId) {
        super(imgId);
        spriteWidth = getBm().getWidth() / colCount;
        spriteHeight = getBm().getHeight() / rowCount;
    }

    @Override
    public Rect getSrcRect() {
        int row = 0;
        if(curFrame > 8){
            row = 1;
        }

        int col = curFrame % 8;

        if(System.currentTimeMillis() - lastUpdateTime > 100){
            curFrame = ( curFrame + 1 )% (rowCount * colCount);
            lastUpdateTime = System.currentTimeMillis();
        }

        return new Rect(col * spriteWidth , row * spriteHeight , (col+1) * spriteWidth, (row+1)*spriteHeight);
    }

    public boolean isAlive(){
        return curFrame != (rowCount * colCount - 1);
    }

    @Override
    public void postUpdate() {
        super.postUpdate();
    }

    @Override
    public Rect draw(Canvas canvas) {
        return super.draw(canvas);
    }
}
