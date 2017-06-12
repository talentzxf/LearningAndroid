package com.example.vincentzhang.Sprite.Controller;

import com.example.vincentzhang.Sprite.AbstractSprite;
import com.example.vincentzhang.Sprite.ActorSprite;
import com.example.vincentzhang.Sprite.ControllerAbstractSprite;
import com.example.vincentzhang.Sprite.DIRECTIONS;
import com.example.vincentzhang.Sprite.HasLifeAbstractSprite;
import com.example.vincentzhang.Sprite.SpriteWorld;
import com.example.vincentzhang.Sprite.TerrainSystem.Building;
import com.example.vincentzhang.Sprite.Utilities;
import com.example.vincentzhang.Sprite.Vector2D;

import java.util.ArrayList;

/**
 * Created by VincentZhang on 5/24/2017.
 */

public class ScorpionController implements Controller {
    private ActorSprite target;

    private final int PUSH_DISTANCE = 20;
    private int damage = 10;
    private int distance = 500;

    private ArrayList<Vector2D> patrolPoints = new ArrayList<>();
    private int curPatrolDstIdx = 0;
    private boolean isTracking = false;

    public ScorpionController(ControllerAbstractSprite target) {
        if (!(target instanceof ActorSprite)) {
            throw new RuntimeException("ScorpionController can only accept ActorSprite!");
        }
        this.target = (ActorSprite) target;
        this.target.setMoving(true);
        this.target.setMoveSpeed(8);
    }

    @Override
    public void update() {
        // ImageSprite leadingSprite = SpriteWorld.getInst().getLeadingSprite();
        HasLifeAbstractSprite sprite = SpriteWorld.getInst().getNearestEnermySprite(target.getCurCenterPos(), target.getTeamNumber(), distance);
        if (sprite != null) {
            DIRECTIONS nextDir = Utilities.calculateDir(target.getSpritePos(), sprite.getSpritePos());
            target.setMoving(true);
            target.setCurDirection(nextDir);
            // Previously not tracking, actively advance to avoid infinite loop
            if(!isTracking){
                target.setSpritePos(target.getSpritePos().applyDir(nextDir, target.getMoveSpeed()));
            }
            isTracking = true;
        } else { // Patrol around the lair
            isTracking = false;
            if (patrolPoints.size() != 0) {
                Vector2D curPatrolDst = patrolPoints.get(curPatrolDstIdx);
                DIRECTIONS nextDir = Utilities.calculateDir(target.getSpritePos(), curPatrolDst);
                target.setMoving(true);
                target.setCurDirection(nextDir);

                if (target.getSpritePos().equals(curPatrolDst, 30)) {
                    curPatrolDstIdx = (curPatrolDstIdx + 1) % patrolPoints.size();
                }

            } else {
                target.setMoving(false);
            }
        }
    }

    @Override
    public void onCollide(AbstractSprite collideTarget) {
        if (collideTarget instanceof ActorSprite) {
            ActorSprite collideTargetActor = (ActorSprite) collideTarget;
            ActorSprite curActor = target;

            if (collideTargetActor.getTeamNumber() != curActor.getTeamNumber()) {
                // Log.i("Collide", "Collide with enermy!!!!");

                Vector2D newPos = collideTarget.getSpritePos().applyDir(collideTargetActor.getCurDirection(), -PUSH_DISTANCE);
                collideTargetActor.setSpritePos(newPos);

                collideTargetActor.reduceHP(damage, this.target);
            }
        } else if (collideTarget instanceof Building) { // Attack enermy building
            if (((Building) collideTarget).getTeamNumber() != target.getTeamNumber()) {
                ((Building) collideTarget).reduceHP(damage, this.target);
            }
        }
    }

    public void addPatrolPoint(Vector2D patrolPoint) {
        patrolPoints.add(patrolPoint);
    }
}
