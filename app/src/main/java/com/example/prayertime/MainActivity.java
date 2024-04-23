package com.example.prayertime;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.batoulapps.adhan.CalculationMethod;
import com.batoulapps.adhan.CalculationParameters;
import com.batoulapps.adhan.Coordinates;
import com.batoulapps.adhan.PrayerTimes;
import com.batoulapps.adhan.data.DateComponents;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;
import java.time.chrono.HijrahDate;
import java.util.Locale;
public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView fajr = findViewById(R.id.fajr);

        TextView sunrise = findViewById(R.id.sunrise);
        TextView dhuhr = findViewById(R.id.dhuhr);
        TextView asr = findViewById(R.id.asr);
        TextView maghrib = findViewById(R.id.maghrib);
        TextView isha = findViewById(R.id.isha);
        TextView datePerview = findViewById(R.id.date);
        TextView hijraDate = findViewById(R.id.hijra_date);
        DateComponents date = DateComponents.from(new Date());
        HijrahDate islamicDate = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            islamicDate = HijrahDate.now();
        }
        Intent intent = getIntent();
        Double latitude = intent.getDoubleExtra("latitude", 0.0);
        Double longitude = intent.getDoubleExtra("longitude", 0.0);


        Coordinates coordinates = new Coordinates(latitude, longitude);
        CalculationParameters params =
                CalculationMethod.MUSLIM_WORLD_LEAGUE.getParameters();
        PrayerTimes prayerTimes = new PrayerTimes(coordinates, date, params);
        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm a");
        DateTimeFormatter formatter_arabic = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            formatter_arabic = DateTimeFormatter.ofPattern("dd MMMM yyyy", new Locale("ar", "SA"));
        }
        DateTimeFormatter formatter_date = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            formatter_date = DateTimeFormatter.ofPattern("dd MMMM yyyy",Locale.ENGLISH);
        }
        String formattedIslamicDate = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            formattedIslamicDate = islamicDate.format(formatter_arabic);
        }
        LocalDate christianDate =null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
             christianDate = LocalDate.now();
        }

        String formattedChristianDate = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            formattedChristianDate = christianDate.format(formatter_date);
        }
        formatter.setTimeZone(TimeZone.getTimeZone(TimeZoneUtil.getTimeZone()));
        datePerview.setText(formattedChristianDate);
        hijraDate.setText(formattedIslamicDate);
        fajr.setText("Fajr : " + formatter.format(prayerTimes.fajr));
        sunrise.setText("Sunrise: " + formatter.format(prayerTimes.sunrise));
        dhuhr.setText("Dhuhr: " + formatter.format(prayerTimes.dhuhr));
        asr.setText("Asr: " + formatter.format(prayerTimes.asr));
        maghrib.setText("Maghrib: " + formatter.format(prayerTimes.maghrib));
        isha.setText("Isha: " + formatter.format(prayerTimes.isha));


    }


}