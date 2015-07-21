package com.example.lizheng.querypvapp.Test;

import java.util.ArrayList;

/**
 * Created by Lizheng on 2015/7/3.
 */
public class DateQuery {
    private String query;
    private int pv;
    private int uv;
    private int id;
    private ArrayList<String[]> rates;

    public DateQuery() {
    }

    public DateQuery(String query, int pv, int uv, int id, ArrayList<String[]> list) {
        this.query = query;
        this.pv = pv;
        this.uv = uv;
        this.id = id;
        this.rates = list;
    }

    public String toString(){
        String ratestr = "";
        for (int i = 0; i< rates.size(); i ++){
            ratestr += " ,["+rates.get(i)[0]+", "+rates.get(i)[1]+"]";
        }
        return "[ "+query+", " +pv +", "+uv +", "+ id +","+ratestr+" ]";
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public ArrayList<String[]> getRates() {
        return rates;
    }

    public void setRates(ArrayList<String[]> rates) {
        this.rates = rates;
    }

    public int getPv() {
        return pv;
    }

    public void setPv(int pv) {
        this.pv = pv;
    }

    public int getUv() {
        return uv;
    }

    public void setUv(int uv) {
        this.uv = uv;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


}
