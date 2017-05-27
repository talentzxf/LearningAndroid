package com.example.vincentzhang.Sprite;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by VincentZhang on 5/25/2017.
 */

public class ActorSprite extends ImageSprite {
    private float hp_max = 10000;
    private float hp = 10000;
    private int teamNumber = 0;
    private String name;
    private ActorSprite killedBy;
    private int killedMonsters = 0;

    public ActorSprite(int imgId, String name) {
        super(imgId);
        this.name = name;
    }

    public int getTeamNumber() {
        return teamNumber;
    }

    public void setTeamNumber(int teamNumber) {
        this.teamNumber = teamNumber;
    }

    public ActorSprite getKilledBy() {
        return killedBy;
    }

    @Override
    public Rect draw(Canvas canvas) {
        Rect retRect = super.draw(canvas);

        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(5.0f);
        paint.setStyle(Paint.Style.STROKE);
        Rect drawRect = this.getScrRect();
        canvas.drawRect(drawRect.left, drawRect.top, drawRect.left + drawRect.width(), drawRect.top + drawRect.height() * 0.07f, paint);

        Paint paint2 = new Paint();
        paint2.setColor(Color.RED);
        paint2.setStrokeWidth(1.0f);
        paint2.setStyle(Paint.Style.FILL);
        canvas.drawRect(drawRect.left, drawRect.top, drawRect.left + drawRect.width() * this.hp/this.hp_max, drawRect.top + drawRect.height() * 0.07f, paint2);

        return retRect;
    }

    /**
     *
     * @param damage
     * @return the sprite is dead or not
     */
    public boolean reduceHP(int damage, ActorSprite enermy) {
        this.hp -= damage;
        if(this.hp <= 0 ){
            this.hp = 0;
            killedBy = enermy;
        }

        return this.hp == 0;
    }

    public boolean isDead() {
        return this.hp == 0;
    }

    public void addCredit(int i) {
        killedMonsters += i;
    }

    public int getCredit(){
        return killedMonsters;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }
}
