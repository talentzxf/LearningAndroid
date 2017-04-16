package com.example.vincentzhang.virtuallifes;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.Random;

/**
 * Created by VincentZhang on 4/9/2017.
 */

public class VirtualWorld {
    private static final int WIDTH = 100;

    private int[][] lifes = new int[WIDTH + 2][WIDTH + 2];
    private int[][] nextLifes = new int[WIDTH + 2][WIDTH + 2];
    private Random rand = new Random();
    private static Paint p = new Paint();

    static {
        p.setColor(Color.GREEN);
    }

    public VirtualWorld() {

        // Init virtual lifes
        for (int i = 0; i < WIDTH + 2; i++) {
            for (int j = 0; j < WIDTH + 2; j++) {
                if (i < 0 || j < 0 || i == WIDTH + 1|| j == WIDTH + 1) {
                    lifes[i][j] = 0;
                }
                lifes[i][j] = rand.nextInt(2);
            }
        }

//        lifes[2][1] = 1;
//        lifes[2][2] = 1;
//        lifes[2][3] = 1;

    }

    private void update() {
        for (int y = 1; y <= WIDTH; y++) {
            for (int x = 1; x <= WIDTH; x++) {
                int totalSurvivalCount = 0;
                for (int deltaX = -1; deltaX <= 1; deltaX++) {
                    for (int deltaY = -1; deltaY <= 1; deltaY++) {
                        if(deltaX != 0 || deltaY != 0 ){
                            totalSurvivalCount += lifes[(WIDTH+y+deltaY)%WIDTH][(WIDTH+x + deltaX)%WIDTH];
                        }
                    }
                }

                if(totalSurvivalCount < 2 || totalSurvivalCount > 3){
                    nextLifes[y][x] = 0;
                } else if(totalSurvivalCount == 3){
                    nextLifes[y][x] = 1;
                } else{
                    nextLifes[y][x] = lifes[y][x];
                }
            }
        }

        for(int y = 1 ; y <= WIDTH; y++){
            for(int x = 1; x <= WIDTH; x++){
                lifes[y][x] = nextLifes[y][x];
            }
        }

    }

    public void draw(Canvas canvas) {

        update();

        int totalWidth = Math.min(canvas.getWidth(), canvas.getHeight());
        int blockWidth = totalWidth / WIDTH;

        for (int i = 1; i <= WIDTH; i++) {
            for (int j = 1; j <= WIDTH; j++) {
                if (lifes[i][j] == 1) {
                    canvas.drawRect(i * blockWidth, j * blockWidth, (i + 1) * blockWidth, (j + 1) * blockWidth, p);
                }
            }
        }
    }
}
