package com.example.vincentzhang.learnandroid;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.example.vincentzhang.Sprite.imgemanagement.Space4DTree;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

/**
 * Created by VincentZhang on 5/1/2017.
 */

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class Space4DTreeUT {
    public Space4DTreeUT(){
    }

    @Test
    public void testSpace4D(){
        Bitmap bm = Bitmap.createBitmap(400,800, Bitmap.Config.ARGB_8888);
        Canvas cv = new Canvas(bm);
        cv.drawColor(Color.WHITE);
        Paint pt = new Paint();
        pt.setColor(0xFFF0F0F0);
        cv.drawCircle(200,400,200,pt);
        cv.save( Canvas.ALL_SAVE_FLAG );

        Space4DTree space4DTree = new Space4DTree(bm);
    }
}
