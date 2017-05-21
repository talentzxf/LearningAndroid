package com.example.vincentzhang.Sprite.TerrainSystem;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

import com.example.vincentzhang.Sprite.AbstractSprite;
import com.example.vincentzhang.Sprite.CoordinateSystem;
import com.example.vincentzhang.Sprite.ImageSprite;
import com.example.vincentzhang.Sprite.SubSystem;
import com.example.vincentzhang.Sprite.imgemanagement.ImageManager;
import com.example.vincentzhang.Sprite.Vector2D;

import java.util.ArrayList;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import static com.example.vincentzhang.Sprite.CoordinateSystem.getViewPortPos;
import static com.example.vincentzhang.Sprite.Utilities.getXmlSource;

/**
 * Created by VincentZhang on 4/22/2017.
 */

public class TerrainSystem implements SubSystem{
    private boolean inited = false;
    private ArrayList<ArrayList<Integer>> mapData = new ArrayList<>();

    private int tileDefImgId = -1;

    public boolean init(String level, Resources resources, Canvas canvas) {
        Log.i("Begin to load resource:", level);

        // Read data from resource file.
        try {
            XPath xPath = XPathFactory.newInstance().newXPath();
            String mapDataStrings = xPath.evaluate("/game/map", getXmlSource(resources, level)).trim();
            String[] lines = mapDataStrings.split("\n");

            for (String line : lines) {
                ArrayList<Integer> lineData = new ArrayList<>();
                String[] dataString = line.trim().split("\\s+");
                for (String elem : dataString) {
                    lineData.add(Integer.valueOf(elem));
                }
                mapData.add(lineData);
            }

            tileDefImgId = Integer.valueOf(xPath.evaluate("/game/map/@tile_def_imgid", getXmlSource(resources, level) ));

            int tileWidth = getScaledTileWidth(canvas);
            int tileHeight = getScaledTileHeight(canvas);
            CoordinateSystem.setTileDimension(new Vector2D(tileWidth, tileHeight));

            Log.i("End of loading file:", "res/xml/" + level +".xml:" + mapData);
            inited = true;

        } catch (XPathExpressionException e) {
            Log.i("XPath expression wrong", "Xpath wrong?", e);
            return false;
        }

        return true;
    }

    public int getScaledTileWidth(Canvas canvas) {
        return ImageManager.inst().getImg(tileDefImgId).getScaledWidth(canvas);
    }

    public int getScaledTileHeight(Canvas canvas) {
        return ImageManager.inst().getImg(tileDefImgId).getScaledHeight(canvas);
    }

    @Override
    public AbstractSprite detectCollide(ImageSprite imgSprite) {
        return null;
    }

    @Override
    public void preUpdate() {

    }

    @Override
    public void postUpdate() {

    }

    public void draw(Canvas canvas) {
        int tileWidth = getScaledTileWidth(canvas);
        int tileHeight = getScaledTileHeight(canvas);
        CoordinateSystem.setTileDimension(new Vector2D(tileWidth, tileHeight));

        // Log.i("Tilewidth:", Integer.toString(tileWidth) + ":" + Integer.toString(tileHeight));

        for (int y = 0; y < mapData.size(); y++) {
            for (int x = 0; x < mapData.get(y).size(); x++) {
                Vector2D world_pos = CoordinateSystem.gridToWorld(new Vector2D(x,y));
                int realWorld_x = (int)world_pos.getX();
                int realWorld_y = (int)world_pos.getY();

                int scr_x = (int) (realWorld_x - getViewPortPos().getX());
                int scr_y = (int) (realWorld_y - getViewPortPos().getY());

                int imgId = mapData.get(y).get(x);
                Bitmap bm = ImageManager.inst().getImg(imgId);
                if (bm != null)
                    canvas.drawBitmap(bm, new Rect(0, 0, bm.getWidth(), bm.getHeight()),
                            new Rect(scr_x, scr_y, scr_x + tileWidth, scr_y + tileHeight), null);
            }
        }
    }

    @Override
    public void beforeCollision() {
    }

}

