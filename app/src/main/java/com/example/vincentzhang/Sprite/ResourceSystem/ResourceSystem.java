package com.example.vincentzhang.Sprite.ResourceSystem;

import android.content.res.Resources;
import android.graphics.Canvas;

import com.example.vincentzhang.Sprite.AbstractSprite;
import com.example.vincentzhang.Sprite.ImageSprite;
import com.example.vincentzhang.Sprite.SubSystem;

import java.util.ArrayList;

/**
 * Created by VincentZhang on 6/4/2017.
 */

public class ResourceSystem implements SubSystem {

    private ArrayList<Resource> resourceArray = new ArrayList<>();

    @Override
    public boolean init(String level, Resources resources, Canvas canvas) {
        return false;
    }

    public void addResource(Resource resource){
        resourceArray.add(resource);
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
        for(Resource resource:resourceArray){
            resource.detectCollide(imgSprite);
        }

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

        ArrayList<Resource> tobeDeletedResources = new ArrayList<>();

        for(Resource resource:resourceArray){
            resource.postUpdate();
            if(resource.isUsed())
                tobeDeletedResources.add(resource);
        }

        for(Resource tobeDeletedResource: tobeDeletedResources){
            resourceArray.remove(tobeDeletedResource);
        }
    }
}
