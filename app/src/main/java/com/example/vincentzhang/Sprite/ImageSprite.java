package com.example.vincentzhang.Sprite;

import android.graphics.Bitmap;
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
    private Map<DIRECTIONS, ArrayList<Vector2D>> dirImgRowColumnMap = new HashMap<DIRECTIONS, ArrayList<Vector2D>>();
    private DIRECTIONS curDirection = DIRECTIONS.DOWN;

    // TODO: read this from XML config
    int rowCount = 8;
    int colCount = 4;

    private int curSpriteIndex = 0;
    private int spriteWidth = -1;
    private int spriteHeight = -1;

    private boolean isMoving = false;

    public ImageSprite(int imgId) {
        super(imgId);

        this.setSpritePos(new Vector2D(200,200));
    }

    public boolean isMoving() {
        return isMoving;
    }

    public void setMoving(boolean moving) {
        isMoving = moving;
    }

    public void setCurDirection(DIRECTIONS curDirection) {
        if(this.curDirection != curDirection){
            CollideDetector.setDirtyFlag(true);
        }
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

                ArrayList<Vector2D> rowColumnSequence = dirImgRowColumnMap.get(targetDir);
                if(rowColumnSequence == null){
                    rowColumnSequence = new ArrayList<Vector2D>();
                }
                rowColumnSequence.add(new Vector2D(col,row));
                dirImgRowColumnMap.put(targetDir, rowColumnSequence);
            }
        }
    }

    // TODO: Performance optimization, find the feasible position through binary search.
    @Override
    public void preUpdate(){
        if(this.isMoving){
            ArrayList<Rect> spriteSequence = dirSpriteMap.get(curDirection);
            curSpriteIndex = (curSpriteIndex + 1) % spriteSequence.size();

            Vector2D newPos = this.getSpritePos().applyDir(this.getCurDirection(), this.getMoveSpeed());
            this.setSpritePos(newPos);
            CollideDetector.setDirtyFlag(true);
        }
    }

    @Override
    public Rect getSrcRect() {
        ArrayList<Rect> spriteSequence = dirSpriteMap.get(curDirection);
        Rect curRect = spriteSequence.get(curSpriteIndex);
        return curRect;
    }

    @Override
    protected void onCollide(AbstractSprite target) {
        super.onCollide(target);
        Vector2D newPos = this.getSpritePos().applyDir(this.getCurDirection(), -1);
        this.setSpritePos(newPos);
        CollideDetector.setDirtyFlag(true);
    }

    @Override
    public Vector2D getImgRowColumn() {
        ArrayList<Vector2D> rowColumnPosList = dirImgRowColumnMap.get(curDirection);
        Vector2D curRowColumnPos = rowColumnPosList.get(curSpriteIndex);
        return curRowColumnPos;
    }
}
