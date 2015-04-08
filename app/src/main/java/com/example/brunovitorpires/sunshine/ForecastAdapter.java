package com.example.brunovitorpires.sunshine;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.brunovitorpires.sunshine.data.WheatherContract;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ForecastAdapter extends CursorAdapter {
    public static final int TYPE_TODAY = 0;
    public static final int TYPE_REGULAR = 1;
    public ForecastAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }
    @Override
    public int getItemViewType(int position) {
        return position == 0 ? TYPE_TODAY : TYPE_REGULAR;
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        int viewType = getItemViewType(cursor.getPosition());
        int layout = (viewType == TYPE_TODAY) ?
                R.layout.list_item_forecast_today :
                R.layout.list_item_forecast;

        View view = LayoutInflater.from(context).inflate(layout, parent, false);
        ViewHolder holder = new ViewHolder(view);
        view.setTag(holder);
        return view;
    }
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder = (ViewHolder)view.getTag();

        int idx_max_temp = cursor.getColumnIndex(
                WheatherContract.WeatherEntry.COLUMN_MAX_TEMP);
        int idx_min_temp = cursor.getColumnIndex(
                WheatherContract.WeatherEntry.COLUMN_MIN_TEMP);
        int idx_date = cursor.getColumnIndex(
                WheatherContract.WeatherEntry.COLUMN_DATETEXT);
        int idx_short_desc = cursor.getColumnIndex(
                WheatherContract.WeatherEntry.COLUMN_SHORT_DESC);
        int idx_weather_id = cursor.getColumnIndex(WheatherContract.WeatherEntry.COLUMN_WEATHER_ID);

        boolean isMetric = Utility.isMetric(mContext);


        //////////////////////////////////////////////////////////////////////////////////

        int viewType = getItemViewType(cursor.getPosition());
        int weatherId = cursor.getInt(idx_weather_id);


        holder.dateView.setText(Utility.getFriendlyDayString(context,cursor.getString(idx_date)));
        holder.descriptionView.setText(cursor.getString(idx_short_desc));
        holder.highView.setText(Utility.formatTemperature(context, cursor.getDouble(idx_max_temp),isMetric));
        holder.lowView.setText(Utility.formatTemperature(context, cursor.getDouble(idx_min_temp),isMetric));
        holder.iconView.setImageResource(viewType == TYPE_TODAY ? Utility.getArtResourceForWeatherCondition(weatherId): Utility.getIconResourceForWeatherCondition(weatherId));
    }

    static class ViewHolder {
        public final ImageView iconView;
        public final TextView dateView;
        public final TextView descriptionView;
        public final TextView highView;
        public final TextView lowView;
        ViewHolder(View view) {
            iconView = (ImageView)
                    view.findViewById(R.id.list_item_icon);
            dateView = (TextView)
                    view.findViewById(R.id.list_item_date_textview);
            descriptionView = (TextView)
                    view.findViewById(R.id.list_item_forecast_textview);
            highView = (TextView)
                    view.findViewById(R.id.list_item_high_textview);
            lowView = (TextView)
                    view.findViewById(R.id.list_item_low_textview);
        }
    }

}