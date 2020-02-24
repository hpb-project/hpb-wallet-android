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

import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;
import com.zhaoxi.Open_source_Android.DAppConstants;
import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.libs.utils.CollectionUtil;
import com.zhaoxi.Open_source_Android.libs.utils.DateUtilSL;
import com.zhaoxi.Open_source_Android.libs.utils.SlinUtil;
import com.zhaoxi.Open_source_Android.libs.utils.StrUtil;
import com.zhaoxi.Open_source_Android.net.bean.TranferRecordBean;
import com.zhaoxi.Open_source_Android.ui.activity.TransationRecordActivity;
import com.zhaoxi.Open_source_Android.ui.activity.TransferRecodeDetailsActivity;

import java.math.BigDecimal;
import java.util.List;

/**
 * 业绩管理列表
 * Created by zhutt on 2018/6/25.
 */
public class TransationRecordAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements StickyRecyclerHeadersAdapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<TranferRecordBean.TransferInfo> mDataList;

    public TransationRecordAdapter(Context context, List<TranferRecordBean.TransferInfo> datas) {
        mContext = context;
        mDataList = datas;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_transation_record_list, parent, false);
        return new ItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final TranferRecordBean.TransferInfo entity = mDataList.get(position);
        ItemHolder itemHolder = (ItemHolder) holder;
        BigDecimal money = new BigDecimal("" + entity.getValue());
        String strMoney = SlinUtil.formatNumFromWeiT(mContext, money, 18);

        String transferType = "-";
        if (TransationRecordActivity.mChooseAddress.equals(entity.getFromAccount())) {//转出
            itemHolder.mTxtAddress.setText(StrUtil.addressFilter(entity.getToAccount()));
            itemHolder.mTxtMoney.setTextColor(mContext.getResources().getColor(R.color.color_E86A6A));
            transferType = "-";
            itemHolder.mImgStyle.setBackgroundResource(R.mipmap.icon_transfer_record_out);
        } else {
            itemHolder.mTxtAddress.setText(StrUtil.addressFilter(entity.getFromAccount()));
            itemHolder.mTxtMoney.setTextColor(mContext.getResources().getColor(R.color.color_44C8A3));
            transferType = "+";
            itemHolder.mImgStyle.setBackgroundResource(R.mipmap.icon_transfer_record_in);
        }
        String type_coin = DAppConstants.TYPE_HPB;
        if (StrUtil.isEmpty(entity.getTokenType())) {
            itemHolder.mTxtCoinType.setVisibility(View.GONE);
            type_coin = DAppConstants.TYPE_HPB;
            itemHolder.mTxtMoney.setText(transferType + strMoney +" "+ mContext.getResources().getString(R.string.wallet_manager_txt_money_unit_01));
        } else {
            if (DAppConstants.TYPE_HRC_721.equals(entity.getTokenType())) {
                itemHolder.mTxtMoney.setText(mContext.getResources().getString(R.string.fragment_token_id_02) + entity.getTokenId());
            } else if (DAppConstants.TYPE_HRC_20.equals(entity.getTokenType())) {
                itemHolder.mTxtMoney.setText(transferType + entity.getValue() + " " + entity.getTokenSymbol());
            } else {
                itemHolder.mTxtMoney.setText(transferType + SlinUtil.formatNumFromWeiT(mContext, new BigDecimal(entity.getValue()), 18) + " " + entity.getTokenSymbol());
            }
            itemHolder.mTxtCoinType.setText(entity.getTokenType());
            itemHolder.mTxtCoinType.setVisibility(View.VISIBLE);
            type_coin = entity.getTokenType();
        }

        itemHolder.mTxtDate.setText(DateUtilSL.getTransferDate(mContext, entity.getTimestap(), 0));

        String fin = type_coin;
        itemHolder.mLayoutBase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it_details = new Intent(mContext, TransferRecodeDetailsActivity.class);
                it_details.putExtra(TransferRecodeDetailsActivity.TRANSFER_RECODE_ADDRESS, TransationRecordActivity.mChooseAddress);
                it_details.putExtra(TransferRecodeDetailsActivity.TRANSFER_RECODE_TYPE, fin);
                it_details.putExtra(TransferRecodeDetailsActivity.TRANSFER_RECODE_DETAILS, entity);
                mContext.startActivity(it_details);
            }
        });
    }

    @Override
    public int getItemCount() {
        return CollectionUtil.isCollectionEmpty(mDataList) ? 0 : mDataList.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        private TextView mTxtDate, mTxtAddress, mTxtMoney;
        private ImageView mImgStyle;
        private LinearLayout mLayoutBase;
        private TextView mTxtCoinType;

        public ItemHolder(View itemView) {
            super(itemView);
            mLayoutBase = itemView.findViewById(R.id.item_transfer_layout_base);
            mTxtDate = itemView.findViewById(R.id.item_transfer_txt_time);
            mImgStyle = itemView.findViewById(R.id.item_transfer_img_biaoshi);
            mTxtAddress = itemView.findViewById(R.id.item_transfer_txt_address);
            mTxtMoney = itemView.findViewById(R.id.item_transfer_txt_money);
            mLayoutBase = itemView.findViewById(R.id.item_transfer_layout_base);
            mTxtCoinType = itemView.findViewById(R.id.item_transfer_txt_cointype);

        }
    }

    /*固定头部*/
    @Override
    public long getHeaderId(int position) {
        return getSortType(position);
    }

    //获取当前球队的类型
    public int getSortType(int position) {
        TranferRecordBean.TransferInfo entity = mDataList.get(position);
        return Integer.valueOf(DateUtilSL.dateToStr(DateUtilSL.getDateByLong(entity.getTimestap(), 0)).replace("-", ""));
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup viewGroup) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_transation_record_list_time, viewGroup, false);
        return new HeaderHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        final TranferRecordBean.TransferInfo entity = mDataList.get(i);
        HeaderHolder viewHolder1 = (HeaderHolder) viewHolder;
        String date = DateUtilSL.dateToStr(DateUtilSL.getDateByLong(entity.getTimestap(), 0));
        if (date.equals(DateUtilSL.getCurrentDate(1))) {
            date = mContext.getResources().getString(R.string.item_tansfer_recode_txt_01);
        }
        viewHolder1.headerTime.setText(date);
    }

    public class HeaderHolder extends RecyclerView.ViewHolder {
        public TextView headerTime;

        public HeaderHolder(View itemView) {
            super(itemView);
            headerTime = (TextView) itemView.findViewById(R.id.transation_record_list_time);
        }
    }
}

