package com.example.vincentzhang.learnandroid.shapes;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

import com.example.vincentzhang.learnandroid.OpenGLRenderer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by VincentZhang on 3/30/2017.
 */
public class Cube {

    private FloatBuffer mVertexBuffer;
    private FloatBuffer mColorBuffer;
    private ByteBuffer mIndexBuffer;

    private float vertices[] = {
            -1.0f, -1.0f, -1.0f,
            1.0f, -1.0f, -1.0f,
            1.0f, 1.0f, -1.0f,
            -1.0f, 1.0f, -1.0f,
            -1.0f, -1.0f, 1.0f,
            1.0f, -1.0f, 1.0f,
            1.0f, 1.0f, 1.0f,
            -1.0f, 1.0f, 1.0f
    };

    private float colors[] = {
            1.0f, 0.0f, 0.0f, 1.0f,
            1.0f, 0.0f, 0.0f, 1.0f,
            1.0f, 0.0f, 0.0f, 1.0f,
            1.0f, 0.0f, 0.0f, 1.0f,
            1.0f, 0.0f, 0.0f, 1.0f,
            1.0f, 0.0f, 0.0f, 1.0f,
            1.0f, 0.0f, 0.0f, 1.0f,
            1.0f, 0.0f, 0.0f, 1.0f
    };

    private byte indices[] = {
            0, 4, 5, 0, 5, 1,
            1, 5, 6, 1, 6, 2,
            2, 6, 7, 2, 7, 3,
            3, 7, 4, 3, 4, 0,
            4, 7, 6, 4, 6, 5,
            3, 0, 1, 3, 1, 2
    };

    private float lightPos[] = {3.0f, 3.0f, -3.0f, 1.0f};

    private final String vertexShaderCode =
            // This matrix member variable provides a hook to manipulate
            // the coordinates of the objects that use this vertex shader
            "uniform mat4 projection;" +
                    "uniform mat4 model;" +
                    "uniform mat4 view;" +
                    "attribute vec4 vColor;" +
                    "attribute vec4 vPosition;" +
                    "varying vec4 v_color;" +
                    "varying vec4 frag_pos;" +
                    "varying vec4 normal;" +
                    "void main() {" +
                    // The matrix must be included as a modifier of gl_Position.
                    // Note that the uMVPMatrix factor *must be first* in order
                    // for the matrix multiplication product to be correct.
                    "  gl_Position = projection * view * model * vPosition;" +
                    " frag_pos = model * vPosition;" +
                    " normal = vPosition;" +
                    "v_color = vColor;" +
                    "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "varying vec4 frag_pos;" +
                    "varying vec4 normal;" +
                    "varying vec4 v_color;" +
                    "uniform vec4 lightPos;" +
                    "void main() {" +
                    " float ambientStrength = 0.2;" +
                    " vec3 lightColor = vec3(0.5,0.5,0.5);" +
                    "vec3 ambient = ambientStrength * lightColor;" +
                    "vec3 norm = normalize(normal.xyz);" +
                    "vec3 lightDir = normalize(lightPos.xyz - frag_pos.xyz);" +
                    "float diff = max(dot(norm,lightDir),0.0);" +
                    "vec3 diffuse = diff * lightColor * v_color.xyz;" +
                    "vec3 result = (ambient + diffuse).xyz; " +
                    "gl_FragColor = vec4(result,1.0);" +
                    "}";

    private int program;
    private int mPositionHandle;
    private int mColorHandle;
    private int mModelMatrix;
    private int mViewMatrix;
    private int mProjectionMatrix;
    private int mLightPos;
    private int COORDS_PER_VERTEX = 3;
    private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex
    private final int vertexCount = vertices.length / COORDS_PER_VERTEX;

    public Cube() {

        ByteBuffer byteBuf = ByteBuffer.allocateDirect(vertices.length * 4);
        byteBuf.order(ByteOrder.nativeOrder());
        mVertexBuffer = byteBuf.asFloatBuffer();
        mVertexBuffer.put(vertices);
        mVertexBuffer.position(0);

        byteBuf = ByteBuffer.allocateDirect(colors.length * 4);
        byteBuf.order(ByteOrder.nativeOrder());
        mColorBuffer = byteBuf.asFloatBuffer();
        mColorBuffer.put(colors);
        mColorBuffer.position(0);

        mIndexBuffer = ByteBuffer.allocateDirect(indices.length);
        mIndexBuffer.put(indices);
        mIndexBuffer.position(0);

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

    // mvMatrix -- model view matrix
    // projectMatrix -- projection matrix
    public void draw(float[] model, float[] view, float[] projection) {
        GLES20.glUseProgram(program);

        // get handle to vertex shader's vPosition member
        mPositionHandle = GLES20.glGetAttribLocation(program, "vPosition");
        OpenGLRenderer.checkGlError("glGetAttribLocation");

        mColorHandle = GLES20.glGetAttribLocation(program, "vColor");
        OpenGLRenderer.checkGlError("glGetAttribLocation");

        // Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(
                mPositionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                vertexStride, mVertexBuffer);
        OpenGLRenderer.checkGlError("glGetUniformLocation");

        GLES20.glVertexAttribPointer(mColorHandle, 4, GLES20.GL_FLOAT, false, 16, mColorBuffer);
        OpenGLRenderer.checkGlError("glGetUniformLocation");
        GLES20.glEnableVertexAttribArray(mColorHandle);
        OpenGLRenderer.checkGlError("glGetUniformLocation");

        // get handle to shape's transformation matrix
        mModelMatrix = GLES20.glGetUniformLocation(program, "model");
        OpenGLRenderer.checkGlError("glGetUniformLocation");

        mViewMatrix = GLES20.glGetUniformLocation(program, "view");
        OpenGLRenderer.checkGlError("glGetUniformLocation");

        mProjectionMatrix = GLES20.glGetUniformLocation(program, "projection");
        OpenGLRenderer.checkGlError("glGetUniformLocation");

        // get handle to light's position
        mLightPos = GLES20.glGetUniformLocation(program, "lightPos");
        OpenGLRenderer.checkGlError("glGetUniformLocation");
        GLES20.glUniform4fv(mLightPos, 1, this.lightPos, 0);

        // Apply the projection and view transformation
        GLES20.glUniformMatrix4fv(mModelMatrix, 1, false, model, 0);
        OpenGLRenderer.checkGlError("glUniformMatrix4fv");

        GLES20.glUniformMatrix4fv(mViewMatrix, 1, false, view, 0);
        OpenGLRenderer.checkGlError("glUniformMatrix4fv");

        GLES20.glUniformMatrix4fv(mProjectionMatrix, 1, false, projection, 0);
        OpenGLRenderer.checkGlError("glUniformMatrix4fv");

        // Draw the triangle
        // GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, indices.length, GLES20.GL_UNSIGNED_BYTE, mIndexBuffer);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
        GLES20.glDisableVertexAttribArray(mColorHandle);
    }
}
