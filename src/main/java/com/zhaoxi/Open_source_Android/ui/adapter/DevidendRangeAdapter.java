package com.zhaoxi.Open_source_Android.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.net.bean.NodeDividendBean;

import java.util.ArrayList;
import java.util.List;

/**
 * des:
 * Created by ztt on 2018/6/7.
 */
public class DevidendRangeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<NodeDividendBean> mData;

    public DevidendRangeAdapter(Context context, List<NodeDividendBean> data) {
        this.mContext = context;
        this.mData = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.view_devidend_range_item, viewGroup, false);
        return new ItemHodler(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        NodeDividendBean bean = mData.get(position);
        ItemHodler itemHodler = (ItemHodler) holder;
        itemHodler.mTxtRank.setText(bean.getRank());
        itemHodler.mTxtAddress.setText(bean.getAddress());
        itemHodler.mTxtPollNum.setText(bean.getPollNUm());

        itemHodler.mCheckBox.setChecked(bean.isChecked());

        itemHodler.mCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bean.setChecked(!bean.isChecked());
                notifyDataSetChanged();
                if(mListener != null) {
                    List<String> holdIds = new ArrayList<>();
                    for(NodeDividendBean entity : mData){
                        if(entity.isChecked())
                            holdIds.add(entity.getHoldId());
                    }
                    mListener.onChanged(holdIds);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    private class ItemHodler extends RecyclerView.ViewHolder {
        private CheckBox mCheckBox;
        private TextView mTxtRank,mTxtAddress, mTxtPollNum;

        public ItemHodler(View itemView) {
            super(itemView);
            mCheckBox = itemView.findViewById(R.id.item_devidend_checkbox);
            mTxtRank = itemView.findViewById(R.id.item_devidend_txt_rank);
            mTxtAddress = itemView.findViewById(R.id.item_devidend_txt_address);
            mTxtPollNum = itemView.findViewById(R.id.item_devidend_txt_poll);
        }
    }
    public interface OnCheckedChangedListener {
        void onChanged(List<String> holdIds);
    }
    private OnCheckedChangedListener mListener;

    public void setListener(OnCheckedChangedListener mListener) {
        this.mListener = mListener;
    }
}
