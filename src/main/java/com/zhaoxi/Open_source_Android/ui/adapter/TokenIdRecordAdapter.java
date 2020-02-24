package com.zhaoxi.Open_source_Android.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.libs.utils.ChangeLanguageUtil;
import com.zhaoxi.Open_source_Android.libs.utils.CollectionUtil;
import com.zhaoxi.Open_source_Android.libs.utils.DateUtilSL;
import com.zhaoxi.Open_source_Android.libs.utils.StrUtil;
import com.zhaoxi.Open_source_Android.net.bean.StockDetailBean;
import com.zhaoxi.Open_source_Android.net.bean.TokenIdDetailBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * create by fangz
 * create date:2019/9/5
 * create time:9:58
 */
public class TokenIdRecordAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEAD = 0x00;
    private static final int TYPE_ITEM = 0x01;

    private List<TokenIdDetailBean.TokenIdInfo> tokenIdInfos;
    private StockDetailBean.TokenInfo tokenInfo;
    private Context mContext;
    private int mLanguage;


    public TokenIdRecordAdapter(Context context, StockDetailBean.TokenInfo tokenInfo, List<TokenIdDetailBean.TokenIdInfo> tokenIdInfos) {
        this.tokenIdInfos = tokenIdInfos;
        this.mContext = context;
        this.tokenInfo = tokenInfo;
        mLanguage = ChangeLanguageUtil.languageType(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        if (viewType == TYPE_HEAD) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_token_id_head, parent, false);
            return new HeadViewHolder(itemView);
        } else {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_token_id_list_layout, parent, false);
            return new ItemViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeadViewHolder) {
            HeadViewHolder headViewHolder = (HeadViewHolder) holder;
            String url = tokenInfo.getTokenURI();
            if (!TextUtils.isEmpty(url)) {
                Glide.with(mContext)
                        .load(url)
                        .centerCrop()
                        .placeholder(R.mipmap.icon_default_header_large)
                        .error(R.mipmap.icon_default_header_large)
                        .into(headViewHolder.mTokenHeader);
            }

            if (CollectionUtil.isCollectionEmpty(tokenIdInfos)) {
                headViewHolder.mIvDefaultEmpty.setVisibility(View.VISIBLE);
                if (mLanguage == 1)
                    headViewHolder.mIvDefaultEmpty.setImageResource(R.drawable.icon_list_empty_zh);
                else
                    headViewHolder.mIvDefaultEmpty.setImageResource(R.drawable.icon_list_empty_en);
            } else {
                headViewHolder.mIvDefaultEmpty.setVisibility(View.GONE);
            }

            headViewHolder.mTokenId.setText(tokenInfo.getTokenId());
            headViewHolder.mTokenTransferCount.setText(String.valueOf(tokenInfo.getCount()));
            headViewHolder.mTokenHeader.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onCatchHeader(headViewHolder.mTokenHeader, url);
                    }

                }
            });

            headViewHolder.mCollection.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onCollection();
                    }
                }
            });

            headViewHolder.mTransfer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onTransfer();
                    }
                }
            });

        } else if (holder instanceof ItemViewHolder) {
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            TokenIdDetailBean.TokenIdInfo tokenIdInfo = tokenIdInfos.get(position - 1);
            itemViewHolder.mTokenTransferFromAddress.setText(StrUtil.formatAddress(tokenIdInfo.getFromAddress()));
            itemViewHolder.mTokenTransferToAddress.setText(StrUtil.formatAddress(tokenIdInfo.getToAddress()));
            itemViewHolder.mTokenTransferTime.setText(DateUtilSL.getTransferDate(mContext, tokenIdInfo.getBlockTimestamp(), 0));
            itemViewHolder.mTokenItemContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        listener.onItemClick(tokenIdInfo);
                    }
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        return CollectionUtil.isCollectionEmpty(tokenIdInfos) ? 1 : tokenIdInfos.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEAD;
        } else {
            return TYPE_ITEM;
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_token_id_list_time)
        TextView mTokenTransferTime;
        @BindView(R.id.item_main_token_detail_list_from_address)
        TextView mTokenTransferFromAddress;
        @BindView(R.id.item_main_token_detail_list_to_address)
        TextView mTokenTransferToAddress;
        @BindView(R.id.item_assets_list_container)
        LinearLayout mTokenItemContainer;

        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class HeadViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_token_id_header)
        CircleImageView mTokenHeader;
        @BindView(R.id.tv_token_id)
        TextView mTokenId;
        @BindView(R.id.tv_token_transfer_count)
        TextView mTokenTransferCount;
        @BindView(R.id.item_token_type)
        TextView mTokenType;
        @BindView(R.id.ll_token_id_collection)
        LinearLayout mCollection;
        @BindView(R.id.ll_token_id_transfer)
        LinearLayout mTransfer;
        @BindView(R.id.iv_default_empty)
        ImageView mIvDefaultEmpty;


        public HeadViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(TokenIdDetailBean.TokenIdInfo tokenIdInfo);

        void onCatchHeader(View view, String url);

        void onCollection();

        void onTransfer();
    }

}
