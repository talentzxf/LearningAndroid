package com.example.vincentzhang.Sprite.imgemanagement;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static android.R.attr.x;

/**
 * Created by VincentZhang on 4/29/2017.
 * <p>
 * No matter width or height, always start from 0
 * i.e.  col from 0 --> width-1
 * row from 0 --> height-1
 */

class Wrapped2DIntArray {
    private int[][] data;

    Wrapped2DIntArray(int height, int width) {
        data = new int[height][width];
    }

    int get(int y, int x) {
        if (y <  0)
            return 0;
        if (x < 0)
            return 0;
        return data[y][x];
    }

    void set(int y, int x, int v) {
        data[y][x] = v;
    }

}

public class BitmapMask {
    private boolean[][] mask;
    private Wrapped2DIntArray pixelCount;
    /**
     * The count of pixels from 0,0 to given coordiate x,y
     */

    private int imgId = -1;

    public BitmapMask(Integer imgId, Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        mask = new boolean[height][width];
        pixelCount = new Wrapped2DIntArray(height, width);

        String fileName = Environment.getExternalStorageDirectory() + File.separator + "img" + imgId + ".dat";

        try (BufferedWriter writer =
                     new BufferedWriter(new FileWriter(fileName))) {
            Log.i("Opened file:", fileName);
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int pixel = bitmap.getPixel(x, y);
                    if (pixel == 0) {
                        mask[y][x] = false;
                    } else {
                        mask[y][x] = true;
                    }

                    int topWithMyColPixelCount = pixelCount.get(y - 1, x);
                    int topWithoutMyColPixelCount = pixelCount.get(y - 1, x - 1);

                    int leftTotalCount = pixelCount.get(y, x - 1);
                    int rowLeftCount = leftTotalCount - topWithoutMyColPixelCount;

                    int mineCount = mask[y][x] ? 1 : 0;
                    pixelCount.set(y, x, topWithMyColPixelCount + rowLeftCount + mineCount);
                }
            }

        } catch (IOException e) {
            Log.e("File error", fileName, e);
        }

        Log.i("Closed file:", fileName);
    }

    /**
     * @param x -- X coordinate,  0 -- Width -1
     * @param y -- y coordinate,  0 -- Height -1
     */
    public boolean get(int x, int y) {
        return mask[y][x];
    }

    /**
     * Get pixel count in a rectangle
     *
     * @param range, x  0 --> Width-1,  y 0 --> Height-1
     * @return
     */
    public int getRangePixelCount(Rect range) {
        int top = range.top;
        int bottom = range.bottom;
        int left = range.left;
        int right = range.right;

        return pixelCount.get(bottom, right) - pixelCount.get(top, left);
    }

}
