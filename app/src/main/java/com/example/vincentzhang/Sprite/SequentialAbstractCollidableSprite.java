package com.example.vincentzhang.Sprite;

import android.graphics.Rect;

/**
 * Created by VincentZhang on 6/10/2017.
 */

public class SequentialAbstractCollidableSprite extends AbstractCollidableSprite {
    private int curFrame = 0;
    private boolean stopAtLast = false;
    private int rowCount = 1;
    private int colCount = 1;
    private long frameInterval = 100;
    private long lastUpdateTime = -1;
    private int spriteWidth = -1;
    private int spriteHeight = -1;


    public SequentialAbstractCollidableSprite(int imgId) {
        super(imgId);

        rowCount = getSpace4DTree().getRowCount();
        colCount = getSpace4DTree().getColCount();

        spriteWidth = getBm().getWidth() / colCount;
        spriteHeight = getBm().getHeight() / rowCount;

        lastUpdateTime = System.currentTimeMillis();
    }

    public boolean isStopAtLast() {
        return stopAtLast;
    }

    public void setStopAtLast(boolean stopAtLast) {
        this.stopAtLast = stopAtLast;
    }

    @Override
    public Rect getSrcRect() {
        Vector2D imgRowCol = this.getImgRowColumn();

        int row = (int) imgRowCol.getY();
        int col = (int) imgRowCol.getX();

        if(!stoppedAtLast()){
            if (System.currentTimeMillis() - lastUpdateTime > frameInterval) {
                curFrame = (curFrame + 1) % (rowCount * colCount);
                lastUpdateTime = System.currentTimeMillis();
            }
        }
        return new Rect(col * spriteWidth, row * spriteHeight, (col + 1) * spriteWidth, (row + 1) * spriteHeight);
    }

    public boolean stoppedAtLast(){
        if(!stopAtLast){
            return false;
        }

        if(curFrame == rowCount * colCount -1){
            return true;
        }
        return false;
    }

    @Override
    public Vector2D getImgRowColumn() {
        int row = curFrame / colCount;
        int col = curFrame % colCount;

        return new Vector2D(col, row);
    }
}
