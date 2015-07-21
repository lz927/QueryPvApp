package com.example.lizheng.querypvapp.FlowTags;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.lizheng.querypvapp.Config.QueryApp;
import com.example.lizheng.querypvapp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by Lizheng on 2015/7/6.
 */
public class BlackListActivity extends Activity {

    private EditText searchEt;
    private Button searchBtn;
    private ImageView backBtn;
    private ListView blacklistview;
    private SimpleAdapter blackAdapter;
//    private ArrayList<String> blacklist;
    private ArrayList<HashMap<String,Object>> blacklist;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blacklist);
        searchEt = (EditText) findViewById(R.id.search_et);
        searchBtn = (Button) findViewById(R.id.search_btn);
        backBtn = (ImageView) findViewById(R.id.back_iv);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        blacklistview = (ListView) findViewById(R.id.black_listview);
        initListView();
    }

    private void initListView() {
        HashSet<String> list = new HashSet<>();
        SharedPreferences sharedPreferences = getSharedPreferences(QueryApp.BLACK_NAME, 0);
//        blacklist = new ArrayList<String>();
//        blacklist.addAll(sharedPreferences.getStringSet("black", list));
//        blackAdapter = new ArrayAdapter<String>(this,R.layout.abc_simple_dropdown_hint,blacklist);
        list = (HashSet<String>) sharedPreferences.getStringSet("black", new HashSet<String>());
        blacklist = new ArrayList<HashMap<String, Object>>();
        int index = 1;
        for (String str : list){
            HashMap<String, Object> map = new HashMap<>();
            map.put("id",index++);
            map.put("query",str);
            blacklist.add(map);
        }
        blackAdapter = new SimpleAdapter(this,blacklist,R.layout.listview_black_list,new String[]{"id","query"},new int[]{R.id.id_tv,R.id.content_tv});
        blacklistview.setAdapter(blackAdapter);
    }

}
