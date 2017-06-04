package com.example.vincentzhang.Sprite.ParticleSystem;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.example.vincentzhang.Sprite.AbstractSprite;
import com.example.vincentzhang.Sprite.Vector2D;

import java.util.Random;

/**
 * Created by VincentZhang on 6/4/2017.
 */

public class Particle extends AbstractSprite {
    private long lastUpdateTime = -1;
    private long createTime = -1;
    private int color;

    // life in milliseconds
    private int life = 3000;

    private Vector2D force = new Vector2D();
    private Vector2D velocity = new Vector2D();

    private float mass = 1.0f;

    public Particle(int imgId) {
        super(imgId);
        Random r = new Random();
        color = Color.argb(255, r.nextInt(255), r.nextInt(255), r.nextInt(255) );
        createTime = System.currentTimeMillis();
    }

    public Vector2D getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector2D velocity) {
        this.velocity = velocity;
    }

    @Override
    public Rect draw(Canvas canvas) {
        // return super.draw(canvas);
        Paint p = new Paint();
        p.setTextSize(50);
        p.setColor(this.color);

        canvas.drawText("+", (float) this.getSpritePos().getX(), (float) getSpritePos().getY(), p);
        return null;
    }

    @Override
    public void preUpdate() {
        if (lastUpdateTime == -1) {
            lastUpdateTime = System.currentTimeMillis();
            return;
        }

        long elpsedTime = System.currentTimeMillis() - lastUpdateTime;
        // Update pos
        Vector2D curPos = this.getSpritePos();
        Vector2D deltaPos = velocity.multiply((float)elpsedTime/1000.f);
        Vector2D newPos = curPos.add(deltaPos);

        // a = f/m
        // v = v0+at ==>  v = v0+t*f/m
        Vector2D deltaVec = force.multiply(1.0f / mass).multiply(elpsedTime);
        velocity = velocity.add(deltaVec);

        this.setSpritePos(newPos);

        lastUpdateTime = System.currentTimeMillis();
    }

    public boolean isLive(){
        return (System.currentTimeMillis() - createTime) < this.life;
    }
}
