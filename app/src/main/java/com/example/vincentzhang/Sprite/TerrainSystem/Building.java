package com.example.vincentzhang.Sprite.TerrainSystem;


import android.graphics.Canvas;
import android.graphics.Rect;

import com.example.vincentzhang.Sprite.CoordinateSystem;
import com.example.vincentzhang.Sprite.HasLifeAbstractSprite;
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
    public void postUpdate() {
        super.postUpdate();
        if (this.getController() != null)
            this.getController().update();
    }
}
