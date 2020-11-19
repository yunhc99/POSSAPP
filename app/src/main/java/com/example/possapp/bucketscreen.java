package com.example.possapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class bucketscreen extends AppCompatActivity {
    //코드를 저장하기 위한 String
    String barcordCode;
    int requestCode = 1;

    private FirebaseDatabase database;
    private DatabaseReference myRef;

    public String name;
    public String price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shoplist);



    }

    public void doit(View view) {
        switch (view.getId()){
            case R.id.cord:
                Intent intent=new Intent(this,Barcord.class);
                startActivityForResult(intent, requestCode);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == this.requestCode) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Barcode Code : " + data.getData().toString(), Toast.LENGTH_SHORT).show();
                barcordCode = data.getData().toString();

                //
                database = FirebaseDatabase.getInstance();
                myRef = database.getReference("product/" + barcordCode + "/name");
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Object value = snapshot.getValue(Object.class);
                        Log.d("bucket", "test:" + value.toString());
                        name = (String)value.toString();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                myRef = database.getReference("product/" + barcordCode + "/price");
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Object value = snapshot.getValue(Object.class);
                        Log.d("bucket", "test:" + value.toString());
                        price = (String)value.toString();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                //추후 수정예정 ------------- 이름, 바코드, 가격 부름

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}


