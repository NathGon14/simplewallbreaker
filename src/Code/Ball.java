package Code;

import java.awt.*;
import java.util.ArrayList;

import static java.lang.Math.*;

public class Ball {

    private  final double speed = 6; // how fast is the ball
    private  final int damage = 1;// ball damage when it hit the brick
    private int BOARD_HEIGHT; // the Screen height
    private int BOARD_WIDTH;// the Screen height
    private int diameter; // diameter  of the ball
    private double ball_X; // ball X coordinate
    private double ball_Y; // ball Y coordinate
    private  double ball_direction_X; // the direction of the ball  X = 1; ball is going to the right x = -1; ball is going to the left
    private  double ball_direction_Y; // the direction of the ball  Y = 1; ball is going down  Y = -1; ball is going up
    private double predicted_X, predicted_Y; // ahead of ball_x and ball_y coordinates
    private boolean disabled = false; // use for checking if the ball is available
    private boolean outside = false; // use for checking if the ball is outside the screen or it fell in the bottom of the screen

   //constructor
    public Ball(int BOARD_WIDTH, int BOARD_HEIGHT,int diameter) {
        //assigning the properties variables
        this.BOARD_WIDTH = BOARD_WIDTH;
        this.BOARD_HEIGHT = BOARD_HEIGHT;
        this.diameter = diameter;
        reset();//reset method called for bring the ball to the default position
    }


   //this method is used for reseting the ball to its initial state
    public void reset(){
        setOutside(false);//bring the ball inside the screen
        setBallPosition( BOARD_WIDTH/2  , BOARD_HEIGHT -BOARD_WIDTH/2 ); //positioning the ball to its initial or default state
        setDirection("top-right"); // the direction which the ball is going

    }

    //method for setting the X and Y coordinates in which the ball is going
    public void setDirection(String direction) {

        // the direction of the ball  X = 1; ball is going to the right x = -1; ball is going to the left
        // the direction of the ball  Y = 1; ball is going down  Y = -1; ball is going up
        switch (direction){
            case"down-right":
                this.ball_direction_X = 1;
                this.ball_direction_Y = 1;
                break;
            case "down-left":
                this.ball_direction_X = -1;
                this.ball_direction_Y = 1;
                break;
            case "top-left":
                this.ball_direction_X = -1;
                this.ball_direction_Y = -1;
                break;
            case "top-right":
                this.ball_direction_X = 1;
                this.ball_direction_Y = -1;
                break;
        }


    }



    //method use for drawing the ball in the screen
    public void drawBall(Graphics g2d) {
    g2d.setColor(Color.white);
     g2d.fillOval((int)ball_X, (int) ball_Y,diameter,diameter);
    }


    //method for moving the ball
    public void  moveBall(){
        ball_X =  (ball_X + ball_direction_X * speed);
        ball_Y =  (ball_Y + ball_direction_Y * speed);
    }
    //method for moving the ball one step ahead of the current position of the ball
    public void predictMove(){
        predicted_X =  (ball_X + ball_direction_X * (speed));
        predicted_Y =  (ball_Y + ball_direction_Y * (speed));
    }

    // method for checking if the ball collided on each side of the screen
    public boolean collidedOnScreen() {
        predictMove(); //one step ahead of the ball current positon
        boolean collided = true; // variable to tell if the ball collided on the side of the screen

        // ball hit the right side of the board
        // if else statement for each side
        // if the ball hit the side of the screen
        // revers
        if (predicted_X + (diameter) >= BOARD_WIDTH) {
            inverseX(); //reverse the X
            setBallPosition(BOARD_WIDTH - (diameter),predicted_Y); //move the ball
        }else if (predicted_X <= 0) {
            inverseX(); //reverse the X
            setBallPosition(0,predicted_Y);//move the ball

        } else if (predicted_Y <= 0  ) {    // ball hit the top
            inverseY();//reverse the Y
            setBallPosition(predicted_X,0);//move the ball
        } else  if (predicted_Y >= BOARD_HEIGHT ) {    // ball falls down at the bottom
            inverseY();//reverse the Y
            setBallPosition(predicted_X,BOARD_HEIGHT - (diameter));//move the ball not necessary but to display the ball falling outside
            setOutside(true);//set the ball outside;
        }else  {// did not collided on any side of the screen
            collided =false; // change collided to false beacause it didnt collided
            moveBall(); // move the ball
        }
         return  collided;
    }

    public Brick getClosestBrick(ArrayList<Brick> bricks) {
        double minDistance = 1000000;
        Brick closestBrick= null;
        //find the closest point
        for (Brick brick:bricks
             ) {
            int brick_size = brick.getSize();
            int middle_x_brick = brick.getX() + (brick_size/2);
            int middle_y_brick = brick.getY() + (brick_size/2);
            int radius = diameter/2;
            double middle_x_ball = (int) ball_X + radius;
            int middle_y_ball = (int) ball_Y +radius;
            double a = middle_x_ball-middle_x_brick;
            double b = middle_y_ball-middle_y_brick;
            double distance = sqrt( (a*a) + (b*b) );
            if(distance <= minDistance){
                closestBrick = brick;
                minDistance =distance;
            }
        }
        hit(closestBrick.getX(),closestBrick.getY() , closestBrick.getSize(),closestBrick.getSize());
        return closestBrick;

    }


    public  boolean  collided( int x_position, int y_position , int width, int height){
        predictMove();// move the ball on step ahead of the current position

        int collided_height= height;// the height of where the ball collided
        int collided_width= width; // the width of where the ball collided

        int collided_left= x_position;// the X coordinate of where the ball collided
        int collided_top=y_position; // the Y coordinate of where the ball collided
        int collided_right= collided_left +collided_width; ; // right side of X coordinate
        int collided_bottom= collided_top +collided_height; // bottom side of Y coordinate

        double radius = diameter/2;
        double ball_middle_point_x =  radius + predicted_X ; // a
        double ball_middle_point_y =  radius + predicted_Y ; // b


        double closestX = ball_middle_point_x;
        double closestY = ball_middle_point_y;
        //check which point of the rectangle x and y of the edge of the rectangle is the closest to the point of the circle
        //find the close X and Y of where the ball collided
        if (ball_middle_point_x < collided_left) {
            closestX = collided_left;      // test left edge
        }
        else if (ball_middle_point_x >collided_right){
            closestX =collided_right;  // right edge
        }

        if (ball_middle_point_y < collided_top)     {
            closestY = collided_top;      // top edge
        }
        else if (ball_middle_point_y >collided_bottom) {
            closestY =collided_bottom;// bottom edge
        }

        //pythagorean theorem this is the formula of getting the distance
        double a = ball_middle_point_x-closestX;
        double b = ball_middle_point_y-closestY;

        double distance = sqrt( (a*a) + (b*b) );
        //collision checking
        // if the distance is less than or equal the radius of the ball
        // it collided

        if (distance <=radius) {
            return true;
        }
        return false;

    }

    //method use for figuring out whether the ball collided on the paddle or the bricks
    public void hit(int x_position, int y_position , int width, int height) {
        predictMove();// move the ball on step ahead of the current position
        int collided_height= height;// the height of where the ball collided
        int collided_width= width; // the width of where the ball collided

        int collided_left= x_position;// the X coordinate of where the ball collided
        int collided_top=y_position; // the Y coordinate of where the ball collided
        int collided_right= collided_left +collided_width; ; // right side of X coordinate
        int collided_bottom= collided_top +collided_height; // bottom side of Y coordinate
        double radius = diameter/2;
        double ball_middle_point_x =  radius + predicted_X ; // a
        double ball_middle_point_y =  radius + predicted_Y ; // b

        //     (x,y) = (0,5) , width:5 height:5
        //                                      top: 5
        //        --------------             --------------
        //        |            |             |            |
        //        |            |             |            |
        // left:0 |            | Right:5     |            |
        //        |            |             |            |
        //        |            |             |            |
        //        --------------             --------------
        //                                     Bottom: 10

        String collided_side = "none";


             //check if the ball is inside the box
            // remove it from inside send it to the neareast area without the bricks
                if(isInsideCollsion(collided_top,collided_left,collided_right,collided_bottom)){
                    double collision_outside_x = ball_X - diameter * toNearestOne(ball_direction_X);
                    double collision_outside_y = ball_Y - diameter * toNearestOne(ball_direction_Y);
                    setBallPosition(collision_outside_x, collision_outside_y); //change the ball position
                    hit(collided_left,collided_top , collided_width, collided_height); // call this method again
                }

            //collision
            //check collision base on their direction
            // checking which part of the bricks or paddle got hit
            if(ball_direction_Y >0  && ball_direction_X>0) {// top going to right
                collided_side =  predicted_Y <= collided_top? "top":"left";
            }
            else  if (ball_direction_Y <0  && ball_direction_X>0){ //bottom to right
                collided_side =  predicted_Y+diameter >= collided_bottom? "bottom":"left";

            }     else  if (ball_direction_Y >0  && ball_direction_X<0){ //top  to left
                collided_side =  predicted_Y <= collided_top? "top":"right";

            }else if (ball_direction_Y <0  && ball_direction_X<0) { //bottom  to left
                collided_side =  predicted_Y+diameter >= collided_bottom? "bottom":"right";
            }
            // apply ball physics
            //to change the reduce the speed of the X and Y coordinates
            ballPhysics(collided_side ,ball_middle_point_x,ball_middle_point_y,collided_left,collided_right,collided_bottom,collided_top);
            //bounce the ball
            bounce(collided_side ,collided_left,collided_right,collided_bottom,collided_top);


    }
    // this method is for checking if the ball is inside the paddle or the brick
    private boolean isInsideCollsion(int collided_top,int  collided_left,int collided_right, int collided_bottom){
        if (
                predicted_X>= collided_left &&
                        predicted_X + diameter <= collided_right &&
                        predicted_Y >= collided_top &&
                        predicted_Y + diameter <= collided_bottom
        ) return  true;
         else
                 return false;

    }

    // how will the ball bounce
    // on where it collided
    private void bounce(String ballSideCollided,double collidedLeft, double collidedRight, double collidedBottom, double collidedTop) {
    switch (ballSideCollided){
        case "top":
            setBallPosition(predicted_X, collidedTop - (diameter));
            inverseY();
            break;
        case "bottom":
            setBallPosition(predicted_X, collidedBottom);
            inverseY();

            break;
        case "right":
            setBallPosition(collidedRight, predicted_Y);
            inverseX();

            break;
        case "left":
            setBallPosition(collidedLeft-diameter, predicted_Y);
            inverseX();
            break;
    }
    }

    public void ballPhysics(String collisionSide, double ball_middle_point_x, double ball_middle_point_y, double collided_left, double collided_right, double collided_bottom, int collided_top) {


        //check how far is the ball from the tip of the paddle width or height

        double width_of_rectangle = collided_right - collided_left;
        double extra_space_x = collided_left;
        double ball_point_x = ball_middle_point_x - extra_space_x;
        double decimal_percentage_x = (ball_point_x / width_of_rectangle) ; // percentage in decimal form

        double height_of_rectangle = collided_bottom - collided_top;
        double extra_space_y = collided_top;
        double ball_point_y = ball_middle_point_y - extra_space_y;
        double decimal_percentage_y = (ball_point_y / height_of_rectangle) ;
        ;

        //ball direction is to the left then convert the percentage
        //negative means the direction of the ball is to the left
        if (ball_direction_X < 0) {
            decimal_percentage_x = 1 - decimal_percentage_x;
        }
        if (ball_direction_Y >0) {
            decimal_percentage_y = 1 - decimal_percentage_y;
        }
        //prevent for stuck
        if(decimal_percentage_x >.9){
            decimal_percentage_x = .9;
        }
        if(decimal_percentage_y >.9){
            decimal_percentage_y = .9;
        }

        resetDirection();
        //reduce the speed of either axis
        //base on the percentage where the ball hit on the paddle
        if (collisionSide.equals("top") || collisionSide.equals("bottom")) {
            ball_direction_X = ball_direction_X - decimal_percentage_x * (ball_direction_X / Math.abs(ball_direction_X));
            ball_direction_Y = ball_direction_Y + decimal_percentage_x * (ball_direction_Y / Math.abs(ball_direction_Y));
        } else {
            ball_direction_X = ball_direction_X + decimal_percentage_y * (ball_direction_X / Math.abs(ball_direction_X));
            ball_direction_Y = ball_direction_Y - decimal_percentage_y * (ball_direction_Y / Math.abs(ball_direction_Y));
        }

    }

    public boolean isOutside() {
        return outside;
    }

    public void setOutside(boolean outside) {
        this.outside = outside;
    }

    public double toNearestOne(double direction){
        if(direction <0){
            return -1;
        }
        return 1;

}




public void resetDirection(){
      this.ball_direction_X = toNearestOne(     this.ball_direction_X );
    this.ball_direction_Y = toNearestOne(     this.ball_direction_Y );
}

    public void  setBallPosition(double ball_X,double ball_Y){
        this.ball_X = ball_X;
        this.ball_Y = ball_Y;

    }

    public  void inverseY(){
    ball_direction_Y = -ball_direction_Y;

}
    public  void inverseX(){
        ball_direction_X = -ball_direction_X;

    }

    public int getDamage() {
        return damage;
    }
}

