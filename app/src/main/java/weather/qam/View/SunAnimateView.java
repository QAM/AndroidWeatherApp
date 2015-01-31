package weather.qam.View;

//reference to https://github.com/msevrens/SynctoSunrise/blob/master/src/com/pi/synctosunrise/display/DayArcView.java

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.Calendar;

import weather.qam.util.SunTime;
import android.graphics.Paint;
import android.graphics.Color;
import android.graphics.Canvas;
import android.graphics.RectF;


/**
 * Created by qam on 1/31/15.
 */
public class SunAnimateView extends View{
    private static final String TAG = "SunAnimateView";

    SunTime sunset;
    SunTime sunrise;
    Calendar nowCal;
    private RectF rectF;

    public SunAnimateView(Context context) {
        super(context);
        init();
    }

    public SunAnimateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SunAnimateView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setTime(String sunsettime, String sunrisetime){
        sunset = SunTime.getSunTime(sunsettime);
        sunrise = SunTime.getSunTime(sunrisetime);
    }

    private void init(){
        rectF = null;
        nowCal = Calendar.getInstance();
        sunrise = null;
        sunset = null;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        Log.i(TAG, "paint " + getWidth() + " " + getHeight());
        super.onDraw(canvas);
        int x = getWidth();
        int y = getHeight();
        if(y==0) y = x/2;
        rectF = new RectF(x/8, 0, (x/8)+(3*x/4), 2*y);

        Paint paint = new Paint();

        //1. draw line 1/8,3/4,1/8
        paint.setColor(Color.BLACK);
        canvas.drawLine(rectF.left, rectF.centerY(), rectF.right, rectF.centerY(), paint);

        //2. draw half cicle
        paint.setColor(Color.GRAY);

        canvas.drawArc(rectF, -180, 180, true, paint);

        //3. draw day arc
        nowCal = Calendar.getInstance();
        if(null != sunset && null != sunrise && !SunTime.isNight(sunset, sunrise, nowCal)){
            int angle = (int) (180*(nowCal.getTimeInMillis()-sunrise.getCalendar().getTimeInMillis())/(sunset.getCalendar().getTimeInMillis()-sunrise.getCalendar().getTimeInMillis()));
            paint.setColor(Color.YELLOW);
            canvas.drawArc(rectF, -180, angle, true, paint);
        }
    }

}
