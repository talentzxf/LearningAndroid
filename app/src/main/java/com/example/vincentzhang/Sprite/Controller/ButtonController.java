package com.example.vincentzhang.Sprite.Controller;

import android.graphics.Rect;

import com.example.vincentzhang.Sprite.AbstractSprite;
import com.example.vincentzhang.Sprite.ActorSprite;
import com.example.vincentzhang.Sprite.CollideDetector;
import com.example.vincentzhang.Sprite.ControllerAbstractSprite;
import com.example.vincentzhang.Sprite.DIRECTIONS;
import com.example.vincentzhang.Sprite.ResourceSystem.ResourceType;
import com.example.vincentzhang.Sprite.SpriteWorld;
import com.example.vincentzhang.Sprite.TerrainSystem.Hospital;
import com.example.vincentzhang.Sprite.TerrainSystem.MagicTower;
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

                if(SpriteWorld.getInst().getLeadingSprite().getResource(ResourceType.COIN) < 1){
                    SpriteWorld.getInst().getUiSystem().addMessage("不能建造,没钱啊没钱!");
                    break;
                }
                SpriteWorld.getInst().getLeadingSprite().spend(ResourceType.COIN, 1);

                Rect rect = target.getScrRect();
                Vector2D newPos = target.getSpritePos().applyDir(target.getCurDirection(), Math.min(rect.width(), rect.height()));
                SpriteWorld.getInst().getWeaponSystem().addBomb(newPos, target);
            }
            break;
            case 'B': {
                int coin = 5;
                int timber = 2;

                if(SpriteWorld.getInst().getLeadingSprite().getResource(ResourceType.COIN) < coin ||
                        SpriteWorld.getInst().getLeadingSprite().getResource(ResourceType.TIMBER) < timber){
                    SpriteWorld.getInst().getUiSystem().addMessage("不能建造,没钱没木材啊没钱没木材!");
                    break;
                }

                SpriteWorld.getInst().getLeadingSprite().spend(ResourceType.COIN, coin);
                SpriteWorld.getInst().getLeadingSprite().spend(ResourceType.TIMBER, timber);

                Rect rect = target.getScrRect();
                Vector2D newPos = target.getSpritePos().applyDir(target.getCurDirection(), Math.min(rect.width(), rect.height()));
                // Building newBuilding = new Building(11, newPos);
                Hospital hospital = new Hospital(11, newPos);
                hospital.setDistance(500);
                hospital.setDestroyable(true);
                hospital.setMaxHp(1000);
                hospital.setHp(1000);
                hospital.setTeamNumber(target.getTeamNumber());
                ControllerFactory.createController("HospitalController", hospital);
                SpriteWorld.getInst().getBuildingSystem().addBuilding(hospital);
            }
            break;
            case 'C':{
                int coin = 8;
                int timber = 4;

                if(SpriteWorld.getInst().getLeadingSprite().getResource(ResourceType.COIN) < coin ||
                        SpriteWorld.getInst().getLeadingSprite().getResource(ResourceType.TIMBER) < timber){
                    SpriteWorld.getInst().getUiSystem().addMessage("不能建造,没钱没木材啊没钱没木材!");
                    break;
                }

                SpriteWorld.getInst().getLeadingSprite().spend(ResourceType.COIN, coin);
                SpriteWorld.getInst().getLeadingSprite().spend(ResourceType.TIMBER, timber);

                Rect rect = target.getScrRect();
                Vector2D newPos = target.getSpritePos().applyDir(target.getCurDirection(), Math.min(rect.width(), rect.height()));
                // Building newBuilding = new Building(11, newPos);
                MagicTower magicTower = new MagicTower(15, newPos, SpriteWorld.getInst().getLeadingSprite());
                magicTower.setDestroyable(true);
                magicTower.setMaxHp(10000);
                magicTower.setHp(10000);
                magicTower.setTeamNumber(target.getTeamNumber());
                ControllerFactory.createController("MagicTowerController", magicTower);
                SpriteWorld.getInst().getBuildingSystem().addBuilding(magicTower);
            }
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
