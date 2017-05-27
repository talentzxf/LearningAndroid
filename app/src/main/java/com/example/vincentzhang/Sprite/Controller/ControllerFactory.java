package com.example.vincentzhang.Sprite.Controller;

import com.example.vincentzhang.Sprite.ActorSprite;
import com.example.vincentzhang.Sprite.WeaponSystem.WeaponSystem;

/**
 * Created by VincentZhang on 5/20/2017.
 */

public class ControllerFactory {
    static public Controller createController(String controllerName, ActorSprite target, WeaponSystem weaponSystem){

        Controller controller = null;
        switch(controllerName){
            case "ButtonController":
                controller = new ButtonController(target, weaponSystem);
                break;
            case "EnermyController":
                controller = new EnermyController(target, weaponSystem);
                break;
        }

        target.setController(controller);

        return controller;
    }
}
