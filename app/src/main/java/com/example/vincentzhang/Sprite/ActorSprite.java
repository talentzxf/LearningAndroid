package com.example.vincentzhang.Sprite;

/**
 * Created by VincentZhang on 5/25/2017.
 */

public class ActorSprite extends ImageSprite {

    private String name;



    public ActorSprite(int imgId, String name) {
        super(imgId);
        this.name = name;
    }
}
