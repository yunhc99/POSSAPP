package com.example.possapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;

public class bucketscreen extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shoplist);
    }

    public void doit(View view) {
        switch (view.getId()){
            case R.id.cord:
                Intent intent=new Intent(this,Barcord.class);
                startActivity(intent);
                break;
        }
    }
}
