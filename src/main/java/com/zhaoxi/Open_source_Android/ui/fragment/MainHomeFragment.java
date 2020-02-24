package com.zhaoxi.Open_source_Android.ui.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.umeng.analytics.MobclickAgent;
import com.zhaoxi.Open_source_Android.DAppConstants;
import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.base.BaseFragment;
import com.zhaoxi.Open_source_Android.common.base.DappApplication;
import com.zhaoxi.Open_source_Android.common.bean.SimpleAssetBean;
import com.zhaoxi.Open_source_Android.common.bean.SyncAssetsBean;
import com.zhaoxi.Open_source_Android.common.view.DragFloatActionButton;
import com.zhaoxi.Open_source_Android.common.view.refresh.MyPtrFrameLayout;
import com.zhaoxi.Open_source_Android.db.greendao.AssetsBeanDao;
import com.zhaoxi.Open_source_Android.db.greendao.SimpleAssetBeanDao;
import com.zhaoxi.Open_source_Android.libs.tools.CommonDilogTool;
import com.zhaoxi.Open_source_Android.libs.tools.MainChangePagerHomeListener;
import com.zhaoxi.Open_source_Android.libs.tools.SystemLog;
import com.zhaoxi.Open_source_Android.libs.utils.ChangeLanguageUtil;
import com.zhaoxi.Open_source_Android.libs.utils.ColdWalletUtil;
import com.zhaoxi.Open_source_Android.libs.utils.CollectionUtil;
import com.zhaoxi.Open_source_Android.libs.utils.DateUtilSL;
import com.zhaoxi.Open_source_Android.libs.utils.SharedPreferencesUtil;
import com.zhaoxi.Open_source_Android.libs.utils.SlinUtil;
import com.zhaoxi.Open_source_Android.libs.utils.StrUtil;
import com.zhaoxi.Open_source_Android.net.BaseBet.NetResultCallBack;
import com.zhaoxi.Open_source_Android.net.Request.WalletBalanceRequest;
import com.zhaoxi.Open_source_Android.net.bean.AssetsBean;
import com.zhaoxi.Open_source_Android.net.bean.TotalAccountBean;
import com.zhaoxi.Open_source_Android.ui.activity.CreateWalletActivity;
import com.zhaoxi.Open_source_Android.ui.activity.MainActivity;
import com.zhaoxi.Open_source_Android.ui.activity.ReceivablesActivity;
import com.zhaoxi.Open_source_Android.ui.activity.ScaningActivity;
import com.zhaoxi.Open_source_Android.ui.activity.SendRedPacketsActivity;
import com.zhaoxi.Open_source_Android.ui.activity.SwitchAddressActivity;
import com.zhaoxi.Open_source_Android.ui.activity.TokenDetail721Activity;
import com.zhaoxi.Open_source_Android.ui.activity.TokenDetailActivity;
import com.zhaoxi.Open_source_Android.ui.activity.TokenManagerActivity;
import com.zhaoxi.Open_source_Android.ui.activity.TransferActivity;
import com.zhaoxi.Open_source_Android.ui.adapter.MainHomeAdapter;
import com.zhaoxi.Open_source_Android.web3.bean.WalletBean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import in.srain.cube.views.ptr.PtrFrameLayout;

public class MainHomeFragment extends BaseFragment implements MyPtrFrameLayout.OnRefreshListener {
    /**
     * 切换钱包
     */
    private static final int REQUEST_SWITCH_WALLET_CODE = 0x00;
    /**
     * 扫码
     */
    private static final int TYPE_SCAN = 0x01;
    /**
     * 收款
     */
    private static final int TYPE_COLLECTION = 0x02;
    /**
     * 转账
     */
    private static final int TYPE_TRANSFER = 0x03;
    /**
     * 切换地址
     */
    private static final int TYPE_SWITCH_ADDRESS = 0x04;
    /**
     * 发送红包
     */
    private static final int TYPE_SEND_RED_PACKET = 0x05;
    /**
     * 代币管理
     */
    private static final int TYPE_TOKEN_MANAGER = 0x06;

    Unbinder mUnbinder;
    @BindView(R.id.recycler_with_refresh_ptr_frame_layout)
    MyPtrFrameLayout mRefreshPtrFrameLayout;
    @BindView(R.id.main_home_recycler_view)
    RecyclerView mMainHomeRecyclerView;
    @BindView(R.id.main_activity_circle_button)
    DragFloatActionButton mFloatActionBtn;
    private MainActivity mMainActivity;
    private WalletBean mWalletBean;

    private MainHomeAdapter mMainHomeAdapter;
    private AssetsBeanDao mAssetsBeanDao;
    private AssetsBean mHpbBean;

    private List<AssetsBean> assetsBeans = new ArrayList<>();
    private List<AssetsBean> tempList = new ArrayList<>();
    private List<AssetsBean> mDataList = new ArrayList<>();

    private String mDefaultAddress;
    private int mWalletType;
    private static final String PAGE_NAME = MainHomeFragment.class.getSimpleName();
    // 是否跳转至切换钱包
    private boolean isSkipSwitchWallet = false;
    // 是否是刷新
    private boolean isRefresh = false;
    // 是否是跳转代币详情页
    private boolean isSkipTokenDetail = false;
    private SimpleAssetBeanDao mSimpleAssetBeanDao;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_main_home, container, false);
        mUnbinder = ButterKnife.bind(this, contentView);
        mMainActivity = (MainActivity) getActivity();
        mMainActivity.setChangeHomeViewPager(new MainChangePagerHomeListener() {
            @Override
            public void changeHomePager() {
                initFloatBtn();
                mMainHomeRecyclerView.smoothScrollToPosition(0);
            }
        });

        initViews();
        initFloatBtn();
        initListener();
        return contentView;
    }

    private void initListener() {
        mMainHomeAdapter.setOnMainHomeClickListener(new MainHomeAdapter.OnMainHomeClickListener() {

            @Override
            public void onItemClick(AssetsBean assetsBean) {
                isSkipTokenDetail = true;
                Intent intent;
                switch (assetsBean.getContractType()) {
                    case DAppConstants.TYPE_HRC_721:
                        // 代币详情
                        intent = new Intent(mMainActivity, TokenDetail721Activity.class);
                        intent.putExtra(TokenDetail721Activity.ITEM_ASSETS, assetsBean);
                        startActivity(intent);
                        break;
                    case DAppConstants.TYPE_HRC_20:
                    case DAppConstants.TYPE_HPB:
                        // 代币详情
                        intent = new Intent(mMainActivity, TokenDetailActivity.class);
                        intent.putExtra(TokenDetailActivity.ITEM_ASSETS, assetsBean);
                        startActivity(intent);
                        break;

                }

            }

            @Override
            public void refreshCompleted() {

            }

            @Override
            public void onTokenManager() {
                // 代币管理
                handleJump(TYPE_TOKEN_MANAGER);
            }

            @Override
            public void onSwitchAddress() {
                // 地址切换
                handleJump(TYPE_SWITCH_ADDRESS);
            }

            @Override
            public void updateData(String address, WalletBean walletBean) {
                mDefaultAddress = address;
                mWalletBean = walletBean;
                mWalletType = mWalletBean.getIsClodWallet();
            }

            @Override
            public void onScan() {
                // 扫码
                handleJump(TYPE_SCAN);
            }

            @Override
            public void onCollection() {
                // 收款
                handleJump(TYPE_COLLECTION);
            }

            @Override
            public void onTransfer() {
                // 转账
                handleJump(TYPE_TRANSFER);
            }

            @Override
            public void onSyncAssets() {
                // 同步资产
                SyncAssetsBean.ListDataBean listDataBean;
                SimpleAssetBean simpleAssetBean;
                SyncAssetsBean syncAssetsBean = new SyncAssetsBean();
                List<SyncAssetsBean.ListDataBean> listDataBeans = new ArrayList<>();
                if (assetsBeans != null && assetsBeans.size() > 0) {
                    for (AssetsBean assetsBean : assetsBeans) {
                        simpleAssetBean = new SimpleAssetBean();
                        listDataBean = new SyncAssetsBean.ListDataBean();
                        listDataBean.setTokenSymbol(assetsBean.getTokenSymbol());
                        listDataBean.setContractType(assetsBean.getContractType());
                        String token = SlinUtil.formatNumFromWei(new BigDecimal(assetsBean.getBalanceOfToken()), assetsBean.getDecimals());
                        listDataBean.setBalanceOfToken(token);

                        simpleAssetBean.setFrom(mDefaultAddress);
                        simpleAssetBean.setContractType(assetsBean.getContractType());
                        simpleAssetBean.setTokenSymbol(assetsBean.getTokenSymbol());
                        simpleAssetBean.setBalanceOfToken(assetsBean.getBalanceOfToken());
                        mSimpleAssetBeanDao.insertOrReplace(simpleAssetBean);

                        listDataBeans.add(listDataBean);
                    }

                    syncAssetsBean.setFrom(mDefaultAddress);
                    String curTimestamp = DateUtilSL.getCurTimestamp2();
                    syncAssetsBean.setUpdateDate(curTimestamp);
                    syncAssetsBean.setListData(listDataBeans);

                }

                String asyncContent = ColdWalletUtil.toJson(ColdWalletUtil.TYPE_SYNC_ASSETS,syncAssetsBean);
                SystemLog.D("onSyncAssets", "asyncContent = " + asyncContent);

                mMainActivity.showSyncAssetsQrCodeDialog(asyncContent);
            }

            @Override
            public void onCreateWallet() {
                //创建钱包
                startActivity(new Intent(mMainActivity, CreateWalletActivity.class));
            }
        });
    }

    private void handleJump(int type) {
        //判断是否有钱包
        if (StrUtil.isEmpty(mDefaultAddress)) {
            showCreateWalletDilaog();
            return;
        }

        switch (type) {
            case TYPE_SCAN:// 发送红包
                Intent it_scan = new Intent(mMainActivity, ScaningActivity.class);
                it_scan.putExtra(ScaningActivity.RESURE_TYPE, 5);
                it_scan.putExtra(ScaningActivity.RESURE_WALLET_TYPE, mWalletType);
                startActivity(it_scan);
                break;
            case TYPE_COLLECTION:// 收款
                startActivity(new Intent(mMainActivity, ReceivablesActivity.class));
                break;
            case TYPE_TRANSFER:// 转账
//                if (mWalletType == 0) {
                Intent goto_transfer = new Intent(mMainActivity, TransferActivity.class);
                goto_transfer.putExtra(TransferActivity.ADDRESS, "");
                goto_transfer.putExtra(TransferActivity.TRANSFER_TYPE, "");
                startActivity(goto_transfer);
//                } else {
//                    startActivity(new Intent(mMainActivity, ColdTransferActivity.class));
//                }
                break;
            case TYPE_SWITCH_ADDRESS://切换钱包
                startActivityForResult(new Intent(mMainActivity, SwitchAddressActivity.class), REQUEST_SWITCH_WALLET_CODE);
                break;
            case TYPE_SEND_RED_PACKET://发红包
                if (mWalletType == 0) {
                    startActivity(new Intent(mMainActivity, SendRedPacketsActivity.class));
                } else
                    DappApplication.getInstance().showToast(getResources().getString(R.string.main_send_redpackets_txt_01));
                break;
            case TYPE_TOKEN_MANAGER:// 代币管理
                startActivity(new Intent(mMainActivity, TokenManagerActivity.class));
                break;
        }
    }

    @OnClick({R.id.main_activity_circle_button})
    public void onViewClicked(View view) {
        //发红包
        if (view.getId() == R.id.main_activity_circle_button) {
            handleJump(TYPE_SEND_RED_PACKET);
        }
    }


    private void initFloatBtn() {
        int floatBtn = SharedPreferencesUtil.getSharePreInt(mMainActivity, SharedPreferencesUtil.CHANGE_XUANFUBTN_STYLE);
        if (floatBtn == 0) {
            mFloatActionBtn.setVisibility(View.VISIBLE);
        } else {
            mFloatActionBtn.setVisibility(View.GONE);
        }
    }

    private void initViews() {
        MobclickAgent.openActivityDurationTrack(false);
        mRefreshPtrFrameLayout.setLastUpdateTimeRelateObject(this);
        mRefreshPtrFrameLayout.setUltraPullToRefresh(this, mMainHomeRecyclerView);
        mRefreshPtrFrameLayout.changeWhiteBackgroud();
        mAssetsBeanDao = DappApplication.getInstance().getDaoSession().getAssetsBeanDao();
        mSimpleAssetBeanDao = DappApplication.getInstance().getDaoSession().getSimpleAssetBeanDao();
        mMainHomeAdapter = new MainHomeAdapter(mMainActivity, assetsBeans);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mMainActivity);
        mMainHomeRecyclerView.setLayoutManager(linearLayoutManager);
        mMainHomeRecyclerView.setAdapter(mMainHomeAdapter);
        mMainHomeRecyclerView.smoothScrollToPosition(0);
    }

    private void initData() {
        mDefaultAddress = SharedPreferencesUtil.getSharePreString(mMainActivity, SharedPreferencesUtil.CHOOSE_WALLET_ADDRESS);
        if (TextUtils.isEmpty(mDefaultAddress)) {
            return;
        }

        new WalletBalanceRequest(mDefaultAddress).doRequest(mMainActivity, new NetResultCallBack() {
            @Override
            public void onSuccess(JSONArray jsonArray) {
                if (jsonArray.get(2) != null) {
                    assetsBeans.clear();
                    mDataList.clear();
                    tempList.clear();
                    TotalAccountBean totalAccountBean = JSON.parseObject(jsonArray.get(2).toString(), TotalAccountBean.class);
                    String ptCnyValue = totalAccountBean.getPtCnyValue();
                    String ptUsdValue = totalAccountBean.getPtUsdValue();
                    int currentChoseUnit = SharedPreferencesUtil.getSharePreInt(DappApplication.getInstance().getApplicationContext(), SharedPreferencesUtil.CHANGE_COIN_UNIT);
                    if (currentChoseUnit == 0) {
                        int language = SharedPreferencesUtil.getSharePreInt(DappApplication.getInstance().getApplicationContext(), SharedPreferencesUtil.CHANGE_LANGUAGE_NAME);
                        if (language == 0) {
                            String sysLanguage = Locale.getDefault().getLanguage();
                            if ("zh".equals(sysLanguage)) {
                                mMainHomeAdapter.setTotalAssets(ptCnyValue);
                            } else {
                                mMainHomeAdapter.setTotalAssets(ptUsdValue);
                            }
                        } else {
                            if (language == ChangeLanguageUtil.CHANGE_LANGUAGE_CHINA) {
                                mMainHomeAdapter.setTotalAssets(ptCnyValue);
                            } else {
                                mMainHomeAdapter.setTotalAssets(ptUsdValue);
                            }
                        }
                    } else {
                        if (currentChoseUnit == 1) {// 人民币
                            mMainHomeAdapter.setTotalAssets(ptCnyValue);
                        } else {// 美元
                            mMainHomeAdapter.setTotalAssets(ptUsdValue);
                        }
                    }

                    List<TotalAccountBean.AssetsInfo> assetsInfos = totalAccountBean.getList();
                    if (!CollectionUtil.isCollectionEmpty(assetsInfos)) {
                        for (TotalAccountBean.AssetsInfo assetsInfo : assetsInfos) {
                            AssetsBean assetsBean = new AssetsBean(
                                    assetsInfo.getId()
                                    , assetsInfo.getTokenSymbol()
                                    , assetsInfo.getTokenSymbolImageUrl()
                                    , assetsInfo.getTokenName()
                                    , assetsInfo.getContractCreater()
                                    , assetsInfo.getContractAddress()
                                    , assetsInfo.getTokenTotalSupply()
                                    , assetsInfo.getContractType()
                                    , assetsInfo.getTransfersNum()
                                    , assetsInfo.getCnyPrice()
                                    , assetsInfo.getUsdPrice()
                                    , assetsInfo.getCnyTotalValue()
                                    , assetsInfo.getUsdTotalValue()
                                    , true
                                    , assetsInfo.getBalanceOfToken()
                                    , Integer.valueOf(assetsInfo.getDecimals())
                            );

                            // 临时存放后台的数据集合
                            tempList.add(assetsBean);
                            mDataList.clear();
                            mDataList.addAll(mAssetsBeanDao.queryBuilder().where(AssetsBeanDao.Properties.Id.eq(assetsBean.getId())).list());
                            if (mDataList.size() > 0) {
                                AssetsBean bean = mDataList.get(0);
                                bean.setBalanceOfToken(assetsBean.getBalanceOfToken());
                                bean.setCnyTotalValue(assetsBean.getCnyTotalValue());
                                bean.setUsdTotalValue(assetsBean.getUsdTotalValue());
                                bean.setTokenSymbolImageUrl(assetsBean.getTokenSymbolImageUrl());
                                bean.setCnyPrice(assetsBean.getCnyPrice());
                                bean.setUsdPrice(assetsBean.getUsdPrice());
                                bean.setDecimals(assetsBean.getDecimals());
                                bean.setTokenSymbol(assetsBean.getTokenSymbol());
                                bean.setId(assetsBean.getId());
                                bean.setTransfersNum(assetsBean.getTransfersNum());
                                bean.setContractType(assetsBean.getContractType());
                                bean.setTokenTotalSupply(assetsBean.getTokenTotalSupply());
                                bean.setContractAddress(assetsBean.getContractAddress());
                                bean.setContractCreater(assetsBean.getContractCreater());
                                // 更新数据库中的数据
                                mAssetsBeanDao.update(bean);
                            } else {
                                // 如果在数据库中不存在，则插入数据库,除HPB外
                                if (DAppConstants.TYPE_HPB.equals(assetsBean.getId())) {
                                    mHpbBean = assetsBean;
                                } else {
                                    mAssetsBeanDao.insert(assetsBean);
                                }

                            }

                        }


                        // 同步后台配置的数据
                        mDataList.clear();
                        mDataList.addAll(mAssetsBeanDao.queryBuilder().list());
                        for (AssetsBean bean : mDataList) {
                            for (int i = 0; i < tempList.size(); i++) {
                                if (bean.getId().equals(tempList.get(i).getId())) {
                                    // 匹配到
                                    break;
                                }

                                if (i == tempList.size() - 1) {
                                    // 未匹配到
                                    mAssetsBeanDao.delete(bean);
                                }

                            }

                        }


                        mDataList.clear();
                        mDataList.addAll(mAssetsBeanDao.queryBuilder().list());
                        SystemLog.D("assetsBeans", "mDataList = " + mDataList.toString());
                        for (AssetsBean bean : mDataList) {
                            if (bean.isSelected()) {
                                assetsBeans.add(bean);
                            }
                        }

                        assetsBeans.add(0, mHpbBean);
                        mMainHomeAdapter.notifyDataSetChanged();
                    }

                    if (mRefreshPtrFrameLayout != null)
                        mRefreshPtrFrameLayout.refreshComplete();
                }

                if (isSkipSwitchWallet || isRefresh) {
                    isSkipSwitchWallet = false;
                    isRefresh = false;
                    if(mMainHomeRecyclerView != null){
                        mMainHomeRecyclerView.smoothScrollToPosition(0);
                    }
                }

            }

            @Override
            public void onError(String error) {
                mDataList.clear();
                tempList.clear();
                mMainHomeAdapter.setTotalAssets("0");
                mMainHomeAdapter.notifyDataSetChanged();

                if (mRefreshPtrFrameLayout != null)
                    mRefreshPtrFrameLayout.refreshComplete();
                DappApplication.getInstance().showToast(error);
            }
        });

    }

    /**
     * 创建钱包对话框
     */
    private void showCreateWalletDilaog() {
        CommonDilogTool dialogTool = new CommonDilogTool(mMainActivity);
        dialogTool.show(null, mMainActivity.getResources().getString(R.string.dialog_common_title_message_nowallet), null,
                mMainActivity.getResources().getString(R.string.dailog_psd_btn_cancle), null,
                mMainActivity.getResources().getString(R.string.dialog_common_title_message_go_create_wallet), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(mMainActivity, CreateWalletActivity.class);
                        startActivity(intent);
                        dialog.dismiss();
                    }
                });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(PAGE_NAME);
        SystemLog.D("onResume", "PAGE_NAME = " + PAGE_NAME);
        SystemLog.D("onResume", "isSkipTokenDetail = " + isSkipTokenDetail);
        // 自动刷新
        if (!isSkipTokenDetail) {
            mRefreshPtrFrameLayout.autoRefresh();
        }

        isSkipTokenDetail = false;
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(PAGE_NAME);
    }

    @Override
    public void refresh(PtrFrameLayout frame) {
        SystemLog.D("refresh", "refresh = " + isSkipSwitchWallet);
        isRefresh = true;
        initData();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_SWITCH_WALLET_CODE) {
                SystemLog.D("onActivityResult", "isSkipSwitchWallet = " + isSkipSwitchWallet);
                isSkipSwitchWallet = true;
            }
        }
    }
}
