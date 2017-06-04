package com.example.vincentzhang.Sprite;

/**
 * Created by VincentZhang on 4/21/2017.
 */

public class Vector2D {
    private static final double eps = 0.001;
    private double x = 0.0;
    private double y = 0.0;

    public Vector2D() {
    }

    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public Vector2D add(Vector2D v) {
        return new Vector2D(this.getX() + v.getX(), this.getY() + v.getY());
    }

    public Vector2D minus(Vector2D v) {
        return new Vector2D(this.getX() - v.getX(), this.getY() - v.getY());
    }

    public Vector2D multiply(float a){
        return new Vector2D(a * this.getX() , a * this.getY());
    }

    // if isForward==false, means calculate backward coordinate
    public Vector2D applyDir(DIRECTIONS dir, double speed) {
        Vector2D delta = null;
        switch (dir) {
            case DOWN:
                delta = new Vector2D(0, speed);
                break;
            case UP:
                delta = new Vector2D(0, -speed);
                break;
            case LEFT:
                delta = new Vector2D(-speed,0);
                break;
            case RIGHT:
                delta = new Vector2D(speed, 0 );
                break;
            case UPLEFT:
                delta = new Vector2D(-speed, -speed);
                break;
            case DOWNLEFT:
                delta = new Vector2D(-speed, speed);
                break;
            case UPRIGHT:
                delta = new Vector2D(speed, -speed);
                break;
            case DOWNRIGHT:
                delta = new Vector2D(speed,speed);
                break;
        }

        return this.add(delta);
    }

    @Override
    public int hashCode() {
        return this.toString().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Vector2D){
            Vector2D targetObj = (Vector2D) obj;
            if(Math.abs( this.getX() - targetObj.getX() ) <= eps
                    && Math.abs( this.getY() - targetObj.getY() ) <= eps){
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return Double.toString(this.getX()) + "," + Double.toString(this.getY());
    }

    public double distSquare(Vector2D centerPos) {
        return (centerPos.getX() - this.getX()) * (centerPos.getX() - this.getX()) + (centerPos.getY() - this.getY()) * (centerPos.getY() - this.getY());
    }

    public double dist(Vector2D centerPos){
        return Math.sqrt(distSquare(centerPos));
    }
}
