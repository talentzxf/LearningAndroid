package Sample1_5;

import android.annotation.SuppressLint;
import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;


@SuppressLint("NewApi") public class LoadedObjectVertexNormalTexture
{	
	int mProgram;
    int muMVPMatrixHandle;
    int muMMatrixHandle;
    int maPositionHandle; 
    int maNormalHandle; 
    int maLightLocationHandle;
    int maCameraHandle; 
    int maTexCoorHandle; 
    String mVertexShader;
    String mFragmentShader;
	
	FloatBuffer mVertexBuffer;
	FloatBuffer mNormalBuffer;
	FloatBuffer mTexCoorBuffer;
    int vCount=0;
    MySurfaceView mv;
    boolean initFlag=false;
    public LoadedObjectVertexNormalTexture(MySurfaceView mv,float[] vertices,float[] normals,float texCoors[])
    {    	
    	this.mv=mv;
    	
    	initVertexData(vertices,normals,texCoors);
    }
    
    
    public void initVertexData(float[] vertices,float[] normals,float texCoors[])
    {
    	
    	vCount=vertices.length/3;   
		
        
        
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
        vbb.order(ByteOrder.nativeOrder());
        mVertexBuffer = vbb.asFloatBuffer();
        mVertexBuffer.put(vertices);
        mVertexBuffer.position(0);
        
        
        
        
        
        ByteBuffer cbb = ByteBuffer.allocateDirect(normals.length*4);
        cbb.order(ByteOrder.nativeOrder());
        mNormalBuffer = cbb.asFloatBuffer();
        mNormalBuffer.put(normals);
        mNormalBuffer.position(0);
        
        
        
        
        
        ByteBuffer tbb = ByteBuffer.allocateDirect(texCoors.length*4);
        tbb.order(ByteOrder.nativeOrder());
        mTexCoorBuffer = tbb.asFloatBuffer();
        mTexCoorBuffer.put(texCoors);
        mTexCoorBuffer.position(0);
        
        
        
    }

    
    public void initShader()
    {
    	
        mVertexShader=ShaderUtil.loadFromAssetsFile("vertex.sh", mv.getResources());
        
        mFragmentShader=ShaderUtil.loadFromAssetsFile("frag.sh", mv.getResources());  
        
        mProgram = ShaderUtil.createProgram(mVertexShader, mFragmentShader);
        
        maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
        
        maNormalHandle= GLES20.glGetAttribLocation(mProgram, "aNormal");
        
        muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        
        muMMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMMatrix");
        
        maLightLocationHandle= GLES20.glGetUniformLocation(mProgram, "uLightLocation");
        
        maTexCoorHandle= GLES20.glGetAttribLocation(mProgram, "aTexCoor");
        
        maCameraHandle= GLES20.glGetUniformLocation(mProgram, "uCamera");
    }
    
    public void drawSelf(int texId)
    {
    	if(!initFlag)
    	{
    		initShader();
    		initFlag=true;
    	}
    	
    	GLES20.glUseProgram(mProgram);
    	MatrixState.pushMatrix();
    	
    	GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0);
    	
    	GLES20.glUniformMatrix4fv(muMMatrixHandle, 1, false, MatrixState.getMMatrix(), 0);
    	
    	GLES20.glUniform3fv(maLightLocationHandle, 1, MatrixState.lightPositionFB);
    	
    	GLES20.glUniform3fv(maCameraHandle, 1, MatrixState.cameraFB);
    	
    	GLES20.glVertexAttribPointer
    	(
    			maPositionHandle,   
    			3, 
    			GLES20.GL_FLOAT,
    			false,
    			3*4,   
    			mVertexBuffer
    			);       
    	
    	GLES20.glVertexAttribPointer
    	(
    			maNormalHandle, 
    			3,   
    			GLES20.GL_FLOAT,
    			false,
    			3*4,   
    			mNormalBuffer
    			);   
    	
    	GLES20.glVertexAttribPointer
    	(
    			maTexCoorHandle, 
    			2, 
    			GLES20.GL_FLOAT,
    			false,
    			2*4,   
    			mTexCoorBuffer
    			);
    	
    	GLES20.glEnableVertexAttribArray(maPositionHandle);
    	GLES20.glEnableVertexAttribArray(maNormalHandle);
    	GLES20.glEnableVertexAttribArray(maTexCoorHandle);
    	
    	GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
    	GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texId);
    	
    	GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vCount);
    	MatrixState.popMatrix();
    }
}
