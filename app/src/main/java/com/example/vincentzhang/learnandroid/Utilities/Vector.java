package com.example.vincentzhang.learnandroid.Utilities;

import junit.framework.Assert;

/**
 * Created by VincentZhang on 12/5/2017.
 */

public class Vector {
    public static float[] cross3(float[] lhs, float[] rhs){
        Assert.assertEquals(lhs.length, 3);
        Assert.assertEquals(rhs.length, 3);
        float[] retVector = new float[3];
        retVector[0] = lhs[1] * rhs[2] - lhs[2] * rhs[1];
        retVector[1] = lhs[2] * rhs[0] - lhs[0] * rhs[2];
        retVector[2] = lhs[0] * rhs[1] - lhs[1] * rhs[0];

        return retVector;
    }

    public static float[] neg(float[] pos) {
        return new float[]{-pos[0],-pos[1],-pos[2]};
    }

    public static float[] translate(float[] pos, float[] dir, float x) {
        return new float[]{pos[0] + x * dir[0],pos[1] + x * dir[1],pos[2] + x * dir[2]};
    }

    public static float norm(float[] data){
        return (float) Math.sqrt(data[0]*data[0] + data[1]*data[1] + data[2]*data[2]);
    }

    public static float[] normalize(float[] data) {
        float norm = norm(data);
        return new float[]{data[0]/norm,data[1]/norm,data[2]/norm};
    }

    public static float[] multiply(float[] data, float v) {
        return new float[]{data[0]*v,data[1]*v,data[2]*v};
    }

    public static float[] sub(float[] lhs, float[] rhs) {
        return new float[]{lhs[0]-rhs[0],lhs[1]-rhs[1],lhs[2]-rhs[2]};
    }
}
