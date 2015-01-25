package weather.qam.Adapter;

import android.content.Context;
import android.widget.ArrayAdapter;

import weather.qam.JsonParser.JsonYahooWeather;
import java.util.List;
import weather.qam.JsonParser.JsonYahooWeather.Weather.Forecast;
import weather.qam.util.FileOperation;
import weather.qam.weather.R;

import android.view.ViewGroup;
import android.view.View;
import android.widget.TextView;
import android.widget.ImageView;
import android.view.LayoutInflater;
import android.app.Activity;

/**
 * Created by qam on 1/25/15.
 */
public class ForecastAdapter extends ArrayAdapter<Forecast> {


    public ForecastAdapter(Context context, int resource, List<Forecast> objects) {
        super(context, resource, objects);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return super.getCount();
    }

    @Override
    public Forecast getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        final Forecast forecast = this.getItem(position);
        if (convertView == null) {
            LayoutInflater inflater = ((Activity) this.getContext())
                    .getLayoutInflater();
            convertView = inflater.inflate(R.layout.listitem_forecast, parent, false);
            holder = new ViewHolder();
            holder.tv_day = (TextView) convertView.findViewById(R.id.forecast_day);
            holder.tv_tempDesc = (TextView) convertView.findViewById(R.id.forecast_tempdesc);
            holder.iv_code = (ImageView) convertView.findViewById(R.id.forecast_code);
            holder.tv_tempH = (TextView) convertView.findViewById(R.id.forecast_temph);
            holder.tv_tempL = (TextView) convertView.findViewById(R.id.forecast_templ);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tv_day.setText(forecast.day);
        holder.tv_tempDesc.setText(forecast.tempDesc);
        holder.iv_code.setImageDrawable(FileOperation.getAndroidDrawable(getContext(), "w"+forecast.code));
        holder.tv_tempH.setText(forecast.tempH);
        holder.tv_tempL.setText(forecast.tempL);
        return convertView;
    }


    class ViewHolder {
        TextView tv_day;
        TextView tv_tempDesc;
        ImageView iv_code;
        TextView tv_tempH;
        TextView tv_tempL;
    }
}