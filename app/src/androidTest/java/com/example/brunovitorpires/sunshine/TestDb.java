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
public class TestDb extends AndroidTestCase {

    public void testCreateDb(){         //Teste para verificar se o Banco foi criado e se está aberto; Caso a sessão não seja iniciada significa que o banco não foi criado

        mContext.deleteDatabase(WeatherDbHelper.DATABASE_NAME);
        SQLiteDatabase db = new WeatherDbHelper(mContext)
                .getWritableDatabase();
        assertEquals(true, db.isOpen());
        db.close();
    }

    public void testCrud(){

        WeatherDbHelper helper = new WeatherDbHelper(mContext);
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();

        //Guardando Variáveis para comparação em Teste com os Inputs

        String inputCityName = "Recife";
        String inputLocationSetting = "recife";
        double inputLat = -8.0464492;
        double inputLng = -34.9324882;


        //Inserção de dados nas Colunas
        values.put(WheatherContract.LocationEntry.COLUMN_CITY_NAME, "Recife");
        values.put(WheatherContract.LocationEntry.COLUMN_LOCATION_SETTING, "recife");
        values.put(WheatherContract.LocationEntry.COLUMN_COORD_LAT, -8.0464492);
        values.put(WheatherContract.LocationEntry.COLUMN_COORD_LONG, -34.9324882);

        //Erro = -1

        long id = db.insert(WheatherContract.LocationEntry.TABLE_NAME, null, values);

        assertTrue(id !=-1);

        //Verificar os dados inseridos

        Cursor cursor = db.rawQuery(
                "select * from "+ WheatherContract.LocationEntry.TABLE_NAME +
                        " where "+ WheatherContract.LocationEntry._ID +
                        " = ?", new String[]{String.valueOf(id) });
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
