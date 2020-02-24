package com.zhaoxi.Open_source_Android.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.libs.utils.SlinUtil;
import com.zhaoxi.Open_source_Android.net.bean.DividendRecordsBean;
import com.zhaoxi.Open_source_Android.web3.utils.Convert;

import java.math.BigDecimal;
import java.util.List;

/**
 * des:
 * Created by ztt on 2018/6/7.
 */
public class NodeDividendRecordsDetilasAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<DividendRecordsBean.RecordsBean> mData;
    private String mCoinSymbol;

    public NodeDividendRecordsDetilasAdapter(Context context, List<DividendRecordsBean.RecordsBean> data,String coinUnit) {
        this.mContext = context;
        this.mData = data;
        this.mCoinSymbol = coinUnit;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_nodedividend_records_detaisl_list, viewGroup, false);
        return new ItemHodler(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        DividendRecordsBean.RecordsBean bean = mData.get(position);
        ItemHodler itemHodler = (ItemHodler) holder;
        itemHodler.mTxtRank.setText("" + (position + 1));
        BigDecimal money = Convert.fromWei(new BigDecimal(bean.getScore()), Convert.Unit.ETHER);//积分
        itemHodler.mTxtPollNum.setText(""+SlinUtil.tailClearAll(mContext,SlinUtil.NumberFormat8(mContext, money)));
        itemHodler.mTxtAddress.setText(bean.getVoterAddr());
        BigDecimal poll = Convert.fromWei(new BigDecimal(bean.getBonus()), Convert.Unit.ETHER);
        itemHodler.mTxtDividend.setText(SlinUtil.tailClearAll(mContext,SlinUtil.NumberFormat8(mContext, poll))+ " HPB");
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    private class ItemHodler extends RecyclerView.ViewHolder {
        private TextView mTxtRank, mTxtPollNum, mTxtAddress, mTxtDividend;

        public ItemHodler(View itemView) {
            super(itemView);
            mTxtRank = itemView.findViewById(R.id.item_node_records_details_txt_rank);
            mTxtPollNum = itemView.findViewById(R.id.item_node_records_details_txt_pollnum);
            mTxtAddress = itemView.findViewById(R.id.item_node_records_details_txt_address);
            mTxtDividend = itemView.findViewById(R.id.item_node_records_details_txt_dividend);
        }
    }
}
