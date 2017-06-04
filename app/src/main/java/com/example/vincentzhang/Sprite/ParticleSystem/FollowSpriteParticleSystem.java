package com.example.vincentzhang.Sprite.ParticleSystem;

import android.graphics.Rect;

import com.example.vincentzhang.Sprite.AbstractSprite;
import com.example.vincentzhang.Sprite.Utilities;
import com.example.vincentzhang.Sprite.Vector2D;

/**
 * Created by VincentZhang on 6/4/2017.
 */

public class FollowSpriteParticleSystem extends AbstractParticleSystem {
    private AbstractSprite followSprite;

    public FollowSpriteParticleSystem(AbstractSprite sprite) {
        followSprite = sprite;
    }

    @Override
    protected Particle generateNextParticle() {
        Particle p = new Particle(-1);
        int randX = -1;
        int randY = -1;

        Rect generateRect = followSprite.getScrRect();
        randX = Utilities.randInRange(generateRect.left, generateRect.right);
        randY = Utilities.randInRange(generateRect.top, generateRect.bottom);

        // Fly up
        p.setVelocity(new Vector2D(0.0, -50.0));
        p.setSpritePos(new Vector2D(randX, randY));
        return p;
    }
}
