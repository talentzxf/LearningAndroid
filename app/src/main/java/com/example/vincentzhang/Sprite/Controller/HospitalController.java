package com.example.vincentzhang.Sprite.Controller;

import com.example.vincentzhang.Sprite.AbstractSprite;
import com.example.vincentzhang.Sprite.ControllerAbstractSprite;
import com.example.vincentzhang.Sprite.HasLifeAbstractSprite;
import com.example.vincentzhang.Sprite.SpriteWorld;
import com.example.vincentzhang.Sprite.TerrainSystem.Hospital;

import java.security.InvalidParameterException;

/**
 * Created by VincentZhang on 5/31/2017.
 */

public class HospitalController implements Controller {
    private Hospital target;

    private long CureRayStartTime = -1;

    public HospitalController(ControllerAbstractSprite target) {
        if (target instanceof Hospital)
            this.target = (Hospital) target;
        else
            throw new InvalidParameterException("Should input a hospital");
    }

    @Override
    public void update() {
        HasLifeAbstractSprite targetSprite = SpriteWorld.getInst().getNearestInjuredSprite( target.getSpritePos(), target.getTeamNumber(), target.getDistance());
        target.setHealTarget(targetSprite);
        if(targetSprite != null)
            targetSprite.addHp(1);
    }

    @Override
    public void onCollide(AbstractSprite target) {

    }
}
