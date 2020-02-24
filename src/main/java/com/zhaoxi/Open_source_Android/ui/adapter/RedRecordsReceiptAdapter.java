package com.zhaoxi.Open_source_Android.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.libs.utils.CollectionUtil;
import com.zhaoxi.Open_source_Android.libs.utils.DateUtilSL;
import com.zhaoxi.Open_source_Android.libs.utils.SlinUtil;
import com.zhaoxi.Open_source_Android.libs.utils.StrUtil;
import com.zhaoxi.Open_source_Android.net.bean.RedRecordBean;
import com.zhaoxi.Open_source_Android.ui.activity.RedRecordsReceiptDetailsActivity;

import java.math.BigDecimal;
import java.util.List;

/**
 * des:
 * Created by ztt on 2018/6/6.
 */

public class RedRecordsReceiptAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int ITEM_TYPE_01 = 1;
    private static final int ITEM_TYPE_02 = 2;

    private Context mContext;
    private List<RedRecordBean.RedBean> mDataList;
    private String mTotalRedNum;
    private int mCurLaguage;

    public RedRecordsReceiptAdapter(Context context, List<RedRecordBean.RedBean> listData, String redNum,int curlaugage) {
        this.mContext = context;
        this.mDataList = listData;
        this.mTotalRedNum = redNum;
        mCurLaguage = curlaugage;
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
            itemView = LayoutInflater.from(mContext).inflate(R.layout.item_redrecord_receipt_list, viewGroup, false);
            return new ItemHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position == 0) {
            ItemTopHolder topHolder = (ItemTopHolder) holder;
            topHolder.mTxtRedNum.setText("" + mTotalRedNum);
            topHolder.mTxtRedType.setText(mContext.getResources().getString(R.string.activity_red_record_txt_03));
            if(mDataList.size() == 0){
                topHolder.mTxtEmpty.setText(mContext.getResources().getString(R.string.activity_red_get_txt_15_02));
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
        if("1".equals(entity.getRedPacketType())){
            if(mCurLaguage == 1){
                itemHolder.mImgRedType.setBackgroundResource(R.mipmap.icon_red_style_pu);
            }else{
                itemHolder.mImgRedType.setBackgroundResource(R.mipmap.icon_red_style_pu_en);
            }
        }else{
            if(mCurLaguage == 1){
                itemHolder.mImgRedType.setBackgroundResource(R.mipmap.icon_red_style_ping);
            }else{
                itemHolder.mImgRedType.setBackgroundResource(R.mipmap.icon_red_style_ping_en);
            }
        }

        itemHolder.mTxtAddress.setText(StrUtil.addressFilte10r(entity.getFromAddr()));

        BigDecimal money = new BigDecimal("" + entity.getCoinValue());
        itemHolder.mTxtMoney.setText(SlinUtil.formatNumFromWeiS(mContext, money));
        itemHolder.mTxtTime.setText(DateUtilSL.dateToStrymdhms2(DateUtilSL.getDateByLong(entity.getTradeTime(),1)));
        itemHolder.mTxtRedStatus.setText(mContext.getResources().getString(R.string.activity_red_get_txt_10_01));

        itemHolder.mLayoutBase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it_details = new Intent(mContext, RedRecordsReceiptDetailsActivity.class);
                it_details.putExtra(RedRecordsReceiptDetailsActivity.RED_RECORD_BEAN, entity);
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
        private TextView mTxtMoney, mTxtCoinType, mTxtTime, mTxtRedStatus, mTxtAddress;
        private ImageView mImgRedType;
        private LinearLayout mLayoutBase;

        public ItemHolder(View itemView) {
            super(itemView);
            mImgRedType = itemView.findViewById(R.id.item_redrecords_receipt_img_type);
            mTxtMoney = itemView.findViewById(R.id.item_redrecords_receipt_txt_allmoney);
            mTxtCoinType = itemView.findViewById(R.id.item_redrecords_receipt_txt_cointype);
            mTxtTime = itemView.findViewById(R.id.item_redrecords_receipt_txt_time);
            mTxtRedStatus = itemView.findViewById(R.id.item_redrecords_receipt_txt_status);
            mTxtAddress = itemView.findViewById(R.id.item_redrecords_receipt_txt_address);
            mLayoutBase = itemView.findViewById(R.id.item_redrecords_receipt_layout_base);
        }
    }
}
