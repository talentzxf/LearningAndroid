package com.example.vincentzhang.Sprite.Terrain;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.util.Log;

import com.example.vincentzhang.Sprite.Vector2D;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.HashMap;
import java.util.Map;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import static com.example.vincentzhang.Sprite.Utilities.getXmlSource;

/**
 * Created by VincentZhang on 4/23/2017.
 */

public class BuildingSystem {
    private Map<Vector2D, Building> buildings = new HashMap<>();

    public boolean init(String level, Resources resources) {
        XPath xPath = XPathFactory.newInstance().newXPath();
        try {
            NodeList buildingNodes = (NodeList) xPath.evaluate("/game/buildings/building", getXmlSource(resources, level), XPathConstants.NODESET);
            for(int i = 0 ; i < buildingNodes.getLength(); i++){
                Node buildingNode = buildingNodes.item(i);
                int imgId = Integer.valueOf(buildingNode.getAttributes().getNamedItem("imgId").getNodeValue());
                int gridX = Integer.valueOf(buildingNode.getAttributes().getNamedItem("x").getNodeValue());
                int gridY = Integer.valueOf(buildingNode.getAttributes().getNamedItem("y").getNodeValue());
                buildings.put(new Vector2D(gridX, gridY), new Building(imgId, gridX, gridY));
            }
        } catch (XPathExpressionException e) {
            Log.e("Xpath expression error:", "Error!");
            return false;
        }
        return true;
    }

    public void draw(Canvas canvas) {
        for(Building building : buildings.values()){
            building.draw(canvas);
        }
    }
}
