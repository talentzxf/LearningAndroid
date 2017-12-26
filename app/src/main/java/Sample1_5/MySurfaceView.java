package Sample1_5;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.view.MotionEvent;

import com.example.vincentzhang.learnandroid.R;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import max3d.Shared;

@SuppressLint("NewApi")
class MySurfaceView extends GLSurfaceView
{
	private final float TOUCH_SCALE_FACTOR = 180.0f/320;
    private SceneRenderer mRenderer;
    
    private float mPreviousY;
    private float mPreviousX;
    float ratio;
    
    static final int GEN_TEX_WIDTH=1024;
    static final int GEN_TEX_HEIGHT=1024;
    
    int SCREEN_WIDTH;
    int SCREEN_HEIGHT;
	public MySurfaceView(Context context) {
        super(context);
        this.setEGLContextClientVersion(2);
		setEGLContextClientVersion(2);
		setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        mRenderer = new SceneRenderer();	
        setRenderer(mRenderer);				
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }
	
	
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent e)
    {
        float y = e.getY();
        float x = e.getX();
        switch (e.getAction()) {
        case MotionEvent.ACTION_MOVE:
            float dy = y - mPreviousY;
            float dx = x - mPreviousX;
            mRenderer.yAngle += dx * TOUCH_SCALE_FACTOR;
            mRenderer.xAngle+= dy * TOUCH_SCALE_FACTOR;
            requestRender();
        }
        mPreviousY = y;
        mPreviousX = x;
        return true;
    }

	private class SceneRenderer implements GLSurfaceView.Renderer
    {
		float yAngle;
    	float xAngle; 
    	
		LoadedObjectVertexNormalTexture lovo;
		
		int frameBufferId;
		int renderDepthBufferId;
		int textureId;
		int textureIdGHXP;
		TextureRect tr;
		
		public void initFRBuffers()
		{
			int tia[]=new int[1];
			GLES20.glGenFramebuffers(1, tia, 0);
			frameBufferId=tia[0];
			
			GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, frameBufferId);
			
			GLES20.glGenRenderbuffers(1, tia, 0);
			renderDepthBufferId=tia[0];
			
			GLES20.glBindRenderbuffer(GLES20.GL_RENDERBUFFER, renderDepthBufferId);
			
			GLES20.glRenderbufferStorage(GLES20.GL_RENDERBUFFER,
					GLES20.GL_DEPTH_COMPONENT16,GEN_TEX_WIDTH, GEN_TEX_HEIGHT);
			
			int[] tempIds = new int[1];
			GLES20.glGenTextures
    		(
    				1,         
    				tempIds,   
    				0           
    		);
			textureId=tempIds[0];
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,textureId);
			GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
					GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
			GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
					GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
			GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
					GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
			GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
					GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
			GLES20.glTexImage2D
        	(
        		GLES20.GL_TEXTURE_2D,
        		0,						
        		GLES20.GL_RGBA, 		
        		GEN_TEX_WIDTH,			
        		GEN_TEX_HEIGHT,			
        		0,						
        		GLES20.GL_RGBA,			
        		GLES20.GL_UNSIGNED_BYTE,
        		null
        	);
			GLES20.glFramebufferTexture2D		
            (
            	GLES20.GL_FRAMEBUFFER,
            	GLES20.GL_COLOR_ATTACHMENT0,	
            	GLES20.GL_TEXTURE_2D,
            	textureId, 						
            	0								
            );
			GLES20.glFramebufferRenderbuffer	
        	(
        		GLES20.GL_FRAMEBUFFER,
        		GLES20.GL_DEPTH_ATTACHMENT,		
        		GLES20.GL_RENDERBUFFER,			
        		renderDepthBufferId				
        	);
		}
		
		public void generateTextImage()
		{
			
			GLES20.glViewport(0, 0, GEN_TEX_WIDTH, GEN_TEX_HEIGHT);
			
			GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, frameBufferId);
    		
			GLES20.glClear( GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
            
            MatrixState.setProjectFrustum(-ratio, ratio, -1, 1, 2, 100);
            
            MatrixState.setCamera(0,0,0,0f,0f,-1f,0f,1.0f,0.0f);
            
            MatrixState.pushMatrix();
            MatrixState.translate(0, -16f, -80f);
            
            MatrixState.rotate(yAngle, 0, 1, 0);
            MatrixState.rotate(xAngle, 1, 0, 0);
            if(lovo!=null)
            {
            	lovo.drawSelf(textureIdGHXP);
            }
            MatrixState.popMatrix();
		}
		
		public void drawShadowTexture()
		{
			
			GLES20.glViewport(0,0,SCREEN_WIDTH,SCREEN_HEIGHT);
			GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
        	
			GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
            
            MatrixState.setProjectOrtho(-ratio, ratio, -1, 1, 2, 100);
            
            MatrixState.setCamera(0,0,3,0f,0f,0f,0f,1.0f,0.0f);
            MatrixState.pushMatrix();
            tr.drawSelf(textureId);
            MatrixState.popMatrix();
		}
        public void onDrawFrame(GL10 gl)
        {
        	generateTextImage();
        	drawShadowTexture();
        }
        public void onSurfaceChanged(GL10 gl, int width, int height)
        {
        	SCREEN_WIDTH=width;
        	SCREEN_HEIGHT=height;
            ratio = (float) width / height;
            initFRBuffers();
				textureIdGHXP=initTexture(R.drawable.ghxp);
            tr=new TextureRect(MySurfaceView.this,ratio);
        }
        public void onSurfaceCreated(GL10 gl, EGLConfig config)
        {   
			
        	GLES20.glClearColor(0.0f,0.0f,0.0f,1.0f);
            
        	GLES20.glEnable(GLES20.GL_DEPTH_TEST);
            
        	GLES20.glEnable(GLES20.GL_CULL_FACE);
            
            MatrixState.setInitStack();
            
            MatrixState.setLightLocation(40, 100, 20);
            
            lovo=LoadUtil.loadFromFile("ch_t.obj", MySurfaceView.this.getResources(),
            		MySurfaceView.this);
        }
    }
  	public int initTexture(int drawableId)
	{
		
		int[] textures = new int[1];
		GLES20.glGenTextures
		(
				1,          
				textures,   
				0           
		);    
		int textureId=textures[0];
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_REPEAT);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_REPEAT);
        
        
        InputStream is = this.getResources().openRawResource(drawableId);
        Bitmap bitmapTmp;
        try{bitmapTmp = BitmapFactory.decodeStream(is);}
        finally{try{is.close();}catch(IOException e){e.printStackTrace();}
        }
        
	   	GLUtils.texImage2D(
	    		GLES20.GL_TEXTURE_2D, 
	     		0,						
	     		GLUtils.getInternalFormat(bitmapTmp),
	     		bitmapTmp, 
	     		GLUtils.getType(bitmapTmp),
	     		0 
	     );
	    bitmapTmp.recycle(); 		  
        return textureId;
	}
}
