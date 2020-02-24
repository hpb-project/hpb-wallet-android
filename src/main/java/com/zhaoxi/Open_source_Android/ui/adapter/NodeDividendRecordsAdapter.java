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
import com.zhaoxi.Open_source_Android.libs.utils.DateUtilSL;
import com.zhaoxi.Open_source_Android.libs.utils.SlinUtil;
import com.zhaoxi.Open_source_Android.net.bean.DividendRecordsBean;
import com.zhaoxi.Open_source_Android.ui.activity.NodeDividendRecordsDetailsActivity;
import com.zhaoxi.Open_source_Android.web3.utils.Convert;

import java.math.BigDecimal;
import java.util.List;

/**
 * des:
 * Created by ztt on 2018/6/7.
 */
public class NodeDividendRecordsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<DividendRecordsBean.RecordsBean> mData;

    public NodeDividendRecordsAdapter(Context context, List<DividendRecordsBean.RecordsBean> data) {
        this.mContext = context;
        this.mData = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_nodedividend_records_list, viewGroup, false);
        return new ItemHodler(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        DividendRecordsBean.RecordsBean bean = mData.get(position);
        ItemHodler itemHodler = (ItemHodler) holder;
        BigDecimal money = Convert.fromWei(new BigDecimal(bean.getBonusValue()), Convert.Unit.ETHER);
        itemHodler.mTxtMoney.setText(SlinUtil.tailClearAll(mContext,SlinUtil.NumberFormat8(mContext, money)) + " HPB");
        itemHodler.mTxtTime.setText(DateUtilSL.dateToStrymdhm2(DateUtilSL.getDateByLong(bean.getTradeTime(), 2)));

        itemHodler.mLayoutDetials.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it_details = new Intent(mContext, NodeDividendRecordsDetailsActivity.class);
                it_details.putExtra(NodeDividendRecordsDetailsActivity.NODE_RECORDS_ID, bean.getId());
                it_details.putExtra(NodeDividendRecordsDetailsActivity.NODE_RECORDS_COINUNIT, bean.getCoinSymbol());
                it_details.putExtra(NodeDividendRecordsDetailsActivity.NODE_RECORDS_HAX, bean.getTxHash());
                mContext.startActivity(it_details);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    private class ItemHodler extends RecyclerView.ViewHolder {
        private LinearLayout mLayoutDetials;
        private TextView mTxtTime, mTxtMoney;

        public ItemHodler(View itemView) {
            super(itemView);
            mTxtTime = itemView.findViewById(R.id.item_node_records_txt_time);
            mTxtMoney = itemView.findViewById(R.id.item_node_records_text_money);
            mLayoutDetials = itemView.findViewById(R.id.item_node_records_layout_details);
        }
    }
}
