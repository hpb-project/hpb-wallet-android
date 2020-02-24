package com.zhaoxi.Open_source_Android.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.net.bean.TokenIdMoreBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * create by fangz
 * create date:2019/9/5
 * create time:9:58
 */
public class TokenMoreAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<TokenIdMoreBean.TokenIdInfo> tokenIdInfos;
    private Context mContext;

    public TokenMoreAdapter(Context context, List<TokenIdMoreBean.TokenIdInfo> tokenIdInfos) {
        this.tokenIdInfos = tokenIdInfos;
        this.mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_token_id_layout, parent, false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        TokenIdMoreBean.TokenIdInfo tokenIdInfo = tokenIdInfos.get(position);
        itemViewHolder.mTvTokenId.setText(tokenIdInfo.getTokenId());

        if (TextUtils.isEmpty(tokenIdInfo.getTokenURI())) {
            itemViewHolder.mIvTokenHeader.setVisibility(View.GONE);
        } else {
            Glide.with(mContext).
                    load(tokenIdInfo.getTokenURI())
                    .centerCrop()
                    .error(R.mipmap.icon_default_header)
                    .placeholder(R.mipmap.icon_default_header)
                    .into(itemViewHolder.mIvTokenHeader);

        }

    }

    @Override
    public int getItemCount() {
        return tokenIdInfos.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_token_id)
        TextView mTvTokenId;
        @BindView(R.id.iv_token_header)
        CircleImageView mIvTokenHeader;

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
        void onItemClick(int position);
    }
}
