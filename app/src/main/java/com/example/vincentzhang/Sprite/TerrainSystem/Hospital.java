package com.example.vincentzhang.Sprite.TerrainSystem;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.example.vincentzhang.Sprite.HasLifeAbstractSprite;
import com.example.vincentzhang.Sprite.ParticleSystem.AbstractParticleSystem;
import com.example.vincentzhang.Sprite.ParticleSystem.FollowSpriteParticleSystem;
import com.example.vincentzhang.Sprite.Vector2D;

/**
 * Created by VincentZhang on 6/4/2017.
 */

public class Hospital extends Building {
    private HasLifeAbstractSprite healTarget;
    private AbstractParticleSystem particleSystem;
    private int distance = -1;

    public Hospital(int id, int gridX, int gridY) {
        super(id, gridX, gridY);
    }

    public Hospital(int id, Vector2D pos) {
        super(id, pos);
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    @Override
    public void preUpdate() {
        if(particleSystem != null)
            particleSystem.preUpdate();
    }

    @Override
    public Rect draw(Canvas canvas) {

        // Shoot heal ray at target.
        if (distance > 0) {
            Paint p = new Paint();
            p.setStyle(Paint.Style.FILL);
            // p.setARGB(0x0F,0x00,0xF0,0x00);
            p.setColor(Color.BLUE);
            p.setAlpha(20);

            Rect scrRect = this.getScrRect();
            if(scrRect != null)
                canvas.drawCircle(scrRect.centerX(), scrRect.centerY(), this.distance, p);
        }
        Rect retRect = super.draw(canvas);
        if(particleSystem != null)
            particleSystem.draw(canvas);
        return retRect;
    }

    public void setHealTarget(HasLifeAbstractSprite healTarget) {
        if(this.healTarget == healTarget)
            return;

        if(healTarget == null){
            particleSystem = null;
            this.healTarget = null;
            return;
        }

        this.healTarget = healTarget;
        particleSystem = new FollowSpriteParticleSystem(this.healTarget);
    }
}
