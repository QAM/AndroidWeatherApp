package weather.qam.weather;

import android.app.Activity;
import android.os.Bundle;
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

/**
 * Created by qam on 1/24/15.
 */
public class LocActivity extends Activity implements AdapterView.OnItemClickListener{

    ImageButton searchbtn;
    AutoCompleteTextView at;
    ListView cityList;

    CoutryDBHelper countryDB;
    CoutryDBHelper historyDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loc);

        initCountryDB();


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
        at = (AutoCompleteTextView) findViewById(R.id.country_search);
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.select_dialog_singlechoice, languages);
        //countryDB = new CoutryDBHelper(this, AppConstant.COUNTRY_DB_TABLENAME, AppConstant.COUNTRY_DB_TABLENAME);
        //countryDB.openDB();
        //CountryAdapter ca = new CountryAdapter(this, countryDB);
        //at.setAdapter(ca);

        cityList = (ListView) findViewById(R.id.country_history);
        ArrayList<String> hlist = getHistory(); hlist.add(0, AppConstant.currentLoc);
        HistoryAdapter ha = new HistoryAdapter(this, 0, hlist);
        cityList.setAdapter(ha);
        cityList.setOnItemClickListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //countryDB.closeDB();
    }

    private void initCountryDB(){
        countryDB = new CoutryDBHelper(this, AppConstant.COUNTRY_DB_TABLENAME, AppConstant.COUNTRY_DB_TABLENAME);
        countryDB.openDB();
        if(countryDB.getCount() <= 0) insertAllCountries(countryDB);
        countryDB.closeDB();
    }

    private void insertAllCountries(CoutryDBHelper d){
        final String[] countries = new String[] {
                "Afghanistan", "Albania", "Algeria", "American Samoa", "Andorra",
                "Angola", "Anguilla", "Antarctica", "Antigua and Barbuda", "Argentina",
                "Armenia", "Aruba", "Australia", "Austria", "Azerbaijan",
                "Bahrain", "Bangladesh", "Barbados", "Belarus", "Belgium",
                "Belize", "Benin", "Bermuda", "Bhutan", "Bolivia",
                "Bosnia and Herzegovina", "Botswana", "Bouvet Island", "Brazil", "British Indian Ocean Territory",
                "British Virgin Islands", "Brunei", "Bulgaria", "Burkina Faso", "Burundi",
                "Cote d'Ivoire", "Cambodia", "Cameroon", "Canada", "Cape Verde",
                "Cayman Islands", "Central African Republic", "Chad", "Chile", "China",
                "Christmas Island", "Cocos (Keeling) Islands", "Colombia", "Comoros", "Congo",
                "Cook Islands", "Costa Rica", "Croatia", "Cuba", "Cyprus", "Czech Republic",
                "Democratic Republic of the Congo", "Denmark", "Djibouti", "Dominica", "Dominican Republic",
                "East Timor", "Ecuador", "Egypt", "El Salvador", "Equatorial Guinea", "Eritrea",
                "Estonia", "Ethiopia", "Faeroe Islands", "Falkland Islands", "Fiji", "Finland",
                "Former Yugoslav Republic of Macedonia", "France", "French Guiana", "French Polynesia",
                "French Southern Territories", "Gabon", "Georgia", "Germany", "Ghana", "Gibraltar",
                "Greece", "Greenland", "Grenada", "Guadeloupe", "Guam", "Guatemala", "Guinea", "Guinea-Bissau",
                "Guyana", "Haiti", "Heard Island and McDonald Islands", "Honduras", "Hong Kong", "Hungary",
                "Iceland", "India", "Indonesia", "Iran", "Iraq", "Ireland", "Israel", "Italy", "Jamaica",
                "Japan", "Jordan", "Kazakhstan", "Kenya", "Kiribati", "Kuwait", "Kyrgyzstan", "Laos",
                "Latvia", "Lebanon", "Lesotho", "Liberia", "Libya", "Liechtenstein", "Lithuania", "Luxembourg",
                "Macau", "Madagascar", "Malawi", "Malaysia", "Maldives", "Mali", "Malta", "Marshall Islands",
                "Martinique", "Mauritania", "Mauritius", "Mayotte", "Mexico", "Micronesia", "Moldova",
                "Monaco", "Mongolia", "Montserrat", "Morocco", "Mozambique", "Myanmar", "Namibia",
                "Nauru", "Nepal", "Netherlands", "Netherlands Antilles", "New Caledonia", "New Zealand",
                "Nicaragua", "Niger", "Nigeria", "Niue", "Norfolk Island", "North Korea", "Northern Marianas",
                "Norway", "Oman", "Pakistan", "Palau", "Panama", "Papua New Guinea", "Paraguay", "Peru",
                "Philippines", "Pitcairn Islands", "Poland", "Portugal", "Puerto Rico", "Qatar",
                "Reunion", "Romania", "Russia", "Rwanda", "Sqo Tome and Principe", "Saint Helena",
                "Saint Kitts and Nevis", "Saint Lucia", "Saint Pierre and Miquelon",
                "Saint Vincent and the Grenadines", "Samoa", "San Marino", "Saudi Arabia", "Senegal",
                "Seychelles", "Sierra Leone", "Singapore", "Slovakia", "Slovenia", "Solomon Islands",
                "Somalia", "South Africa", "South Georgia and the South Sandwich Islands", "South Korea",
                "Spain", "Sri Lanka", "Sudan", "Suriname", "Svalbard and Jan Mayen", "Swaziland", "Sweden",
                "Switzerland", "Syria", "Taiwan", "Tajikistan", "Tanzania", "Thailand", "The Bahamas",
                "The Gambia", "Togo", "Tokelau", "Tonga", "Trinidad and Tobago", "Tunisia", "Turkey",
                "Turkmenistan", "Turks and Caicos Islands", "Tuvalu", "Virgin Islands", "Uganda",
                "Ukraine", "United Arab Emirates", "United Kingdom",
                "United States", "United States Minor Outlying Islands", "Uruguay", "Uzbekistan",
                "Vanuatu", "Vatican City", "Venezuela", "Vietnam", "Wallis and Futuna", "Western Sahara",
                "Yemen", "Yugoslavia", "Zambia", "Zimbabwe"
        };

        for(int i = 0; i < countries.length; ++i)
            d.insertCountry(countries[i]);

    }

    private void insertHistory(String s){
        historyDB = new CoutryDBHelper(this, AppConstant.HISTORY_DB_TABLENAME, AppConstant.HISTORY_DB_TABLENAME);
        historyDB.openDB();
        historyDB.insertCountry(s);
        historyDB.closeDB();
    }

    private ArrayList<String> getHistory(){
        historyDB = new CoutryDBHelper(this, AppConstant.HISTORY_DB_TABLENAME, AppConstant.HISTORY_DB_TABLENAME);
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
}
