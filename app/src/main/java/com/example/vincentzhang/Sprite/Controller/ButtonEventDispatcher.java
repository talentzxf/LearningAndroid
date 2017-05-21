package com.example.vincentzhang.Sprite.Controller;

import com.example.vincentzhang.Sprite.DIRECTIONS;

import java.util.ArrayList;

/**
 * Created by VincentZhang on 5/20/2017.
 */

public class ButtonEventDispatcher implements ButtonEventListener {
    private ArrayList<ButtonEventListener> listeners = new ArrayList<>();
    private static ButtonEventDispatcher instance = new ButtonEventDispatcher();

    private ButtonEventDispatcher(){}

    public static ButtonEventDispatcher inst(){
        return instance;
    }

    @Override
    public void onClick(Character c) {
        for(ButtonEventListener listener:listeners){
            listener.onClick(c);
        }
    }

    @Override
    public void onClick(DIRECTIONS dir) {
        for(ButtonEventListener listener:listeners){
            listener.onClick(dir);
        }
    }

    public void addListener(ButtonEventListener l){
        this.listeners.add(l);
    }
}
