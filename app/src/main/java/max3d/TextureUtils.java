package max3d;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

/**
 * Created by VincentZhang on 12/18/2017.
 */

public class TextureUtils {

    /**
     * Used by TextureManager
     */
    public static int uploadTextureAndReturnId(Bitmap $bitmap, boolean $generateMipMap) /*package-private*/ {
        int glTextureId;

        int[] a = new int[1];
        GLES20.glGenTextures(1, a, 0); // create a 'texture name' and put it in array element 0
        glTextureId = a[0];
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, glTextureId);

        // Set filtering
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);

//        if ($generateMipMap) {
//            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_GENERATE_MIPMAP_HINT, GLES20.GL_TRUE);
//        } else {
//            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_GENERATE_MIPMAP_HINT, GLES20.GL_FALSE);
//        }

        // 'upload' to gpu
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, $bitmap, 0);

        return glTextureId;
    }

    public static void deleteTexture(int $glTextureId) /*package-private*/ {
        int[] a = new int[1];
        a[0] = $glTextureId;

        GLES20.glDeleteTextures(1, a, 0);
    }
}
