package com.example.vincentzhang.Sprite;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by VincentZhang on 6/1/2017.
 */

public class HasLifeAbstractSprite extends ControllerAbstractSprite {
    protected float hp_max = 100;
    protected float hp = 100;
    private int teamNumber = 0;
    private HasLifeAbstractSprite killedBy;
    private int killedEnermies = 0;

    private boolean destroyable = true;

    public void setHp(int hp) {
        this.hp = hp;
    }

    public void addCredit(int i) {
        killedEnermies += i;
    }

    public int getCredit(){
        return killedEnermies;
    }

    public void setDestroyable(boolean destroyable) {
        this.destroyable = destroyable;
    }

    public HasLifeAbstractSprite getKilledBy() {
        return killedBy;
    }
    /**
     *
     * @param damage
     * @return the sprite is dead or not
     */
    public boolean reduceHP(int damage, ActorSprite enermy) {
        if(destroyable == false)
            return false;

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

    public int getTeamNumber() {
        return teamNumber;
    }

    public void setTeamNumber(int teamNumber) {
        this.teamNumber = teamNumber;
    }

    public HasLifeAbstractSprite(int imgId) {
        super(imgId);
    }

    @Override
    public Rect draw(Canvas canvas) {
        Rect retRect = super.draw(canvas);

        if(destroyable){
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
        }
        return retRect;
    }

    public boolean isDestroyable() {
        return destroyable;
    }
}
