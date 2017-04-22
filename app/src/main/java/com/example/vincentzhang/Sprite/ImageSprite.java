package com.example.vincentzhang.Sprite;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by VincentZhang on 4/15/2017.
 */

public class ImageSprite extends AbstractSprite {
    private Bitmap bm;
    private Map<DIRECTIONS, ArrayList<Rect>> dirSpriteMap = new HashMap<DIRECTIONS, ArrayList<Rect>>();
    private DIRECTIONS curDirection = DIRECTIONS.DOWN;
    int rowCount = 8;
    int colCount = 4;

    private int curSpriteIndex = 0;
    private int spriteWidth = -1;
    private int spriteHeight = -1;

    private int spriteScrWidth = -1;
    private int spriteScrHeight = -1;

    private boolean isMoving = false;

    public boolean isMoving() {
        return isMoving;
    }

    public void setMoving(boolean moving) {
        isMoving = moving;
    }

    public void setCurDirection(DIRECTIONS curDirection) {
        this.curDirection = curDirection;
    }

    public DIRECTIONS getCurDirection() {
        return curDirection;
    }

    public void load(Bitmap bitmap) {
        bm = bitmap;
        setResLoaded(true);
        splitImage();
    }

    void splitImage() {
        int imgWidth = bm.getWidth();
        int imgHeight = bm.getHeight();


        spriteWidth = imgWidth / colCount;
        spriteHeight = imgHeight / rowCount;

        for (int row = 0; row < rowCount; row++) {
            for (int col = 0; col < colCount; col++) {
                DIRECTIONS targetDir = DIRECTIONS.fromDirNum(row);
                ArrayList<Rect> rectSequence = dirSpriteMap.get(targetDir);
                if (rectSequence == null) {
                    rectSequence = new ArrayList<Rect>();
                }
                rectSequence.add(new Rect(col * spriteWidth, row * spriteHeight, (col + 1) * spriteWidth, (row + 1) * spriteHeight));
                dirSpriteMap.put(targetDir, rectSequence);
            }
        }
    }

    public int getSpriteWidth(){
        return spriteScrWidth;
    }

    public int getSpriteHeight(){
        return spriteScrHeight;
    }

    @Override
    public void update() {
        if (this.isMoving) {
            ArrayList<Rect> spriteSequence = dirSpriteMap.get(curDirection);
            curSpriteIndex = (curSpriteIndex + 1) % spriteSequence.size();

            Vector2D newPos = this.getSpritePos().applyDir(this.getCurDirection(), this.getMoveSpeed());
            this.setSpritePos(newPos);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        spriteScrWidth = bm.getScaledWidth(canvas) / colCount;
        spriteScrHeight = bm.getScaledHeight(canvas) / rowCount;
        ArrayList<Rect> spriteSequence = dirSpriteMap.get(curDirection);
        Rect curRect = spriteSequence.get(curSpriteIndex);
        float ratio = this.spriteWidth / this.spriteHeight;

        int tileHeight = (int) CoordinateSystem.getTileDimension().getY();

        int real_scrWidth = (int) (ratio * tileHeight);
        Vector2D viewPortPos = CoordinateSystem.worldToScr(getSpritePos());
        int spriteViewPosX = (int) viewPortPos.getX();
        int spriteViewPosY = (int) viewPortPos.getY();

        canvas.drawBitmap(bm, curRect, new Rect(spriteViewPosX, spriteViewPosY, spriteViewPosX + real_scrWidth, spriteViewPosY + tileHeight), null);
    }
}
