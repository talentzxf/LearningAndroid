package com.example.vincentzhang.learnandroid;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.example.vincentzhang.learnandroid.Camera.Camera;
import com.example.vincentzhang.learnandroid.shapes.Cube;
import com.example.vincentzhang.learnandroid.shapes.MoocCube;
import com.example.vincentzhang.learnandroid.shapes.SphereRenderer;
import com.example.vincentzhang.learnandroid.shapes.Square;
import com.example.vincentzhang.learnandroid.shapes.Triangle;
import com.example.vincentzhang.learnandroid.shapes.Wall;
import com.example.vincentzhang.learnandroid.shapes.Water;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import max3d.Shared;
import max3d.core.Object3dContainer;
import max3d.parser.Max3DSParser;
import max3d.primitives.Rectangle;

import static android.opengl.GLU.gluErrorString;

/**
 * Created by VincentZhang on 3/30/2017.
 */

public class OpenGLRenderer implements GLSurfaceView.Renderer {
    private Cube mCube;
    private MoocCube mMoocCube;
    private float mCubeRotation;

    private static final String TAG = "MyGLRenderer";
    private Triangle mTriangle;
    private Square mSquare;
    private Water waterSurface;
    private Wall wall;
    private SphereRenderer sphereRenderer;
    private Camera camera;

    // mMVPMatrix is an abbreviation for "Model View Projection Matrix"
    private final float[] mModelMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private float[] mViewMatrix;

    private float ratio;
    private long startTime = System.currentTimeMillis();

    private Object3dContainer millenium_falcon;

    public OpenGLRenderer() {
        super();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        Log.i("OpenGL", "onSurfaceCreated!");
        // Set the background frame color
        GLES20.glClearColor(0.3f, 0.3f, 0.3f, 1.0f);

        camera = new Camera();
        camera.setPos(new float[]{0.0f, 0.0f, -7.0f});
        camera.setLookAt(new float[]{0.0f, 0.0f, 0.0f});

        mTriangle = new Triangle();
        mSquare = new Square();
        mCube = new Cube();
        mMoocCube = new MoocCube();
        waterSurface = new Water(2.0f,2.0f,100,100, camera);
         wall = new Wall();
         wall.setWater(waterSurface);

        sphereRenderer = new SphereRenderer(0.3f, 30, 30);
        sphereRenderer.setWater(waterSurface);

        // Load object from 3ds file
//        Max3DSParser millenium_falcon_parser = new Max3DSParser(Shared.context().getResources(),
//                "com.example.vincentzhang.learnandroid:raw/milleniumfalcon3ds", false);
//        millenium_falcon_parser.parse();
//        millenium_falcon = millenium_falcon_parser.getParsedObject();
//        Log.i("OpenGL", " millenium_falcon loaded");
    }

    private int frameCount = 0;
    @Override
    public void onDrawFrame(GL10 unused) {
        if(frameCount % 1000 == 0){
            Log.i("OpenGL", "Totally drawed:" + frameCount + " frames!");
            frameCount++;
        }
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_DST_ALPHA, GLES20.GL_ONE_MINUS_DST_ALPHA);
        GLES20.glDepthFunc(GLES20.GL_LEQUAL);
        GLES20.glDepthMask(true);

        float[] scratch = new float[16];

        GLES20.glViewport(0,0,
                (int)camera.getViewportWidth(), (int)camera.getViewportHeight());
        // Draw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        // Set the camera position (View matrix)
        mViewMatrix = camera.getViewMatrix();

        // Set model matrix
        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.scaleM(mModelMatrix, 0, 1.0f, 1.0f, 1.0f);
        float elapsedTimeSec = ((float)(System.currentTimeMillis() - startTime)/1000.0f);
        // Matrix.rotateM(mModelMatrix,0, elapsedTimeSec * 10, 0.0f, 1.0f, 0.0f);
        //mMoocCube.draw(mModelMatrix, mViewMatrix, mProjectionMatrix);
        // mCube.draw(mModelMatrix, mViewMatrix, mProjectionMatrix);

        waterSurface.draw(mModelMatrix, mViewMatrix, mProjectionMatrix);

        GLES20.glViewport(0,0,
                (int)camera.getViewportWidth(), (int)camera.getViewportHeight());
        sphereRenderer.draw(mModelMatrix, mViewMatrix, mProjectionMatrix);
        wall.draw(mModelMatrix, mViewMatrix, mProjectionMatrix);
    }

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        Log.i("OpenGL", "surface changed!");
        // Adjust the viewport based on geometry changes,
        // such as screen rotation
        GLES20.glViewport(0, 0, width, height);

        ratio = (float) width / height;
        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
        Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 3, 20);
        camera.setViewport(width, height);

        camera.rotate( 10, 10);
        Log.i("OpenGL", "surface changed finished!");
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
            Log.e(TAG, glOperation + ": glError " + error + " Error String:" + gluErrorString(error)
                    + "Features:" + GLES20.glGetString(GLES20.GL_EXTENSIONS));
            throw new RuntimeException(glOperation + ": glError " + error +
                    " Error String:" + gluErrorString(error)
                    + "Features:" + GLES20.glGetString(GLES20.GL_EXTENSIONS));
        }
    }

    public Camera getCamera() {
        return camera;
    }

    public void onDestroy() {
        waterSurface.onDestroy();
    }
}
