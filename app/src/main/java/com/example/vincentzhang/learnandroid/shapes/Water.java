package com.example.vincentzhang.learnandroid.shapes;

import android.graphics.Rect;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.example.vincentzhang.learnandroid.Camera.Camera;
import com.example.vincentzhang.learnandroid.OpenGLRenderer;

import java.util.HashMap;
import java.util.Map;

import max3d.Shared;
import max3d.core.AbstractBufferList;
import max3d.core.ObjectRenderer;
import max3d.primitives.Rectangle;

/**
 * Created by VincentZhang on 12/18/2017.
 */

public class Water {
    private Rectangle rectangle;
    private ObjectRenderer renderer;
    private Camera camera;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public Water(float $width, float $height, int $segsW, int $segsH, Camera $cam) {
        rectangle = new Rectangle($width, $height, $segsW, $segsH);
        renderer = new ObjectRenderer("shaders/lambert_no_texture.vert",
                "shaders/lambert_no_texture.frag", rectangle);
        camera = $cam;
    }

    public void draw(float[] model, float[] view, float[] projection) {
        GLES20.glDisable(GLES20.GL_CULL_FACE);
        // Do some local transforms
        float[] localRotate = new float[16];
        Matrix.setRotateM(localRotate, 0, -90.0f, 1.0f, 0.0f, 0.0f);
        Matrix.multiplyMM(model, 0, model, 0, localRotate, 0);

        Map<String, Object> uniformMap = new HashMap<>();
        uniformMap.put("projection", projection);
        uniformMap.put("view", view);
        uniformMap.put("model", model);
        uniformMap.put("vColor", new float[]{0.25f, 1.0f, 1.25f, 0.1f});
        uniformMap.put("cameraPos", this.camera.getPos());
        // uniformMap.put("lightPos", new float[]{5.0f, 10.0f, 0.0f, 1.0f});

        // setup attributes
        Map<String, AbstractBufferList> attributeMap = new HashMap<>();
        attributeMap.put("vPosition", rectangle.points());
        attributeMap.put("vNormal", rectangle.normals());
        renderer.drawObject(uniformMap, attributeMap);
    }
}
