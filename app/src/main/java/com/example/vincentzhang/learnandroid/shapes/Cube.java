package com.example.vincentzhang.learnandroid.shapes;

import android.opengl.GLES20;
import android.util.Log;

import com.example.vincentzhang.learnandroid.OpenGLActivity;
import com.example.vincentzhang.learnandroid.OpenGLRenderer;
import com.example.vincentzhang.learnandroid.R;
import com.example.vincentzhang.learnandroid.Texture.TextureHelper;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by VincentZhang on 3/30/2017.
 */
public class Cube {

    private FloatBuffer mVertexBuffer;
    private FloatBuffer mColorBuffer;
    private ByteBuffer mIndexBuffer;
    private FloatBuffer mTextureBuffer;

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

    private float texCoords[] = {
            0.0f, 0.0f,
            1.0f, 0.0f,
            1.0f, 1.0f,
            0.0f, 1.0f,

            0.0f, 1.0f,
            1.0f, 1.0f,
            1.0f, 0.0f,
            0.0f, 0.0f,
    };

    private float colors[] = {
            1.0f, 1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 1.0f, 1.0f,

            1.0f, 1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 1.0f, 1.0f
    };

    private byte indices[] = {
            0, 4, 5, 0, 5, 1, // Bottom
            1, 5, 6, 1, 6, 2, // Right
            2, 6, 7, 2, 7, 3, // Top
            3, 7, 4, 3, 4, 0, // LEFT
            4, 7, 6, 4, 6, 5, // FRONT
            3, 0, 1, 3, 1, 2  // BACK
    };


    private float lightPos[] = {10.0f, 10.0f, 0.0f, 1.0f};
    private float light2Pos[] = {-100.0f, -100.0f, 0.0f, 1.0f};

    private final String vertexShaderCode =
            // This matrix member variable provides a hook to manipulate
            // the coordinates of the objects that use this vertex shader
            "uniform mat4 projection;" +
                    "const float poolHeight = 1.0;" +
                    "uniform mat4 model;" +
                    "uniform mat4 view;" +
                    "attribute vec4 vColor;" +
                    "attribute vec4 vPosition;" +
                    "attribute vec2 vTexCoord;" +
                    "varying vec4 v_color;" +
                    "varying vec4 frag_pos;" +
                    "varying vec4 normal;" +
                    "varying vec2 texCoord;" +
                    "void main() {" +
                    // The matrix must be included as a modifier of gl_Position.
                    // Note that the uMVPMatrix factor *must be first* in order
                    // for the matrix multiplication product to be correct.
                    "gl_Position = projection * view * model * vPosition;" +
                    " frag_pos = model * vPosition;" +
                    " normal = vPosition;" +
                    "v_color = vColor;" +
                    "texCoord = vTexCoord;" +
                    "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "varying vec4 frag_pos;" +
                    "varying vec4 normal;" +
                    "varying vec4 v_color;" +
                    "varying vec2 texCoord;" +
                    "uniform vec4 lightPos;" +
                    "uniform vec4 lightPos2;" +
                    "uniform sampler2D u_Texture;" +    // The input texture.
                    "const float dimp_radius = 0.1;" +
                    "void main() {" +
                    " float ambientStrength = 0.3;" +
                    " vec3 lightColor = vec3(1.0,1.0,1.0);" +
                    "vec3 ambient = ambientStrength * lightColor;" +
                    "vec3 norm = normalize(normal.xyz);" +
                    "norm = normalize(norm + 2.0*texture2D(u_Texture, texCoord).xyz -1.0);" +
                    "vec3 lightDir = normalize(lightPos.xyz - frag_pos.xyz);" +
                    "vec3 lightDir2 = normalize(lightPos2.xyz-frag_pos.xyz);" +
                    "float diff = max(dot(norm,lightDir),0.0);" +
                    "float diff2 = max(dot(norm, lightDir2),0.0);" +
                    "vec3 diffuse = diff * lightColor * v_color.xyz ;" +
                    "vec3 result = (ambient + diffuse).xyz; " + //* texture2D(u_Texture, texCoord).xyz; " +

                    "gl_FragColor = vec4(result,1.0);" +
                    "}";


    int[] textureIDs = new int[1];   // Array for 1 texture-ID (NEW)

    private int program;
    private int mPositionHandle;
    private int mColorHandle;
    private int mTexCoordHandle;
    private int mTextureUniformHandle;
    private int mTextureDataHandle;
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

        byteBuf = ByteBuffer.allocateDirect(texCoords.length * 4);
        byteBuf.order(ByteOrder.nativeOrder());
        mTextureBuffer = byteBuf.asFloatBuffer();
        mTextureBuffer.put(texCoords);
        mTextureBuffer.position(0);

        mTextureDataHandle = TextureHelper.loadTexture(OpenGLActivity.getContext(), R.drawable.bumpmap);

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

        mTexCoordHandle = GLES20.glGetAttribLocation(program, "vTexCoord");
        OpenGLRenderer.checkGlError("glGetAttribLocation");

        mTextureUniformHandle = GLES20.glGetUniformLocation(program, "u_Texture");

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureDataHandle);
        GLES20.glUniform1i(mTextureUniformHandle, 0);

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

        GLES20.glVertexAttribPointer(mTexCoordHandle, 2, GLES20.GL_FLOAT, false, 8, mTextureBuffer);
        OpenGLRenderer.checkGlError("glGetUniformLocation");
        GLES20.glEnableVertexAttribArray(mTexCoordHandle);


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

        int mLightPos2 = GLES20.glGetUniformLocation(program, "lightPos2");
        OpenGLRenderer.checkGlError("glGetUniformLocation");
        GLES20.glUniform4fv(mLightPos2, 1, this.light2Pos, 0);

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
        GLES20.glDisableVertexAttribArray(mTexCoordHandle);
    }
}
