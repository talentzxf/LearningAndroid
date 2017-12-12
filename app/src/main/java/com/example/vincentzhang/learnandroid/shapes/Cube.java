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

//    private final String fragmentShaderCode =
//            "precision mediump float;" +
//                    "varying vec4 frag_pos;" +
//                    "varying vec4 normal;" +
//                    "varying vec4 v_color;" +
//                    "varying vec2 texCoord;" +
//                    "uniform vec4 lightPos;" +
//                    "uniform vec4 lightPos2;" +
//                    "uniform sampler2D u_Texture;" +    // The input texture.
//                    "const float dimp_radius = 0.1;" +
//                    "void main() {" +
//                    " float ambientStrength = 0.3;" +
//                    " vec3 lightColor = vec3(1.0,1.0,1.0);" +
//                    "vec3 ambient = ambientStrength * lightColor;" +
//                    "vec3 norm = normalize(normal.xyz);" +
//                    // "norm = normalize(norm + 2.0*texture2D(u_Texture, texCoord).xyz -1.0);" +
//                    "vec3 lightDir = normalize(lightPos.xyz - frag_pos.xyz);" +
//                    "vec3 lightDir2 = normalize(lightPos2.xyz-frag_pos.xyz);" +
//                    "float diff = max(dot(norm,lightDir),0.0);" +
//                    "float diff2 = max(dot(norm, lightDir2),0.0);" +
//                    "vec3 diffuse = diff * lightColor * v_color.xyz ;" +
//                    "vec3 result = (ambient + diffuse).xyz * texture2D(u_Texture, texCoord).xyz; " +
//
//                    "gl_FragColor = vec4(result,1.0);" +
//                    "}";

    private final String noiseFuncions = "//\n" +
            "// Description : Array and textureless GLSL 2D/3D/4D simplex \n" +
            "//               noise functions.\n" +
            "//      Author : Ian McEwan, Ashima Arts.\n" +
            "//  Maintainer : stegu\n" +
            "//     Lastmod : 20110822 (ijm)\n" +
            "//     License : Copyright (C) 2011 Ashima Arts. All rights reserved.\n" +
            "//               Distributed under the MIT License. See LICENSE file.\n" +
            "//               https://github.com/ashima/webgl-noise\n" +
            "//               https://github.com/stegu/webgl-noise\n" +
            "// \n" +
            "\n" +
            "vec3 mod289(vec3 x) {\n" +
            "  return x - floor(x * (1.0 / 289.0)) * 289.0;\n" +
            "}\n" +
            "\n" +
            "vec4 mod289(vec4 x) {\n" +
            "  return x - floor(x * (1.0 / 289.0)) * 289.0;\n" +
            "}\n" +
            "\n" +
            "vec4 permute(vec4 x) {\n" +
            "     return mod289(((x*34.0)+1.0)*x);\n" +
            "}\n" +
            "\n" +
            "vec4 taylorInvSqrt(vec4 r)\n" +
            "{\n" +
            "  return 1.79284291400159 - 0.85373472095314 * r;\n" +
            "}\n" +
            "\n" +
            "float snoise(vec3 v)\n" +
            "  { \n" +
            "  const vec2  C = vec2(1.0/6.0, 1.0/3.0) ;\n" +
            "  const vec4  D = vec4(0.0, 0.5, 1.0, 2.0);\n" +
            "\n" +
            "// First corner\n" +
            "  vec3 i  = floor(v + dot(v, C.yyy) );\n" +
            "  vec3 x0 =   v - i + dot(i, C.xxx) ;\n" +
            "\n" +
            "// Other corners\n" +
            "  vec3 g = step(x0.yzx, x0.xyz);\n" +
            "  vec3 l = 1.0 - g;\n" +
            "  vec3 i1 = min( g.xyz, l.zxy );\n" +
            "  vec3 i2 = max( g.xyz, l.zxy );\n" +
            "\n" +
            "  //   x0 = x0 - 0.0 + 0.0 * C.xxx;\n" +
            "  //   x1 = x0 - i1  + 1.0 * C.xxx;\n" +
            "  //   x2 = x0 - i2  + 2.0 * C.xxx;\n" +
            "  //   x3 = x0 - 1.0 + 3.0 * C.xxx;\n" +
            "  vec3 x1 = x0 - i1 + C.xxx;\n" +
            "  vec3 x2 = x0 - i2 + C.yyy; // 2.0*C.x = 1/3 = C.y\n" +
            "  vec3 x3 = x0 - D.yyy;      // -1.0+3.0*C.x = -0.5 = -D.y\n" +
            "\n" +
            "// Permutations\n" +
            "  i = mod289(i); \n" +
            "  vec4 p = permute( permute( permute( \n" +
            "             i.z + vec4(0.0, i1.z, i2.z, 1.0 ))\n" +
            "           + i.y + vec4(0.0, i1.y, i2.y, 1.0 )) \n" +
            "           + i.x + vec4(0.0, i1.x, i2.x, 1.0 ));\n" +
            "\n" +
            "// Gradients: 7x7 points over a square, mapped onto an octahedron.\n" +
            "// The ring size 17*17 = 289 is close to a multiple of 49 (49*6 = 294)\n" +
            "  float n_ = 0.142857142857; // 1.0/7.0\n" +
            "  vec3  ns = n_ * D.wyz - D.xzx;\n" +
            "\n" +
            "  vec4 j = p - 49.0 * floor(p * ns.z * ns.z);  //  mod(p,7*7)\n" +
            "\n" +
            "  vec4 x_ = floor(j * ns.z);\n" +
            "  vec4 y_ = floor(j - 7.0 * x_ );    // mod(j,N)\n" +
            "\n" +
            "  vec4 x = x_ *ns.x + ns.yyyy;\n" +
            "  vec4 y = y_ *ns.x + ns.yyyy;\n" +
            "  vec4 h = 1.0 - abs(x) - abs(y);\n" +
            "\n" +
            "  vec4 b0 = vec4( x.xy, y.xy );\n" +
            "  vec4 b1 = vec4( x.zw, y.zw );\n" +
            "\n" +
            "  //vec4 s0 = vec4(lessThan(b0,0.0))*2.0 - 1.0;\n" +
            "  //vec4 s1 = vec4(lessThan(b1,0.0))*2.0 - 1.0;\n" +
            "  vec4 s0 = floor(b0)*2.0 + 1.0;\n" +
            "  vec4 s1 = floor(b1)*2.0 + 1.0;\n" +
            "  vec4 sh = -step(h, vec4(0.0));\n" +
            "\n" +
            "  vec4 a0 = b0.xzyw + s0.xzyw*sh.xxyy ;\n" +
            "  vec4 a1 = b1.xzyw + s1.xzyw*sh.zzww ;\n" +
            "\n" +
            "  vec3 p0 = vec3(a0.xy,h.x);\n" +
            "  vec3 p1 = vec3(a0.zw,h.y);\n" +
            "  vec3 p2 = vec3(a1.xy,h.z);\n" +
            "  vec3 p3 = vec3(a1.zw,h.w);\n" +
            "\n" +
            "//Normalise gradients\n" +
            "  vec4 norm = taylorInvSqrt(vec4(dot(p0,p0), dot(p1,p1), dot(p2, p2), dot(p3,p3)));\n" +
            "  p0 *= norm.x;\n" +
            "  p1 *= norm.y;\n" +
            "  p2 *= norm.z;\n" +
            "  p3 *= norm.w;\n" +
            "\n" +
            "// Mix final noise value\n" +
            "  vec4 m = max(0.6 - vec4(dot(x0,x0), dot(x1,x1), dot(x2,x2), dot(x3,x3)), 0.0);\n" +
            "  m = m * m;\n" +
            "  return 42.0 * dot( m*m, vec4( dot(p0,x0), dot(p1,x1), \n" +
            "                                dot(p2,x2), dot(p3,x3) ) );\n" +
            "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
                    noiseFuncions +
                    "varying vec4 frag_pos;" +
                    "varying vec4 normal;" +
                    "varying vec4 v_color;" +
                    "varying vec2 texCoord;" +
                    "uniform vec4 lightPos;" +
                    "uniform vec4 lightPos2;" +
                    "uniform sampler2D u_Texture;" +    // The input texture.
                    "const float dimp_radius = 0.1;" +
                    "const int roughness = 4;" +
                    "float turbulence (vec3 P, int numFreq)" +
                    "{" +
                    "float val = 0.0;" +
                    "float freq = 1.0;" +
                    "for (int i=0; i<numFreq; i++) {" +
                    "val += abs (snoise (P*freq) / freq);" +
                    "freq *= 2.07;" +
                    "}" +
                    "return val;" +
                    "}" +
                    "  // Expects -1<x<1\n" +
                    "  vec3 marble_color (float x)\n" +
                    "  {\n" +
                    "    vec3 col;\n" +
                    "    x = sqrt(x);             // make x fall of rapidly...\n" +
                    "    x = sqrt(x);\n" +
                    "    x = sqrt(x);\n" +
                    "    col = vec3(.2 + .75*x);  // scale x from 0<x<1 to 0.2<x<0.95\n" +
                    "    col.b*=0.95;             // slightly reduce blue component (make color \"warmer\"):\n" +
                    "    return col;\n" +
                    "  }" +


                    "void main() {" +
                    " float ambientStrength = 0.3;" +
                    " vec3 lightColor = vec3(1.0,1.0,1.0);" +
                    "vec3 ambient = ambientStrength * lightColor;" +
                    "vec3 norm = normalize(normal.xyz);" +
                    // "norm = normalize(norm + 2.0*texture2D(u_Texture, texCoord).xyz -1.0);" +
                    "vec3 lightDir = normalize(lightPos.xyz - frag_pos.xyz);" +
                    "vec3 lightDir2 = normalize(lightPos2.xyz-frag_pos.xyz);" +
                    "float diff = max(dot(norm,lightDir),0.0);" +
                    "float diff2 = max(dot(norm, lightDir2),0.0);" +
                    "vec3 diffuse = diff * lightColor * v_color.xyz ;" +
                    // "vec3 result = (ambient + diffuse).xyz * texture2D(u_Texture, texCoord).xyz; " +
                    "float freq = 10.0;" +
                    // "float t = 6.28 * ((texCoord.x - 0.5)*(texCoord.x-0.5)+(texCoord.y - 0.5)*(texCoord.y-0.5));" +
                    "float t = 6.28 * texCoord.x;" +
                    "t += turbulence(vec3(texCoord.xy,1.0), 4);" +
                    "t=sin(t);" +
                    "vec3 result = (ambient + diffuse).xyz * marble_color(sin(freq*t))*vec3(1, 0.776, 0.294);" +
                    // "result *= vec3(sqrt(sin(freq * texCoord.x)),sqrt(sin(freq * texCoord.x)),sqrt(sin(freq * texCoord.x)));" +
//                    " if(texCoord.y < texCoord.x*texCoord.x){" +
//                    " result *= vec3(1.0,0.0,0.0); " +
//                    "}"+
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

        mTextureDataHandle = TextureHelper.loadTexture(OpenGLActivity.getContext(), R.drawable.imooc);

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
