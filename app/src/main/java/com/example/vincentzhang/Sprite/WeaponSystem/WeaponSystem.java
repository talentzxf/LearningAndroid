package com.example.vincentzhang.Sprite.WeaponSystem;

import android.content.res.Resources;
import android.graphics.Canvas;

import com.example.vincentzhang.Sprite.AbstractSprite;
import com.example.vincentzhang.Sprite.CoordinateSystem;
import com.example.vincentzhang.Sprite.ImageSprite;
import com.example.vincentzhang.Sprite.Vector2D;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by VincentZhang on 4/24/2017.
 */

public class WeaponSystem {

    private ConcurrentLinkedQueue<AbstractSprite> bombs = new ConcurrentLinkedQueue<>();

    public boolean init(String level, Resources resources, Canvas canvas){
//        XPath xPath = XPathFactory.newInstance().newXPath();
//        try {
//            NodeList weaponDefs = (NodeList) xPath.evaluate("/weapons/node()", getXmlSource(resources, level), XPathConstants.NODESET);
//            for(int i = 0 ; i < weaponDefs.getLength(); i++){
//
//            }
//
//        } catch (XPathExpressionException e) {
//            Log.e("XPath error", "Error!");
//            return false;
//        }
        return true;
    }

    public void addBomb(double gridX, double gridY){
        Bomb bomb = new Bomb();
        bomb.setSpritePos(CoordinateSystem.gridToWorld(new Vector2D(gridX, gridY)));
        this.bombs.add(bomb);
    }

    public void draw(Canvas canvas){
        for(AbstractSprite bomb:bombs){
            bomb.draw(canvas);
        }
    }

    public void detectCollide(ImageSprite imgSprite) {
        for(AbstractSprite bomb:bombs){
            bomb.detectCollide(imgSprite);
        }
    }
}
