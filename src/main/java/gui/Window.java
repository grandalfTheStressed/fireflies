package gui;

import tools.Input;

import javax.swing.JFrame;
import java.awt.image.BufferedImage;

public class Window extends JFrame {

    private final Screen SCREEN;

    public Window(int WIDTH, int HEIGHT, Input input){

        this.setTitle("Fireflies");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(true);
        this.setSize(WIDTH, HEIGHT);
        this.addKeyListener(input);
        this.addMouseListener(input);
        this.addMouseMotionListener(input);
        this.addMouseWheelListener(input);

        SCREEN = new Screen(WIDTH, HEIGHT);

        this.add(SCREEN);

        this.setVisible(true);
    }

    public void render(BufferedImage[] bi){
        SCREEN.render(bi);
    }
}

