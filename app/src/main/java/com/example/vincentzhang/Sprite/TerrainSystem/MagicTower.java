package com.example.vincentzhang.Sprite.TerrainSystem;

import android.graphics.Canvas;
import android.graphics.Rect;

import com.example.vincentzhang.Sprite.CoordinateSystem;
import com.example.vincentzhang.Sprite.Vector2D;
import com.example.vincentzhang.Sprite.WeaponSystem.Bullet;

/**
 * Created by VincentZhang on 6/10/2017.
 */

public class MagicTower extends Building {
    Bullet topBullet = null;

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
    }

    @Override
    public void beforeCollision() {
        super.beforeCollision();
        if (topBullet != null) {
            topBullet.beforeCollision();
        }
    }

    @Override
    public Rect draw(Canvas canvas) {
        Rect rect = super.draw(canvas);
        if (topBullet != null)
            topBullet.draw(canvas);
        return rect;
    }

    @Override
    public void postUpdate() {
        super.postUpdate();

        if (topBullet == null) {
            topBullet = new Bullet(16);
        }
        topBullet.postUpdate();
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
    }
}
