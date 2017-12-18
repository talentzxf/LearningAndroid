package max3d.vos;

import android.opengl.GLES20;

public class TexEnvxVo
{
	public int pname = -1;
	public int param = -1;

	public TexEnvxVo()
	{
	}
	
	public TexEnvxVo(int $pname, int $param)
	{
		pname = $pname;
		param = $param;
	}

	/**
	 * Convenience method
	 */
	public void setAll(int $pname, int $param)
	{
		pname = $pname;
		param = $param;
	}
}
