package com.zhaoxi.Open_source_Android.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

public class GetRedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int ITEM_TYPE_01 = 1;
    private static final int ITEM_TYPE_02 = 2;
    private static final int ITEM_TYPE_03 = 3;

    private Context mContext;
    private List<RedDetailsBean.RedDetails> mDataList;
    private RedDetailsBean mRedBean;

    public GetRedAdapter(Context context, List<RedDetailsBean.RedDetails> listData,
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
            itemView = LayoutInflater.from(mContext).inflate(R.layout.view_red_get_top, viewGroup, false);
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
                topHolder.mTxtRedType.setText("1".equals(mRedBean.getType()) ? mContext.getResources().getString(R.string.activity_red_record_txt_09_2) : mContext.getResources().getString(R.string.activity_red_record_txt_09_1));//1-普通，2-拼手气红包
                BigDecimal money = new BigDecimal("" + mRedBean.getTokenValue());
                topHolder.mTxtMoney.setText(SlinUtil.formatNumFromWeiS(mContext, money));
                topHolder.mTxtAddress.setText(mContext.getResources().getString(R.string.activity_red_record_txt_06,
                        StrUtil.addressFilte10r(mRedBean.getFrom())));
                topHolder.mTxtDes.setText(mRedBean.getTitle());

                if ("10".equals(mRedBean.getDrawStatus())) {
                    String statusDes = "";
                    if ("3".equals(mRedBean.getIsOver())) {
                        statusDes = mContext.getResources().getString(R.string.activity_red_get_txt_11_01);
                        topHolder.mBtnChoose.setVisibility(View.GONE);
                        topHolder.mTxtStatus.setVisibility(View.VISIBLE);
                        topHolder.mTxtStatus.setText(statusDes);
                    } else {
                        if ("1".equals(mRedBean.getIsOver()) && "0".equals(mRedBean.getKeyIsVaild())) {
                            topHolder.mBtnChoose.setVisibility(View.VISIBLE);
                            topHolder.mTxtStatus.setVisibility(View.GONE);
                        } else {
                            if (mRedBean.getUsedNum() == mRedBean.getTotalPacketNum()) {
                                statusDes = mContext.getResources().getString(R.string.activity_red_get_txt_11_02);
                            } else if ("1".equals(mRedBean.getIsOver()) && "1".equals(mRedBean.getKeyIsVaild())) {
                                statusDes = mContext.getResources().getString(R.string.activity_red_get_txt_11_03);
                            } else if ("2".equals(mRedBean.getIsOver())) {
                                statusDes = mContext.getResources().getString(R.string.activity_red_get_txt_11_04);
                            }
                            topHolder.mBtnChoose.setVisibility(View.GONE);
                            topHolder.mTxtStatus.setVisibility(View.VISIBLE);
                            topHolder.mTxtStatus.setText(statusDes);
                        }
                    }
                } else {
                    String statusDes = "";
                    switch (mRedBean.getDrawStatus()) {
                        case "0":
                            statusDes = mContext.getResources().getString(R.string.activity_red_get_txt_07_02);
                            break;
                        case "1":
                            statusDes = mContext.getResources().getString(R.string.activity_red_get_txt_07_01);
                            break;
                        case "5":
                            statusDes = mContext.getResources().getString(R.string.activity_red_get_txt_14);
                            break;
                    }
                    topHolder.mBtnChoose.setVisibility(View.GONE);
                    topHolder.mTxtStatus.setVisibility(View.VISIBLE);
                    topHolder.mTxtStatus.setText(statusDes);
                }

            }

            topHolder.mBtnChoose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnBtnChooseListener.setOnChooseListener();
                }
            });
        } else if (position == 1) {
            if (mRedBean == null) return;
            ItemMiddleHolder middleHolder = (ItemMiddleHolder) holder;
            BigDecimal money = new BigDecimal("" + mRedBean.getTotalCoin());
            String content = SlinUtil.formatNumFromWeiS(mContext, money);
            String pre = mContext.getResources().getString(R.string.activity_red_get_txt_10_01);
            if(mRedBean.getUsedNum() == mRedBean.getTotalPacketNum()){
                pre = mContext.getResources().getString(R.string.activity_red_get_txt_09_02);
            }else{
                if("2".equals(mRedBean.getIsOver())){
                    pre = mContext.getResources().getString(R.string.activity_red_get_txt_09_04);
                }else{
                    pre = mContext.getResources().getString(R.string.activity_red_get_txt_10_01);
                }
            }
            middleHolder.mTxtMNum.setText(pre +
                    mContext.getResources().getString(R.string.activity_red_record_txt_1,
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
        String content = SlinUtil.FormatNum(mContext, money);
        itemHolder.mTxtMoney.setText(content);
        itemHolder.mTxtCoinType.setText("HPB");
        itemHolder.mTxtTime.setText(DateUtilSL.dateToStrymdhms2(DateUtilSL.getDateByLong(entity.getGmtCreate(), 1)));

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
        private TextView mTxtRedType, mTxtDes, mTxtMoney, mTxtStatus;
        private MyTextView mTxtAddress;
        private Button mBtnChoose;

        public ItemTopHolder(View itemView) {
            super(itemView);
            mTxtRedType = itemView.findViewById(R.id.item_get_red_txt_top_type);
            mTxtAddress = itemView.findViewById(R.id.item_get_red_txt_top_address);
            mTxtDes = itemView.findViewById(R.id.item_get_red_txt_top_des);
            mTxtMoney = itemView.findViewById(R.id.item_get_red_top_money);
            mBtnChoose = itemView.findViewById(R.id.item_get_red_btn_choose);
            mTxtStatus = itemView.findViewById(R.id.item_get_red_txt_top_status);
        }
    }

    public class ItemMiddleHolder extends RecyclerView.ViewHolder {
        private MyTextView mTxtMNum;

        public ItemMiddleHolder(View itemView) {
            super(itemView);
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

    public interface OnBtnChooseListener {
        void setOnChooseListener();
    }

    public void setOnBtnChooseListener(OnBtnChooseListener btnChooseListener) {
        this.mOnBtnChooseListener = btnChooseListener;
    }

    private OnBtnChooseListener mOnBtnChooseListener;


}
