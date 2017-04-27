package com.example.vincentzhang.Sprite;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.example.vincentzhang.Sprite.Terrain.TerrainSystem;
import com.example.vincentzhang.Sprite.WeaponSystem.WeaponSystem;
import com.example.vincentzhang.learnandroid.R;

/**
 * Created by VincentZhang on 4/15/2017.
 */

public class SpriteWorld {

    private ImageSprite imgSprite;
    private static final int VIEWPORT_MARGIN = 100;

    private boolean inited = false;

    /**
     * TODO: 1. Terrain should be move to a separate class. 2. Add more types. 3. Make it more customizable. 4. Culling invisible elements.
     */
    private TerrainSystem terrainSystem = new TerrainSystem();
    private WeaponSystem weaponSystem = new WeaponSystem();

    private boolean loadMap(Context context, Canvas canvas) {
        String level = "level1";
        ImageManager.inst().init(level, context.getResources());

        // TODO: Don't hard code here!
        imgSprite = new ImageSprite(7);
        weaponSystem.init(level,context.getResources(), canvas);
        return terrainSystem.init("level1", context.getResources(), canvas);
    }

    public boolean inited(){
        return inited;
    }

    public boolean init(Context context, Canvas canvas) {
        loadMap(context, canvas);
        imgSprite.load(BitmapFactory.decodeResource(context.getResources(), R.drawable.green));

        inited = true;
        return true;
    }

    private long steps = 0;

    public void preUpdate() {
        steps++;
        if (steps % 2 == 0)
            imgSprite.preUpdate();

        // Update view port, to at least leave 1 tile to the sprite
        Vector2D curViewPort = CoordinateSystem.getViewPortPos();
        Vector2D curSpritePos = imgSprite.getSpritePos();
        Vector2D scrDim = CoordinateSystem.getScrDimension();

        int spriteWidth = imgSprite.getSpriteWidth();
        int spriteHeight = imgSprite.getSpriteHeight();

        if(scrDim == null || spriteWidth == -1 || spriteHeight == -1 ){ // CoordinateSystem not inited yet.
            return ;
        }

        Vector2D newViewPortPos = curViewPort;
        if(curSpritePos.getX() - curViewPort.getX() < VIEWPORT_MARGIN){
            newViewPortPos.setX( curSpritePos.getX() - VIEWPORT_MARGIN);
        }

        if(curSpritePos.getX() + spriteWidth > curViewPort.getX() + scrDim.getX() - VIEWPORT_MARGIN){
            newViewPortPos.setX( curSpritePos.getX() + spriteWidth + VIEWPORT_MARGIN - scrDim.getX());
        }

        if(curSpritePos.getY() - curViewPort.getY() < VIEWPORT_MARGIN){
            newViewPortPos.setY( curSpritePos.getY() - VIEWPORT_MARGIN);
        }

        if(curSpritePos.getY() + spriteHeight > curViewPort.getY() + scrDim.getY() - VIEWPORT_MARGIN){
            newViewPortPos.setY( curSpritePos.getY() + VIEWPORT_MARGIN - scrDim.getY() + spriteHeight);
        }

        CoordinateSystem.setViewPortPos(newViewPortPos);
    }

    public void postUpdate(){
        imgSprite.postUpdate();
    }

    public void draw(Canvas canvas) {
        CoordinateSystem.setScrDimension(new Vector2D(canvas.getWidth(), canvas.getHeight()));

        terrainSystem.draw(canvas);
        weaponSystem.draw(canvas);
        imgSprite.draw(canvas);
    }

    public void onClick(DIRECTIONS dir) {
        if (dir == DIRECTIONS.UNKNOWN) {
            imgSprite.setMoving(false);
        } else {
            imgSprite.setCurDirection(dir);
            imgSprite.setMoving(true);
        }
    }

    public void onClick(Character but){
        switch(but){
            case 'A':
                Vector2D spritePos = imgSprite.getSpritePos();
                Vector2D gridPos = CoordinateSystem.worldToGrid(spritePos);
                weaponSystem.addBomb(gridPos.getX(), gridPos.getY());
                break;
        }
    }
}
