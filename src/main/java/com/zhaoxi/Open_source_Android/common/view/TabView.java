package com.zhaoxi.Open_source_Android.common.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhaoxi.Open_source_Android.dapp.R;

/**
 * des:
 * Created by ztt on 2019/2/14.
 */

public class TabView extends RelativeLayout {

    private TextView mTextView;

    private String titleText;
    private int iconId;

    private Context context;
    public TabView(Context context) {
        super(context);
        this.context = context;
    }

    public TabView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //
        ////加载视图的布局
        LayoutInflater.from(context).inflate(R.layout.tabview,this,true);
        TypedArray a=context.obtainStyledAttributes(attrs,R.styleable.Tab);
        titleText=a.getString(R.styleable.Tab_umname);

        a.recycle();
    }

    /**
     * 此方法会在所有的控件都从xml文件中加载完成后调用
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        //获取子控件
        mTextView= (TextView) findViewById(R.id.txt_tabview_name);
        mTextView.setText(titleText);

    }
    public void setTabSelected(){
        mTextView.setTextColor(getResources().getColor(R.color.color_2E2F47));
        findViewById(R.id.view_tabview_selected).setVisibility(VISIBLE);
    }
    public void setTabUnSelected(){
        mTextView.setTextColor(getResources().getColor(R.color.color_9C9EB9));
        findViewById(R.id.view_tabview_selected).setVisibility(GONE);
    }

    public void setText(String text) {
        mTextView.setText(text);
    }
}
