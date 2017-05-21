package com.example.vincentzhang.Sprite;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

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
    private int imgId = -1;

    public AbstractSprite(int imgId){
        bm = ImageManager.inst().getImg(imgId);
        space4DTree = ImageManager.inst().getSpace4DTree(imgId);
        this.imgId = imgId;
    }

    public Space4DTree getSpace4DTree(){
        return space4DTree;
    }

    protected Bitmap getBm(){
        return bm;
    }

    public Rect getScrRect(){
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
        return new Rect(0,0, bm.getWidth() - 1, bm.getHeight() - 1);
    }

    public void preUpdate(){}

    public Vector2D getImgRowColumn(){return new Vector2D(0,0);}

    public Rect draw(Canvas canvas){
        Rect srcRect = getSrcRect(); // Source rect
        if(mScrRect != null)
            canvas.drawBitmap(bm, srcRect, mScrRect, null);
        // getSpace4DTree().draw(canvas, getImgRowColumn(), 4, mScrRect);
        return mScrRect;
    }

    public void beforeCollision(){
        Rect srcRect = getSrcRect(); // Source rect
        float ratio = (float)srcRect.width()/(float)srcRect.height();
        int tileHeight = (int) CoordinateSystem.getTileDimension().getY();

        int real_scrWidth = (int) (ratio * tileHeight);
        Vector2D viewPortPos = CoordinateSystem.worldToScr(getSpritePos());
        int spriteViewPosX = (int) viewPortPos.getX();
        int spriteViewPosY = (int) viewPortPos.getY();

        mScrRect = new Rect(spriteViewPosX, spriteViewPosY, spriteViewPosX + real_scrWidth, spriteViewPosY + tileHeight);
    }

    public void postUpdate(){

    }

    protected void onCollide(AbstractSprite target){
        Log.i("Collide!!!!", " really??");
    }

    public boolean detectCollide(AbstractSprite target){
        if(target.mScrRect == null || this.mScrRect == null)
            return false;

        // If two rectangles collide
        if(!Utilities.detectCollide(target.mScrRect, this.mScrRect)){
            return false;
        }

        CollideDetector collideDetector = new CollideDetector(this, target);
        if(!collideDetector.detect()){
            return false;
        }

        target.onCollide(this);
        this.onCollide(target);
        return true;
    }
}
