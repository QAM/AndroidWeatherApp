package weather.qam.util;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.File;
import java.io.OutputStream;

import android.content.Context;
import java.io.FileInputStream;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.Log;

/**
 * Created by qam on 1/24/15.
 */
public class FileOperation {
    private static final String TAG = "FileOperation";

    //1. store weather Json file
    public static boolean storeFile(final Context context, InputStream in, String folder, String op_filename){
        try {
            final File file = new File(folder, op_filename);
            if(file.exists()) file.delete();
            final OutputStream op = new FileOutputStream(file, true);
            final byte[] buffer = new byte[1024];
            int size;
            while( (size = in.read(buffer)) != -1){
               op.write(buffer, 0, size);
            }
            op.flush();
            op.close();
        }catch( Exception e ){
            e.printStackTrace();
            return false;
        }finally{
            //in.close();
        }
        return true;
    }

    public static InputStream readFile(final Context context, String folder, String in_filename){
        InputStream in = null;
        try {
            final File file = new File(folder, in_filename);
            in = new FileInputStream(file);
        }catch( Exception e ){
            e.printStackTrace();
        }finally{
            return in;
        }
    }

    //2.
    public synchronized static Drawable getAndroidDrawable(final Context ctx, String pDrawableName){
        Log.i(TAG, pDrawableName+" "+ctx.getPackageName());
        int resourceId=ctx.getResources().getIdentifier(pDrawableName, "drawable", ctx.getPackageName());
        Log.i(TAG, "id is:"+resourceId);
        try{
            if(resourceId==0){
                return null;
            } else {
                return ctx.getResources().getDrawable(resourceId);
            }
        }catch(Resources.NotFoundException e){
            e.printStackTrace();
            return null;
        }
    }

}
