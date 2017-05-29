package com.example.vincentzhang.Sprite.Controller;

import com.example.vincentzhang.Sprite.ControllerAbstractSprite;

/**
 * Created by VincentZhang on 5/20/2017.
 *
 * TODO: Use refactor to improve this stupid code.
 */

public class ControllerFactory {
    static public Controller createController(String controllerName, ControllerAbstractSprite target){

        Controller controller = null;
        switch(controllerName){
            case "ButtonController":
                controller = new ButtonController(target);
                break;
            case "EnermyController":
                controller = new EnermyController(target);
                break;
            case "ScorpionLairController":
                controller = new ScorpionLairController(target);
                break;
        }

        target.setController(controller);

        return controller;
    }
}
