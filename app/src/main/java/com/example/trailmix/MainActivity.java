package com.example.trailmix;
/*
Author: Patrick Tan
This is the launch screen.
 */
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private boolean onManuel = true;
    private GoogleApiClient mGoogleApiClient;
    private static final String TAG = "MainActivity";
    private Location mLastLocation;
    private final int REQUEST_LOCATION = 200;
    private final int REQUEST_CHECK_SETTINGS = 300;
    private LocationRequest mLocationRequest;
    private PendingResult<LocationSettingsResult> result;
    private LocationSettingsRequest.Builder builder;
    private double longitude;
    private double latitude;
    private String lon;
    private String lat;
    private Double tripDuration;
    private String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }
    //code for getting lat. and long. from https://inducesmile.com/android/android-location-service-api-using-google-play-services/
    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }
    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }
    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = createLocationRequest();
        builder = new LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest);
        result = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                final LocationSettingsStates mState = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can
                        // initialize location requests here.
                        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
                        } else {
                            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                            if (mLastLocation != null) {
                                latitude = mLastLocation.getLatitude();
                                longitude = mLastLocation.getLongitude();
                                //getAddressFromLocation(mLastLocation, getApplicationContext(), new GeoCoderHandler());
                            }
                        }
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied, but this can be fixed
                        // by showing the user a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(MainActivity.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way
                        // to fix the settings so we won't show the dialog.
                        break;
                }
            }
        });
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
                        } else {
                            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                            if (mLastLocation != null) {
                                latitude = mLastLocation.getLatitude();
                                longitude = mLastLocation.getLongitude();
                                //getAddressFromLocation(mLastLocation, getApplicationContext(), new GeoCoderHandler());
                            }
                        }
                        break;
                    case Activity.RESULT_CANCELED:
                        // user does not want to update setting. Handle it in a way that it will to affect your app functionality
                        Toast.makeText(MainActivity.this, "User does not update location setting", Toast.LENGTH_LONG).show();
                        break;
                }
                break;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
    @Override
    public void onLocationChanged(Location location) {
        if (mLastLocation != null) {
            latitude = mLastLocation.getLatitude();
            longitude = mLastLocation.getLongitude();
            //getAddressFromLocation(mLastLocation, getApplicationContext(), new GeoCoderHandler());
        }
    }
    public static void getAddressFromLocation(final Location location, final Context context, final Handler handler) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                String result = null;
                try {
                    List<Address> list = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    if (list != null && list.size() > 0) {
                        Address address = list.get(0);
                        // sending back first address line and locality
                        result = address.getAddressLine(0) + ", " + address.getLocality() + ", " + address.getCountryName();
                    }
                } catch (IOException e) {
                    Log.e(TAG, "Impossible to connect to Geocoder", e);
                } finally {
                    Message msg = Message.obtain();
                    msg.setTarget(handler);
                    if (result != null) {
                        msg.what = 1;
                        Bundle bundle = new Bundle();
                        bundle.putString("address", result);
                        msg.setData(bundle);
                    } else
                        msg.what = 0;
                    msg.sendToTarget();
                }
            }
        };
        thread.start();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    protected LocationRequest createLocationRequest() {
        @SuppressLint("RestrictedApi") LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return mLocationRequest;
    }
    //when there is an input of time
    public void getDuration(View v){
        Context context = getApplicationContext();
        CharSequence text = "Please enter a time at least 3 minutes and less than 600 minutes";
        int duration = Toast.LENGTH_LONG;
        if(onManuel){
            Log.d("Music", "getDuration starting with manuel");
            EditText timeText = findViewById(R.id.editText);
            String timeStr = timeText.getText().toString();
            //starts timer
            if(timeStr.isEmpty())
                timeStr = "0";

            Log.d("Music", timeStr);
            Double timeD = Double.parseDouble(timeStr);
            Log.d("Music", "TargetTime = " + timeD*60);

            if(timeD < 3 || timeD >= 600){
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
            else{
                //SongRetriever sr = new SongRetriever(this);
                long startTime = System.currentTimeMillis();

                Intent intent = new Intent(this,TimeActivity.class);
                intent.putExtra("MinutesTime", timeD);

                startActivity(intent);
                Log.d("Music", "time activity intent started");
            }
        }
        else{
            Boolean mLocationPermissionsGranted = false;
            Log.d("Music", "getDuration starting with maps");
            EditText addressText = findViewById(R.id.address);
            address = addressText.getText().toString();
            //starts timer
            Log.d("Music", address);

            //SongRetriever sr = new SongRetriever(this);
            long startTime = System.currentTimeMillis();
            Log.d("Latitude",""+latitude);
            Log.d("Longitude", ""+longitude);
            lat = ""+latitude;
            lon = ""+longitude;
            //Double tripDuration = AddressParcer.getTripDuration(latitude,longitude,address);
            AddressParcer ap = new AddressParcer(this);
            ap.execute();
            while(tripDuration==null){
                //Log.d("Test","Nah");
            }
            Log.d("Music", "ap.execute");
            Log.d("Time", ""+tripDuration);
            Intent intent = new Intent(this,TimeActivity.class);
            intent.putExtra("MinutesTime", tripDuration);

            startActivity(intent);
            Log.d("Music", "time activity intent started");

        }
    }
    public boolean checkLocationPermission(){
        String permission = "android.permission.ACCESS_FINE_LOCATION";
        int res = this.checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }
    //if need be, this is the get location option
    public void getLocation(View view){
//        Intent intentMaps= new Intent(this,MapsActivity.class);
//        startActivity(intentMaps);
        Button manuel = (Button) findViewById(R.id.manuel);
        manuel.setVisibility(View.VISIBLE);
        Button maps = (Button) findViewById(R.id.maps);
        maps.setVisibility(View.INVISIBLE);
        TextView Question = (TextView) findViewById(R.id.question);
        Question.setVisibility(View.VISIBLE);
        TextView timeView = (TextView) findViewById(R.id.timeView);
        timeView.setVisibility(View.INVISIBLE);
        TextView minutes = (TextView) findViewById(R.id.minutes);
        minutes.setVisibility(View.INVISIBLE);
        EditText number = (EditText) findViewById(R.id.editText);
        number.setVisibility(View.INVISIBLE);
        EditText address = (EditText) findViewById(R.id.address);
        address.setVisibility(View.VISIBLE);
        onManuel = false;


    }
    public void getManuel(View view){
        Button manuel = (Button) findViewById(R.id.manuel);
        manuel.setVisibility(View.INVISIBLE);
        Button maps = (Button) findViewById(R.id.maps);
        maps.setVisibility(View.VISIBLE);
        TextView Question = (TextView) findViewById(R.id.question);
        Question.setVisibility(View.INVISIBLE);
        TextView timeView = (TextView) findViewById(R.id.timeView);
        timeView.setVisibility(View.VISIBLE);
        TextView minutes = (TextView) findViewById(R.id.minutes);
        minutes.setVisibility(View.VISIBLE);
        EditText number = (EditText) findViewById(R.id.editText);
        number.setVisibility(View.VISIBLE);
        EditText address = (EditText) findViewById(R.id.address);
        address.setVisibility(View.INVISIBLE);
        onManuel = true;

    }

    public void setTripDuration(Double tripDuration) {
        this.tripDuration = tripDuration;
    }
    public String getLat(){
        return lat;
    }
    public String getLon(){
        return lon;
    }
    public String getAddress(){
        return address;
    }
}