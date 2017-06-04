package com.example.vincentzhang.Sprite.WeaponSystem;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.example.vincentzhang.Sprite.AbstractCollidableSprite;
import com.example.vincentzhang.Sprite.ActorSprite;

/**
 * Created by VincentZhang on 4/24/2017.
 */

public class Bomb extends AbstractCollidableSprite {
    private int step = 5;
    private long lastUpdateTime = -1;

    private boolean exploded = false;
    private ActorSprite owner;

    public Bomb() {
        // TODO: don't hard code here
        super(6);
    }

    public boolean isExploded() {
        return exploded;
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

        if(step == 0){
            exploded = true;
        }
    }

    @Override
    public Rect draw(Canvas canvas){
        Rect impactedRect = super.draw(canvas);

        Paint textPaint = new Paint( Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize( 35);
        textPaint.setColor( Color.RED);

        if(impactedRect != null && getScrRect() != null)
            canvas.drawText(String.valueOf(step), getScrRect().centerX(), getScrRect().centerY(), textPaint);
        return impactedRect;
    }

    @Override
    public void onCollide(AbstractCollidableSprite sprite){
        // Log.i("Collide", "collide!!!!");
    }

    public void setOwner(ActorSprite owner) {
        this.owner = owner;
    }

    public ActorSprite getOwner(){
        return this.owner;
    }
}
