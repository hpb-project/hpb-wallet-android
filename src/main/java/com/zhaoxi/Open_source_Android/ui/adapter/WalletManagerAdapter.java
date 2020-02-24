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
import com.zhaoxi.Open_source_Android.libs.utils.ChangeLanguageUtil;
import com.zhaoxi.Open_source_Android.libs.utils.SharedPreferencesUtil;
import com.zhaoxi.Open_source_Android.libs.utils.SlinUtil;
import com.zhaoxi.Open_source_Android.libs.utils.StrUtil;
import com.zhaoxi.Open_source_Android.ui.activity.WalletDetailsActivity;
import com.zhaoxi.Open_source_Android.web3.bean.WalletBean;

import java.math.BigDecimal;
import java.util.List;

/**
 * des:
 * Created by ztt on 2018/6/7.
 */
public class WalletManagerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private ChangeLanguageUtil languageUtil;
    private List<WalletBean> mWalletData;
    private int mCoinUnit;
    private int mLanguage;

    public WalletManagerAdapter(Context context, List<WalletBean> walletData) {
        this.mContext = context;
        this.mWalletData = walletData;
        mCoinUnit = SharedPreferencesUtil.getSharePreInt(mContext, SharedPreferencesUtil.CHANGE_COIN_UNIT);
        languageUtil = new ChangeLanguageUtil();
        mLanguage = languageUtil.languageType(mContext);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_wallet_manager, viewGroup, false);
        return new WalletHodler(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        WalletBean walletBean = mWalletData.get(position);
        WalletHodler walletHodler = (WalletHodler) holder;
        walletHodler.mImgHader.setText(StrUtil.getWalletHeaderName(walletBean.getWalletBName()));

        walletHodler.mTxtName.setText(walletBean.getWalletBName());
        walletHodler.mTxtAddress.setText(walletBean.getAddress());

        if (StrUtil.isEmpty(walletBean.getMnemonic())) {
            walletHodler.mTxtNoCopy.setVisibility(View.GONE);
        } else {
            walletHodler.mTxtNoCopy.setVisibility(View.VISIBLE);
        }

        if (walletBean.getIsClodWallet() != 0) {
            walletHodler.mTxtColdStatus.setVisibility(View.VISIBLE);
        } else {
            walletHodler.mTxtColdStatus.setVisibility(View.GONE);
        }
        String moneys = "";
        if (mCoinUnit == 0) {//根据本地语言显示
            if (mLanguage == 1) {//人民币
                BigDecimal money = new BigDecimal("" + walletBean.getMoney_c());
                moneys = "≈ ¥ "+ SlinUtil.NumberFormat2(mContext, money);
            } else {
                BigDecimal money = new BigDecimal("" + walletBean.getMoney_u());
                moneys = "≈ $ "+ SlinUtil.NumberFormat2(mContext, money);
            }
        } else if (mCoinUnit == 1) {//人民币
            BigDecimal money = new BigDecimal("" + walletBean.getMoney_c());
            moneys = "≈ ¥ "+ SlinUtil.NumberFormat2(mContext, money);
        } else {
            BigDecimal money = new BigDecimal("" + walletBean.getMoney_u());
            moneys = "≈ $ "+ SlinUtil.NumberFormat2(mContext, money);
        }

        if (moneys.length() > 12) {
            walletHodler.mTxtMoney.setTextSize(18f);
        }
        walletHodler.mTxtMoney.setText("" + moneys);

        walletHodler.mBaseLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goto_details = new Intent(mContext, WalletDetailsActivity.class);
                goto_details.putExtra(WalletDetailsActivity.WALLET_TITLE, walletBean.getWalletBName());
                goto_details.putExtra(WalletDetailsActivity.WALLET_ADDRESS, walletBean.getAddress());
                mContext.startActivity(goto_details);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mWalletData.size();
    }

    private class WalletHodler extends RecyclerView.ViewHolder {
        private LinearLayout mBaseLayout;
        private TextView mImgHader;
        private TextView mTxtName, mTxtMoney, mTxtNoCopy, mTxtColdStatus, mTxtAddress;

        public WalletHodler(View itemView) {
            super(itemView);
            mBaseLayout = itemView.findViewById(R.id.item_wallet_manager_base);
            mImgHader = itemView.findViewById(R.id.item_wallet_manager_header_img);
            mTxtName = itemView.findViewById(R.id.item_wallet_manager_name);
            mTxtMoney = itemView.findViewById(R.id.item_wallet_manager_money);
            mTxtNoCopy = itemView.findViewById(R.id.item_wallet_manager_img_no_copy);
            mTxtColdStatus = itemView.findViewById(R.id.item_wallet_manager_img_cold_status);
            mTxtAddress = itemView.findViewById(R.id.item_wallet_manager_address);
        }
    }
}
