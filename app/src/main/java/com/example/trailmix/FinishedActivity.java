package com.example.trailmix;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class FinishedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finished);
    }
    public void onYes(View view){
        Intent intent3 = new Intent(this,MainActivity.class);
        startActivity(intent3);
    }
    public static void onNo(View view){

    }
}
