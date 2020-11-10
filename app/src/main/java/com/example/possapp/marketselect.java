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

import org.w3c.dom.Text;

import java.util.ArrayList;

public class marketselect extends AppCompatActivity {
    ListView listProduct;
    ArrayList<String> arraymarket=new ArrayList<>();
    FirebaseDatabase database;
    DatabaseReference myRef;
    ListAdapter listAdapte;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
                System.out.println("값 출력"+name);
                arraymarket.add(name);
                System.out.println("크기="+arraymarket.size());
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
            System.out.println("포지션"+position);
            convertView = _inflater.inflate(R.layout.market_item, parent, false);
            TextView marketlabel=convertView.findViewById(R.id.labelll);
            marketlabel.setText(arraymarket.get(position));
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context,bucketscreen.class);
                    startActivity(intent);
                }
            });
            return convertView;
        }
    }

}
