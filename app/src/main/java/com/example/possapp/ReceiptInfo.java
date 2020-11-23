package com.example.possapp;

public class ReceiptInfo {
    String key;
    int totalprice;
    String date;
    String marketname;
    String where;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getTotalprice() {
        return totalprice;
    }

    public void setTotalprice(int totalprice) {
        this.totalprice = totalprice;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMarketname() {
        return marketname;
    }

    public void setMarketname(String marketname) {
        this.marketname = marketname;
    }

    public String getWhere() {
        return where;
    }

    public void setWhere(String where) {
        this.where = where;
    }
}
