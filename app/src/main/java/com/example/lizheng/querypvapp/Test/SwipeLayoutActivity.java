package com.example.lizheng.querypvapp.Test;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.lizheng.querypvapp.Config.QueryApp;
import com.example.lizheng.querypvapp.CustomerView.MyListViewAdapter;
import com.example.lizheng.querypvapp.DayQuery;
import com.example.lizheng.querypvapp.HttpHelper;
import com.example.lizheng.querypvapp.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;


public class SwipeLayoutActivity extends ActionBarActivity implements View.OnClickListener , SwipeListViewAdapter.OnItemDeleteListener{

    private ListView listView;
    private ArrayAdapter<String> mAdapter;
    private MyListViewAdapter myAdapter;
    private SwipeListViewAdapter swipeListViewAdapter;
    private ImageView goTopBtn;
    private EditText editText;
    private Button button, blackBtn ;
    private View footerView;
    private View dialogView;
    private DatePicker mDatePicker;
    private HashSet<String> blackList;//黑名单
    private  ArrayList<DateQuery> list ;
    public ArrayList<HashMap<String,DayQuery>> qlist;
    private int mPage;
    private String mDate;


    private Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg){
            swipeListViewAdapter.notifyDataSetChanged();
            isRefreshed = true;
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = (EditText) findViewById(R.id.date_et);
        button = (Button) findViewById(R.id.button);
        blackBtn = (Button) findViewById(R.id.black_btn);
        goTopBtn = (ImageView) findViewById(R.id.gotop_iv);
        editText.setOnClickListener(this);
        button.setOnClickListener(this);
        blackBtn.setOnClickListener(this);
        goTopBtn.setOnClickListener(this);
        footerView = LayoutInflater.from(this).inflate(R.layout.listview_footer_view,null);

        listView = (ListView) findViewById(R.id.listview);
        listView.addFooterView(footerView);

        blackList = initBlackList(this);
        list = new ArrayList<DateQuery>();
        initDate(editText);
        mPage=1;
        final InitThread initThread = new InitThread(mDate,mPage);
        initThread.start();

        swipeListViewAdapter = new SwipeListViewAdapter(this,R.layout.listview_swipe_with_gridview_layout,R.id.swiplayout_with_gridview,list, this);
        listView.setAdapter(swipeListViewAdapter);

        //设置下拉刷新
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            int mScrollState;
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                mScrollState = scrollState;
            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (((firstVisibleItem+visibleItemCount) == totalItemCount) && mScrollState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                    Toast.makeText(SwipeLayoutActivity.this, "刷新！", Toast.LENGTH_SHORT).show();
                    toRefresh = true;
                    refreshList();
                }
                else if ((firstVisibleItem+visibleItemCount) == totalItemCount && mScrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE){
//                    Toast.makeText(SwipeLayoutActivity.this, "加载完", Toast.LENGTH_SHORT).show();
                    listView.setSelection(firstVisibleItem-1);

                }else {
                    toRefresh = false;
                }
            }
        });
    }

    @Override
    protected void onPause(){
        super.onPause();
        saveBlackList(this,blackList);

    }

    @Override
    protected  void onResume(){
        super.onResume();
        blackList = initBlackList(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.date_et:
                showDialog(SwipeLayoutActivity.this, editText);
                break;
            case R.id.black_btn:
                Intent intent = new Intent(SwipeLayoutActivity.this, com.example.lizheng.querypvapp.Test.BlackListActivity.class);
                startActivity(intent);
                break;
            case R.id.button:
                /*Intent swipeIntent = new Intent(SwipeLayoutActivity.this, TestSwipeLayoutActivity.class);
                startActivity(swipeIntent);*/
                String date = formatDateToString(editText.getText().toString());
                mPage = 1;
//                list = new ArrayList<>();
                list.clear();
                myHandler.sendEmptyMessage(0);
                InitThread initThread = new InitThread(date,mPage);
                initThread.start();
                break;
            case R.id.gotop_iv:
                listView.setSelection(0);
                break;
        }
    }

    @Override
    public void onItemDelete(int position) {
        blackList.add(list.get(position).getQuery());
        list.remove(position);
//        myHandler.sendEmptyMessage(0);
    }


    boolean toRefresh = false,isRefreshed = false;

    //  加载更多，刷新列表
    private void refreshList() {
        if(toRefresh && isRefreshed) {
            InitThread initThread1 = new InitThread(mDate, mPage);
            initThread1.start();
        }
    }

    //  list除去黑名单中内容
    private ArrayList<DateQuery> removeBlackList(ArrayList<DateQuery> list) {
        for (String s : blackList){
            listRemove(list,s);
        }
        return list;
    }
    private void listRemove(ArrayList<DateQuery> list, String s) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getQuery().equals(s)){
                list.remove(i);
                break;
            }
        }
    }

    //  初始化黑名单，从sharedPreference中获取黑名单列表
    private HashSet<String> initBlackList(Context context) {
        HashSet<String> list = new HashSet<>();
        SharedPreferences sp = context.getSharedPreferences(QueryApp.BLACK_NAME, 0);
        list.addAll(sp.getStringSet("black", list));
        return list;
    }

    //  保存黑名单
    private void saveBlackList(Context context,HashSet<String> blackList) {
        SharedPreferences sp = context.getSharedPreferences(QueryApp.BLACK_NAME, 0);
        SharedPreferences.Editor editor = sp.edit();
        editor.putStringSet("black",blackList);
        editor.commit();
    }

    //  显示日期对话框
    public void showDialog(Context context, final EditText editText){
        String date = editText.getText().toString();
        String[] dts = date.split("-");
        int year = Integer.valueOf(dts[0]);
        int month = Integer.valueOf(dts[1])-1;
        int day = Integer.valueOf(dts[2]);
        dialogView = LayoutInflater.from(context).inflate(R.layout.choose_date_layout, null);
        mDatePicker = (DatePicker) dialogView.findViewById(R.id.datePicker);
        mDatePicker.clearFocus();
        mDatePicker.setDescendantFocusability(DatePicker.FOCUS_BLOCK_DESCENDANTS);
        mDatePicker.init(year, month, day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                editText.setText(getFormatDate(year, monthOfYear + 1, dayOfMonth));
            }
        });
         AlertDialog.Builder alertDialog = new AlertDialog.Builder(context)
                .setTitle("选择日期")
                .setView(dialogView)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    // 初始化日期
    private void initDate(EditText editText) {
        Calendar calendar = Calendar.getInstance();
        int y = calendar.get(Calendar.YEAR);
        int m = calendar.get(Calendar.MONTH)+1;
        int d = calendar.get(Calendar.DAY_OF_MONTH)-1;
        String date = getFormatDate(y,m,d);
        mDate = formatDateToString(date);
        editText.setText(date);
    }

    private String formatDateToString(String date) {
        String[] dts = date.split("-");
        return dts[0]+dts[1]+dts[2];
    }

    private String getFormatDate(int year, int month, int dayOfMonth) {
        if (month<10)
            return dayOfMonth < 10 ?  year + "-0" + month + "-0" + dayOfMonth : year + "-0" + month + "-" + dayOfMonth;
        else
            return dayOfMonth < 10 ? year+"-" + month + "-0" + dayOfMonth : year + "-" + month + "-" + dayOfMonth ;
    }

    private class InitThread extends Thread {
        private String mDate;
        private int page;

        public InitThread(String date, int page) {
            this.mDate = date;
            this.page = page;
        }

        @Override
        public void run() {
            isRefreshed = false;
            ArrayList<DateQuery> dlist = HttpHelper.GetHttp(mDate, page);
            dlist = removeBlackList(dlist);
            list.addAll(dlist);
            mPage++;
            myHandler.sendEmptyMessage(0);
        }
    }

}
