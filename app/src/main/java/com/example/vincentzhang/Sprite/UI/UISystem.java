package com.example.vincentzhang.Sprite.UI;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.util.Pair;

import com.example.vincentzhang.Sprite.AbstractSprite;
import com.example.vincentzhang.Sprite.ImageSprite;
import com.example.vincentzhang.Sprite.ResourceSystem.ResourceType;
import com.example.vincentzhang.Sprite.SpriteWorld;
import com.example.vincentzhang.Sprite.SubSystem;
import com.example.vincentzhang.Sprite.XMLUtilities.XMLRefactorDecoder;

import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import static com.example.vincentzhang.Sprite.Utilities.getXmlSource;

/**
 * Created by VincentZhang on 5/27/2017.
 */

public class UISystem implements SubSystem {
    private List<ButtonArray> buttonArray = new ArrayList<>();
    @Override
    public boolean init(String level, Resources resources, Canvas canvas) {
        XPath xPath = XPathFactory.newInstance().newXPath();
        try {
            Node uiNode = (Node) xPath.evaluate("/ui/", getXmlSource(resources, level), XPathConstants.NODE);
            XMLRefactorDecoder.decode(uiNode, UIInfo.class);


//            for (int i = 0; i < buttonArrayDefs.getLength(); i++) {
//                Node buttonArrayDefNode = buttonArrayDefs.item(i);
//                String buttonArrayName = buttonArrayDefNode.getAttributes().getNamedItem("name").getNodeValue();
//                ButtonArray buttonArray = new ButtonArray(buttonArrayName);
//                NodeList buttons = buttonArrayDefNode.getChildNodes();
//                for(int j = 0 ; j < buttons.getLength(); j++){
//                    Node buttonNode = buttons.item(j);
//
//                }
//            }
        } catch (XPathExpressionException e) {
            Log.e("XPath error", "Error!");
            return false;
        }
        
        return false;
    }

    private LinkedList<Pair<String, Long>> messages = new LinkedList<>();

    // Each message show 5s
    private static final long MESSAGE_SHOWTIME = 10000L;
    private static final long MAX_MESSAGE_COUNT = 3;

    @Override
    public void draw(Canvas canvas) {
        int currentlyKilled = SpriteWorld.getInst().getLeadingSprite().getCredit();
        int scrWidth = canvas.getWidth();
        int scrHeight = canvas.getHeight();

        String text = "Kills:" + currentlyKilled + " Coins:" + SpriteWorld.getInst().getLeadingSprite().getResource(ResourceType.COIN) +
                " Timber:" + SpriteWorld.getInst().getLeadingSprite().getResource(ResourceType.TIMBER);
        Paint p = new Paint();
        p.setColor(0xFFFF00FF);
        p.setTextSize(70);
        p.setStrokeWidth(10);
        p.setStyle(Paint.Style.FILL);
        Rect textRect = new Rect();
        p.getTextBounds(text, 0, text.length(), textRect);
        float textWidth = p.measureText(text);

        // Paint rectP = new Paint();
        // rectP.setColor(Color.GRAY);
        // canvas.drawRect(new Rect(0,0,canvas.getWidth(), textRect.height()), rectP);
        canvas.drawText(text, scrWidth - textWidth, textRect.height() + 10, p);


        Paint msgP = new Paint();
        msgP.setColor(Color.YELLOW);
        msgP.setTextSize(60);
        msgP.setStrokeWidth(10);
        msgP.setStyle(Paint.Style.FILL);

        int itr = 0;
        ArrayList<Pair<String, Long>> tobeDeletedMessages = new ArrayList<>();
        for (Pair<String, Long> messagePair : messages) {
            if (messagePair.second < System.currentTimeMillis() - MESSAGE_SHOWTIME) {
                tobeDeletedMessages.add(messagePair);
                continue;
            }

            Rect msgTextRect = new Rect();
            msgP.getTextBounds(text, 0, text.length(), msgTextRect);

            long showTime = System.currentTimeMillis() - messagePair.second;
            int alpha = (int) (255 * (float) showTime / (float) MESSAGE_SHOWTIME);

            msgP.setARGB(255 - alpha, 255, 255, 0);
            int offsetX = 200;
            int offsetY = 500;
            int padding = 20;
            int canvasHeight = canvas.getHeight();

            canvas.drawText(messagePair.first, offsetX, canvasHeight - offsetY - itr * (msgTextRect.height() + padding), msgP);
            itr++;
        }

        for (Pair<String, Long> tobeDeletedMessagePair : tobeDeletedMessages) {
            messages.remove(tobeDeletedMessagePair);
        }
    }

    public void addMessage(String content) {
        this.messages.addFirst(new Pair<String, Long>(content, System.currentTimeMillis()));
        while (this.messages.size() > MAX_MESSAGE_COUNT) {
            this.messages.removeLast();
        }
    }

    @Override
    public void beforeCollision() {

    }

    @Override
    public AbstractSprite detectCollide(ImageSprite imgSprite) {
        return null;
    }

    @Override
    public void preUpdate() {

    }

    @Override
    public void postUpdate() {

    }
}
