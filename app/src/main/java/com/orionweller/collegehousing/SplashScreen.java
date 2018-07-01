package com.orionweller.collegehousing;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

/**
 * Created by Orion on 3/7/2015.
 */
public class SplashScreen extends Activity {
    //This class corresponds to the activity_spash XML file
    //This is just the loading up screen that says my apps name and who created it


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen_layout);
        int SPASH_TIME_OUT = 1500;

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                Intent i = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        }, SPASH_TIME_OUT);

    }
}

