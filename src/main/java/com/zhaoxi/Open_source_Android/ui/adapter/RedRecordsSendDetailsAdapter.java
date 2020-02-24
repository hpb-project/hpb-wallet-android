package com.zhaoxi.Open_source_Android.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.view.MyTextView;
import com.zhaoxi.Open_source_Android.libs.utils.CollectionUtil;
import com.zhaoxi.Open_source_Android.libs.utils.DateUtilSL;
import com.zhaoxi.Open_source_Android.libs.utils.SlinUtil;
import com.zhaoxi.Open_source_Android.libs.utils.StrUtil;
import com.zhaoxi.Open_source_Android.net.bean.RedDetailsBean;

import java.math.BigDecimal;
import java.util.List;

/**
 * des:
 * Created by ztt on 2018/6/6.
 */

public class RedRecordsSendDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int ITEM_TYPE_01 = 1;
    private static final int ITEM_TYPE_02 = 2;
    private static final int ITEM_TYPE_03 = 3;

    private Context mContext;
    private List<RedDetailsBean.RedDetails> mDataList;
    private RedDetailsBean mRedBean;

    public RedRecordsSendDetailsAdapter(Context context, List<RedDetailsBean.RedDetails> listData,
                                        RedDetailsBean redBean) {
        this.mContext = context;
        this.mDataList = listData;
        this.mRedBean = redBean;
    }

    public void setTopData(RedDetailsBean redBean) {
        this.mRedBean = redBean;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView;
        if (viewType == ITEM_TYPE_01) {
            itemView = LayoutInflater.from(mContext).inflate(R.layout.view_redrecords_send_details_top, viewGroup, false);
            return new ItemTopHolder(itemView);
        } else if (viewType == ITEM_TYPE_02) {
            itemView = LayoutInflater.from(mContext).inflate(R.layout.view_redrecords_send_details_middle, viewGroup, false);
            return new ItemMiddleHolder(itemView);
        } else {
            itemView = LayoutInflater.from(mContext).inflate(R.layout.item_redrecord_send_details_list, viewGroup, false);
            return new ItemHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position == 0) {
            ItemTopHolder topHolder = (ItemTopHolder) holder;
            if (mRedBean != null) {
                topHolder.mImgType.setText("1".equals(mRedBean.getType()) ? mContext.getResources().getString(R.string.activity_red_record_txt_09_2) : mContext.getResources().getString(R.string.activity_red_record_txt_09_1));//1-普通，2-拼手气红包
                BigDecimal money = new BigDecimal("" + mRedBean.getTotalCoin());
                topHolder.mTxtRedMoney.setText(SlinUtil.formatNumFromWeiS(mContext, money));
                topHolder.mTxtFormAddress.setText(mContext.getResources().getString(R.string.activity_red_record_txt_06,
                        StrUtil.addressFilte10r(mRedBean.getFrom())));
                topHolder.mTxtDes.setText(mRedBean.getTitle());
            }

        } else if (position == 1) {
            ItemMiddleHolder middleHolder = (ItemMiddleHolder) holder;
            BigDecimal money = new BigDecimal("" + mRedBean.getTotalCoin());
            String content = SlinUtil.formatNumFromWeiS(mContext, money);
            if(mContext.getResources().getString(R.string.activity_red_get_txt_09_02).equals(mRedBean.getRedStatus())){
                middleHolder.mTxtStatus.setText(mContext.getResources().getString(R.string.activity_red_get_txt_09_02));
            }else if(mContext.getResources().getString(R.string.activity_red_get_txt_09_03).equals(mRedBean.getRedStatus())){
                middleHolder.mTxtStatus.setText(mContext.getResources().getString(R.string.activity_red_get_txt_10_01));
            }else if(mContext.getResources().getString(R.string.activity_red_get_txt_09_04).equals(mRedBean.getRedStatus())){
                middleHolder.mTxtStatus.setText(mContext.getResources().getString(R.string.activity_red_get_txt_09_04));
            }
            middleHolder.mTxtMNum.setText(mContext.getResources().getString(R.string.activity_red_record_txt_1,
                    mRedBean.getUsedNum() + "/" + mRedBean.getTotalPacketNum(), content, "HPB"));
        } else {
            setDatas(holder, position, 2);
        }
    }

    private void setDatas(RecyclerView.ViewHolder holder, int position, int sub) {
        ItemHolder itemHolder = (ItemHolder) holder;
        final RedDetailsBean.RedDetails entity = mDataList.get(position - sub);
        itemHolder.mTxtAddress.setText(StrUtil.addressFilte10r(entity.getToAddr()));
        BigDecimal money = new BigDecimal("" + entity.getCoinValue());
        String content = SlinUtil.formatNumFromWeiS(mContext, money);
        itemHolder.mTxtMoney.setText(content);
        itemHolder.mTxtCoinType.setText("HPB");
        itemHolder.mTxtTime.setText(DateUtilSL.dateToStrymdhms2(DateUtilSL.getDateByLong(entity.getTradeTime(),1)));
        if(entity.getMaxFlag().equals("1")){
            itemHolder.mImgGood.setVisibility(View.VISIBLE);
        }else itemHolder.mImgGood.setVisibility(View.GONE);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {//第一行
            return ITEM_TYPE_01;
        } else if (position == 1) {
            return ITEM_TYPE_02;
        } else {
            return ITEM_TYPE_03;
        }
    }

    @Override
    public int getItemCount() {
        int count = CollectionUtil.isCollectionEmpty(mDataList) ? 2 : mDataList.size() + 2;
        return count;
    }


    public class ItemTopHolder extends RecyclerView.ViewHolder {
        private TextView mTxtRedMoney;
        private MyTextView mTxtFormAddress;
        private TextView mImgType, mTxtDes;

        public ItemTopHolder(View itemView) {
            super(itemView);
            mTxtRedMoney = itemView.findViewById(R.id.item_redrecords_details_txt_top_money);
            mImgType = itemView.findViewById(R.id.item_redrecords_details_txt_top_type);
            mTxtFormAddress = itemView.findViewById(R.id.item_redrecords_details_txt_top_address);
            mTxtDes = itemView.findViewById(R.id.item_redrecords_details_txt_top_des);
        }
    }

    public class ItemMiddleHolder extends RecyclerView.ViewHolder {
        private TextView mTxtMNum;
        private TextView mTxtStatus;

        public ItemMiddleHolder(View itemView) {
            super(itemView);
            mTxtStatus = itemView.findViewById(R.id.item_redrecords_details_txt_status);
            mTxtMNum = itemView.findViewById(R.id.item_redrecords_details_txt_mnum);
        }
    }


    public class ItemHolder extends RecyclerView.ViewHolder {
        private TextView mTxtAddress, mTxtMoney, mTxtCoinType, mTxtTime;
        private LinearLayout mImgGood;

        public ItemHolder(View itemView) {
            super(itemView);
            mTxtAddress = itemView.findViewById(R.id.item_redrecords_details_txt_address);
            mTxtMoney = itemView.findViewById(R.id.item_redrecords_details_txt_allmoney);
            mTxtCoinType = itemView.findViewById(R.id.item_redrecords_details_txt_cointype);
            mTxtTime = itemView.findViewById(R.id.item_redrecords_details_txt_time);
            mImgGood = itemView.findViewById(R.id.item_redrecords_details_layout_good);
        }
    }
}
