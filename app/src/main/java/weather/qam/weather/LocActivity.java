package weather.qam.weather;

import android.app.Activity;
import android.content.Loader;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.AutoCompleteTextView;
import android.content.Intent;
import android.widget.ListView;
import java.util.ArrayList;

import weather.qam.util.AppConstant;
import weather.qam.util.CoutryDBHelper;

import java.util.Collections;
import weather.qam.Adapter.HistoryAdapter;
import weather.qam.Adapter.CityAdapter;
import weather.qam.util.DBAsyncTaskLoader;
import android.app.LoaderManager;

/**
 * Created by qam on 1/24/15.
 */
public class LocActivity extends Activity implements AdapterView.OnItemClickListener,
        LoaderManager.LoaderCallbacks<Integer>{
    private static final String TAG = "LocActivity";

    ImageButton searchbtn;
    AutoCompleteTextView at;
    ListView cityList;

    CoutryDBHelper countryDB;
    CoutryDBHelper historyDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loc);

        //initCountryDB();


        searchbtn = (ImageButton) findViewById(R.id.search_btn);
        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(at.getText().length() > 0){
                    insertHistory(at.getText().toString());
                    String message=at.getText().toString();
                    Intent intent=new Intent();
                    intent.putExtra("MESSAGE",message);
                    setResult(RESULT_OK,intent);
                    finish();//finishing activity
                }
            }
        });

        //String[] languages = { "C","C++","Java","C#","PHP","JavaScript","jQuery","AJAX","JSON" };
        getLoaderManager().restartLoader(1, null, this);
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.select_dialog_singlechoice, languages);
        //countryDB = new CoutryDBHelper(this, AppConstant.COUNTRY_DB_TABLENAME, AppConstant.COUNTRY_DB_TABLENAME);
        //countryDB.openDB();
        //CountryAdapter ca = new CountryAdapter(this, countryDB);
        //at.setAdapter(ca);

        historyDB = new CoutryDBHelper(this, AppConstant.HISTORY_DB_TABLENAME, AppConstant.HISTORY_DB_TABLENAME);
        cityList = (ListView) findViewById(R.id.country_history);
        ArrayList<String> hlist = getHistory(); hlist.add(0, AppConstant.currentLoc);
        HistoryAdapter ha = new HistoryAdapter(this, 0, hlist);
        cityList.setAdapter(ha);
        cityList.setOnItemClickListener(this);
        //ViewHelper.adjustListView(cityList);
    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy(){
        countryDB.closeDB();
        super.onDestroy();
    }


    private void insertHistory(String s){
        historyDB.openDB();
        historyDB.insertCountry(s);
        historyDB.closeDB();
    }

    private ArrayList<String> getHistory(){
        historyDB.openDB();
        ArrayList history = new ArrayList();
        Collections.addAll(history, historyDB.getAllCountries());
        historyDB.closeDB();
        return history;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String message=(String) cityList.getAdapter().getItem(position);
        Intent intent=new Intent();
        intent.putExtra("MESSAGE",message);
        setResult(RESULT_OK,intent);
        finish();//finishing activity
    }

    @Override
    public Loader<Integer> onCreateLoader(int id, Bundle args) {
        Log.i(TAG,"onCreateLoader");
        return new DBAsyncTaskLoader(this, args);
    }

    @Override
    public void onLoadFinished(Loader<Integer> loader, Integer data) {
        if( 0 == data ){
            countryDB = new CoutryDBHelper(this, AppConstant.COUNTRY_DB_TABLENAME, AppConstant.COUNTRY_DB_TABLENAME);
            countryDB.openDB();
            at = (AutoCompleteTextView) findViewById(R.id.country_search);
            CityAdapter ca = new CityAdapter(this, 0, countryDB);
            at.setAdapter(ca);
            at.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String result = (String) parent.getItemAtPosition(position);
                    insertHistory(at.getText().toString());
                    Intent intent=new Intent();
                    intent.putExtra("MESSAGE",result);
                    setResult(RESULT_OK,intent);
                    finish();
                }
            });
        }else Log.i(TAG, "Error: onLoadFinished");
    }

    @Override
    public void onLoaderReset(Loader<Integer> loader) {

    }
}
