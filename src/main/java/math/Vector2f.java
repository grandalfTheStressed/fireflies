/*
    Author: Grant Fields
    Created Date: 06/16/2021
    !Generic vector math class
 */

package math;

public class Vector2f {

    protected float x;
    protected float y;

    public Vector2f(float x, float y){

        this.x = x;
        this.y = y;
    }

    public void translate(Vector2f vec){

        x += vec.getX();
        y += vec.getY();
    }

    public void translate(float x, float y){

        this.x += x;
        this.y += y;
    }

    public Vector2f add(Vector2f vec){

        return new Vector2f(x + vec.x, y + vec.y);
    }

    public Vector2f subtract(Vector2f vec){

        return new Vector2f(x - vec.x, y - vec.y);
    }

    public void scale(float scale){

        x *= scale;
        y *= scale;
    }

    public float distance(Vector2f vec){

        return (float)Math.sqrt((x-vec.x)*(x-vec.x) + (y-vec.y)*(y-vec.y));
    }

    public float length(){

        return this.distance(new Vector2f(0,0));
    }

    public Vector2f normalize(){

        Vector2f out = new Vector2f(x,y);
        float length = this.length();
        if(length == 0) return new Vector2f(0,0);
        out.scale(1/length);
        return out;
    }

    public float getX(){
        return x;
    }

    public float getY(){
        return y;
    }

    public void setX(float x){
        this.x = x;
    }

    public void setY(float y){
        this.y = y;
    }
}
