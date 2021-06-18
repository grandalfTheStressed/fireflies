/*
    Author: Grant Fields
    Created Date: 06/16/2021
    !Used for Timing the program, divvying work between the Drawing threads, and passing the finished images to the screen for painting
 */
package gui;

import bugs.Firefly;
import tools.Input;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

public class VisualThread implements Runnable{

    private final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();
    private final int WIDTH = (int) ((SCREEN_SIZE.getWidth()));
    private final int HEIGHT = (int) (SCREEN_SIZE.getHeight());

    private Thread thread;
    final int TOTAL_DRAWING_THREADS = 12;
    private DrawFliesThread[] drawingFlies = new DrawFliesThread[TOTAL_DRAWING_THREADS];

    private long timer;
    private long startTime;
    private double delta = 0;
    private boolean running;
    private long period;
    private int targetHertz;
    private double radians = 0;
    static final double TAU = (2 * Math.PI);
    final double RADIAN_STEP = TAU / 200;
    private int mouseGravity = 0;

    final int AMOUNT_OF_FLIES = 1000;
    private Firefly[] fireflies = new Firefly[AMOUNT_OF_FLIES];

    Input input = new Input();
    Window window = new Window(WIDTH, HEIGHT, input);

    public boolean isRunning() {
        return running;
    }

    public VisualThread(int targetHertz){

        for(int i = 0; i < AMOUNT_OF_FLIES; i++){

            fireflies[i] = new Firefly();
        }

        if(targetHertz <= 0)
            throw new IllegalArgumentException("Refresh rate must be above 0");

        this.targetHertz = targetHertz;
        this.period = (1000000000L / (long)targetHertz);
        this.timer = System.nanoTime();
        this.startTime = System.nanoTime();
        start();
    }

    @Override
    public void run(){

        BufferedImage[] bufferedImages = new BufferedImage[TOTAL_DRAWING_THREADS];

        while(running){

            detectKeys();

            if(System.nanoTime() >= timer) {
                timer = period + System.nanoTime();
                delta = (double)((System.nanoTime() - startTime) / 1000000000L);

                radians += RADIAN_STEP;

                for(int i = 0; i < TOTAL_DRAWING_THREADS; i++)
                    drawingFlies[i] = new DrawFliesThread(
                            fireflies,
                            i * (AMOUNT_OF_FLIES/TOTAL_DRAWING_THREADS),
                            (AMOUNT_OF_FLIES * (i + 1))/TOTAL_DRAWING_THREADS,
                            WIDTH,
                            HEIGHT,
                            radians,
                            getMouseX(),
                            getMouseY()
                    );

                for(DrawFliesThread thread: drawingFlies)
                    thread.stop();

                for(int i = 0;i < TOTAL_DRAWING_THREADS; i++)
                    bufferedImages[i] = drawingFlies[i].getBufferedImage();

                window.render(bufferedImages);
            }
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

    public int getTargetHertz() {
        return targetHertz;
    }

    public void setTargetHertz(int targetHertz){

        try {
            if (targetHertz <= 0)
                throw new IllegalArgumentException("Refresh rate must be above 0");
        }catch (IllegalArgumentException e){
            e.printStackTrace();
        }
        this.targetHertz = targetHertz;
        this.period = (1000000000l / targetHertz);
        this.timer = System.nanoTime();
    }

    public Color getColor(float r, float g, float b, float a){

        return new Color(r, g, b, a);
    }

    public double getDelta(){
        return delta;
    }

    private void detectKeys(){

        if(input.isKey(KeyEvent.VK_EQUALS)){
            setTargetHertz(getTargetHertz() + 1);
        }

        if(input.isKey(KeyEvent.VK_MINUS)){
            if(getTargetHertz() < 2) return;
            setTargetHertz(getTargetHertz() - 1);
        }

        if(input.isKey(KeyEvent.VK_SHIFT)){
            mouseGravity = 0;
        }

        if(input.isKey(KeyEvent.VK_ENTER)){
            mouseGravity = 1;
        }

        if(input.isKey(KeyEvent.VK_BACK_SLASH)){

            mouseGravity = -1;
        }
    }

    public int getMouseX(){
        if(mouseGravity == 1 || mouseGravity == -1)
            return input.getMouseX();
        else
            return -1;
    }

    public int getMouseY(){
        if(mouseGravity == -1)
            return -input.getMouseY();
        else
            return input.getMouseY();
    }
}