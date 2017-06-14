package com.example.vincentzhang.Sprite.Controller;

import com.example.vincentzhang.Sprite.AbstractSprite;
import com.example.vincentzhang.Sprite.ControllerAbstractSprite;
import com.example.vincentzhang.Sprite.CoordinateSystem;
import com.example.vincentzhang.Sprite.ResourceSystem.Timber;
import com.example.vincentzhang.Sprite.SpriteWorld;
import com.example.vincentzhang.Sprite.TerrainSystem.Building;
import com.example.vincentzhang.Sprite.Vector2D;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by VincentZhang on 6/14/2017.
 */

public class LoggingCampController implements Controller {
    private Building target;
    private long generateFrequency = 3000;
    private long lastTimberTime = -1;
    private int range = 200;
    private int capacity = 5;

    private ArrayList<Timber> timberList = new ArrayList<>();

    public LoggingCampController(ControllerAbstractSprite target) {
        if (!(target instanceof Building))
            throw new InvalidParameterException("Should input a building");

        this.target = (Building) target;
    }

    @Override
    public void update() {
        if (lastTimberTime == -1 || System.currentTimeMillis() > lastTimberTime + generateFrequency) {
            lastTimberTime = System.currentTimeMillis();

            ArrayList<Timber> tobeDeletedArray = new ArrayList<>();
            for(Timber timber:timberList){
                if(timber.isUsed()){
                    tobeDeletedArray.add(timber);
                }
            }

            for(Timber tobeDeletedCoin:tobeDeletedArray){
                timberList.remove(tobeDeletedCoin);
            }

            if (timberList.size() > capacity)
                return;

            Random rand = new Random();
            int X = (int) (target.getScrRect().centerX() + rand.nextInt(range));
            int Y = (int) (target.getScrRect().centerY() + rand.nextInt(range));

            Timber newTimer = new Timber(13);
            newTimer.setSpritePos(CoordinateSystem.scrToWorld(new Vector2D(X, Y)));

            SpriteWorld.getInst().getResourceSystem().addResource(newTimer);
            timberList.add(newTimer);
        }
    }


    @Override
    public void onCollide(AbstractSprite target) {

    }
}
