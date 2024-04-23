package com.example.prayertime;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import android.os.Looper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prayertime.dataModels.City;
import com.example.prayertime.dataModels.Country;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;


public class GetLocationActivity extends AppCompatActivity {


    private Button GetPrayerTimeGPS;
    private Button activateGPSButton;
    private Button givePermissionButton;
    private LocationRequest locationRequest;
    private Spinner countrySpinner;
    private Spinner citySpinner;
    private DatabaseHelper dbHelper;

    private Button getPrayerTime;
    Double cityLatitude;
    Double cityLongitude;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_location);

        countrySpinner = findViewById(R.id.countrySpinner);
        citySpinner = findViewById(R.id.citySpinner);

        try {
            dbHelper = new DatabaseHelper(getApplicationContext());
            dbHelper.copyDatabaseFromAssets();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        populateCountrySpinner();
        // Set listener for country spinner item selection
       countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Cast the parent AdapterView to a Spinner
                Spinner spinner = (Spinner) parent;

                // Get the selected item (country) from the Spinner
                Country selectedCountry = (Country) spinner.getSelectedItem();

                // Get the country code from the selected item
                String countryCode = selectedCountry.getCode();
                populateCitySpinner(countryCode);// Get selected country code from spinner

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Cast the parent AdapterView to a Spinner
                Spinner spinner = (Spinner) parent;

                // Get the selected item (country) from the Spinner
                City selectedCity = (City) spinner.getSelectedItem();

                // Get the country code from the selected item
                cityLatitude = selectedCity.getLatitude();
                cityLongitude = selectedCity.getLongitude();


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
        GetPrayerTimeGPS = findViewById(R.id.GetPrayerTimeGPS);
        activateGPSButton=findViewById(R.id.activateGPS);
        givePermissionButton=findViewById(R.id.givePermission);
        getPrayerTime=findViewById(R.id.GetPrayerTime);
        locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY,5000)
                .setMinUpdateIntervalMillis(2000).build();
        activateGPSButton.setOnClickListener(v -> turnOnGPS());

        GetPrayerTimeGPS.setOnClickListener(v -> getCurrentLocation());
        givePermissionButton.setOnClickListener(v -> requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1));
        getPrayerTime.setOnClickListener(v -> getPrayerTimeByCity());


    }

    private void getPrayerTimeByCity() {

        Intent intent = new Intent(GetLocationActivity.this, MainActivity.class);
        intent.putExtra("latitude", cityLatitude); // Add data to the intent
        intent.putExtra("longitude", cityLongitude);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){

                if (isGPSEnabled()) {

                    String text = "Permission granted ,and localisation is already turned on ";
                    Toast.makeText(getApplicationContext(),text, Toast.LENGTH_LONG).show();

                }else {
                    String text = "Permission granted ,turn on localisation to continue";
                    Toast.makeText(getApplicationContext(),text, Toast.LENGTH_LONG).show();
                }
            }
        }


    }

   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {

                getCurrentLocation();
            }
        }
    }
*/
    private void getCurrentLocation() {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(GetLocationActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                if (isGPSEnabled()) {

                    LocationServices.getFusedLocationProviderClient(GetLocationActivity.this)
                            .requestLocationUpdates(locationRequest, new LocationCallback() {
                                @Override
                                public void onLocationResult(@NonNull LocationResult locationResult) {
                                    super.onLocationResult(locationResult);

                                    LocationServices.getFusedLocationProviderClient(GetLocationActivity.this)
                                            .removeLocationUpdates(this);

                                    if (locationResult != null && locationResult.getLocations().size() >0){

                                        int index = locationResult.getLocations().size() - 1;
                                        double latitude = locationResult.getLocations().get(index).getLatitude();
                                        double longitude = locationResult.getLocations().get(index).getLongitude();
                                        Intent intent = new Intent(GetLocationActivity.this, MainActivity.class);
                                        intent.putExtra("latitude", latitude); // Add data to the intent
                                        intent.putExtra("longitude", longitude);
                                        startActivity(intent);



                                    }
                                }
                            }, Looper.getMainLooper());

                } else {
                    String text = "you need to turn on localisation first";
                    Toast.makeText(getApplicationContext(),text, Toast.LENGTH_LONG).show();
                }

            } else {
                String text = "you need to give permission first";
                Toast.makeText(getApplicationContext(),text, Toast.LENGTH_LONG).show();
            }
        }
    }

    private void turnOnGPS() {



        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(getApplicationContext())
                .checkLocationSettings(builder.build());

        result.addOnCompleteListener(task -> {

            try {
                LocationSettingsResponse response = task.getResult(ApiException.class);
                Toast.makeText(GetLocationActivity.this, "localisation is already turned on", Toast.LENGTH_SHORT).show();

            } catch (ApiException e) {

                switch (e.getStatusCode()) {
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                        try {
                            ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                            resolvableApiException.startResolutionForResult(GetLocationActivity.this, 2);
                        } catch (IntentSender.SendIntentException ex) {
                            ex.printStackTrace();
                        }
                        break;

                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        //Device does not have location
                        break;
                }
            }
        });

    }

    private boolean isGPSEnabled() {
        LocationManager locationManager = null;
        boolean isEnabled = false;

        if (locationManager == null) {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        }

        isEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return isEnabled;

    }
    private void populateCountrySpinner() {
        List<Country> countries = dbHelper.getCountries();

        ArrayAdapter<Country> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, countries);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        countrySpinner.setAdapter(adapter);
    }
    private void populateCitySpinner(String countryCode) {
        List<City> cities = dbHelper.getCities(countryCode);
        ArrayAdapter<City> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, cities);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        citySpinner.setAdapter(adapter);
    }


}
