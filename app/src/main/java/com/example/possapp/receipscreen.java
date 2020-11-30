package com.example.possapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

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

import java.util.ArrayList;

public class receipscreen  extends AppCompatActivity {
    String where,totalprice,marketname,date;
    TextView storeName,buyDay,totalPirce;
    ListView listview;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    FirebaseUser user;

    ArrayList<BucketInfo> bucketInfos=new ArrayList<>();
    myAdapter myAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);
        Intent intent=getIntent();
        where=intent.getStringExtra("where");
        totalprice=intent.getStringExtra("totalprice");
        marketname=intent.getStringExtra("marketname");
        date=intent.getStringExtra("date");

        storeName=findViewById(R.id.storeName);
        buyDay=findViewById(R.id.buyDay);
        totalPirce=findViewById(R.id.totalPirce);
        listview=findViewById(R.id.receiplist);
        database = FirebaseDatabase.getInstance();

        storeName.setText(marketname);
        buyDay.setText(date);
        totalPirce.setText(totalprice);

        myAdapter=new myAdapter(bucketInfos,getApplicationContext());
        listview.setAdapter(myAdapter);
        refresh();


//        ListView receiptlist=findViewById(R.id.receiptlist);
//        database = FirebaseDatabase.getInstance();
//        user= FirebaseAuth.getInstance().getCurrentUser();
//
//        adapter=new receiplistscreen.myAdapter(receiptInfos,getApplicationContext());
//        receiptlist.setAdapter(adapter);
//
//        refresh();

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
            convertView = _inflater.inflate(R.layout.receipt_item, parent, false);
            TextView name=convertView.findViewById(R.id.product_Name);
            TextView price=convertView.findViewById(R.id.product_Price);


            final BucketInfo a=bucketInfos.get(position);

            name.setText(a.getName());
            price.setText(a.getPrice()+"");

            return convertView;
        }
    }
    public void refresh(){
        bucketInfos.clear();
        FirebaseDatabase database3 = FirebaseDatabase.getInstance();
        DatabaseReference myRef3 = database3.getReference("bacordlist/" + where );
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
                        myAdapter.notifyDataSetChanged();

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
