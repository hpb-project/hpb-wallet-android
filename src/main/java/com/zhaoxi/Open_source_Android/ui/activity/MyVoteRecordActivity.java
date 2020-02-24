package com.zhaoxi.Open_source_Android.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.zhaoxi.Open_source_Android.DAppConstants;
import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.base.BaseTitleBarActivity;
import com.zhaoxi.Open_source_Android.common.base.DappApplication;
import com.zhaoxi.Open_source_Android.common.dialog.CommonPsdPop;
import com.zhaoxi.Open_source_Android.common.view.recycler.LoadMoreRecyclerAdapter;
import com.zhaoxi.Open_source_Android.common.view.recycler.LoadMoreRecyclerView;
import com.zhaoxi.Open_source_Android.common.view.refresh.MyPtrFrameLayout;
import com.zhaoxi.Open_source_Android.libs.tools.ExportWalletAsyncTask;
import com.zhaoxi.Open_source_Android.libs.tools.SystemLog;
import com.zhaoxi.Open_source_Android.libs.utils.CollectionUtil;
import com.zhaoxi.Open_source_Android.libs.utils.SharedPreferencesUtil;
import com.zhaoxi.Open_source_Android.net.BaseBet.CommonResultListener;
import com.zhaoxi.Open_source_Android.net.BaseBet.NetResultCallBack;
import com.zhaoxi.Open_source_Android.net.Request.GetHpbFeeRequest;
import com.zhaoxi.Open_source_Android.net.Request.VoteCancelRequest;
import com.zhaoxi.Open_source_Android.net.Request.VoteHistoryRequest;
import com.zhaoxi.Open_source_Android.net.bean.MapEthBean;
import com.zhaoxi.Open_source_Android.net.bean.VoteBean;
import com.zhaoxi.Open_source_Android.ui.adapter.MyVoteAdapter;
import com.zhaoxi.Open_source_Android.web3.utils.Convert;
import com.zhaoxi.Open_source_Android.web3.utils.TransferUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrFrameLayout;

public class MyVoteRecordActivity extends BaseTitleBarActivity {

    @BindView(R.id.activity_myvote_record_list)
    LoadMoreRecyclerView mRecyclerView;
    @BindView(R.id.activity_myvote_record_refresh)
    MyPtrFrameLayout mHistoryRefresh;
    @BindView(R.id.root_myvote_record)
    LinearLayout mRootView;

    private LoadMoreRecyclerAdapter mListAdapter;

    private boolean isLoading = false; // 标注是否正在加载 防止多次加载
    private int mCurrentPage = 1;
    private int mTotalPages;

    private MyVoteAdapter mMyVoteAdapter;
    private List<VoteBean.VoteInfo> mListDatas = new ArrayList<>();
    private VoteBean.MyhistoryVote mYVote;
    private boolean isFisrtIn = true;

    private BigInteger mGasPrice;
    private BigInteger mGasLimit;
    private BigInteger mNonce;
    private String voteContractAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_vote_record);
        ButterKnife.bind(this);
        initViews();
    }

    private void initViews() {
        setTitleTransparent();
        setTitle(R.string.activity_vote_txt_01,true);
        mHistoryRefresh.setLastUpdateTimeRelateObject(this);
        mHistoryRefresh.setUltraPullToRefresh(mRefreshListener, mRecyclerView);
        mHistoryRefresh.changeBackgroud();

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mMyVoteAdapter = new MyVoteAdapter(this, mListDatas, mYVote,isFisrtIn);
        mMyVoteAdapter.setOnCexiaoListener(new MyVoteAdapter.OnCexiaoListener() {
            @Override
            public void setClick(String blockHash, int poll, String toAddress) {
                showprivateKeyDialog(blockHash, poll, toAddress);
            }
        });
        mListAdapter = new LoadMoreRecyclerAdapter(this, mMyVoteAdapter, false);

        mRecyclerView.setAdapter(mListAdapter);
        // 设置滑到底部加载更多
        mRecyclerView.setListener(mLoadMoreListener);
        voteContractAddress = SharedPreferencesUtil.getSharePreString(this,DAppConstants.VOTE_CONTRACT_ADDRESS);
        initDatas(mCurrentPage);
    }

    private void initDatas(int pageNum) {
        isLoading = true;
        if (isFisrtIn) {
            showProgressDialog();
            isFisrtIn = false;
        }

        String address = SharedPreferencesUtil.getSharePreString(this, SharedPreferencesUtil.CHOOSE_WALLET_ADDRESS);
        new VoteHistoryRequest(address, pageNum).doRequest(this, new NetResultCallBack() {
            @Override
            public void onSuccess(JSONArray jsonArray) {
                VoteBean voteBean = JSON.parseObject(jsonArray.get(2).toString(), VoteBean.class);
                if (pageNum == 1) {
                    mListDatas.clear();
                    mRecyclerView.setSmoothPosition(0);
                }
                mYVote = voteBean.getExtention();
                mMyVoteAdapter.setTopData(mYVote,isFisrtIn);
                if (CollectionUtil.isCollectionEmpty(voteBean.getList())) {
                    mListAdapter.setIsNeedLoadMore(false);
                    mListAdapter.notifyDataSetChanged();
                } else {
                    mTotalPages = voteBean.getPages();
                    mListDatas.addAll(voteBean.getList());
                    mListAdapter.setIsNeedLoadMore(mCurrentPage < mTotalPages ? true : false);// 设置是否显示加载更多Item
                    mCurrentPage++;
                    mListAdapter.notifyDataSetChanged();
                }
                mHistoryRefresh.refreshComplete();
                isLoading = false;
                dismissProgressDialog();
            }

            @Override
            public void onError(String error) {
                dismissProgressDialog();
                mHistoryRefresh.refreshComplete();
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
                initDatas(1);
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
                initDatas(mCurrentPage);
            }
        }
    };

    /**
     * 导出私钥--弹出输入密码框
     */
    private void showprivateKeyDialog(String blockHash, int poll, String toAddress) {
        String address = SharedPreferencesUtil.getSharePreString(MyVoteRecordActivity.this, SharedPreferencesUtil.CHOOSE_WALLET_ADDRESS);
        CommonPsdPop commonPsdPop = new CommonPsdPop(this,mRootView);
        commonPsdPop.setHandlePsd(new CommonPsdPop.HandlePsd() {
            @Override
            public void getInputPsd(String psd) {
                showProgressDialog();
                ExportWalletAsyncTask asyncTask = new ExportWalletAsyncTask(MyVoteRecordActivity.this, address, psd, 10);
                asyncTask.setOnResultListener(new ExportWalletAsyncTask.OnResultExportListener() {
                    @Override
                    public void setOnResultListener(String result) {
                        if (result.startsWith("Failed") || result.contains("失败")) {
                            dismissProgressDialog();
                            DappApplication.getInstance().showToast(getResources().getString(R.string.transfer_activity_dialog_txt_06));
                        } else {
                            //乘以18
                            BigDecimal valueData = Convert.toWei(new BigDecimal(poll), Convert.Unit.ETHER);
                            new GetHpbFeeRequest(address).doRequest(MyVoteRecordActivity.this, new CommonResultListener(MyVoteRecordActivity.this) {
                                @Override
                                public void onSuccess(JSONArray jsonArray) {
                                    super.onSuccess(jsonArray);
                                    MapEthBean mapEthInfo = JSON.parseObject(jsonArray.get(2).toString(), MapEthBean.class);
                                    mGasPrice = new BigInteger(mapEthInfo.getGasPrice());
                                    mGasLimit = new BigInteger("20000000");
                                    mNonce = mapEthInfo.getNonce();
                                    String signedData = TransferUtils.tokenVote("cancelVoteForCandidate", mNonce, mGasPrice, mGasLimit, result,
                                            voteContractAddress, toAddress,address, valueData);
                                    SystemLog.D("SIGN", signedData);
                                    new VoteCancelRequest(blockHash, signedData).doRequest(MyVoteRecordActivity.this, new CommonResultListener(MyVoteRecordActivity.this) {
                                        @Override
                                        public void onSuccess(JSONArray jsonArray) {
                                            super.onSuccess(jsonArray);
                                            dismissProgressDialog();
                                            mCurrentPage = 1;
                                            initDatas(mCurrentPage);
                                        }
                                    });
                                }
                            });
                        }
                    }
                });
                asyncTask.execute();
            }
        });
        commonPsdPop.show(null,null);
    }
}
