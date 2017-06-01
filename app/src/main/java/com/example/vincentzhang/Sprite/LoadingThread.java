package com.example.vincentzhang.Sprite;

import android.graphics.Canvas;

/**
 * Created by VincentZhang on 6/1/2017.
 */

public class LoadingThread extends Thread{
    private Canvas canvas;

    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
    }

    @Override
    public void run() {
        super.run();
    }
}
