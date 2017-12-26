package Sample1_5;

import android.annotation.SuppressLint;
import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

//����������
@SuppressLint("NewApi")
public class TextureRect 
{
	int mProgram;//�Զ�����Ⱦ���߳���id
    int muMVPMatrixHandle;//�ܱ任��������
    int maPositionHandle; //����λ����������
    int maTexCoorHandle; //��������������������
    String mVertexShader;//������ɫ������ű�
    String mFragmentShader;//ƬԪ��ɫ������ű�
	
	FloatBuffer mVertexBuffer;//�����������ݻ���
	FloatBuffer mTexCoorBuffer;//���������������ݻ���
    int vCount=0;
    
    public TextureRect(MySurfaceView mv,float ratio)
    {    	
    	//��ʼ���������ݷ���
    	initVertexData(ratio);
    	//��ʼ����ɫ������
    	initShader(mv);
    }
    
    //��ʼ���������ݷ���
    public void initVertexData(float ratio)
    {
    	//�����������ݵĳ�ʼ��================begin============================
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
		
        //���������������ݻ���
        //vertices.length*4����Ϊһ�������ĸ��ֽ�
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
        vbb.order(ByteOrder.nativeOrder());//�����ֽ�˳��
        mVertexBuffer = vbb.asFloatBuffer();//ת��ΪFloat�ͻ���
        mVertexBuffer.put(vertices);//�򻺳����з��붥����������
        mVertexBuffer.position(0);//���û�������ʼλ��
        //�ر���ʾ�����ڲ�ͬƽ̨�ֽ�˳��ͬ���ݵ�Ԫ�����ֽڵ�һ��Ҫ����ByteBuffer
        //ת�����ؼ���Ҫͨ��ByteOrder����nativeOrder()�������п��ܻ������
        //�����������ݵĳ�ʼ��================end============================
        
        //���������������ݵĳ�ʼ��================begin============================
        float texCoor[]=new float[]//������ɫֵ���飬ÿ������4��ɫ��ֵRGBA
        {    
        		0,1-0, 0,1-1.0f, 1.0f,1-1.0f,
        		1.0f,1-1.0f, 1.0f,1-0, 0,1-0  
        };        
        //�������������������ݻ���
        ByteBuffer cbb = ByteBuffer.allocateDirect(texCoor.length*4);
        cbb.order(ByteOrder.nativeOrder());//�����ֽ�˳��
        mTexCoorBuffer = cbb.asFloatBuffer();//ת��ΪFloat�ͻ���
        mTexCoorBuffer.put(texCoor);//�򻺳����з��붥����������
        mTexCoorBuffer.position(0);//���û�������ʼλ��
        //�ر���ʾ�����ڲ�ͬƽ̨�ֽ�˳��ͬ���ݵ�Ԫ�����ֽڵ�һ��Ҫ����ByteBuffer
        //ת�����ؼ���Ҫͨ��ByteOrder����nativeOrder()�������п��ܻ������
        //���������������ݵĳ�ʼ��================end============================
    }

    //��ʼ����ɫ������
    public void initShader(MySurfaceView mv)
    {
    	//���ض�����ɫ���Ľű�����
        mVertexShader=ShaderUtil.loadFromAssetsFile("vertex_tex.sh", mv.getResources());
        //����ƬԪ��ɫ���Ľű�����
        mFragmentShader=ShaderUtil.loadFromAssetsFile("frag_tex.sh", mv.getResources());  
        //���ڶ�����ɫ����ƬԪ��ɫ����������
        mProgram = ShaderUtil.createProgram(mVertexShader, mFragmentShader);
        //��ȡ�����ж���λ����������
        maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
        //��ȡ�����ж�������������������
        maTexCoorHandle= GLES20.glGetAttribLocation(mProgram, "aTexCoor");
        //��ȡ�������ܱ任��������
        muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
    }
    
    public void drawSelf(int texId)
    {        
    	 //ָ��ʹ��ĳ����ɫ����
    	 GLES20.glUseProgram(mProgram);
    	 MatrixState.pushMatrix();
         //������Z������λ��1
         MatrixState.translate(0, 0, 1);
         //�����ձ任��������Ⱦ����
         GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0);
         //������λ�����ݴ�����Ⱦ����
         GLES20.glVertexAttribPointer
         (
         		maPositionHandle,   
         		3, 
         		GLES20.GL_FLOAT,
         		false,
                3*4,   
                mVertexBuffer
         );       
         //�����������������ݴ�����Ⱦ����
         GLES20.glVertexAttribPointer
         (
        		maTexCoorHandle, 
         		2, 
         		GLES20.GL_FLOAT,
         		false,
                2*4,   
                mTexCoorBuffer
         );   
         //���ö���λ����������
         GLES20.glEnableVertexAttribArray(maPositionHandle);
         //���ö���������������
         GLES20.glEnableVertexAttribArray(maTexCoorHandle);
         
         //������
         GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
         GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texId);
         
         //�����������
         GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vCount);
         MatrixState.popMatrix();
    }
}
