package com.example.vincentzhang.learnandroid.shapes;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.example.vincentzhang.learnandroid.OpenGLActivity;
import com.example.vincentzhang.learnandroid.OpenGLRenderer;
import com.example.vincentzhang.learnandroid.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import max3d.Shared;
import max3d.core.AbstractBufferList;
import max3d.core.ObjectRenderer;
import max3d.primitives.Sphere;
import max3d.vos.TextureVo;

/**
 * Created by VincentZhang on 12/7/2017.
 * https://github.com/kibotu/net.gtamps/blob/refactoring3d/android/graphic/src/net/gtamps/android/renderer/graph/scene/primitives/Sphere.java
 */

public class SphereRenderer {
    private Sphere sphereInternal;
    private ObjectRenderer renderer;

//    private final String vertexShaderCode =
//            // This matrix member variable provides a hook to manipulate
//            // the coordinates of the objects that use this vertex shader
//            "uniform mat4 projection;" +
//                    "uniform mat4 view;" +
//                    "uniform mat4 model;" +
//                    "attribute vec4 vPosition;" +
//                    "attribute vec4 vNormal;" +
//                    "attribute vec2 vTexCoord;" +
//                    "uniform vec4 vColor;" +
//                    "uniform vec4 lightPos;" +
//                    "vec4 ambient = vec4(0.7,0.7,0.7,1.0);" +
//                    "varying vec4 aColor;" +
//                    "varying vec2 texCoord;" +
//                    "void main() {" +
//                    // The matrix must be included as a modifier of gl_Position.
//                    // Note that the uMVPMatrix factor *must be first* in order
//                    // for the matrix multiplication product to be correct.
//                    "  gl_Position = projection * view * model * vPosition;" +
//                    " aColor = vColor * dot( normalize(vNormal.xyz), normalize((lightPos - model*vPosition).xyz)) + ambient;" +
//                    "texCoord = vTexCoord;" +
//                    "}";
//
//    private final String fragmentShaderCode =
//            "precision mediump float;" +
//                    "varying vec2 texCoord;" +
//                    "varying vec4 aColor;" +
//                    "varying vec4 fragPos;" +
//                    "uniform sampler2D u_Texture;" +
//                    "void main() {" +
//                    "  gl_FragColor = aColor * vec4(texture2D(u_Texture, texCoord).xyz,1.0);" +
//                    "}";


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public SphereRenderer(float radius, int stacks, int slices) {
        sphereInternal = new Sphere(radius, stacks, slices);
        OpenGLRenderer.checkGlError("new Sphere");
        Bitmap earthTexture = BitmapFactory.decodeResource(OpenGLActivity.getContext().getResources(),
                R.drawable.earth);
        Shared.textureManager().addTextureId(earthTexture, "earth");
        earthTexture.recycle();
        OpenGLRenderer.checkGlError("addTextureId");
        sphereInternal.textures().add(new TextureVo("earth"));

        renderer = new ObjectRenderer("shaders/lambert_shader_v.vert",
                "shaders/lambert_shader_f.frag", sphereInternal);
    }

    // mvMatrix -- model view matrix
    // projectMatrix -- projection matrix
    public void draw(float[] model, float[] view, float[] projection) {
        float[] local_model = new float[16];
        Matrix.setIdentityM(local_model,0);
        Matrix.translateM(local_model,0, 0.0f,-0.5f,0.0f);

        // setup uniform
        Map<String, Object> uniformMap = new HashMap<>();
        uniformMap.put("projection", projection);
        uniformMap.put("view", view);
        uniformMap.put("model", local_model);
        uniformMap.put("vColor", new float[]{1.0f, 1.0f, 1.0f, 1.0f});
        uniformMap.put("lightPos", new float[]{5.0f, 10.0f, 0.0f, 1.0f});
        uniformMap.put("u_Texture", 0);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, Shared.textureManager().getGlTextureId("earth"));

        OpenGLRenderer.checkGlError("glBindTexture");

        // setup attributes
        Map<String, AbstractBufferList> attributeMap = new HashMap<>();
        attributeMap.put("vPosition", sphereInternal.points());
        attributeMap.put("vNormal", sphereInternal.normals());
        attributeMap.put("vTexCoord", sphereInternal.uvs());

        renderer.drawObject(uniformMap, attributeMap);
    }
}
