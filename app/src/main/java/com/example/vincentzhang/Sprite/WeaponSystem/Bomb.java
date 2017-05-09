package com.example.vincentzhang.Sprite.WeaponSystem;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import com.example.vincentzhang.Sprite.AbstractSprite;

/**
 * Created by VincentZhang on 4/24/2017.
 */

public class Bomb extends AbstractSprite {
    private int step = 5;
    private long lastUpdateTime = -1;

    public Bomb() {
        // TODO: don't hard code here
        super(6);
    }

    @Override
    public void postUpdate() {
        if(lastUpdateTime == -1){
            lastUpdateTime = System.currentTimeMillis();
        }else{
            if(System.currentTimeMillis() - lastUpdateTime > 1000){
                step = (step-1 + 5)%5;
                lastUpdateTime = System.currentTimeMillis();
            }
        }
    }

    @Override
    public Rect draw(Canvas canvas){
        Rect impactedRect = super.draw(canvas);

        Paint textPaint = new Paint( Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize( 35);
        textPaint.setColor( Color.RED);

        canvas.drawText(String.valueOf(step), getScrRect().centerX(), getScrRect().centerY(), textPaint);

        // TODO: Consolidate all preUpdate postUpdate events
        postUpdate();

        return impactedRect;
    }

    @Override
    public void onCollide(AbstractSprite sprite){
        Log.i("Collide", "collide!!!!");
    }
}
