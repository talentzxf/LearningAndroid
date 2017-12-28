package com.example.vincentzhang.learnandroid;

import android.util.Log;

import com.example.vincentzhang.learnandroid.R;
import min3d.core.Object3dContainer;
import min3d.core.RendererActivity;
import min3d.parser.IParser;
import min3d.parser.Parser;
import min3d.vos.Light;

/**
 * Created by VincentZhang on 12/12/2017.
 */

public class Min3DLoadObjActivity extends RendererActivity {
    private Object3dContainer objModel;

    @Override
    public void initScene() {
        scene.lights().add(new Light());

        Log.i("Load obj"," Begin to load obj!");
        IParser parser = Parser.createParser(Parser.Type.OBJ,
                getResources(),  getResources().getResourceName(R.raw.ironman_obj), true);
        parser.parse();

        objModel = parser.getParsedObject();
        objModel.scale().x = objModel.scale().y = objModel.scale().z = .7f;
        objModel.doubleSidedEnabled(true);
        objModel.scale().multiply(0.002f);
        scene.addChild(objModel);

        Log.i("Load obj"," Obj load done!");
    }

    @Override
    public void updateScene() {
        objModel.rotation().x++;
        objModel.rotation().z++;
    }

    @Override
    protected void glSurfaceViewConfig() {
        super.glSurfaceViewConfig();
        glSurfaceView().setEGLContextClientVersion(1);
        glSurfaceView().setEGLConfigChooser(8 , 8, 8, 8, 16, 0);
    }
}
