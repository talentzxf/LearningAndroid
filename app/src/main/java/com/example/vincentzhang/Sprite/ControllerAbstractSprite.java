package com.example.vincentzhang.Sprite;

import com.example.vincentzhang.Sprite.Controller.Controller;

/**
 * Created by VincentZhang on 5/29/2017.
 */

public abstract class ControllerAbstractSprite extends AbstractSprite {
    private Controller controller;
    public ControllerAbstractSprite(int imgId) {
        super(imgId);
    }

    public Controller getController() {
        return controller;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }
}
