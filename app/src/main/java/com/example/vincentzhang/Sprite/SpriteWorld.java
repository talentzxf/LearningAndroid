package com.example.vincentzhang.Sprite;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;

import com.example.vincentzhang.learnandroid.R;

import org.xml.sax.InputSource;

import java.util.ArrayList;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import static com.example.vincentzhang.Sprite.CoordinateSystem.getViewPortPos;

/**
 * Created by VincentZhang on 4/15/2017.
 */

public class SpriteWorld {
    private ImageSprite imgSprite = new ImageSprite();
    private ArrayList<ArrayList<Integer>> mapData = new ArrayList<>();
    private static final int VIEWPORT_MARGIN = 100;

    /**
     * TODO: 1. Terrain should be move to a separate class. 2. Add more types. 3. Make it more customizable. 4. Culling invisible elements.
     */
    private Bitmap normalTerrain;
    private Bitmap normalBaldTerrain;

    private boolean loadMap(Context context) {
        String level1 = "level1.xml";
        Log.i("Begin to load resource:", level1);


        // Read data from resource file.
        try {
            InputSource inputSource = new InputSource(context.getResources().openRawResource(R.raw.level1));

            XPath xPath = XPathFactory.newInstance().newXPath();
            String mapDataStrings = xPath.evaluate("//game/map", inputSource).trim();
            String[] lines = mapDataStrings.split("\n");

            for (String line : lines) {
                ArrayList<Integer> lineData = new ArrayList<>();
                String[] dataString = line.trim().split("\\s+");
                for (String elem : dataString) {
                    lineData.add(Integer.valueOf(elem));
                }
                mapData.add(lineData);
            }

            Log.i("End of loading file:", "res/xml/level1.xml:" + mapData);

        } catch (XPathExpressionException e) {
            Log.i("XPath expression wrong", "Xpath wrong?", e);
            return false;
        }

        return true;
    }

    public boolean init(Context context) {
        loadMap(context);
        normalTerrain = BitmapFactory.decodeResource(context.getResources(), R.drawable.normal_terrain);
        normalBaldTerrain = BitmapFactory.decodeResource(context.getResources(), R.drawable.normal_bald_terrain);
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

    private void drawTerrain(Canvas canvas) {
        int tileWidth = normalBaldTerrain.getScaledWidth(canvas);
        int tileHeight = normalBaldTerrain.getScaledHeight(canvas);
        CoordinateSystem.setTileDimension(new Vector2D(tileWidth, tileHeight));
        Log.i("Tilewidth:", Integer.toString(tileWidth) + ":" + Integer.toString(tileHeight));

        for (int y = 0; y < mapData.size(); y++) {
            for (int x = 0; x < mapData.get(y).size(); x++) {
                int realWorld_x = x * tileWidth / 2;
                int realWorld_y = y * tileHeight - x * tileHeight / 2;

                int scr_x = (int) (realWorld_x - getViewPortPos().getX());
                int scr_y = (int) (realWorld_y - getViewPortPos().getY());
                switch (mapData.get(y).get(x)) {
                    case 1:
                        canvas.drawBitmap(normalTerrain, scr_x, scr_y, null);
                        break;
                    case 2:
                        canvas.drawBitmap(normalBaldTerrain, scr_x, scr_y, null);
                        break;
                }
            }
        }
    }

    public void draw(Canvas canvas) {
        CoordinateSystem.setScrDimension(new Vector2D(canvas.getWidth(), canvas.getHeight()));

        drawTerrain(canvas);
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
