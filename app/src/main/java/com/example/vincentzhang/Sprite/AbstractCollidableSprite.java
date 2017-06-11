package com.example.vincentzhang.Sprite;

/**
 * Created by VincentZhang on 6/4/2017.
 */

public abstract class AbstractCollidableSprite extends AbstractSprite {

    public AbstractCollidableSprite(int imgId) {
        super(imgId);
    }

    protected void onCollide(AbstractCollidableSprite target) {
        // Log.i("Collide!!!!", " really??");
    }

    public boolean detectCollide(AbstractCollidableSprite target) {
        if (target.getScrRect() == null || this.getScrRect() == null)
            return false;

        // If two rectangles collide
        if (!Utilities.detectCollide(target.getScrRect(), this.getScrRect())) {
            return false;
        }

        CollideDetector collideDetector = new CollideDetector(this, target);
        if (!collideDetector.detect()) {
            return false;
        }

        target.onCollide(this);
        this.onCollide(target);
        return true;
    }

}
