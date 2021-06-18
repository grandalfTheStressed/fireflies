package bugs;

import math.Vector2f;

import java.awt.*;
import java.util.UUID;

public class Firefly {

    protected UUID id = UUID.randomUUID();
    protected Vector2f pos;
    protected double phase;
    protected Color color;

    public Firefly(){

        pos = new Vector2f((float)Math.random(), (float)Math.random());
        phase = 1 + Math.random() * (3 - 1);
        color = new Color(.7f + (float)Math.random() * (.2f), .4f + (float)Math.random() * (.3f), 0, 1);
    }

    public void sync(Firefly[] flies, float mouseX, float mouseY){

        for(Firefly fly: flies){

            //Bump a fireflies phase based on its neighbors
            if(!fly.id.equals(this.id)) {
                float distance = pos.distance(fly.pos);
                if (distance <= .2f) {

                    if (fly.phase < phase) {
                        phase -= .00001f;
                    } else if (fly.phase > phase) {
                        phase += .00001f;
                    }
                }

                //this logic is for handling repulsion and gravity of the mouse;
                if(mouseX >= 0) {
                    Vector2f moveDir;

                    if (mouseY >= 0) {
                        Vector2f mouse = new Vector2f(mouseX, mouseY);
                        distance = pos.distance(mouse);

                        moveDir = pos.subtract(mouse).normalize();

                        moveDir.scale(.000001f);
                    } else {
                        Vector2f mouse = new Vector2f(mouseX, -mouseY);
                        distance = pos.distance(mouse);

                        moveDir = pos.subtract(mouse).normalize();

                        moveDir.scale(-.000001f);
                    }

                    if(distance < .3f)
                        pos.translate(moveDir);
                }

                //no thought behind this, just tried incorporating their phase so that the movement would be look random but controlled
                //Correction stops them from flying down and to the left, stagnating their movement slightly
                pos.translate((float) Math.cos(phase)/1000000, (float) Math.sin(phase)/1000000);
                Vector2f correction = new Vector2f(.95f,-1.01f).normalize();
                correction.scale(.000001f);
                pos.translate(correction);

                if(pos.getX() <= 0)
                    pos.setX(1f);
                else if(pos.getX() >= 1)
                    pos.setX(0f);

                if(pos.getY() <= 0)
                    pos.setY(1f);
                else if(pos.getY() >= 1)
                    pos.setY(0f);
            }
        }
    }

    public Color getColor(float r, float g, float b, float a){

        return new Color(r, g, b, a);
    }

    public Color getGlow(float alpha, float brightness){

        return new Color(
                color.getRed()*(brightness/3)/255,
                color.getGreen()*(brightness/3)/255,
                color.getBlue()*(brightness/3)/255,
                alpha
        );
    }

    public Color getMainColor(float brightness){

        return new Color(
                color.getRed()*brightness/255,
                color.getGreen()*brightness/255,
                color.getBlue()*brightness/255,
                1
        );
    }

    public double getPhase(){
        return phase;
    }

    public Vector2f getPos(){
        return pos;
    }
}
