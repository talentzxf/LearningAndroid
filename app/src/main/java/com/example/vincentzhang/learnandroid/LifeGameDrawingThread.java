package com.example.vincentzhang.learnandroid;

import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.view.SurfaceHolder;

import com.example.vincentzhang.virtuallifes.VirtualWorld;

import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by VincentZhang on 4/9/2017.
 */

public class LifeGameDrawingThread extends Thread {
    private SurfaceHolder surfaceHolder;
    private AtomicBoolean isRunning = new AtomicBoolean(false);
    private VirtualWorld world = new VirtualWorld();

    public void setSurfaceHolder(SurfaceHolder surfaceHolder){
        this.surfaceHolder = surfaceHolder;
    }

    public void stopDrawing(){
        isRunning.set(false);
    }

    public void startRunning(){
        isRunning.set(true);
        this.start();
    }

    @Override
    public void run(){
        Random rand = new Random();
//        try {
//            Thread.sleep(10000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        while(isRunning.get()){
            Canvas canvas = this.surfaceHolder.lockCanvas();
            if(canvas == null){
                Log.e("LifeGameDrawingThread","Can't get canvas");
                continue;
            }

            canvas.drawColor(Color.BLACK);
            world.draw(canvas);

            this.surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }
}
