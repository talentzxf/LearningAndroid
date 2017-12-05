package com.example.vincentzhang.learnandroid;

import java.util.PriorityQueue;
import java.util.Stack;

/**
 * Created by VincentZhang on 12/4/2017.
 */

public class MatrixUtilities {
    private Stack<float[]> matrixStack = new Stack<float[]>();
    void pushMatrix(float[] curMatrix){
        matrixStack.push(curMatrix);
    }

    float[] popMatrix(){
        return matrixStack.pop();
    }

    void clearMatrix(){
        matrixStack = new Stack<float[]>();
    }
}
