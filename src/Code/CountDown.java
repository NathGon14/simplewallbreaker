package Code;


public class CountDown{

    private long startingTime ;

    private boolean isUsed =false;
        public CountDown(){
        }
        public long getElapsedTime(){
            long elpasedTime =  System.currentTimeMillis() -  startingTime;
            startingTime =  System.currentTimeMillis() ;
            return  elpasedTime;
        }


    public void startTime(boolean used) {
        startingTime =  System.currentTimeMillis() ;
        isUsed = used;
    }
    public boolean isStarted(){
            return  isUsed;
    }
    public void reset(){
        startingTime =  System.currentTimeMillis() ;
        isUsed = false;
    }
}
