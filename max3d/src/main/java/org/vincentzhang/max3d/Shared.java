package org.vincentzhang.max3d;

import android.content.Context;
import android.util.Log;

import org.vincentzhang.max3d.core.TextureManager;

/**
 * Holds static references to TextureManager, and the application Context.
 * As one app has multiple windows, don't think it's a good idea to hold Renderer here.
 */
public class Shared 
{
	private static Context _context;
	private static TextureManager _textureManager = null;

	
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
	public static synchronized TextureManager textureManager()
	{
		Log.d("Texture", "Getting texture manager!");
		if(_textureManager == null){
			Log.i("Texture", "Creating texture manager!");
			_textureManager = new TextureManager();
		}
		return _textureManager;
	}

	public static synchronized void release() {
		_textureManager.reset();
		_textureManager = null;
	}
}
