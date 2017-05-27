package com.example.vincentzhang.Sprite.Controller;

import android.graphics.Rect;

import com.example.vincentzhang.Sprite.AbstractSprite;
import com.example.vincentzhang.Sprite.CollideDetector;
import com.example.vincentzhang.Sprite.DIRECTIONS;
import com.example.vincentzhang.Sprite.ImageSprite;
import com.example.vincentzhang.Sprite.Vector2D;
import com.example.vincentzhang.Sprite.WeaponSystem.WeaponSystem;

/**
 * Created by VincentZhang on 5/20/2017.
 */

public class ButtonController implements ButtonEventListener,Controller{
    private ImageSprite target;
    private WeaponSystem weaponSystem;

    public ButtonController(ImageSprite target, WeaponSystem weaponSystem) {
        this.target = target;
        this.weaponSystem = weaponSystem;
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
    public void onClick(Character but){
        switch(but){
            case 'A':
                Vector2D spritePos = target.getSpritePos();
                Rect rect = target.getScrRect();
                Vector2D newPos = target.getSpritePos().applyDir(target.getCurDirection(), Math.min(rect.width(), rect.height()));
                weaponSystem.addBomb(newPos);
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
