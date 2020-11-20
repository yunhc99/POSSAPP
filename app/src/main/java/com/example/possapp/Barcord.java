package com.example.possapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class Barcord extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcord);
        IntentIntegrator intentIntegrator=new IntentIntegrator(Barcord.this);
        intentIntegrator.setOrientationLocked(false);
        intentIntegrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        Intent code = new Intent();

        //result is not null
        if (result != null) {
            if (result.getContents() != null) {
                //값이 정상적으로 읽었을 경우
                Toast.makeText(this, "Barcode Code : " + result.getContents(), Toast.LENGTH_SHORT).show();
                //bucketscreen 쪽으로 코드값을 이동하기 위한 코드
                code.setData(Uri.parse(result.getContents()));
                setResult(RESULT_OK, code);
            } else {
                //값을 널값인 경우
                Toast.makeText(this, "No data find", Toast.LENGTH_SHORT).show();
                //Test
                    code.setData(Uri.parse("88002798"));
                    setResult(RESULT_OK, code);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
        finish();
    }

}