package wavepattern;

import java.util.HashMap;
import java.util.Map;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.annotation.SuppressLint;
import android.content.Context;

import org.vincentzhang.max3d.Shared;
import org.vincentzhang.max3d.core.ObjectRenderer;
import org.vincentzhang.max3d.core.TextureRenderer;
import org.vincentzhang.max3d.primitives.Rectangle;

@SuppressLint("NewApi")
class MySurfaceView extends GLSurfaceView {
    private final float TOUCH_SCALE_FACTOR = 180.0f / 320;
    private SceneRenderer mRenderer;

    private float mPreviousY;
    private float mPreviousX;
    float ratio;

    static final int GEN_TEX_WIDTH = 256;
    static final int GEN_TEX_HEIGHT = 256;

    int SCREEN_WIDTH;
    int SCREEN_HEIGHT;

    public MySurfaceView(Context context) {
        super(context);
        setEGLContextClientVersion(2);

        Shared.context(context);

        setEGLConfigChooser(8, 8, 8, 8, 16, 0);

        mRenderer = new SceneRenderer();
        setRenderer(mRenderer);
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent e) {
        float y = e.getY();
        float x = e.getX();
        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE:
                float dy = y - mPreviousY;
                float dx = x - mPreviousX;
                mRenderer.yAngle += dx * TOUCH_SCALE_FACTOR;
                mRenderer.xAngle += dy * TOUCH_SCALE_FACTOR;
                requestRender();
        }
        mPreviousY = y;
        mPreviousX = x;
        return true;
    }

    private class SceneRenderer implements GLSurfaceView.Renderer {
        float yAngle;
        float xAngle;

        private TextureRenderer textureA = new TextureRenderer();
        private TextureRenderer textureB = new TextureRenderer();

        private TextureRenderer causticTexture = new TextureRenderer();

        Rectangle rectangle = new Rectangle(-2.0f, 2.0f, 1, 1);
        private ObjectRenderer objectRenderer;
        private ObjectRenderer dropRenderer;
        private ObjectRenderer updateRenderer;
        private ObjectRenderer updateNormalRenderer;
        private ObjectRenderer causticRenderer;

        boolean dropAdded = false;

        public void initFRBuffers() {
            textureA.init(GEN_TEX_WIDTH, GEN_TEX_HEIGHT);
            textureB.init(GEN_TEX_WIDTH, GEN_TEX_HEIGHT);

            dropRenderer = new ObjectRenderer("shaders/water/vertex.vert",
                    "shaders/water/drop.frag", rectangle);
            updateRenderer = new ObjectRenderer("shaders/water/vertex.vert",
                    "shaders/water/update.frag", rectangle);
            updateNormalRenderer = new ObjectRenderer("shaders/water/vertex.vert",
                    "shaders/water/normal.frag", rectangle);
            objectRenderer = new ObjectRenderer("shaders/water/showtexture.vert",
                    "shaders/water/showtexture.frag", rectangle);

            causticRenderer = new ObjectRenderer("shaders/water/caustics.vert",
                    "shaders/water/caustics.frag", rectangle);
        }

        public void addDrop() {

            textureB.drawTo(new Runnable() {
                @Override
                public void run() {
                    GLES20.glViewport(0, 0, GEN_TEX_WIDTH, GEN_TEX_HEIGHT);
                    GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);

                    Map uniformMap = new HashMap();

                    Map attributeMap = new HashMap();
                    attributeMap.put("vPosition", rectangle.points());

                    uniformMap.put("center", new float[]{0.0f, 0.0f});
                    uniformMap.put("radius", 0.03f);
                    uniformMap.put("strength", 0.1f);
                    uniformMap.put("texture", 0);

                    // Provide texture A information
                    GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
                    GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureA.getTextureId());
                    dropRenderer.drawObject(uniformMap, attributeMap);
                }
            });

            // Swap texture B to texture A
            swapRTT();
        }

        public void updateWater() {

            textureB.drawTo(new Runnable() {
                @Override
                public void run() {
                    GLES20.glViewport(0, 0, GEN_TEX_WIDTH, GEN_TEX_HEIGHT);
                    GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);

                    Map uniformMap = new HashMap();

                    Map attributeMap = new HashMap();
                    attributeMap.put("vPosition", rectangle.points());
                    uniformMap.put("texture", 0);
                    uniformMap.put("delta", new float[]{1.0f / GEN_TEX_WIDTH, 1.0f / GEN_TEX_HEIGHT});

                    // Provide texture A information
                    GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
                    GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureA.getTextureId());
                    updateRenderer.drawObject(uniformMap, attributeMap);
                }
            });

            swapRTT();
        }

        public void updateWaterNormal() {

            textureB.drawTo(new Runnable() {
                @Override
                public void run() {
                    GLES20.glViewport(0, 0, GEN_TEX_WIDTH, GEN_TEX_HEIGHT);
                    GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);

                    Map uniformMap = new HashMap();

                    Map attributeMap = new HashMap();
                    attributeMap.put("vPosition", rectangle.points());
                    uniformMap.put("texture", 0);
                    uniformMap.put("delta", new float[]{1.0f / GEN_TEX_WIDTH, 1.0f / GEN_TEX_HEIGHT});

                    // Provide texture A information
                    GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
                    GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureA.getTextureId());
                    updateNormalRenderer.drawObject(uniformMap, attributeMap);
                }
            });



            swapRTT();
        }

        private void swapRTT() {
            TextureRenderer tmp = textureA;
            textureA = textureB;
            textureB = tmp;
        }

        public void drawShadowTexture() {

            GLES20.glViewport(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
            GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);

            GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);

            MatrixState.setProjectOrtho(-ratio, ratio, -1, 1, 2, 100);

            MatrixState.setCamera(0, 0, 3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
            MatrixState.pushMatrix();

            Map uniformMap = new HashMap();
            uniformMap.put("mvpMatrix", MatrixState.getFinalMatrix());
            uniformMap.put("texture", 0);

            Map attributeMap = new HashMap();
            attributeMap.put("vPosition", rectangle.points());
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureA.getTextureId());

            objectRenderer.drawObject(uniformMap, attributeMap);
            MatrixState.popMatrix();
        }

        public void onDrawFrame(GL10 gl) {
            if (!dropAdded) {
                addDrop();
                dropAdded = true;
            }

            updateWater();
            updateWater();
            // updateWaterNormal();

            // updateWater();
            drawShadowTexture();
        }

        public void onSurfaceChanged(GL10 gl, int width, int height) {
            SCREEN_WIDTH = width;
            SCREEN_HEIGHT = height;
            ratio = (float) width / height;
            initFRBuffers();
        }

        public void onSurfaceCreated(GL10 gl, EGLConfig config) {

            GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

            GLES20.glEnable(GLES20.GL_DEPTH_TEST);

            // GLES20.glEnable(GLES20.GL_CULL_FACE);

            MatrixState.setInitStack();

            MatrixState.setLightLocation(40, 100, 20);
        }
    }
}
