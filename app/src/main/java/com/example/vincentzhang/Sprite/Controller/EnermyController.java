package com.example.vincentzhang.Sprite.Controller;

import android.util.Log;

import com.example.vincentzhang.Sprite.AbstractSprite;
import com.example.vincentzhang.Sprite.ActorSprite;
import com.example.vincentzhang.Sprite.ControllerAbstractSprite;
import com.example.vincentzhang.Sprite.DIRECTIONS;
import com.example.vincentzhang.Sprite.ImageSprite;
import com.example.vincentzhang.Sprite.SpriteWorld;
import com.example.vincentzhang.Sprite.Utilities;
import com.example.vincentzhang.Sprite.Vector2D;

/**
 * Created by VincentZhang on 5/24/2017.
 */

public class EnermyController implements Controller {
    private ActorSprite target;

    private final int PUSH_DISTANCE = 20;
    private int damage = 10;
    public EnermyController(ControllerAbstractSprite target) {
        if(!(target instanceof ActorSprite)){
            throw new RuntimeException("EnermyController can only accept ActorSprite!");
        }
        this.target = (ActorSprite) target;
        this.target.setMoving(true);
        this.target.setMoveSpeed(8);
        this.target.setHp(100);
    }

    @Override
    public void update(){
        ImageSprite leadingSprite = SpriteWorld.getInst().getLeadingSprite();
        DIRECTIONS nextDir = Utilities.calculateDir( target.getSpritePos(), leadingSprite.getSpritePos());
        target.setCurDirection(nextDir);


    }

    @Override
    public void onCollide(AbstractSprite collideTarget) {
        if(collideTarget instanceof ActorSprite){
            ActorSprite collideTargetActor = (ActorSprite) collideTarget;
            ActorSprite curActor = target;

            if(collideTargetActor.getTeamNumber() != curActor.getTeamNumber()){
                Log.i("Collide", "Collide with enermy!!!!");

                Vector2D newPos = collideTarget.getSpritePos().applyDir(collideTargetActor.getCurDirection(), -PUSH_DISTANCE);
                collideTargetActor.setSpritePos(newPos);

                collideTargetActor.reduceHP(damage, this.target);
            }
        }
    }
}
