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

/**
 * Created by VincentZhang on 4/15/2017.
 */

public class SpriteWorld {
    private ImageSprite imgSprite = new ImageSprite();
    private ArrayList<ArrayList<Integer>> mapData = new ArrayList<>();

    /**
     * TODO: 1. Terrain should be move to a separate class. 2. Add more types. 3. Make it more customizable. 4. Culling invisible elements.
     */
    private Bitmap normalTerrain;
    private Bitmap normalBaldTerrain;

    private int viewPortX = 0;
    private int viewPortY = 0;
    private int scrWidth = -1;
    private int scrHeight = -1;
    private int tileWidth = -1;
    private int tileHeight = -1;

    private boolean viewPortMoving = false;

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

    public void update() {
        imgSprite.update();

        if(this.viewPortMoving == true){
            int speed = 10;
            switch(imgSprite.getCurDirection()){
                case DOWN:
                    viewPortY += speed;
                    break;
                case UP:
                    viewPortY -= speed;
                    break;
                case LEFT:
                    viewPortX -= speed;
                    break;
                case RIGHT:
                    viewPortX += speed;
                    break;
            }
        }
    }

    private void drawTerrain(Canvas canvas) {
        tileWidth = normalBaldTerrain.getScaledWidth(canvas);
        tileHeight = normalBaldTerrain.getScaledHeight(canvas);

        imgSprite.setSpriteDim(tileWidth, tileHeight);

        Log.i("Tilewidth:", Integer.toString(tileWidth) + ":" + Integer.toString(tileHeight));

        for (int y = 0; y < mapData.size(); y++) {
            for (int x = 0; x < mapData.get(y).size(); x++) {
                int realWorld_x = x * tileWidth/2;
                int realWorld_y = y * tileHeight - x*tileHeight/2;

                int scr_x = realWorld_x - viewPortX;
                int scr_y = realWorld_y - viewPortY;
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
        scrWidth = canvas.getWidth();
        scrHeight = canvas.getHeight();

        drawTerrain(canvas);
        imgSprite.draw(canvas);
    }

    public void onClick(DIRECTIONS dir) {
        if(dir == DIRECTIONS.UNKNOWN){
            viewPortMoving = false;
        }else{
            imgSprite.setCurDirection(dir);
            viewPortMoving = true;
        }
    }
}
