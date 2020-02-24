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

import com.zhaoxi.Open_source_Android.DAppConstants;
import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.libs.utils.CollectionUtil;
import com.zhaoxi.Open_source_Android.libs.utils.DateUtilSL;
import com.zhaoxi.Open_source_Android.libs.utils.SlinUtil;
import com.zhaoxi.Open_source_Android.libs.utils.StrUtil;
import com.zhaoxi.Open_source_Android.net.bean.TranferRecordBean;
import com.zhaoxi.Open_source_Android.ui.activity.TotalAssetsActivity;
import com.zhaoxi.Open_source_Android.ui.activity.TransferRecodeDetailsActivity;

import java.math.BigDecimal;
import java.util.List;

/**
 * des:
 * Created by ztt on 2018/6/6.
 */

public class TotalAssetsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int ITEM_TYPE_01 = 1;
    private static final int ITEM_TYPE_02 = 2;
    private static final int ITEM_TYPE_03 = 3;

    private Context mContext;
    private List<TranferRecordBean.TransferInfo> mDataList;
    private BigDecimal mTotalMoney;
    private int mEyesStatus;

    public TotalAssetsAdapter(Context context, List<TranferRecordBean.TransferInfo> listData, BigDecimal totalMoney,int eyesStatus) {
        this.mContext = context;
        this.mDataList = listData;
        this.mTotalMoney = totalMoney;
        this.mEyesStatus = eyesStatus;
    }

    public void setTopData(BigDecimal totalMoney,int eyesStatus) {
        this.mTotalMoney = totalMoney;
        this.mEyesStatus = eyesStatus;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView;
        if (viewType == ITEM_TYPE_01) {
            itemView = LayoutInflater.from(mContext).inflate(R.layout.view_total_assets_top, viewGroup, false);
            return new ItemTopHolder(itemView);
        } else if (viewType == ITEM_TYPE_02) {
            itemView = LayoutInflater.from(mContext).inflate(R.layout.view_total_assets_middle, viewGroup, false);
            return new ItemMiddleHolder(itemView);
        } else {//news
            itemView = LayoutInflater.from(mContext).inflate(R.layout.item_transation_record_list, viewGroup, false);
            return new ItemHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position == 0) {
            ItemTopHolder topHolder = (ItemTopHolder) holder;
            topHolder.mTxtBalace.setText("" + SlinUtil.formatNumFromWeiS(mContext,mTotalMoney)
                    + " " + mContext.getResources().getString(R.string.wallet_manager_txt_money_unit_01));

            topHolder.mImgBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnBackListener.setClickBack();
                }
            });
        } else if (position > 1) {
            setDatas(holder, position, 2);
        }
    }

    private void setDatas(RecyclerView.ViewHolder holder, int position, int sub) {
        ItemHolder itemHolder = (ItemHolder) holder;
        final TranferRecordBean.TransferInfo entity = mDataList.get(position - sub);
        BigDecimal money = new BigDecimal("" + entity.getValue());
        String strMoney = SlinUtil.formatNumFromWeiS(mContext, money);
        if (TotalAssetsActivity.mDefultAddress.equals(entity.getFromAccount())) {//转出
            itemHolder.mTxtAddress.setText(StrUtil.addressFilter(entity.getToAccount()));
            itemHolder.mTxtMoney.setTextColor(mContext.getResources().getColor(R.color.color_FF4465));
            itemHolder.mTxtMoney.setText("-" + strMoney + mContext.getResources().getString(R.string.wallet_manager_txt_money_unit_01));
            itemHolder.mImgStyle.setBackgroundResource(R.mipmap.icon_transfer_record_out);
        } else {
            itemHolder.mTxtAddress.setText(StrUtil.addressFilter(entity.getFromAccount()));
            itemHolder.mTxtMoney.setTextColor(mContext.getResources().getColor(R.color.color_3FE77B));
            itemHolder.mTxtMoney.setText("+" + strMoney + mContext.getResources().getString(R.string.wallet_manager_txt_money_unit_01));
            itemHolder.mImgStyle.setBackgroundResource(R.mipmap.icon_transfer_record_in);
        }
        itemHolder.mTxtDate.setText(DateUtilSL.getTransferDate(mContext,entity.getTimestap(), 0));

        itemHolder.mLayoutBase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it_details = new Intent(mContext, TransferRecodeDetailsActivity.class);
                it_details.putExtra(TransferRecodeDetailsActivity.TRANSFER_RECODE_ADDRESS, TotalAssetsActivity.mDefultAddress);
                it_details.putExtra(TransferRecodeDetailsActivity.TRANSFER_RECODE_TYPE, DAppConstants.TYPE_HPB);
                it_details.putExtra(TransferRecodeDetailsActivity.TRANSFER_RECODE_DETAILS, entity);
                mContext.startActivity(it_details);
            }
        });

    }

    public interface OnBackListener {
        void setClickBack();
    }

    public void setOnBackListener(OnBackListener onBackListener) {
        this.mOnBackListener = onBackListener;
    }

    private OnBackListener mOnBackListener;


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
        private TextView mTxtBalace;
        private ImageView mImgBack;

        public ItemTopHolder(View itemView) {
            super(itemView);
            mImgBack = itemView.findViewById(R.id.view_total_assets_top_img_back);
            mTxtBalace = itemView.findViewById(R.id.view_total_assets_top_txt_money);
        }
    }

    public class ItemMiddleHolder extends RecyclerView.ViewHolder {
        public ItemMiddleHolder(View itemView) {
            super(itemView);

        }
    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        private TextView mTxtDate, mTxtAddress, mTxtMoney;
        private ImageView mImgStyle;
        private LinearLayout mLayoutBase;

        public ItemHolder(View itemView) {
            super(itemView);
            mTxtDate = itemView.findViewById(R.id.item_transfer_txt_time);
            mImgStyle = itemView.findViewById(R.id.item_transfer_img_biaoshi);
            mTxtAddress = itemView.findViewById(R.id.item_transfer_txt_address);
            mTxtMoney = itemView.findViewById(R.id.item_transfer_txt_money);
            mLayoutBase = itemView.findViewById(R.id.item_transfer_layout_base);
        }
    }
}
