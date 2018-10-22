package com.example.writeyourquote.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

import com.example.writeyourquote.util.CommonSharedPreference;
import com.example.writeyourquote.R;

public class SplashActivity extends Activity {
    private CommonSharedPreference commonSharedPreference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        commonSharedPreference = CommonSharedPreference.getInstance(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                Intent nextIntent;
                if (commonSharedPreference.containsKey("LoggedIn") && commonSharedPreference.getBoolean("LoggedIn")) {
                    nextIntent = new Intent(SplashActivity.this, ListQuotesActivity.class);
                } else {
                    nextIntent = new Intent(SplashActivity.this, LoginActivity.class);
                }
                startActivity(nextIntent);
                finish();
            }
        }, getResources().getInteger(R.integer.splash_time));
    }
}
