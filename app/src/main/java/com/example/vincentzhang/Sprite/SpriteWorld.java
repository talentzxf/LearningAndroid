package com.example.vincentzhang.Sprite;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;

import com.example.vincentzhang.learnandroid.R;

import org.xml.sax.InputSource;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

/**
 * Created by VincentZhang on 4/15/2017.
 */

public class SpriteWorld {
    ImageSprite imgSprite = new ImageSprite();
    public boolean init(Context context){
        imgSprite.load(BitmapFactory.decodeResource(context.getResources(), R.drawable.green));
        String level1 = "level1.xml";
        Log.i("Begin to load resource:" , level1);
        try {
            InputSource inputSource = new InputSource(context.getResources().openRawResource(R.raw.level1));

            XPath xPath = XPathFactory.newInstance().newXPath();
            String mapData = xPath.evaluate("//game/map", inputSource);

            Log.i("End of loading file:", "res/xml/level1.xml:" + mapData);
        } catch (XPathExpressionException e) {
            Log.i("XPathexpressionwrong", "Xpath wrong?", e);
        }
        return true;
    }

    public void update(){
        imgSprite.update();;
    }

    public void draw(Canvas canvas){
        imgSprite.draw(canvas);
    }
}
