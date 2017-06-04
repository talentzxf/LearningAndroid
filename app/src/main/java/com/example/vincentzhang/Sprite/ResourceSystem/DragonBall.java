package com.example.vincentzhang.Sprite.ResourceSystem;

import android.graphics.Rect;

import com.example.vincentzhang.Sprite.Vector2D;

/**
 * Created by VincentZhang on 6/4/2017.
 */

public class DragonBall extends Resource {
    private int curFrame = 1;
    private int rowCount = 2;
    private int colCount = 2;

    private int spriteWidth = -1;
    private int spriteHeight = -1;

    public DragonBall(int imgId) {
        super(imgId);
        spriteWidth = getBm().getWidth() / colCount;
        spriteHeight = getBm().getHeight() / rowCount;
    }

    @Override
    public Rect getSrcRect() {
        Vector2D imgRowCol = this.getImgRowColumn();

        int row = (int) imgRowCol.getY();
        int col = (int) imgRowCol.getX();

        curFrame = (curFrame + 1) % (rowCount * colCount);

        return new Rect(col * spriteWidth, row * spriteHeight, (col + 1) * spriteWidth, (row + 1) * spriteHeight);
    }

    @Override
    public Vector2D getImgRowColumn() {
        int row = 0;
        if (curFrame >= colCount) {
            row = 1;
        }

        int col = curFrame % colCount;

        return new Vector2D(col, row);
    }
}
