package com.example.vincentzhang.Sprite.imgemanagement;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.example.vincentzhang.Sprite.Utilities;

import java.util.ArrayList;

/**
 * Created by VincentZhang on 5/11/2017.
 */

enum TreeNodeType {
    INNER, LEAF
}

public class Space4DTreeNode {
    private ArrayList<Space4DTreeNode> treeNodes = new ArrayList<>();
    private Rect rect;
    private BitmapMask mask;
    private TreeNodeType type = TreeNodeType.INNER;
    private int totalNodeCount = 1;

    public int getTotalNodeCount() {
        return totalNodeCount;
    }

    public Space4DTreeNode(BitmapMask mask, Rect rect) {
        this.mask = mask;
        this.rect = rect;

        if (rect.height() <= 4 && rect.width() <= 4) {
            type = TreeNodeType.LEAF;
            return;
        }

        Rect rects[] = new Rect[4];
        // leftTop
        rects[0] = new Rect(rect.left, rect.top, rect.centerX(), rect.centerY());
        // rightTop
        rects[1] = new Rect(rect.centerX(), rect.top, rect.right, rect.centerY());
        // leftDown
        rects[2] = new Rect(rect.left, rect.centerY(), rect.centerX(), rect.bottom);
        // rightDown
        rects[3] = new Rect(rect.centerX(), rect.centerY(), rect.right, rect.bottom);

        boolean hasChildNode = false;
        for (int i = 0; i < 4; i++) {
            if (mask.getRangePixelCount(rects[i]) > 0) {
                Space4DTreeNode newNode = new Space4DTreeNode(mask, rects[i]);
                treeNodes.add(newNode);
                this.totalNodeCount += newNode.getTotalNodeCount();
                hasChildNode = true;
            }
        }

        if (!hasChildNode) {
            type = TreeNodeType.LEAF;
            return;
        }
    }

    public boolean isLeafNode() {
        return type == TreeNodeType.LEAF;
    }

    public ArrayList<Space4DTreeNode> getChildren() {
        return this.treeNodes;
    }

    void draw(Canvas canvas, int level, Rect targetRect) {
        // Level == 0 means draw current level
        if (level == 0) {
            Paint p = new Paint();
            p.setStyle(Paint.Style.STROKE);
            p.setStrokeWidth(3);
            p.setColor(Color.RED);
            canvas.drawRect(targetRect, p);
        } else {
            for (Space4DTreeNode node : treeNodes) {
                Rect srcRect = Utilities.mapRect(rect, node.rect, targetRect);
                node.draw(canvas, level - 1, srcRect);
            }
        }
    }

    public Rect getRect() {
        return rect;
    }
}