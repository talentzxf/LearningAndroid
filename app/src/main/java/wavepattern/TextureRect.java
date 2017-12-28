package wavepattern;

import android.annotation.SuppressLint;
import android.opengl.GLES20;

import com.example.vincentzhang.learnandroid.OpenGLRenderer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;


@SuppressLint("NewApi")
public class TextureRect 
{
	int mProgram;
    int muMVPMatrixHandle;
    int maPositionHandle; 
    int maTexCoorHandle; 
    String mVertexShader;
    String mFragmentShader;
	
	FloatBuffer mVertexBuffer;
	FloatBuffer mTexCoorBuffer;
    int vCount=0;
    
    public TextureRect(MySurfaceView mv,float ratio)
    {
    	initVertexData(ratio);
    	initShader(mv);
    }
    
    
    public void initVertexData(float ratio)
    {
        vCount=6;
        final float UNIT_SIZE=1.0f;
        float vertices[]=new float[]
        {
        		-ratio*UNIT_SIZE,UNIT_SIZE,0,
        		-ratio*UNIT_SIZE,-UNIT_SIZE,0,
        		ratio*UNIT_SIZE,-UNIT_SIZE,0,
        		
        		ratio*UNIT_SIZE,-UNIT_SIZE,0,
        		ratio*UNIT_SIZE,UNIT_SIZE,0,
        		-ratio*UNIT_SIZE,UNIT_SIZE,0
        };

        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
        vbb.order(ByteOrder.nativeOrder());
        mVertexBuffer = vbb.asFloatBuffer();
        mVertexBuffer.put(vertices);
        mVertexBuffer.position(0);

        float texCoor[]=new float[]
        {    
        		0,1-0, 0,1-1.0f, 1.0f,1-1.0f,
        		1.0f,1-1.0f, 1.0f,1-0, 0,1-0  
        };        
        
        ByteBuffer cbb = ByteBuffer.allocateDirect(texCoor.length*4);
        cbb.order(ByteOrder.nativeOrder());
        mTexCoorBuffer = cbb.asFloatBuffer();
        mTexCoorBuffer.put(texCoor);
        mTexCoorBuffer.position(0);
    }

    
    public void initShader(MySurfaceView mv)
    {
    	
        mVertexShader=ShaderUtil.loadFromAssetsFile("vertex_tex.sh", mv.getResources());
        
        mFragmentShader=ShaderUtil.loadFromAssetsFile("frag_tex.sh", mv.getResources());  
        
        mProgram = ShaderUtil.createProgram(mVertexShader, mFragmentShader);
        
        maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
        
        maTexCoorHandle= GLES20.glGetAttribLocation(mProgram, "aTexCoor");
        
        muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
    }
    
    public void drawSelf(int texId)
    {        
    	 
    	 GLES20.glUseProgram(mProgram);
        OpenGLRenderer.checkGlError("glLinkProgram");
    	 MatrixState.pushMatrix();
         MatrixState.translate(0, 0, 1);
         
         GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0);
        OpenGLRenderer.checkGlError("glLinkProgram");

         GLES20.glVertexAttribPointer
         (
         		maPositionHandle,   
         		3, 
         		GLES20.GL_FLOAT,
         		false,
                3*4,   
                mVertexBuffer
         );
        OpenGLRenderer.checkGlError("glLinkProgram");
         
         GLES20.glVertexAttribPointer
         (
        		maTexCoorHandle, 
         		2, 
         		GLES20.GL_FLOAT,
         		false,
                2*4,   
                mTexCoorBuffer
         );
        OpenGLRenderer.checkGlError("glLinkProgram");
         
         GLES20.glEnableVertexAttribArray(maPositionHandle);
        OpenGLRenderer.checkGlError("glLinkProgram");
         GLES20.glEnableVertexAttribArray(maTexCoorHandle);
        OpenGLRenderer.checkGlError("glLinkProgram");
         
         
         GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        OpenGLRenderer.checkGlError("glLinkProgram");
         GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texId);
        OpenGLRenderer.checkGlError("glLinkProgram");
         
         
         GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vCount);
        OpenGLRenderer.checkGlError("glLinkProgram");
         MatrixState.popMatrix();
    }
}
