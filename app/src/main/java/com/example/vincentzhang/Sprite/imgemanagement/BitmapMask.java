package com.example.vincentzhang.Sprite.imgemanagement;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.BitSet;

/**
 * Created by VincentZhang on 4/29/2017.
 * <p>
 * No matter width or height, always start from 0
 * i.e.  col from 0 --> width-1
 * row from 0 --> height-1
 */

class Wrapped2DIntArray {
    final static int SIZEOFINT = 4;
    private int[] data;
    private int width;
    private int height;

    Wrapped2DIntArray(int height, int width) {
        data = new int[height * width];
        this.width = width;
        this.height = height;
    }

    int get(int y, int x) {
        if (y < 0)
            return 0;
        if (x < 0)
            return 0;
        return data[y * width + x];
    }

    void set(int y, int x, int v) {
        data[y * width + x] = v;
    }

    public void write(OutputStream fos) throws IOException {
        ByteBuffer byteBuffer = ByteBuffer.allocate(width * height * SIZEOFINT);
        IntBuffer intBuffer = byteBuffer.asIntBuffer();
        intBuffer.put(data);
        fos.write(byteBuffer.array());
    }

    public void load(InputStream fis) throws IOException {
        byte[] bytes = new byte[width * height * SIZEOFINT];
        int length = fis.read(bytes);
        Log.i("Read:", "length:" + length);
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        IntBuffer intBuffer = byteBuffer.asIntBuffer();
        this.data = new int[width * height];
        intBuffer.get(this.data);
    }
}

public class BitmapMask {
    private BitSet mask;
    private Wrapped2DIntArray pixelCount;
    /**
     * The count of pixels from 0,0 to given coordiate x,y
     */

    private int imgId = -1;

    // TODO: Save as zip file to save space.
    public BitmapMask(Integer imgId, Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        mask = new BitSet(height * width);
        pixelCount = new Wrapped2DIntArray(height, width);

        String folderName = Environment.getExternalStorageDirectory() + File.separator + "BombmanGame";
        String maskFileName = folderName + File.separator + "img" + imgId + "_mask.dat";
        String pixelCountFileName = folderName + File.separator + "img" + imgId + "_pixelcount.dat";

        File folderFile = new File(folderName);
        File maskFile = new File(maskFileName);
        File pixelCountFile = new File(pixelCountFileName);

        if (!maskFile.exists() || !pixelCountFile.exists()) { // Create the file
            try {
                folderFile.mkdirs();
                maskFile.createNewFile();
                pixelCountFile.createNewFile();
            } catch (IOException e) {
                Log.i("Error create file", e.getMessage(), e);
            }

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int pixel = bitmap.getPixel(x, y);
                    if (pixel == 0) {
                        mask.set(y * width + x, false);
                    } else {
                        mask.set(y * width + x, true);
                    }

                    int topWithMyColPixelCount = pixelCount.get(y - 1, x);
                    int topWithoutMyColPixelCount = pixelCount.get(y - 1, x - 1);

                    int leftTotalCount = pixelCount.get(y, x - 1);
                    int rowLeftCount = leftTotalCount - topWithoutMyColPixelCount;

                    int mineCount = mask.get(y * width + x) ? 1 : 0;
                    pixelCount.set(y, x, topWithMyColPixelCount + rowLeftCount + mineCount);
                }

                if (y % 100 == 0)
                    Log.i("Finished", y + " lines, out of" + height + " lines.");
            }

            try (BufferedOutputStream fos = new BufferedOutputStream(new FileOutputStream(maskFile))) {
                Log.i("Opened file:", maskFileName);
                fos.write(this.mask.toByteArray());
            } catch (IOException e) {
                Log.e("File error", maskFileName, e);
            }

            try (BufferedOutputStream fos = new BufferedOutputStream(new FileOutputStream(pixelCountFile))) {
                Log.i("Opened file:", pixelCountFileName);
                pixelCount.write(fos);
            } catch (IOException e) {
                Log.e("File error", maskFileName, e);
            }
        } else {
            try (BufferedInputStream fis = new BufferedInputStream(new FileInputStream(maskFile))) {
                byte[] bytes = new byte[width * height];
                fis.read(bytes);
                this.mask = BitSet.valueOf(bytes);
            } catch (IOException e) {
                Log.e("File error", maskFileName, e);
            }

            try (BufferedInputStream fis = new BufferedInputStream(new FileInputStream(pixelCountFile))) {
                Log.i("Opened file:", pixelCountFileName);
                pixelCount.load(fis);
            } catch (IOException e) {
                Log.e("File error", maskFileName, e);
            }
        }


        Log.i("Finished processing:", "Image:" + imgId.toString());
    }

    /**
     * Get pixel count in a rectangle
     *
     * @param range, x  0 --> Width-1,  y 0 --> Height-1
     * @return
     */
    public int getRangePixelCount(Rect range) {
        int t = range.top;
        int b = range.bottom;
        int l = range.left;
        int r = range.right;

        return pixelCount.get(b, r) - pixelCount.get(t, r) - pixelCount.get(b, l) + pixelCount.get(t, l);
    }

}
