package com.zhaoxi.Open_source_Android.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.WindowManager;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.zhaoxi.Open_source_Android.DAppConstants;
import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.base.BaseActivity;
import com.zhaoxi.Open_source_Android.common.base.DappApplication;
import com.zhaoxi.Open_source_Android.common.dialog.CommonCopyDialog;
import com.zhaoxi.Open_source_Android.common.view.recycler.LoadMoreRecyclerAdapter;
import com.zhaoxi.Open_source_Android.common.view.recycler.LoadMoreRecyclerView;
import com.zhaoxi.Open_source_Android.common.view.refresh.MyPtrFrameLayout;
import com.zhaoxi.Open_source_Android.db.CreateDbWallet;
import com.zhaoxi.Open_source_Android.db.DBManager;
import com.zhaoxi.Open_source_Android.libs.utils.CollectionUtil;
import com.zhaoxi.Open_source_Android.libs.utils.SharedPreferencesUtil;
import com.zhaoxi.Open_source_Android.libs.utils.StrUtil;
import com.zhaoxi.Open_source_Android.net.BaseBet.NetResultCallBack;
import com.zhaoxi.Open_source_Android.net.Request.TotalAccountRequest;
import com.zhaoxi.Open_source_Android.net.Request.TransactionHistoryRequest;
import com.zhaoxi.Open_source_Android.net.UrlContext;
import com.zhaoxi.Open_source_Android.net.bean.TranferRecordBean;
import com.zhaoxi.Open_source_Android.ui.adapter.TotalAssetsAdapter;
import com.zhaoxi.Open_source_Android.web3.bean.WalletBean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * 总资产
 *
 * @author zhutt on 2018-06-14
 */
public class TotalAssetsActivity extends BaseActivity {
    @BindView(R.id.recycler_with_refresh_ptr_frame_layout)
    MyPtrFrameLayout mRefreshPtrFrameLayout;
    @BindView(R.id.transation_record_list)
    LoadMoreRecyclerView mContentView;

    private LoadMoreRecyclerAdapter mListAdapter;
    private List<TranferRecordBean.TransferInfo> mDataList = new ArrayList<>();
    private TotalAssetsAdapter TotalAssetsAdapter;
    private BigDecimal mCurMOney = new BigDecimal("0");
    private CreateDbWallet mCreateDbWallet;
    private WalletBean mWalletBean;
    public static String mDefultAddress;
    private int mEyesStatus;

    private boolean isLoading = false;
    private int mCurrentPage = 1;
    private int mTotalPages;
    private boolean isFirstIn = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total_assets);
        ButterKnife.bind(this);

        mDefultAddress = SharedPreferencesUtil.getSharePreString(this, SharedPreferencesUtil.CHOOSE_WALLET_ADDRESS);
        mCreateDbWallet = new CreateDbWallet(this);
        mWalletBean = mCreateDbWallet.queryWallet(this,mDefultAddress);
        mEyesStatus = SharedPreferencesUtil.getSharePreInt(this, DAppConstants.MONEY_EYES_STATUS);;
        mRefreshPtrFrameLayout.setLastUpdateTimeRelateObject(this);
        mRefreshPtrFrameLayout.setUltraPullToRefresh(mRefreshListener, mContentView);
        mRefreshPtrFrameLayout.changeThemeBackgroud();

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mContentView.setLayoutManager(mLayoutManager);
        TotalAssetsAdapter = new TotalAssetsAdapter(this, mDataList, mCurMOney, mEyesStatus);
        TotalAssetsAdapter.setOnBackListener(new TotalAssetsAdapter.OnBackListener() {
            @Override
            public void setClickBack() {
                finish();
            }
        });

        mListAdapter = new LoadMoreRecyclerAdapter(this, TotalAssetsAdapter, false);

        mContentView.setAdapter(mListAdapter);
        // 设置滑到底部加载更多
        mContentView.setListener(mLoadMoreListener);
        initData(mDefultAddress);
    }

    private void initData(String address) {
        new TotalAccountRequest(address).doRequest(this, new NetResultCallBack() {
            @Override
            public void onSuccess(JSONArray jsonArray) {
                String status = (String) jsonArray.get(0);
                if ("000000".equals(status)) {//代表处理成功
                    mCurMOney = new BigDecimal("" + jsonArray.get(2));
                    TotalAssetsAdapter.setTopData(mCurMOney, mEyesStatus);
                }
            }

            @Override
            public void onError(String error) {
                DappApplication.getInstance().showToast(error);
            }
        });

        initRecordData(mCurrentPage);
    }

    private void initRecordData(int pageNum) {
        String address = SharedPreferencesUtil.getSharePreString(this, SharedPreferencesUtil.CHOOSE_WALLET_ADDRESS);
        if (isFirstIn && pageNum == 1 && mDefultAddress.equals(address)) {
            String datas = DBManager.queryHistory(address);
            if (!StrUtil.isEmpty(datas)) {
                mDataList.clear();
                mContentView.setSmoothPosition(0);
                TranferRecordBean tranferRecordBean = JSON.parseObject(datas, TranferRecordBean.class);
                mDataList.addAll(tranferRecordBean.getList());
                mListAdapter.notifyDataSetChanged();
                mRefreshPtrFrameLayout.refreshComplete();
            }
        }
        isLoading = true;
        if (pageNum == 1) {
            showProgressDialog();
        }
        new TransactionHistoryRequest(mDefultAddress, pageNum, UrlContext.Url_getTransactionHistory.getContext(), 0)
                .doRequest(this, new NetResultCallBack() {
                    @Override
                    public void onSuccess(JSONArray jsonArray) {
                        TranferRecordBean tranferRecordBean = JSON.parseObject(jsonArray.get(2).toString(), TranferRecordBean.class);
//                        if (pageNum == 1) {
//                            mDataList.clear();
//                            mContentView.setSmoothPosition(0);
//                        }
                        if (CollectionUtil.isCollectionEmpty(tranferRecordBean.getList())) {
                            mListAdapter.setIsNeedLoadMore(false);
                            mListAdapter.notifyDataSetChanged();
                        } else {
                            mTotalPages = tranferRecordBean.getPages();
                            mDataList.addAll(tranferRecordBean.getList());
                            mListAdapter.setIsNeedLoadMore(mCurrentPage < mTotalPages ? true : false);// 设置是否显示加载更多Item
                            mCurrentPage++;
                            mListAdapter.notifyDataSetChanged();
                            isFirstIn = false;
                        }

                        mRefreshPtrFrameLayout.refreshComplete();
                        isLoading = false;
                        dismissProgressDialog();
                    }

                    @Override
                    public void onError(String error) {
                        dismissProgressDialog();
                        mRefreshPtrFrameLayout.refreshComplete();
                        isLoading = false;
                        DappApplication.getInstance().showToast(error);
                    }
                });
    }

    private MyPtrFrameLayout.OnRefreshListener mRefreshListener = new MyPtrFrameLayout.OnRefreshListener() {
        @Override
        public void refresh(PtrFrameLayout frame) {
            if (!isLoading) {
                mCurrentPage = 1;
                initData(mDefultAddress);
            }
        }
    };

    /**
     * 加载更多
     */
    private LoadMoreRecyclerView.OnLoadMoreListener mLoadMoreListener = new LoadMoreRecyclerView.OnLoadMoreListener() {
        @Override
        public void loadMore() {
            // 设置滑到底部加载更多
            if (!isLoading && mCurrentPage <= mTotalPages) {
                initRecordData(mCurrentPage);
            }
        }
    };

    @OnClick({R.id.layout_total_assets_recieve, R.id.layout_total_assets_transfer})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_total_assets_recieve:
                handleJump(1,true);
                break;
            case R.id.layout_total_assets_transfer:
                handleJump(2,true);
                break;
        }
    }

    private void handleJump(int type, boolean isNeedCopy) {
        //判断是否需要判断助记词 是，有助记词就需要弹出备份助记词对话框
        if (isNeedCopy && !StrUtil.isEmpty(mWalletBean.getMnemonic())) {
            showGoCopyDialog();
            return;
        }

        switch (type) {
            case 1:
                startActivity(new Intent(this, ReceivablesActivity.class));
                break;
            case 2:
                if(mCreateDbWallet.isColdWallet(this,mDefultAddress)){
                    startActivity(new Intent(this, ColdTransferActivity.class));
                }else{
                    Intent goto_transfer = new Intent(this, TransferActivity.class);
                    goto_transfer.putExtra(TransferActivity.ADDRESS, "");
                    startActivity(goto_transfer);
                }
                break;
        }
    }

    /**
     * 提醒备份助记词对话框
     */
    private void showGoCopyDialog() {
        CommonCopyDialog.Builder builder = new CommonCopyDialog.Builder(this);
        builder.setPositiveButton(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent goto_details = new Intent(TotalAssetsActivity.this, WalletDetailsActivity.class);
                goto_details.putExtra(WalletDetailsActivity.WALLET_TITLE,mWalletBean.getWalletBName());
                goto_details.putExtra(WalletDetailsActivity.WALLET_ADDRESS, mWalletBean.getAddress());
                goto_details.putExtra(WalletDetailsActivity.WALLET_SHOW_DAILOG, true);
                startActivity(goto_details);
            }
        });
        CommonCopyDialog dialog = builder.create();
        dialog.show();

        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = (int) (getWindowManager().getDefaultDisplay().getWidth() * 0.8);
        dialog.getWindow().setAttributes(params);
    }
}
