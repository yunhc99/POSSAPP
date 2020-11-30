package com.example.possapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
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
import java.util.HashMap;

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


    FirebaseUser user;
    String key;
    String marketname;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context=getApplicationContext();
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
                Toast.makeText(context,receiptInfo.getTotalprice()+"원이 결제되었습니다",Toast.LENGTH_LONG).show();
                break;
            case R.id.back_Button:
                database = FirebaseDatabase.getInstance();
                database.getReference("personalbucket/").child(user.getUid()).removeValue();
                database.getReference("bacordlist/").child(key).removeValue();
                finish();
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == this.requestCode) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Barcode Code : " + data.getData().toString(), Toast.LENGTH_SHORT).show();
                final String barcordCode = data.getData().toString();
                database = FirebaseDatabase.getInstance();
                myRef = database.getReference("product2/" + barcordCode );
                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(!snapshot.exists()){
                            LayoutInflater inflater=(LayoutInflater)context.getSystemService(LAYOUT_INFLATER_SERVICE);
                            View item=inflater.inflate(R.layout.newbacordscreen,null);
                            AlertDialog.Builder alert=new AlertDialog.Builder(bucketscreen.this);
                            alert.setTitle("상품 추가하기");
                            alert.setView(item);
                            final EditText bacord=item.findViewById(R.id.barcord);
                            final EditText name=item.findViewById(R.id.name);
                            final EditText price=item.findViewById(R.id.price);
                            bacord.setText(barcordCode);

                            alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String proname=name.getText().toString();
                                    String proprice=price.getText().toString();
                                    if(proname.equals("")||proprice.equals("")){
                                        Toast.makeText(bucketscreen.this,"이름과 가격을 적어주세요",Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        BucketInfo bucketInfo = new BucketInfo();
                                        bucketInfo.setBarcode(barcordCode);
                                        bucketInfo.setName(name.getText().toString());
                                        bucketInfo.setPrice(Integer.parseInt(price.getText().toString()));
                                        myRef=database.getReference("product2/").child(barcordCode);
                                        myRef.push().setValue(bucketInfo);
                                        myRef=database.getReference("bacordlist/").child(key);
                                        myRef.push().setValue(barcordCode);
                                        refresh();
                                    }
                                }
                            });
                            alert.setNegativeButton("취소",null);
                            AlertDialog b=alert.create();
                            b.show();
                        }
                        else{
                            myRef=database.getReference("bacordlist/").child(key);
                            myRef.push().setValue(barcordCode);
                            refresh();
                        }
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

        FirebaseDatabase database3 = FirebaseDatabase.getInstance();
        DatabaseReference myRef3 = database3.getReference("bacordlist/" + key );
        myRef3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists()){
                    bucketInfos.clear();
                    totalprice.setText(0 + "원");
                    adapter.notifyDataSetChanged();
                }
                else {
                    bucketInfos.clear();
                    final HashMap<String,String> map=(HashMap) snapshot.getValue();
                    for(final String aa:map.keySet()){
                        FirebaseDatabase database2 = FirebaseDatabase.getInstance();
                        DatabaseReference myRef2 = database2.getReference("product2/" + map.get(aa));

                        myRef2.addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                BucketInfo bucketInfo = snapshot.getValue(BucketInfo.class);
                                bucketInfo.setBarcode(map.get(aa));
                                bucketInfo.setKey(aa);
                                bucketInfos.add(bucketInfo);

                                totalprice.setText(counttotal() + "원");
                                adapter.notifyDataSetChanged();
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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

}


