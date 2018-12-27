package com.apps.newsviews.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.apps.newsviews.R;
import com.apps.newsviews.utility.SharedPrefManager;
import com.apps.newsviews.utility.SplashPref2Manager;

public class SplashScreen2Activity extends AppCompatActivity {

    private Button button;
    private boolean isLoggedIn;
    private SplashPref2Manager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen2);

        isLoggedIn = SharedPrefManager.getInstance(SplashScreen2Activity.this).getSharedPrefIsLoggedIn();
        prefManager = new SplashPref2Manager(this);

        if (!isLoggedIn) {
            startActivity(new Intent(SplashScreen2Activity.this, LoginActivity.class));
            finish();
        } else if (!prefManager.isFirstTimeLaunch()) {
            startActivity(new Intent(SplashScreen2Activity.this, LoginActivity.class));
            finish();
        }

        button = (Button) findViewById(R.id.start_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prefManager.setFirstTimeLaunch(false);
                startActivity(new Intent(SplashScreen2Activity.this, LoginActivity.class));
                finish();
            }
        });
    }
}
