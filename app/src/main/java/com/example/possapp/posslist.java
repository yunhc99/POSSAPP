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

public class posslist extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posslist);

        Intent intent = getIntent();  // 넘어온 Intent 객체를 받는다
        int year = intent.getIntExtra("year", 0); // 키, 디폴트값
        int month = intent.getIntExtra("month", 0);  // 키, 디폴트값
        int day = intent.getIntExtra("day", 0);  // 키, 디폴트값
        String strWeekDay = intent.getStringExtra("weekday");


        TextView tv = (TextView) findViewById(R.id.textView2);
        tv.setText("Intent 로 받은 값 : " + year + "년 " + month + "월 " + day + "일 " + strWeekDay);

    }
}

