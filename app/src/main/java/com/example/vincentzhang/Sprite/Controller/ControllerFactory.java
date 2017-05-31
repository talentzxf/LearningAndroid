package com.example.vincentzhang.Sprite.Controller;

import android.util.Log;

import com.example.vincentzhang.Sprite.ControllerAbstractSprite;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by VincentZhang on 5/20/2017.
 * <p>
 * TODO: Use refactor to improve this stupid code.
 */

public class ControllerFactory {
    static public Controller createController(String controllerName, ControllerAbstractSprite target) {

        Controller controller = null;

        String packageName = ControllerFactory.class.getPackage().getName();

        Class controllerClz = null;
        try {
            controllerClz = Class.forName(packageName + "." + controllerName);
            Constructor constructor = controllerClz.getDeclaredConstructor(new Class[]{ControllerAbstractSprite.class});
            controller = (Controller) constructor.newInstance(new Object[]{target});
        } catch (ClassNotFoundException e) {
            Log.e("Can't find class", "Controller not found:" + controllerName, e);
        } catch (NoSuchMethodException e) {
            Log.e("Can't find constructor", "constructor not found:" + controllerName, e);
        } catch (IllegalAccessException e) {
            Log.e("Constructor error", "IllegalAccessException", e);
        } catch (InstantiationException e) {
            Log.e("Constructor error", "InstantiationException", e);
        } catch (InvocationTargetException e) {
            Log.e("Constructor error", "InvocationTargetException", e);
        }

        target.setController(controller);

        return controller;
    }
}
