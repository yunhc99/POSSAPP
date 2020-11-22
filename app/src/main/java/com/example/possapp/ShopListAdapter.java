package com.example.possapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ShopListAdapter extends LinearLayout {
    TextView txtCode;
    TextView txtName;
    TextView txtPrice;

    public ShopListAdapter(Context context) {
        super(context);
        inflationInit(context);

        txtCode = (TextView) findViewById(R.id.txtCode);
        txtName = (TextView) findViewById(R.id.txtName);
        txtPrice = (TextView) findViewById(R.id.txtPrice);

    }

    private void inflationInit(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.shoplist_item, this, true);
    }

    public void setTxtCode (String code) {
        txtCode.setText("제품코드 : " + code);
    }

    public void setTxtName (String name) {
        txtName.setText("제품이름 : " + name);
    }

    public void setTxtPrice (String price) {
        txtPrice.setText("제품가격 : " + price);
    }
}
