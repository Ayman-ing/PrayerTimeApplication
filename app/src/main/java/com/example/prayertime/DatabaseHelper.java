package com.example.prayertime;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.prayertime.dataModels.City;
import com.example.prayertime.dataModels.Country;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "locations.db3";
    private static final int DATABASE_VERSION = 2;

    private  String COUNTRY_TABLE = "countries";

    private final Context context;
    public DatabaseHelper(@Nullable Context context) throws IOException {
        super(context,DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }



    SQLiteDatabase db = this.getReadableDatabase();


    @Override
    public void onCreate(SQLiteDatabase db) {
        //no need
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //no need
    }

    public void copyDatabaseFromAssets() throws IOException {
        InputStream inputStream = context.getAssets().open(DATABASE_NAME);
        String outFileName = context.getDatabasePath(DATABASE_NAME).getPath();

        OutputStream outputStream = new FileOutputStream(outFileName);

        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
        }

        outputStream.flush();
        outputStream.close();
        inputStream.close();
    }

    @SuppressLint("Range")
    public List<Country> getCountries() {
        List<Country> countries = new ArrayList<>();


        Cursor cursor = db.rawQuery("SELECT * FROM countries",null);

        if (cursor.moveToFirst()) {
            do {
                String countryCode = cursor.getString(cursor.getColumnIndex("countryCode"));
                String nameEN = cursor.getString(cursor.getColumnIndex("nameEN"));
                String nameAR = cursor.getString(cursor.getColumnIndex("nameAR"));
                countries.add(new Country(countryCode, nameEN, nameAR));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return countries;

    }



    public List<City> getCities(String countryCode) {
        List<City> cities = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String[] selectionArgs = {countryCode};
        Cursor cursor = db.rawQuery("SELECT * FROM cities WHERE countryCode = ?", selectionArgs);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int cityId = cursor.getInt(cursor.getColumnIndex("cityId"));
                @SuppressLint("Range") String nameEN = cursor.getString(cursor.getColumnIndex("nameEN"));
                @SuppressLint("Range") String nameAR = cursor.getString(cursor.getColumnIndex("nameAR"));
                @SuppressLint("Range") double latitude = cursor.getDouble(cursor.getColumnIndex("latitude"));
                @SuppressLint("Range") double longitude = cursor.getDouble(cursor.getColumnIndex("longitude"));
                @SuppressLint("Range") int altitude = cursor.getInt(cursor.getColumnIndex("altitude"));
                cities.add(new City(cityId, nameEN, nameAR, latitude, longitude, altitude));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return cities;
    }
}
