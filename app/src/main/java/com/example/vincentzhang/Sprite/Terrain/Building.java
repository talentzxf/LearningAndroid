package com.example.vincentzhang.Sprite.Terrain;


import com.example.vincentzhang.Sprite.AbstractSprite;
import com.example.vincentzhang.Sprite.CoordinateSystem;
import com.example.vincentzhang.Sprite.Vector2D;

/**
 * Created by VincentZhang on 4/23/2017.
 */
public class Building extends AbstractSprite{
    public Building(int id, int gridX, int gridY){
        super(id);
        this.setSpritePos(CoordinateSystem.gridToWorld(new Vector2D(gridX, gridY)));
    }
}
