package com.example.vincentzhang.Sprite;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.example.vincentzhang.Sprite.Terrain.TerrainSystem;
import com.example.vincentzhang.learnandroid.R;

/**
 * Created by VincentZhang on 4/15/2017.
 */

public class SpriteWorld {
    private ImageSprite imgSprite = new ImageSprite();
    private static final int VIEWPORT_MARGIN = 100;

    /**
     * TODO: 1. Terrain should be move to a separate class. 2. Add more types. 3. Make it more customizable. 4. Culling invisible elements.
     */
    private TerrainSystem terrainSystem = new TerrainSystem();

    private boolean loadMap(Context context) {
        return terrainSystem.init("level1.xml", context.getResources());
    }

    public boolean init(Context context) {
        loadMap(context);
        imgSprite.load(BitmapFactory.decodeResource(context.getResources(), R.drawable.green));

        return true;
    }

    private long steps = 0;

    public void update() {
        steps++;
        if (steps % 2 == 0)
            imgSprite.update();

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

    public void draw(Canvas canvas) {
        CoordinateSystem.setScrDimension(new Vector2D(canvas.getWidth(), canvas.getHeight()));

        terrainSystem.draw(canvas);
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
}
