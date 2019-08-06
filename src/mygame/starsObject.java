package mygame;

import java.awt.image.BufferedImage;

public abstract class starsObject {

    protected BufferedImage image;
    protected int width = 100;
    protected int height = 100;
    protected int state;
    protected int x;
    protected int y;

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getState() {
        return state;
    }

}
