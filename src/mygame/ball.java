package mygame;

import java.awt.Graphics;
import java.util.Random;
import static mygame.MyGame.JP_WIDTH;

public class ball extends starsObject implements Runnable {

    protected int ball_width = 55;
    protected int ball_height = 55;
    protected boolean out;

    private final MyGame mygame;
    private final Graphics g;
    private final int acce;

    public ball(int x, int y, Graphics g, MyGame mygame) {
        this.image = MyGame.ball;
        this.mygame = mygame;
        this.g = g;
        this.x = x;
        this.y = y;
        this.out = false;
        Random ry = new Random();
        acce = ry.nextInt(2) + 1;
    }

    public int getAcce() {
        return this.acce;
    }

    public void moveTo(int x, int y) {
        this.x = this.x - x;
        this.y = this.y - y;
    }

    public void paint(Graphics g) {
        if (state == 1) {
            g.drawImage(image, this.getX(), this.getY(), ball_width, ball_height, mygame);
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
            moveTo(-acce, 0);
            if (this.getX() > JP_WIDTH) {
                out = true;
            }
        }
    }
}
