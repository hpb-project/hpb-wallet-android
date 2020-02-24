package com.zhaoxi.Open_source_Android.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.net.bean.TokenIdBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * create by fangz
 * create date:2019/9/5
 * create time:9:58
 */
public class TokenIdAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<TokenIdBean> tokenIdBeans;
    private Context mContext;
    private CheckBox mLastCheckBox;

    public TokenIdAdapter(Context context, List<TokenIdBean> tokenIdBeans) {
        this.tokenIdBeans = tokenIdBeans;
        this.mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_select_token_id_layout, parent, false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        TokenIdBean tokenIdBean = tokenIdBeans.get(position);
        itemViewHolder.mTvTokenId.setText(tokenIdBean.getTokenId());
        itemViewHolder.mTokenSortNumber.setText(String.valueOf(position + 1));
        itemViewHolder.mSelectTokenIdCheckBox.setChecked(tokenIdBean.isCheck());

        if (TextUtils.isEmpty(tokenIdBean.getHeadPath())) {
            itemViewHolder.mIvTokenHeader.setVisibility(View.GONE);
        } else {
            Glide.with(mContext).
                    load(tokenIdBean.getHeadPath().trim())
                    .centerCrop()
                    .error(R.mipmap.icon_default_header)
                    .placeholder(R.mipmap.icon_default_header)
                    .into(itemViewHolder.mIvTokenHeader);
        }


        itemViewHolder.mSelectTokenIdCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                tokenIdBean.setCheck(isChecked);
                if (listener != null) {
                    listener.onItemClick(tokenIdBean);
                }

                // 实现单选
                if (mLastCheckBox != null) {
                    mLastCheckBox.setChecked(false);
                }
                // 记录上次选择的checkbox
                mLastCheckBox = itemViewHolder.mSelectTokenIdCheckBox;

            }
        });
    }

    @Override
    public int getItemCount() {
        return tokenIdBeans.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_token_id)
        TextView mTvTokenId;
        @BindView(R.id.tv_token_sort_number)
        TextView mTokenSortNumber;
        @BindView(R.id.iv_token_header)
        CircleImageView mIvTokenHeader;
        @BindView(R.id.select_token_id_check_box)
        CheckBox mSelectTokenIdCheckBox;

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
        void onItemClick(TokenIdBean tokenIdBean);
    }
}
