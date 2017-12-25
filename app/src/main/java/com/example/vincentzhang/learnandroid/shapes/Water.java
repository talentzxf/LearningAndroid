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
    private ObjectRenderer renderer;
    private Camera camera;

    // The data in the texture is (position.y, velocity.y, normal.x, normal.z)
    private TextureRenderer textureA = new TextureRenderer();
    private TextureRenderer textureB = new TextureRenderer();

    private TextureRenderer currentTexture = textureA;
    private TextureRenderer backTexture = textureB;

    private ObjectRenderer updateRenderer;
    private ObjectRenderer addDropRenderer;

    private void swapTexture() {
        TextureRenderer tmp = currentTexture;
        currentTexture = backTexture;
        backTexture = tmp;
    }

    boolean initDropAdded = false;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public Water(float $width, float $height, int $segsW, int $segsH, Camera $cam) {
        rectangle = new Rectangle($width, $height, $segsW, $segsH);
        renderer = new ObjectRenderer("shaders/lambert_no_texture.vert",
                "shaders/lambert_no_texture.frag", rectangle);
        camera = $cam;

        textureA.init(256, 256);
        textureB.init(256, 256);
        currentTexture = textureA;
        updateRenderer = new ObjectRenderer("shaders/water/vertex.vert",
                "shaders/water/update.frag", rectangle);
        addDropRenderer = new ObjectRenderer("shaders/water/vertex.vert",
                "shaders/water/drop.frag", rectangle);

    }

    public void draw(float[] model, float[] view, float[] projection) {
        GLES20.glDisable(GLES20.GL_CULL_FACE);

        Map<String, Object> uniformMap = new HashMap<>();
        uniformMap.put("projection", projection);
        uniformMap.put("view", view);
        uniformMap.put("model", model);
        uniformMap.put("vColor", new float[]{0.25f, 1.0f, 1.25f, 0.1f});
        uniformMap.put("cameraPos", this.camera.getPos());

        float[] local_model = new float[16];
        Matrix.setIdentityM(local_model, 0);
        Matrix.translateM(local_model, 0, 0.0f, -0.5f, 0.0f);
        uniformMap.put("sphere_model", local_model);
        // uniformMap.put("lightPos", new float[]{5.0f, 10.0f, 0.0f, 1.0f});

        uniformMap.put("sph_Texture", 0);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, Shared.textureManager().getGlTextureId("earth"));

        uniformMap.put("wall_Texture", 1);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, Shared.textureManager().getGlTextureId("imooc"));

        uniformMap.put("info_Texture", 2);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE2);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, currentTexture.getTextureId());

        // setup attributes
        Map<String, AbstractBufferList> attributeMap = new HashMap<>();
        attributeMap.put("vPosition", rectangle.points());
        attributeMap.put("vNormal", rectangle.normals());
        renderer.drawObject(uniformMap, attributeMap);

        if(!initDropAdded){
            addDrop();
            initDropAdded = true;
        }

        updateInfo();
    }

    void updateInfo() {
        currentTexture.drawTo(new Runnable() {
            @Override
            public void run() {
                Map<String, Object> uniformMap = new HashMap<>();
                uniformMap.put("delta", new float[]{1/backTexture.getWidth(), 1/backTexture.getHeight()});

                Map<String, AbstractBufferList> attributeMap = new HashMap<>();
                attributeMap.put("vPosition", rectangle.points());

                GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, backTexture.getTextureId());
                uniformMap.put("texture", 0);
                updateRenderer.drawObject(uniformMap, attributeMap);
            }
        });
        swapTexture();
    }

    void addDrop(){
        currentTexture.drawTo(new Runnable() {
            @Override
            public void run() {
                Map<String, Object> uniformMap = new HashMap<>();
                uniformMap.put("delta", new float[]{1/backTexture.getWidth(), 1/backTexture.getHeight()});
                uniformMap.put("center", new float[]{0.0f,0.0f});
                uniformMap.put("radius", 5.0f);
                uniformMap.put("strength", 5.0f);

                Map<String, AbstractBufferList> attributeMap = new HashMap<>();
                attributeMap.put("vPosition", rectangle.points());

                GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, backTexture.getTextureId());
                uniformMap.put("texture", 0);
                addDropRenderer.drawObject(uniformMap, attributeMap);
            }
        });
        swapTexture();
    }
}
