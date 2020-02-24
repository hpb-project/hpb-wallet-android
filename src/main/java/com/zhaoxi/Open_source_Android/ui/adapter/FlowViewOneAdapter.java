package com.zhaoxi.Open_source_Android.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.bean.FlowBean;
import com.zhaoxi.Open_source_Android.common.view.AutoWrapViewGroup;

import java.util.List;

/**
 * des:
 * Created by ztt on 2018/6/11.
 */

public class FlowViewOneAdapter {
    private List<FlowBean> mFolwData;
    private Context mContext;
    private AutoWrapViewGroup mLayoutWords;
    private LayoutInflater mInflater;

    public FlowViewOneAdapter(Context context, AutoWrapViewGroup parent, List<FlowBean> datas) {
        this.mContext = context;
        this.mFolwData = datas;
        this.mLayoutWords = parent;
        mInflater = LayoutInflater.from(mContext);
        bindView();
    }

    private void bindView() {
        mLayoutWords.removeAllViews();
        for (int i = 0; i < mFolwData.size(); i++) {
            final FlowBean flowBean = mFolwData.get(i);
            final TextView text = (TextView) mInflater.inflate(R.layout.view_flow_txt_02, mLayoutWords, false);
            text.setText(flowBean.getContent());
            if (flowBean.isChecked()) {//是否被点击
                text.setTextColor(mContext.getResources().getColor(R.color.white));
                text.setBackgroundResource(R.drawable.bg_hot_selected_word);
            } else {
                text.setTextColor(mContext.getResources().getColor(R.color.color_3F415D));
                text.setBackgroundResource(R.drawable.bg_hot_init_word);
            }

            mLayoutWords.addView(text);
        }
    }
}
