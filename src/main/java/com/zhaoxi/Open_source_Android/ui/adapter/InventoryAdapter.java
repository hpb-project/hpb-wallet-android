package com.zhaoxi.Open_source_Android.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.libs.tools.SystemLog;
import com.zhaoxi.Open_source_Android.net.bean.StockDetailBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * create by fangz
 * create date:2019/9/5
 * create time:9:58
 */
public class InventoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private List<StockDetailBean.TokenInfo> tokenInfos;
    private Context context;

    public InventoryAdapter(List<StockDetailBean.TokenInfo> tokenInfos, Context context) {
        this.tokenInfos = tokenInfos;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_inventory_list_layout, parent, false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        StockDetailBean.TokenInfo tokenInfo = tokenInfos.get(position);
        itemViewHolder.mTokenId.setText(tokenInfo.getTokenId());
        itemViewHolder.mTokenTransferCount.setText(String.valueOf(tokenInfo.getCount()));

        String tokenURI = tokenInfo.getTokenURI();
        SystemLog.D("onBindViewHolder", "tokenURI = " + tokenURI);
        if (!TextUtils.isEmpty(tokenURI)) {
            itemViewHolder.mTokenHeader.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .load(tokenURI)
                    .centerCrop()
                    .error(R.mipmap.icon_default_header)
                    .placeholder(R.mipmap.icon_default_header)
                    .into(itemViewHolder.mTokenHeader);

        }

        itemViewHolder.mContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(tokenInfo);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return tokenInfos.size();
    }


    class ItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.token_header)
        CircleImageView mTokenHeader;
        @BindView(R.id.tv_token_id)
        TextView mTokenId;
        @BindView(R.id.tv_token_transfer_count)
        TextView mTokenTransferCount;
        @BindView(R.id.item_assets_list_container)
        CardView mContainer;

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
        void onItemClick(StockDetailBean.TokenInfo tokenInfo);
    }
}
