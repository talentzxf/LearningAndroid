package com.example.vincentzhang.learnandroid.shapes;

import android.opengl.GLES20;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import max3d.Shared;
import max3d.core.Object3dContainer;
import max3d.core.ObjectRenderer;
import max3d.core.TextureList;
import max3d.parser.Max3DSParser;
import max3d.vos.TextureVo;

/**
 * Created by VincentZhang on 1/5/2018.
 */

public class Model3DS {
    private Object3dContainer object3dContainer;
    private List<ObjectRenderer> objectRenderers = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public Model3DS(String fileResourceString, float scale){
        // Load object from 3ds file
        Max3DSParser parser = new Max3DSParser(Shared.context().getResources(),fileResourceString,
                false, scale);
        parser.parse();
        object3dContainer = parser.getParsedObject();
        Log.i("OpenGL", " millenium_falcon loaded");

        for(int i = 0; i < object3dContainer.numChildren(); i++){
            objectRenderers.add(new ObjectRenderer("shaders/object/object.vert",
                    "shaders/object/object.frag", object3dContainer.getChildAt(i)));
        }
    }

    public void draw(float[] model, float[] view, float[] projection) {
        Map uniformMap = new HashMap();
        uniformMap.put("project", projection);
        uniformMap.put("view", view);
        uniformMap.put("model", model);
        uniformMap.put("light", new float[]{2.0f, 10.0f, -1.0f});

        for(ObjectRenderer renderer:objectRenderers){
            Map attributeMap = new HashMap();
            attributeMap.put("vPos", renderer.getObject().points());
            attributeMap.put("vNormal", renderer.getObject().normals());
            if( renderer.getObject().texturesEnabled() ){
                TextureList textureList = renderer.getObject().textures();

                for(int textureId = 0 ; textureId < textureList.size(); textureId++){
                    TextureVo textureVo = textureList.get(textureId);
                    uniformMap.put("texture", textureId);
                    GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + textureId);
                    GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, Shared.textureManager().getGlTextureId(textureVo.textureId));
                }

                attributeMap.put("texCoord", renderer.getObject().uvs());
            }
            renderer.drawObject(uniformMap, attributeMap);
        }
    }
}
