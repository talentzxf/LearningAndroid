package com.example.vincentzhang.Sprite.ParticleSystem;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.example.vincentzhang.Sprite.AbstractSprite;
import com.example.vincentzhang.Sprite.ImageSprite;
import com.example.vincentzhang.Sprite.SubSystem;

import java.util.ArrayList;

/**
 * Created by VincentZhang on 6/4/2017.
 */

public abstract class AbstractParticleSystem implements SubSystem {
    private ArrayList<Particle> particles = new ArrayList<>();
    private Rect generateArea;
    private AbstractSprite followSprite;
    private long createTime = System.currentTimeMillis();
    private long age = 10;
    private long particleInterval = 500;
    private long lastParticleTime = -1;

    public AbstractParticleSystem() {

    }

    @Override
    public boolean init(String level, Resources resources, Canvas canvas) {
        return false;
    }

    @Override
    public void draw(Canvas canvas) {
        for (Particle p : particles) {
            p.draw(canvas);
        }
    }

    @Override
    public void beforeCollision() {

    }

    @Override
    public AbstractSprite detectCollide(ImageSprite imgSprite) {
        return null;
    }

    abstract protected Particle generateNextParticle();

    @Override
    public void preUpdate() {
        if (lastParticleTime == -1 || System.currentTimeMillis() - lastParticleTime > this.particleInterval) {
            lastParticleTime = System.currentTimeMillis();
            this.particles.add(generateNextParticle());
        }

        ArrayList<Particle> tobeDeletedParticles = new ArrayList<>();
        for (Particle p : particles) {
            p.preUpdate();

            if (!p.isLive()) {
                tobeDeletedParticles.add(p);
            }
        }

        for (Particle tobeDeletedParticle : tobeDeletedParticles) {
            particles.remove(tobeDeletedParticle);
        }

    }

    public boolean isDead() {
        return (System.currentTimeMillis() - createTime) > age;
    }

    @Override
    public void postUpdate() {

    }
}
