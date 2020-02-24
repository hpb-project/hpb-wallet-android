package com.zhaoxi.Open_source_Android.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.libs.utils.CollectionUtil;
import com.zhaoxi.Open_source_Android.libs.utils.SlinUtil;
import com.zhaoxi.Open_source_Android.libs.utils.StrUtil;
import com.zhaoxi.Open_source_Android.net.bean.NodeDividenBean;
import com.zhaoxi.Open_source_Android.web3.utils.Convert;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * des:
 * Created by ztt on 2018/6/6.
 */

public class NoteVoteDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int ITEM_TYPE_01 = 1;
    private static final int ITEM_TYPE_02 = 2;

    private Context mContext;
    private NodeDividenBean mNodeDividendBean;
    private List<NodeDividenBean.VoteDetailsBean> mDataList = new ArrayList<>();

    public NoteVoteDetailsAdapter(Context context, NodeDividenBean nodeDividendBean, List<NodeDividenBean.VoteDetailsBean> dataList) {
        this.mContext = context;
        this.mNodeDividendBean = nodeDividendBean;
        this.mDataList = dataList;
    }


    public void setTopData(NodeDividenBean nodeDividendBean) {
        this.mNodeDividendBean = nodeDividendBean;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView;
        if (viewType == ITEM_TYPE_01) {
            itemView = LayoutInflater.from(mContext).inflate(R.layout.item_node_vote_details_top, viewGroup, false);
            return new ItemTopHolder(itemView);
        } else {//news
            itemView = LayoutInflater.from(mContext).inflate(R.layout.view_node_vote_details_item, viewGroup, false);
            return new ItemRankHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position == 0) {
            ItemTopHolder itemTopHolder = (ItemTopHolder) holder;
            itemTopHolder.mBtnSendFH.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnReadListenner.onSendDividendlistenner();
                }
            });
            itemTopHolder.mBtnExprotData.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnReadListenner.onExprotDatalistenner();
                }
            });
            itemTopHolder.mTxtAllperson.setText(SlinUtil.NumberFormat0(mContext, new BigDecimal(mNodeDividendBean.getVotersNum())));
            BigDecimal votejfNum = Convert.fromWei(new BigDecimal(mNodeDividendBean.getPoolsNum()), Convert.Unit.ETHER);
            itemTopHolder.mTxtAllPoll.setText(SlinUtil.NumberFormat0(mContext, SlinUtil.ValueScale(votejfNum,0)));
        } else {
            setNewsData(holder, position, 1);
        }
    }

    private void setNewsData(RecyclerView.ViewHolder holder, int position, int reduce) {
        ItemRankHolder itemHolder = (ItemRankHolder) holder;
        NodeDividenBean.VoteDetailsBean nodeInfo = mDataList.get(position - reduce);
        switch (position - reduce) {
            case 0:
                itemHolder.mTxtRank.setBackgroundResource(R.mipmap.icon_node_vote_rank_01);
                itemHolder.mTxtRank.setText("");
                break;
            case 1:
                itemHolder.mTxtRank.setBackgroundResource(R.mipmap.icon_node_vote_rank_02);
                itemHolder.mTxtRank.setText("");
                break;
            case 2:
                itemHolder.mTxtRank.setBackgroundResource(R.mipmap.icon_node_vote_rank_03);
                itemHolder.mTxtRank.setText("");
                break;
            default:
                itemHolder.mTxtRank.setBackgroundColor(Color.argb(0, 0, 0, 0));
                itemHolder.mTxtRank.setText("" + (position - reduce));
                break;
        }

        itemHolder.mTxtAddress.setText(StrUtil.addressAlign(nodeInfo.getAddress()));
        BigDecimal votejfNum = Convert.fromWei(new BigDecimal(nodeInfo.getVoteNum()), Convert.Unit.ETHER);
        itemHolder.mTxtPollNum.setText(SlinUtil.NumberFormat0(mContext, SlinUtil.ValueScale(votejfNum,0)));
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {//第一行
            return ITEM_TYPE_01;
        } else {
            return ITEM_TYPE_02;
        }
    }

    @Override
    public int getItemCount() {
        int count = 1;
        count = CollectionUtil.isCollectionEmpty(mDataList) ? 1 : mDataList.size() + 1;
        return count;
    }

    /**
     * top
     */
    public class ItemTopHolder extends RecyclerView.ViewHolder {
        private TextView mTxtAllperson, mTxtAllPoll;
        private Button mBtnSendFH, mBtnExprotData;
        private LinearLayout mLayoutWhat;

        public ItemTopHolder(View itemView) {
            super(itemView);
            mTxtAllperson = itemView.findViewById(R.id.item_node_vote_details_txt_allperson);
            mTxtAllPoll = itemView.findViewById(R.id.item_node_vote_details_txt_allpoll);
            mBtnSendFH = itemView.findViewById(R.id.item_node_vote_details_btn_sendfh);
            mBtnExprotData = itemView.findViewById(R.id.item_node_vote_details_btn_exprot);
        }
    }

    /**
     * rank
     */
    public class ItemRankHolder extends RecyclerView.ViewHolder {
        private TextView mTxtRank;
        private TextView mTxtAddress, mTxtPollNum;

        public ItemRankHolder(View itemView) {
            super(itemView);
            mTxtRank = itemView.findViewById(R.id.txt_node_vote_item_rank);
            mTxtAddress = itemView.findViewById(R.id.txt_node_vote_item_address);
            mTxtPollNum = itemView.findViewById(R.id.txt_node_vote_item_poll);
        }
    }

    public interface OnReadListenner {
        void onSendDividendlistenner();

        void onExprotDatalistenner();
    }

    public void setOnReadListenner(OnReadListenner mOnReadListenner) {
        this.mOnReadListenner = mOnReadListenner;
    }

    private OnReadListenner mOnReadListenner;

}
