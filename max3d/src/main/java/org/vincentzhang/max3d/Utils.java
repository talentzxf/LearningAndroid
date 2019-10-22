package org.vincentzhang.max3d;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import org.vincentzhang.max3d.core.Object3d;

import static android.opengl.GLU.gluErrorString;

public class Utils 
{
	private static final String TAG = "Max3DRenderer";

	public static final float DEG = (float)(Math.PI / 180f);
		
	private static final int BYTES_PER_FLOAT = 4;  
	
	/**
	 * Convenience method to create a Bitmap given a Context's drawable resource ID. 
	 */
	public static Bitmap makeBitmapFromResourceId(Context $context, int $id)
	{
		InputStream is = $context.getResources().openRawResource($id);
		
		Bitmap bitmap;
		try {
		   bitmap = BitmapFactory.decodeStream(is);
		} finally {
		   try {
		      is.close();
		   } catch(IOException e) {
		      // Ignore.
		   }
		}
	      
		return bitmap;
	}
	
	/**
	 * Convenience method to create a Bitmap given a drawable resource ID from the application Context. 
	 */
	public static Bitmap makeBitmapFromResourceId(int $id)
	{
		return makeBitmapFromResourceId(Shared.context(), $id);
	}
	
	/**
	 * Add two triangles to the Object3d's faces using the supplied indices
	 */
	public static void addQuad(Object3d $o, int $upperLeft, int $upperRight, int $lowerRight, int $lowerLeft)
	{
		$o.faces().add((short)$upperLeft, (short)$lowerRight, (short)$upperRight);
		$o.faces().add((short)$upperLeft, (short)$lowerLeft, (short)$lowerRight);
	}
	
	public static FloatBuffer makeFloatBuffer3(float $a, float $b, float $c)
	{
		ByteBuffer b = ByteBuffer.allocateDirect(3 * BYTES_PER_FLOAT);
		b.order(ByteOrder.nativeOrder());
		FloatBuffer buffer = b.asFloatBuffer();
		buffer.put($a);
		buffer.put($b);
		buffer.put($c);
		buffer.position(0);
		return buffer;
	}

	public static FloatBuffer makeFloatBuffer4(float $a, float $b, float $c, float $d)
	{
		ByteBuffer b = ByteBuffer.allocateDirect(4 * BYTES_PER_FLOAT);
		b.order(ByteOrder.nativeOrder());
		FloatBuffer buffer = b.asFloatBuffer();
		buffer.put($a);
		buffer.put($b);
		buffer.put($c);
		buffer.put($d);
		buffer.position(0);
		return buffer;
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
					+ " Features:" + GLES20.glGetString(GLES20.GL_EXTENSIONS));
			throw new RuntimeException(glOperation + ": glError " + error +
					" Error String:" + gluErrorString(error)
					+ "Features:" + GLES20.glGetString(GLES20.GL_EXTENSIONS));
		}
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

}
