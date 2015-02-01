package weather.qam.JsonParser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;

import java.io.InputStreamReader;

/**
 * Created by qam on 1/22/15.
 */
public class JsonYahooPlace {
    private static final String TAG = "JsonYahooPlace";

    protected String woeid;
    protected String county;

    private JsonYahooPlace(){
        woeid = null;
        county = null;
    }

    public static List<JsonYahooPlace> placeParse(InputStream in) throws IOException {
        List<JsonYahooPlace> result = null;
        int count = 0;
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        try {
            reader.beginObject();
            String name = reader.nextName(); //query
            reader.beginObject();

            while (reader.hasNext()) {
                if(reader.peek() == JsonToken.NAME){
                    name = reader.nextName();
                    Log.i(TAG, name);
                    if(0 == name.compareTo("count")){
                        count = reader.nextInt();
                    }else if (0 == name.compareTo("results")) {
                        if(reader.peek() == JsonToken.NULL) break;
                        reader.beginObject();
                        name = reader.nextName();
                        if( 0 == name.compareTo("Result") ){
                            result = getData(reader, count);
                            break;
                        }
                    } else {
                        reader.skipValue();
                    }
                }else {
                    reader.skipValue();
                }
            }
        }finally{
            reader.close();
            in.close();
        }
        return result;
    }

    private static List<JsonYahooPlace> getData(JsonReader reader, int count) throws IOException {
        List<JsonYahooPlace> result = new ArrayList<>();
        if(count > 1) reader.beginArray();
        while(reader.hasNext()){
            if(reader.peek() == JsonToken.BEGIN_OBJECT){
                reader.beginObject();
                JsonYahooPlace p = new JsonYahooPlace();
                while(reader.hasNext()){
                    if(reader.peek() == JsonToken.END_OBJECT) break;
                    else{
                        String name = reader.nextName();
                        if (name.equals("county")) {
                            p.county = reader.nextString();
                        } else if (name.equals("woeid")) {
                            p.woeid = reader.nextString();
                        } else {
                            reader.skipValue();
                        }
                    }
                }
                reader.endObject();
                result.add(p);
            }
        }
        return result;
    }

    public String getPlace(){
        return county;
    }
}
