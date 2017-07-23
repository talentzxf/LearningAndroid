package com.example.vincentzhang.Sprite.XMLUtilities;

import android.annotation.TargetApi;
import android.os.Build;

import org.apache.commons.beanutils.BeanUtils;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;


/**
 * Created by VincentZhang on 7/19/2017.
 */

public class XMLRefactorDecoder {
    @TargetApi(Build.VERSION_CODES.N)
    static public Object decode(Node xmlNode, Class clz) {

        Object newInstance = null;
        try {
            newInstance = clz.newInstance();

            NamedNodeMap attributeMap = xmlNode.getAttributes();
            // Set all attributes
            for (int idx = 0; idx < attributeMap.getLength(); idx++) {
                Node attribute = attributeMap.item(idx);
                String attributeName = attribute.getLocalName();
                String attributeValue = attribute.getNodeValue();
                BeanUtils.setProperty(newInstance, attributeName, attributeValue);
            }

            // Set all child nodes
            NodeList nodeList = xmlNode.getChildNodes();
            for (int idx = 0; idx < nodeList.getLength(); idx++) {
                Node node = nodeList.item(idx);
                // TODO: use factory method
                // This is a list
                if (node.getNodeName() == "list") {
                    NamedNodeMap listAttributeMap = node.getAttributes();
                    String propertyName = listAttributeMap.getNamedItem("propertyName").getNodeValue();
                    Field field = clz.getDeclaredField(propertyName);

                    XMLElementType annotatedType = (XMLElementType) field.getDeclaredAnnotation(XMLElementType.class);
                    Class elementType = annotatedType.value();

                    NodeList nodes = node.getChildNodes();
                    ArrayList arrayList = decode(nodes, elementType);
                }
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
            return null;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            return null;
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return newInstance;
    }

    static public ArrayList decode(NodeList xmlNodeList, Class clz) {
        ArrayList retArray = new ArrayList();
        for (int i = 0; i < xmlNodeList.getLength(); i++) {
            Node node = xmlNodeList.item(i);
            if(node.getNodeType() == Node.ELEMENT_NODE){
                Object newObj = decode(node, clz);
                retArray.add(newObj);
            }
        }

        return retArray;
    }
}
