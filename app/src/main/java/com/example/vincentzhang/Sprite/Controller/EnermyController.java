package com.example.vincentzhang.Sprite.Controller;

import com.example.vincentzhang.Sprite.DIRECTIONS;
import com.example.vincentzhang.Sprite.ImageSprite;
import com.example.vincentzhang.Sprite.SpriteWorld;
import com.example.vincentzhang.Sprite.Utilities;
import com.example.vincentzhang.Sprite.WeaponSystem.WeaponSystem;

/**
 * Created by VincentZhang on 5/24/2017.
 */

public class EnermyController implements Controller {
    private ImageSprite target;
    private WeaponSystem weaponSystem;
    public EnermyController(ImageSprite target, WeaponSystem weaponSystem) {
        this.target = target;
        this.weaponSystem = weaponSystem;
        target.setMoving(true);
        target.setMoveSpeed(1);
    }

    @Override
    public void update(){
        ImageSprite leadingSprite = SpriteWorld.getInst().getLeadingSprite();
        DIRECTIONS nextDir = Utilities.calculateDir( target.getSpritePos(), leadingSprite.getSpritePos());
        target.setCurDirection(nextDir);


    }
}
