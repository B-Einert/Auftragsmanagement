package application;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;


public class DaylyTask extends TimerTask {
    private final static long ONCE_PER_DAY = 1000*60*60*24;

    private final static int Midnight = 0;
    private final static int ZERO_MINUTES = 0;


    @Override
    public void run() {
        System.out.println("Dayly Function is executed now");
        Server.midnight();
    }
    private static Date getTomorrowMorning(){
    	Date dateMidnght = new java.util.Date(); 
        dateMidnght.setHours(Midnight);
        dateMidnght.setMinutes(ZERO_MINUTES);
        dateMidnght.setSeconds(0);
        dateMidnght.setDate(dateMidnght.getDate()+1);
        System.out.println(dateMidnght.toString());
        return dateMidnght;
      }
    //call this method from your servlet init method
    public static void startTask(){
        DaylyTask task = new DaylyTask();
        Timer timer = new Timer();  
        timer.schedule(task,getTomorrowMorning(), ONCE_PER_DAY);// for your case u need to give 1000*60*60*24
    }
}
