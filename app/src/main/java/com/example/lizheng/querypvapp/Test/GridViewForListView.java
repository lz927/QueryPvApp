package com.example.lizheng.querypvapp.Test;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by Lizheng on 2015/7/3.
 */
public class GridViewForListView extends GridView {
    public GridViewForListView(Context context) {
        this(context, null);
    }

    public GridViewForListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GridViewForListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
