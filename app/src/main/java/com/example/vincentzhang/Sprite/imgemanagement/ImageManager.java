package com.example.vincentzhang.Sprite.imgemanagement;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;

import com.example.vincentzhang.Sprite.DIRECTIONS;
import com.example.vincentzhang.learnandroid.R;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import static com.example.vincentzhang.Sprite.Utilities.getId;
import static com.example.vincentzhang.Sprite.Utilities.getXmlSource;

/**
 * Created by VincentZhang on 4/23/2017.
 * Singleton
 */
public class ImageManager {
    private Map<Integer, Bitmap> imgMap = new HashMap<>();
    private Map<Integer, Space4DTree> space4DTreeMap = new HashMap<>();
    private Map<Integer, ArrayList<DIRECTIONS>> dirArrayMap = new HashMap<>();
    private Map<Integer, Float> imgSizeScaleMap = new HashMap<>();

    private static ImageManager instance = new ImageManager();

    private ImageManager() {
    }

    public static ImageManager inst(){
        return instance;
    }

    public boolean init(String level, Resources resources, Canvas canvas) {
        XPath xPath = XPathFactory.newInstance().newXPath();
        NodeList imgMaps = null;
        try {
            imgMaps = (NodeList) xPath.evaluate("/game/imgs/img", getXmlSource(resources, level), XPathConstants.NODESET);
        } catch (XPathExpressionException e) {
            Log.e("XPath expression error!", "Unknown xpath expression!");
            return false;
        }

        for (int nodeIdx = 0; nodeIdx < imgMaps.getLength(); nodeIdx++) {
            Node imgNode = imgMaps.item(nodeIdx);
            String src = imgNode.getAttributes().getNamedItem("src").getNodeValue();
            Node node = imgNode.getAttributes().getNamedItem("rowCount");
            String rowCountStr = node == null ? "1": node.getNodeValue();
            node = imgNode.getAttributes().getNamedItem("colCount");
            String colCountStr = node == null ? "1":node.getNodeValue();
            Integer rowCount = rowCountStr == null?1:Integer.valueOf(rowCountStr);
            Integer colCount = colCountStr == null?1:Integer.valueOf(colCountStr);
            Node dirArrayNode = imgNode.getAttributes().getNamedItem("directions");
            String dirArray = dirArrayNode == null? "DOWN,RIGHT,UP,LEFT,DOWNLEFT,DOWNRIGHT,UPLEFT,UPRIGHT":dirArrayNode.getNodeValue();
            Node sizeScaleNode = imgNode.getAttributes().getNamedItem("scale");
            Float sizeScale = sizeScaleNode == null? 1.0f:Float.valueOf(sizeScaleNode.getNodeValue());

            Log.i("Initing img:", "id:" + src);
            Integer imgId = Integer.valueOf(imgNode.getAttributes().getNamedItem("id").getNodeValue());

            Bitmap imgBM = BitmapFactory.decodeResource(resources, getId(src, R.drawable.class));

            Space4DTree space4DTree = new Space4DTree(imgId, imgBM, rowCount, colCount);
            imgMap.put(imgId, imgBM);
            space4DTreeMap.put(imgId, space4DTree);
            imgSizeScaleMap.put(imgId, sizeScale);

            ArrayList<DIRECTIONS> dirList = new ArrayList<>();
            for(String dirStr : dirArray.split(",") ){
                dirList.add(DIRECTIONS.valueOf(dirStr));
            }

            dirArrayMap.put(imgId, dirList);

            Log.i("End of init img:", "id:" + src);
        }
        return true;
    }

    public Bitmap getImg(int id) {
        return imgMap.get(id);
    }
    public Space4DTree getSpace4DTree(int id){
        return space4DTreeMap.get(id);
    }

    public ArrayList<DIRECTIONS> getDirectionArray(int id){
        return dirArrayMap.get(id);
    }

    public float getSizeScale(int imgId) {
        Float sizeScale = imgSizeScaleMap.get(imgId);
        if(sizeScale == null){
            return 1.0f;
        }
        return sizeScale;
    }
}
