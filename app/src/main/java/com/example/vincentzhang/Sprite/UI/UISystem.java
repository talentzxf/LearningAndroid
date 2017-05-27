package com.example.vincentzhang.Sprite.UI;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.example.vincentzhang.Sprite.AbstractSprite;
import com.example.vincentzhang.Sprite.ImageSprite;
import com.example.vincentzhang.Sprite.SpriteWorld;
import com.example.vincentzhang.Sprite.SubSystem;

/**
 * Created by VincentZhang on 5/27/2017.
 */

public class UISystem implements SubSystem {
    @Override
    public boolean init(String level, Resources resources, Canvas canvas) {
        return false;
    }

    @Override
    public void draw(Canvas canvas) {
        int currentlyKilled = SpriteWorld.getInst().getLeadingSprite().getCredit();
        int scrWidth = canvas.getWidth();
        int scrHeight = canvas.getHeight();

        String text = "You've killed:" + currentlyKilled + " monstors!";
        Paint p = new Paint();
        p.setColor(Color.BLUE);
        p.setTextSize(70);
        p.setStrokeWidth(10);
        p.setStyle(Paint.Style.FILL);
        Rect textRect = new Rect();
        p.getTextBounds(text, 0, text.length(), textRect);
        float textWidth = p.measureText(text);
        canvas.drawText(text,scrWidth - textWidth,textRect.height(),p);
    }

    @Override
    public void beforeCollision() {

    }

    @Override
    public AbstractSprite detectCollide(ImageSprite imgSprite) {
        return null;
    }

    @Override
    public void preUpdate() {

    }

    @Override
    public void postUpdate() {

    }
}
