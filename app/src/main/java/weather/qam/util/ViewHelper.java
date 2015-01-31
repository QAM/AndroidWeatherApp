package weather.qam.util;

import android.util.Log;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ListView;
import android.view.View;
/**
 * Created by qam on 1/30/15.
 */
public class ViewHelper {
    private static final String TAG = "ViewHelper";

    //expand listview without scorllbar
    public static void adjustListView(ListView lv){
        Adapter ad = lv.getAdapter();
        int totalheightsize = 0;
        for(int i=0; i<ad.getCount(); ++i) {
            View v = ad.getView(i, null, lv);
            v.measure(0, 0);
            totalheightsize += v.getMeasuredHeight();
            Log.i(TAG, "v height is: "+v.getMeasuredHeight());
        }
        Log.i(TAG, "height is: "+totalheightsize);
        ViewGroup.LayoutParams lp = lv.getLayoutParams();
        lp.height = (int) ((float)totalheightsize/2); //+ (lv.getDividerHeight() * (ad.getCount() - 1));
        lv.setLayoutParams(lp);
    }

    public static void adjustHeight(View v){
        ViewGroup.LayoutParams params = v.getLayoutParams();
        Log.i(TAG, "width is: "+v.getWidth()+" height is:"+v.getHeight());
        params.height = v.getWidth()*3/8;
        v.requestLayout();
    }
}
