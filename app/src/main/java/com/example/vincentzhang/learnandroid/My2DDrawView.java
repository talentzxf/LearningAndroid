package com.example.vincentzhang.learnandroid;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.view.View;

/**
 * Created by VincentZhang on 4/8/2017.
 */

public class My2DDrawView extends View {
    private static final int SEGS=32;
    private static final int SIZE=500;
    private static final int X=0;
    private static final int Y=1;

    private float[] mPts;

    public My2DDrawView(Context context) {
        super(context);
        buildPoints();
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        canvas.translate(10,10);
        canvas.drawColor(Color.WHITE);
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStrokeWidth(0);
        canvas.drawLines(mPts, paint);

        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(3);
        canvas.drawPoints(mPts, paint);

        RectF rect = new RectF(10,300,290,430);
        Path path = new Path();
        path.addArc(rect, -180, 180);
        paint.setTextSize(28);
        paint.setColor(Color.BLUE);
        canvas.drawTextOnPath("Draw line", path, 0, 0 , paint);
    }

    private void buildPoints(){
        final int ptCount = (SEGS+1)*2;
        mPts = new float[ptCount * 2];

        float value = 0;
        final float delta = SIZE/SEGS;
        for(int i=0; i<= SEGS; i++){
            mPts[i*4 + X] = SIZE - value;
            mPts[i*4 + Y] = 0;
            mPts[i*4+X+2] = 0;
            mPts[i*4 + Y+ 2 ] = value;
            value += delta;
        }
    }

}
