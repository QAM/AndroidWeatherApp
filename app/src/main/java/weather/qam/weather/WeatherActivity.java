package weather.qam.weather;

import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;

import weather.qam.Adapter.ForecastAdapter;
import weather.qam.JsonParser.JsonYahooWeather;
import android.app.LoaderManager;
import weather.qam.util.AppConstant;

import weather.qam.util.FileOperation;
import weather.qam.util.JsonAsyncTaskLoader;
import android.app.Activity;
import android.content.Loader;
import android.view.MotionEvent;
import android.view.View;
import java.util.List;
import android.util.Log;
import android.view.View.OnTouchListener;
import weather.qam.View.RippleView;
import android.widget.PopupWindow;
import android.app.Dialog;
import android.content.Intent;
import android.view.Window;
import android.view.WindowManager;
import android.view.Display;
import android.graphics.Point;
import android.location.LocationManager;
import android.location.LocationManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.Criteria;
import android.location.LocationProvider;
import android.location.LocationListener;
import android.content.Context;
import android.provider.Settings;
import android.net.Uri;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.ListView;

public class WeatherActivity extends ActionBarActivity
                            implements LoaderManager.LoaderCallbacks<List<JsonYahooWeather>>
                                        ,LocationListener
{
    private static final String TAG = "WeatherActivity";

    List<JsonYahooWeather> weatherList;
    RippleView rv_btn;
    PopupWindow popupWindow;
    View rlw;
    private Dialog mEnableGPS;
    private LocationManager locationManager;
    String locProvider;
    Activity mActivity;
    int activityReqNum = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        mActivity = this;



        //btn = (ImageButton) findViewById(R.id.btn);
        rv_btn = (RippleView) findViewById(R.id.rv);

        rv_btn.setOnTouchListener(new OnTouchListener() {
            int[] touchdown = new int[] { 0, 0 };
            int[] touchXY = new int[]{ 0, 0 };

            public boolean onTouch(View v, MotionEvent event) {

                int action = event.getAction();

                int x = (int) event.getRawX();
                int y = (int) event.getRawY();
                //Log.i(TAG, "the is:"+x+" "+y+" "+touchXY[0]+" "+touchXY[1]);
                switch (action) {

                    case MotionEvent.ACTION_DOWN: // touch down so check if the
                        touchdown[0] = (int) event.getX();
                        touchdown[1] = y - v.getTop();
                        touchXY[0] = x; touchXY[1] = y;
                        break;

                    case MotionEvent.ACTION_MOVE: // touch drag with the ball
                        Display display = getWindowManager().getDefaultDisplay();
                        Point size = new Point();
                        display.getSize(size);
                        //TODO control float button position
                        int[] boundary = new int[]{
                                (x - touchdown[0]), (y - touchdown[1]),
                                (x + v.getWidth() - touchdown[0]), (y - touchdown[1] + v.getHeight())};
                        Log.i(TAG, "si is:"+boundary[0]+" "+boundary[1]+" "+boundary[2]+" "+boundary[3]+" "
                                +rlw.getWidth()+" "+rlw.getHeight());
                        if( boundary[0] < 0 || boundary[1] < 0 || boundary[2] > rlw.getWidth() || boundary[3] > rlw.getHeight() )
                            ;
                        else
                            v.layout(x - touchdown[0], y - touchdown[1], x + v.getWidth()
                                    - touchdown[0], y - touchdown[1] + v.getHeight());
                        break;

                    case MotionEvent.ACTION_UP:
                        //Log.i(TAG, "au is:"+event.getX()+" "+(y - v.getTop())+" "+touchdown[0]+" "+touchdown[1]);
                        if(touchXY[0] == x && touchXY[1] == y){
                            WeatherActivity.startActivityForResult(mActivity, LocActivity.class, null, activityReqNum);
                            Log.i(TAG, "popup window");
                        }
                        break;
                }

                return false;
            }

        });

        rlw = (View) findViewById(R.id.rlw);
        rlw.setOnTouchListener(new OnTouchListener() {
            int touchdownY = 0;

            public boolean onTouch(View v, MotionEvent event) {

                //int action = event.getAction();

                int x = (int) event.getRawX();
                int y = (int) event.getRawY();

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: // touch down so check if the
                        touchdownY = y;
                        break;

                    case MotionEvent.ACTION_MOVE: // touch drag with the ball
                        Log.i(TAG, "value is: " + (y - touchdownY));
                        if (y < touchdownY) {
                            if (rv_btn.getVisibility() == View.VISIBLE)
                                rv_btn.setVisibility(View.INVISIBLE);
                        } else if (y > touchdownY) {
                            if (rv_btn.getVisibility() == View.INVISIBLE)
                                rv_btn.setVisibility(View.VISIBLE);
                        }
                        break;

                    case MotionEvent.ACTION_UP:
                        break;
                }

                return true;
            }

        });


        //initPopUpLOC(this);
        initLoc();
        popDlgGpsOnOff();
    }


    @Override
    protected void onResume() {
        super.onResume();
        locationManager.requestLocationUpdates(locProvider, 400, 1, this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }

    private void initLoc(){
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        locProvider = locationManager.getBestProvider(criteria, false);
        Log.i(TAG,"loc provider is:"+locProvider);
        Location location = locationManager.getLastKnownLocation(locProvider);


    }

    public void popupLOC(){
        //popupWindow.showAsDropDown(btnOpenPopup, 50, -30);
        popupWindow.showAtLocation(rlw, 50, 50, 0b0);
        //mLOCLookUp = new Dialog(this);
        //mLOCLookUp.setTitle(mActivity.getResources().getString(R.string.Login_text));
        //mLOCLookUp.setCancelable(false);
        //mLOCLookUp.setContentView(R.layout.loc_popup);
        //Button loginBtnOK = (Button)mLoginRegisterDlg.findViewById(R.id.btnOK);
        //Button loginBtnCancel = (Button)mLoginRegisterDlg.findViewById(R.id.btnCancel);
        //loginBtnOK.setOnClickListener(loginDlgBtnOKOnClkLis);
        //loginBtnCancel.setOnClickListener(loginRegisterDlgBtnCancelOnClkLis);


        //mLOCLookUp.show();
    }

    private void popDlgGpsOnOff(){
        if( locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ) return;
        else{
            mEnableGPS = new Dialog(mActivity);
            mEnableGPS.setTitle(mActivity.getResources().getString(R.string.gps_question));
            mEnableGPS.setCancelable(false);
            mEnableGPS.setContentView(R.layout.dlg_gpsonof);
            Button loginBtnOK = (Button)mEnableGPS.findViewById(R.id.btnOK);
            Button loginBtnCancel = (Button)mEnableGPS.findViewById(R.id.btnCancel);
            loginBtnOK.setOnClickListener(loginDlgBtnOKOnClkLis);
            loginBtnCancel.setOnClickListener(loginDlgBtnSkipOnClkLis);
            mEnableGPS.show();
        }
    }

    public void enableGPSService(){
        /*String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        if(!provider.contains("gps")){
            //if gps is disabled
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            sendBroadcast(poke);
        }*/
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS/*"android.location.GPS_ENABLED_CHANGE"*/);
        //intent.putExtra("enabled", true);
        //mActivity.sendBroadcast(intent);
        startActivity(intent);
    }



    public static void startActivity(final Activity activity, Class<?> cls, Bundle b){
        Intent i = new Intent(activity, cls);
        if(null != b) i.putExtra("bundle",b);
        activity.startActivity(i);
    }

    public static void startActivityForResult(final Activity activity, Class<?> cls, Bundle b, int reqnum){
        Intent i = new Intent(activity, cls);
        if(null != b) i.putExtra("bundle",b);
        activity.startActivityForResult(i, reqnum);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == activityReqNum) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // The user picked a contact.
                // The Intent's data Uri identifies which contact was selected.
                Log.i(TAG, "onActivityResult");
                getWeatherBYText(data.getStringExtra("MESSAGE"));
                // Do something with the contact here (bigger example below)
            }
        }
    }

    private Button.OnClickListener loginDlgBtnOKOnClkLis = new Button.OnClickListener() {
        public void onClick(View v) {
            enableGPSService();
            mEnableGPS.cancel();
        }
    };

    private Button.OnClickListener loginDlgBtnSkipOnClkLis = new Button.OnClickListener() {
        public void onClick(View v) {
            mEnableGPS.cancel();
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_weather, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed(){
        Log.i(TAG,"onBackPressed");
        super.onBackPressed();
    }

    //getWeatherBYText("New York");
    private void getWeatherBYText(String place /*new york*/){
        Bundle b = new Bundle();
        b.putString(AppConstant.asynctaskGetPlace,place);
        b.putInt(AppConstant.asynctaskGetCommand, 1);
        getLoaderManager().restartLoader(0, b, this);
    }

    //getWeatherBYLatLong(37.416275, -122.025092);
    private void getWeatherBYLatLong(double lat, double longi){
        locationManager.removeUpdates(this);
        //locationManager.requestLocationUpdates(locProvider, 600000, 10000, this);

        Bundle b = new Bundle();
        String s = new StringBuilder().append(String.valueOf(lat)).append(",").append(String.valueOf(longi)).toString();
        //String s = "tai";
        b.putString(AppConstant.asynctaskGetPlace,s);
        b.putInt(AppConstant.asynctaskGetCommand,2);
        getLoaderManager().restartLoader(0, b, this);
    }

    private void fillUI(List<JsonYahooWeather> w){
        if(null == w || w.size() == 0) return;
        JsonYahooWeather.Weather d = w.get(0).weather;
        TextView loc_city = (TextView) findViewById(R.id.location_city);
        loc_city.setText(d.location.city);
        TextView loc_country = (TextView) findViewById(R.id.location_country);
        loc_country.setText(d.location.country);
        TextView cond_date = (TextView) findViewById(R.id.condition_date);
        cond_date.setText(d.condition.date);
        TextView cond_tempdesc = (TextView) findViewById(R.id.condition_tempdesc);
        cond_tempdesc.setText(d.condition.tempDesc);
        TextView cond_temp = (TextView) findViewById(R.id.condition_temp);
        cond_temp.setText(d.condition.temp);
        ImageView cond_code = (ImageView) findViewById(R.id.condition_code);
        assignTempIcon(cond_code, d.condition.code);
        TextView forecast_name = (TextView) findViewById(R.id.forecast_name);
        forecast_name.setText("Forecast");
        ListView forecast_list = (ListView) findViewById(R.id.forecast_list);
        ForecastAdapter adapter = new ForecastAdapter(this, 0, d.forecastList);
        forecast_list.setAdapter(adapter);
    }

    private void assignTempIcon(ImageView v, String code){
        v.setImageDrawable(FileOperation.getAndroidDrawable(this, "w"+code));
    }

    @Override
    public Loader<List<JsonYahooWeather>> onCreateLoader(int id, Bundle args) {
        return new JsonAsyncTaskLoader(this, args);
    }

    @Override
    public void onLoadFinished(Loader<List<JsonYahooWeather>> loader, List<JsonYahooWeather> data) {
        Log.i(TAG, "onLoadFinished");
        if(null == data) return;
        data.get(0).printWeather();
        weatherList = data;
        fillUI(weatherList);

    }

    @Override
    public void onLoaderReset(Loader<List<JsonYahooWeather>> loader) {
        //TODO update UI
        weatherList = null;
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.i(TAG, "onLocationChanged");
        getWeatherBYLatLong(location.getLatitude(), location.getLongitude());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.i(TAG, "onStatusChanged");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.i(TAG, "onProviderEnabled");
        locProvider = provider;
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.i(TAG, "onProviderDisabled");

    }
}