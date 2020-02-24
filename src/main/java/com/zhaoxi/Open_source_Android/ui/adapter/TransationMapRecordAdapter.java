package com.zhaoxi.Open_source_Android.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhaoxi.Open_source_Android.DAppConstants;
import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.libs.utils.CollectionUtil;
import com.zhaoxi.Open_source_Android.libs.utils.DateUtilSL;
import com.zhaoxi.Open_source_Android.libs.utils.SlinUtil;
import com.zhaoxi.Open_source_Android.net.bean.TranferRecordBean;
import com.zhaoxi.Open_source_Android.ui.activity.TransationMapRecordActivity;
import com.zhaoxi.Open_source_Android.ui.activity.TransferRecodeDetailsActivity;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by zhutt on 2018/6/25.
 */
public class TransationMapRecordAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<TranferRecordBean.TransferInfo> mDataList;

    public TransationMapRecordAdapter(Context context, List<TranferRecordBean.TransferInfo> datas) {
        mContext = context;
        mDataList = datas;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_map_transation_record_list, parent, false);
        return new ItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final TranferRecordBean.TransferInfo entity = mDataList.get(position);
        ItemHolder itemHolder = (ItemHolder) holder;
        BigDecimal money = new BigDecimal("" + entity.getValue());
        String strMoney = SlinUtil.formatNumFromWeiS(mContext, money);

        float size = 16f;
        if (strMoney.length() <= 12) {
            size = 18f;
        } else if (strMoney.length() > 12 && strMoney.length() <= 16) {
            size = 16f;
        } else
            size = 14f;
        itemHolder.mTxtMoney.setTextSize(TypedValue.COMPLEX_UNIT_DIP, size);

        if (TransationMapRecordActivity.mChooseAddress.equals(entity.getFromAccount())) {//转出
            itemHolder.mTxtMoney.setTextColor(mContext.getResources().getColor(R.color.color_FF4465));
            itemHolder.mTxtMoney.setText("-" + strMoney);
        } else {
            itemHolder.mTxtMoney.setTextColor(mContext.getResources().getColor(R.color.color_3FE77B));
            itemHolder.mTxtMoney.setText("+" + strMoney);
        }
        itemHolder.mTxtType.setText(mContext.getResources().getString(R.string.activity_main_map_txt_05));

        itemHolder.mTxtDate.setText(DateUtilSL.getTransferDate(mContext, entity.getTimestap(), 0));

        itemHolder.mLayoutBase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it_details = new Intent(mContext, TransferRecodeDetailsActivity.class);
                it_details.putExtra(TransferRecodeDetailsActivity.TRANSFER_RECODE_ADDRESS, TransationMapRecordActivity.mChooseAddress);
                it_details.putExtra(TransferRecodeDetailsActivity.TRANSFER_RECODE_TYPE, DAppConstants.TYPE_HPB);
                it_details.putExtra(TransferRecodeDetailsActivity.TRANSFER_RECODE_DETAILS, entity);
                it_details.putExtra(TransferRecodeDetailsActivity.TRANSFER_RECODE_RESOUCE, 1);
                mContext.startActivity(it_details);
            }
        });
    }

    @Override
    public int getItemCount() {
        return CollectionUtil.isCollectionEmpty(mDataList) ? 0 : mDataList.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        private TextView mTxtType, mTxtDate, mTxtMoney;
        private LinearLayout mLayoutBase;

        public ItemHolder(View itemView) {
            super(itemView);
            mLayoutBase = itemView.findViewById(R.id.item_map_transfer_layout_base);
            mTxtType = itemView.findViewById(R.id.item_map_transfer_txt_type);
            mTxtDate = itemView.findViewById(R.id.item_map_transfer_txt_time);
            mTxtMoney = itemView.findViewById(R.id.item_map_transfer_txt_money);
        }
    }
}

