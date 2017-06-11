package com.example.vincentzhang.Sprite.Controller;

import com.example.vincentzhang.Sprite.AbstractSprite;
import com.example.vincentzhang.Sprite.ControllerAbstractSprite;
import com.example.vincentzhang.Sprite.CoordinateSystem;
import com.example.vincentzhang.Sprite.ResourceSystem.Coin;
import com.example.vincentzhang.Sprite.SpriteWorld;
import com.example.vincentzhang.Sprite.TerrainSystem.Building;
import com.example.vincentzhang.Sprite.Vector2D;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by VincentZhang on 6/6/2017.
 */

public class GoldMineController implements Controller {
    private Building target;
    private long generateFrequency = 3000;
    private long lastCoinTime = -1;
    private int range = 200;
    private int capacity = 5;

    private ArrayList<Coin> coinList = new ArrayList<>();

    public GoldMineController(ControllerAbstractSprite target) {
        if (!(target instanceof Building))
            throw new InvalidParameterException("Should input a building");

        this.target = (Building) target;
    }

    @Override
    public void update() {
        if (lastCoinTime == -1 || System.currentTimeMillis() > lastCoinTime + generateFrequency) {
            lastCoinTime = System.currentTimeMillis();

            ArrayList<Coin> tobeDeletedArray = new ArrayList<>();
            for(Coin coin:coinList){
                if(coin.isUsed()){
                    tobeDeletedArray.add(coin);
                }
            }

            for(Coin tobeDeletedCoin:tobeDeletedArray){
                coinList.remove(tobeDeletedCoin);
            }

            if (coinList.size() > capacity)
                return;

            Random rand = new Random();
            int X = (int) (target.getScrRect().centerX() + rand.nextInt(range));
            int Y = (int) (target.getScrRect().centerY() + rand.nextInt(range));

            Coin newCoin = new Coin(12);
            newCoin.setSpritePos(CoordinateSystem.scrToWorld(new Vector2D(X, Y)));

            SpriteWorld.getInst().getResourceSystem().addResource(newCoin);
            coinList.add(newCoin);
        }
    }

    @Override
    public void onCollide(AbstractSprite target) {

    }
}
