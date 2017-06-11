package com.example.vincentzhang.Sprite.imgemanagement;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.util.Pair;

import com.example.vincentzhang.Sprite.DIRECTIONS;
import com.example.vincentzhang.Sprite.Vector2D;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by VincentZhang on 6/10/2017.
 */

public class SpriteImage {
    private ArrayList<Bitmap> bmArray = new ArrayList<>();
    private ArrayList<Space4DTree> space4DTrees = new ArrayList<>();
    private ArrayList<ArrayList<DIRECTIONS>> dirArrayMapList = new ArrayList<>();
    private ArrayList<Map<DIRECTIONS, ArrayList<Rect>>> dirSpriteMapArray = new ArrayList<>();
    private ArrayList<Map<DIRECTIONS, ArrayList<Vector2D>>> dirImgRowColumnMapArray = new ArrayList<>();

    private Float scale = 1.0f;

    private int idx = 0;

    public void addBitImage(Bitmap imgBM, Space4DTree space4DTree, ArrayList<DIRECTIONS> dirArrayMap) {
        bmArray.add(imgBM);
        space4DTrees.add(space4DTree);
        this.dirArrayMapList.add(dirArrayMap);

        Pair<Map, Map> retMapPair = splitImage(imgBM, space4DTree, dirArrayMap);
        Map<DIRECTIONS, ArrayList<Rect>> dirSpriteMap = retMapPair.first;
        Map<DIRECTIONS, ArrayList<Vector2D>> dirImgRowColumnMap = retMapPair.second;

        dirSpriteMapArray.add(dirSpriteMap);
        dirImgRowColumnMapArray.add(dirImgRowColumnMap);
    }

    Pair<Map, Map> splitImage(Bitmap bm, Space4DTree space4DTree, ArrayList<DIRECTIONS> dirArrayMap) {
        Map<DIRECTIONS, ArrayList<Rect>> dirSpriteMap = new HashMap();
        Map<DIRECTIONS, ArrayList<Vector2D>> dirImgRowColumnMap = new HashMap<>();
        int imgWidth = bm.getWidth();
        int imgHeight = bm.getHeight();

        int rowNum = space4DTree.getRowCount();
        int colNum = space4DTree.getColCount();

        int spriteWidth = imgWidth / colNum;
        int spriteHeight = imgHeight / rowNum;

        for (int row = 0; row < rowNum; row++) {
            for (int col = 0; col < colNum; col++) {
                DIRECTIONS targetDir = dirArrayMap.get(row);

                ArrayList<Rect> rectSequence = dirSpriteMap.get(targetDir);
                if (rectSequence == null) {
                    rectSequence = new ArrayList<Rect>();
                }
                rectSequence.add(new Rect(col * spriteWidth, row * spriteHeight, (col + 1) * spriteWidth, (row + 1) * spriteHeight));
                dirSpriteMap.put(targetDir, rectSequence);

                ArrayList<Vector2D> rowColumnSequence = dirImgRowColumnMap.get(targetDir);
                if(rowColumnSequence == null){
                    rowColumnSequence = new ArrayList<Vector2D>();
                }
                rowColumnSequence.add(new Vector2D(col,row));
                dirImgRowColumnMap.put(targetDir, rowColumnSequence);
            }
        }
        return new Pair<Map, Map>(dirSpriteMap, dirImgRowColumnMap);
    }

    public void setScale(Float scale) {
        this.scale = scale;
    }

    // Advance to next png image.
    public void advance() {
        idx = (idx + 1) % bmArray.size();
    }

    public Bitmap currentBitmap(){
        return bmArray.get(idx);
    }

    public int getWidth() {
        return currentBitmap().getWidth();
    }

    public int getHeight() {
        return currentBitmap().getHeight();
    }

    public Float getScale() {
        return scale;
    }

    public  ArrayList<Rect> getDirSequence(DIRECTIONS dir) {
        return dirSpriteMapArray.get(idx).get(dir);
    }

    public ArrayList<Vector2D> getRowColumnPostList(DIRECTIONS dir){
        return dirImgRowColumnMapArray.get(idx).get(dir);
    }

    public Space4DTree getSpace4DTree() {
        return space4DTrees.get(idx);
    }
}
