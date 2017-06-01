package com.example.vincentzhang.Sprite.TerrainSystem;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.util.Log;

import com.example.vincentzhang.Sprite.AbstractSprite;
import com.example.vincentzhang.Sprite.Controller.ControllerFactory;
import com.example.vincentzhang.Sprite.ImageSprite;
import com.example.vincentzhang.Sprite.SubSystem;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import static com.example.vincentzhang.Sprite.Utilities.getXmlSource;

/**
 * Created by VincentZhang on 4/23/2017.
 */

public class BuildingSystem implements SubSystem{
    private ConcurrentLinkedQueue<Building> buildings = new ConcurrentLinkedQueue<>();
    private ConcurrentLinkedQueue<Building> destroyableBuildings = new ConcurrentLinkedQueue<>();

    @Override
    public boolean init(String level, Resources resources, Canvas canvas) {
        XPath xPath = XPathFactory.newInstance().newXPath();
        try {
            NodeList buildingNodes = (NodeList) xPath.evaluate("/game/buildings/building", getXmlSource(resources, level), XPathConstants.NODESET);
            for(int i = 0 ; i < buildingNodes.getLength(); i++){
                Node buildingNode = buildingNodes.item(i);
                int imgId = Integer.valueOf(buildingNode.getAttributes().getNamedItem("imgId").getNodeValue());
                int gridX = Integer.valueOf(buildingNode.getAttributes().getNamedItem("x").getNodeValue());
                int gridY = Integer.valueOf(buildingNode.getAttributes().getNamedItem("y").getNodeValue());

                Node controllerNode = buildingNode.getAttributes().getNamedItem("controller");
                String controller = controllerNode == null ? null: controllerNode.getNodeValue();
                Building newBuilding = new Building(imgId, gridX, gridY);

                Node destroyableNode = buildingNode.getAttributes().getNamedItem("destroyable");
                if(destroyableNode != null){
                    boolean destroyable = Boolean.valueOf(destroyableNode.getNodeValue());
                    newBuilding.setDestroyable(destroyable);
                }

                if(controller != null){
                    ControllerFactory.createController(controller, newBuilding);
                }

                buildings.add(newBuilding);
                if(newBuilding.isDestroyable()){
                    destroyableBuildings.add(newBuilding);
                }
            }
        } catch (XPathExpressionException e) {
            Log.e("Xpath expression error:", "Error!");
            return false;
        }
        return true;
    }

    public void draw(Canvas canvas) {
        for(Building building : buildings){
            building.draw(canvas);
        }
    }

    public void beforeCollision(){
        for(Building building : buildings){
            building.beforeCollision();
        }
    }

    @Override
    public void preUpdate() {

    }

    @Override
    public void postUpdate() {
        ArrayList<Building> deletedBuildings = new ArrayList<>();
        for(Building building: buildings){
            building.postUpdate();
            if(building.isDead()){
                deletedBuildings.add(building);
            }
        }

        for(Building tobeDeletedBuilding : deletedBuildings){
            buildings.remove(tobeDeletedBuilding);
            this.destroyableBuildings.remove(tobeDeletedBuilding);
        }
    }

    @Override
    public AbstractSprite detectCollide(ImageSprite target) {
        for(Building building: buildings){
            if(building.detectCollide(target)){
                return building;
            }
        }
        return null;
    }

    public void addBuilding(Building newBuilding) {
        buildings.add(newBuilding);
    }

    public ConcurrentLinkedQueue<Building> getDestroyableBuildings() {
        return destroyableBuildings;
    }
}
