package com.example.vincentzhang.Sprite;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.example.vincentzhang.Sprite.Terrain.TerrainSystem;
import com.example.vincentzhang.Sprite.WeaponSystem.WeaponSystem;
import com.example.vincentzhang.Sprite.imgemanagement.ImageManager;
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

    public boolean inited(){
        return inited;
    }

    public boolean init(Context context, Canvas canvas) {
        String level = "level1";
        ImageManager.inst().init(level, context.getResources(), canvas);

        weaponSystem.init(level,context.getResources(), canvas);

        // TODO: Don't hard code imgId here!
        imgSprite = new ImageSprite(7);
        imgSprite.load(BitmapFactory.decodeResource(context.getResources(), R.drawable.green));

        terrainSystem.init("level1", context.getResources(), canvas);
        inited = true;
        return true;
    }

    private long steps = 0;

    public void preUpdate(){
        updateViewPort();
        imgSprite.preUpdate();
    }

    public void beforeCollision() {
        imgSprite.beforeCollision();
        this.terrainSystem.beforeCollision();
        this.weaponSystem.beforeCollision();
    }

    private void updateViewPort(){

        // Update view port, to at least leave 1 tile to the sprite
        Vector2D curViewPort = CoordinateSystem.getViewPortPos();
        Vector2D curSpritePos = imgSprite.getSpritePos();
        Vector2D scrDim = CoordinateSystem.getScrDimension();

        Rect spriteRect = imgSprite.getScrRect();

        if(scrDim == null || spriteRect == null){ // CoordinateSystem not inited yet.
            return ;
        }
        int spriteWidth = spriteRect.width();
        int spriteHeight = spriteRect.height();

        if(spriteHeight == 0 || spriteWidth == 0){
            return;
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

    /**
     *
     * @return need reprocess or not.
     */
    public boolean processCollision(){
        if(CollideDetector.isDirtyFlag()){
            if( null != terrainSystem.detectCollide(imgSprite) )
                return true;
            else if(null != weaponSystem.detectCollide(imgSprite))
                return true;
        }

        return false;
    }

    public void postUpdate(){
        imgSprite.postUpdate();
        weaponSystem.postUpdate();
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
        CollideDetector.setDirtyFlag(true);
    }

    public void onClick(Character but){
        switch(but){
            case 'A':
                Vector2D spritePos = imgSprite.getSpritePos();
                Vector2D gridPos = CoordinateSystem.worldToGrid(spritePos);
                weaponSystem.addBomb(gridPos.getX(), gridPos.getY());
                break;
        }
        CollideDetector.setDirtyFlag(true);
    }
}
