package com.blockchain.scribl.scribl;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class InputPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_password);
        Intent intent = new Intent(InputPasswordActivity.this, InputFingerprintActivity.class);
        startActivity(intent);
    }
}
