package com.example.vincentzhang.learnandroid.shapes;

import android.graphics.Rect;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.example.vincentzhang.learnandroid.Camera.Camera;
import com.example.vincentzhang.learnandroid.OpenGLRenderer;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

import max3d.Shared;
import max3d.core.AbstractBufferList;
import max3d.core.ObjectRenderer;
import max3d.core.TextureRenderer;
import max3d.primitives.Rectangle;

/**
 * Created by VincentZhang on 12/18/2017.
 */

public class Water {
    private Rectangle rectangle;
    private final int GEN_TEX_WIDTH = 256;
    private final int GEN_TEX_HEIGHT = 256;

    // Used for shader only
    private Rectangle shaderRectangle = new Rectangle(-2.0f, 2.0f, 1, 1);

    private ObjectRenderer renderer;
    private Camera camera;

    // The data in the texture is (position.y, velocity.y, normal.x, normal.z)
    private TextureRenderer textureA = new TextureRenderer();
    private TextureRenderer textureB = new TextureRenderer();

    private TextureRenderer causticTexture = new TextureRenderer();

    private ObjectRenderer dropRenderer;
    private ObjectRenderer updateRenderer;
    private ObjectRenderer updateNormalRenderer;
    private ObjectRenderer causticRenderer;

    private void swapRTT() {
        TextureRenderer tmp = textureA;
        textureA = textureB;
        textureB = tmp;
    }

    boolean initDropAdded = false;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public Water(float $width, float $height, int $segsW, int $segsH, Camera $cam) {
        rectangle = new Rectangle($width, $height, $segsW, $segsH);
        renderer = new ObjectRenderer("shaders/lambert_no_texture.vert",
                "shaders/lambert_no_texture.frag", rectangle);
        causticRenderer = new ObjectRenderer("shaders/water/caustics.vert",
                "shaders/water/caustics.frag", rectangle);
        camera = $cam;

        textureA.init(GEN_TEX_WIDTH, GEN_TEX_HEIGHT);
        textureB.init(GEN_TEX_WIDTH, GEN_TEX_HEIGHT);
        causticTexture.init(1024,1024);

        dropRenderer = new ObjectRenderer("shaders/water/vertex.vert",
                "shaders/water/drop.frag", shaderRectangle);
        updateRenderer = new ObjectRenderer("shaders/water/vertex.vert",
                "shaders/water/update.frag", shaderRectangle);
        updateNormalRenderer = new ObjectRenderer("shaders/water/vertex.vert",
                "shaders/water/normal.frag", shaderRectangle);
    }

    public void draw(float[] model, float[] view, float[] projection) {
        GLES20.glDisable(GLES20.GL_CULL_FACE);

        Map<String, Object> uniformMap = new HashMap<>();
        uniformMap.put("projection", projection);
        uniformMap.put("view", view);
        uniformMap.put("model", model);
        // uniformMap.put("vColor", new float[]{0.25f, 1.0f, 1.25f, 0.1f});
        uniformMap.put("cameraPos", this.camera.getPos());

        float[] local_model = new float[16];
        Matrix.setIdentityM(local_model, 0);
        Matrix.translateM(local_model, 0, 0.0f, -0.5f, 0.0f);
        uniformMap.put("sphere_model", local_model);
        // uniformMap.put("light", new float[]{5.0f, 10.0f, 0.0f, 1.0f});

        uniformMap.put("sph_Texture", 0);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, Shared.textureManager().getGlTextureId("earth"));

        uniformMap.put("wall_Texture", 1);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, Shared.textureManager().getGlTextureId("imooc"));

        uniformMap.put("info_Texture", 2);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE2);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureA.getTextureId());

        uniformMap.put("caustics_Texture",3);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE3);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, causticTexture.getTextureId());

        // setup attributes
        Map<String, AbstractBufferList> attributeMap = new HashMap<>();
        attributeMap.put("vPosition", rectangle.points());
        // attributeMap.put("vNormal", rectangle.normals());
        renderer.drawObject(uniformMap, attributeMap);

        if (!initDropAdded) {
            addDrop();
            initDropAdded = true;
        }

        updateWater();
        updateWater();
        updateWater();
        updateWaterNormal();

        updateCaustics();
    }

    private void updateCaustics(){
        causticTexture.drawTo(new Runnable() {
            @Override
            public void run() {
                GLES20.glViewport(0,0,1024,1024);
                GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
                Map uniformMap = new HashMap();
                uniformMap.put("light", new float[]{2.0f, 2.0f, -1.0f});
                uniformMap.put("info_Texture", 0);

                Map attributeMap = new HashMap();
                attributeMap.put("vPosition", rectangle.points());

                // Provide texture A information
                GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureA.getTextureId());
                causticRenderer.drawObject(uniformMap, attributeMap);
            }
        });
    }

    public void addDrop() {
        textureB.drawTo(new Runnable() {
            @Override
            public void run() {
                GLES20.glViewport(0, 0, GEN_TEX_WIDTH, GEN_TEX_HEIGHT);
                GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);

                Map uniformMap = new HashMap();

                Map attributeMap = new HashMap();
                attributeMap.put("vPosition", shaderRectangle.points());

                uniformMap.put("center", new float[]{0.0f, 0.0f});
                uniformMap.put("radius", 0.03f);
                uniformMap.put("strength", 3.01f);
                uniformMap.put("texture", 0);

                // Provide texture A information
                GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureA.getTextureId());
                dropRenderer.drawObject(uniformMap, attributeMap);
            }
        });

        // Swap texture B to texture A
        swapRTT();
    }

    public void updateWater() {

        textureB.drawTo(new Runnable() {
            @Override
            public void run() {
                GLES20.glViewport(0, 0, GEN_TEX_WIDTH, GEN_TEX_HEIGHT);
                GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);

                Map uniformMap = new HashMap();

                Map attributeMap = new HashMap();
                attributeMap.put("vPosition", shaderRectangle.points());
                uniformMap.put("texture", 0);
                uniformMap.put("delta", new float[]{1.0f / GEN_TEX_WIDTH, 1.0f / GEN_TEX_HEIGHT});

                // Provide texture A information
                GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureA.getTextureId());

                updateRenderer.drawObject(uniformMap, attributeMap);
            }
        });

        swapRTT();
    }

    public void updateWaterNormal() {

        textureB.drawTo(new Runnable() {
            @Override
            public void run() {
                GLES20.glViewport(0, 0, GEN_TEX_WIDTH, GEN_TEX_HEIGHT);
                GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);

                Map uniformMap = new HashMap();

                Map attributeMap = new HashMap();
                attributeMap.put("vPosition", shaderRectangle.points());
                uniformMap.put("texture", 0);
                uniformMap.put("delta", new float[]{1.0f / GEN_TEX_WIDTH, 1.0f / GEN_TEX_HEIGHT});

                // Provide texture A information
                GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureA.getTextureId());
                updateNormalRenderer.drawObject(uniformMap, attributeMap);
            }
        });
        swapRTT();
    }

    public int getInforTextureId() {
        return textureA.getTextureId();
    }

    public int causticsTextureId() {
        return causticTexture.getTextureId();
    }
}
