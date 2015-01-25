package weather.qam.util;

import android.content.AsyncTaskLoader;
import android.content.Context;
import weather.qam.JsonParser.*;
import android.os.Bundle;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.URL;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.util.List;
import java.io.InputStream;

/**
 * Created by qam on 1/22/15.
 */
public class JsonAsyncTaskLoader extends AsyncTaskLoader<List<JsonYahooWeather>>{
    private static final String TAG = "JsonAsyncTaskLoader";
    int command = 0;
    String place;
    String latlongi;

    @Override
    protected void onStartLoading() {
        Log.d(TAG, "onStartLoading");
        forceLoad();
    }

    public JsonAsyncTaskLoader(Context context, Bundle b) {
        super(context);
        latlongi =null; place = null;
        command = b.getInt(AppConstant.asynctaskGetCommand);
        if( 1 == command ) place = b.getString(AppConstant.asynctaskGetPlace);
        else if( 2 == command ) latlongi = b.getString(AppConstant.asynctaskGetPlace);
        Log.i(TAG, command + " " + latlongi);
    }

    @Override
    public List<JsonYahooWeather> loadInBackground() {
        Log.i(TAG, "loadInBackground");
        List<JsonYahooPlace> places;
        try {
            if( 2 == command /*get place and weather*/){
                latlongi = formUrl(new StringBuilder().append(AppConstant.yahooPlaceStart).append(latlongi).append(AppConstant.yahooPlaceEnd).toString());
                places = getPlace(latlongi);
                if(null == places) return null;
                place = places.get(0).getPlace();
                //TODO check which place I should use
            }
            place =  formUrl(new StringBuilder().append(AppConstant.yahooWTStart).append(place).append(AppConstant.yahooWTEnd).toString());
            return getWeather(place);

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    protected List<JsonYahooWeather> getWeather(String i) throws IOException {
        HttpURLConnection conn = prepareConnection(i);
        InputStream in = conn.getInputStream();
        if(!FileOperation.storeFile(this.getContext(), in, this.getContext().getExternalCacheDir().toString(), AppConstant.weatherFileName)) return null;
        InputStream in2 = FileOperation.readFile(this.getContext(),this.getContext().getExternalCacheDir().toString(), AppConstant.weatherFileName);
        if(null == in2) return null;
        return JsonYahooWeather.weatherParse(in2);
    }

    protected List<JsonYahooPlace> getPlace(String i) throws IOException {
        HttpURLConnection conn = prepareConnection(i);
        return JsonYahooPlace.placeParse(conn.getInputStream());
    }

    protected String formUrl(String in) throws UnsupportedEncodingException {
        return new StringBuilder().append(AppConstant.yahooApiYQLBase).append(URLEncoder.encode(in, "utf-8")).append(AppConstant.yahooApiYQLFormatJson).toString();
    }

    protected HttpURLConnection prepareConnection(String i) throws IOException {
        Log.i(TAG, i);
        URL url = new URL(i);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000 );
        conn.setConnectTimeout(25000);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        conn.connect();
        return conn;
    }
}

