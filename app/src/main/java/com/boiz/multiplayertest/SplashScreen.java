package com.boiz.multiplayertest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Thread screen = new Thread()
        {
            public void run()
            {
                try {
                    sleep(3*1000);
                    startActivity(new Intent(SplashScreen.this, GetName.class));
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        screen.start();
    }
}
