package org.vincentzhang.max3d.core;

import android.opengl.GLES20;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.example.vincentzhang.learnandroid.OpenGLRenderer;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.vincentzhang.max3d.Shared;

/**
 * Created by VincentZhang on 12/14/2017.
 */

public class ObjectRenderer {
    private Object3d $o;
    private int program;
    private final static String INCLUDE_STRING = "#include\\s*([a-zA-Z0-9/._]*)";
    private final static Pattern INCLUDE_PATTERN = Pattern.compile(INCLUDE_STRING);

    List<Integer> vertexAttributes = new ArrayList<>();
    private int vertexShader;
    private int fragmentShader;

    public Object3d getObject() {
        return $o;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    String getContentFromAssets(String path){
        return getContentFromAssets(path, new HashSet<String>());
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    String getContentFromAssets(String path, Set<String> fileNameStack){
        String returnValue = "";
        try (InputStream is = Shared.context().getAssets().open(path)){
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            returnValue = new String(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String finalResult = returnValue;

        // Replace all #include directives
        Matcher m = INCLUDE_PATTERN.matcher(returnValue);
        while(m.find()){
            String fileName = m.group(1);
            if(!fileNameStack.contains(fileName)){
                String subFileContent = getContentFromAssets(fileName, fileNameStack);
                String fileNamePattern = "#include\\s*" + fileName;
                finalResult = finalResult.replaceAll(fileNamePattern, subFileContent);
                fileNameStack.add(fileName);
            }
        }

        return finalResult;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public ObjectRenderer(String vertexShaderPath, String fragmentShaderPath, Object3d object3d) {
        $o = object3d;
        initShaders(getContentFromAssets(vertexShaderPath), getContentFromAssets(fragmentShaderPath));

        try {
            Log.i("Vertex Shader OPENGL:" + vertexShaderPath, GLES20.glGetShaderInfoLog(vertexShader));
            Log.i("Fragment Shader OPENGL:" + fragmentShaderPath, GLES20.glGetShaderInfoLog(fragmentShader));
        } catch (Exception e) {
            Log.e("OPENGL", "Found error trying to get shader infor.");
        }
    }

    private void initShaders(String vertexShaderCode, String fragmentShaderCode) {
        OpenGLRenderer.checkGlError("initShaders");
        // prepare shaders and OpenGL program
        vertexShader = OpenGLRenderer.loadShader(
                GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        OpenGLRenderer.checkGlError("loadShader");
        fragmentShader = OpenGLRenderer.loadShader(
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
    }

    private void bindAttributes(Map<String, AbstractBufferList> shaderObjectMap) {
        if(shaderObjectMap == null){
            return;
        }

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

    private void bindUniform(Map<String, Object> uniformMap) throws Exception {
        if(uniformMap == null){
            return ;
        }

        for (String key : uniformMap.keySet()) {
            Object valueArrayObj = uniformMap.get(key);
            int uniformLocation = GLES20.glGetUniformLocation(program, key);
            OpenGLRenderer.checkGlError("glGetUniformLocation");
            if (uniformLocation < 0) {
                throw new Exception("uniform " + key + " can't be found!");
            }

            if (valueArrayObj instanceof float[]) {
                float[] valueArray = (float[]) valueArrayObj;
                switch (valueArray.length) {
                    case 16: // Matrix
                        GLES20.glUniformMatrix4fv(uniformLocation, 1, false, valueArray, 0);
                        break;
                    case 4: // vec4
                        GLES20.glUniform4fv(uniformLocation, 1, valueArray, 0);
                        break;
                    case 3: // vec3
                        GLES20.glUniform3fv(uniformLocation, 1, valueArray, 0);
                        break;
                    case 2: // vec2
                        GLES20.glUniform2fv(uniformLocation, 1, valueArray, 0);
                        break;
                }
            } else if (valueArrayObj instanceof Integer) {
                GLES20.glUniform1i(uniformLocation, (Integer) valueArrayObj);
            } else if (valueArrayObj instanceof Float){
                GLES20.glUniform1f(uniformLocation, (Float) valueArrayObj);
            }
            OpenGLRenderer.checkGlError("bindUniform");
        }
    }

    public void drawObject(Map<String, Object> uniformMap, Map<String, AbstractBufferList> shaderObjectMap) {
        try {
            GLES20.glUseProgram(program);
            bindUniform(uniformMap);
            bindAttributes(shaderObjectMap);

            GLES20.glDrawElements(
                    $o.renderType().glValue(),
                    $o.faces().size() * $o.faces().PROPERTIES_PER_ELEMENT,
                    GLES20.GL_UNSIGNED_INT,
                    $o.faces().buffer().position(0));

            OpenGLRenderer.checkGlError("Draw Object");

            unBindAttributes();
        } catch (Exception e) {
            Log.e("Error", "Exception " + e);
        }
    }

    public void destroy() {
        GLES20.glDeleteShader(vertexShader);
        GLES20.glDeleteShader(fragmentShader);
        GLES20.glDeleteProgram(program);
    }
}
