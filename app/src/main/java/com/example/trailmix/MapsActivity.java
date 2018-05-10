package com.example.trailmix;

/*
Author: Patrick Tan
This activity is mainly focused on the google maps API.
 */
//import android.Manifest;
//import android.content.pm.PackageManager;
//import android.support.v4.app.ActivityCompat;
//import android.support.v4.app.FragmentActivity;
//import android.os.Bundle;
//import android.support.v4.content.ContextCompat;
//
//import com.google.android.gms.maps.CameraUpdateFactory;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.OnMapReadyCallback;
//import com.google.android.gms.maps.SupportMapFragment;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.MarkerOptions;

import android.*;
import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

/**
 * Created by User on 10/2/2017.
 */
//refrences include from the google maps api, https://www.youtube.com/watch?v=iTBnuCYeq3E, and Tech Academy on YouTube.
public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = "MapActivity";

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;
    private LocationRequest nLocationRequest;

    //vars
    private Boolean mLocationPermissionsGranted = false;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        getLocationPermission();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(this, "Map is Ready", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onMapReady: map is ready");
        mMap = googleMap;
        boolean checkPermission = checkLocationPermission();
        if (!checkPermission) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }
        mMap.setMyLocationEnabled(true);
        //mMap.getUiSettings().setMyLocationButtonEnabled(false);

       /* if (mLocationPermissionsGranted) {
            getDeviceLocation();

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }


        }*/
        LatLng WPI = new LatLng(42.2746, -71.8063);
        mMap.addMarker(new MarkerOptions().position(WPI).title("WPI"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(WPI));
    }
    public boolean checkLocationPermission(){
        String permission = "android.permission.ACCESS_FINE_LOCATION";
        int res = this.checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }
    // Calculates distance and time between two places

//    public void distanceMatrix(String origin, String destination) {
//
//        Log.d("mapActivity", "Entered distanceMatrix() method");
//
//        // Defines the API key to use
//
//        String API_KEY = "AIzaSyASVU_Ws92GaMXBNhtREbNYXR3WBsbqDP0";
//
//        //Provides context for the matrix
//
//        GeoApiContext context = new GeoApiContext().setApiKey(API_KEY);
//
//
//
//        // If internet permission is not granted, ask for it
//
//        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
//
//            Toast.makeText(getBaseContext(), "This app needs internet permission to work", Toast.LENGTH_SHORT).show();
//
//            ActivityCompat.requestPermissions((this), new String[]{Manifest.permission.INTERNET}, 1);
//
//        }
//
//        try {
//
//            DistanceMatrixApiRequest req = DistanceMatrixApi.newRequest(context);
//
//            //Takes the approved request and turns it into an actual distance matrix using the users current lat and long,
//
//            // and the recipient's address
//
//            //Matrix takes user's origin as a lat and long value, while it takes the recipients location as a street address
//
//            Log.d("mapActivity", "Called request");
//
//            DistanceMatrix distanceMatrix = req.origins(origin).destinations(destination).await();
//
//            //Takes the duration given by distance matrix and writes it to the global variable finalEstimatedTime
//
//            Log.d("mapActivity", "Distance Matrix Created");
//
//            finalEstimatedTime = (distanceMatrix.rows[0].elements[0].duration.humanReadable);
//
//            Log.d("mapActivity", "distanceMatrix functions as desired. Final time: " + finalEstimatedTime);
//
//        } catch (Exception e) {
//
//            Log.d("mapActivity", "Catching things if they fail!");
//
//            e.printStackTrace();
//
//        }
//
//    }


    private void getDeviceLocation(){
        Log.d(TAG, "getDeviceLocation: getting the devices current location");

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try{
            if(mLocationPermissionsGranted){

                final Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "onComplete: found location!");
                            Location currentLocation = (Location) task.getResult();

                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),
                                    DEFAULT_ZOOM);

                        }else{
                            Log.d(TAG, "onComplete: current location is null");
                            Toast.makeText(MapsActivity.this, "unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }catch (SecurityException e){
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage() );
        }
    }

    private void moveCamera(LatLng latLng, float zoom){
        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude );
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

    private void initMap(){
        Log.d(TAG, "initMap: initializing map");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(MapsActivity.this);
    }

    private void getLocationPermission(){
        Log.d(TAG, "getLocationPermission: getting location permissions");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                mLocationPermissionsGranted = true;
                initMap();
            }else{
                ActivityCompat.requestPermissions(this,
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        }else{
            ActivityCompat.requestPermissions(this,
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: called.");
        mLocationPermissionsGranted = false;

        switch(requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE:{
                if(grantResults.length > 0){
                    for(int i = 0; i < grantResults.length; i++){
                        if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                            mLocationPermissionsGranted = false;
                            Log.d(TAG, "onRequestPermissionsResult: permission failed");
                            return;
                        }
                    }
                    Log.d(TAG, "onRequestPermissionsResult: permission granted");
                    mLocationPermissionsGranted = true;
                    //initialize our map
                    initMap();
                }
            }
        }
    }
   /* private class MyThread implements Runnable{

        @Override
        public void run() {
            getDeviceLocation();
        }
    }*/
   /* // Calculates distance and time between two places
    public void distanceMatrix(String origin, String destination) {
        Log.d("mapActivity", "Entered distanceMatrix() method");
        // Defines the API key to use
        String API_KEY = "ADD API KEY HERE";
        //Provides context for the matrix
        GeoApiContext context = new GeoApiContext().setApiKey(API_KEY);

        // If internet permission is not granted, ask for it
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getBaseContext(), "This app needs internet permission to work", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions((this), new String[]{Manifest.permission.INTERNET}, 1);
        }
        try {
            DistanceMatrixApiRequest req = DistanceMatrixApi.newRequest(context);
            //Takes the approved request and turns it into an actual distance matrix using the users current lat and long,
            // and the recipient's address
            //Matrix takes user's origin as a lat and long value, while it takes the recipients location as a street address
            Log.d("mapActivity", "Called request");
            DistanceMatrix distanceMatrix = req.origins(origin).destinations(destination).await();
            //Takes the duration given by distance matrix and writes it to the global variable finalEstimatedTime
            Log.d("mapActivity", "Distance Matrix Created");
            finalEstimatedTime = (distanceMatrix.rows[0].elements[0].duration.humanReadable);
            Log.d("mapActivity", "distanceMatrix functions as desired. Final time: " + finalEstimatedTime);
        } catch (Exception e) {
            Log.d("mapActivity", "Catching things if they fail!");
            e.printStackTrace();
        }
    }

*/

}
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        mMap = googleMap;
//
//        // Add a marker in Sydney and move the camera
//        LatLng WPI = new LatLng(42.2746, -71.8063);
//        mMap.addMarker(new MarkerOptions().position(WPI).title("WPI"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(WPI));
//    }

