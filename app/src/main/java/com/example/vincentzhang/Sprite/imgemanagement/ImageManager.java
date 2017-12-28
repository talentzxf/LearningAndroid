package com.example.vincentzhang.Sprite.imgemanagement;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;

import com.example.vincentzhang.Sprite.DIRECTIONS;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import com.example.vincentzhang.learnandroid.R;

import static com.example.vincentzhang.Sprite.Utilities.getId;
import static com.example.vincentzhang.Sprite.Utilities.getXmlSource;

/**
 * Created by VincentZhang on 4/23/2017.
 * Singleton
 */
public class ImageManager {

    // Key is Image Id. Formate: img_id
    private Map<Integer, SpriteImage> imgMap = new HashMap<>();

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

            SpriteImage spriteImage = new SpriteImage();
            String[] srcList = src.split(",");
            for(int i = 0 ; i < srcList.length; i++){
                String subSrc = srcList[i];
                Bitmap imgBM = BitmapFactory.decodeResource(resources, getId(subSrc, R.drawable.class));

                String stringImgId = null;
                if(srcList.length == 1){
                    stringImgId = Integer.toString(imgId);
                } else {
                    stringImgId = imgId + "_" + i;
                }

                Space4DTree space4DTree = new Space4DTree(stringImgId, imgBM, rowCount, colCount);

                spriteImage.setScale(sizeScale);

                ArrayList<DIRECTIONS> dirList = new ArrayList<>();
                for(String dirStr : dirArray.split(",") ){
                    dirList.add(DIRECTIONS.valueOf(dirStr));
                }
                spriteImage.addBitImage(imgBM,space4DTree,dirList);

                imgMap.put(imgId, spriteImage);
            }

            Log.i("End of init img:", "id:" + src);
        }
        return true;
    }

    public SpriteImage getImg(int id) {
        return this.imgMap.get(id);
    }
}
