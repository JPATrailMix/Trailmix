package com.example.trailmix;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //what the fiddle stick
        //Log.d("MainActivity", r1.toString());


    }
    public void getDuration(View v){
        EditText time = findViewById(R.id.editText);
        String timeStr = time.getText().toString();

        if(timeStr.isEmpty())
            timeStr = "0";

        Log.d("MainActivity", timeStr);
        Double num1 = Double.parseDouble(timeStr);

        Intent intent = new Intent(this,TimeActivity.class);
        intent.putExtra("time", time.toString());
        startActivity(intent);
    }

}
