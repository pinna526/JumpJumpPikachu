/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author xl330
 */
public class MyGame extends JPanel {

    //设置面板
    public static final int JP_WIDTH = 1500;
    public static final int JP_HEIGHT = 900;
    //初始化游戏的状态 
    private int state;
    private static final int START = 0;
    private static final int RUNNING = 1;
    private static final int PAUSE = 2;
    private static final int GAME_OVER = 3;

    //初始化游戏的得分与时间
    private int score; // 得分  
    private Timer timer; // 定时器  
    private final int intervel = 1000 / 251; // 时间间隔(毫秒)  //越小跳的越慢

    //定义图片资源变量
    public static BufferedImage background_start;
    public static BufferedImage background_pause;
    public static BufferedImage background_gameover;
    public static BufferedImage background;
    public static BufferedImage ground;
    public static BufferedImage player0;
    public static BufferedImage player1;
    public static BufferedImage star;
    public static BufferedImage ball;

    //初始化游戏中的对象
    private player player;
    private stars[] stars = {};
    private ball[] balls = {};

    //初始化音频资源
    private static URL bgm;
    private static AudioClip ac;

    //创建全局画笔
    public Graphics g;

    //静态初始化资源
    static {
        try {
            //不知道图片资源该放在哪里所以用绝对路径引用图片
            background = ImageIO.read(MyGame.class.getResource("bg_background.png"));
            ground = ImageIO.read(MyGame.class.getResource("ground.png"));
            background_start = ImageIO.read(MyGame.class.getResource("bg_start.png"));
            background_pause = ImageIO.read(MyGame.class.getResource("bg_pause.png"));
            background_gameover = ImageIO.read(MyGame.class.getResource("bg_gameover.png"));

            player0 = ImageIO.read(MyGame.class.getResource("pikachu_l.png"));
            player1 = ImageIO.read(MyGame.class.getResource("pikachu_r.png"));
            star = ImageIO.read(MyGame.class.getResource("light.png"));
            ball = ImageIO.read(MyGame.class.getResource("ball.png"));

            bgm = MyGame.class.getResource("pikachu.wav");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        paintBG(g);
        paintScore(g);
        paintLife(g);
        paintStars(g);
        paintState(g);
        paintBalls(g);
    }

    public void paintBG(Graphics g) {
        if (state == RUNNING) {
            g.drawImage(background, 0, 0, this.getWidth(), this.getHeight(), this);
            g.drawImage(ground, 0, this.getWidth(), this.getHeight() - 200, 200, this);
            g.drawImage(player.getImage(), player.getX(), player.getY() + 50, player.width / 3, player.height / 3, this);
        }
    }

    public void paintState(Graphics g) {
        switch (state) {
            case START:
                g.drawImage(background_start, 0, 0, this.getWidth(), this.getHeight(), this);
                break;
            case PAUSE:
                g.drawImage(background_pause, 0, 0, this.getWidth(), this.getHeight(), this);
                break;
            case GAME_OVER:
                g.drawImage(background_gameover, 0, 0, this.getWidth(), this.getHeight(), this);
                g.setColor(new Color(0xFF0000));
                g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 80)); // 设置字体  
                g.drawString("" + score, 400, 450); // 画分数  
                repaint();
                break;
        }
    }

    public void paintStars(Graphics g) {
        if (state == RUNNING) {
            for (stars s : stars) {
                g.drawImage(s.getImage(), s.getX(), s.getY(), s.stars_width, s.stars_height, this);
            }
        }
    }

    public void paintBalls(Graphics g) {
        if (state == RUNNING) {
            for (ball b : balls) {
                g.drawImage(b.getImage(), b.getX(), b.getY(), b.ball_width, b.ball_height, this);
            }
        }
    }

    public void paintScore(Graphics g) {
        if (state == RUNNING) {
            g.setColor(new Color(0xFFFFFF));
            g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 28)); // 设置字体  
            g.drawString("SCORE:" + score, 15, 40); // 画分数  
        }
    }

    public void paintLife(Graphics g) {
        if (state == RUNNING) {
            g.setColor(new Color(0xFFFFFF));
            g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 30)); // 设置字体  
            g.drawString("Life:" + player.life, 15, 70); // 画分数  
        }
    }

    public void initUI() {
        g = this.getGraphics();
        state = 0;
        score = 0;
        paint(g);
    }

    public void start() {
        g = this.getGraphics();
        player = new player(g, this);
    }

    public void clearGame() {
        ac.stop();
        setState(GAME_OVER);
        stars = new stars[0];
        balls = new ball[0];
    }

    public void setState(int x) {
        this.state = x;
        player.setState(x);
        for (stars s : stars) {
            s.setState(x);
        }
        for (ball b : balls) {
            b.setState(x);
        }

    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("game");
        MyGame game = new MyGame();

        frame.add(game);
        frame.setSize(JP_WIDTH, JP_HEIGHT);
        frame.setAlwaysOnTop(true); // 设置其总在最上  
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 默认关闭操作 
        frame.setLocationRelativeTo(null); // 设置窗体初始位置,不然就在左上角了
        frame.setResizable(false);//设置不允许缩放！
        frame.setVisible(true); // 尽快调用paint
        frame.requestFocus();

        game.initUI();
        game.start();
        game.action();//启动键盘监听

    }

    public void action() {
        this.setFocusable(true);
        this.requestFocus();
        this.addKeyListener(new KeyListener() {

            int p = 1;//暂停恢复的标志
            int j = 1;  //能否起跳的标志

            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {

                int keyCode = e.getKeyCode();
                int x = player.getX();//获取小怪兽的横坐标，通过控制键盘左右让他移动；
                int y = player.getY();
                int distance = 20;
                int z;//左还是右的标志

                switch (keyCode) {
                    case KeyEvent.VK_ENTER:
                        if (state == START) {
                            setState(RUNNING);
                            new Thread(player).start();
                            player.life = 3;
                            score = 0;
                            repaint();
                            /**
                             * 播放音乐
                             */
                            ac = Applet.newAudioClip(bgm);
                            ac.loop();
                        }
                        break;
                    case KeyEvent.VK_SPACE:
                        if (state == GAME_OVER) {
                            start();
                            setState(START);
                            action();
                            repaint();
                        }
                        break;
                    case KeyEvent.VK_P:
                        if (state == RUNNING || state == PAUSE) {
                            p = -p;
                            if (p == -1) {
                                state = PAUSE;
                                setState(PAUSE);
                                repaint();
                                ac.stop();
                            }
                            if (p == 1) {
                                state = RUNNING;
                                setState(RUNNING);
                                repaint();
                                ac.play();
                            }
                        }

                        break;
                    case KeyEvent.VK_LEFT:
                        player.turn_left = true;
                        break;
                    case KeyEvent.VK_RIGHT:
                        player.turn_right = true;
                        break;
                    case KeyEvent.VK_UP:
                        player.jump = true;
                        break;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (state == 1) {
                    repaint();
                    enterStars();
                    boomLight();
                    disAppear();
                    enterBalls();
                    boomBall();
                    if (player.life <= 0) {
                        state = GAME_OVER;
                        setState(GAME_OVER);
                        clearGame();
                        repaint();
                        timer.cancel();
                    }
                }
            }

        }, intervel, intervel);
    }

    int starsIndex = 0;

    public void enterStars() {
        starsIndex++;
        if (starsIndex % 400 == 0) {//每这么多秒生成一个星星
            Random rx = new Random();
            Random ry = new Random();
            int x = rx.nextInt(JP_WIDTH - 150);
            int y = ry.nextInt(400) + 100;
            stars obj = new stars(x, y, g, this);
            Thread th = new Thread(obj);
            obj.setState(1);
            th.start();
            stars = Arrays.copyOf(stars, stars.length + 1);
            stars[stars.length - 1] = obj;
        }
    }

    public void boomLight() {
        for (int i = 0; i < stars.length; i++) {
            if (player.hitStars(stars[i]) == true) {
                score += stars[i].getScore();
                stars[i].out = true;

                stars one = stars[i]; //记录被吃掉的闪电
                stars temp = stars[i]; // 被击中的闪电与最后一个闪电交换  
                stars[i] = stars[stars.length - 1];
                stars[stars.length - 1] = temp;
                stars = Arrays.copyOf(stars, stars.length - 1);
            }
        }
    }

    int ballsIndex = 0;

    public void enterBalls() {
        ballsIndex++;
        if (ballsIndex % 500 == 0) {//每这么多秒生成一个球
            Random ry = new Random();
            int x = -30;   //球从视线之外出现！
            int y = ry.nextInt(400) + 200;
            ball obj = new ball(x, y, g, this);
            Thread th = new Thread(obj);
            obj.setState(RUNNING);
            th.start();
            balls = Arrays.copyOf(balls, balls.length + 1);
            balls[balls.length - 1] = obj;
        }
    }

    public void boomBall() {
        for (int i = 0; i < balls.length; i++) {
            if (player.hitBall(balls[i]) == true) {
                player.subLife();
                balls[i].out = true;
                ball one = balls[i];
                ball temp = balls[i];
                balls[i] = balls[balls.length - 1];
                balls[balls.length - 1] = temp;
                balls = Arrays.copyOf(balls, balls.length - 1);
            }
        }
    }

    public void disAppear() {  //超过一定时间就没得闪电了哦
        for (int i = 0; i < stars.length; i++) {
            if (stars[i].out == true) {

                stars one = stars[i];
                stars temp = stars[i];
                stars[i] = stars[stars.length - 1];
                stars[stars.length - 1] = temp;
                stars = Arrays.copyOf(stars, stars.length - 1);
            }

        }
    }
}
