package com.example.vincentzhang.learnandroid.shapes;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.example.vincentzhang.learnandroid.OpenGLActivity;
import com.example.vincentzhang.learnandroid.R;

import java.util.HashMap;
import java.util.Map;

import max3d.Shared;
import max3d.core.ObjectRenderer;
import max3d.primitives.Box;

/**
 * Created by VincentZhang on 1/3/2018.
 */

public class Wall {
    private Box box;
    private ObjectRenderer boxRenderer;
    private Water water;

    public void setWater(Water water) {
        this.water = water;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public Wall(){
        box = new Box(2.0f,2.0f,2.0f);
        boxRenderer = new ObjectRenderer("shaders/water/wall_surface.vert",
                "shaders/water/wall_surface.frag", box);
    }

    // mvMatrix -- model view matrix
    // projectMatrix -- projection matrix
    public void draw(float[] model, float[] view, float[] projection) {
        GLES20.glEnable(GLES20.GL_CULL_FACE);
        GLES20.glCullFace(GLES20.GL_FRONT);

        Map uniformMap = new HashMap();
        uniformMap.put("project", projection);
        uniformMap.put("view", view);
        uniformMap.put("model", model);
        uniformMap.put("light", new float[]{2.0f, 2.0f, -1.0f, 1.0f});

        Map attributeMap = new HashMap();
        attributeMap.put("vPos", box.points());

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        uniformMap.put("tiles", 0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, Shared.textureManager().getGlTextureId("imooc"));

        GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
        uniformMap.put("info_Texture", 1);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, water.getInforTextureId());

        GLES20.glActiveTexture(GLES20.GL_TEXTURE2);
        uniformMap.put("causticTex", 2);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, water.getCausticsTextureId());

        boxRenderer.drawObject(uniformMap, attributeMap);
    }

}
