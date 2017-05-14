package com.example.vincentzhang.learnandroid;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceView;

import com.example.vincentzhang.Sprite.CollideDetector;
import com.example.vincentzhang.Sprite.SpriteWorld;

/**
 * Created by VincentZhang on 4/15/2017.
 */

public class MyGameView extends SurfaceView implements Runnable, GestureDetector.OnGestureListener{
    private SpriteWorld spriteWorld = new SpriteWorld();
    private boolean isDrawing = false;
    private final static long FPS = 20;
    private GestureDetector gestureDetector;
    private Context context;

    public SpriteWorld getWorld(){
        return spriteWorld;
    }

    public MyGameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        isDrawing = true;
        Thread t = new Thread(this);
        t.start();
        gestureDetector = new GestureDetector(context, this);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        Log.i("onTouchEvent", event.toString());
        return super.onTouchEvent(event);
    }

    @Override
    public void run() {
        long lastMilliSeconds = System.currentTimeMillis();

        while (isDrawing) {
            if (System.currentTimeMillis() - lastMilliSeconds > 1000/FPS) {
                Canvas canvas = this.getHolder().lockCanvas();

                if (canvas != null) {
                    if(!spriteWorld.inited()){
                        Log.i("Context", "begin to init world");
                        spriteWorld.init(context, canvas);
                        Log.i("Context", "World inited");
                    }

                    canvas.drawColor(Color.BLACK);
                    this.spriteWorld.preUpdate();
                    do{
                        this.spriteWorld.beforeCollision();
                    }while( this.spriteWorld.processCollision() );

                    // All collisions should have been handled here.
                    CollideDetector.setDirtyFlag(false);

                    this.spriteWorld.draw(canvas);
                    this.spriteWorld.postUpdate();
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

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        Log.i("OnScroll, on scroll", "e1.getAction:" + e1.getAction() + " distanceX:" + distanceX + " distanceY:" + distanceY);
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }
}
