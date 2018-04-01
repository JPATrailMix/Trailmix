package com.example.trailmix;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time);
        Bundle bundle = getIntent().getExtras();
        Double time = bundle.getDouble("time");
        int millis = (int)(time*60*1000);
        CountDownTimer countDown = new CountDownTimer(millis, 1000) {
            boolean finish = false;
            TextView tv = (TextView) findViewById( R.id.time);
            public void onTick(long millisUntilFinished) {
                tv.setText("seconds remaining: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                tv.setText("done!");
            }
        }.start();
    Intent intent2 = new Intent(this,FinishedActivity.class);
    intent2.putExtra("finish",true);
    startActivity(intent2);

}
    public static void showTime(View view){

    }
}
