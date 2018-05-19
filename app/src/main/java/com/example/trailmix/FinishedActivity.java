package com.example.trailmix;
/**
 * @author Patrick Tan
 * @date 05/18/2018
 * Displays playlist finished message and prompts user to return to the home screen.
 */

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

    /**
     * prompts user to go back to the home screen.
     * @param view
     */
    public void onBack(View view){
        Intent intent3 = new Intent(this,MainActivity.class);
        startActivity(intent3);
    }
}
