package com.example.lizheng.querypvapp;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;

import com.example.lizheng.querypvapp.Test.DateQuery;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by Lizheng on 2015/7/3.
 */
public class HttpHelper {

    private  Context mContext;

    public HttpHelper(Context context){
        this.mContext = context;
    }

    public ArrayList<DayQuery> getDatas(String day){
        String[] idBags = {"+1","--","-3","-1","+2","-13","+22","上榜"};
        float[] pvBags = {0.88f,0.55f,1.90f,1.40f,1.20f,1.11f,1.09f};

        ArrayList<DayQuery> lists = new ArrayList<>();
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(mContext.getAssets().open("topquery20150621.txt"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line ;
            int index = 0;
            while ( (line = reader.readLine()) != null ){
                DayQuery  dayQuery = new DayQuery();
                dayQuery.setQuery(line.split("\t")[0]);
                String[] ids = new String[7];
                for (int i = 0;i<7;i++){
                    ids[i] = idBags[((int) (Math.random() * 8))];
                }
                dayQuery.setIdDlts(ids);
                float[] pvs = new float[7];
                for (int i = 0;i<7;i++){
                    pvs[i] = pvBags[((int) (Math.random() * 7))];
                }
                dayQuery.setPvRates(pvs);
                dayQuery.setPv("233507");
                dayQuery.setUv("233507");
                lists.add(dayQuery);
                index++;
                if (index>100){
                    break;
                }
            }
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return  lists;
    }


    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static  ArrayList<DateQuery> GetHttp(String day,int page) {

        ArrayList<DateQuery> list = new ArrayList<DateQuery>();

//        String urlString = "http://10.99.16.90:8001/?date="+day+ "&page="+page;
//        String urlString = "http://42.120.173.40:8080/";
        String urlString = "http://42.120.173.40:8080/?date="+day+ "&page="+page;
        //代理
        String host = "42.120.173.9";
        int port = 9999;
        try {
            URL url = new URL(urlString);
            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(host, port));    //设置代理
            HttpURLConnection connection = (HttpURLConnection) url.openConnection(proxy);
//            connection.setRequestProperty("Accept-Charset", "utf-8");
//            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//            connection.setDoInput(true);
//            connection.setRequestMethod("GET");
//            connection.connect();

            InputStreamReader in = new InputStreamReader(connection.getInputStream(), "utf-8");
            BufferedReader bufferedReader = new BufferedReader(in);

            DecimalFormat fnum  =   new  DecimalFormat("##0.00");
            String line  ;
            while ( (line = bufferedReader.readLine())!=null) {
                JSONArray jsonArray = new JSONArray(line);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONArray queryArray = jsonArray.getJSONArray(i);
                    ArrayList<String[]> rates = new ArrayList<>();
                    for (int j = 0; j<7; j++){
                        String[] dayRates = new String[2];
                        if(4+j < queryArray.length()) {
                            JSONArray array = (JSONArray) queryArray.get(4 + j);
                            if (array.get(0) == JSONObject.NULL){
                                dayRates[0] = "--";
                                dayRates[1] = "--";
                            }else {
                                dayRates[0] = fnum.format( array.getDouble(0) );
                                dayRates[1] = array.getString(1);
                            }
                        }else {
                            dayRates[0] = "--";
                            dayRates[1] = "--";
                        }
                        rates.add(dayRates);
                    }
                    DateQuery dateQuery = new DateQuery(queryArray.getString(0),queryArray.getInt(1),queryArray.getInt(2),queryArray.getInt(3),rates);
//                    System.out.println(dateQuery.toString());
                    list.add(dateQuery);
                }
            }
            bufferedReader.close();
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }
}
