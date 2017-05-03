package com.example.vincentzhang.Sprite.imgemanagement;

import android.graphics.Bitmap;
import android.graphics.Rect;

/**
 * Created by VincentZhang on 4/29/2017.
 */

public class BitmapMask {
    private boolean[][] mask;

    /**
     * The count of pixels from 0,0 to given coordiate x,y
     */
    private int[][] pixelCount;
    public BitmapMask(Bitmap bitmap){
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        mask = new boolean[height][width];
        pixelCount = new int[height][width];

        for(int y = 0 ; y < height; y++){
            for(int x = 0 ; x < width; x++){
                int pixel = bitmap.getPixel(x,y);
                if(pixel == 0){
                    mask[y][x] = false;
                } else {
                    mask[y][x] = true;

                    int leftPartPixelCount = x==0? 0 :pixelCount[y][x-1];
                    int topPartPixelCount = 0;
                    if(x==0 && y > 0){
                        topPartPixelCount = pixelCount[y-1][x];
                    }
                    pixelCount[y][x] = topPartPixelCount + leftPartPixelCount + 1;
                }
            }
        }
    }

    /**
     *
     * @param x  -- X coordinate,  0 -- Width -1
     * @param y  -- y coordinate,  0 -- Height -1
     */
    public boolean get(int x, int y){
        return mask[y][x];
    }

    /**
     * Get pixel count in a rectangle
     * @param range
     * @return
     */
    public int getRangePixelCount(Rect range){
        int top = range.top;
        int bottom = range.bottom;
        int left = range.left;
        int right = range.right;

        return pixelCount[top][left] - pixelCount[bottom][right];
    }

}
