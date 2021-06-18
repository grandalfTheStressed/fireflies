/*
    Author: Grant Fields
    Created Date: 06/16/2021
 */

package gui;

import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

class Screen extends JPanel {

    private int width, height;

    private BufferedImage[] buff;

    public Screen(int width, int height){

        this.width = width;
        this.height = height;
        this.buff = new BufferedImage[1];
        this.buff[0] = new BufferedImage(WIDTH, HEIGHT,BufferedImage.TYPE_INT_ARGB);
    }

    public void render(BufferedImage[] buff){

        this.buff = buff;
        repaint();
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.setColor(new Color(.02f,.02f,.02f, 1));
        g.fillRect(0,0,width,height);

        for(BufferedImage image: buff)
            g.drawImage(image, 0,0,null);
    }
}