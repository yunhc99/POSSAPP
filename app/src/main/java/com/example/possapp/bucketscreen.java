package com.example.possapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class bucketscreen extends AppCompatActivity {
    //어뎁터
    ListView listView;
    myAdapter adapter;

    ArrayList<String> codeTxt = new ArrayList<String>();
    ArrayList<String> nameTxt = new ArrayList<String>();
    ArrayList<String> priceTxt = new ArrayList<String>();

    //제품코
    int requestCode = 1;

    private FirebaseDatabase database;
    private DatabaseReference myRef;

    String barcordCode;
    public String name;
    public String price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shoplist);

        listView = (ListView) findViewById(R.id.listProduct);
        adapter = new myAdapter();
        listView.setAdapter(adapter);
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

                codeTxt.add(barcordCode);
                nameTxt.add(name);
                priceTxt.add(price);
                adapter.notifyDataSetChanged();

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    class myAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return codeTxt.size();
        }

        @Override
        public Object getItem(int position) {
            return codeTxt.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ShopListAdapter view = new ShopListAdapter(getApplicationContext());
            String val = codeTxt.get(position);
            view.setTxtCode(val);
            val = nameTxt.get(position);
            view.setTxtName(val);
            val = priceTxt.get(position);
            view.setTxtPrice(val);

            return view;
        }
    }
}


