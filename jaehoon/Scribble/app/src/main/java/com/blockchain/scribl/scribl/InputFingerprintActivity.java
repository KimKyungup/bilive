package com.blockchain.scribl.scribl;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class InputFingerprintActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_fingerprint);
        Intent intent = new Intent(InputFingerprintActivity.this, IntroduceActivity.class);
        startActivity(intent);
    }
}
