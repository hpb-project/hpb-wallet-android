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

import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.libs.utils.DateUtilSL;
import com.zhaoxi.Open_source_Android.libs.utils.StrUtil;
import com.zhaoxi.Open_source_Android.net.bean.TranferRecordBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * create by fangz
 * create date:2019/9/5
 * create time:9:58
 */
public class TransRecord721Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private Context context;
    private List<TranferRecordBean.TransferInfo> mDataList;

    public TransRecord721Adapter(Context context, List<TranferRecordBean.TransferInfo> mDataList) {
        this.context = context;
        this.mDataList = mDataList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_token_trans_reocrd_721_list_layout, parent, false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ItemViewHolder itemHolder = (ItemViewHolder) holder;

        final TranferRecordBean.TransferInfo entity = mDataList.get(position);

        if ("0".equals(entity.getFlag())) {//转出
            itemHolder.mAssetsStatusHead.setImageResource(R.mipmap.icon_roll_out_small);
        } else {
            itemHolder.mAssetsStatusHead.setImageResource(R.mipmap.icon_into_small);
        }

        itemHolder.mTokenFromAddress.setText(StrUtil.formatAddress(entity.getFromAccount()));
        itemHolder.mTokenToAddress.setText(StrUtil.formatAddress(entity.getToAccount()));
        itemHolder.mTokenId.setText(String.valueOf(entity.getTokenId()));
        itemHolder.mTokenDetailListTime.setText(DateUtilSL.getTransferDate(context, entity.getTimestap(), 0));

        itemHolder.mTokenDetailContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(entity);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_assets_status_head)
        ImageView mAssetsStatusHead;
        @BindView(R.id.item_token_id)
        TextView mTokenId;
        @BindView(R.id.item_token_detail_list_time)
        TextView mTokenDetailListTime;
        @BindView(R.id.item_main_token_detail_list_from_address)
        TextView mTokenFromAddress;
        @BindView(R.id.item_main_token_detail_list_to_address)
        TextView mTokenToAddress;
        @BindView(R.id.item_assets_list_container)
        LinearLayout mTokenDetailContainer;

        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(TranferRecordBean.TransferInfo transInfo);
    }
}
