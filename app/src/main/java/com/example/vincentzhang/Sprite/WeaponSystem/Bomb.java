package com.example.vincentzhang.Sprite.WeaponSystem;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.example.vincentzhang.Sprite.AbstractSprite;
import com.example.vincentzhang.Sprite.CoordinateSystem;
import com.example.vincentzhang.Sprite.ImageManager;

/**
 * Created by VincentZhang on 4/24/2017.
 */

public class Bomb extends AbstractSprite {
    @Override
    public void draw(Canvas canvas) {
        Bitmap bm = ImageManager.inst().getImg(6);
        int posX = (int) (this.getSpritePos().getX() - CoordinateSystem.getViewPortPos().getX());
        int posY = (int) (this.getSpritePos().getY() - CoordinateSystem.getViewPortPos().getY());
        int imgWidth = bm.getWidth();
        int imgHeight = bm.getHeight();

        canvas.drawBitmap(bm, posX , posY, null );
    }

    @Override
    public void update() {

    }
}
