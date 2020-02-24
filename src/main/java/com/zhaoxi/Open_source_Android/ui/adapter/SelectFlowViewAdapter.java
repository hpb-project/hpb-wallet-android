package com.zhaoxi.Open_source_Android.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.bean.FlowBean;
import com.zhaoxi.Open_source_Android.common.view.AutoWrapViewGroup;

import java.util.List;

/**
 * des:
 * Created by ztt on 2018/6/11.
 */

public class SelectFlowViewAdapter {
    private List<FlowBean> mFolwData;
    private Context mContext;
    private AutoWrapViewGroup mLayoutWords;
    private LayoutInflater mInflater;

    public SelectFlowViewAdapter(Context context, AutoWrapViewGroup parent, List<FlowBean> datas) {
        this.mContext = context;
        this.mFolwData = datas;
        this.mLayoutWords = parent;
        mInflater = LayoutInflater.from(mContext);
        bindView();
    }

    public void notifyDataChange(List<FlowBean> folwData){
        this.mFolwData = folwData;
        bindView();
    }

    private void bindView() {
        mLayoutWords.removeAllViews();
        for (int i = 0; i < mFolwData.size(); i++) {
            final FlowBean flowBean = mFolwData.get(i);
            final TextView text = (TextView) mInflater.inflate(R.layout.view_flow_txt, mLayoutWords, false);
            text.setText(flowBean.getContent());
//            text.setTextColor(mContext.getResources().getColor(R.color.colorAccent));
//            text.setBackgroundResource(R.drawable.bg_hot_init_word);
            text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnDeleteListener.setOnDeleteListener(flowBean.getContent());
                }
            });

            mLayoutWords.addView(text);
        }
    }

    public interface OnDeleteListener {
        void setOnDeleteListener(String content);
    }

    public void setOnDeleteListener(OnDeleteListener mOnDeleteListener) {
        this.mOnDeleteListener = mOnDeleteListener;
    }

    private OnDeleteListener mOnDeleteListener;

}
