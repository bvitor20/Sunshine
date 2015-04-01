package com.example.brunovitorpires.sunshine.data;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Bruno Vitor Pires on 10/03/2015.
 */
public class WheatherContract {

    public static final String CONTENT_AUTHORITY = "com.example.brunovitorpires.sunshine";
    public static final Uri BASE_CONTENT_URI =
            Uri.parse("content://"+ CONTENT_AUTHORITY);
    public static final String PATH_LOCATION = "location";
    public static final String PATH_WEATHER = "weather";




    public static final class LocationEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_LOCATION).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/"+ CONTENT_AUTHORITY +"/"+ PATH_LOCATION;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/"+ CONTENT_AUTHORITY +"/"+ PATH_LOCATION;

        //Atribuir uma ID para o meu URI
        public static Uri buildLocationUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);

        }
        public static final String TABLE_NAME = "location";
        public static final String COLUMN_LOCATION_SETTING = "location_setting";
        public static final String COLUMN_CITY_NAME = "city_name";
        public static final String COLUMN_COORD_LAT = "coord_lat";
        public static final String COLUMN_COORD_LONG = "coord_long";
    }
    public static final class WeatherEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_WEATHER).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/"+ CONTENT_AUTHORITY +"/"+ PATH_WEATHER;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/"+ CONTENT_AUTHORITY +"/"+ PATH_WEATHER;



        //content://br.com.nglauber.sunshine/weather/<id> -- Retornando a URI
        public static Uri buildWeatherUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
        //content://br.com.nglauber.sunshine/weather/<location>
        public static Uri buildWeatherLocation(String location){
            return CONTENT_URI.buildUpon()
                    .appendPath(location)
                    .build();
        }

        //content://br.com.nglauber.sunshine/weather/<location>?date=<date> --- Location
        public static Uri buildWeatherLocationWithStartDate(
                String location, String date){
            return CONTENT_URI.buildUpon()
                    .appendPath(location)
                    .appendQueryParameter(COLUMN_DATETEXT, date)
                    .build();
        }
        //content://br.com.nglauber.sunshine/weather/<location>/<date> --- Location + Data
        public static Uri buildWeatherLocationWithDate(
                String location, String date){
            return CONTENT_URI.buildUpon()
                    .appendPath(location)
                    .appendPath(date)
                    .build();
        }

        public static String getLocationFromUri(Uri uri){
            return uri.getPathSegments().get(1);
        }
        public static String getStartDateFromUri(Uri uri){
            return uri.getQueryParameter(COLUMN_DATETEXT);
        }

        public static final String DATE_FORMAT = "yyyyMMdd";
        public static String getDbDateString(Date date){
            return new SimpleDateFormat(DATE_FORMAT).format(date);
        }

        public static String getDateFromUri(Uri uri){
            return uri.getPathSegments().get(2);
        }

        public static final String TABLE_NAME = "weather";
        public static final String COLUMN_LOC_KEY = "location_id";
        public static final String COLUMN_DATETEXT = "date";
        public static final String COLUMN_WEATHER_ID = "weather_id";
        public static final String COLUMN_SHORT_DESC = "short_desc";
        public static final String COLUMN_MIN_TEMP = "min";
        public static final String COLUMN_MAX_TEMP = "max";
        public static final String COLUMN_HUMIDITY = "humidity";
        public static final String COLUMN_PRESSURE = "pressure";
        public static final String COLUMN_WIND_SPEED = "wind";
        public static final String COLUMN_DEGREES = "degrees";

        public static Date getDateFromDb(String dateText) {
            SimpleDateFormat dbDateFormat = new SimpleDateFormat(DATE_FORMAT);
            try{
                return dbDateFormat.parse(dateText);

            }catch(ParseException e){
                e.printStackTrace();
                return null;
            }

        }
    }
}
