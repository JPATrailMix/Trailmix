package com.example.trailmix;
/*
* @author Patrick Tan
* This Activity should be used in future extensions.
* If need of template for specific output.
*/
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class LocationActivity extends AppCompatActivity {
private Double tripDuration;
private String lat;
private String lon;
private String address;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        Bundle bundle = getIntent().getExtras();
        lat = ""+bundle.getDouble("latitude");
        lon = ""+bundle.getDouble("longitude");
    }
    public void getDuration(View v){
        EditText num = findViewById(R.id.num);
        String numStr = num.getText().toString();
        EditText street = findViewById(R.id.streetN);
        String streetStr = num.getText().toString();
        EditText state = findViewById(R.id.stateN);
        String stateStr = num.getText().toString();
        address = numStr + " " + streetStr + " " + stateStr;
        MainActivity mainActivity = new MainActivity();
        mainActivity.setAddress(address);
        AddressParcer ap = new AddressParcer(mainActivity);
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
    public void setTripDuration(Double tripDuration) {
        this.tripDuration = tripDuration;
    }
    public String getLat(){
        return lat;
    }
    public String getLon(){
        return lon;
    }
    public String address(){return address;}

}
