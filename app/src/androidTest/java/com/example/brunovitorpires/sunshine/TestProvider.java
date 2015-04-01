package com.example.brunovitorpires.sunshine;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import com.example.brunovitorpires.sunshine.data.WeatherDbHelper;
import com.example.brunovitorpires.sunshine.data.WheatherContract;

/**
 * Created by Bruno Vitor Pires on 10/03/2015.
 */
public class TestProvider extends AndroidTestCase {


    public void testGetType(){
        //Weather
        String type = getContext().getContentResolver().getType(
                WheatherContract.WeatherEntry.CONTENT_URI);
        assertEquals(type, WheatherContract.WeatherEntry.CONTENT_TYPE);
        //Location
        type = getContext().getContentResolver().getType(
                WheatherContract.LocationEntry.CONTENT_URI);
        assertEquals(type, WheatherContract.LocationEntry.CONTENT_TYPE);
        //Weather + Location
        type = getContext().getContentResolver().getType(
                WheatherContract.WeatherEntry.buildWeatherLocation("Recife"));
        assertEquals(type, WheatherContract.WeatherEntry.CONTENT_TYPE);

        //Weather+Location+Date
        type = getContext().getContentResolver().getType(
                WheatherContract.WeatherEntry.buildWeatherLocationWithDate("Recife", "20150317"));
        assertEquals(type, WheatherContract.WeatherEntry.CONTENT_ITEM_TYPE);


    // Fazer para as 3 outras URIs...
    }



    public void testCrud(){

        WeatherDbHelper helper = new WeatherDbHelper(mContext);
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();

        //Guardando Variáveis para comparação em Teste com os Inputs

        String inputCityName = "Recife";
        String inputLocationSetting = "recife123";
        double inputLat = -8.0464492;
        double inputLng = -34.9324882;


        //Inserção de dados nas Colunas
        values.put(WheatherContract.LocationEntry.COLUMN_CITY_NAME, inputCityName);
        values.put(WheatherContract.LocationEntry.COLUMN_LOCATION_SETTING, inputLocationSetting);
        values.put(WheatherContract.LocationEntry.COLUMN_COORD_LAT, inputLat);
        values.put(WheatherContract.LocationEntry.COLUMN_COORD_LONG, inputLng);

        //Erro = -1 

        long id = db.insert(WheatherContract.LocationEntry.TABLE_NAME, null, values);

        assertTrue(id !=-1);

        //Verificar os dados inseridos

        Cursor cursor = getContext().getContentResolver().query(
                WheatherContract.LocationEntry.CONTENT_URI,
                null, WheatherContract.LocationEntry._ID + " = ?", new String[] {String.valueOf(id)}, null);


        //ler os valores de cada coluna
        assertTrue (cursor.moveToNext()); {
            String name = cursor.getString(
                    cursor.getColumnIndex(
                            WheatherContract.LocationEntry.COLUMN_CITY_NAME));
            String location = cursor.getString(
                    cursor.getColumnIndex(
                            WheatherContract.LocationEntry.COLUMN_LOCATION_SETTING));
            double lat = cursor.getDouble(
                    cursor.getColumnIndex(
                            WheatherContract.LocationEntry.COLUMN_COORD_LAT));
            double lng = cursor.getDouble(
                    cursor.getColumnIndex(
                            WheatherContract.LocationEntry.COLUMN_COORD_LONG));

            assertEquals(name, inputCityName);
            assertEquals(location, inputLocationSetting);
            assertEquals(lat, inputLat);
            assertEquals(lng, inputLng);

        }
        cursor.close();

        db.close();
    }

}
