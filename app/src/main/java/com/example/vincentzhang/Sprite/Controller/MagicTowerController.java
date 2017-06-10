package com.example.vincentzhang.Sprite.Controller;

import com.example.vincentzhang.Sprite.AbstractSprite;
import com.example.vincentzhang.Sprite.ControllerAbstractSprite;
import com.example.vincentzhang.Sprite.TerrainSystem.Building;

/**
 * Created by VincentZhang on 6/9/2017.
 */

public class MagicTowerController implements Controller {
    private Building target;

    public MagicTowerController(ControllerAbstractSprite target) {
        if (!(target instanceof Building)) {
            throw new RuntimeException("MagicTowerController can only accept Building!");
        }
        this.target = (Building) target;
    }

    @Override
    public void update() {

    }

    @Override
    public void onCollide(AbstractSprite target) {

    }
}
