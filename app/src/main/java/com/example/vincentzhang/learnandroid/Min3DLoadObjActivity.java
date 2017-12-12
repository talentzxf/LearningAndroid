package com.example.vincentzhang.learnandroid;

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

        IParser parser = Parser.createParser(Parser.Type.OBJ,
                getResources(),  getResources().getResourceName(R.raw.camaro_obj), true);
        parser.parse();

        objModel = parser.getParsedObject();
        objModel.scale().x = objModel.scale().y = objModel.scale().z = .7f;
        scene.addChild(objModel);
    }

    @Override
    public void updateScene() {
        objModel.rotation().x++;
        objModel.rotation().z++;
    }
}
