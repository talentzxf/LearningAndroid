package com.example.vincentzhang.Sprite.Terrain;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

import com.example.vincentzhang.Sprite.CoordinateSystem;
import com.example.vincentzhang.Sprite.Vector2D;
import com.example.vincentzhang.learnandroid.R;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import static com.example.vincentzhang.Sprite.CoordinateSystem.getViewPortPos;

/**
 * Created by VincentZhang on 4/22/2017.
 */

public class TerrainSystem {
    private boolean inited = false;
    private ArrayList<ArrayList<Integer>> mapData = new ArrayList<>();
    private Map<Integer, Bitmap> imgMap = new HashMap<>();

    public static int getId(String resourceName, Class<?> c) {
        try {
            Field idField = c.getDeclaredField(resourceName);
            return idField.getInt(idField);
        } catch (Exception e) {
            throw new RuntimeException("No resource ID found for: "
                    + resourceName + " / " + c, e);
        }
    }

    public boolean init(String xml, Resources resources) {
        Log.i("Begin to load resource:", xml);

        // Read data from resource file.
        try {
            InputSource inputSource = new InputSource(resources.openRawResource(R.raw.level1));

            XPath xPath = XPathFactory.newInstance().newXPath();
            String mapDataStrings = xPath.evaluate("/game/map", inputSource).trim();
            String[] lines = mapDataStrings.split("\n");

            for (String line : lines) {
                ArrayList<Integer> lineData = new ArrayList<>();
                String[] dataString = line.trim().split("\\s+");
                for (String elem : dataString) {
                    lineData.add(Integer.valueOf(elem));
                }
                mapData.add(lineData);
            }

            inputSource = new InputSource(resources.openRawResource(R.raw.level1));
            NodeList imgMaps = (NodeList) xPath.evaluate("/game/imgs/img", inputSource, XPathConstants.NODESET);
            for (int nodeIdx = 0; nodeIdx < imgMaps.getLength(); nodeIdx++) {
                Node imgNode = imgMaps.item(nodeIdx);
                String src = imgNode.getAttributes().getNamedItem("src").getNodeValue();
                Integer imgId = Integer.valueOf(imgNode.getAttributes().getNamedItem("src").getNodeValue());

                Bitmap imgBM = BitmapFactory.decodeResource(resources, getId(src, R.drawable.class));
                imgMap.put(imgId, imgBM);
            }

            Log.i("End of loading file:", "res/xml/level1.xml:" + mapData);
            inited = true;

        } catch (XPathExpressionException e) {
            Log.i("XPath expression wrong", "Xpath wrong?", e);
            return false;
        }

        return true;
    }

    public int getScaledTileWidth(Canvas canvas) {
        return imgMap.get(0).getScaledWidth(canvas);
    }

    public int getScaledTileHeight(Canvas canvas) {
        return imgMap.get(0).getScaledHeight(canvas);
    }

    public void draw(Canvas canvas) {
        int tileWidth = getScaledTileWidth(canvas);
        int tileHeight = getScaledTileHeight(canvas);
        CoordinateSystem.setTileDimension(new Vector2D(tileWidth, tileHeight));
        Log.i("Tilewidth:", Integer.toString(tileWidth) + ":" + Integer.toString(tileHeight));

        for (int y = 0; y < mapData.size(); y++) {
            for (int x = 0; x < mapData.get(y).size(); x++) {
                int realWorld_x = x * tileWidth / 2;
                int realWorld_y = y * tileHeight - x * tileHeight / 2;

                int scr_x = (int) (realWorld_x - getViewPortPos().getX());
                int scr_y = (int) (realWorld_y - getViewPortPos().getY());

                int imgId = mapData.get(y).get(x);
                Bitmap bm = imgMap.get(imgId);
                if (bm != null)
                    canvas.drawBitmap(bm, new Rect(0, 0, bm.getWidth(), bm.getHeight()),
                            new Rect(scr_x, scr_y, scr_x + tileWidth, scr_y + tileHeight), null);
            }
        }
    }

}

