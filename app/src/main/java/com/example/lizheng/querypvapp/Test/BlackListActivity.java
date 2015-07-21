package com.example.lizheng.querypvapp.Test;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.lizheng.querypvapp.Config.QueryApp;
import com.example.lizheng.querypvapp.FlowTags.FlowLayout;
import com.example.lizheng.querypvapp.R;
import com.readystatesoftware.viewbadger.BadgeView;

import java.util.ArrayList;
import java.util.HashSet;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Created by Lizheng on 2015/7/6.
 */
public class BlackListActivity extends Activity {

    private EditText searchEt;
    private Button searchBtn;
    private ImageView backBtn;
    private ArrayList<String> blacklist;
    private FlowLayout mFlowLayout;
    private ArrayList<BadgeView> badgeList;
    private int badgeId = -1;


    private Handler myHandler = new Handler(){
        @Override
        public void handleMessage(Message message){
            initFlowTags();
            mFlowLayout.invalidate();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blacklist_flow_tag);
        searchEt = (EditText) findViewById(R.id.search_et);
        searchBtn = (Button) findViewById(R.id.search_btn);
        backBtn = (ImageView) findViewById(R.id.back_iv);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mFlowLayout = (FlowLayout) findViewById(R.id.black_flow_layout);
        initListView();
    }

    private void initListView() {
        HashSet<String> list = new HashSet<>();
        SharedPreferences sharedPreferences = getSharedPreferences(QueryApp.BLACK_NAME, 0);
        blacklist = new ArrayList<String>();
        blacklist.addAll(sharedPreferences.getStringSet("black", list));

        initFlowTags();
    }
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void initFlowTags(){
        badgeList = new ArrayList<>();
        mFlowLayout.removeAllViews();
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        for (int i = 0; i < blacklist.size(); i++){
            final LinearLayout llayout = new LinearLayout(this);
            final TextView textView = new TextView(this);
            textView.setText(blacklist.get(i));
            textView.setPadding(30, 20, 30, 20);
            textView.setSingleLine();
            textView.setTextSize(16);
            textView.setTextColor(Color.argb(200, 0, 0, 0));
            textView.setBackgroundDrawable(getResources().getDrawable(R.drawable.textview_bg));
            llayout.setTag(i);
            llayout.setPadding(30, 50, 50, 0);
            llayout.setLayoutParams(layoutParams);
            llayout.addView(textView);
            mFlowLayout.addView(llayout, i);

            final BadgeView badge = new BadgeView(BlackListActivity.this, llayout);
            badge.setPadding(10,0,10,20);
            badge.setBackground(new ColorDrawable());//
            badge.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.mipmap.icon_failure_least),null,null,null);
            badge.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int index = (int) ((BadgeView) v).getTarget().getTag();
                    Log.e("BlackListActivity", "index = " + index);
                    blacklist.remove(index);
                    myHandler.sendEmptyMessage(0);
                }
            });
            badgeList.add(badge);
            llayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    badge.hide();
                    if(badgeId != -1) {
                        badgeList.get(badgeId).hide();
                        badgeId  = -1;
                    }
                }
            });
            llayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if(badgeId != -1) {
                        badgeList.get(badgeId).hide();
                    }
                    badgeId = (int) v.getTag();
//                    badge.show();
                    badgeList.get(badgeId).show();
                    return true;
                }
            });
        }
    }

    //  保存黑名单
    private void saveBlackList(Context context,ArrayList<String> blackList) {
        SharedPreferences sp = context.getSharedPreferences(QueryApp.BLACK_NAME, 0);
        SharedPreferences.Editor editor = sp.edit();
        HashSet<String> list = new HashSet<>(blackList);
        editor.remove("black");
        editor.commit();
//        editor.clear();
        editor.putStringSet("black",list);
        editor.commit();
    }

    @Override
    protected void onPause(){
        super.onPause();
        saveBlackList(this, blacklist);
    }

    @Override
    protected void onResume(){
        super.onResume();
//        initListView();
    }
}
