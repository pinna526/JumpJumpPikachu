package mygame;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Timer;
import javax.imageio.ImageIO;
import static mygame.MyGame.JP_WIDTH;

public class player extends starsObject implements Runnable {

    private BufferedImage[] images = {};
    public static BufferedImage player0;
    public static BufferedImage player1;
    private final MyGame mygame;
    private final Graphics g;
    
    private Timer timer;
    private final int acce;
    private final int intervel = 1000 / 100; // 时间间隔(毫秒)  

    protected boolean turn_left = false;
    protected boolean turn_right = false;
    protected boolean jump = false;
    protected int life;

    public player(Graphics g, MyGame mygame) {
        life = 3;//初始5条命
        acce = 5;//初始加速度   //越大跳的越高
        images = new BufferedImage[]{MyGame.player0, MyGame.player1};
        image = MyGame.player0;
        width = image.getWidth();
        height = image.getHeight();
        y = MyGame.background.getHeight() + 140;
        x = MyGame.background.getWidth() / 2;
        this.mygame = mygame;
        this.g = g;
    }

    public void paint(Graphics g) {
        if (state == 1) {
            g.drawImage(this.getImage(), this.getX(), this.getY() + 50, this.width / 3, this.height / 3, mygame);
        }
    }

    public void subLife() {
        life--;
    }

    public void moveTo(int x, int y) {
        this.x = this.x - x;
        this.y = this.y - y;
    }

    public int getAcce() {
        return this.acce;
    }

    public void turn(int x) { //改变朝向
        if (images.length > 0) {
            if (x == 1) {
                image = images[0];
            }
            if (x == -1) {
                image = images[1];
            }
        }
    }

    /**
     * 碰撞检测
     *
     * @param star
     * @return
     */
    public boolean hitStars(stars star) {
        int xmin = star.x - this.width / 2;   //x坐标最小距离
        int xmax = star.x + this.width / 2 - star.stars_width;   //x坐标最大距离  
        int ymin = star.y - this.height / 2;  //y坐标最小距离  
        int ymax = star.y + this.height / 2 - star.stars_height; //y坐标最大距离  
        int playerx = this.getX();               //皮卡丘x坐标中心点距离 
        int playery = this.getY();              //皮卡丘y坐标中心点距离  
        /*
        if((playerx > xmin && playerx < xmax && playery > ymin && playery < ymax)==true){
        System.out.println("xmin"+ (playerx-xmin));
        System.out.println("xmax"+ (-playerx+xmax));
        System.out.println("ymin"+ (playery-ymin));
        System.out.println("ymax"+ (-playery+ymax));
        System.out.println("------------star------");
        }
         */
        return playerx > xmin && playerx < xmax && playery > ymin && playery < ymax;   //区间范围内为撞上了 
    }

    public boolean hitBall(ball star) {
        int xmin = star.x - this.width / 2 + 100;   //x坐标最小距离
        int xmax = star.x + this.width / 2 - star.ball_width - 130;   //x坐标最大距离  
        int ymin = star.y - this.height / 2 + 50;  //y坐标最小距离  
        int ymax = star.y + this.height / 2 - star.ball_height - 50; //y坐标最大距离  
        int playerx = this.getX();               //皮卡丘x坐标中心点距离 
        int playery = this.getY();              //皮卡丘y坐标中心点距离  
        /*
        if((playerx > xmin && playerx < xmax && playery > ymin && playery < ymax)==true){
        System.out.println("xmin"+ (playerx-xmin));
        System.out.println("xmax"+ (-playerx+xmax));
        System.out.println("ymin"+ (playery-ymin));
        System.out.println("ymax"+ (-playery+ymax));
        System.out.println("------------ball------");
        }
         */
        return playerx > xmin && playerx < xmax && playery > ymin && playery < ymax;   //区间范围内为撞上了 
    }

    int z;//左还是右的标志
    int distance = 20;//每一帧起跳的加速度
    int jumpIndex = 0;//起跳的标志

    @Override
    public synchronized void run() {
        while ((state == 1 || state == 2) && life > 0) {
            paint(g);
            if (turn_left == true) {
                //向左转并换图
                z = 1;
                if (x > 0) {
                    turn(z);
                    moveTo(distance, 0);
                    turn_left = false;
                }
            }
            if (turn_right == true) {
                z = -1;
                if (x < JP_WIDTH - 150) {
                    turn(z);
                    moveTo(-distance, 0);
                    turn_right = false;
                }
            }
            if (jump == true) {
                try {
                    Thread.sleep(3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int myy = getY();
                jumpIndex++;
                if (jumpIndex <= 100) {
                    moveTo(0, getAcce());
                } else if (100 < jumpIndex && jumpIndex <= 200) {
                    moveTo(0, -getAcce());
                } else {
                    jump = false;
                    jumpIndex = 0;
                }
            }
        }
    }
}
