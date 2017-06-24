package com.example.vincentzhang.Sprite.WeaponSystem;


import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;

import com.example.vincentzhang.Sprite.ActorSprite;
import com.example.vincentzhang.Sprite.CoordinateSystem;
import com.example.vincentzhang.Sprite.HasLifeAbstractSprite;
import com.example.vincentzhang.Sprite.Vector2D;

/**
 * Created by VincentZhang on 6/10/2017.
 */

public class Lightning extends Bullet {

    // The start and end position in Screen coordination.
    private Vector2D startPoint;
    private Vector2D currentEnd;

    private HasLifeAbstractSprite target;

    private boolean isGoingForward = true;

    private boolean isAlive = true;

    private int damage =  10;
    private float speed = 100.0f;
    private ActorSprite owner;

    public Lightning(ActorSprite owner) {
        super(17);
        this.owner = owner;
    }

    public void setStart(Vector2D startPoint) {
        this.startPoint = startPoint;
        if (this.currentEnd == null)
            this.currentEnd = this.startPoint.clone();
    }

    public void setTarget(HasLifeAbstractSprite target) {
        this.target = target;
    }

    @Override
    public void preUpdate() {
        super.preUpdate();
    }

    @Override
    public Rect draw(Canvas canvas) {
        Rect srcRect = getSrcRect(); // Source rect
        Matrix mat = new Matrix();

        if (getScrRect() != null) {
            // Bitmap rotatedBitmap = Bitmap.createBitmap(getBm(), srcRect.left, srcRect.top, srcRect.width(), srcRect.height(), mat, true);
            if (currentEnd != null && startPoint != null) {

                Vector2D scrCurrentEnd = CoordinateSystem.worldToScr(currentEnd);
                Vector2D scrStart = CoordinateSystem.worldToScr(startPoint);
                float deltaX = (float) (scrCurrentEnd.getX() - scrStart.getX());
                float deltaY = (float) (scrCurrentEnd.getY() - scrStart.getY());
                float angle = (float) (180.0 * Math.atan2(deltaY, deltaX) / Math.PI - 90);

                float height = getScrRect().height();
                float distToEnd = (float) scrStart.dist(scrCurrentEnd);
                float scale = distToEnd / height;

                mat.preRotate(angle);
                mat.postTranslate((float) scrStart.getX(), (float) scrStart.getY());

                Matrix scaleMatrix = new Matrix();
                scaleMatrix.setScale(1.0f, scale);
                mat.preConcat(scaleMatrix);
                canvas.drawBitmap(getBm().currentBitmap(), mat, null);
            }
        }
        // getSpace4DTree().draw(canvas, getImgRowColumn(), 4, mScrRect);
        return srcRect;
    }

    @Override
    public void postUpdate() {
        super.postUpdate();
        getBm().advance();

        Vector2D scrTarget = target.getCurCenterPos();

        if (startPoint != null && scrTarget != null) {
            if (isGoingForward) {
                this.currentEnd = this.currentEnd.advance(scrTarget, speed);
                if (this.currentEnd.distSquare(scrTarget) <= speed * speed) {
                    this.currentEnd = scrTarget.clone();
                    isGoingForward = false;
                    target.reduceHP(this.damage, owner);
                }
            } else {
                this.currentEnd = this.currentEnd.advance(this.startPoint, speed);
                if(currentEnd.distSquare(this.startPoint) <= speed*speed){
                    currentEnd = this.startPoint.clone();
                    isGoingForward = true;

                    isAlive = false;
                }
            }
        }
    }

    public boolean isAlive() {
        return isAlive;
    }
}
