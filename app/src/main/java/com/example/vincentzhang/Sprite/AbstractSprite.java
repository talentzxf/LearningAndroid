package com.example.vincentzhang.Sprite;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.example.vincentzhang.Sprite.imgemanagement.ImageManager;
import com.example.vincentzhang.Sprite.imgemanagement.Space4DTree;

/**
 * Created by VincentZhang on 4/15/2017.
 */

public abstract class AbstractSprite {
    private Vector2D spritePos = new Vector2D();
    private float moveSpeed = 20;
    private Bitmap bm;
    private Rect mScrRect;
    private Space4DTree space4DTree;


    public AbstractSprite(int imgId){
        bm = ImageManager.inst().getImg(imgId);
        space4DTree = ImageManager.inst().getSpace4DTree(imgId);
    }

    protected Bitmap getBm(){
        return bm;
    }

    protected Rect getScrRect(){
        return mScrRect;
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

    public Rect getSrcRect(){
        return new Rect(0,0, bm.getWidth(), bm.getHeight());
    }

    public void draw(Canvas canvas){
        Rect srcRect = getSrcRect(); // Source rect

        float ratio = (float)srcRect.width()/(float)srcRect.height();
        int tileHeight = (int) CoordinateSystem.getTileDimension().getY();

        int real_scrWidth = (int) (ratio * tileHeight);
        Vector2D viewPortPos = CoordinateSystem.worldToScr(getSpritePos());
        int spriteViewPosX = (int) viewPortPos.getX();
        int spriteViewPosY = (int) viewPortPos.getY();
        float spriteScrWidth = real_scrWidth;
        float spriteScrHeight = tileHeight;

        mScrRect = new Rect(spriteViewPosX, spriteViewPosY, spriteViewPosX + real_scrWidth, spriteViewPosY + tileHeight);
        canvas.drawBitmap(bm, srcRect, mScrRect, null);
        space4DTree.draw(canvas, 3, mScrRect);
    }

    public void preUpdate(){

    }

    public void postUpdate(){

    }

    protected void onCollide(AbstractSprite target){

    }

    public boolean detectCollide(AbstractSprite target){
        // If two rectangles collide
        if(!Utilities.detectCollide(target.mScrRect, this.mScrRect)){
            return false;
        }

        target.onCollide(this);
        this.onCollide(target);
        return true;
    }
}
