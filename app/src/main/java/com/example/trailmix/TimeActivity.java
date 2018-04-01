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

        new CountDownTimer(3000000, 1000) {
            TextView tv = (TextView) findViewById( R.id.time);
            public void onTick(long millisUntilFinished) {
                tv.setText("seconds remaining: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                tv.setText("done!");
            }
        }.start();
    Intent intent2 = new Intent(this,FinishedActivity.class);
    startActivity(intent2);

}
    public static void showTime(View view){

    }
}
