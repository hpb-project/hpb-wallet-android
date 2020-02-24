package com.zhaoxi.Open_source_Android.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.libs.utils.CollectionUtil;
import com.zhaoxi.Open_source_Android.libs.utils.SlinUtil;
import com.zhaoxi.Open_source_Android.net.bean.VoteBean;
import com.zhaoxi.Open_source_Android.ui.activity.VoteDetailsActivity;
import com.zhaoxi.Open_source_Android.web3.utils.Convert;

import java.math.BigDecimal;
import java.util.List;

/**
 * 投票结果
 * Created by zhutt on 2018/6/25.
 */
public class VoteQueryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<VoteBean.VoteInfo> mDataList;

    public VoteQueryAdapter(Context context, List<VoteBean.VoteInfo> datas) {
        mContext = context;
        mDataList = datas;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.view_vote_info_item, parent, false);
        return new ItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final VoteBean.VoteInfo voteInfo = mDataList.get(position);
        ItemHolder itemHolder = (ItemHolder) holder;
        itemHolder.mTxtRank.setText(voteInfo.getRank());
        itemHolder.mTxtName.setText(voteInfo.getName());
        BigDecimal money = Convert.fromWei(voteInfo.getCount(), Convert.Unit.ETHER);
        itemHolder.mTxtPoll.setText("" + SlinUtil.NumberFormat0(mContext, SlinUtil.ValueScale(money, 0)));
        if (position % 2 == 0) {
            itemHolder.mLayoutBase.setBackgroundColor(mContext.getResources().getColor(R.color.color_F9FAFF));
        } else {
            itemHolder.mLayoutBase.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        }
        itemHolder.mLayoutBase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it_deatisl = new Intent(mContext, VoteDetailsActivity.class);
                it_deatisl.putExtra(VoteDetailsActivity.VOTE_DETAILS, voteInfo);
                mContext.startActivity(it_deatisl);
            }
        });

    }

    @Override
    public int getItemCount() {
        return CollectionUtil.isCollectionEmpty(mDataList) ? 0 : mDataList.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        private TextView mTxtRank;
        private TextView mTxtName;
        private TextView mTxtPoll;
        private TextView mTxtVote;
        private LinearLayout mLayoutBase;

        public ItemHolder(View itemView) {
            super(itemView);
            mTxtRank = itemView.findViewById(R.id.txt_vote_item_rank);
            mTxtName = itemView.findViewById(R.id.txt_vote_item_name);
            mTxtVote = itemView.findViewById(R.id.txt_vote_item_vote);
            mTxtPoll = itemView.findViewById(R.id.txt_vote_item_poll);
            mLayoutBase = itemView.findViewById(R.id.vote_item_layout_base);
        }
    }
}

