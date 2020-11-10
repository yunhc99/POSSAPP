package com.example.possapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.zxing.integration.android.IntentIntegrator;

public class Barcord extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcord);
        IntentIntegrator intentIntegrator=new IntentIntegrator(Barcord.this);
        intentIntegrator.setOrientationLocked(false);
        intentIntegrator.initiateScan();
    }
}