package com.example.brunovitorpires.sunshine;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.ShareActionProvider;
import android.widget.TextView;

import com.example.brunovitorpires.sunshine.data.WheatherContract;


public class DetailActivity2 extends ActionBarActivity {

    public static final String DATE_KEY = "forecast_date";
    private static final String LOCATION_KEY = "location";
    private static final String FORECAST_SHARE_HASHTAG = "#SunshineApp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_activity2);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new DetailFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail_activity2, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (item.getItemId()) {
            case R.id.action_share:



                return true;//alterar p true
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity2.class));

            default:
                return super.onOptionsItemSelected(item);
        }

    }


    public static class DetailFragment extends Fragment
            implements LoaderManager.LoaderCallbacks<Cursor> {
        private static final int DETAIL_LOADER_ID = 1;
        String mDate;
        public static final String DATE_KEY = "forecast_date";

       private ShareActionProvider mShareActionProvider;
        private String mLocation;
        private String mForecast;

        private static final String[] FORECAST_COLUMNS = {
                WheatherContract.WeatherEntry.TABLE_NAME + "." + WheatherContract.WeatherEntry._ID,
                WheatherContract.WeatherEntry.COLUMN_DATETEXT,
                WheatherContract.WeatherEntry.COLUMN_SHORT_DESC,
                WheatherContract.WeatherEntry.COLUMN_MAX_TEMP,
                WheatherContract.WeatherEntry.COLUMN_MIN_TEMP,
        };
        public DetailFragment() {
            setHasOptionsMenu(true);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_detail_activity2, container, false);

        }
        private Intent createShareIntent(){
            Intent it = new Intent(Intent.ACTION_SEND);
            it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            it.setType("text/plain");
            it.putExtra(Intent.EXTRA_TEXT, mForecast +" #SunshineApp");
            return it;
        }

        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            getLoaderManager().initLoader(DETAIL_LOADER_ID, null, this);
        }
        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            Uri uri = getActivity().getIntent().getData();
            return new CursorLoader(getActivity(), uri, null, null, null, null);
        }
        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            if (data.moveToFirst()) {

                boolean isMetric = Utility.isMetric(getActivity());

                String date = Utility.getFriendlyDayString(getActivity(),data.getString(data.getColumnIndex(
                        WheatherContract.WeatherEntry.COLUMN_DATETEXT)));
                String descr = data.getString(data.getColumnIndex(
                        WheatherContract.WeatherEntry.COLUMN_SHORT_DESC));
                String max = Utility.formatTemperature(getActivity(),data.getDouble(data.getColumnIndex(WheatherContract.WeatherEntry.COLUMN_MAX_TEMP)),isMetric);
                String min = Utility.formatTemperature(getActivity(),data.getDouble(data.getColumnIndex(WheatherContract.WeatherEntry.COLUMN_MIN_TEMP)),isMetric);
                String degress= Utility.formatTemperature(getActivity(),data.getDouble(data.getColumnIndex(WheatherContract.WeatherEntry.COLUMN_DEGREES)),isMetric);
                String humidity = String.valueOf(data.getDouble(data.getColumnIndex(WheatherContract.WeatherEntry.COLUMN_HUMIDITY)));
                String wind = String.valueOf(data.getDouble(data.getColumnIndex(WheatherContract.WeatherEntry.COLUMN_WIND_SPEED)));
                String pressure = String.valueOf(data.getDouble(data.getColumnIndex(WheatherContract.WeatherEntry.COLUMN_PRESSURE)));

                View view = getView();
                if (view != null) {
                    ((TextView) view.findViewById(R.id.text_forecast)).setText(date);
                    ((TextView) view.findViewById(R.id.text_forecast_descr)).setText(descr);
                    ((TextView) view.findViewById(R.id.text_forecast_tempMax)).setText(max);
                    ((TextView) view.findViewById(R.id.text_forecast_tempMin)).setText(min);
                    ((TextView) view.findViewById(R.id.text_forecast_degress)).setText(degress);
                    ((TextView) view.findViewById(R.id.text_forecast_humidity)).setText(humidity);
                    ((TextView) view.findViewById(R.id.text_forecast_winSpeed)).setText(wind);
                    ((TextView) view.findViewById(R.id.text_forecast_pressure)).setText(pressure);




            // Outros campos...
                }
                mForecast = date +" - " + descr +" - " + max +"/"+ min;
               //mShareActionProvider.setShareIntent(createShareIntent());
            }
        }



        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
        }


    }



}