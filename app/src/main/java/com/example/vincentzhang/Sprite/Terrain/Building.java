package com.example.vincentzhang.Sprite.Terrain;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.example.vincentzhang.Sprite.AbstractSprite;
import com.example.vincentzhang.Sprite.CoordinateSystem;
import com.example.vincentzhang.Sprite.ImageManager;
import com.example.vincentzhang.Sprite.Vector2D;

import static com.example.vincentzhang.Sprite.CoordinateSystem.getViewPortPos;

/**
 * Created by VincentZhang on 4/23/2017.
 */
public class Building extends AbstractSprite{
    private int imgId = -1;
    public Building(int id, int gridX, int gridY){
        imgId = id;
        this.setSpritePos(CoordinateSystem.gridToWorld(new Vector2D(gridX, gridY)));
    }

    @Override
    public void draw(Canvas canvas) {
        Bitmap img = ImageManager.inst().getImg(imgId);

        int left = (int) this.getSpritePos().getX();
        int top = (int) this.getSpritePos().getY();

        int scr_left = (int) (left - getViewPortPos().getX());
        int scr_top = (int) (top - getViewPortPos().getY());

        float ratio = (float)img.getHeight() / (float)img.getWidth();
        int img_scr_width = Math.min((int) CoordinateSystem.getTileDimension().getX(), img.getScaledWidth(canvas));
        int img_scr_height = (int) (img_scr_width * ratio);

        canvas.drawBitmap(img, new Rect(0,0,img.getWidth(),img.getHeight()) , new Rect(scr_left,scr_top,img_scr_width + scr_left,scr_top + img_scr_height), null);
    }

    @Override
    public void update() {

    }
}
