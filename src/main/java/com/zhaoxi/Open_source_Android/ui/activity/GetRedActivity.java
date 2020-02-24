package com.zhaoxi.Open_source_Android.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.base.BaseTitleBarActivity;
import com.zhaoxi.Open_source_Android.common.base.DappApplication;
import com.zhaoxi.Open_source_Android.common.view.recycler.LoadMoreRecyclerAdapter;
import com.zhaoxi.Open_source_Android.common.view.recycler.LoadMoreRecyclerView;
import com.zhaoxi.Open_source_Android.common.view.refresh.MyPtrFrameLayout;
import com.zhaoxi.Open_source_Android.db.CreateDbWallet;
import com.zhaoxi.Open_source_Android.libs.tools.CommonDilogTool;
import com.zhaoxi.Open_source_Android.libs.utils.CollectionUtil;
import com.zhaoxi.Open_source_Android.libs.utils.SharedPreferencesUtil;
import com.zhaoxi.Open_source_Android.libs.utils.SlinUtil;
import com.zhaoxi.Open_source_Android.libs.utils.StrUtil;
import com.zhaoxi.Open_source_Android.net.BaseBet.CommonResultListener;
import com.zhaoxi.Open_source_Android.net.BaseBet.NetResultCallBack;
import com.zhaoxi.Open_source_Android.net.Request.GetRedDesRequest;
import com.zhaoxi.Open_source_Android.net.Request.RedDrawRequest;
import com.zhaoxi.Open_source_Android.net.bean.GetRedStatusBean;
import com.zhaoxi.Open_source_Android.net.bean.RedDetailsBean;
import com.zhaoxi.Open_source_Android.ui.adapter.GetRedAdapter;
import com.zhaoxi.Open_source_Android.ui.dialog.GetRedChooseWalletDialog;
import com.zhaoxi.Open_source_Android.web3.bean.WalletBean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrFrameLayout;

public class GetRedActivity extends BaseTitleBarActivity {
    public static final String RED_RECORD_ID = "GetRedActivity.RED_RECORD_ID";
    public static final String RED_RECORD_KEY = "GetRedActivity.RED_RECORD_KEY";

    @BindView(R.id.get_red_recycler_list)
    LoadMoreRecyclerView mContentView;
    @BindView(R.id.get_red_refresh_layout)
    MyPtrFrameLayout mRefreshLayout;
    @BindView(R.id.get_red_txt_des)
    TextView mTxtDes;

    private LoadMoreRecyclerAdapter mListAdapter;
    private boolean isLoading = false;
    private int mCurrentPage = 1;
    private int mTotalPages;
    public String mDefultAddress;
    private String mCurChooseAddres = "";
    private List<RedDetailsBean.RedDetails> mDataList = new ArrayList<>();
    private RedDetailsBean mRedBean = new RedDetailsBean();
    private GetRedAdapter mGetRedAdapter;
    private List<WalletBean> mWalletDatas = new ArrayList<>();
    private CreateDbWallet mCreateDbWallet;
    private int mFirstJuesePostition = 0;
    private String mTokenId;

    private String mRedId, mRedKey;
    private boolean isFirst = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_red);
        ButterKnife.bind(this);
        mRedId = getIntent().getStringExtra(RED_RECORD_ID);
        mRedKey = getIntent().getStringExtra(RED_RECORD_KEY);
        if (StrUtil.isEmpty(mRedId) || StrUtil.isEmpty(mRedKey)) {
            return;
        }
        mCreateDbWallet = new CreateDbWallet(this);

        initViews();
    }

    private void initViews() {
        setTitle("", true);

        mRefreshLayout.setLastUpdateTimeRelateObject(this);
        mRefreshLayout.setUltraPullToRefresh(mRefreshListener, mContentView);
        mRefreshLayout.changeFlshBackgroud(R.color.color_2E2F47);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mContentView.setLayoutManager(mLayoutManager);
        mGetRedAdapter = new GetRedAdapter(this, mDataList, mRedBean);
        mGetRedAdapter.setOnBtnChooseListener(new GetRedAdapter.OnBtnChooseListener() {
            @Override
            public void setOnChooseListener() {
                //判断是否有钱包
                mDefultAddress = SharedPreferencesUtil.getSharePreString(GetRedActivity.this, SharedPreferencesUtil.CHOOSE_WALLET_ADDRESS);
                if (StrUtil.isEmpty(mDefultAddress)) {
                    showCreateWalletDilaog();
                    return;
                }

                //选择钱包地址
                showChooseWallet();
            }
        });


        mListAdapter = new LoadMoreRecyclerAdapter(this, mGetRedAdapter, false);

        mContentView.setAdapter(mListAdapter);
        // 设置滑到底部加载更多
        mContentView.setListener(mLoadMoreListener);
        initData(1);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        mRedId = intent.getStringExtra(RED_RECORD_ID);
        mRedKey = intent.getStringExtra(RED_RECORD_KEY);
        if (StrUtil.isEmpty(mRedId) || StrUtil.isEmpty(mRedKey)) {
            return;
        }
        mCurrentPage = 1;
        isLoading = false;
        isFirst = true;
        mCurChooseAddres = "";
        initData(mCurrentPage);
    }

    private void initData(int pageNum) {
        isLoading = true;
        if (isFirst) {
            showProgressDialog();
        }
        new GetRedDesRequest(mRedId, mRedKey, pageNum,mCurChooseAddres).doRequest(this, new NetResultCallBack() {
            @Override
            public void onSuccess(JSONArray jsonArray) {
                isFirst = false;
                if (jsonArray.get(2).toString() == null) {
                    mRedBean.setIsOver("3");//不存在的红包
                    mTxtDes.setVisibility(View.GONE);
                    mGetRedAdapter.setTopData(mRedBean);
                    return;
                }
                RedDetailsBean redDetailsBean = JSON.parseObject(jsonArray.get(2).toString(), RedDetailsBean.class);
                mRedBean.setType(redDetailsBean.getType());
                mRedBean.setIsOver(redDetailsBean.getIsOver());
                mRedBean.setFrom(redDetailsBean.getFrom());
                mRedBean.setTotalCoin(redDetailsBean.getTotalCoin());
                mRedBean.setTitle(redDetailsBean.getTitle());
                mRedBean.setUsedNum(redDetailsBean.getUsedNum());
                mRedBean.setKeyIsVaild(redDetailsBean.getKeyIsVaild());
                mRedBean.setTotalPacketNum(redDetailsBean.getTotalPacketNum());
                mRedBean.setTokenValue(redDetailsBean.getTokenValue());
                if(!StrUtil.isEmpty(mCurChooseAddres)){
                    mRedBean.setDrawStatus(redDetailsBean.getDrawStatus());
                } else{
                    mRedBean.setDrawStatus("10");
                }
                if((mRedBean.getUsedNum() == mRedBean.getTotalPacketNum())||"2".equals(mRedBean.getIsOver())){
                    mTxtDes.setVisibility(View.GONE);
                }else  mTxtDes.setVisibility(View.VISIBLE);

                mTokenId = redDetailsBean.getTokenId();

                mGetRedAdapter.setTopData(mRedBean);
                if (pageNum == 1) {
                    mDataList.clear();
                    mContentView.setSmoothPosition(0);
                }
                if (CollectionUtil.isCollectionEmpty(redDetailsBean.getList())) {
                    mListAdapter.setIsNeedLoadMore(false);
                    mListAdapter.notifyDataSetChanged();
                } else {
                    mTotalPages = redDetailsBean.getPages();
                    mDataList.addAll(redDetailsBean.getList());
                    mListAdapter.setIsNeedLoadMore(mCurrentPage < mTotalPages ? true : false);// 设置是否显示加载更多Item
                    mCurrentPage++;
                    mListAdapter.notifyDataSetChanged();
                }

                mRefreshLayout.refreshComplete();
                isLoading = false;
                dismissProgressDialog();
            }

            @Override
            public void onError(String error) {
                mDataList.clear();
                mContentView.setSmoothPosition(0);
                mRedBean.setType("1");
                mRedBean.setTotalCoin("0");
                mRedBean.setTitle("");
                mRedBean.setUsedNum(0);
                mRedBean.setTotalPacketNum(0);
                mRedBean.setTokenValue("0");
                mRedBean.setIsOver("3");//不存在的红包
                mTxtDes.setVisibility(View.GONE);
                mGetRedAdapter.setTopData(mRedBean);
                mListAdapter.notifyDataSetChanged();
                dismissProgressDialog();
                mRefreshLayout.refreshComplete();
                isLoading = false;
                isFirst = false;
                DappApplication.getInstance().showToast(error);
            }
        });
    }

    private MyPtrFrameLayout.OnRefreshListener mRefreshListener = new MyPtrFrameLayout.OnRefreshListener() {
        @Override
        public void refresh(PtrFrameLayout frame) {
            if (!isLoading) {
                mCurrentPage = 1;
                initData(mCurrentPage);
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
                initData(mCurrentPage);
            }
        }
    };

    private void showChooseWallet() {
        mWalletDatas.clear();
        mWalletDatas = mCreateDbWallet.queryAllWallet(GetRedActivity.this);
        new GetRedChooseWalletDialog(GetRedActivity.this, findViewById(R.id.common_toolbar),
                new GetRedChooseWalletDialog.OnSureClickedListener() {
                    @Override
                    public void sure(int curretPosition) {
                        mFirstJuesePostition = curretPosition;
                        mCurChooseAddres = mWalletDatas.get(curretPosition).getAddress();
                        new RedDrawRequest(mRedId, mRedKey, mCurChooseAddres, mTokenId).doRequest(GetRedActivity.this,
                                new CommonResultListener(GetRedActivity.this) {
                                    @Override
                                    public void onSuccess(JSONArray jsonArray) {
                                        GetRedStatusBean statusBean = JSON.parseObject(jsonArray.get(2).toString(), GetRedStatusBean.class);
                                        switch (statusBean.getStatus()) {
                                            case "1":
                                                DappApplication.getInstance().showToast(getResources().getString(R.string.activity_red_get_txt_07_01));
                                                //刷新页面
                                                if (!isLoading) {
                                                    mCurrentPage = 1;
                                                    isFirst = true;
                                                    initData(mCurrentPage);
                                                }
                                                break;
                                            case "0":
                                                mCurChooseAddres = "";
                                                DappApplication.getInstance().showToast(getResources().getString(R.string.activity_red_get_txt_07_02));
                                                break;
                                            case "2":
                                                mCurChooseAddres = "";
                                                DappApplication.getInstance().showToast(getResources().getString(R.string.activity_red_get_txt_07_03));
                                                break;
                                            case "3":
                                                mCurChooseAddres = "";
                                                DappApplication.getInstance().showToast(getResources().getString(R.string.activity_red_get_txt_11_05));
                                                break;
                                            case "4":
                                                mCurChooseAddres = "";
                                                DappApplication.getInstance().showToast(getResources().getString(R.string.activity_red_get_txt_07_05));
                                                break;
                                            case "5":
                                                BigDecimal money = new BigDecimal("" + statusBean.getCoinValue());
                                                showSValue(SlinUtil.FormatNum(GetRedActivity.this, money));
                                                break;
                                        }

                                    }
                                });
                    }
                }, mWalletDatas, mFirstJuesePostition).show();
    }

    private void showSValue(String money) {
        CommonDilogTool dialogTool = new CommonDilogTool(this);
        String msg = getResources().getString(R.string.activity_red_get_txt_07_06);
        dialogTool.show(null, msg, null,
                null, null,
                getResources().getString(R.string.dailog_psd_btn_ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!isLoading) {
                            mCurrentPage = 1;
                            initData(mCurrentPage);
                        }
                        dialog.dismiss();
                    }
                });
    }

    /**
     * 创建钱包对话框
     */
    private void showCreateWalletDilaog() {
        CommonDilogTool dialogTool = new CommonDilogTool(this);
        dialogTool.show(null, getResources().getString(R.string.dialog_common_title_message_nowallet), null,
                getResources().getString(R.string.dailog_psd_btn_cancle), null,
                getResources().getString(R.string.dialog_common_title_message_go_create_wallet), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(GetRedActivity.this, CreateWalletActivity.class);
                        startActivity(intent);
                        dialog.dismiss();
                    }
                });
    }
}
