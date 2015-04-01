package com.example.brunovitorpires.sunshine;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.brunovitorpires.sunshine.data.WheatherContract;

public class ForecastAdapter extends CursorAdapter {
    public ForecastAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.list_item_forecast, parent, false);
        return view;
    }
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        int idx_max_temp = cursor.getColumnIndex(
                WheatherContract.WeatherEntry.COLUMN_MAX_TEMP);
        int idx_min_temp = cursor.getColumnIndex(
                WheatherContract.WeatherEntry.COLUMN_MIN_TEMP);
        int idx_date = cursor.getColumnIndex(
                WheatherContract.WeatherEntry.COLUMN_DATETEXT);
        int idx_short_desc = cursor.getColumnIndex(
                WheatherContract.WeatherEntry.COLUMN_SHORT_DESC);
        boolean isMetric = Utility.isMetric(mContext);

        String highAndLow =
                Utility.formatTemperature(cursor.getDouble(idx_max_temp), isMetric)
                        + "/" +
                        Utility.formatTemperature(cursor.getDouble(idx_min_temp), isMetric);
        String weatherText = Utility.formatDate(cursor.getString(idx_date))
                + " - " + cursor.getString(idx_short_desc) + " - " + highAndLow;

        TextView txtDate = (TextView)view.findViewById(R.id.list_item_date_textview);
        txtDate.setText(Utility.formatDate(cursor.getString(idx_date)));

        TextView txtDescr = (TextView)view.findViewById(R.id.list_item_forecast_textview);
        txtDescr.setText(cursor.getString(idx_short_desc));

        TextView txtMax = (TextView)view.findViewById(R.id.list_item_high_textview);
        txtMax.setText(Utility.formatTemperature(cursor.getDouble(idx_max_temp),isMetric));

        TextView txtMin = (TextView)view.findViewById(R.id.list_item_low_texview);
        txtMin.setText(Utility.formatTemperature(cursor.getDouble(idx_min_temp),isMetric));
    }
}