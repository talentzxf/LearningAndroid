package com.example.vincentzhang.Sprite.Controller;

import com.example.vincentzhang.Sprite.AbstractSprite;
import com.example.vincentzhang.Sprite.ActorSprite;
import com.example.vincentzhang.Sprite.ControllerAbstractSprite;
import com.example.vincentzhang.Sprite.DIRECTIONS;
import com.example.vincentzhang.Sprite.SpriteWorld;
import com.example.vincentzhang.Sprite.Vector2D;

/**
 * Created by VincentZhang on 5/29/2017.
 */

public class ScorpionLairController implements BuildingController {
    private ControllerAbstractSprite target;
    private long lastScorpionTime = -1;

    public ScorpionLairController(ControllerAbstractSprite target) {
        this.target = target;
    }

    @Override
    public void update() {
        if(lastScorpionTime == -1 || System.currentTimeMillis() - lastScorpionTime > 10000){
            ActorSprite newScorpion = new ActorSprite(9, "enermy_avatar");
            newScorpion.setTeamNumber(1); // TODO should be configurable here.
            newScorpion.setMaxHp(300);
            newScorpion.setHp(300);
            Vector2D newSpritePos = target.getSpritePos().applyDir(DIRECTIONS.DOWNRIGHT, target.getScrRect().width());
            newScorpion.setSpritePos(newSpritePos);
            ControllerFactory.createController("ScorpionController", newScorpion);

            ScorpionController scorpionController = (ScorpionController) newScorpion.getController();

            int guardRange = 300;

            scorpionController.addPatrolPoint(target.getSpritePos().applyDir(DIRECTIONS.DOWNRIGHT, target.getScrRect().width() + guardRange));
            scorpionController.addPatrolPoint(target.getSpritePos().applyDir(DIRECTIONS.RIGHT, target.getScrRect().width() + guardRange).applyDir(DIRECTIONS.UP,guardRange));
            scorpionController.addPatrolPoint(target.getSpritePos().applyDir(DIRECTIONS.LEFT, guardRange).applyDir(DIRECTIONS.UP, guardRange));
            scorpionController.addPatrolPoint(target.getSpritePos().applyDir(DIRECTIONS.DOWN, target.getScrRect().height() + guardRange).applyDir(DIRECTIONS.LEFT, guardRange));

            SpriteWorld.getInst().getSpriteSystem().addSprite(newScorpion);
            lastScorpionTime = System.currentTimeMillis();
        }
    }

    @Override
    public void onCollide(AbstractSprite target) {

    }
}
