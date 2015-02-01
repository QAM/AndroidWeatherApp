package weather.qam.Adapter;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.List;
import weather.qam.weather.R;

import android.view.ViewGroup;
import android.view.View;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.app.Activity;

/**
 * Created by qam on 1/29/15.
 */
public class HistoryAdapter extends ArrayAdapter<String>{


    public HistoryAdapter(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public String getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        final String s = this.getItem(position);
        if (convertView == null) {
            LayoutInflater inflater = ((Activity) this.getContext())
                    .getLayoutInflater();
            convertView = inflater.inflate(R.layout.listitem_country, parent, false);
            holder = new ViewHolder();
            holder.loc = (TextView) convertView.findViewById(R.id.loc);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.loc.setText(s);
        return convertView;
    }


    class ViewHolder {
        TextView loc;
    }
}
