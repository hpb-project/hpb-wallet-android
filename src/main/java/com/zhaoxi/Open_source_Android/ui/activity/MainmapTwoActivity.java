package com.zhaoxi.Open_source_Android.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.base.BaseTitleBarActivity;
import com.zhaoxi.Open_source_Android.common.view.refresh.MyPtrFrameLayout;
import com.zhaoxi.Open_source_Android.db.CreateDbWallet;
import com.zhaoxi.Open_source_Android.ui.adapter.MainmapManagerAdapter;
import com.zhaoxi.Open_source_Android.web3.bean.WalletBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrFrameLayout;

public class MainmapTwoActivity extends BaseTitleBarActivity {

    @BindView(R.id.activity_main_map_two_recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.activity_main_map_two_ptrframelayout)
    MyPtrFrameLayout mPtrFrameLayout;

    private List<WalletBean> mListData = new ArrayList<>();
    private List<WalletBean> mDbListData = new ArrayList<>();
    private CreateDbWallet mCreateDbWallet;
    private MainmapManagerAdapter mAdapter;
    private boolean isLoading = false; // 标注是否正在加载 防止多次加载

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainmap_two);
        ButterKnife.bind(this);
        mCreateDbWallet = new CreateDbWallet(this);
        setTitle(R.string.activity_main_map_txt_03,true);
        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //获取本地钱包
        initDatas();
    }

    private void initViews() {
        mPtrFrameLayout.setLastUpdateTimeRelateObject(this);
        mPtrFrameLayout.setUltraPullToRefresh(mRefreshListener, mRecyclerView);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MainmapManagerAdapter(this, mListData);
        mRecyclerView.setAdapter(mAdapter);
    }

    /**
     * 下拉刷新监听事件
     */
    private MyPtrFrameLayout.OnRefreshListener mRefreshListener = new MyPtrFrameLayout.OnRefreshListener() {
        @Override
        public void refresh(PtrFrameLayout frame) {
            if (!isLoading) {
                initDatas();
            }
        }
    };


    private void initDatas() {
        isLoading = true;
        if (mDbListData.size() != 0) {
            mDbListData.clear();
        }
        if (mListData.size() != 0) {
            mListData.clear();
        }
        mDbListData = mCreateDbWallet.queryAllMapWallet(this);
        if (mDbListData.size() != 0) {
            for (int i = 0; i < mDbListData.size(); i++) {
                WalletBean walletBean = mDbListData.get(i);
                walletBean.setMoney("0");
                mListData.add(walletBean);
            }
            mAdapter.notifyDataSetChanged();
        }
        isLoading = false;
        mPtrFrameLayout.refreshComplete();
    }
}
