package com.example.vincentzhang.Sprite.WeaponSystem;

import android.graphics.Canvas;
import android.graphics.Rect;

import com.example.vincentzhang.Sprite.AbstractCollidableSprite;
import com.example.vincentzhang.Sprite.ActorSprite;
import com.example.vincentzhang.Sprite.DIRECTIONS;
import com.example.vincentzhang.Sprite.HasLifeAbstractSprite;
import com.example.vincentzhang.Sprite.SequentialAbstractCollidableSprite;
import com.example.vincentzhang.Sprite.Vector2D;

/**
 * Created by VincentZhang on 5/17/2017.
 */

public class Explosion extends SequentialAbstractCollidableSprite {
    // TODO: Don't hard code here
    private long explodeTime = 0;

    private ActorSprite owner;

    private DIRECTIONS explodeDir;

    public ActorSprite getOwner() {
        return owner;
    }

    public void setOwner(ActorSprite owner) {
        this.owner = owner;
    }

    public Explosion(int imgId, DIRECTIONS explodeDir) {
        super(imgId);

        explodeTime = System.currentTimeMillis();
        this.explodeDir = explodeDir;
        this.setStopAtLast(true);
    }

    public Explosion(int imgId, DIRECTIONS explodeDir, int delayMilliSecs) {
        this(imgId, explodeDir);
        this.explodeTime = System.currentTimeMillis() + delayMilliSecs;
    }

    public boolean isAlive() {
        return !stoppedAtLast();
    }

    @Override
    public void postUpdate() {
        if (System.currentTimeMillis() > this.explodeTime) {
            super.postUpdate();
        }
    }

    @Override
    public Rect getSrcRect() {
        if(System.currentTimeMillis() > this.explodeTime)
            return super.getSrcRect();
        return null;
    }

    @Override
    protected void onCollide(AbstractCollidableSprite target) {
        if (System.currentTimeMillis() > this.explodeTime) {
            super.onCollide(target);
            if (target instanceof HasLifeAbstractSprite) {
                HasLifeAbstractSprite targetSprite = (HasLifeAbstractSprite) target;
                targetSprite.reduceHP(10, this.owner);

                if(target instanceof ActorSprite){
                    // Shock the sprite
                    Vector2D explosionCenter = this.getCurCenterPos();
                    Vector2D spriteCenter = target.getCurCenterPos();

                    double dist = explosionCenter.dist(spriteCenter);

                    // Suck in
                    // target.setSpriteCenterPos(spriteCenter.advance(explosionCenter, (float) dist));

                    // Blow off
                    float explodeDist = (float) (-0.5*dist);
                    target.setSpriteCenterPos(spriteCenter.advance(explosionCenter, explodeDist));
                }
            }
        }
    }

    @Override
    public Rect draw(Canvas canvas) {
        if (System.currentTimeMillis() > this.explodeTime) {
            super.draw(canvas);
        }
        return null;
    }
}
