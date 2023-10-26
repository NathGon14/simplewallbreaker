package Code;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;


public class Board extends JPanel implements ActionListener {

    int brick_size = 30;
    private final String brick_name = "brick";
    private int delay = 5;
    protected int BALL_SREEN_DIAMETER = 20;
    protected int ball_diameter = 15;
    protected int number_of_bricks_on_width = 12;
    private int height_multiplier = 22;
    private Random random = new Random();
    protected int BOARD_HEIGHT = brick_size * height_multiplier;
    protected int BOARD_WIDTH = brick_size * number_of_bricks_on_width;

    private ArrayList<Brick> bricks;

    private  long milisec = 1000;
    private  int DEFAULT_BALLS = 2;
    private  long DEFAULT_TIME = 120 * milisec;
    private  int remaining_balls = DEFAULT_BALLS;
    private  int remaining_bricks;

    private double countdown;
    private  long  remaining_time= 0 ;
    private double SCORE_ADDED = 1000;
    private double Score = 0;
    private String mapStucture [][] = {
            {},
            {},
            {},
            {},
            {},
            {
        "", brick_name, brick_name, brick_name, brick_name, brick_name, brick_name, brick_name, brick_name, brick_name, brick_name, "",
    },{
            "", brick_name, brick_name, brick_name, brick_name, brick_name, brick_name, brick_name, brick_name, brick_name, brick_name, "",
    },{
            "", brick_name, brick_name, brick_name, brick_name, brick_name, brick_name, brick_name, brick_name, brick_name, brick_name, "",
    },{
            "", brick_name, brick_name, brick_name, brick_name, brick_name, brick_name, brick_name, brick_name, brick_name, brick_name, "",
    },{
            "", brick_name, brick_name, brick_name, brick_name, brick_name, brick_name, brick_name, brick_name, brick_name, brick_name, "",
    }
    };
    protected  int level = 0 ;
    protected  int maxLevel = 5;
    private Timer timer;
    private Paddle player = new Paddle(BOARD_WIDTH,BOARD_HEIGHT,brick_size);
    private Ball ball = new Ball(BOARD_WIDTH,BOARD_HEIGHT,ball_diameter);
    private boolean gamePause =false;
    private CountDown elapseTime  = new CountDown();

    public Board() {

        init();

    }
    public  void init() {
        setFocusable(true);
        requestFocus();
        addKeyListener(new keyBoardLisner());
        addMouseMotionListener(new MouseListenerHandler());

        timer = new Timer(delay,this);
        timer.start();
//
//
//
     nextLevel();
    }
    public  void  nextLevel(){
        level++;
        createBricks();
        remaining_bricks = bricks.size();
        ball.reset();
        player.reset();
        if(remaining_balls <6)        remaining_balls +=1;
        remaining_time += DEFAULT_TIME;
        elapseTime.reset();
        startCountDown(3);
    }
    public  void  resetGame(){
        Score = 0;
        level=0;
        remaining_balls = DEFAULT_BALLS;
        remaining_time = 0;
        nextLevel();

    }

    public void createBricks(){
        bricks = new ArrayList<>();
        for (int y = 0 ; y<mapStucture.length;y++){
            for (int x= 0 ; x<mapStucture[y].length;x++){
                if(!mapStucture[y][x].equals(brick_name))continue;
             int toughness = random.nextInt(level,level+1);
             int brickY = y *brick_size;
             int brickX= x *brick_size;
                bricks.add(new Brick(toughness,brickX,brickY,brick_size));
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        gameStatus(g);
    }

    public void gameStatus(Graphics g2d){
        String gameState = getGameState();

        if(!gameState.equals("ingame")){
            gamePause =true;
            ball.reset();
            player.reset();
            player.disableMovement();
        }

        if(gameState.equals("countdown")){
            draw(g2d);
            drawCountDown(g2d);
        }else if (gameState.equals("time")){
            draw(g2d);
            drawIntermission(g2d,"Out of time :(");

        }else if (gameState.equals("ball")){
            draw(g2d);
            drawIntermission(g2d,"out of ball :(");
        }else if (gameState.equals("completed")){
            draw(g2d);
            drawIntermission(g2d,"You beat the game :)");
        }else if(gameState.equals("ingame")){
            player.enableMovement();
            collision();
            draw(g2d);
        }

    }


    //
    public  String  getGameState(){

        // if there is a countdown

        if(elapseTime.isStarted())
                    return  "countdown";

        if(remaining_time <=0)
            return  "time";



        if(remaining_balls == 0)
            return "ball";

        if(ball.isOutside()){
            remaining_balls--;
            if(remaining_balls <=0){
                remaining_balls=0;
                return  "ball";
            }

            startCountDown(3);
            return  "countdown";

        }

        if(remaining_bricks == 0){
            if(level > maxLevel) {
                return  "completed";
            }
            else{
                nextLevel();
                return  "countdown";
            }


        }

        return  "ingame";

    }

    public void draw(Graphics g2d){

        drawBackground(g2d);
        drawBricks(g2d);
        drawPlayer( g2d);
        drawBall(g2d);
        drawRemainingTime(g2d);
        drawScreenData( g2d);
    }

    public void drawBackground(Graphics g2d){
        g2d.setColor(Color.black);
        g2d.fillRect(0,0,BOARD_WIDTH,BOARD_HEIGHT);

    }

    public void drawBricks(Graphics g2d){
        for(int i =0;i<bricks.size();i++){
            bricks.get(i).drawBrick(g2d);
        }


    }
    public void drawPlayer(Graphics g2d){
        player.drawPaddle(g2d);
    }
    public void drawBall(Graphics g2d){
        ball.drawBall(g2d);
    }


    DecimalFormat formatter = new DecimalFormat("#0,000");
    public void drawScreenData(Graphics g2d){
        // score label
        int fontSize = 25;
        g2d.setColor(Color.white);

        g2d.setFont(new Font("arial",Font.BOLD,fontSize));
        g2d.drawString(formatter.format(Score),  5,fontSize*2+5);
         // level label
        g2d.setColor(Color.green);
        fontSize = 20;
        g2d.setFont(new Font("arial",Font.BOLD,fontSize));
        g2d.drawString("Level "+level,  5,fontSize+5);

        // remaining balls label
        g2d.setColor(Color.white);
        fontSize = 20;
        g2d.setFont(new Font("arial",Font.BOLD,fontSize));
        g2d.drawString(remaining_balls+ "",  5,BOARD_HEIGHT-fontSize*2);
        g2d.fillOval( 20,(BOARD_HEIGHT-fontSize*2) -BALL_SREEN_DIAMETER+3 , BALL_SREEN_DIAMETER,BALL_SREEN_DIAMETER);


    }

    public void drawRemainingTime(Graphics g2d){
        checkRemainingTime();
        long minutes = (remaining_time/1000)/60;
        long seconds = (remaining_time/1000) % 60;
        String minutesString =minutes <10 ? "0"+minutes: minutes+"";
        String secondsString = seconds <10 ? "0"+seconds: seconds+"";
        g2d.setColor(Color.RED);
        int fontSize = 20;
        g2d.setFont(new Font("arial",Font.BOLD,fontSize));
        String remainingTime = "Time: "+minutesString+":"+secondsString;
        g2d.drawString(remainingTime,  5,BOARD_HEIGHT-fontSize/2);

    }

    public void drawCountDown(Graphics g2d){
        doCountDown();
        String countDownSecond =   (int)(countdown/milisec)+"";
        int fontSize = 20;
        g2d.setColor(Color.red);
        g2d.setFont(new Font("arial",Font.BOLD,fontSize));
        g2d.drawString("Game start in: "+countDownSecond,  5,(fontSize+6)*3);

    }

    public void startCountDown(int sec){
        countdown = sec * milisec;
        gamePause= true;
        elapseTime.startTime(true);
    }

    public void doCountDown(){
        countdown = countdown - elapseTime.getElapsedTime();
        if(countdown <=0){
            removeCountDown();
        }
    }

    public  void removeCountDown(){
        countdown= 0 ;
        gamePause  = false;
        elapseTime.reset();//reset so the remaing time use it

    }
    public void checkRemainingTime(){
        if(gamePause) return;
        remaining_time = remaining_time - elapseTime.getElapsedTime();
    }


    public void drawIntermission(Graphics g2d,String message){
       g2d.setColor(Color.red);
       if(level >= maxLevel){
           g2d.setColor(Color.green);
       }
        g2d.drawString(message,  5,(26)*3);
    }

    public  void  collision(){
        //screen collision
        if(ball.collidedOnScreen())return;
        //screen player collision
        if(collidedOnPlayer()){
            ball.hit(player.getPaddle_X(),player.getPaddle_Y(),player.getPaddle_width(),player.getPaddle_height());
            return;
        }

        //screen player bricks
        ArrayList<Brick> bricksCollisions =  new ArrayList<>();
        for (Brick brick : bricks) {
            if (ball.collided(brick.getX(), brick.getY(), brick_size, brick_size))
                bricksCollisions.add(brick);
        }
      if(bricksCollisions.isEmpty()) return; // no brick collision

         Brick closestBrick =  ball.getClosestBrick(bricksCollisions);
        closestBrick.reduceToughness(ball.getDamage());
        Score+=SCORE_ADDED;
        if(closestBrick.getToughness() <=0){
            Score+=SCORE_ADDED;
            remaining_time+=(DEFAULT_TIME*.01);
            remaining_bricks--;
            bricks.remove(closestBrick);
        }


    }

    public  boolean  collidedOnPlayer(){
        return ball.collided(player.getPaddle_X(),player.getPaddle_Y(),player.getPaddle_width(),player.getPaddle_height());

    }



    @Override
    public void addNotify() {
        super.addNotify();

    }


    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }

    class keyBoardLisner implements KeyListener {

        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent e) {

        }

        @Override
        public void keyReleased(KeyEvent e) {
            int key = e.getKeyCode();
            if(key == KeyEvent.VK_SPACE)
            removeCountDown();
            else if (key == KeyEvent.VK_ENTER) {
                resetGame();
            }
        }
    }


    class MouseListenerHandler implements MouseMotionListener{

        @Override
        public void mouseDragged(MouseEvent e) {

        }

        public void mouseMoved(MouseEvent e){
            int a =  e.getX();
         player.updatePostionMouse(a);
        }


    }


}