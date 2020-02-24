package com.zhaoxi.Open_source_Android.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.libs.utils.CollectionUtil;
import com.zhaoxi.Open_source_Android.libs.utils.DateUtilSL;
import com.zhaoxi.Open_source_Android.libs.utils.SlinUtil;
import com.zhaoxi.Open_source_Android.net.bean.RedRecordBean;
import com.zhaoxi.Open_source_Android.ui.activity.RedRecordsSendDetailsActivity;

import java.math.BigDecimal;
import java.util.List;

/**
 * des:
 * Created by ztt on 2018/6/6.
 */

public class RedRecordsSendAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int ITEM_TYPE_01 = 1;
    private static final int ITEM_TYPE_02 = 2;

    private Context mContext;
    private List<RedRecordBean.RedBean> mDataList;
    private String mTotalRedNum;

    public RedRecordsSendAdapter(Context context, List<RedRecordBean.RedBean> listData, String redNum) {
        this.mContext = context;
        this.mDataList = listData;
        this.mTotalRedNum = redNum;
    }

    public void setTopData(String redNum) {
        this.mTotalRedNum = redNum;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView;
        if (viewType == ITEM_TYPE_01) {
            itemView = LayoutInflater.from(mContext).inflate(R.layout.view_redrecords_send_top, viewGroup, false);
            return new ItemTopHolder(itemView);
        } else {
            itemView = LayoutInflater.from(mContext).inflate(R.layout.item_redrecord_send_list, viewGroup, false);
            return new ItemHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position == 0) {
            ItemTopHolder topHolder = (ItemTopHolder) holder;
            topHolder.mTxtRedNum.setText("" + mTotalRedNum);
            topHolder.mTxtRedType.setText(mContext.getResources().getString(R.string.activity_red_record_txt_04));
            if(mDataList.size() == 0){
                topHolder.mTxtEmpty.setText(mContext.getResources().getString(R.string.activity_red_get_txt_15_01));
                topHolder.mLayoutEmpty.setVisibility(View.VISIBLE);
            }else{
                topHolder.mLayoutEmpty.setVisibility(View.GONE);
            }
        } else {
            setDatas(holder, position, 1);
        }
    }

    private void setDatas(RecyclerView.ViewHolder holder, int position, int sub) {
        ItemHolder itemHolder = (ItemHolder) holder;
        final RedRecordBean.RedBean entity = mDataList.get(position - sub);
        itemHolder.mTxtRedType.setText("1".equals(entity.getRedPacketType())?mContext.getResources().getString(R.string.activity_red_get_txt_08_01):
                mContext.getResources().getString(R.string.activity_red_get_txt_08_02));//1-普通，2-拼手气红包
        BigDecimal money = new BigDecimal("" + entity.getTotalCoin());
        itemHolder.mTxtMoney.setText(SlinUtil.formatNumFromWeiS(mContext, money));
        itemHolder.mTxtCoinType.setText(entity.getCoinSymbol());
        itemHolder.mTxtTime.setText(DateUtilSL.dateToStrymdhms2(DateUtilSL.getDateByLong(entity.getStartTime(),1)));

        String status = entity.getStatus();
        String statusDes = mContext.getResources().getString(R.string.activity_red_get_txt_09_01);
        itemHolder.mTxtRedStatus.setTextColor(mContext.getResources().getColor(R.color.color_9FA1BB));
        if("2".equals(status)){
            statusDes = mContext.getResources().getString(R.string.activity_red_get_txt_09_01);
        } else if ("1".equals(status)) {
            if(entity.getUsedNum() == entity.getTotalNum())
                statusDes = mContext.getResources().getString(R.string.activity_red_get_txt_09_02);
            else{
                if ("1".equals(entity.getIsOver())) {
                    statusDes = mContext.getResources().getString(R.string.activity_red_get_txt_09_03);
                    itemHolder.mTxtRedStatus.setTextColor(mContext.getResources().getColor(R.color.color_4A5FE2));
                }else{
                    statusDes = mContext.getResources().getString(R.string.activity_red_get_txt_09_04);
                }
            }
        }

        itemHolder.mTxtRedStatus.setText(statusDes);

        itemHolder.mTxtPlan.setText(entity.getUsedNum() + "/" + entity.getTotalNum());
        String redId = entity.getRedPacketNo();
        String redTitle = entity.getTitle();

        String finalStatusDes = statusDes;
        itemHolder.mLayoutBase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it_details = new Intent(mContext, RedRecordsSendDetailsActivity.class);
                it_details.putExtra(RedRecordsSendDetailsActivity.RED_RECORD_ID, redId);
                it_details.putExtra(RedRecordsSendDetailsActivity.RED_RECORD_TITLE, redTitle);
                it_details.putExtra(RedRecordsSendDetailsActivity.RED_RECORD_STATUS, finalStatusDes);

                mContext.startActivity(it_details);
            }
        });

    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {//第一行
            return ITEM_TYPE_01;
        } else {
            return ITEM_TYPE_02;
        }
    }

    @Override
    public int getItemCount() {
        int count = CollectionUtil.isCollectionEmpty(mDataList) ? 1 : mDataList.size() + 1;
        return count;
    }


    public class ItemTopHolder extends RecyclerView.ViewHolder {
        private TextView mTxtRedNum;
        private TextView mTxtRedType,mTxtEmpty;
        private LinearLayout mLayoutEmpty;

        public ItemTopHolder(View itemView) {
            super(itemView);
            mTxtRedNum = itemView.findViewById(R.id.view_redrecords_send_top_txt_num);
            mTxtRedType = itemView.findViewById(R.id.view_redrecords_send_top_txt_type);
            mLayoutEmpty = itemView.findViewById(R.id.view_redrecords_send_top_empty);
            mTxtEmpty = itemView.findViewById(R.id.view_redrecords_send_top_empty_txt);
        }
    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        private TextView mTxtRedType, mTxtMoney, mTxtCoinType, mTxtTime, mTxtRedStatus, mTxtPlan;
        private LinearLayout mLayoutBase;

        public ItemHolder(View itemView) {
            super(itemView);
            mTxtRedType = itemView.findViewById(R.id.item_redrecords_send_txt_redtype);
            mTxtMoney = itemView.findViewById(R.id.item_redrecords_send_txt_allmoney);
            mTxtCoinType = itemView.findViewById(R.id.item_redrecords_send_txt_cointype);
            mTxtTime = itemView.findViewById(R.id.item_redrecords_send_txt_time);
            mTxtRedStatus = itemView.findViewById(R.id.item_redrecords_send_txt_status);
            mTxtPlan = itemView.findViewById(R.id.item_redrecords_send_txt_rednum);
            mLayoutBase = itemView.findViewById(R.id.item_redrecords_send_layout_base);
        }
    }
}
