package com.example.vincentzhang.Sprite.imgemanagement;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

import com.example.vincentzhang.Sprite.Utilities;

/**
 * Created by VincentZhang on 4/29/2017.
 */

enum TreeNodeType {
    INNER, LEAF
}

class Space4DTreeNode {
    private Space4DTreeNode treeNodes[] = new Space4DTreeNode[4];
    private Rect rect;
    private BitmapMask mask;
    private TreeNodeType type = TreeNodeType.INNER;

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
        rects[1] = new Rect(rect.centerX(), rect.top, rect.right - 1, rect.centerY());
        // leftDown
        rects[2] = new Rect(rect.left, rect.centerY(), rect.centerX(), rect.bottom - 1);
        // rightDown
        rects[3] = new Rect(rect.centerX(), rect.centerY(), rect.right - 1, rect.bottom - 1);

        for (int i = 0; i < 4; i++) {
            if (mask.getRangePixelCount(rects[i]) > 0) {
                treeNodes[i] = new Space4DTreeNode(mask, rects[i]);
            }
        }
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
            for (int i = 0; i < 4; i++) {
                if(treeNodes[i] != null){
                    Rect srcRect = Utilities.mapRect(rect, treeNodes[i].rect, targetRect);
                    treeNodes[i].draw(canvas, level - 1, srcRect);
                }
            }
        }
    }

//    public boolean collide(Space4DTreeNode otherTreeNode){
//        if(!Utilities.detectCollide(this.rect, otherTreeNode.rect)){
//            return false;
//        }
//
//        if(mask.getRangePixelCount())
//
//        return true;
//    }
}

public class Space4DTree {
    private RectF[][] occupyMatrix = new RectF[2][2];
    private BitmapMask bmMask;
    private Space4DTreeNode rootNode;

    public Space4DTree(Bitmap bm) {
        bmMask = new BitmapMask(bm);
        rootNode = new Space4DTreeNode(bmMask, new Rect(0, 0, bm.getWidth(), bm.getHeight()));
    }

    public void draw(Canvas canvas, int level, Rect targetRect) {
        rootNode.draw(canvas, level, targetRect);
    }

//    public boolean collide(Space4DTree otherTree){
//        return rootNode.collide(otherTree.rootNode);
//    }
}
