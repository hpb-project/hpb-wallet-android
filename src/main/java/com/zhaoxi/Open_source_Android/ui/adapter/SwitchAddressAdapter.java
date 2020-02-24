package com.zhaoxi.Open_source_Android.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhaoxi.Open_source_Android.DAppConstants;
import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.libs.utils.ChangeLanguageUtil;
import com.zhaoxi.Open_source_Android.libs.utils.SharedPreferencesUtil;
import com.zhaoxi.Open_source_Android.web3.bean.WalletBean;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * create by fangz
 * create date:2019/9/5
 * create time:7:58
 */
public class SwitchAddressAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_ITEM_HEAD = 0x00;
    private static final int TYPE_ITEM_WALLET_LIST = 0x01;
    private static final int TYPE_ITEM_CREATE_WALLET = 0x02;

    static final int STATUS_HIDE = 1;// 隐藏金额
    static final int STATUS_SHOW = 0;// 显示金额

    private List<WalletBean> mListData;
    private Context mContext;
    private String mCnyTotalValue;
    private WalletAdapter walletAdapter;

    public SwitchAddressAdapter(List<WalletBean> mListData, Context context) {
        this.mListData = mListData;
        mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        if (viewType == TYPE_ITEM_HEAD) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_switch_address_head, parent, false);
            return new HeadViewHolder(itemView);
        } else if (viewType == TYPE_ITEM_WALLET_LIST) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_switch_address_wallet_recycler, parent, false);
            return new WalletListViewHolder(itemView);
        } else {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_switch_address_create_wallet, parent, false);
            return new CreateWalletViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeadViewHolder) {
            HeadViewHolder headViewHolder = (HeadViewHolder) holder;
            int currentChoseUnit = SharedPreferencesUtil.getSharePreInt(mContext, SharedPreferencesUtil.CHANGE_COIN_UNIT);
            String mCurrentAssetsUnit;
            if (currentChoseUnit == 0) {
                int language = SharedPreferencesUtil.getSharePreInt(mContext, SharedPreferencesUtil.CHANGE_LANGUAGE_NAME);
                if (language == 0) {
                    String sysLanguage = Locale.getDefault().getLanguage();
                    if ("zh".equals(sysLanguage)) {
                        mCurrentAssetsUnit = mContext.getResources().getString(R.string.activity_rmb_value);
                    } else {
                        mCurrentAssetsUnit = mContext.getResources().getString(R.string.activity_dollar_value);
                    }
                } else {
                    if (language == ChangeLanguageUtil.CHANGE_LANGUAGE_CHINA) {
                        mCurrentAssetsUnit = mContext.getResources().getString(R.string.activity_rmb_value);
                    } else {
                        mCurrentAssetsUnit = mContext.getResources().getString(R.string.activity_dollar_value);
                    }
                }
            } else {
                if (currentChoseUnit == 1) {// 人民币
                    mCurrentAssetsUnit = mContext.getResources().getString(R.string.activity_rmb_value);
                } else {// 美元
                    mCurrentAssetsUnit = mContext.getResources().getString(R.string.activity_dollar_value);
                }

            }
            headViewHolder.mAssetsYuan.setText(mCurrentAssetsUnit);
            headViewHolder.mEyesAssets.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int enableShowStatus = SharedPreferencesUtil.getSharePreInt(mContext, DAppConstants.MONEY_EYES_STATUS);
                    if (enableShowStatus == STATUS_SHOW) {
                        setAssetsEnableShow(headViewHolder, STATUS_HIDE);
                    } else {
                        setAssetsEnableShow(headViewHolder, STATUS_SHOW);
                    }
                }
            });

            if (!TextUtils.isEmpty(mCnyTotalValue)) {
                int enableShowStatus = SharedPreferencesUtil.getSharePreInt(mContext, DAppConstants.MONEY_EYES_STATUS);
                setAssetsEnableShow(headViewHolder, enableShowStatus);
            }

        } else if (holder instanceof WalletListViewHolder) {
            WalletListViewHolder walletListViewHolder = (WalletListViewHolder) holder;
            LinearLayoutManager lm = new LinearLayoutManager(mContext);
            walletListViewHolder.recyclerView.setLayoutManager(lm);
            walletAdapter = new WalletAdapter(mListData);
            walletAdapter.setOnSwitchWalletListener(new WalletAdapter.OnSwitchWalletListener() {
                @Override
                public void switchWallet(String address) {
                    if (onCreateWalletListener != null) {
                        onCreateWalletListener.switchWallet(address);
                    }
                }
            });
            walletListViewHolder.recyclerView.setAdapter(walletAdapter);
        } else if (holder instanceof CreateWalletViewHolder) {
            CreateWalletViewHolder createWalletViewHolder = (CreateWalletViewHolder) holder;
            createWalletViewHolder.mCreateWallet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onCreateWalletListener != null) {
                        onCreateWalletListener.onCreateWallet();
                    }
                }
            });

        }
    }

    private void setAssetsEnableShow(HeadViewHolder headViewHolder, int eyesStatus) {
        headViewHolder.mEyesAssets.setVisibility(View.VISIBLE);
        if (eyesStatus == STATUS_HIDE) {//隐藏
            headViewHolder.mAssetsYuan.setVisibility(View.GONE);
            headViewHolder.mEyesAssets.setImageResource(R.mipmap.icon_hide_assets);
            headViewHolder.mWalletAssets.setText(mContext.getResources().getString(R.string.main_home_money_eyes_close));
        } else {//显示
            headViewHolder.mAssetsYuan.setVisibility(View.VISIBLE);
            headViewHolder.mEyesAssets.setImageResource(R.mipmap.icon_show_assets);
            setCurrentMoney(headViewHolder, mCnyTotalValue);
        }

        walletAdapter.notifyDataSetChanged();
        SharedPreferencesUtil.setSharePreInt(mContext, DAppConstants.MONEY_EYES_STATUS, eyesStatus);
    }


    public void setCnyTotalValue(String cnyTotalValue) {
        this.mCnyTotalValue = cnyTotalValue;
    }

    private void setCurrentMoney(HeadViewHolder headViewHolder, String assets) {
        String curMoney = String.valueOf(assets);
        float size = 24f;
        if (curMoney.length() > 12 && curMoney.length() <= 16) {
            size = 20f;
        } else if (curMoney.length() > 16) {
            size = 18f;
        }
        headViewHolder.mWalletAssets.setTextSize(TypedValue.COMPLEX_UNIT_DIP, size);
        headViewHolder.mWalletAssets.setText(assets);
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_ITEM_HEAD;
        } else if (position == 1) {
            return TYPE_ITEM_WALLET_LIST;
        } else {
            return TYPE_ITEM_CREATE_WALLET;
        }
    }

    class HeadViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_switch_address_assets)
        TextView mWalletAssets;
        @BindView(R.id.iv_eyes_assets)
        ImageView mEyesAssets;
        @BindView(R.id.tv_switch_address_assets_yuan)
        TextView mAssetsYuan;

        public HeadViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class WalletListViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.wallet_recycler_view)
        RecyclerView recyclerView;

        public WalletListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class CreateWalletViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.switch_address_create_wallet)
        LinearLayout mCreateWallet;

        public CreateWalletViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private OnWalletChangeListener onCreateWalletListener;

    public void setOnCreateWalletListener(OnWalletChangeListener listener) {
        onCreateWalletListener = listener;
    }

    public interface OnWalletChangeListener {
        void onCreateWallet();

        void refreshCompleted();

        void switchWallet(String address);
    }
}
