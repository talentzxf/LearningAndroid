package com.example.vincentzhang.learnandroid;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceView;

import com.example.vincentzhang.Sprite.SpriteWorld;

/**
 * Created by VincentZhang on 4/15/2017.
 */

public class MyGameView extends SurfaceView implements Runnable {
    private SpriteWorld spriteWorld = new SpriteWorld();
    private boolean isDrawing = false;
    private final static long FPS = 20;


    public MyGameView(Context context, AttributeSet attrs) {
        super(context, attrs);

        Log.i("Context", "begin to init world");
        if (spriteWorld.init(context)) {
            Log.i("Context", "World inited");
            isDrawing = true;
            Thread t = new Thread(this);
            t.start();
        }
    }

    @Override
    public void run() {
        long lastMilliSeconds = System.currentTimeMillis();

        while (isDrawing) {
            if (System.currentTimeMillis() - lastMilliSeconds > 1000/FPS) {
                Canvas canvas = this.getHolder().lockCanvas();
                if (canvas != null) {
                    this.spriteWorld.update();
                    this.spriteWorld.draw(canvas);
                    this.getHolder().unlockCanvasAndPost(canvas);
                    lastMilliSeconds = System.currentTimeMillis();
                } else {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
