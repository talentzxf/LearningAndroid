package com.example.vincentzhang.Sprite;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.vincentzhang.learnandroid.R;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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
    private static ImageManager instance = new ImageManager();

    private ImageManager() {
    }

    public static ImageManager inst(){
        return instance;
    }

    public boolean init(String level, Resources resources) {
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
            Integer imgId = Integer.valueOf(imgNode.getAttributes().getNamedItem("id").getNodeValue());

            Bitmap imgBM = BitmapFactory.decodeResource(resources, getId(src, R.drawable.class));
            imgMap.put(imgId, imgBM);
        }
        return true;
    }

    public Bitmap getImg(int id) {
        return imgMap.get(id);
    }
}
