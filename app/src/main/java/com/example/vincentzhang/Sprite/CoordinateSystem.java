package com.example.vincentzhang.Sprite;


/**
 * Created by VincentZhang on 4/21/2017.
 */

public class CoordinateSystem {
    private static Vector2D scrDimension;
    private static Vector2D viewPortPos = new Vector2D(); //Left-Top point of the view port
    private static Vector2D tileDimension;

    public static Vector2D getTileDimension() {
        return tileDimension;
    }

    public static void setTileDimension(Vector2D tileDimension) {
        CoordinateSystem.tileDimension = tileDimension;
    }

    public static int getScrWidth(){
        return (int) scrDimension.getX();
    }

    public static int getScrHeight(){
        return (int) scrDimension.getY();
    }

    public static Vector2D getScrDimension() {
        return scrDimension;
    }

    public static void setScrDimension(Vector2D inScrDimension) {
        scrDimension = inScrDimension;
    }

    public static Vector2D getViewPortPos() {
        return viewPortPos;
    }

    public static void setViewPortPos(Vector2D inViewPortPos) {
        viewPortPos = inViewPortPos;
    }

    public static Vector2D worldToScr(Vector2D worldPos){
        return worldPos.minus(viewPortPos);
    }

    public static Vector2D scrToWorld(Vector2D scrPos){
        return scrPos.add(viewPortPos);
    }

    /**
     * Return the left-top world coordination of the grid.
     * @param gridPos
     * @return
     */
    public static Vector2D gridToWorld(Vector2D gridPos){
        int realWorld_x = (int) (gridPos.getX() * tileDimension.getX());
        int realWorld_y = (int) (gridPos.getY() * tileDimension.getY() - gridPos.getX() * tileDimension.getX());

        return new Vector2D(realWorld_x, realWorld_y);
    }
}
