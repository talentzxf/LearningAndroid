package com.example.vincentzhang.Sprite.SpriteSystem;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.util.Log;

import com.example.vincentzhang.Sprite.AbstractSprite;
import com.example.vincentzhang.Sprite.ActorSprite;
import com.example.vincentzhang.Sprite.Controller.ControllerFactory;
import com.example.vincentzhang.Sprite.ImageSprite;
import com.example.vincentzhang.Sprite.SubSystem;
import com.example.vincentzhang.Sprite.Vector2D;
import com.example.vincentzhang.Sprite.WeaponSystem.WeaponSystem;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import static com.example.vincentzhang.Sprite.Utilities.getXmlSource;

/**
 * Created by VincentZhang on 5/20/2017.
 */

public class SpriteSystem implements SubSystem {
    private Map<String, ImageSprite> spriteMap = new HashMap<>();
    private WeaponSystem weaponSystem;
    private ImageSprite leadingSprite;

    public void setWeaponSystem(WeaponSystem weaponSystem){
        this.weaponSystem = weaponSystem;
    }
    @Override
    public boolean init(String level, Resources resources, Canvas canvas) {
        XPath xPath = XPathFactory.newInstance().newXPath();
        try {
            NodeList spriteNodes = (NodeList) xPath.evaluate("/game/sprites/sprite", getXmlSource(resources, level), XPathConstants.NODESET);
            for(int i = 0 ; i < spriteNodes.getLength(); i++){
                Node spriteNode = spriteNodes.item(i);
                String name = spriteNode.getAttributes().getNamedItem("name").getNodeValue();

                int imgId = Integer.valueOf(spriteNode.getAttributes().getNamedItem("imgId").getNodeValue());
                float posX = Float.valueOf(spriteNode.getAttributes().getNamedItem("x").getNodeValue());
                float posY = Float.valueOf(spriteNode.getAttributes().getNamedItem("y").getNodeValue());

                boolean isLeading = Boolean.valueOf(spriteNode.getAttributes().getNamedItem("isleading").getNodeValue());
                String controller = spriteNode.getAttributes().getNamedItem("controller").getNodeValue();
                int teamNumber = Integer.valueOf(spriteNode.getAttributes().getNamedItem("team").getNodeValue());

                ActorSprite sprite = new ActorSprite(imgId);
                sprite.setSpritePos(new Vector2D(posX, posY));
                sprite.setTeamNumber(teamNumber);
                spriteMap.put(name, sprite);

                if(weaponSystem != null)
                    ControllerFactory.createController(controller, sprite, weaponSystem);

                if(isLeading){
                    leadingSprite = sprite;
                }

            }
        } catch (XPathExpressionException e) {
            Log.e("Xpath expression error:", "Error!");
            return false;
        }
        return true;
    }

    public ImageSprite getLeadingSprite(){
        return leadingSprite;
    }

    public Collection<ImageSprite> getAllSprites(){
        return this.spriteMap.values();
    }

    @Override
    public void draw(Canvas canvas) {
        for(ImageSprite sprite : spriteMap.values() ){
            sprite.draw(canvas);
        }
    }

    @Override
    public void beforeCollision() {
        for(ImageSprite sprite : spriteMap.values() ){
            sprite.beforeCollision();
        }
    }

    @Override
    public AbstractSprite detectCollide(ImageSprite imgSprite) {
        for(ImageSprite sprite : spriteMap.values()){
            // No need to collide self
            if(sprite != imgSprite){
                sprite.detectCollide(imgSprite);
            }
        }

        // No need to populate the change.
        return null;
    }

    @Override
    public void preUpdate() {
        for(ImageSprite sprite : spriteMap.values() ){
            sprite.preUpdate();
        }
    }

    @Override
    public void postUpdate() {
        for(ImageSprite sprite : spriteMap.values() ){
            sprite.postUpdate();
        }
    }
}
