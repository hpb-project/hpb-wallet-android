package com.zhaoxi.Open_source_Android.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.bean.FlowBean;
import com.zhaoxi.Open_source_Android.common.view.AutoWrapViewGroup;

import java.util.Collections;
import java.util.List;

/**
 * des:
 * Created by ztt on 2018/6/11.
 */

public class FlowViewAdapter {
    private List<FlowBean> mFolwData;
    private Context mContext;
    private AutoWrapViewGroup mLayoutWords;
    private LayoutInflater mInflater;
    private boolean mInClick = false;

    public FlowViewAdapter(Context context, AutoWrapViewGroup parent, List<FlowBean> datas,boolean isClick) {
        this.mContext = context;
        this.mFolwData = datas;
        this.mLayoutWords = parent;
        mInflater = LayoutInflater.from(mContext);
        mInClick = isClick;
        bindView();
    }

    private void notitfyChange(){
        bindView();
    }

    public void notitfyCancleChange(List<FlowBean> folwData) {
        this.mFolwData = folwData;
        bindView();
    }

    private void bindView() {
        mLayoutWords.removeAllViews();
        Collections.shuffle(mFolwData);
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
            if(mInClick){
                text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (flowBean.isChecked()) {
                            text.setTextColor(mContext.getResources().getColor(R.color.color_3F415D));
                            text.setBackgroundResource(R.drawable.bg_hot_init_word);
                            flowBean.setChecked(false);
                            mOnClickListener.setOnCanleListener(flowBean.getContent());
                        } else {
                            text.setTextColor(mContext.getResources().getColor(R.color.white));
                            text.setBackgroundResource(R.drawable.bg_hot_selected_word);
                            flowBean.setChecked(true);
                            mOnClickListener.setOnClickListener(flowBean.getContent());
                        }
                        notitfyChange();
                    }
                });
            }

            mLayoutWords.addView(text);
        }
    }

    public interface OnClickListener {
        void setOnClickListener(String content);

        void setOnCanleListener(String content);
    }

    public void setOnClickListener(OnClickListener mOnClickListener) {
        this.mOnClickListener = mOnClickListener;
    }

    private OnClickListener mOnClickListener;

}
