package com.example.vincentzhang.Sprite.Controller;

import com.example.vincentzhang.Sprite.AbstractSprite;
import com.example.vincentzhang.Sprite.ControllerAbstractSprite;

/**
 * Created by VincentZhang on 5/31/2017.
 */

public class HospitalController implements Controller {
    private ControllerAbstractSprite target;

    public HospitalController(ControllerAbstractSprite target){
        this.target = target;
    }

    @Override
    public void update() {

    }

    @Override
    public void onCollide(AbstractSprite target) {

    }
}
