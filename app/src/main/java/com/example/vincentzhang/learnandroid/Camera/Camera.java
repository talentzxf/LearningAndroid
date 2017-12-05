package com.example.vincentzhang.learnandroid.Camera;

import android.opengl.Matrix;

import com.example.vincentzhang.learnandroid.Utilities.Vector;

/**
 * Created by VincentZhang on 12/5/2017.
 */

public class Camera {
    private float[] pos = new float[3];
    private float[] lookAt = new float[3];
    private float[] up = {0, 1, 0};
    private float[] viewMatrix = new float[16];
    private float scrWidth;
    private float scrHeight;

    public float[] getPos() {
        return pos;
    }

    public void setPos(float[] pos) {
        this.pos = pos;
    }

    public float[] getLookAt() {
        return lookAt;
    }

    public void setLookAt(float[] lookAt) {
        this.lookAt = lookAt;
    }

    public float[] getUp() {
        return up;
    }

    public void setUp(float[] up) {
        this.up = up;
    }

    public void setViewMatrix(float[] viewMatrix) {
        this.viewMatrix = viewMatrix;
    }

    public float[] getViewMatrix() {
        Matrix.setLookAtM(viewMatrix, 0, pos[0], pos[1], pos[2], lookAt[0], lookAt[1], lookAt[2], up[0], up[1], up[2]);
        return viewMatrix;
    }

    public void rotate(float x, float y){
        float[] cameraLeft = Vector.normalize(Vector.cross3(this.pos, this.up));
        // Go along camera Left, delta
        float[] cameraRealUp = Vector.normalize(Vector.cross3(Vector.neg(this.pos), cameraLeft));

        float[] newPosX = Vector.translate(this.pos, cameraLeft, x);

        float currentCameraToTargetVec = Vector.norm(Vector.sub(this.pos, this.lookAt));
        this.pos = Vector.multiply(Vector.normalize(Vector.translate(newPosX, cameraRealUp, y)),currentCameraToTargetVec);
    }

    public void setViewport(int width, int height) {
        this.scrWidth = width;
        this.scrHeight = height;
    }
}

