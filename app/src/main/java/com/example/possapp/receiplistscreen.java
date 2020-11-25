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

public class receiplistscreen extends AppCompatActivity {
    ArrayList<ReceiptInfo> receiptInfos=new ArrayList<>();
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    FirebaseUser user;
    myAdapter adapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt_list);
        ListView receiptlist=findViewById(R.id.receiptlist);
        database = FirebaseDatabase.getInstance();
        user= FirebaseAuth.getInstance().getCurrentUser();

        adapter=new myAdapter(receiptInfos,getApplicationContext());
        receiptlist.setAdapter(adapter);

        refresh();

    }
    class myAdapter extends BaseAdapter {
        ArrayList<ReceiptInfo> receiptInfos;
        Context context;
        LayoutInflater _inflater;

        public myAdapter(ArrayList<ReceiptInfo> receiptInfos, Context context){
            this.receiptInfos=receiptInfos;
            this.context=context;
            _inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }

        @Override
        public int getCount() {
            return receiptInfos.size();
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
            convertView = _inflater.inflate(R.layout.receipt_list_item, parent, false);
            TextView market_name=convertView.findViewById(R.id.market_name);
            TextView days=convertView.findViewById(R.id.days);
            TextView times=convertView.findViewById(R.id.times);
            TextView price=convertView.findViewById(R.id.total_p);

            final ReceiptInfo a=receiptInfos.get(position);

            final String date[]=a.getDate().split(" ");

            market_name.setText(a.getMarketname());
            price.setText(a.getTotalprice()+"");
            days.setText(date[0]);
            times.setText(date[1]);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context,receipscreen.class);
                    System.out.println("보내는값="+a.getKey());
                    intent.putExtra("where",a.getKey());
                    intent.putExtra("totalprice",a.getTotalprice()+"");
                    intent.putExtra("marketname",a.getMarketname());
                    intent.putExtra("date",a.getDate());
                    startActivity(intent);
                }
            });
            return convertView;
        }
    }
    public void refresh(){
        receiptInfos.clear();
        FirebaseDatabase database3 = FirebaseDatabase.getInstance();
        DatabaseReference myRef3 = database3.getReference("receip/" + user.getUid() );
        myRef3.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                ReceiptInfo receiptInfo=(ReceiptInfo)snapshot.getValue(ReceiptInfo.class);
                receiptInfos.add(receiptInfo);
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
