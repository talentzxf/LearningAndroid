package com.example.vincentzhang.Sprite;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by VincentZhang on 5/25/2017.
 */

public class ActorSprite extends ImageSprite {
    private float hp_max = 100;
    private float hp = 70;

    public ActorSprite(int imgId) {
        super(imgId);
    }

    @Override
    public Rect draw(Canvas canvas) {
        Rect retRect = super.draw(canvas);

        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(5.0f);
        paint.setStyle(Paint.Style.STROKE);
        Rect drawRect = this.getScrRect();
        canvas.drawRect(drawRect.left, drawRect.top, drawRect.left + drawRect.width(), drawRect.top + drawRect.height() * 0.07f, paint);

        Paint paint2 = new Paint();
        paint2.setColor(Color.RED);
        paint2.setStrokeWidth(1.0f);
        paint2.setStyle(Paint.Style.FILL);
        canvas.drawRect(drawRect.left, drawRect.top, drawRect.left + drawRect.width() * this.hp/this.hp_max, drawRect.top + drawRect.height() * 0.07f, paint2);

        return retRect;
    }
}
