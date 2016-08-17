package com.ly.disney;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

/**
 * Created by liuyan on 2016/8/15.
 */
public class AppStart extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appstart);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(AppStart.this,RegistActivity.class);
                startActivity(intent);
                AppStart.this.finish();
            }
        },2000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
