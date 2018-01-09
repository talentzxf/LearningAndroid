package com.example.vincentzhang.learnandroid;

import android.app.Activity;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import org.vincentzhang.max3d.Shared;

/**
 * Created by VincentZhang on 11/30/2017.
 */

public class OpenGLActivity extends Activity {
    private static Activity instance = null;
    private OpenGLSurfaceView surfaceView;

    public static Context getContext() {
        if (instance == null)
            return null;
        return instance.getApplicationContext();
    }

    @Override
    protected void onDestroy() {
        Log.i("Activity", "Destroied activity!");
        super.onDestroy();
        Shared.release();
        surfaceView.getRenderer().onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("Activity", "Oncreate activity!");
        super.onCreate(savedInstanceState);

        instance = this;
        try {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);

            surfaceView = new OpenGLSurfaceView(this);
            setContentView(surfaceView);

        } catch (Exception e) {
            Log.e("Exception happened!", "Exception during initing main window", e);
            return;
        }
    }
}
