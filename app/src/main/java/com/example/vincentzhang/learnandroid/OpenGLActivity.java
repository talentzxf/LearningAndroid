package com.example.vincentzhang.learnandroid;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.vincentzhang.Sprite.Utilities;

/**
 * Created by VincentZhang on 11/30/2017.
 */

public class OpenGLActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);

            GLSurfaceView surfaceView = new OpenGLSurfaceView(this);
            setContentView(surfaceView);

        }catch(Exception e){
            Log.e("Exception happened!", "Exception during initing main window", e);
            return;
        }
    }
}
