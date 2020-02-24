package com.zhaoxi.Open_source_Android.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.base.BaseActivity;
import com.zhaoxi.Open_source_Android.libs.utils.CollectionUtil;
import com.zhaoxi.Open_source_Android.net.bean.TupCoinBean;

import java.util.List;

/**
 * des:交易记录 代币选择
 * Created by ztt on 2018/6/14.
 */

public class ChooseRecordsDbAdapter extends BaseAdapter {
    private BaseActivity mContext;
    private List<TupCoinBean.TupBean> mListData;

    public ChooseRecordsDbAdapter(BaseActivity context, List<TupCoinBean.TupBean> listData) {
        mContext = context;
        mListData = listData;
    }

    @Override
    public int getCount() {
        return CollectionUtil.isCollectionEmpty(mListData) ? 0 : mListData.size();
    }

    @Override
    public Object getItem(int position) {
        return mListData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ItemHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_choose_transfer_records_list, null);
            holder = new ItemHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ItemHolder) convertView.getTag();
        }

        TupCoinBean.TupBean tupBean = mListData.get(position);

        holder.mTxtName.setText(tupBean.getTokenSymbol());

        return convertView;
    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        private TextView mTxtName;

        public ItemHolder(View itemView) {
            super(itemView);
            mTxtName = itemView.findViewById(R.id.item_choose_transfer_records_db_name);
        }
    }
}
