package com.zhaoxi.Open_source_Android.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.base.BaseTitleBarActivity;
import com.zhaoxi.Open_source_Android.common.base.DappApplication;
import com.zhaoxi.Open_source_Android.db.CreateDbWallet;
import com.zhaoxi.Open_source_Android.net.BaseBet.NetResultCallBack;
import com.zhaoxi.Open_source_Android.net.Request.GetListLegalBalancesRequest;
import com.zhaoxi.Open_source_Android.net.bean.WalletListBean;
import com.zhaoxi.Open_source_Android.ui.adapter.WalletManagerAdapter;
import com.zhaoxi.Open_source_Android.web3.bean.WalletBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 钱包管理
 *
 * @author zhutt on 2018-06-07
 */
public class WalletManagerActivity extends BaseTitleBarActivity {

    @BindView(R.id.wallet_manager_recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.wallet_manager_layout_no_wallet)
    LinearLayout mLayoutNoWallet;

    private List<WalletBean> mListData = new ArrayList<>();
    private List<WalletBean> mDbListData = new ArrayList<>();
    private List<String> mListAddress = new ArrayList<>();
    private CreateDbWallet mCreateDbWallet;
    private WalletManagerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_manager);
        ButterKnife.bind(this);
        mCreateDbWallet = new CreateDbWallet(this);
        setTitle(R.string.main_me_manager_txt_wallet, true);
        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //获取本地钱包
        initDatas();
    }

    private void initViews() {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new WalletManagerAdapter(this, mListData);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initDatas() {
        if (mDbListData.size() != 0) {
            mDbListData.clear();
        }
        if (mListData.size() != 0) {
            mListData.clear();
        }
        mDbListData = mCreateDbWallet.queryAllWallet(this);
        if (mDbListData.size() == 0) {
            mLayoutNoWallet.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        } else {
            mLayoutNoWallet.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            //获取余额
            getMoney();
        }
    }

    /**
     * 获取相应钱包的金额
     */
    private void getMoney() {
        showProgressDialog();
        mListAddress = mCreateDbWallet.queryAllAddress();
        new GetListLegalBalancesRequest(mListAddress).doRequest(this, new NetResultCallBack() {
            @Override
            public void onSuccess(JSONArray jsonArray) {
                if (jsonArray.get(2) == null) {
                    return;
                } else {
                    WalletListBean walletListBean = JSON.parseObject(jsonArray.get(2).toString(), WalletListBean.class);
                    List<WalletBean> hasMoneyData = walletListBean.getList();
                    if(hasMoneyData.size() == 0){
                        for (int i = 0; i < mDbListData.size(); i++) {
                            WalletBean walletBean = mDbListData.get(i);
                            walletBean.setMoney("0");
                            mListData.add(walletBean);
                        }
                    }else{
                        for (int i = 0; i < mDbListData.size(); i++) {
                            WalletBean walletBean = mDbListData.get(i);
                            for (int j = 0; j < hasMoneyData.size(); j++) {
                                WalletBean bean = hasMoneyData.get(j);
                                if (walletBean.getAddress().toLowerCase().equals(bean.getAddress().toLowerCase())) {
                                    walletBean.setMoney_c(bean.getCnyTotalValue());
                                    walletBean.setMoney_u(bean.getUsdTotalValue());
                                    mListData.add(walletBean);
                                    continue;
                                }
                            }
                        }

                    }
                    mAdapter.notifyDataSetChanged();
                }

                dismissProgressDialog();
            }

            @Override
            public void onError(String error) {
                for (int i = 0; i < mDbListData.size(); i++) {
                    WalletBean walletBean = mDbListData.get(i);
                    walletBean.setMoney("0");
                    mListData.add(walletBean);
                }
                mAdapter.notifyDataSetChanged();
                dismissProgressDialog();
                DappApplication.getInstance().showToast(error);
            }
        });
    }

    @OnClick({R.id.wallet_manager_create_wallet, R.id.wallet_manager_import_wallet, R.id.wallet_manager_txt_no_wallet})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.wallet_manager_create_wallet://创建钱包
                startActivity(new Intent(this, CreateWalletActivity.class));
                break;
            case R.id.wallet_manager_import_wallet://导入钱包
                startActivity(new Intent(this, ImportWalletTwoActivity.class));
                break;
            case R.id.wallet_manager_txt_no_wallet:
                startActivity(new Intent(this, CreateWalletActivity.class));
                break;
        }
    }
}
