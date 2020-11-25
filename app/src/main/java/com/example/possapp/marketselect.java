package com.example.possapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
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
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class marketselect extends AppCompatActivity {
    ListView listProduct;
    ArrayList<String> arraymarket=new ArrayList<>();
    FirebaseDatabase database;
    DatabaseReference myRef;
    ListAdapter listAdapte;
    Marketinfo marketinfo=new Marketinfo();
    FirebaseUser user;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        context=getApplicationContext();
        database = FirebaseDatabase.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference myRef2 = database.getReference("personalbucket/"+user.getUid());//~에있는값 가져오기
        myRef2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshots : snapshot.getChildren()) {
                    Marketinfo marketinfo = snapshots.getValue(Marketinfo.class);
                    System.out.println(marketinfo.getName());
                    if(marketinfo!=null){
                        System.out.println("자동 intent");
                        Intent intent=new Intent(context,bucketscreen.class);
                        intent.putExtra("marketname",marketinfo.getName());
                        intent.putExtra("key",marketinfo.getWhere());
                        startActivity(intent);
                        finish();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marketselect);
        listProduct=findViewById(R.id.listProduct);
        listAdapte=new ListAdapter(arraymarket,this);
        listProduct.setAdapter(listAdapte);

        refresh();

    }

    public void refresh(){
        arraymarket.clear();
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();//아이디 가져오기
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("market/");//~에있는값 가져오기
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String name=(String)snapshot.getValue();
                arraymarket.add(name);
                listAdapte.notifyDataSetChanged();
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
    public class ListAdapter extends BaseAdapter {
        ArrayList<String> arraymarket;
        Context context;
        LayoutInflater _inflater;

        public ListAdapter(ArrayList<String> arraymarket, Context context){
            this.arraymarket=arraymarket;
            this.context=context;
            _inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }
        @Override
        public int getCount() {
            return arraymarket.size();
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
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = _inflater.inflate(R.layout.market_item, parent, false);
            TextView marketlabel=convertView.findViewById(R.id.labelll);
            marketlabel.setText(arraymarket.get(position));
            final int a=position;
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("버튼 intent");
                    Intent intent=new Intent(context,bucketscreen.class);

                    myRef=database.getReference("bacordlist").push();
                    String key=myRef.getKey();
                    marketinfo=new Marketinfo();
                    marketinfo.setName(arraymarket.get(a));
                    marketinfo.setWhere(key);
                    myRef=database.getReference("personalbucket").child(user.getUid()).push();
                    myRef.setValue(marketinfo);

                    intent.putExtra("marketname",arraymarket.get(a));
                    intent.putExtra("key",key);

                    startActivity(intent);
                    finish();
                }
            });
            return convertView;
        }
    }

}
