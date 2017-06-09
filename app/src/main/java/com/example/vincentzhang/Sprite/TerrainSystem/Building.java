package com.example.vincentzhang.Sprite.TerrainSystem;


import android.graphics.Canvas;
import android.graphics.Rect;

import com.example.vincentzhang.Sprite.AbstractCollidableSprite;
import com.example.vincentzhang.Sprite.ActorSprite;
import com.example.vincentzhang.Sprite.CollideDetector;
import com.example.vincentzhang.Sprite.CoordinateSystem;
import com.example.vincentzhang.Sprite.DIRECTIONS;
import com.example.vincentzhang.Sprite.HasLifeAbstractSprite;
import com.example.vincentzhang.Sprite.Utilities;
import com.example.vincentzhang.Sprite.Vector2D;

/**
 * Created by VincentZhang on 4/23/2017.
 */
public class Building extends HasLifeAbstractSprite {

    public Building(int id, int gridX, int gridY) {
        super(id);
        this.setSpritePos(CoordinateSystem.gridToWorld(new Vector2D(gridX, gridY)));
    }

    public Building(int id, Vector2D pos){
        super(id);
        this.setSpritePos(pos);
    }

    @Override
    public Rect draw(Canvas canvas) {
        Rect scrRect = super.draw(canvas);
        // getSpace4DTree().draw(canvas, 3, scrRect);
        return scrRect;
    }

    @Override
    protected void onCollide(AbstractCollidableSprite target) {
        if(target instanceof ActorSprite){
            super.onCollide(target);
            // Push the sprite away
            Vector2D curCenterPos = target.getCurCenterPos();
            Vector2D targetOldCenterPos = target.getOldCenterPos();
            double half_dist = targetOldCenterPos.dist(curCenterPos)/2;

            Vector2D centerPos = getCurCenterPos();
            // Move the sprite away
            DIRECTIONS moveDir = Utilities.calculateDir(centerPos, targetOldCenterPos);
            Vector2D newPos = target.getSpritePos().applyDir(moveDir, half_dist > 1? half_dist:1);
            target.setSpritePos(newPos);
            CollideDetector.setDirtyFlag(true);
        }
    }

    @Override
    public void postUpdate() {
        super.postUpdate();
        if (this.getController() != null)
            this.getController().update();
    }
}
