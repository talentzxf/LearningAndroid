package com.example.vincentzhang.Sprite.WeaponSystem;

import android.content.res.Resources;
import android.graphics.Canvas;

import com.example.vincentzhang.Sprite.AbstractSprite;
import com.example.vincentzhang.Sprite.ActorSprite;
import com.example.vincentzhang.Sprite.DIRECTIONS;
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

    public void addBomb(Vector2D pos, ActorSprite owner){
        Bomb bomb = new Bomb();
        bomb.setSpritePos(pos);
        bomb.setOwner(owner);
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

            // Add cur Pos
            Explosion newExplosion = new Explosion(8);
            newExplosion.setSpritePos(bomb.getSpritePos());
            newExplosion.setOwner(bomb.getOwner());
            explosions.add(newExplosion);

            int range = 3;
            for(int i = 1 ; i < range; i++){
                Explosion explosionUP = new Explosion(8, i * 500);
                explosionUP.setSpritePos(bomb.getSpritePos().applyDir(DIRECTIONS.UP, bomb.getScrRect().height() * i));
                explosionUP.setOwner(bomb.getOwner());
                Explosion explosionDOWN = new Explosion(8, i * 500);
                explosionDOWN.setSpritePos(bomb.getSpritePos().applyDir(DIRECTIONS.DOWN, bomb.getScrRect().height() * i));
                explosionDOWN.setOwner(bomb.getOwner());
                Explosion explosionLEFT = new Explosion(8, i * 500);
                explosionLEFT.setSpritePos(bomb.getSpritePos().applyDir(DIRECTIONS.LEFT, bomb.getScrRect().width() * i));
                explosionLEFT.setOwner(bomb.getOwner());
                Explosion explosionRIGHT = new Explosion(8, i * 500);
                explosionRIGHT.setSpritePos(bomb.getSpritePos().applyDir(DIRECTIONS.RIGHT, bomb.getScrRect().width() * i));
                explosionRIGHT.setOwner(bomb.getOwner());

                explosions.add(explosionUP);
                explosions.add(explosionDOWN);
                explosions.add(explosionLEFT);
                explosions.add(explosionRIGHT);
            }
        }

        for(Explosion explosion:tobeDeletedExplosions){
            explosions.remove(explosion);
        }
    }
}
