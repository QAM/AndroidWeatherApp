package weather.qam.util;

import android.util.Log;

import java.util.Calendar;

/**
 * Created by qam on 1/31/15.
 */
public class SunTime {
    private static final String TAG = "SunTime";

    private Calendar calendar;
    private String time;

    //time format: hour:min am/pm
    //example: 10:05 am
    private SunTime(String t){
        time = t;
        String[] ta = time.split("[:\\s]+");
        Log.i(TAG, ta[0]+" "+ta[1]+" "+ta[2]);

        double hr = Double.valueOf(ta[0]);
        double min = Double.valueOf(ta[1]);
        if(ta[2].equals("pm")) hr+=12;

        calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, (int)hr);
        calendar.set(Calendar.MINUTE, (int)min);
    }

    public static SunTime getSunTime(String t){
        SunTime st = new SunTime(t);
        return st;
    }

    public Calendar getCalendar(){
        return calendar;
    }

    public String getTime(){
        return time;
    }

    public static boolean isNight(SunTime sunset, SunTime sunrise, Calendar nowTime){
        Log.i(TAG,"SET:"+nowTime.compareTo(sunrise.getCalendar())+" rise:"+nowTime.compareTo(sunset.getCalendar()));
        if(nowTime.compareTo(sunrise.getCalendar())>0 && nowTime.compareTo(sunset.getCalendar())<0)
            return false;
        return true;
    }

}
