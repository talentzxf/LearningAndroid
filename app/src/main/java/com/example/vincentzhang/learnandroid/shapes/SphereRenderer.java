package com.example.vincentzhang.learnandroid.shapes;

import android.opengl.Matrix;

import java.util.HashMap;
import java.util.Map;

import min3d.core.Number3dBufferList;
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
            "uniform mat4 projection;" +
                    "uniform mat4 view;" +
                    "uniform mat4 model;" +
                    "attribute vec4 vPosition;" +
                    "attribute vec4 vNormal;" +
                    "uniform vec4 vColor;" +
                    "uniform vec4 lightPos;" +
                    "vec4 ambient = vec4(0.1,0.1,0.1,1.0);"+
                    "varying vec4 aColor;" +
                    "void main() {" +
                    // The matrix must be included as a modifier of gl_Position.
                    // Note that the uMVPMatrix factor *must be first* in order
                    // for the matrix multiplication product to be correct.
                    "  gl_Position = projection * view * model * vPosition;" +
                    " aColor = (ambient+dot( normalize(vNormal.xyz), normalize((lightPos - model*vPosition).xyz)))*vColor;" +
                    "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "varying vec4 aColor;" +
                    "void main() {" +
                    "  gl_FragColor = aColor;" +
                    "}";

    public SphereRenderer(float radius, int stacks, int slices) {
        sphereInternal = new Sphere(radius, stacks, slices);
        renderer = new OGLES20ObjectRender(vertexShaderCode, fragmentShaderCode, sphereInternal);
    }

    // mvMatrix -- model view matrix
    // projectMatrix -- projection matrix
    public void draw(float[] model, float[] view, float[] projection) {
        // setup uniform
        Map<String, float[]> uniformMap = new HashMap<>();
        uniformMap.put("projection", projection);
        uniformMap.put("view", view);
        uniformMap.put("model", model);
        uniformMap.put("vColor", new float[]{1.0f, 1.0f, 1.0f, 1.0f});
        uniformMap.put("lightPos", new float[]{5.0f, 10.0f, 0.0f, 1.0f});

        // setup attributes
        Map<String, Number3dBufferList> attributeMap = new HashMap<>();
        attributeMap.put("vPosition", sphereInternal.points());
        attributeMap.put("vNormal", sphereInternal.normals());

        renderer.drawObject(uniformMap, attributeMap);
    }
}
