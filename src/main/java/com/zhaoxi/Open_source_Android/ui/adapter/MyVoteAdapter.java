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
import com.zhaoxi.Open_source_Android.libs.utils.DateUtilSL;
import com.zhaoxi.Open_source_Android.libs.utils.SlinUtil;
import com.zhaoxi.Open_source_Android.net.bean.VoteBean;
import com.zhaoxi.Open_source_Android.ui.activity.VoteDetailsActivity;
import com.zhaoxi.Open_source_Android.web3.utils.Convert;

import java.math.BigDecimal;
import java.util.List;

/**
 * des:
 * Created by ztt on 2018/6/6.
 */

public class MyVoteAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int ITEM_TYPE_01 = 1;
    private static final int ITEM_TYPE_02 = 2;
    private static final int ITEM_TYPE_03 = 3;

    private Context mContext;
    private List<VoteBean.VoteInfo> mDataList;
    private VoteBean.MyhistoryVote mYVote;
    private boolean mIsFirst;

    public MyVoteAdapter(Context context, List<VoteBean.VoteInfo> listData,
                         VoteBean.MyhistoryVote myVote,boolean isFisrt) {
        this.mContext = context;
        this.mDataList = listData;
        this.mYVote = myVote;
        this.mIsFirst = isFisrt;
    }

    public void setTopData(VoteBean.MyhistoryVote myVote,boolean isFisrt) {
        this.mYVote = myVote;
        this.mIsFirst = isFisrt;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView;
        if (viewType == ITEM_TYPE_01) {
            itemView = LayoutInflater.from(mContext).inflate(R.layout.view_my_vote_info_top, viewGroup, false);
            return new ItemTopHolder(itemView);
        } else if (viewType == ITEM_TYPE_02) {
            itemView = LayoutInflater.from(mContext).inflate(R.layout.view_my_vote_info_middle, viewGroup, false);
            return new ItemMiddleHolder(itemView);
        } else {//news
            itemView = LayoutInflater.from(mContext).inflate(R.layout.view_my_vote_info_item, viewGroup, false);
            return new ItemHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position == 0) {
            if (mYVote != null) {
                ItemTopHolder topHolder = (ItemTopHolder) holder;
                BigDecimal mBalance = Convert.fromWei(mYVote.getAviliable(), Convert.Unit.ETHER);
                topHolder.mTxtBalace.setText("" + SlinUtil.NumberFormat0(mContext, SlinUtil.ValueScale(mBalance, 0)));
                BigDecimal mHistory = Convert.fromWei(mYVote.getTotalVoteNum(), Convert.Unit.ETHER);
                topHolder.mTxtHistotyPoll.setText("" + SlinUtil.NumberFormat0(mContext, SlinUtil.ValueScale(mHistory, 0)));
            }
        }else if(position ==1){
            ItemMiddleHolder middleHolder = (ItemMiddleHolder) holder;
            if(mIsFirst){
                middleHolder.mTxtEmpty.setVisibility(View.GONE);
                return;
            }
            if(mDataList.size() == 0){
                middleHolder.mTxtEmpty.setVisibility(View.VISIBLE);
            }else{
                middleHolder.mTxtEmpty.setVisibility(View.GONE);
            }
        } else if (position > 1) {
            setDatas(holder, position, 2);
        }
    }

    private void setDatas(RecyclerView.ViewHolder holder, int position, int sub) {
        ItemHolder itemHolder = (ItemHolder) holder;
        VoteBean.VoteInfo voteInfo = mDataList.get(position - sub);
        itemHolder.mTxtName.setText(voteInfo.getName());
        BigDecimal mPoll1 = Convert.fromWei(voteInfo.getCountNum(), Convert.Unit.ETHER);
        itemHolder.mTxtCurAllPoll.setText("" + SlinUtil.NumberFormat0(mContext, SlinUtil.ValueScale(mPoll1, 0)));
        BigDecimal mPoll2 = Convert.fromWei(voteInfo.getVoteNum(), Convert.Unit.ETHER);
        itemHolder.mTxtMyVotePoll.setText("" + SlinUtil.NumberFormat0(mContext, SlinUtil.ValueScale(mPoll2, 0)));
        itemHolder.mTxtVoteTime.setText(DateUtilSL.dateToStrymdhms(DateUtilSL.getDateByLong(voteInfo.getTimestap(), 1)));
        itemHolder.mItemLayoutBase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it_deatisl = new Intent(mContext, VoteDetailsActivity.class);
                it_deatisl.putExtra(VoteDetailsActivity.VOTE_DETAILS, voteInfo);
                mContext.startActivity(it_deatisl);
            }
        });

        //0：已撤销  1:已投票
        switch (voteInfo.getState()) {
            case "0":
                itemHolder.mTextStatus.setText(mContext.getResources().getString(R.string.activity_vote_txt_29));
                break;
            case "1":
                itemHolder.mTextStatus.setText(mContext.getResources().getString(R.string.activity_voted_txt_29));
                break;
        }

    }

    public interface OnCexiaoListener {
        void setClick(String blockHash, int poll, String toAddress);
    }

    public void setOnCexiaoListener(OnCexiaoListener mOnCexiaoListener) {
        this.mOnCexiaoListener = mOnCexiaoListener;
    }

    private OnCexiaoListener mOnCexiaoListener;


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
        private TextView mTxtBalace, mTxtHistotyPoll;

        public ItemTopHolder(View itemView) {
            super(itemView);
            mTxtBalace = itemView.findViewById(R.id.view_my_vote_top_01);
            mTxtHistotyPoll = itemView.findViewById(R.id.view_my_vote_top_02);
        }
    }

    public class ItemMiddleHolder extends RecyclerView.ViewHolder {
        private TextView mTxtEmpty;
        public ItemMiddleHolder(View itemView) {
            super(itemView);
            mTxtEmpty = itemView.findViewById(R.id.view_my_vote_data_empty);
        }
    }

    public class ItemHolder extends RecyclerView.ViewHolder {

        private LinearLayout mItemLayoutBase;
        private TextView mTxtName;
        private TextView mTxtCurAllPoll;
        private TextView mTxtMyVotePoll;
        private TextView mTxtVoteTime;
        private TextView mTextStatus;

        public ItemHolder(View itemView) {
            super(itemView);
            mItemLayoutBase = itemView.findViewById(R.id.vote_item_layout_base);
            mTextStatus = itemView.findViewById(R.id.view_my_vote_data_status);
            mTxtName = itemView.findViewById(R.id.view_my_vote_data_01);
            mTxtCurAllPoll = itemView.findViewById(R.id.view_my_vote_data_02);
            mTxtMyVotePoll = itemView.findViewById(R.id.view_my_vote_data_03);
            mTxtVoteTime = itemView.findViewById(R.id.view_my_vote_data_04);
        }
    }
}
