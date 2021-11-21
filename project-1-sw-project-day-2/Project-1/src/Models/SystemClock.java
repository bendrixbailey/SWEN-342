package Models;
import java.time.*;

/**
 * This function represents the system clock that all the classes can use to check
 * time and ensure that they are on track for the tasks they must do.
 * 
 * startTime refers to the exact time when the project manager started his shift, aka 8:00 am.
 * all the other threads must refer to this start time when going about their day. This value never changes.
 * 
 * Since startTime is always 8:00, all time methods will return time relative to this. If getCurrentTIme
 * is called 600 ms after the clock is instantiated, then the return method will return "9:00"
 */
public class SystemClock{
    private long startTime;
    private static Clock sysClock;

    /**
     * Builds the system clock, starts it, and logs current time as start time for the day.
     */
    public SystemClock(){
        SystemClock.sysClock = Clock.systemDefaultZone();
        this.startTime = sysClock.millis();
    }

    /**
     * Restart the system clock. Used for debugging mostly
     */
    public void restart(){
        this.startTime = sysClock.millis();
    }

    /**
     * This function converts the millisecond difference between start time and current time
     * to a readable hour:minute format. Since it only uses local variables it does not need
     * to be synchronized.
     * 
     * @return difference between start time and current time, converted to hours:minutes
     */
    public String GetCurrentTimeHoursMinutes(){
        int millisDifference = (int) (sysClock.millis() - startTime);
        String tod = " am";

        int totalMinutes = millisDifference / 10;

        int hours = totalMinutes / 60 + 8;
        int minutes = totalMinutes % 60;

        if(hours >= 12){          //reset back to 0:00 for pm
            if(hours > 12){
                hours = hours - 12;  
            }
            tod = " pm"; 
        }
        
        //return Integer.toString(hours) + ":" + Integer.toString(minutes) + tod;
        return String.format("%d:%02d%s", hours, minutes, tod);
    }


    /**
     * Simply returns millisecond difference between now and start time of the clock.
     * @return
     */
    public long GetCurrentTimeLong(){
        return sysClock.millis() - startTime;
    }


}