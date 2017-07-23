package com.example.vincentzhang.learnandroid;

import com.example.vincentzhang.Sprite.UI.UIInfo;
import com.example.vincentzhang.Sprite.XMLUtilities.XMLRefactorDecoder;

import junit.framework.Assert;

import org.junit.Test;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import java.io.StringReader;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

/**
 * Created by VincentZhang on 7/23/2017.
 */

public class TestXMLUtils {

    @Test
    public void testXMLRefactor(){
        String xml = "    <ui name=\"界面\">\n" +
                "        <list propertyName=\"buttonArrays\">\n" +
                "            <buttons name=\"武器\">\n" +
                "                <list propertyName=\"buttons\">\n" +
                "                    <button id=\"hospital_button\" imgId=\"11\" />\n" +
                "                    <button id=\"magicaltower_button\" imgId=\"15\" />\n" +
                "                </list>\n" +
                "            </buttons>\n" +
                "            <buttons displayname=\"建筑\">\n" +
                "                <list propertyName=\"buttons\">\n" +
                "                    <button id=\"bomb_button\" imgId=\"6\"/>\n" +
                "                </list>\n" +
                "            </buttons>\n" +
                "        </list>\n" +
                "    </ui>";

        XPath xPath = XPathFactory.newInstance().newXPath();

        try {
            InputSource source = new InputSource(new StringReader(xml));
            Node uiNode = (Node) xPath.evaluate("/ui", source, XPathConstants.NODE);
            UIInfo uiinfo = (UIInfo) XMLRefactorDecoder.decode(uiNode, UIInfo.class);
            Assert.assertEquals(uiinfo.getName(),"界面");
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }

        System.out.println("Everything good!");
    }
}
