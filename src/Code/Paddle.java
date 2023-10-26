package Code;


import java.awt.*;


public class Paddle {

    private  int paddle_height=20;
    private int paddle_width;
    private double scale_width = 0.4;
    private int SCREEN_WIDTH,SCREEN_HEIGHT;
    private int paddle_X ,paddle_Y ;
    private  boolean disabled = true;
    private  int brick_size;
    Image paddleImage;
    public Paddle(int SCREEN_WIDTH ,int SCREEN_HEIGHT,int brick_size) {
        this.SCREEN_WIDTH = SCREEN_WIDTH;
        this.SCREEN_HEIGHT = SCREEN_HEIGHT;
        this.brick_size= brick_size;
        paddle_width = (int) (SCREEN_WIDTH * scale_width);
        reset();
    }
    public void reset(){
        paddle_Y = SCREEN_HEIGHT - (brick_size * 3);
        paddle_X = ((SCREEN_WIDTH) - paddle_width)/ 2;

    }
    public  void  drawPaddle(Graphics g2d){
        g2d.setColor(Color.red);
        g2d.fillRect(paddle_X,paddle_Y,paddle_width,paddle_height);
    }

    public  void  updatePostionMouse(int request_X ){
        if(disabled){
            return;
        }

        paddle_X =   request_X -paddle_width/2;

        //boundary limit the paddle movement

        // paddle is exceeded the limit on the right
        //set the paddle to the limit to the screen width
        if(paddle_X+paddle_width > SCREEN_WIDTH ){
            paddle_X = SCREEN_WIDTH - paddle_width;
        }
        // paddle is exceeded the limit on the left
        //set the paddle to the limit to the screen width
        if((paddle_X)< 0 ){
            paddle_X = 0;
        }

    }

    public int getPaddle_X() {
        return paddle_X;
    }

    public int getPaddle_Y() {
        return paddle_Y;
    }


    public int getPaddle_width() {
        return paddle_width;
    }

    public int getPaddle_height() {
        return paddle_height;
    }

    public void enableMovement() {
        this.disabled =false;
    }

    public void disableMovement() {
        this.disabled =true;
    }



}
