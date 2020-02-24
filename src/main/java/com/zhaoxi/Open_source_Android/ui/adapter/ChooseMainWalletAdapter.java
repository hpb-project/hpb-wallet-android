package com.zhaoxi.Open_source_Android.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhaoxi.Open_source_Android.DAppConstants;
import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.base.BaseActivity;
import com.zhaoxi.Open_source_Android.libs.utils.CollectionUtil;
import com.zhaoxi.Open_source_Android.libs.utils.SharedPreferencesUtil;
import com.zhaoxi.Open_source_Android.libs.utils.SlinUtil;
import com.zhaoxi.Open_source_Android.libs.utils.StrUtil;
import com.zhaoxi.Open_source_Android.web3.bean.WalletBean;

import java.math.BigDecimal;
import java.util.List;

/**
 * des:
 * Created by ztt on 2018/6/14.
 */

public class ChooseMainWalletAdapter extends BaseAdapter {
    private BaseActivity mContext;
    private List<WalletBean> mListData;
    private int mEyesStatus;

    public ChooseMainWalletAdapter(BaseActivity context, List<WalletBean> listData) {
        mContext = context;
        mListData = listData;
        mEyesStatus = SharedPreferencesUtil.getSharePreInt(mContext, DAppConstants.MONEY_EYES_STATUS);
    }

    public void setEyesStatus(int eyesStatus) {
        mEyesStatus = eyesStatus;
        notifyDataSetChanged();
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
        WalletBean walletBean = mListData.get(position);
        ItemHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_choose_main_wallet_list, null);
            holder = new ItemHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ItemHolder) convertView.getTag();
        }

        holder.mTxtName.setTextColor(mContext.getResources().getColor(R.color.color_252735));
        holder.mTxtMoney.setTextColor(mContext.getResources().getColor(R.color.color_252735));
        if (walletBean.isChooseWallet()) {
            holder.mBaseLayout.setBackgroundColor(mContext.getResources().getColor(R.color.color_205870a4));
            holder.mTxtName.setAlpha(1f);
            holder.mTxtMoney.setAlpha(1f);
        } else {
            holder.mBaseLayout.setBackgroundColor(mContext.getResources().getColor(R.color.white));
            holder.mTxtName.setAlpha(0.5f);
            holder.mTxtMoney.setAlpha(0.5f);
        }

        if(walletBean.getIsClodWallet() == 1){
            holder.mTxtType.setVisibility(View.VISIBLE);
        }else{
            holder.mTxtType.setVisibility(View.GONE);
        }

        if (mEyesStatus != 0) {
            holder.mTxtMoney.setText(mContext.getResources().getString(R.string.main_home_money_eyes_close));
        } else {
            holder.mTxtMoney.setText(SlinUtil.formatNumFromWeiS(mContext, new BigDecimal(walletBean.getMoney()))+" "+mContext.getResources().getString(R.string.wallet_manager_txt_money_unit_01));
        }

        holder.mImgHeader.setText(StrUtil.getWalletHeaderName(walletBean.getWalletBName()));

        holder.mTxtName.setText(walletBean.getWalletBName());
        return convertView;
    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        private TextView mImgHeader;
        private TextView mTxtName, mTxtMoney,mTxtType;
        private LinearLayout mBaseLayout;

        public ItemHolder(View itemView) {
            super(itemView);
            mBaseLayout = itemView.findViewById(R.id.item_choose_main_wallet_base_layout);
            mImgHeader = itemView.findViewById(R.id.item_choose_main_wallet_img_header);
            mTxtName = itemView.findViewById(R.id.item_choose_main_wallet_txt_name);
            mTxtMoney = itemView.findViewById(R.id.item_choose_main_wallet_txt_money);
            mTxtType = itemView.findViewById(R.id.item_choose_main_wallet_txt_type);
        }
    }
}
