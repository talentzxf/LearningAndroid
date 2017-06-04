package com.example.vincentzhang.Sprite;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.example.vincentzhang.Sprite.imgemanagement.ImageManager;

/**
 * Created by VincentZhang on 4/15/2017.
 */

public abstract class AbstractSprite {
    private Vector2D spritePos = new Vector2D();
    private float moveSpeed = 20;
    private Bitmap bm;
    private Rect mScrRect;

    private int imgId = -1;
    private float sizeScale = 1.0f;

    public AbstractSprite(int imgId) {
        if(imgId == -1)
            return;
        bm = ImageManager.inst().getImg(imgId);

        this.imgId = imgId;
        this.sizeScale = ImageManager.inst().getSizeScale(imgId);
    }

    public int getImgId() {
        return imgId;
    }

    public float getSizeScale() {
        return sizeScale;
    }

    public void setSizeScale(float sizeScale) {
        this.sizeScale = sizeScale;
    }

    protected Bitmap getBm() {
        return bm;
    }

    public Rect getScrRect() {
        return mScrRect;
    }

    public void beforeCollision() {
        Rect srcRect = getSrcRect(); // Source rect
        if(srcRect == null)
            return;
        float ratio = (float)srcRect.width()/(float)srcRect.height();
        int tileHeight = (int) (CoordinateSystem.getTileDimension().getY() * getSizeScale());

        int real_scrWidth = (int) (ratio * tileHeight);
        Vector2D viewPortPos = CoordinateSystem.worldToScr(getSpritePos());
        int spriteViewPosX = (int) viewPortPos.getX();
        int spriteViewPosY = (int) viewPortPos.getY();

        mScrRect = new Rect(spriteViewPosX, spriteViewPosY, spriteViewPosX + real_scrWidth, spriteViewPosY + tileHeight);
    }

    public float getMoveSpeed() {
        return moveSpeed;
    }

    public void setMoveSpeed(float moveSpeed) {
        this.moveSpeed = moveSpeed;
    }

    public Vector2D getSpritePos() {
        return spritePos;
    }

    public void setSpritePos(Vector2D spritePos) {
        this.spritePos = spritePos;
    }

    public void setResLoaded(boolean resLoaded) {
        this.resLoaded = resLoaded;
    }

    public boolean isResLoaded() {
        return resLoaded;
    }

    private boolean resLoaded = false;

    public Rect getSrcRect() {
        return new Rect(0, 0, bm.getWidth() - 1, bm.getHeight() - 1);
    }

    public void preUpdate(){

    }

    public Vector2D getImgRowColumn() {
        return new Vector2D(0, 0);
    }

    public Rect draw(Canvas canvas) {
        Rect srcRect = getSrcRect(); // Source rect
        if (mScrRect != null)
            canvas.drawBitmap(bm, srcRect, mScrRect, null);
        // getSpace4DTree().draw(canvas, getImgRowColumn(), 4, mScrRect);
        return mScrRect;
    }

    public void postUpdate() {

    }


}
