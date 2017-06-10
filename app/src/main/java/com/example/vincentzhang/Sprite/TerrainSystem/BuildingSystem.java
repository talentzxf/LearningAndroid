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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
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

public class BuildingSystem implements SubSystem {
    private ConcurrentLinkedQueue<Building> buildings = new ConcurrentLinkedQueue<>();
    private ConcurrentLinkedQueue<Building> destroyableBuildings = new ConcurrentLinkedQueue<>();

    private Building createBuilding(String type, Integer imgId, Integer gridX, Integer gridY) {

        Building newBuilding = null;
        String packageName = BuildingSystem.class.getPackage().getName();

        Class buildingClz = null;
        try {
            buildingClz = Class.forName(packageName + "." + type);
            Constructor constructor = buildingClz.getDeclaredConstructor(new Class[]{Integer.class, Integer.class, Integer.class});
            newBuilding = (Building) constructor.newInstance(new Object[]{imgId, gridX, gridY});
        } catch (ClassNotFoundException e) {
            Log.e("Can't find class", "Controller not found:" + type, e);
        } catch (NoSuchMethodException e) {
            Log.e("Can't find constructor", "constructor not found:" + type, e);
        } catch (IllegalAccessException e) {
            Log.e("Constructor error", "IllegalAccessException", e);
        } catch (InstantiationException e) {
            Log.e("Constructor error", "InstantiationException", e);
        } catch (InvocationTargetException e) {
            Log.e("Constructor error", "InvocationTargetException", e);
        }

        return newBuilding;
    }

    @Override
    public boolean init(String level, Resources resources, Canvas canvas) {
        XPath xPath = XPathFactory.newInstance().newXPath();
        try {
            NodeList buildingNodes = (NodeList) xPath.evaluate("/game/buildings/building", getXmlSource(resources, level), XPathConstants.NODESET);
            for (int i = 0; i < buildingNodes.getLength(); i++) {
                Node buildingNode = buildingNodes.item(i);
                int imgId = Integer.valueOf(buildingNode.getAttributes().getNamedItem("imgId").getNodeValue());
                int gridX = Integer.valueOf(buildingNode.getAttributes().getNamedItem("x").getNodeValue());
                int gridY = Integer.valueOf(buildingNode.getAttributes().getNamedItem("y").getNodeValue());

                Node controllerNode = buildingNode.getAttributes().getNamedItem("controller");
                String controller = controllerNode == null ? null : controllerNode.getNodeValue();

                Building newBuilding = null;

                Node buildingTypeNode = buildingNode.getAttributes().getNamedItem("type");
                if (buildingTypeNode == null) {
                    newBuilding = new Building(imgId, gridX, gridY);
                } else {
                    String buildingType = buildingTypeNode.getNodeValue();
                    newBuilding = createBuilding(buildingType, imgId, gridX, gridY);
                }

                Node destroyableNode = buildingNode.getAttributes().getNamedItem("destroyable");
                if (destroyableNode != null) {
                    boolean destroyable = Boolean.valueOf(destroyableNode.getNodeValue());
                    newBuilding.setDestroyable(destroyable);
                }

                Node teamNode = buildingNode.getAttributes().getNamedItem("team");
                if (teamNode != null) {
                    int teamNumber = Integer.valueOf(teamNode.getNodeValue());
                    newBuilding.setTeamNumber(teamNumber);
                }

                if (controller != null) {
                    ControllerFactory.createController(controller, newBuilding);
                }

                addBuilding(newBuilding);
            }
        } catch (XPathExpressionException e) {
            Log.e("Xpath expression error:", "Error!");
            return false;
        }
        return true;
    }

    public void draw(Canvas canvas) {
        for (Building building : buildings) {
            building.draw(canvas);
        }
    }

    public void beforeCollision() {
        for (Building building : buildings) {
            building.beforeCollision();
        }
    }

    @Override
    public void preUpdate() {
        for (Building building : buildings) {
            building.preUpdate();
        }
    }

    @Override
    public void postUpdate() {
        ArrayList<Building> deletedBuildings = new ArrayList<>();
        for (Building building : buildings) {
            building.postUpdate();
            if (building.isDead()) {
                deletedBuildings.add(building);
            }
        }

        for (Building tobeDeletedBuilding : deletedBuildings) {
            buildings.remove(tobeDeletedBuilding);
            this.destroyableBuildings.remove(tobeDeletedBuilding);
        }
    }

    @Override
    public AbstractSprite detectCollide(ImageSprite target) {
        for (Building building : buildings) {
            if (building.detectCollide(target)) {
                return building;
            }
        }
        return null;
    }

    public void addBuilding(Building newBuilding) {
        buildings.add(newBuilding);

        if (newBuilding.isDestroyable()) {
            destroyableBuildings.add(newBuilding);
        }
    }

    public ConcurrentLinkedQueue<Building> getDestroyableBuildings() {
        return destroyableBuildings;
    }
}
