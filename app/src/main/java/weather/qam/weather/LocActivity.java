package weather.qam.weather;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.AutoCompleteTextView;
import android.content.Intent;

/**
 * Created by qam on 1/24/15.
 */
public class LocActivity extends Activity{

    ImageButton searchbtn;
    AutoCompleteTextView at;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loc);

        searchbtn = (ImageButton) findViewById(R.id.search_btn);
        at = (AutoCompleteTextView) findViewById(R.id.country_search);
        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(at.getText().length() > 0){
                    String message=at.getText().toString();
                    Intent intent=new Intent();
                    intent.putExtra("MESSAGE",message);
                    setResult(RESULT_OK,intent);
                    finish();//finishing activity
                }
            }
        });
    }


}
