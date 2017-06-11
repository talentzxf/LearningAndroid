package com.example.vincentzhang.Sprite.WeaponSystem;


import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;

import com.example.vincentzhang.Sprite.Vector2D;

/**
 * Created by VincentZhang on 6/10/2017.
 */

public class Lightning extends Bullet {

    // The start and end position in Screen coordination.
    private Vector2D scrStart;
    private Vector2D scrCurrentEnd;

    private Vector2D scrTarget;

    private boolean isGoingForward = true;

    private float speed = 100.0f;

    public Lightning() {
        super(17);
    }

    public void setScrStart(Vector2D scrStart) {
        this.scrStart = scrStart;
        if (this.scrCurrentEnd == null)
            this.scrCurrentEnd = this.scrStart.clone();
    }

    public void setScrTarget(Vector2D scrTarget) {
        this.scrTarget = scrTarget;
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
            if (scrCurrentEnd != null && scrStart != null) {
                float deltaX = (float) (scrCurrentEnd.getX() - scrStart.getX());
                float deltaY = (float) (scrCurrentEnd.getY() - scrStart.getY());
                float angle = (float) (180.0 * Math.atan2(deltaY, deltaX) / Math.PI - 90);

                float height = getScrRect().height();
                float distToEnd = (float) scrStart.dist(scrCurrentEnd);
                float scale = distToEnd / height;

                mat.preRotate(angle);
                mat.postTranslate((float) this.scrStart.getX(), (float) this.scrStart.getY());

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

        if (scrCurrentEnd != null && scrTarget != null) {
            if (isGoingForward) {
                this.scrCurrentEnd = this.scrCurrentEnd.advance(this.scrTarget, speed);
                if (this.scrCurrentEnd.distSquare(this.scrTarget) <= speed * speed) {
                    this.scrCurrentEnd = this.scrTarget.clone();
                    isGoingForward = false;
                }
            } else {
                this.scrCurrentEnd = this.scrCurrentEnd.advance(this.scrStart, speed);
                if(scrCurrentEnd.distSquare(this.scrStart) <= speed*speed){
                    scrCurrentEnd = this.scrStart.clone();
                    isGoingForward = true;
                }
            }
        }
    }
}
