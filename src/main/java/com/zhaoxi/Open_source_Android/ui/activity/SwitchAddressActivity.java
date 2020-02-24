package com.zhaoxi.Open_source_Android.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.gyf.immersionbar.ImmersionBar;
import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.base.BaseTitleBarActivity;
import com.zhaoxi.Open_source_Android.common.base.DappApplication;
import com.zhaoxi.Open_source_Android.common.view.refresh.MyPtrFrameLayout;
import com.zhaoxi.Open_source_Android.db.CreateDbWallet;
import com.zhaoxi.Open_source_Android.libs.tools.SystemLog;
import com.zhaoxi.Open_source_Android.libs.utils.ChangeLanguageUtil;
import com.zhaoxi.Open_source_Android.libs.utils.SharedPreferencesUtil;
import com.zhaoxi.Open_source_Android.libs.utils.SlinUtil;
import com.zhaoxi.Open_source_Android.net.BaseBet.NetResultCallBack;
import com.zhaoxi.Open_source_Android.net.Request.GetMultiWalletBalanceRequest;
import com.zhaoxi.Open_source_Android.net.bean.WalletBalanceBean;
import com.zhaoxi.Open_source_Android.ui.adapter.SwitchAddressAdapter;
import com.zhaoxi.Open_source_Android.web3.bean.WalletBean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrFrameLayout;

public class SwitchAddressActivity extends BaseTitleBarActivity implements MyPtrFrameLayout.OnRefreshListener {

    @BindView(R.id.recycler_with_refresh_ptr_frame_layout)
    MyPtrFrameLayout mPtrFrameLayout;
    @BindView(R.id.wallet_list_recycler_view)
    RecyclerView mWalletListRecyclerView;

    private List<WalletBean> mListData;
    private List<WalletBean> mDbListData;
    private List<String> mListAddress;

    private CreateDbWallet mCreateDbWallet;
    private SwitchAddressAdapter walletRecyclerViewAdapter;
    private String mDefaultAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_switch_address);
        ButterKnife.bind(this);
        initView();
        initData();
        initListener();
    }


    private void initView() {
        ImmersionBar.with(this)
                .statusBarDarkFont(true, 0.2f)
                .init();
        setTitleBgColor(R.color.white, true);
        setTitle(getResources().getString(R.string.activity_switch_address_txt_1), true);

        mPtrFrameLayout.setLastUpdateTimeRelateObject(this);
        mPtrFrameLayout.setUltraPullToRefresh(this, mWalletListRecyclerView);
        mPtrFrameLayout.changeWhiteBackgroud();

        LinearLayoutManager lm = new LinearLayoutManager(this);
        mWalletListRecyclerView.setLayoutManager(lm);
    }

    private void initData() {
        mListData = new ArrayList<>();
        mDbListData = new ArrayList<>();
        mListAddress = new ArrayList<>();

        mCreateDbWallet = new CreateDbWallet(this);
        walletRecyclerViewAdapter = new SwitchAddressAdapter(mListData, this);
        mWalletListRecyclerView.setAdapter(walletRecyclerViewAdapter);
    }

    public void initListener() {
        walletRecyclerViewAdapter.setOnCreateWalletListener(new SwitchAddressAdapter.OnWalletChangeListener() {
            @Override
            public void onCreateWallet() {
                //创建钱包
                startActivity(new Intent(SwitchAddressActivity.this, CreateWalletActivity.class));
            }

            @Override
            public void switchWallet(String address) {
                // 切换钱包
                SharedPreferencesUtil.setSharePreString(SwitchAddressActivity.this, SharedPreferencesUtil.CHOOSE_WALLET_ADDRESS, address);
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void refreshCompleted() {
                mPtrFrameLayout.refreshComplete();
            }
        });
    }

    private void updateData() {
        showProgressDialog();
        if (mDbListData.size() != 0) {
            mDbListData.clear();
        }
        if (mListData.size() != 0) {
            mListData.clear();
        }

        mDbListData = mCreateDbWallet.queryAllWallet(this);
        mListAddress = mCreateDbWallet.queryAllAddress();
        mDefaultAddress = SharedPreferencesUtil.getSharePreString(this, SharedPreferencesUtil.CHOOSE_WALLET_ADDRESS);
        new GetMultiWalletBalanceRequest(mListAddress).doRequest(this, new NetResultCallBack() {
            @Override
            public void onSuccess(JSONArray jsonArray) {
                SystemLog.D("GetListBalanceRequest", JSON.toJSONString(jsonArray));
                if (jsonArray.get(2) != null) {
                    WalletBalanceBean walletBalanceBean = JSON.parseObject(jsonArray.get(2).toString(), WalletBalanceBean.class);
                    String cnyTotalValue = SlinUtil.NumberFormat2(SwitchAddressActivity.this, new BigDecimal(walletBalanceBean.getCnyTotalValue()));
                    String usdTotalValue = SlinUtil.NumberFormat2(SwitchAddressActivity.this, new BigDecimal(walletBalanceBean.getUsdTotalValue()));

                    int currentChoseUnit = SharedPreferencesUtil.getSharePreInt(DappApplication.getInstance().getApplicationContext(), SharedPreferencesUtil.CHANGE_COIN_UNIT);
                    if (currentChoseUnit == 0) {
                        int language = SharedPreferencesUtil.getSharePreInt(DappApplication.getInstance().getApplicationContext(), SharedPreferencesUtil.CHANGE_LANGUAGE_NAME);
                        if (language == 0) {
                            String sysLanguage = Locale.getDefault().getLanguage();
                            if ("zh".equals(sysLanguage)) {
                                walletRecyclerViewAdapter.setCnyTotalValue(cnyTotalValue);
                            } else {
                                walletRecyclerViewAdapter.setCnyTotalValue(usdTotalValue);
                            }
                        } else {
                            if (language == ChangeLanguageUtil.CHANGE_LANGUAGE_CHINA) {
                                walletRecyclerViewAdapter.setCnyTotalValue(cnyTotalValue);
                            } else {
                                walletRecyclerViewAdapter.setCnyTotalValue(usdTotalValue);
                            }
                        }
                    } else {
                        if (currentChoseUnit == 1) {// 人民币
                            walletRecyclerViewAdapter.setCnyTotalValue(cnyTotalValue);
                        } else {// 美元
                            walletRecyclerViewAdapter.setCnyTotalValue(usdTotalValue);
                        }
                    }

                    List<WalletBalanceBean.BalanceInfo> balanceInfos = walletBalanceBean.getList();
                    if (balanceInfos.size() > 0) {
                        for (int i = 0; i < mDbListData.size(); i++) {
                            WalletBean walletBean = mDbListData.get(i);
                            WalletBalanceBean.BalanceInfo balanceInfo = balanceInfos.get(i);
                            String cny = balanceInfo.getCnyTotalValue();
                            String usd = balanceInfo.getUsdTotalValue();

                            if (currentChoseUnit == 0) {
                                int language = SharedPreferencesUtil.getSharePreInt(DappApplication.getInstance().getApplicationContext(), SharedPreferencesUtil.CHANGE_LANGUAGE_NAME);
                                if (language == 0) {
                                    String sysLanguage = Locale.getDefault().getLanguage();
                                    if ("zh".equals(sysLanguage)) {
                                        walletBean.setMoney(cny);
                                    } else {
                                        walletBean.setMoney(usd);
                                    }
                                } else {
                                    if (language == ChangeLanguageUtil.CHANGE_LANGUAGE_CHINA) {
                                        walletBean.setMoney(cny);
                                    } else {
                                        walletBean.setMoney(usd);
                                    }
                                }
                            } else {
                                if (currentChoseUnit == 1) {// 人民币
                                    walletBean.setMoney(cny);
                                } else {// 美元
                                    walletBean.setMoney(usd);
                                }
                            }

                            walletBean.setAddress(balanceInfo.getAddress());
                            if (mDefaultAddress.equals(walletBean.getAddress())) {
                                walletBean.setChooseWallet(true);
                            }
                            mListData.add(walletBean);
                        }
                    } else {
                        for (int i = 0; i < mDbListData.size(); i++) {
                            WalletBean walletBean = mDbListData.get(i);
                            walletBean.setMoney("0");
                            if (mDefaultAddress.equals(walletBean.getAddress())) {
                                walletBean.setChooseWallet(true);
                            }
                            mListData.add(walletBean);
                        }
                    }
                }
                dismissProgressDialog();

                walletRecyclerViewAdapter.notifyDataSetChanged();
                mPtrFrameLayout.refreshComplete();
            }

            @Override
            public void onError(String error) {
                String totalValue = SlinUtil.NumberFormat2(SwitchAddressActivity.this, new BigDecimal("0"));
                walletRecyclerViewAdapter.setCnyTotalValue(totalValue);

                //列表
                for (int i = 0; i < mDbListData.size(); i++) {
                    WalletBean walletBean = mDbListData.get(i);
                    walletBean.setMoney("0");
                    if (mDefaultAddress.equals(walletBean.getAddress())) {
                        walletBean.setChooseWallet(true);
                    }
                    mListData.add(walletBean);
                }

                walletRecyclerViewAdapter.notifyDataSetChanged();
                mPtrFrameLayout.refreshComplete();
                dismissProgressDialog();
                DappApplication.getInstance().showToast(error);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        updateData();
    }

    @Override
    public void refresh(PtrFrameLayout frame) {
        updateData();
    }
}
