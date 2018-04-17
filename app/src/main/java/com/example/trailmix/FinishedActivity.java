package com.example.trailmix;
/*Author: Patrick Tan*/
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
    //prompts user to go back to the home screen.
    public void onBack(View view){
        Intent intent3 = new Intent(this,MainActivity.class);
        startActivity(intent3);
    }
}
