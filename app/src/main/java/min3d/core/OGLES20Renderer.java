package min3d.core;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

/**
 * Created by VincentZhang on 12/17/2017.
 */

public class OGLES20Renderer extends Renderer{

    public OGLES20Renderer(Scene $scene) {
        super($scene);
    }

    /**
     * Used by TextureManager
     */
    int uploadTextureAndReturnId(Bitmap $bitmap, boolean $generateMipMap) /*package-private*/
    {
        int glTextureId;

        int[] a = new int[1];
        GLES20.glGenTextures(1, a, 0); // create a 'texture name' and put it in array element 0
        glTextureId = a[0];
        GLES20.glBindTexture(GL10.GL_TEXTURE_2D, glTextureId);

        // Set filtering
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);

        if($generateMipMap) {
            GLES20.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_GENERATE_MIPMAP, GL11.GL_TRUE);
        } else {
            GLES20.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_GENERATE_MIPMAP, GL11.GL_FALSE);
        }

        // 'upload' to gpu
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, $bitmap, 0);

        return glTextureId;
    }
}
