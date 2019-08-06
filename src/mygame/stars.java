package mygame;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import javax.imageio.ImageIO;

public class stars extends starsObject implements Runnable {

    public static BufferedImage star;
    private final MyGame mygame;
    private final Graphics g;
    private final int intervel = 1000 / 251; // 时间间隔(毫秒)  //越小跳的越慢
    private final int scores = 5;
    private int life = 1000;
    

    protected int stars_width = 100;
    protected int stars_height = 100;
    protected boolean out = false;

    public stars(int x, int y, Graphics g, MyGame mygame) {
        this.x = x;
        this.y = y;
        this.image = MyGame.star;
        this.mygame = mygame;
        this.g = g;
        this.state = 1;
    }

    public int getScore() {
        return this.scores;
    }

    public int getLife() {
        return this.life;
    }

    public void subLife() {
        this.life--;
    }

    public void paint(Graphics g) {
        if (state == 1) {
            g.drawImage(image, this.getX(), this.getY(), this.stars_width, this.stars_height, mygame);
        }
    }

    @Override
    public void run() {
        while ((state == 1 || state == 2) && out == false) {
            //paint(g);
            try {
                Thread.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            subLife();
            if (life <= 0 || out == true) {
                out = true;
            }
        }
    }

}
