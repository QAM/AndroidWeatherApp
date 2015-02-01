package weather.qam.JsonParser;

import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by qam on 1/22/15.
 */
public class JsonYahooWeather {
    private static final String TAG = "JsonYahooWeather";

    public Weather weather;
    private JsonYahooWeather(){
        weather = new Weather();
    }

    public static List<JsonYahooWeather> weatherParse(InputStream in) throws IOException {
        List<JsonYahooWeather> result = null;
        int count = 0;
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        try {
            reader.beginObject();
            String name = reader.nextName(); //query
            reader.beginObject();

            while (reader.hasNext()) {
                Log.i(TAG, "loop");
                if(reader.peek() == JsonToken.NAME){
                    name = reader.nextName();
                    Log.i(TAG, name);
                    if(0 == name.compareTo("count")){
                        count = reader.nextInt();
                    }else if (0 == name.compareTo("results")) {
                        if(reader.peek() == JsonToken.NULL) break;
                        reader.beginObject();
                        name = reader.nextName();
                        if( 0 == name.compareTo("channel") ){
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
            Log.i(TAG,"CLOSE READER IN");
            reader.close();
            in.close();
        }
        return result;
    }


    private static List<JsonYahooWeather> getData(JsonReader reader, int count) throws IOException {
        List<JsonYahooWeather> result = new ArrayList<>();
        if(count > 1) reader.beginArray();
        while(reader.hasNext()){
            if(reader.peek() == JsonToken.BEGIN_OBJECT){
                reader.beginObject();
                JsonYahooWeather w = new JsonYahooWeather();
                while(reader.hasNext()){
                    if(reader.peek() == JsonToken.END_OBJECT) break;
                    else{
                        String name = reader.nextName();
                        if (name.equals("location")) {
                            reader.beginObject();
                            while(reader.hasNext()){
                                if(reader.peek() == JsonToken.END_OBJECT) break;
                                else {
                                    name = reader.nextName();
                                    if (name.equals("city"))
                                        w.weather.location.city = reader.nextString();
                                    else if (name.equals("country"))
                                        w.weather.location.country = reader.nextString();
                                    else if (name.equals("region"))
                                        w.weather.location.region = reader.nextString();
                                }
                            }reader.endObject();
                        } else if (name.equals("units")) {
                            reader.beginObject();
                            while(reader.hasNext()){
                                if(reader.peek() == JsonToken.END_OBJECT) break;
                                else {
                                    name = reader.nextName();
                                    if (name.equals("distance"))
                                        w.weather.units.distance = reader.nextString();
                                    else if (name.equals("pressure"))
                                        w.weather.units.pressure = reader.nextString();
                                    else if (name.equals("speed"))
                                        w.weather.units.speed = reader.nextString();
                                    else if (name.equals("temperature"))
                                        w.weather.units.temp = reader.nextString();
                                }
                            }reader.endObject();
                        }else if (name.equals("wind")) {
                            reader.beginObject();
                            while(reader.hasNext()){
                                if(reader.peek() == JsonToken.END_OBJECT) break;
                                else {
                                    name = reader.nextName();
                                    if (name.equals("chill"))
                                        w.weather.wind.chill = reader.nextString();
                                    else if (name.equals("direction"))
                                        w.weather.wind.direc = reader.nextString();
                                    else if (name.equals("speed"))
                                        w.weather.wind.speed = reader.nextString();
                                }
                            }reader.endObject();
                        }else if (name.equals("atmosphere")) {
                            reader.beginObject();
                            while(reader.hasNext()){
                                if(reader.peek() == JsonToken.END_OBJECT) break;
                                else {
                                    name = reader.nextName();
                                    if (name.equals("humidity"))
                                        w.weather.atmosphere.humidity = reader.nextString();
                                    else if (name.equals("pressure"))
                                        w.weather.atmosphere.pressure = reader.nextString();
                                    else if (name.equals("rising"))
                                        w.weather.atmosphere.rising = reader.nextString();
                                    else if (name.equals("visibility"))
                                        w.weather.atmosphere.visibility = reader.nextString();
                                }
                            }reader.endObject();
                        }else if (name.equals("astronomy")) {
                            reader.beginObject();
                            while(reader.hasNext()){
                                if(reader.peek() == JsonToken.END_OBJECT) break;
                                else {
                                    name = reader.nextName();
                                    if (name.equals("sunrise"))
                                        w.weather.astronomy.sunrise = reader.nextString();
                                    else if (name.equals("sunset"))
                                        w.weather.astronomy.sunset = reader.nextString();
                                }
                            }reader.endObject();
                        }else if (name.equals("item")) {
                            reader.beginObject();
                            while(reader.hasNext()){
                                if(reader.peek() == JsonToken.END_OBJECT) break;
                                else {
                                    name = reader.nextName();
                                    if (name.equals("condition")){
                                        reader.beginObject();
                                        while(reader.hasNext()){
                                            if(reader.peek() == JsonToken.END_OBJECT) break;
                                            else {
                                                name = reader.nextName();
                                                if (name.equals("date"))
                                                    w.weather.condition.date = reader.nextString();
                                                else if (name.equals("code"))
                                                    w.weather.condition.code = reader.nextString();
                                                else if (name.equals("temp"))
                                                    w.weather.condition.temp = reader.nextString();
                                                else if (name.equals("text"))
                                                    w.weather.condition.tempDesc = reader.nextString();
                                                else reader.skipValue();
                                            }
                                        }reader.endObject();
                                    }else if(name.equals("forecast")){
                                        reader.beginArray();
                                        while(reader.hasNext()){
                                            if(reader.peek() == JsonToken.BEGIN_OBJECT){
                                                reader.beginObject();
                                                Weather.Forecast f = w.weather.createForecast();
                                                while(reader.hasNext()){
                                                    if(reader.peek() == JsonToken.END_OBJECT) break;
                                                    else{
                                                        name = reader.nextName();
                                                        Log.i(TAG,name);
                                                        if (name.equals("date"))
                                                            f.date = reader.nextString();
                                                        else if (name.equals("day"))
                                                            f.day = reader.nextString();
                                                        else if (name.equals("high"))
                                                            f.tempH = reader.nextString();
                                                        else if (name.equals("low"))
                                                            f.tempL = reader.nextString();
                                                        else if (name.equals("text"))
                                                            f.tempDesc = reader.nextString();
                                                        else if (name.equals("code"))
                                                            f.code = reader.nextString();
                                                        else reader.skipValue();
                                                    }
                                                }
                                                reader.endObject();
                                                w.weather.forecastList.add(f);
                                            }else break;
                                        }
                                        reader.endArray();
                                    }
                                    else reader.skipValue();
                                }
                            }reader.endObject();
                        }else {
                            reader.skipValue();
                        }
                    }
                }
                reader.endObject();
                result.add(w);
            }
        }
        return result;
    }

    public void printWeather(){
        Log.i(TAG, weather.location.country);
        Log.i(TAG, weather.location.city);
        Log.i(TAG, weather.wind.chill);
        Log.i(TAG, weather.wind.direc);
        Log.i(TAG, weather.wind.speed);
        Log.i(TAG, "size is:"+weather.forecastList.size());
    }

    public class Weather{

        public String imageUrl;
        public Condition condition = new Condition();
        public Wind wind = new Wind();
        public Atmosphere atmosphere = new Atmosphere();
        public Location location = new Location();
        public Astronomy astronomy = new Astronomy();
        public Units units = new Units();
        public  List<Forecast> forecastList = new ArrayList<>();

        public Forecast createForecast(){
            return new Forecast();
        }

        public class Location{
            public String city;
            public String country;
            public String region;
        }

        public class Units{
            public String distance;
            public String pressure;
            public String speed;
            public String temp;
        }

        public class Wind{
            public String chill;
            public String direc;
            public String speed;
        }

        public class Atmosphere{
            public String humidity;
            public String pressure;
            public String rising;
            public String visibility;
        }

        public class Astronomy{
            public String sunrise;
            public String sunset;
        }

        public class Condition{
            public String code;
            public String date;
            public String temp;
            public String tempDesc; //text
        }

        public class Forecast{
            public String code;
            public String date;
            public String day;
            public String tempH;
            public String tempL;
            public String tempDesc; //text
        }

    }


}



