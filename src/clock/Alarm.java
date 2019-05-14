package clock;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * This an alarm class this the place when I store  alarms details this details included 
 * Hours of the alarm,Minutes of the alarm, Day of the alarm, Month of the alarm, also it store message 
 * for the alarm.All alarm details is sanded form AddAlarm function form the View class.
 * 
 * 
*/
public class Alarm {

    private int currentHours, currentMinutes = 0;
    private int currentDay, currentMonth = 0;
    private String alarmMessage = "";
    public Alarm(int aHours, int aMinutes,int aDay, int aMonth, String Message){
        
        this.currentHours = aHours;
        this.currentMinutes = aMinutes;
        this.currentDay = aDay;
        this.currentMonth =  aMonth;
        alarmMessage = Message;
        
        
System.out.println("Hours : "+currentHours+"  Minutes : "+currentMinutes+" Day : "+currentDay+"  Month : "+currentMonth+" Message : "+alarmMessage);
    }
    /**
     *   For making my alarm system I used epoch time. I used epoch time to convert alarm time to milliseconds  and divide them on 1000 and return as 
    epoch time in order to make the corect priority for the alarm item, this priority will be used for the priority queue. 
    */
     public long epoch (String hours, String minutes, String day, String month) throws ParseException{
            
            String year = "2019";
            String seconds = "00";
            String timestamp = (""+year+"-"+month+"-"+day+" "+hours+":"+minutes+":"+seconds);
            SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date dt = sd.parse(timestamp);
            long epoch = dt.getTime();
            long epochtime = epoch/1000;
            
            return epochtime;
        
    }
    
    
    
}
