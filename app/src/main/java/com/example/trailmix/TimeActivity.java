package com.example.trailmix;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class TimeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time);
        Bundle bundle = getIntent().getExtras();

        Double time = bundle.getDouble("time");
        TextView timeView = findViewById(R.id.timeView);
        while(time > 0){
            timeView.setText(time.toString());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            time--;
        }
        Intent intent2 = new Intent(this,FinishedActivity.class);
        startActivity(intent2);

    }
    public static void showTime(View view){

    }
}
