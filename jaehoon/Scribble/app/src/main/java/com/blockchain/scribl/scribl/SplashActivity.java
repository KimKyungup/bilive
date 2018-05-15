package com.blockchain.scribl.scribl;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        Log.i("TAG", "display width : " + metrics.widthPixels + ", height : " + metrics.heightPixels + ", densityDpi : " + metrics.densityDpi);

        Intent intent = new Intent(SplashActivity.this, InputPasswordActivity.class);
        startActivity(intent);
    }
}
