package com.zhaoxi.Open_source_Android.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhaoxi.Open_source_Android.DAppConstants;
import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.libs.utils.DateUtilSL;
import com.zhaoxi.Open_source_Android.libs.utils.SlinUtil;
import com.zhaoxi.Open_source_Android.libs.utils.StrUtil;
import com.zhaoxi.Open_source_Android.net.bean.TranferRecordBean;
import com.zhaoxi.Open_source_Android.ui.activity.TokenDetailActivity;

import java.math.BigDecimal;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * create by fangz
 * create date:2019/9/5
 * create time:7:58
 */
public class TokenDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<TranferRecordBean.TransferInfo> mDataList;
    private Context mContext;

    public TokenDetailAdapter(Context context,List<TranferRecordBean.TransferInfo> dataList) {
        this.mContext = context;
        this.mDataList = dataList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_token_detail_list_layout, parent, false);
        return new TokenItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        TokenItemViewHolder itemHolder = (TokenItemViewHolder) holder;
        final TranferRecordBean.TransferInfo entity = mDataList.get(position);

        itemHolder.mTokenFromAddress.setText(StrUtil.formatAddress(entity.getFromAccount()));
        itemHolder.mTokenToAddress.setText(StrUtil.formatAddress(entity.getToAccount()));

        String tokenSymbol = entity.getTokenSymbol();
        String value = entity.getValue();
        BigDecimal money = new BigDecimal(value);
        String strMoney = SlinUtil.formatNumFromWeiT(mContext,money,18);
        String tokenType = entity.getTokenType();

        if (TokenDetailActivity.mWalletAddress.equals(entity.getFromAccount())) {//转出
            itemHolder.mTokenDetailListName.setTextColor(mContext.getResources().getColor(R.color.color_E86A6A));
            itemHolder.mAssetsStatusHead.setImageResource(R.mipmap.icon_roll_out_small);
            if (DAppConstants.TYPE_HRC_20.equals(tokenType)){
                itemHolder.mTokenDetailListName.setText("-" + value + tokenSymbol);
            }else {
                itemHolder.mTokenDetailListName.setText("-" + strMoney + DAppConstants.TYPE_HPB);
            }
        } else {
            itemHolder.mTokenDetailListName.setTextColor(mContext.getResources().getColor(R.color.color_44C8A3));
            itemHolder.mAssetsStatusHead.setImageResource(R.mipmap.icon_into_small);
            if (DAppConstants.TYPE_HRC_20.equals(tokenType)){
                itemHolder.mTokenDetailListName.setText("+" + value + tokenSymbol);
            }else {
                itemHolder.mTokenDetailListName.setText("+" + strMoney + DAppConstants.TYPE_HPB);
            }
        }

        itemHolder.mTokenDetailListTime.setText(DateUtilSL.getTransferDate(mContext,entity.getTimestap(), 0));
        itemHolder.mTokenDetailContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null){
                    onItemClickListener.onItemClick(entity);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    class TokenItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_assets_status_head)
        ImageView mAssetsStatusHead;
        @BindView(R.id.item_main_token_detail_list_name)
        TextView mTokenDetailListName;
        @BindView(R.id.item_token_detail_list_time)
        TextView mTokenDetailListTime;
        @BindView(R.id.item_main_token_detail_list_from_address)
        TextView mTokenFromAddress;
        @BindView(R.id.item_main_token_detail_list_to_address)
        TextView mTokenToAddress;
        @BindView(R.id.item_assets_list_container)
        LinearLayout mTokenDetailContainer;

        public TokenItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        onItemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(TranferRecordBean.TransferInfo entity);
    }
}
