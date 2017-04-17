package com.example.vincentzhang.Sprite;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.example.vincentzhang.learnandroid.R;

/**
 * Created by VincentZhang on 4/15/2017.
 */

public class SpriteWorld {
    ImageSprite imgSprite = new ImageSprite();
    public boolean init(Context context){
        imgSprite.load(BitmapFactory.decodeResource(context.getResources(), R.drawable.green));
        return true;
    }

    public void update(){
        imgSprite.update();;
    }

    public void draw(Canvas canvas){
        imgSprite.draw(canvas);
    }
}
