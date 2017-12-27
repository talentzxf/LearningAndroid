package Sample1_5;

import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Stack;


public class MatrixState 
{
	private static float[] mProjMatrix = new float[16];
    private static float[] mVMatrix = new float[16];
    private static float[] currMatrix;
    public static float[] lightLocation=new float[]{0,0,0};
    public static FloatBuffer cameraFB;
    public static FloatBuffer lightPositionFB;
    
    public static Stack<float[]> mStack=new Stack<float[]>();
    
    public static void setInitStack()
    {
    	currMatrix=new float[16];
    	Matrix.setRotateM(currMatrix, 0, 0, 1, 0, 0);
    }
    
    public static void pushMatrix()
    {
    	mStack.push(currMatrix.clone());
    }
    
    public static void popMatrix()
    {
    	currMatrix=mStack.pop();
    }
    
    public static void translate(float x,float y,float z)
    {
    	Matrix.translateM(currMatrix, 0, x, y, z);
    }
    
    public static void rotate(float angle,float x,float y,float z)
    {
    	Matrix.rotateM(currMatrix,0,angle,x,y,z);
    }
    
    
    
    public static void setCamera
    (
    		float cx,	
    		float cy,   
    		float cz,   
    		float tx,   
    		float ty,   
    		float tz,   
    		float upx,  
    		float upy,  
    		float upz   
    )
    {
    	Matrix.setLookAtM
        (
        		mVMatrix, 
        		0, 
        		cx,
        		cy,
        		cz,
        		tx,
        		ty,
        		tz,
        		upx,
        		upy,
        		upz
        );
    	
    	float[] cameraLocation=new float[3];
    	cameraLocation[0]=cx;
    	cameraLocation[1]=cy;
    	cameraLocation[2]=cz;
    	
    	ByteBuffer llbb = ByteBuffer.allocateDirect(3*4);
        llbb.order(ByteOrder.nativeOrder());
        cameraFB=llbb.asFloatBuffer();
        cameraFB.put(cameraLocation);
        cameraFB.position(0);  
    }
    
    
    public static void setProjectFrustum
    (
    	float left,		
    	float right,    
    	float bottom,   
    	float top,      
    	float near,		
    	float far       
    )
    {
    	Matrix.frustumM(mProjMatrix, 0, left, right, bottom, top, near, far);
    }
    
    
    public static void setProjectOrtho
    (
    	float left,		
    	float right,    
    	float bottom,   
    	float top,      
    	float near,		
    	float far       
    )
    {    	
    	Matrix.orthoM(mProjMatrix, 0, left, right, bottom, top, near, far);
    }   
   
    
    public static float[] getFinalMatrix()
    {
    	float[] mMVPMatrix=new float[16];
    	Matrix.multiplyMM(mMVPMatrix, 0, mVMatrix, 0, currMatrix, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix, 0, mMVPMatrix, 0);
        return mMVPMatrix;
    }
    
    
    public static float[] getMMatrix()
    {       
        return currMatrix;
    }
    
    
    public static void setLightLocation(float x,float y,float z)
    {
    	lightLocation[0]=x;
    	lightLocation[1]=y;
    	lightLocation[2]=z;
    	ByteBuffer llbb = ByteBuffer.allocateDirect(3*4);
        llbb.order(ByteOrder.nativeOrder());
        lightPositionFB=llbb.asFloatBuffer();
        lightPositionFB.put(lightLocation);
        lightPositionFB.position(0);
    }
}
