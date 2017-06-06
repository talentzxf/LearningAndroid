package com.example.vincentzhang.Sprite.ResourceSystem;

import android.util.Log;

import com.example.vincentzhang.Sprite.AbstractCollidableSprite;
import com.example.vincentzhang.Sprite.CoordinateSystem;
import com.example.vincentzhang.Sprite.Vector2D;

/**
 * Created by VincentZhang on 6/4/2017.
 */

public class Resource extends AbstractCollidableSprite {
    private boolean used = false;
    private boolean flying = false;

    private float flySpeed = 100.f;

    public Resource(int imgId) {
        super(imgId);
    }

    @Override
    protected void onCollide(AbstractCollidableSprite target) {
        if(!flying){
            super.onCollide(target);
            Log.i("Collide coin!!!!", " Collide Coin!!");
            used = false;
            flying = true;
        }
    }

    @Override
    public void postUpdate() {
        super.postUpdate();
        if(flying){
            // Fly to up right corner
            int scrWidth = (int) CoordinateSystem.getScrDimension().getX();
            Vector2D targetScrPos = new Vector2D(scrWidth - 100, 0);

            Vector2D curScrPos = CoordinateSystem.worldToScr(this.getSpritePos());

            Vector2D nextScrPos = curScrPos.advance(targetScrPos, this.flySpeed);

            this.setSpritePos(CoordinateSystem.scrToWorld(nextScrPos));

            if(nextScrPos.distSquare(targetScrPos) <= flySpeed * flySpeed){
                used = true;
                flying = false;
            }
        }
    }

    public boolean isUsed() {
        return used;
    }
}
