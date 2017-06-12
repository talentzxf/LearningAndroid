package com.example.vincentzhang.Sprite.TerrainSystem;

import android.graphics.Canvas;
import android.graphics.Rect;

import com.example.vincentzhang.Sprite.CoordinateSystem;
import com.example.vincentzhang.Sprite.HasLifeAbstractSprite;
import com.example.vincentzhang.Sprite.SpriteWorld;
import com.example.vincentzhang.Sprite.Vector2D;
import com.example.vincentzhang.Sprite.WeaponSystem.Bullet;
import com.example.vincentzhang.Sprite.WeaponSystem.Lightning;

import java.util.ArrayList;

/**
 * Created by VincentZhang on 6/10/2017.
 */

public class MagicTower extends Building {
    private Bullet topBullet = null;
    private int attackRange = 500;

    private ArrayList<Lightning> lightnings = new ArrayList<>();

    public MagicTower(Integer id, Integer gridX, Integer gridY) {
        super(id, gridX, gridY);
    }

    public MagicTower(Integer id, Vector2D pos) {
        super(id, pos);
    }

    @Override
    public void preUpdate() {
        super.preUpdate();
        if (topBullet != null)
            topBullet.preUpdate();

        if(getScrRect() != null){

            Vector2D towerCenter = CoordinateSystem.scrToWorld( new Vector2D(getScrRect().centerX(), getScrRect().centerY()));

            HasLifeAbstractSprite enermySprite = SpriteWorld.getInst().getNearestEnermySprite( towerCenter, getTeamNumber(), attackRange);

            if(enermySprite != null){
                if(this.lightnings.size() <= 1){
                    Lightning lightning = new Lightning();
                    lightning.setScrStart(new Vector2D(getScrRect().centerX(), getScrRect().top));
                    lightning.setTarget(enermySprite);
                    this.lightnings.add(lightning);
                }
            }
        }
    }

    @Override
    public void beforeCollision() {
        super.beforeCollision();
        if (topBullet != null) {
            topBullet.beforeCollision();
        }

        for(Lightning lightning: lightnings){
            lightning.beforeCollision();
        }
    }

    @Override
    public Rect draw(Canvas canvas) {
        Rect rect = super.draw(canvas);
        if (topBullet != null)
            topBullet.draw(canvas);

        for(Lightning lightning: lightnings){
            lightning.draw(canvas);
        }
        return rect;
    }

    @Override
    public void postUpdate() {
        super.postUpdate();

        if (topBullet == null) {
            topBullet = new Bullet(16);
        }

        if (!topBullet.isInited()) {
            if (topBullet.getScrRect() != null) {
                Rect towerRect = getScrRect();
                Vector2D towerPosWorld = CoordinateSystem.scrToWorld(
                        new Vector2D(towerRect.centerX(), towerRect.top)
                );
                topBullet.setSpriteCenterPos(towerPosWorld);

                topBullet.setInited(true);
            }
        }


        topBullet.postUpdate();

        ArrayList<Lightning> tobeDeletedLightnings = new ArrayList<>();
        for(Lightning lightning: lightnings){
            lightning.postUpdate();
            if(!lightning.isAlive()){
                tobeDeletedLightnings.add(lightning);
            }
        }

        for(Lightning tobeDeletedLightning:tobeDeletedLightnings){
            this.lightnings.remove(tobeDeletedLightning);
        }
    }
}
