package com.example.vincentzhang.Sprite.Controller;

import android.graphics.Rect;

import com.example.vincentzhang.Sprite.AbstractSprite;
import com.example.vincentzhang.Sprite.ActorSprite;
import com.example.vincentzhang.Sprite.CollideDetector;
import com.example.vincentzhang.Sprite.ControllerAbstractSprite;
import com.example.vincentzhang.Sprite.DIRECTIONS;
import com.example.vincentzhang.Sprite.SpriteWorld;
import com.example.vincentzhang.Sprite.TerrainSystem.Building;
import com.example.vincentzhang.Sprite.Vector2D;

/**
 * Created by VincentZhang on 5/20/2017.
 */

public class ButtonController implements ButtonEventListener, Controller {
    private ActorSprite target;

    public ButtonController(ControllerAbstractSprite target) {
        if (!(target instanceof ActorSprite)) {
            throw new RuntimeException("ButtonController can only accept ActorSprite!");
        }
        this.target = (ActorSprite) target;
        ButtonEventDispatcher.inst().addListener(this);
    }

    @Override
    public void onClick(DIRECTIONS dir) {
        if (dir == DIRECTIONS.UNKNOWN) {
            target.setMoving(false);
        } else {
            target.setCurDirection(dir);
            target.setMoving(true);
        }
        CollideDetector.setDirtyFlag(true);
    }

    @Override
    public void onClick(Character but) {
        switch (but) {
            case 'A': {
                Rect rect = target.getScrRect();
                Vector2D newPos = target.getSpritePos().applyDir(target.getCurDirection(), Math.min(rect.width(), rect.height()));
                SpriteWorld.getInst().getWeaponSystem().addBomb(newPos, target);
            }
            break;
            case 'B': {
                Rect rect = target.getScrRect();
                Vector2D newPos = target.getSpritePos().applyDir(target.getCurDirection(), Math.min(rect.width(), rect.height()));
                Building newBuilding = new Building(11, newPos);
                newBuilding.setDestroyable(false);
                ControllerFactory.createController("HospitalController", newBuilding);
                SpriteWorld.getInst().getBuildingSystem().addBuilding(newBuilding);
            }
            break;
        }
        CollideDetector.setDirtyFlag(true);
    }

    @Override
    public void update() {

    }

    @Override
    public void onCollide(AbstractSprite target) {

    }
}
