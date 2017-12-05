package com.example.vincentzhang.learnandroid;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

import com.example.vincentzhang.learnandroid.Camera.Camera;
import com.example.vincentzhang.learnandroid.shapes.Cube;
import com.example.vincentzhang.learnandroid.shapes.Square;
import com.example.vincentzhang.learnandroid.shapes.Triangle;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLU.gluErrorString;

/**
 * Created by VincentZhang on 3/30/2017.
 */

public class OpenGLRenderer implements GLSurfaceView.Renderer {
    private Cube mCube;
    private float mCubeRotation;

    private static final String TAG = "MyGLRenderer";
    private Triangle mTriangle;
    private Square mSquare;
    private Camera camera;

    // mMVPMatrix is an abbreviation for "Model View Projection Matrix"
    private final float[] mModelMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private float[] mViewMatrix;

    private float ratio;

    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {

        // Set the background frame color
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        mTriangle = new Triangle();
        mSquare = new Square();
        mCube = new Cube();
        camera = new Camera();
        camera.setPos(new float[]{0.0f,0.0f,-7.0f});
        camera.setLookAt(new float[]{0.0f,0.0f,0.0f});
    }

    @Override
    public void onDrawFrame(GL10 unused) {
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        GLES20.glDepthFunc(GLES20.GL_LEQUAL);
        GLES20.glDepthMask(true);

        float[] scratch = new float[16];

        // Draw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        // Set the camera position (View matrix)
        mViewMatrix = camera.getViewMatrix();

        // Set model matrix
        Matrix.setIdentityM(mModelMatrix, 0);
        mCube.draw(mModelMatrix, mViewMatrix, mProjectionMatrix);
    }

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        // Adjust the viewport based on geometry changes,
        // such as screen rotation
        GLES20.glViewport(0, 0, width, height);

        ratio = (float) width / height;
        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
        Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 3, 20);
        camera.setViewport(width, height);
    }

    /**
     * Utility method for compiling a OpenGL shader.
     * <p>
     * <p><strong>Note:</strong> When developing shaders, use the checkGlError()
     * method to debug shader coding errors.</p>
     *
     * @param type       - Vertex or fragment shader type.
     * @param shaderCode - String containing the shader code.
     * @return - Returns an id for the shader.
     */
    public static int loadShader(int type, String shaderCode) {

        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }

    /**
     * Utility method for debugging OpenGL calls. Provide the name of the call
     * just after making it:
     * <p>
     * <pre>
     * mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
     * MyGLRenderer.checkGlError("glGetUniformLocation");</pre>
     * <p>
     * If the operation is not successful, the check throws an error.
     *
     * @param glOperation - Name of the OpenGL call to check.
     */
    public static void checkGlError(String glOperation) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e(TAG, glOperation + ": glError " + error + " Error String:" + gluErrorString(error));
            throw new RuntimeException(glOperation + ": glError " + error + " Error String:" + gluErrorString(error));
        }
    }

    public Camera getCamera(){
        return camera;
    }
}
