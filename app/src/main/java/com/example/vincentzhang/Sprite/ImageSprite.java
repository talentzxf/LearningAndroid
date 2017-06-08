package com.example.vincentzhang.Sprite;

import android.graphics.Bitmap;
import android.graphics.Rect;

import com.example.vincentzhang.Sprite.TerrainSystem.Building;
import com.example.vincentzhang.Sprite.WeaponSystem.Bomb;
import com.example.vincentzhang.Sprite.WeaponSystem.Explosion;
import com.example.vincentzhang.Sprite.imgemanagement.ImageManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by VincentZhang on 4/15/2017.
 */

public class ImageSprite extends HasLifeAbstractSprite {
    private Bitmap bm;
    private Map<DIRECTIONS, ArrayList<Rect>> dirSpriteMap = new HashMap<>();
    private Map<DIRECTIONS, ArrayList<Vector2D>> dirImgRowColumnMap = new HashMap<DIRECTIONS, ArrayList<Vector2D>>();
    private DIRECTIONS curDirection = DIRECTIONS.DOWN;

    private int curSpriteIndex = 0;
    private int spriteWidth = -1;
    private int spriteHeight = -1;
    private int rowNum = 1;
    private int colNum = 1;

    private boolean isMoving = false;

    public ImageSprite(int imgId) {
        super(imgId);
        rowNum = ImageManager.inst().getSpace4DTree(imgId).getRowCount();
        colNum = ImageManager.inst().getSpace4DTree(imgId).getColCount();

        bm = ImageManager.inst().getImg(imgId);
        splitImage();
        setResLoaded(true);
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

    void splitImage() {
        int imgWidth = bm.getWidth();
        int imgHeight = bm.getHeight();


        spriteWidth = imgWidth / colNum;
        spriteHeight = imgHeight / rowNum;

        for (int row = 0; row < rowNum; row++) {
            for (int col = 0; col < colNum; col++) {
                DIRECTIONS targetDir = ImageManager.inst().getDirectionArray(getImgId()).get(row);

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
        super.preUpdate();
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

    // TODO, use binary search to find the position faster.
    @Override
    protected void onCollide(AbstractCollidableSprite target) {
        super.onCollide(target);
        this.getController().onCollide(target);

        // Hard code, not good!
        if(target instanceof Building || target instanceof Explosion || target instanceof Bomb){
            Vector2D oldCenterPos = getOldCenterPos();
            Vector2D curCenterPos = getCurCenterPos();
//            if(oldCenterPos.getX() <= 0 || oldCenterPos.getY() <= 0 || oldCenterPos.equals(curCenterPos)){
//                Vector2D newPos = this.getSpritePos().applyDir(this.getCurDirection(), -1);
//                this.setSpritePos(newPos);
//                CollideDetector.setDirtyFlag(true);
//            } else {
//                // Center better be stable before & after collision.
//                // Vector2D newCenterPos = oldCenterPos.advance(curCenterPos, 1);
//
//                DIRECTIONS moveDir = Utilities.calculateDir(oldCenterPos, curCenterPos);
//                this.setSpritePos(this.getSpritePos().applyDir(moveDir, -1));
//
//                CollideDetector.setDirtyFlag(true);
//            }

            double half_dist = oldCenterPos.dist(curCenterPos)/2;

            Vector2D targetCenterPos = target.getCurCenterPos();
            // Move the sprite away
            DIRECTIONS moveDir = Utilities.calculateDir(targetCenterPos, curCenterPos);
            Vector2D newPos = this.getSpritePos().applyDir(moveDir, half_dist > 1? half_dist:1);
            this.setSpritePos(newPos);
            CollideDetector.setDirtyFlag(true);
        }
    }

    @Override
    public Vector2D getImgRowColumn() {
        ArrayList<Vector2D> rowColumnPosList = dirImgRowColumnMap.get(curDirection);
        Vector2D curRowColumnPos = rowColumnPosList.get(curSpriteIndex);
        return curRowColumnPos;
    }

    @Override
    public void postUpdate() {
        super.postUpdate();
        if(getController() != null)
            getController().update();
    }
}
