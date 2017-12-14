package min3d.core;

import android.opengl.GLES20;
import android.util.Log;

import com.example.vincentzhang.learnandroid.OpenGLRenderer;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by VincentZhang on 12/14/2017.
 */

public class OGLES20ObjectRender {
    private Object3d $o;
    private String vertexShaderCode;
    private String fragmentShaderCode;

    private int program;

    IntBuffer indexBuffer;

    Map<String, Number3dBufferList> shaderObjectMap = new HashMap<>();
    List<Integer> vertexAttributes = new ArrayList<>();

    public OGLES20ObjectRender(String vertexShaderCode, String fragmentShaderCode, Object3d object3d){
        $o = object3d;
        initShaders(vertexShaderCode, fragmentShaderCode);
        initBuffers(object3d);
    }

    private void initBuffers(Object3d $o) {
        indexBuffer = $o.faces().buffer();
        shaderObjectMap.put("vPosition",  $o.vertices().points());
        // shaderObjectMap.put("vNormal", $o.normals());
    }

    private void initShaders(String vertexShaderCode, String fragmentShaderCode) {
        this.vertexShaderCode = vertexShaderCode;
        this.fragmentShaderCode = fragmentShaderCode;

        // prepare shaders and OpenGL program
        int vertexShader = OpenGLRenderer.loadShader(
                GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = OpenGLRenderer.loadShader(
                GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);
        program = GLES20.glCreateProgram();             // create empty OpenGL Program
        OpenGLRenderer.checkGlError("glGetAttribLocation");
        GLES20.glAttachShader(program, vertexShader);   // add the vertex shader to program
        OpenGLRenderer.checkGlError("glGetAttribLocation");
        GLES20.glAttachShader(program, fragmentShader); // add the fragment shader to program
        OpenGLRenderer.checkGlError("glGetAttribLocation");
        GLES20.glLinkProgram(program);                  // create OpenGL program executables
        OpenGLRenderer.checkGlError("glGetAttribLocation");
        try {
            Log.i("OPENGL", GLES20.glGetShaderInfoLog(vertexShader));
            Log.i("OPENGL", GLES20.glGetShaderInfoLog(fragmentShader));
        } catch (Exception e) {
            Log.e("OPENGL", "Found error trying to get shader infor.");
        }
    }

    private void bindAttributes(){
        for(String attributeName : shaderObjectMap.keySet()){
            Number3dBufferList buffer = shaderObjectMap.get(attributeName);
            int newBufferHandle = GLES20.glGetAttribLocation(program, attributeName);
            OpenGLRenderer.checkGlError("glGetAttribLocation");

            if(newBufferHandle < 0){
                Log.e("BindAttribute", "Can's get attribute for buffer:" + attributeName);
            }
            // Enable a handle to the triangle vertices
            GLES20.glEnableVertexAttribArray(newBufferHandle);
            // Prepare the triangle coordinate data
            GLES20.glVertexAttribPointer(
                    newBufferHandle, buffer.PROPERTIES_PER_ELEMENT,
                    GLES20.GL_FLOAT, false,
                    buffer.BYTES_PER_PROPERTY*buffer.PROPERTIES_PER_ELEMENT, buffer.buffer().position(0));
            OpenGLRenderer.checkGlError("glVertexAttribPointer");

            vertexAttributes.add(newBufferHandle);
        }
    }

    private void unBindAttributes(){
        for(Integer bufferHandle: vertexAttributes){
            GLES20.glDisableVertexAttribArray(bufferHandle);
        }
        vertexAttributes.clear();
    }

    private void setUniform(Map<String, float[]> uniformMap){
        for(String key:uniformMap.keySet()){
            float[] valueArray = uniformMap.get(key);
            int uniformLocation = GLES20.glGetUniformLocation(program, key);
            OpenGLRenderer.checkGlError("glGetUniformLocation");

            // Apply the projection and view transformation
            GLES20.glUniformMatrix4fv(uniformLocation, 1, false, valueArray, 0);
            OpenGLRenderer.checkGlError("glUniformMatrix4fv");
        }
    }

    public void drawObject(Map<String, float[]> uniformMap) {
        GLES20.glUseProgram(program);
        setUniform(uniformMap);

        bindAttributes();

        GLES20.glDrawElements(
                $o.renderType().glValue(),
                $o.faces().size() * $o.faces().PROPERTIES_PER_ELEMENT,
                GLES20.GL_UNSIGNED_INT,
                $o.faces().buffer().position(0));

        unBindAttributes();
    }
}
