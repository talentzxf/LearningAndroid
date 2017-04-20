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

public class ImageSprite implements AbstractSprite {

    private boolean resLoaded = false;
    private Bitmap bm;
    private Map<DIRECTIONS, ArrayList<Rect>> dirSpriteMap = new HashMap<DIRECTIONS, ArrayList<Rect>>();
    private DIRECTIONS curDirection = DIRECTIONS.DOWNLEFT;
    private int curSpriteIndex = 0;
    private int spriteWidth = -1;
    private int spriteHeight = -1;

    private int scrWidth = -1;
    private int scrHeight = -1;

    public void setCurDirection(DIRECTIONS curDirection) {
        this.curDirection = curDirection;
    }

    public DIRECTIONS getCurDirection(){
        return curDirection;
    }

    public void load(Bitmap bitmap) {
        bm = bitmap;
        resLoaded = true;

        splitImage();
    }

    void splitImage() {
        int imgWidth = bm.getWidth();
        int imgHeight = bm.getHeight();
        int rowCount = 8;
        int colCount = 4;

        spriteWidth = imgWidth / colCount;
        spriteHeight = imgHeight / rowCount;

        for (int row = 0; row < rowCount; row++) {
            for (int col = 0; col < colCount; col++) {
                DIRECTIONS targetDir = DIRECTIONS.UNKNOWN;
                switch (row) {
                    case 0:
                        targetDir = DIRECTIONS.DOWN;
                        break;
                    case 1:
                        targetDir = DIRECTIONS.RIGHT;
                        break;
                    case 2:
                        targetDir = DIRECTIONS.UP;
                        break;
                    case 3:
                        targetDir = DIRECTIONS.LEFT;
                        break;
                    case 4:
                        targetDir = DIRECTIONS.DOWNLEFT;
                        break;
                    case 5:
                        targetDir = DIRECTIONS.DOWNRIGHT;
                        break;
                    case 6:
                        targetDir = DIRECTIONS.UPLEFT;
                        break;
                    case 7:
                        targetDir = DIRECTIONS.UPRIGHT;
                        break;
                }

                ArrayList<Rect> rectSequence = dirSpriteMap.get(targetDir);
                if (rectSequence == null) {
                    rectSequence = new ArrayList<Rect>();
                }
                rectSequence.add(new Rect(col * spriteWidth, row * spriteHeight, (col + 1) * spriteWidth, (row + 1) * spriteHeight));
                dirSpriteMap.put(targetDir, rectSequence);
            }
        }


    }

    @Override
    public void setSpriteDim(int width, int height) {
        scrWidth = width;
        scrHeight = height;
    }

    @Override
    public boolean loaded() {
        return resLoaded;
    }

    @Override
    public void update() {
        ArrayList<Rect> spriteSequence = dirSpriteMap.get(curDirection);
        curSpriteIndex = (curSpriteIndex + 1) % spriteSequence.size();
    }

    @Override
    public void draw(Canvas canvas) {
        ArrayList<Rect> spriteSequence = dirSpriteMap.get(curDirection);
        Rect curRect = spriteSequence.get(curSpriteIndex);
        float ratio = this.spriteWidth/this.spriteHeight;
        int real_scrWidth = (int) (ratio * scrHeight);

        canvas.drawBitmap(bm, curRect, new Rect(0, 0, real_scrWidth, scrHeight), null);
    }
}
