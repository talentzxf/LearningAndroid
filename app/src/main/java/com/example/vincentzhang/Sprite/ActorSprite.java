package com.example.vincentzhang.Sprite;

import com.example.vincentzhang.Sprite.ResourceSystem.ResourceType;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by VincentZhang on 5/25/2017.
 */

public class ActorSprite extends ImageSprite {

    private String name;
    private Map<ResourceType, Integer> resourceAmount = new HashMap<>();

    public int getResource(ResourceType resourceType) {
        if (resourceAmount.get(resourceType) == null) {
            resourceAmount.put(resourceType, 0);
        }

        return resourceAmount.get(resourceType);
    }

    public void addResource(ResourceType resourceType, int amount) {
        if (resourceAmount.get(resourceType) == null) {
            resourceAmount.put(resourceType, amount);
        } else {
            resourceAmount.put(resourceType, resourceAmount.get(resourceType) + amount);
        }
    }

    public ActorSprite(int imgId, String name) {
        super(imgId);
        this.name = name;
    }

    public void spend(ResourceType resourceType, int amount) {
        resourceAmount.put(resourceType, resourceAmount.get(resourceType) - amount);
    }
}
