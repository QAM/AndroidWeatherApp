package weather.qam.util;

/**
 * Created by qam on 1/22/15.
 */
public class AppConstant {
    //1. weather api, search via place text
    //select * from weather.forecast where woeid in (select woeid from geo.places(1) where text="nome, ak")
    //https://query.yahooapis.com/v1/public/yql?q=
    //        select%20*%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22nome%2C%20ak%22)
    //        &format=json
    //2. find woeid via lat and long
    //select * from geo.placefinder where text="37.416275,-122.025092" and gflags="R"
    //https://query.yahooapis.com/v1/public/yql?q=
    //        select%20*%20from%20geo.placefinder%20where%20text%3D%2237.416275%2C-122.025092%22%20and%20gflags%3D%22R%22
    //        &format=json
    //3. weather api, search via woeid
    //select * from weather.forecast where woeid=12797150
    //https://query.yahooapis.com/v1/public/yql?q=
    //        select%20*%20from%20weather.forecast%20where%20woeid%3D12797150
    //        &format=json

    public static final String yahooApiYQLBase = "http://query.yahooapis.com/v1/public/yql?q=";
    public static final String yahooApiYQLFormatJson = "&format=json";

    public static final String yahooWTStart = "select * from weather.forecast where woeid in (select woeid from geo.places(1) where text=\"";
    public static final String yahooWTEnd = "\")";

    public static final String yahooPlaceStart = "select * from geo.placefinder where text=\"";
    public static final String yahooPlaceEnd = "\" and gflags=\"R\"";

    public static final String asynctaskGetPlace = "place";
    public static final String asynctaskGetCommand = "command";  //1: weather search, 2: place search -> weather search


    public static final String weatherFileName = "weatherJson";

    public static final String placesFileName = "places";

    public static final String currentLoc = "Current Location";

    public static final String COUNTRY_DB_TABLENAME = "country";
    public static final String HISTORY_DB_TABLENAME = "history";
}
