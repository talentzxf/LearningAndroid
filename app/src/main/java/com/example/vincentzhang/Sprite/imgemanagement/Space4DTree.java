package com.example.vincentzhang.Sprite.imgemanagement;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

import com.example.vincentzhang.Sprite.Vector2D;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by VincentZhang on 4/29/2017.
 */

public class Space4DTree {
    private BitmapMask bmMask;
    private Map<Vector2D, Space4DTreeNode> rootNodes = new HashMap<>();
    private String imgId;
    private int rowCount = 1;
    private int colCount = 1;

    public Space4DTree(String imgId, Bitmap bm, Integer rowCount, Integer colCount) {
        bmMask = new BitmapMask(imgId, bm);

        this.rowCount = rowCount;
        this.colCount = colCount;

        int spriteWidth = bm.getWidth() / colCount;
        int spriteHeight = bm.getHeight() / rowCount;

        for (int r = 0; r < rowCount; r++) {
            for (int c = 0; c < colCount; c++) {
                Space4DTreeNode rootNode = new Space4DTreeNode(bmMask, new Rect(c * spriteWidth, r * spriteHeight, (c + 1) * spriteWidth - 1, (r + 1) * spriteHeight - 1));

                rootNodes.put(new Vector2D(c, r), rootNode);
            }
        }
        this.imgId = imgId;
    }

    public Space4DTreeNode getRootNode(Vector2D rowCol){
        return rootNodes.get(rowCol);
    }

    public BitmapMask getBmMask(){
        return bmMask;
    }

    public Rect getRootRect(Vector2D rowCol){
        return rootNodes.get(rowCol).getRect();
    }

    public void draw(Canvas canvas, Vector2D rowColumnVec, int level, Rect targetRect) {
        if (rowColumnVec == null) {
            Log.i("Row Column Vec is null", "Null");
            return;
        }

        if (rootNodes.get(rowColumnVec) == null) {
            Log.i("Can't get rowColumnVec.", rowColumnVec.toString());
            return;
        }
        rootNodes.get(rowColumnVec).draw(canvas, level, targetRect);
    }

    public int getRowCount() {
        return rowCount;
    }

    public int getColCount(){
        return colCount;
    }
}
