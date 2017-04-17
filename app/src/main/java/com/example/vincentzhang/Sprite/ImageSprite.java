package com.example.vincentzhang.Sprite;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by VincentZhang on 4/15/2017.
 */

enum DIRECTION {
    UNKNOWN,
    UP,
    DOWN,
    LEFT,
    RIGHT,
    UPLEFT,
    UPRIGHT,
    DOWNLEFT,
    DOWNRIGHT
}

public class ImageSprite implements AbstractSprite {
    private boolean resLoaded = false;
    private Bitmap bm;
    private Map<DIRECTION, ArrayList<Rect>> dirSpriteMap = new HashMap<DIRECTION, ArrayList<Rect>>();
    private DIRECTION curDirection = DIRECTION.DOWNLEFT;
    private int curSpriteIndex = 0;
    private int spriteWidth = -1;
    private int spriteHeight = -1;

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
                DIRECTION targetDir = DIRECTION.UNKNOWN;
                switch (row) {
                    case 0:
                        targetDir = DIRECTION.DOWN;
                        break;
                    case 1:
                        targetDir = DIRECTION.RIGHT;
                        break;
                    case 2:
                        targetDir = DIRECTION.UP;
                        break;
                    case 3:
                        targetDir = DIRECTION.LEFT;
                        break;
                    case 4:
                        targetDir = DIRECTION.DOWNLEFT;
                        break;
                    case 5:
                        targetDir = DIRECTION.DOWNRIGHT;
                        break;
                    case 6:
                        targetDir = DIRECTION.UPLEFT;
                        break;
                    case 7:
                        targetDir = DIRECTION.UPRIGHT;
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

        canvas.drawColor(Color.BLACK);
        canvas.drawBitmap(bm, curRect, new Rect(0, 0, spriteWidth, spriteHeight), null);
    }
}
