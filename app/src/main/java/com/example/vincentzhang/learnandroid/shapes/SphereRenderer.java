package com.example.vincentzhang.learnandroid.shapes;

import android.opengl.Matrix;

import java.util.HashMap;

import min3d.core.OGLES20ObjectRender;
import min3d.objectPrimitives.Sphere;

/**
 * Created by VincentZhang on 12/7/2017.
 * https://github.com/kibotu/net.gtamps/blob/refactoring3d/android/graphic/src/net/gtamps/android/renderer/graph/scene/primitives/Sphere.java
 */

public class SphereRenderer {
    private Sphere sphereInternal;
    private OGLES20ObjectRender renderer;

    private final String vertexShaderCode =
            // This matrix member variable provides a hook to manipulate
            // the coordinates of the objects that use this vertex shader
            "uniform mat4 uMVPMatrix;" +
                    "attribute vec4 vPosition;" +
                    "void main() {" +
                    // The matrix must be included as a modifier of gl_Position.
                    // Note that the uMVPMatrix factor *must be first* in order
                    // for the matrix multiplication product to be correct.
                    "  gl_Position = uMVPMatrix * vPosition;" +
                    "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vec4(1.0,0.0,0.0, 1.0);" +
                    "}";

    public SphereRenderer(float radius, int stacks, int slices) {
        sphereInternal = new Sphere(radius, stacks, slices);
        renderer = new OGLES20ObjectRender(vertexShaderCode, fragmentShaderCode, sphereInternal);
    }

    // mvMatrix -- model view matrix
    // projectMatrix -- projection matrix
    public void draw(float[] model, float[] view, float[] projection) {
        HashMap<String, float[]> uniformMap = new HashMap<>();
        float[] mvpMatrix = new float[16];
        Matrix.multiplyMM(mvpMatrix, 0, projection, 0, view, 0);
        Matrix.multiplyMM(mvpMatrix, 0, mvpMatrix, 0, model, 0);
        uniformMap.put("uMVPMatrix", mvpMatrix);
        renderer.drawObject(uniformMap);
    }
}
