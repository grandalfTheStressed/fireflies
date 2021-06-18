/*
    Author: Grant Fields
    Created Date: 06/16/2021
    !Draws a section of the Fireflies and handles their synchronization and movement logic
 */
package gui;

import bugs.Firefly;

import java.awt.*;
import java.awt.image.BufferedImage;

public class DrawFliesThread implements Runnable{

    private Thread thread;
    private boolean running;
    Firefly[] flies;
    private int startIndex;

    private BufferedImage bufferedImage;
    final Graphics GRAPHICS;
    final int END_INDEX;
    final int RADIUS = 16;
    final int WIDTH;
    final int HEIGHT;
    final double RADIANS;
    final float MOUSE_X;
    final float MOUSE_Y;

    public DrawFliesThread(Firefly[] flies, int startIndex, int END_INDEX, int WIDTH, int HEIGHT, double RADIANS, float MOUSE_X, float MOUSE_Y){

        this.flies = flies;
        this.startIndex = startIndex;
        this.END_INDEX = END_INDEX;
        this.WIDTH = WIDTH;
        this.HEIGHT = HEIGHT;
        this.RADIANS = RADIANS;
        this.bufferedImage = new BufferedImage(WIDTH, HEIGHT,BufferedImage.TYPE_INT_ARGB);
        this.GRAPHICS = bufferedImage.getGraphics();
        //Converting to fly vector space
        this.MOUSE_X = MOUSE_X/ (float)WIDTH;
        this.MOUSE_Y = MOUSE_Y/ (float)HEIGHT;
        start();
    }

    @Override
    public void run() {

        for(; startIndex < END_INDEX; startIndex++) {

            int flyX = (int) (flies[startIndex].getPos().getX() * WIDTH);
            int flyY = (int) (flies[startIndex].getPos().getY() * HEIGHT);

            float brightness = (float) (.5 * (1 + Math.sin(RADIANS * flies[startIndex].getPhase())));

            if(brightness == 0)
                continue;

            for(int tempRadius = 1; tempRadius <= RADIUS; tempRadius+=2){

                float alpha = (1 - (float) tempRadius / (float) RADIUS);
                int tempDiameter = tempRadius * 2;
                GRAPHICS.setColor(flies[startIndex].getGlow(alpha, brightness));
                GRAPHICS.drawOval(flyX - tempRadius, flyY - tempRadius, tempDiameter, tempDiameter);
            }

            GRAPHICS.setColor(flies[startIndex].getMainColor(brightness));
            GRAPHICS.drawRect(flyX - 2, flyY - 1, 3, 1);
            GRAPHICS.drawRect(flyX - 1, flyY - 2, 1, 3);

            flies[startIndex].sync(flies, MOUSE_X, MOUSE_Y);
        }

    }

    public synchronized void start(){

        if(running) return;

        running = true;
        thread = new Thread(this);
        thread.start();
    }

    public synchronized void stop(){

        if(!running) return;

        running = false;

        try{
            thread.join();
        }catch(InterruptedException e){

            e.printStackTrace();
        }
    }

    public BufferedImage getBufferedImage() {
        return bufferedImage;
    }
}
