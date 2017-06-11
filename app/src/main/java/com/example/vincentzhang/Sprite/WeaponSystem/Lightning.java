package com.example.vincentzhang.Sprite.WeaponSystem;


import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;

/**
 * Created by VincentZhang on 6/10/2017.
 */

public class Lightning extends Bullet {
    public Lightning() {
        super(17);
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

            mat.postRotate(30.0f);
            mat.postTranslate(getScrRect().left, getScrRect().top);
            canvas.drawBitmap(getBm().currentBitmap(), mat, null);
        }
        // getSpace4DTree().draw(canvas, getImgRowColumn(), 4, mScrRect);
        return srcRect;
    }

    @Override
    public void postUpdate() {
        super.postUpdate();
        getBm().advance();
    }
}
