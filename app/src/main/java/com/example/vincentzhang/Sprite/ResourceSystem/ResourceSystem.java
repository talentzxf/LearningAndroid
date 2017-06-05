package com.example.vincentzhang.Sprite.ResourceSystem;

import android.content.res.Resources;
import android.graphics.Canvas;

import com.example.vincentzhang.Sprite.AbstractSprite;
import com.example.vincentzhang.Sprite.ImageSprite;
import com.example.vincentzhang.Sprite.SubSystem;
import com.example.vincentzhang.Sprite.Vector2D;

import java.util.ArrayList;

/**
 * Created by VincentZhang on 6/4/2017.
 */

public class ResourceSystem implements SubSystem {

    private ArrayList<Resource> resourceArray = new ArrayList<>();

    @Override
    public boolean init(String level, Resources resources, Canvas canvas) {
        Resource coin = new Coin(12);
        coin.setSpritePos(new Vector2D(100,200));

        Resource timber = new Timber(13);
        timber.setSpritePos(new Vector2D(300,400));
        resourceArray.add(coin);
        resourceArray.add(timber);
        return false;
    }

    @Override
    public void draw(Canvas canvas) {
        for(Resource resource:resourceArray){
            resource.draw(canvas);
        }
    }

    @Override
    public void beforeCollision() {
        for(Resource resource:resourceArray){
            resource.beforeCollision();
        }
    }

    @Override
    public AbstractSprite detectCollide(ImageSprite imgSprite) {
        return null;
    }

    @Override
    public void preUpdate() {
        for(Resource resource:resourceArray){
            resource.preUpdate();
        }
    }

    @Override
    public void postUpdate() {
        for(Resource resource:resourceArray){
            resource.postUpdate();
        }
    }
}
