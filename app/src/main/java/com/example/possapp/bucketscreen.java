package com.example.possapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class bucketscreen extends AppCompatActivity {
    //어뎁터
    ListView listView;
    myAdapter adapter;
    ArrayList<BucketInfo> bucketInfos=new ArrayList<>();

    TextView totalprice;

    //제품코
    int requestCode = 1;

    private FirebaseDatabase database;
    private DatabaseReference myRef;

    String barcordCode;
    FirebaseUser user;
    String key;
    String marketname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("실행됨");
        user= FirebaseAuth.getInstance().getCurrentUser();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shoplist);
        Intent intent2=getIntent();
        marketname=intent2.getStringExtra("marketname");
        key=intent2.getStringExtra("key");

        listView = (ListView) findViewById(R.id.listProduct);
        adapter = new myAdapter(bucketInfos,getApplicationContext());
        listView.setAdapter(adapter);
        totalprice = findViewById(R.id.totalprice);

        refresh();
        adapter.notifyDataSetChanged();


    }

    public void doit(View view) {
        switch (view.getId()) {
            case R.id.cord:
                Intent intent = new Intent(this, Barcord.class);
                startActivityForResult(intent, requestCode);
                break;
            case R.id.pay:
                progress progress = new progress(this);
                database = FirebaseDatabase.getInstance();
                counttotal();
                ReceiptInfo receiptInfo=new ReceiptInfo();
                receiptInfo.setKey(key);
                receiptInfo.setMarketname(marketname);
                receiptInfo.setTotalprice(counttotal());
                SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                receiptInfo.setDate(sdf.format(new Date()));
                myRef=database.getReference("receip/").child(user.getUid());
                myRef.push().setValue(receiptInfo);
                database.getReference("personalbucket/").child(user.getUid()).removeValue();
                finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == this.requestCode) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Barcode Code : " + data.getData().toString(), Toast.LENGTH_SHORT).show();
                barcordCode = data.getData().toString();
                database = FirebaseDatabase.getInstance();
                myRef = database.getReference("product2/" + barcordCode );
                myRef.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                        BucketInfo bucketInfo = snapshot.getValue(BucketInfo.class);
                        bucketInfo.setBarcode(barcordCode);
                        if(bucketInfo.getName()!=null){
                            myRef=database.getReference("bacordlist/").child(key);
                            myRef.push().setValue(barcordCode);
                        }
                        else{}//바코드가 없을때
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    class myAdapter extends BaseAdapter {
        ArrayList<BucketInfo> bucketInfos;
        Context context;
        LayoutInflater _inflater;

        public myAdapter(ArrayList<BucketInfo> bucketInfos, Context context){
            this.bucketInfos=bucketInfos;
            this.context=context;
            _inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }

        @Override
        public int getCount() {
            return bucketInfos.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            convertView = _inflater.inflate(R.layout.shoplist_item, parent, false);
            TextView name=convertView.findViewById(R.id.txtName);
            TextView price=convertView.findViewById(R.id.txtPrice);
            TextView code=convertView.findViewById(R.id.txtCode);
            ImageView btndel=convertView.findViewById(R.id.btnDelete);

            final BucketInfo a=bucketInfos.get(position);

            name.setText(a.getName());
            price.setText(a.getPrice()+"");
            code.setText(a.getBarcode());
            btndel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    database = FirebaseDatabase.getInstance();
                    System.out.println("a.key="+a.getKey());
                    database.getReference("bacordlist/").child(key).child(a.getKey()).removeValue();
                    refresh();
                }
            });


            return convertView;
        }
    }

    public int counttotal() {
        int inttotalprice=0;
        for (int i = 0; i < bucketInfos.size(); i++) {

            int price = bucketInfos.get(i).getPrice();
            inttotalprice += price;
        }
        return inttotalprice;

    }

    public void refresh(){
        bucketInfos.clear();
        FirebaseDatabase database3 = FirebaseDatabase.getInstance();
        DatabaseReference myRef3 = database3.getReference("bacordlist/" + key );
        myRef3.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                final String name=(String)snapshot.getValue();
                final String itemkey=snapshot.getKey();
                FirebaseDatabase database2 = FirebaseDatabase.getInstance();
                DatabaseReference myRef2 = database2.getReference("product2/" + name );
                myRef2.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        BucketInfo bucketInfo = snapshot.getValue(BucketInfo.class);
                        bucketInfo.setBarcode(name);
                        bucketInfo.setKey(itemkey);
                        bucketInfos.add(bucketInfo);
                        adapter.notifyDataSetChanged();
                        totalprice.setText(counttotal() + "원");
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}


