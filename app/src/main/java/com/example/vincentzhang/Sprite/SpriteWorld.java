package com.example.vincentzhang.Sprite;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.example.vincentzhang.Sprite.ResourceSystem.ResourceSystem;
import com.example.vincentzhang.Sprite.SpriteSystem.SpriteSystem;
import com.example.vincentzhang.Sprite.TerrainSystem.Building;
import com.example.vincentzhang.Sprite.TerrainSystem.BuildingSystem;
import com.example.vincentzhang.Sprite.TerrainSystem.TerrainSystem;
import com.example.vincentzhang.Sprite.UI.UISystem;
import com.example.vincentzhang.Sprite.WeaponSystem.WeaponSystem;
import com.example.vincentzhang.Sprite.imgemanagement.ImageManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by VincentZhang on 4/15/2017.
 */

public class SpriteWorld extends Thread {

    private static final int VIEWPORT_MARGIN = 100;

    private boolean inited = false;
    private boolean initThreadStarted = false;
    private Context context;
    private Canvas canvas;

    private ArrayList<SubSystem> subSystems = new ArrayList<>();
    private WeaponSystem weaponSystem;
    private SpriteSystem spriteSystem;
    private BuildingSystem buildingSystem;
    private ResourceSystem resourceSystem;
    private UISystem uiSystem;
    private Object nearestSprite;

    private SpriteWorld() {
    }

    private static SpriteWorld inst = new SpriteWorld();

    static public SpriteWorld getInst() {
        return inst;
    }

    public boolean inited() {
        return inited;
    }

    @Override
    public void run() {
        this.init(context, canvas);
    }

    public boolean init(Context context, Canvas canvas) {
        String level = "level1";
        ImageManager.inst().init(level, context.getResources(), canvas);

        subSystems.add(new TerrainSystem());

        buildingSystem = new BuildingSystem();
        subSystems.add(buildingSystem);
        // TODO: Decouple weaponSystem dependency to controller.
        weaponSystem = new WeaponSystem();
        subSystems.add(weaponSystem);

        resourceSystem = new ResourceSystem();
        subSystems.add(resourceSystem);

        SpriteSystem spriteSystem = new SpriteSystem();
        this.spriteSystem = spriteSystem;
        subSystems.add(spriteSystem);

        uiSystem = new UISystem();
        subSystems.add(uiSystem);

        for (SubSystem subSystem : subSystems) {
            subSystem.init(level, context.getResources(), canvas);
        }

        inited = true;
        return true;
    }

    private long steps = 0;

    public void preUpdate() {
        updateViewPort();
        for (SubSystem subSystem : subSystems) {
            subSystem.preUpdate();
        }
    }

    public void beforeCollision() {

        for (SubSystem subSystem : subSystems) {
            subSystem.beforeCollision();
        }
    }

    public ActorSprite getLeadingSprite() {
        if (spriteSystem == null) return null;
        return spriteSystem.getLeadingSprite();
    }

    private void updateViewPort() {
        if (spriteSystem == null || spriteSystem.getLeadingSprite() == null) {
            return;
        }

        // Update view port, to at least leave 1 tile to the sprite
        Vector2D curViewPort = CoordinateSystem.getViewPortPos();
        Vector2D curSpritePos = spriteSystem.getLeadingSprite().getSpritePos();
        Vector2D scrDim = CoordinateSystem.getScrDimension();

        Rect spriteRect = spriteSystem.getLeadingSprite().getScrRect();

        if (scrDim == null || spriteRect == null) { // CoordinateSystem not inited yet.
            return;
        }
        int spriteWidth = spriteRect.width();
        int spriteHeight = spriteRect.height();

        if (spriteHeight == 0 || spriteWidth == 0) {
            return;
        }

        Vector2D newViewPortPos = curViewPort;
        if (curSpritePos.getX() - curViewPort.getX() < VIEWPORT_MARGIN) {
            newViewPortPos.setX(curSpritePos.getX() - VIEWPORT_MARGIN);
        }

        if (curSpritePos.getX() + spriteWidth > curViewPort.getX() + scrDim.getX() - VIEWPORT_MARGIN) {
            newViewPortPos.setX(curSpritePos.getX() + spriteWidth + VIEWPORT_MARGIN - scrDim.getX());
        }

        if (curSpritePos.getY() - curViewPort.getY() < VIEWPORT_MARGIN) {
            newViewPortPos.setY(curSpritePos.getY() - VIEWPORT_MARGIN);
        }

        if (curSpritePos.getY() + spriteHeight > curViewPort.getY() + scrDim.getY() - VIEWPORT_MARGIN) {
            newViewPortPos.setY(curSpritePos.getY() + VIEWPORT_MARGIN - scrDim.getY() + spriteHeight);
        }

        CoordinateSystem.setViewPortPos(newViewPortPos);
    }

    /**
     * @return need reprocess or not.
     */
    public boolean processCollision() {
        if (spriteSystem == null || spriteSystem.getLeadingSprite() == null) {
            return false;
        }

        if (CollideDetector.isDirtyFlag()) {

            for (SubSystem subSystem : subSystems) {
                for (ImageSprite target : spriteSystem.getAllSprites()) {
                    if (null != subSystem.detectCollide(target)) {
                        // Log.i("Collide detected!", subSystem.getClass().toString() + " with " + target.getImgId());
                        return true;
                    }
                }
            }

            // Bulidings can be destroyed
            for (Building building : buildingSystem.getDestroyableBuildings()) {
                weaponSystem.detectExplodeDamage(building);
            }
        }

        return false;
    }

    public void postUpdate() {
        for (SubSystem subSystem : subSystems) {
            subSystem.postUpdate();
        }
    }

    public void draw(Canvas canvas) {
        CoordinateSystem.setScrDimension(new Vector2D(canvas.getWidth(), canvas.getHeight()));

        for (SubSystem subSystem : subSystems) {
            subSystem.draw(canvas);
        }
    }

    public SpriteSystem getSpriteSystem() {
        return spriteSystem;
    }

    public WeaponSystem getWeaponSystem() {
        return weaponSystem;
    }

    public BuildingSystem getBuildingSystem() {
        return buildingSystem;
    }

    public ResourceSystem getResourceSystem() {
        return resourceSystem;
    }

    public void start_init(Context context, Canvas canvas) {
        this.context = context;
        this.canvas = canvas;
        if (!initThreadStarted) {
            initThreadStarted = true;
            this.start();
        }
    }

    public HasLifeAbstractSprite getNearestInjuredSprite(Vector2D centerPos, int teamNumber, int distance) {
        List<HasLifeAbstractSprite> distanceSortedSpriteList = new ArrayList<>();
        for (ActorSprite sprite : this.spriteSystem.getAllSprites()) {
            if (sprite.getTeamNumber() == teamNumber) {
                if (sprite.getSpritePos().distSquare(centerPos) < distance * distance) {
                    if (sprite.isInjured())
                        distanceSortedSpriteList.add(sprite);
                }
            }
        }

        if (distanceSortedSpriteList.size() == 0) {
            return null;
        }

        Collections.sort(distanceSortedSpriteList, new Utilities.SpriteDistanceComparator(centerPos));

        return distanceSortedSpriteList.get(0);
    }

    public HasLifeAbstractSprite getNearestEnermySprite(Vector2D centerPos, int teamNumber, int distance) {
        if(centerPos == null)
            return null;

        List<HasLifeAbstractSprite> distanceSortedSpriteList = new ArrayList<>();
        for (ActorSprite sprite : this.spriteSystem.getAllSprites()) {
            if (sprite.getTeamNumber() != teamNumber) {
                if (sprite.getSpritePos().distSquare(centerPos) < distance * distance) {
                    distanceSortedSpriteList.add(sprite);
                }
            }
        }

        for (Building building : this.buildingSystem.getDestroyableBuildings()) {
            if (building.getTeamNumber() != teamNumber) {
                if (building.getSpritePos().distSquare(centerPos) < distance * distance) {
                    distanceSortedSpriteList.add(building);
                }
            }
        }

        if (distanceSortedSpriteList.size() == 0) {
            return null;
        }

        Collections.sort(distanceSortedSpriteList, new Utilities.SpriteDistanceComparator(centerPos));
        return distanceSortedSpriteList.get(0);
    }
}
