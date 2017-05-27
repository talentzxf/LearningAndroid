package com.example.vincentzhang.Sprite.WeaponSystem;

import android.content.res.Resources;
import android.graphics.Canvas;

import com.example.vincentzhang.Sprite.AbstractSprite;
import com.example.vincentzhang.Sprite.ImageSprite;
import com.example.vincentzhang.Sprite.SubSystem;
import com.example.vincentzhang.Sprite.Vector2D;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by VincentZhang on 4/24/2017.
 */

public class WeaponSystem implements SubSystem {

    private ConcurrentLinkedQueue<Bomb> bombs = new ConcurrentLinkedQueue<>();
    private ConcurrentLinkedQueue<Explosion> explosions = new ConcurrentLinkedQueue<>();


    @Override
    public boolean init(String level, Resources resources, Canvas canvas) {
//        XPath xPath = XPathFactory.newInstance().newXPath();
//        try {
//            NodeList weaponDefs = (NodeList) xPath.evaluate("/weapons/node()", getXmlSource(resources, level), XPathConstants.NODESET);
//            for(int i = 0 ; i < weaponDefs.getLength(); i++){
//
//            }
//
//        } catch (XPathExpressionException e) {
//            Log.e("XPath error", "Error!");
//            return false;
//        }
        return true;
    }

    public void addBomb(Vector2D pos){
        Bomb bomb = new Bomb();
        bomb.setSpritePos(pos);
        this.bombs.add(bomb);
    }

    @Override
    public void draw(Canvas canvas) {
        for (AbstractSprite bomb : bombs) {
            bomb.draw(canvas);
        }

        for(AbstractSprite explosion:explosions){
            explosion.draw(canvas);
        }
    }

    @Override
    public void beforeCollision() {
        for (AbstractSprite bomb : bombs) {
            bomb.beforeCollision();
        }

        for(AbstractSprite explosion: explosions){
            explosion.beforeCollision();
        }
    }

    @Override
    public AbstractSprite detectCollide(ImageSprite imgSprite) {
        for (AbstractSprite bomb : bombs) {
            if (bomb.detectCollide(imgSprite))
                return bomb;
        }

        for(AbstractSprite explosion: explosions){
            if(explosion.detectCollide(imgSprite))
                return explosion;
        }

        return null;
    }

    @Override
    public void preUpdate() {

    }

    @Override
    public void postUpdate() {
        ArrayList<Bomb> tobeDeletedBoms = new ArrayList<>();
        for (Bomb bomb : bombs) {
            bomb.postUpdate();
            if(bomb.isExploded()){
                tobeDeletedBoms.add(bomb);
            }
        }

        ArrayList<Explosion> tobeDeletedExplosions = new ArrayList<>();
        for( Explosion explosion: explosions){
            if(!explosion.isAlive()){
                tobeDeletedExplosions.add(explosion);
            }
        }

        for(Bomb bomb:tobeDeletedBoms){
            bombs.remove(bomb);
            Explosion newExplosion = new Explosion(8);
            newExplosion.setSpritePos(bomb.getSpritePos());
            explosions.add(newExplosion);
        }

        for(Explosion explosion:tobeDeletedExplosions){
            explosions.remove(explosion);
        }
    }
}
