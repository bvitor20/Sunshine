package com.example.brunovitorpires.sunshine;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.brunovitorpires.sunshine.data.WheatherContract;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Bruno Vitor Pires on 25/02/2015.
 */
public class ForecastFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    ForecastAdapter adapter;
    private String mLocation;
    ListView listView;



    private static final String[] COLUMNS = {
            WheatherContract.WeatherEntry.TABLE_NAME + "." + WheatherContract.WeatherEntry._ID,
            WheatherContract.WeatherEntry.COLUMN_DATETEXT,
            WheatherContract.WeatherEntry.COLUMN_SHORT_DESC,
            WheatherContract.WeatherEntry.COLUMN_MAX_TEMP,
            WheatherContract.WeatherEntry.COLUMN_MIN_TEMP,
            WheatherContract.LocationEntry.COLUMN_LOCATION_SETTING,
            WheatherContract.WeatherEntry.COLUMN_WEATHER_ID
    };


    private static final int FORECAST_LOADER = 1;

    public ForecastFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        String locationSetting = Utility.getLocationSetting(getActivity());
        String sortOrder = WheatherContract.WeatherEntry.COLUMN_DATETEXT + " ASC";
        Uri weatherForLocationUri =
                WheatherContract.WeatherEntry.buildWeatherLocationWithStartDate(
                        locationSetting, WheatherContract.WeatherEntry.getDbDateString(new Date()));
        Cursor cur = getActivity().getContentResolver().query(
                weatherForLocationUri, null, null, null, sortOrder);

        adapter = new ForecastAdapter(getActivity(), cur, 0);


         listView= (ListView)
                rootView.findViewById(R.id.listview_forecast);

                listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent,
                                            View view, int position, long id) {
                        Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                        if (cursor != null) {
                            String locationSetting =
                                    Utility.getLocationSetting(getActivity());
                            String datStr = cursor.getString(cursor.getColumnIndex(
                                    WheatherContract.WeatherEntry.COLUMN_DATETEXT));
                            Uri uri = WheatherContract.WeatherEntry.buildWeatherLocationWithDate(
                                    locationSetting, datStr);
                            Intent intent = new Intent(getActivity(),
                                    DetailActivity2.class);
                            intent.setData(uri);
                            startActivity(intent);
                        }
                    }
                });
        listView.setAdapter(adapter);


        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(FORECAST_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.forecast_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_refresh) {

            updateWeather();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //@Override
    /*public void onStart() {
        super.onStart();
        updateWeather();
    }
    */

    public void onResume() {
        super.onResume();
        if (mLocation != null && !mLocation.equals(
                Utility.getLocationSetting(getActivity()))) {
            getLoaderManager().restartLoader(FORECAST_LOADER, null, this);
        }
    }

    private void updateWeather() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String location = prefs.getString(
                getString(R.string.pref_location_key),
                getString(R.string.pref_location_default));


        FetchWeatherTask weatherTask = new FetchWeatherTask(getActivity());
        weatherTask.execute(location);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String startDate = WheatherContract.WeatherEntry
                .getDbDateString(new Date());
        String sortOrder = WheatherContract.WeatherEntry.COLUMN_DATETEXT +" ASC";
        mLocation = Utility.getLocationSetting(getActivity());
        Uri uri = WheatherContract.WeatherEntry
                .buildWeatherLocationWithStartDate(mLocation, startDate);
        return new CursorLoader(
                getActivity(),
                uri,
                COLUMNS,
                null,
                null,
                sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor data) {

        adapter.swapCursor(data);
        listView.setAdapter(adapter);



    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

        adapter.swapCursor(null);


    }
}
