package Code;


import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.util.Random;

public class Brick {

    private int toughness;
    private  int x;
    private  int y;
    private  int size;
    Color color;

    public Brick(int toughness,int x,int y,int size){
    this.toughness = toughness;
    this.x =x;
    this.y = y;
    this.size = size;
    }




    public void drawBrick(Graphics g2d){
        pickColor();
      g2d.setColor(color);
      g2d.fillRect(x,y,size,size);
        g2d.setColor(Color.black);
        g2d.drawRect(x,y,size,size);
    }

    public  void pickColor(){
        switch (toughness){
            case 1:
                color = Color.cyan;
                break;
            case 2:
                color = Color.PINK;
                break;
            case 3:
                color = Color.blue;
                break;

            case 4:
                color = Color.magenta;
                break;
            default:
                color = Color.red;

        }


    }



    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getSize() {
        return size;
    }

    public int getToughness() {
        return toughness;
    }

    public void reduceToughness(int damage) {
        this.toughness =  this.toughness - damage;

    }
}
