package max3d;

import android.content.Context;
import max3d.core.TextureManager;

/**
 * Holds static references to TextureManager, and the application Context.
 * As one app has multiple windows, don't think it's a good idea to hold Renderer here.
 */
public class Shared 
{
	private static Context _context;
	private static TextureManager _textureManager;

	
	public static Context context()
	{
		return _context;
	}
	public static void context(Context $c)
	{
		_context = $c;
	}

	/**
	 * You must access the TextureManager instance through this accessor
	 */
	public static TextureManager textureManager()
	{
		if(_textureManager == null){
			_textureManager = new TextureManager();
		}
		return _textureManager;
	}
}
