package com.zhaoxi.Open_source_Android.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhaoxi.Open_source_Android.DAppConstants;
import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.base.DappApplication;
import com.zhaoxi.Open_source_Android.libs.utils.ChangeLanguageUtil;
import com.zhaoxi.Open_source_Android.libs.utils.SharedPreferencesUtil;
import com.zhaoxi.Open_source_Android.libs.utils.SlinUtil;
import com.zhaoxi.Open_source_Android.libs.utils.StrUtil;
import com.zhaoxi.Open_source_Android.web3.bean.WalletBean;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * create by fangz
 * create date:2019/9/5
 * create time:9:49
 */
public class WalletAdapter extends RecyclerView.Adapter<WalletAdapter.ItemViewHolder> {

    private List<WalletBean> mListData;
    private String mCurrentAssetsUnit;

    public WalletAdapter(List<WalletBean> mListData) {
        this.mListData = mListData;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_switch_address_wallet_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        int mCurrentChoseUnit = SharedPreferencesUtil.getSharePreInt(DappApplication.getInstance().getApplicationContext(), SharedPreferencesUtil.CHANGE_COIN_UNIT);
        if (mCurrentChoseUnit == 0) {
            int languge = SharedPreferencesUtil.getSharePreInt(DappApplication.getInstance().getApplicationContext(), SharedPreferencesUtil.CHANGE_LANGUAGE_NAME);
            if (languge == 0) {
                String sysLanguage = Locale.getDefault().getLanguage();
                if ("zh".equals(sysLanguage)) {
                    mCurrentAssetsUnit = DappApplication.getInstance().getApplicationContext().getResources().getString(R.string.activity_rmb_value);
                } else {
                    mCurrentAssetsUnit = DappApplication.getInstance().getApplicationContext().getResources().getString(R.string.activity_dollar_value);
                }
            } else {
                if (languge == ChangeLanguageUtil.CHANGE_LANGUAGE_CHINA) {
                    mCurrentAssetsUnit = DappApplication.getInstance().getApplicationContext().getResources().getString(R.string.activity_rmb_value);
                } else {
                    mCurrentAssetsUnit = DappApplication.getInstance().getApplicationContext().getResources().getString(R.string.activity_dollar_value);
                }
            }
        } else {
            if (mCurrentChoseUnit == 1) {// 人民币
                mCurrentAssetsUnit = DappApplication.getInstance().getApplicationContext().getResources().getString(R.string.activity_rmb_value);
            } else {// 美元
                mCurrentAssetsUnit = DappApplication.getInstance().getApplicationContext().getResources().getString(R.string.activity_dollar_value);
            }

        }


        holder.mWalletAssetUnit.setText(mCurrentAssetsUnit);
        WalletBean walletBean = mListData.get(position);
        holder.mWalletHeader.setText(StrUtil.getWalletHeaderName(walletBean.getWalletBName()));
        holder.mWalletName.setText(walletBean.getWalletBName());
        if (walletBean.isChooseWallet()) {
            holder.mWalletContainer.setBackgroundColor(DappApplication.getInstance().getApplicationContext().getResources().getColor(R.color.color_F5F6F8));
        } else {
            holder.mWalletContainer.setBackgroundColor(DappApplication.getInstance().getApplicationContext().getResources().getColor(R.color.white));
        }

        if (walletBean.getIsClodWallet() == 1) {
            holder.mWalletType.setVisibility(View.VISIBLE);
        } else {
            holder.mWalletType.setVisibility(View.GONE);
        }

        String address = walletBean.getAddress();
        holder.mWalletAddress.setText(StrUtil.formatAddress(address));

        int mEyesStatus = SharedPreferencesUtil.getSharePreInt(DappApplication.getInstance().getApplicationContext(), DAppConstants.MONEY_EYES_STATUS);
        if (mEyesStatus == SwitchAddressAdapter.STATUS_SHOW) {//显示时隐藏
            holder.mWalletAssetUnit.setVisibility(View.VISIBLE);
            holder.mWalletAssetsValue.setText(SlinUtil.NumberFormat2(DappApplication.getInstance().getApplicationContext(), new BigDecimal(walletBean.getMoney())));
        } else {// 隐藏时显示
            holder.mWalletAssetUnit.setVisibility(View.GONE);
            holder.mWalletAssetsValue.setText(DappApplication.getInstance().getApplicationContext().getResources().getString(R.string.main_home_money_eyes_close));
        }

        holder.mWalletContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.switchWallet(walletBean.getAddress());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mListData.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_choose_main_wallet_img_header)
        TextView mWalletHeader;
        @BindView(R.id.item_choose_main_wallet_txt_type)
        TextView mWalletType;
        @BindView(R.id.item_choose_main_wallet_txt_name)
        TextView mWalletName;
        @BindView(R.id.item_choose_main_wallet_txt_address)
        TextView mWalletAddress;
        @BindView(R.id.item_wallet_asset_value)
        TextView mWalletAssetsValue;
        @BindView(R.id.wallet_item_container)
        LinearLayout mWalletContainer;
        @BindView(R.id.tv_wallet_assets_unit)
        TextView mWalletAssetUnit;

        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private OnSwitchWalletListener listener;

    public void setOnSwitchWalletListener(OnSwitchWalletListener listener) {
        this.listener = listener;
    }

    public interface OnSwitchWalletListener {
        void switchWallet(String address);
    }
}
