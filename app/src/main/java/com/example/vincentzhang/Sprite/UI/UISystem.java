package com.example.vincentzhang.Sprite.UI;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.example.vincentzhang.Sprite.AbstractSprite;
import com.example.vincentzhang.Sprite.ImageSprite;
import com.example.vincentzhang.Sprite.ResourceSystem.ResourceType;
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

        String text = "Kills:" + currentlyKilled + " Coins:" + SpriteWorld.getInst().getLeadingSprite().getResource(ResourceType.COIN) +
                " Timber:" + SpriteWorld.getInst().getLeadingSprite().getResource(ResourceType.TIMBER);
        Paint p = new Paint();
        p.setColor(0xFFFF00FF);
        p.setTextSize(70);
        p.setStrokeWidth(10);
        p.setStyle(Paint.Style.FILL);
        Rect textRect = new Rect();
        p.getTextBounds(text, 0, text.length(), textRect);
        float textWidth = p.measureText(text);

        // Paint rectP = new Paint();
        // rectP.setColor(Color.GRAY);
        // canvas.drawRect(new Rect(0,0,canvas.getWidth(), textRect.height()), rectP);
        canvas.drawText(text,scrWidth - textWidth,textRect.height() + 10,p);
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
