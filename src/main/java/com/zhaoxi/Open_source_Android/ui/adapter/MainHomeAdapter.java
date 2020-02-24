package com.zhaoxi.Open_source_Android.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhaoxi.Open_source_Android.DAppConstants;
import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.base.DappApplication;
import com.zhaoxi.Open_source_Android.db.CreateDbWallet;
import com.zhaoxi.Open_source_Android.libs.utils.ChangeLanguageUtil;
import com.zhaoxi.Open_source_Android.libs.utils.CollectionUtil;
import com.zhaoxi.Open_source_Android.libs.utils.DisplayUtils;
import com.zhaoxi.Open_source_Android.libs.utils.SharedPreferencesUtil;
import com.zhaoxi.Open_source_Android.libs.utils.SlinUtil;
import com.zhaoxi.Open_source_Android.libs.utils.StrUtil;
import com.zhaoxi.Open_source_Android.net.bean.AssetsBean;
import com.zhaoxi.Open_source_Android.ui.activity.MainActivity;
import com.zhaoxi.Open_source_Android.web3.bean.WalletBean;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * create by fangz
 * create date:2019/9/4
 * create time:14:34
 */
public class MainHomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    /** 首页总资产、红包、收款、转账部分*/
    private static final int TYPE_MAIN_TOP = 0x00;
    /** 首页资产列表部分*/
    private static final int TYPE_MAIN_ASSETS_LIST = 0x01;
    private BigDecimal mCurrentMoney = new BigDecimal(0);
    private CreateDbWallet mCreateDbWallet;
    private String mCurrentAssetsUnit;

    private Context mContext;
    private String mDefaultAddress;
    private int mEyesStatus;

    private List<AssetsBean> assetsBeans;
    private AssetsAdapter assetsAdapter;
    private String mTopTitleUnit;

    public MainHomeAdapter(Context context, List<AssetsBean> assetsBeans) {
        mCreateDbWallet = new CreateDbWallet(context);
        this.mContext = context;
        this.assetsBeans = assetsBeans;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        if (viewType == TYPE_MAIN_TOP) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main_home_top_layout, parent, false);
            return new MainHomeTopViewHolder(itemView);
        } else {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_assets_recycler, parent, false);
            return new MainHomeAssetsViewHolder(itemView);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MainHomeTopViewHolder) {
            // 展示首页
            mDefaultAddress = SharedPreferencesUtil.getSharePreString(mContext, SharedPreferencesUtil.CHOOSE_WALLET_ADDRESS);
            // 当前金额折合单位
            int currentChoseUnit = SharedPreferencesUtil.getSharePreInt(DappApplication.getInstance().getApplicationContext(), SharedPreferencesUtil.CHANGE_COIN_UNIT);
            int currentNoWalletResId;
            if (currentChoseUnit == 0) {
                int language = SharedPreferencesUtil.getSharePreInt(DappApplication.getInstance().getApplicationContext(), SharedPreferencesUtil.CHANGE_LANGUAGE_NAME);
                if (language == 0) {
                    String sysLanguage = Locale.getDefault().getLanguage();
                    if ("zh".equals(sysLanguage)) {
                        mCurrentAssetsUnit = DappApplication.getInstance().getApplicationContext().getResources().getString(R.string.activity_rmb_value);
                        mTopTitleUnit = mContext.getResources().getString(R.string.activity_coin_unit_txt_02);
                        currentNoWalletResId = R.mipmap.icon_main_home_nowallet;
                    } else {
                        mCurrentAssetsUnit = DappApplication.getInstance().getApplicationContext().getResources().getString(R.string.activity_dollar_value);
                        mTopTitleUnit = mContext.getResources().getString(R.string.activity_coin_unit_txt_03);
                        currentNoWalletResId = R.mipmap.icon_no_wallet;
                    }
                } else {
                    if (language == ChangeLanguageUtil.CHANGE_LANGUAGE_CHINA) {
                        mCurrentAssetsUnit = DappApplication.getInstance().getApplicationContext().getResources().getString(R.string.activity_rmb_value);
                        mTopTitleUnit = mContext.getResources().getString(R.string.activity_coin_unit_txt_02);
                        currentNoWalletResId = R.mipmap.icon_main_home_nowallet;
                    } else {
                        mCurrentAssetsUnit = DappApplication.getInstance().getApplicationContext().getResources().getString(R.string.activity_dollar_value);
                        mTopTitleUnit = mContext.getResources().getString(R.string.activity_coin_unit_txt_03);
                        currentNoWalletResId = R.mipmap.icon_no_wallet;
                    }
                }
            } else {
                if (currentChoseUnit == 1) {// 人民币
                    mCurrentAssetsUnit = DappApplication.getInstance().getApplicationContext().getResources().getString(R.string.activity_rmb_value);
                    mTopTitleUnit = mContext.getResources().getString(R.string.activity_coin_unit_txt_02);
                    currentNoWalletResId = R.mipmap.icon_main_home_nowallet;
                } else {// 美元
                    mCurrentAssetsUnit = DappApplication.getInstance().getApplicationContext().getResources().getString(R.string.activity_dollar_value);
                    mTopTitleUnit = mContext.getResources().getString(R.string.activity_coin_unit_txt_03);
                    currentNoWalletResId = R.mipmap.icon_no_wallet;
                }
            }

            MainHomeTopViewHolder mainHomeTopViewHolder = (MainHomeTopViewHolder) holder;
            mainHomeTopViewHolder.mMainTopTitle.setText(mContext.getResources().getString(R.string.main_home_content_txt_allmoney));
            if (StrUtil.isEmpty(mDefaultAddress)) {//没有就创建最新的钱包
                mainHomeTopViewHolder.mMainHomeWalletAssetsUnit.setVisibility(View.GONE);
                mainHomeTopViewHolder.mMainHomellNoWallet.setVisibility(View.VISIBLE);
                mainHomeTopViewHolder.mMainHomeNoWallet.setImageResource(currentNoWalletResId);
                mainHomeTopViewHolder.mMainHomeAssetsList.setVisibility(View.GONE);
                mainHomeTopViewHolder.mMainHomeTotalAssets.setText(mContext.getResources().getString(R.string.fragment_main_home_no_yuan));
                mainHomeTopViewHolder.mMainHomeRightTopTxt.setVisibility(View.GONE);
                mainHomeTopViewHolder.mMainHomeWalletAddress.setText(mContext.getResources().getString(R.string.main_home_money_eyes_close));
                mainHomeTopViewHolder.mMainHomeWalletAddress.post(new Runnable() {
                    @Override
                    public void run() {
                        ViewGroup.LayoutParams layoutParams = mainHomeTopViewHolder.mMainHomeWalletAddress.getLayoutParams();
                        layoutParams.width = DisplayUtils.dp2px(mContext, 36);
                        mainHomeTopViewHolder.mMainHomeWalletAddress.setLayoutParams(layoutParams);
                    }
                });

            } else {
                mainHomeTopViewHolder.mMainHomeTotalAssets.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mEyesStatus = SharedPreferencesUtil.getSharePreInt(mContext, DAppConstants.MONEY_EYES_STATUS);
                        if (mEyesStatus == SwitchAddressAdapter.STATUS_SHOW) {//显示时隐藏
                            mainHomeTopViewHolder.mMainHomeWalletAssetsUnit.setVisibility(View.GONE);
                            mainHomeTopViewHolder.mMainHomeTotalAssets.setText(mContext.getResources().getString(R.string.main_home_money_eyes_close));
                        } else {// 隐藏时显示
                            setCurrentMoney(mainHomeTopViewHolder, mCurrentMoney);
                        }
                        if(assetsAdapter != null){
                            assetsAdapter.notifyDataSetChanged();
                        }
                        SharedPreferencesUtil.setSharePreInt(mContext, DAppConstants.MONEY_EYES_STATUS
                                , mEyesStatus == SwitchAddressAdapter.STATUS_SHOW ? SwitchAddressAdapter.STATUS_HIDE : SwitchAddressAdapter.STATUS_SHOW);

                    }
                });
                setWalletData(mainHomeTopViewHolder, mDefaultAddress);
            }

            mainHomeTopViewHolder.mMainHomellScan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DappApplication.disableDoubleClick(new DappApplication.OnPerformClickListener() {
                        @Override
                        public void onClick(View... view) {
                            if (isAttachedListener()) {
                                listener.onScan();
                            }
                        }
                    });
                }
            });


            mainHomeTopViewHolder.mMainHomellCollection.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DappApplication.disableDoubleClick(new DappApplication.OnPerformClickListener() {
                        @Override
                        public void onClick(View... view) {
                            if (isAttachedListener()) {
                                listener.onCollection();
                            }
                        }
                    });
                }
            });


            mainHomeTopViewHolder.mMainHomellTransfer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DappApplication.disableDoubleClick(new DappApplication.OnPerformClickListener() {
                        @Override
                        public void onClick(View... view) {
                            if (isAttachedListener()) {
                                listener.onTransfer();
                            }
                        }
                    });
                }
            });

            mainHomeTopViewHolder.mMainHomellSyncAssets.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DappApplication.disableDoubleClick(new DappApplication.OnPerformClickListener() {
                        @Override
                        public void onClick(View... view) {
                            if (isAttachedListener()){
                                listener.onSyncAssets();
                            }
                        }
                    });
                }
            });

            mainHomeTopViewHolder.mMainHomeCreateWallet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isAttachedListener())
                        listener.onCreateWallet();
                }
            });


            mainHomeTopViewHolder.mMainHomeWalletIvAddress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!StrUtil.isEmpty(mDefaultAddress)) {
                        ((MainActivity) mContext).copyLable(mDefaultAddress);
                    }
                }
            });

            mainHomeTopViewHolder.mMainHomeWalletAddress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!StrUtil.isEmpty(mDefaultAddress)) {
                        ((MainActivity) mContext).copyLable(mDefaultAddress);
                    }
                }
            });


            mainHomeTopViewHolder.mMainHomeRightTopTxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isAttachedListener()) {
                        listener.onSwitchAddress();
                    }
                }
            });

            mainHomeTopViewHolder.mMainHomeIvTokenManager.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isAttachedListener()) {
                        listener.onTokenManager();
                    }
                }
            });


        } else if (holder instanceof MainHomeAssetsViewHolder) {
            MainHomeAssetsViewHolder mainHomeAssetsViewHolder = (MainHomeAssetsViewHolder) holder;
            int mLanguage = ChangeLanguageUtil.languageType(mContext);
            if (CollectionUtil.isCollectionEmpty(assetsBeans)) {
                mainHomeAssetsViewHolder.mIvDefultEmpty.setVisibility(View.VISIBLE);
                if (mLanguage == 1)
                    mainHomeAssetsViewHolder.mIvDefultEmpty.setImageResource(R.drawable.icon_list_empty_zh);
                else
                    mainHomeAssetsViewHolder.mIvDefultEmpty.setImageResource(R.drawable.icon_list_empty_en);
            } else {
                mainHomeAssetsViewHolder.mIvDefultEmpty.setVisibility(View.GONE);
                // 展示资产列表
                LinearLayoutManager lm = new LinearLayoutManager(mContext);
                mainHomeAssetsViewHolder.assetsRecyclerView.setLayoutManager(lm);
                assetsAdapter = new AssetsAdapter(mContext, assetsBeans);
                mainHomeAssetsViewHolder.assetsRecyclerView.setAdapter(assetsAdapter);
                mainHomeAssetsViewHolder.assetsRecyclerView.smoothScrollToPosition(0);

                assetsAdapter.setOnItemClickListener(new AssetsAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(AssetsBean assetsBean) {
                        if (isAttachedListener()) {
                            listener.onItemClick(assetsBean);
                        }
                    }
                });
            }
        }

    }

    private void setWalletData(MainHomeTopViewHolder mainHomeTopViewHolder, String address) {
        WalletBean mWalletBean = mCreateDbWallet.queryWallet(mContext, address);
        String walletBName = mWalletBean.getWalletBName();
        mainHomeTopViewHolder.mMainHomeWalletAddress.setText(address);
        mainHomeTopViewHolder.mMainHomeWalletAddress.post(new Runnable() {
            @Override
            public void run() {
                ViewGroup.LayoutParams layoutParams = mainHomeTopViewHolder.mMainHomeWalletAddress.getLayoutParams();
                layoutParams.width = DisplayUtils.dp2px(mContext, 200);
                mainHomeTopViewHolder.mMainHomeWalletAddress.setLayoutParams(layoutParams);
            }
        });
        int mWalletType = mWalletBean.getIsClodWallet();
        if (mWalletType == 0) {
            mainHomeTopViewHolder.mMainHomeWalletType.setVisibility(View.GONE);
            mainHomeTopViewHolder.mMainHomellSyncAssets.setVisibility(View.GONE);
        } else {
            mainHomeTopViewHolder.mMainHomeWalletType.setVisibility(View.VISIBLE);
            mainHomeTopViewHolder.mMainHomeWalletType.setText(mContext.getResources().getString(R.string.import_wallet_item_coldwallet));
            mainHomeTopViewHolder.mMainHomellSyncAssets.setVisibility(View.VISIBLE);
        }

        mainHomeTopViewHolder.mMainHomellNoWallet.setVisibility(View.GONE);
        mainHomeTopViewHolder.mMainHomeAssetsList.setVisibility(View.VISIBLE);
        mainHomeTopViewHolder.mMainHomeRightTopTxt.setVisibility(View.VISIBLE);
        mainHomeTopViewHolder.mMainHomeRightTopTxt.setText(walletBName);

        if (isAttachedListener())
            listener.updateData(mDefaultAddress, mWalletBean);

        refreshUIData(mainHomeTopViewHolder);
    }

    private void setCurrentMoney(MainHomeTopViewHolder mainHomeTopViewHolder, BigDecimal money) {
        mainHomeTopViewHolder.mMainHomeWalletAssetsUnit.setVisibility(View.VISIBLE);
        mainHomeTopViewHolder.mMainHomeWalletAssetsUnit.setText(mCurrentAssetsUnit);
        mainHomeTopViewHolder.mMainHomeTotalAssets.setText(SlinUtil.NumberFormat2(mContext, mCurrentMoney));
    }


    /**
     * 设置总资产
     * @param assetsValue
     */
    public void setTotalAssets(String assetsValue) {
        mCurrentMoney = new BigDecimal(assetsValue);
    }

    /**
     * 刷新UI数据
     * @param mainHomeTopViewHolder
     */
    private void refreshUIData(MainHomeTopViewHolder mainHomeTopViewHolder){
        mEyesStatus = SharedPreferencesUtil.getSharePreInt(mContext, DAppConstants.MONEY_EYES_STATUS);
        if (mEyesStatus == SwitchAddressAdapter.STATUS_SHOW) {// 0：显示
            setCurrentMoney(mainHomeTopViewHolder, mCurrentMoney);
        } else {// 不显示
            mainHomeTopViewHolder.mMainHomeWalletAssetsUnit.setVisibility(View.GONE);
            mainHomeTopViewHolder.mMainHomeTotalAssets.setText(mContext.getResources().getString(R.string.main_home_money_eyes_close));
        }
    }


    @Override
    public int getItemCount() {
        if (StrUtil.isEmpty(mDefaultAddress)) {
            return 1;
        }
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_MAIN_TOP;
        }
        return TYPE_MAIN_ASSETS_LIST;
    }

    public void setmDefaultAddress(String mDefaultAddress) {
        this.mDefaultAddress = mDefaultAddress;
    }

    class MainHomeTopViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_switch_address_assets_yuan)
        TextView mMainHomeWalletAssetsUnit;
        @BindView(R.id.main_home_right_top_txt)
        TextView mMainHomeRightTopTxt;
        @BindView(R.id.main_home_wallet_txt_allmoney)
        TextView mMainHomeTotalAssets;
        @BindView(R.id.main_home_wallet_txt_address)
        TextView mMainHomeWalletAddress;
        @BindView(R.id.main_home_wallet_iv_address)
        ImageView mMainHomeWalletIvAddress;
        @BindView(R.id.main_home_ll_scan)
        LinearLayout mMainHomellScan;
        @BindView(R.id.main_home_ll_collection)
        LinearLayout mMainHomellCollection;
        @BindView(R.id.main_home_ll_transfer)
        LinearLayout mMainHomellTransfer;
        @BindView(R.id.main_home_ll_sync_assets)
        LinearLayout mMainHomellSyncAssets;
        @BindView(R.id.main_home_layout_no_wallet)
        LinearLayout mMainHomellNoWallet;
        @BindView(R.id.main_home_no_wallet_bg)
        ImageView mMainHomeNoWallet;
        @BindView(R.id.main_home_ll_no_wallet)
        LinearLayout mMainHomeCreateWallet;
        @BindView(R.id.main_home_assets_list)
        RelativeLayout mMainHomeAssetsList;
        @BindView(R.id.main_home_wallet_txt_type)
        TextView mMainHomeWalletType;
        @BindView(R.id.main_home_iv_token_manager)
        ImageView mMainHomeIvTokenManager;
        @BindView(R.id.tv_main_top_title_unit)
        TextView mMainTopTitle;

        public MainHomeTopViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    class MainHomeAssetsViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.assets_recycler_view)
        RecyclerView assetsRecyclerView;
        @BindView(R.id.iv_default_empty)
        ImageView mIvDefultEmpty;


        public MainHomeAssetsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private OnMainHomeClickListener listener;

    public void setOnMainHomeClickListener(OnMainHomeClickListener listener) {
        this.listener = listener;
    }

    public boolean isAttachedListener() {
        return listener != null;
    }

    public interface OnMainHomeClickListener {
        /**
         * 扫码
         */
        void onScan();

        /**
         * 收款
         */
        void onCollection();

        /**
         * 转账
         */
        void onTransfer();

        /**
         * 同步资产
         */
        void onSyncAssets();

        /**
         * 创建钱包
         */
        void onCreateWallet();

        /**
         * 更新数据
         *
         * @param address
         * @param walletBean
         */
        void updateData(String address, WalletBean walletBean);

        /**
         * 刷新完成
         */
        void refreshCompleted();

        /**
         * 切换地址
         */
        void onSwitchAddress();

        /**
         * 代币管理
         */
        void onTokenManager();

        /**
         * 点击一条数据
         *
         * @param assetsBean
         */
        void onItemClick(AssetsBean assetsBean);
    }

}
