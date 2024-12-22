package com.karzansoft.fastvmi.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.karzansoft.fastvmi.R;
import com.karzansoft.fastvmi.Utils.AppUtils;

/**
 * Created by Yasir on 10/10/2016.
 */
public class Splash extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 100;
    boolean isCanceled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        isCanceled=false;
        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if(isCanceled)
                    return;
                Intent i;
                if(AppUtils.getAuthToken(Splash.this).length()>0)
                   i=new Intent(Splash.this,FragmentMainActivity.class);
                else
                    i=new Intent(Splash.this,LoginActivity.class);

                startActivity(i);
                finish();
            }
        },SPLASH_TIME_OUT);


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        isCanceled=true;
    }
}
