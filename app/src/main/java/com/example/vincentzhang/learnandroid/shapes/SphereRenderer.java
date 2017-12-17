package com.example.vincentzhang.learnandroid.shapes;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.Matrix;

import com.example.vincentzhang.learnandroid.OpenGLActivity;
import com.example.vincentzhang.learnandroid.R;

import java.util.HashMap;
import java.util.Map;

import min3d.Shared;
import min3d.core.AbstractBufferList;
import min3d.core.Number3dBufferList;
import min3d.core.OGLES20ObjectRender;
import min3d.objectPrimitives.Sphere;
import min3d.vos.TextureVo;

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
                    "attribute vec2 vTexCoord;"+
                    "uniform vec4 vColor;" +
                    "uniform vec4 lightPos;" +
                    "varying vec4 aColor;" +
                    "varying vec4 fragPos;"+
                    "varying vec2 texCoord;"+
                    "void main() {" +
                    // The matrix must be included as a modifier of gl_Position.
                    // Note that the uMVPMatrix factor *must be first* in order
                    // for the matrix multiplication product to be correct.
                    "  gl_Position = projection * view * model * vPosition;" +
                    " aColor = vColor * dot( normalize(vNormal.xyz), normalize((lightPos - model*vPosition).xyz));" +
                    "texCoord = vTexCoord;"+
                    "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "varying vec2 texCoord;" +
                    "varying vec4 aColor;" +
                    "varying vec4 fragPos;"+
                    "uniform sampler2D u_Texture;" +
                    "void main() {" +
                    "  gl_FragColor = aColor * texture2D(u_Texture, texCoord).xyz;" +
                    "}";

    public SphereRenderer(float radius, int stacks, int slices) {
        sphereInternal = new Sphere(radius, stacks, slices);
        Bitmap earthTexture = BitmapFactory.decodeResource(OpenGLActivity.getContext().getResources(),
                R.drawable.earth);
        Shared.textureManager().addTextureId(earthTexture, "earth");
        sphereInternal.textures().add(new TextureVo("earth"));

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
        uniformMap.put("u_Texture", new float[]{Shared.textureManager().getGlTextureId("earth")});

        // setup attributes
        Map<String, AbstractBufferList> attributeMap = new HashMap<>();
        attributeMap.put("vPosition", sphereInternal.points());
        attributeMap.put("vNormal", sphereInternal.normals());
        attributeMap.put("vTexCoord", sphereInternal.uvs());

        renderer.drawObject(uniformMap, attributeMap);
    }
}
