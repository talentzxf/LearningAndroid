package com.example.vincentzhang.Sprite;

import com.example.vincentzhang.Sprite.imgemanagement.ImageManager;
import com.example.vincentzhang.Sprite.imgemanagement.Space4DTree;

/**
 * Created by VincentZhang on 6/4/2017.
 */

public abstract class AbstractCollidableSprite extends AbstractSprite {
    private Space4DTree space4DTree;

    public AbstractCollidableSprite(int imgId) {
        super(imgId);
        space4DTree = ImageManager.inst().getSpace4DTree(imgId);
    }

    public Space4DTree getSpace4DTree() {
        return space4DTree;
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
