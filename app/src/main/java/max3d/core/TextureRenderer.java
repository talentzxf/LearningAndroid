package max3d.core;

import android.opengl.GLES20;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.example.vincentzhang.learnandroid.OpenGLRenderer;

import max3d.Shared;

/**
 * Created by VincentZhang on 12/24/2017.
 */

public class TextureRenderer {
    private int frameBufferId;
    private int renderDepthBufferId;
    private int textureId;
    private int width;
    private int height;

    public void init(int width, int height) {
        this.width = width;
        this.height = height;
        int tia[] = new int[1];//用于存放产生的帧缓冲id的数组
        GLES20.glGenFramebuffers(1, tia, 0);//产生一个帧缓冲id
        frameBufferId = tia[0];//将帧缓冲id记录到成员变量中
        //绑定帧缓冲id
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, frameBufferId);

        GLES20.glGenRenderbuffers(1, tia, 0);//产生一个渲染缓冲id
        renderDepthBufferId = tia[0];//将渲染缓冲id记录到成员变量中
        //绑定指定id的渲染缓冲
        GLES20.glBindRenderbuffer(GLES20.GL_RENDERBUFFER, renderDepthBufferId);
        //为渲染缓冲初始化存储
        GLES20.glRenderbufferStorage(GLES20.GL_RENDERBUFFER,
                GLES20.GL_DEPTH_COMPONENT16, width, height);

        int[] tempIds = new int[1];//用于存放产生纹理id的数组
        GLES20.glGenTextures//产生一个纹理id
                (
                        1,         //产生的纹理id的数量
                        tempIds,   //纹理id的数组
                        0           //偏移量
                );
        textureId = tempIds[0];//将纹理id记录到成员变量
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);//绑定纹理id
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,//设置MIN采样方式
                GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,//设置MAG采样方式
                GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,//设置S轴拉伸方式
                GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,//设置T轴拉伸方式
                GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexImage2D//设置颜色附件纹理图的格式
                (
                        GLES20.GL_TEXTURE_2D,
                        0,                        //层次
                        GLES20.GL_RGBA,        //内部格式
                        width,            //宽度
                        height,            //高度
                        0,                        //边界宽度
                        GLES20.GL_RGBA,            //格式
                        GLES20.GL_FLOAT,//每个像素数据格式
                        //GLES20.GL_UNSIGNED_BYTE, //  final int GL_HALF_FLOAT_OES = 0x8D61;
                        null
                );
        Log.i("OpenGL features", GLES20.glGetString(GLES20.GL_EXTENSIONS));
        if(!GLES20.glGetString(GLES20.GL_EXTENSIONS).contains("GL_OES_texture_float")){
            Handler handler = new Handler(Looper.getMainLooper());

            handler.postDelayed(new Runnable() {
                public void run() {
                    String msg = "Can't run this application, lack of support for GL_OES_texture_float!";
                    Toast.makeText(Shared.context(),msg, Toast.LENGTH_LONG).show();
                }
            }, 10 );
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        OpenGLRenderer.checkGlError("Texture generation");
        GLES20.glFramebufferTexture2D        //设置自定义帧缓冲的颜色缓冲附件
                (
                        GLES20.GL_FRAMEBUFFER,
                        GLES20.GL_COLOR_ATTACHMENT0,    //颜色缓冲附件
                        GLES20.GL_TEXTURE_2D,
                        textureId,                        //纹理id
                        0                                //层次
                );
        GLES20.glFramebufferRenderbuffer    //设置自定义帧缓冲的深度缓冲附件
                (
                        GLES20.GL_FRAMEBUFFER,
                        GLES20.GL_DEPTH_ATTACHMENT,        //深度缓冲附件
                        GLES20.GL_RENDERBUFFER,            //渲染缓冲
                        renderDepthBufferId                //渲染深度缓冲id
                );

        // Restore frame buffer and render buffer
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);//绑定帧缓冲id
        OpenGLRenderer.checkGlError("Texture generation");
    }

    // Draw the callback into the texture
    public void drawTo(Runnable callback) {
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, frameBufferId);
        callback.run();
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);//绑定帧缓冲id
        GLES20.glBindRenderbuffer(GLES20.GL_RENDERBUFFER, 0);
    }

    public int getTextureId() {
        return textureId;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getFrameBufferId() {
        return frameBufferId;
    }

    public int getRenderBufferId() {
        return renderDepthBufferId;
    }

    public void destroy() {
        int[] tempIds = new int[1];
        tempIds[0] = textureId;
        GLES20.glDeleteTextures(1, tempIds,0);

        tempIds[0] = renderDepthBufferId;
        GLES20.glDeleteRenderbuffers(1, tempIds,0);

        tempIds[0] = frameBufferId;
        GLES20.glDeleteFramebuffers(1, tempIds, 0);
    }
}
