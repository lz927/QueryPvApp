package com.example.lizheng.querypvapp.Test;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;
import com.example.lizheng.querypvapp.CustomerView.GridViewForListView;
import com.example.lizheng.querypvapp.R;

import java.util.ArrayList;

/**
 * Created by Lizheng on 2015/7/6.
 */
public class SwipeListViewAdapter extends BaseSwipeAdapter {

    private Context mContext;
    private int mSwipResId;
    private int mLayoutResId;
    private ArrayList<DateQuery> list;
    ArrayAdapter<String> gridViewAdapter;
    private OnItemDeleteListener mOnItemDeleteListener;
    int mPosition = 0;

    public  SwipeListViewAdapter(Context context,int layoutResId, int swipResId, ArrayList<DateQuery> list,OnItemDeleteListener onItemDeleteListener){
        this.mContext = context;
        this.mSwipResId = swipResId;
        this.mLayoutResId = layoutResId;
        this.list = list;
        this.mOnItemDeleteListener = onItemDeleteListener;
    }

    @Override
    public int getSwipeLayoutResourceId(int i) {
        return mSwipResId;
    }

    @Override
    public View generateView(int position, ViewGroup viewGroup) {
        View convertView = LayoutInflater.from(mContext).inflate(mLayoutResId,null);
//        final SwipeLayout swipeLayout = (SwipeLayout)convertView.findViewById(getSwipeLayoutResourceId(position));
//        swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
//        convertView.findViewById(R.id.delete_tv).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                System.out.print("qian");
//                mOnItemDeleteListener.onItemDelete(mPosition);
//                System.out.print("hou");
//                swipeLayout.close(true, false);   //平滑关闭
////                swipeLayout.close(false, false);    //直接关闭
////                list.remove(position);
//                notifyDataSetChanged();
//            }
//        });
        QueryHolder queryHolder = new QueryHolder();
        queryHolder.swipeLayout = (SwipeLayout)convertView.findViewById(getSwipeLayoutResourceId(position));
        queryHolder.swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
        queryHolder.id_tv = (TextView) convertView.findViewById(R.id.id_tv);
        queryHolder.query_tv = (TextView) convertView.findViewById(R.id.query_tv);
        queryHolder.gridView = (GridViewForListView) convertView.findViewById(R.id.gridview);
        queryHolder.pv_tv = (TextView) convertView.findViewById(R.id.pv_tv);
        queryHolder.uv_tv = (TextView) convertView.findViewById(R.id.uv_tv);
        convertView.setTag(queryHolder);
        return convertView;
    }

    @Override
    public void fillValues(final int position, View convertView) {
        final QueryHolder queryHolder = (QueryHolder) convertView.getTag();

        convertView.findViewById(R.id.delete_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnItemDeleteListener.onItemDelete(position);
                queryHolder.swipeLayout.close(true, false);   //平滑关闭
                notifyDataSetChanged();
            }
        });
        DateQuery dateQuery= list.get(position);
        queryHolder.id_tv.setText(dateQuery.getId()+"·");
        queryHolder.query_tv.setText(dateQuery.getQuery());
        queryHolder.pv_tv.setText(""+dateQuery.getPv());
        queryHolder.uv_tv.setText(""+dateQuery.getUv());

        String[] gridlist = getShowData(dateQuery);
        gridViewAdapter = new ArrayAdapter<String>(mContext, R.layout.griview_layout, gridlist);
        queryHolder.gridView.setAdapter(gridViewAdapter);
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class QueryHolder {
        TextView id_tv;
        TextView query_tv;
        GridViewForListView gridView;
        TextView pv_tv;
        TextView uv_tv;
        SwipeLayout swipeLayout;
    }
    private String[] getShowData(DateQuery dateQuery) {
        ArrayList<String[]> rates = dateQuery.getRates();
        int length = rates.size() * 2 + 2;
        String[] gridlist = new String[length];
        int p = rates.size()+1;
        gridlist[0] = "PV";
        gridlist[p] = "排名";
        for (int i = 1 ; i <= rates.size(); i ++){
            gridlist[0+i] = rates.get(i-1)[0];
            gridlist[p+i] = rates.get(i-1)[1];
        }
        return gridlist;
    }

    public interface OnItemDeleteListener{
        public void onItemDelete(int position);
    }
}
