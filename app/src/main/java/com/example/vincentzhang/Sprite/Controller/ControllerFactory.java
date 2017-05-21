package com.example.vincentzhang.Sprite.Controller;

import com.example.vincentzhang.Sprite.ImageSprite;
import com.example.vincentzhang.Sprite.WeaponSystem.WeaponSystem;

/**
 * Created by VincentZhang on 5/20/2017.
 */

public class ControllerFactory {
    static public Controller createController(String controllerName, ImageSprite target, WeaponSystem weaponSystem){
        if(controllerName.equals("ButtonController")){
            return new ButtonController(target, weaponSystem);
        }

        return null;
    }
}
