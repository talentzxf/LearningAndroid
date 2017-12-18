package max3d.core;

import android.opengl.GLES20;
import android.util.Log;

import com.example.vincentzhang.learnandroid.OpenGLRenderer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import max3d.core.AbstractBufferList;
import max3d.core.Object3d;

/**
 * Created by VincentZhang on 12/14/2017.
 */

public class ObjectRenderer {
    private Object3d $o;
    private int program;

    List<Integer> vertexAttributes = new ArrayList<>();

    public ObjectRenderer(String vertexShaderCode, String fragmentShaderCode, Object3d object3d) {
        $o = object3d;
        initShaders(vertexShaderCode, fragmentShaderCode);
    }

    private void initShaders(String vertexShaderCode, String fragmentShaderCode) {
        OpenGLRenderer.checkGlError("initShaders");
        // prepare shaders and OpenGL program
        int vertexShader = OpenGLRenderer.loadShader(
                GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        OpenGLRenderer.checkGlError("loadShader");
        int fragmentShader = OpenGLRenderer.loadShader(
                GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);
        OpenGLRenderer.checkGlError("loadShader");
        program = GLES20.glCreateProgram();             // create empty OpenGL Program
        OpenGLRenderer.checkGlError("glCreateProgram");
        GLES20.glAttachShader(program, vertexShader);   // add the vertex shader to program
        OpenGLRenderer.checkGlError("glAttachShader");
        GLES20.glAttachShader(program, fragmentShader); // add the fragment shader to program
        OpenGLRenderer.checkGlError("glAttachShader");
        GLES20.glLinkProgram(program);                  // create OpenGL program executables
        OpenGLRenderer.checkGlError("glLinkProgram");
        try {
            Log.i("OPENGL", GLES20.glGetShaderInfoLog(vertexShader));
            Log.i("OPENGL", GLES20.glGetShaderInfoLog(fragmentShader));
        } catch (Exception e) {
            Log.e("OPENGL", "Found error trying to get shader infor.");
        }
    }

    private void bindAttributes(Map<String, AbstractBufferList> shaderObjectMap) {
        for (String attributeName : shaderObjectMap.keySet()) {
            AbstractBufferList buffer = shaderObjectMap.get(attributeName);
            int newBufferHandle = GLES20.glGetAttribLocation(program, attributeName);
            OpenGLRenderer.checkGlError("glGetAttribLocation");

            if (newBufferHandle < 0) {
                Log.e("BindAttribute", "Can's get attribute for buffer:" + attributeName);
            }
            // Enable a handle to the triangle vertices
            GLES20.glEnableVertexAttribArray(newBufferHandle);
            // Prepare the triangle coordinate data
            GLES20.glVertexAttribPointer(
                    newBufferHandle, buffer.getPropertiesPerElement(),
                    GLES20.GL_FLOAT, false,
                    buffer.getBytesPerProperty() * buffer.getPropertiesPerElement(), buffer.buffer().position(0));
            OpenGLRenderer.checkGlError("glVertexAttribPointer");

            vertexAttributes.add(newBufferHandle);
        }
    }

    private void unBindAttributes() {
        for (Integer bufferHandle : vertexAttributes) {
            GLES20.glDisableVertexAttribArray(bufferHandle);
        }
        vertexAttributes.clear();
    }

    private void bindUniform(Map<String, float[]> uniformMap) throws Exception {
        for (String key : uniformMap.keySet()) {
            float[] valueArray = uniformMap.get(key);
            int uniformLocation = GLES20.glGetUniformLocation(program, key);
            OpenGLRenderer.checkGlError("glGetUniformLocation");
            if(uniformLocation < 0){
                throw new Exception("uniform " + key + " can't be found!");
            }

            switch (valueArray.length) {
                case 16: // Matrix
                    GLES20.glUniformMatrix4fv(uniformLocation, 1, false, valueArray, 0);
                    break;
                case 4:
                    GLES20.glUniform4fv(uniformLocation, 1, valueArray, 0);
                    break;
            }
            OpenGLRenderer.checkGlError("glUniformMatrix4fv");
        }
    }

    public void drawObject(Map<String, float[]> uniformMap, Map<String, AbstractBufferList> shaderObjectMap) {
        try {

            GLES20.glUseProgram(program);
            bindUniform(uniformMap);
            bindAttributes(shaderObjectMap);

            GLES20.glDrawElements(
                    $o.renderType().glValue(),
                    $o.faces().size() * $o.faces().PROPERTIES_PER_ELEMENT,
                    GLES20.GL_UNSIGNED_INT,
                    $o.faces().buffer().position(0));

            unBindAttributes();
        }catch (Exception e){
            Log.e("Error","Exception " + e);
        }
    }
}