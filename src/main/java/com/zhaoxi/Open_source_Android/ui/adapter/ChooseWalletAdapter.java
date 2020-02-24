package com.zhaoxi.Open_source_Android.ui.adapter;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.base.BaseActivity;
import com.zhaoxi.Open_source_Android.common.view.CircleImageView;
import com.zhaoxi.Open_source_Android.libs.utils.CollectionUtil;
import com.zhaoxi.Open_source_Android.libs.utils.SystemInfoUtil;
import com.zhaoxi.Open_source_Android.web3.bean.WalletBean;

import java.util.List;

/**
 * des:
 * Created by ztt on 2018/6/14.
 */

public class ChooseWalletAdapter extends BaseAdapter {
    private BaseActivity mContext;
    private List<WalletBean> mListData;

    public ChooseWalletAdapter(BaseActivity context,List<WalletBean> listData) {
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
        WalletBean walletBean = mListData.get(position);
        ItemHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_choose_wallet_list, null);
            holder = new ItemHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ItemHolder) convertView.getTag();
        }
        if(walletBean.isChooseWallet()){
            holder.mBaseLayout.setBackgroundColor(mContext.getResources().getColor(R.color.color_F6F7FD));
            holder.mTxtName.setTextColor(mContext.getResources().getColor(R.color.color_2E2F47));
        }else{
            holder.mBaseLayout.setBackgroundColor(mContext.getResources().getColor(R.color.white));
            holder.mTxtName.setTextColor(mContext.getResources().getColor(R.color.color_92939A));
        }
        String imgName = "test_0" + walletBean.getImgId();
        Bitmap bitmap = SystemInfoUtil.getBitmapByName(mContext, imgName);
        holder.mImgHeader.setImageBitmap(bitmap);

        holder.mTxtName.setText(walletBean.getWalletBName());
        return convertView;
    }
    public class ItemHolder extends RecyclerView.ViewHolder {
        private CircleImageView mImgHeader;
        private TextView mTxtName;
        private LinearLayout mBaseLayout;

        public ItemHolder(View itemView) {
            super(itemView);
            mBaseLayout = itemView.findViewById(R.id.item_choose_wallet_base_layout);
            mImgHeader = itemView.findViewById(R.id.item_choose_wallet_img_header);
            mTxtName = itemView.findViewById(R.id.item_choose_wallet_txt_name);
        }
    }
}
