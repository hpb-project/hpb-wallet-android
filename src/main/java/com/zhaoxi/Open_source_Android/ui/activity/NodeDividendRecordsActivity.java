package com.zhaoxi.Open_source_Android.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.gyf.immersionbar.ImmersionBar;
import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.base.BaseTitleBarActivity;
import com.zhaoxi.Open_source_Android.common.view.recycler.LoadMoreRecyclerAdapter;
import com.zhaoxi.Open_source_Android.common.view.recycler.LoadMoreRecyclerView;
import com.zhaoxi.Open_source_Android.common.view.refresh.MyPtrFrameLayout;
import com.zhaoxi.Open_source_Android.libs.utils.ChangeLanguageUtil;
import com.zhaoxi.Open_source_Android.libs.utils.CollectionUtil;
import com.zhaoxi.Open_source_Android.libs.utils.SharedPreferencesUtil;
import com.zhaoxi.Open_source_Android.net.BaseBet.CommonResultListener;
import com.zhaoxi.Open_source_Android.net.Request.GetNodeRecordsRequest;
import com.zhaoxi.Open_source_Android.net.bean.DividendRecordsBean;
import com.zhaoxi.Open_source_Android.ui.adapter.NodeDividendRecordsAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrFrameLayout;

public class NodeDividendRecordsActivity extends BaseTitleBarActivity {

    @BindView(R.id.activity_dividend_records_list_cntent)
    LoadMoreRecyclerView mContentView;
    @BindView(R.id.activity_dividend_records_ptrfrsh_layout)
    MyPtrFrameLayout mRefreshLayout;

    private LoadMoreRecyclerAdapter mListAdapter;
    private NodeDividendRecordsAdapter mAdapter;
    private List<DividendRecordsBean.RecordsBean> mDataList = new ArrayList<>();
    private boolean isLoading = false, isFirst = true;
    private int mCurrentPage = 1;
    private int mTotalPages;
    private int mLanguage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_node_dividend_records);
        ButterKnife.bind(this);
        initViews();
    }

    private void initViews() {
        ImmersionBar.with(this)
                .statusBarDarkFont(true, 0.2f)
                .init();
        setTitleBgColor(R.color.white, true);
        setTitle(getResources().getString(R.string.activity_cion_fh_txt_12), true);
        mLanguage = ChangeLanguageUtil.languageType(this);

        mRefreshLayout.setLastUpdateTimeRelateObject(this);
        mRefreshLayout.setUltraPullToRefresh(mRefreshListener, mContentView);
        mRefreshLayout.changeWhiteBackgroud();

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mContentView.setLayoutManager(mLayoutManager);
        mAdapter = new NodeDividendRecordsAdapter(this, mDataList);
        mListAdapter = new LoadMoreRecyclerAdapter(this, mAdapter, false);

        mContentView.setAdapter(mListAdapter);
        // 设置滑到底部加载更多
        mContentView.setListener(mLoadMoreListener);
        initData(1);
    }

    private void initData(int pageNum) {
        isLoading = true;
        if (isFirst) {
            showProgressDialog();
        }
        String defultAddress = SharedPreferencesUtil.getSharePreString(this, SharedPreferencesUtil.CHOOSE_WALLET_ADDRESS);
        new GetNodeRecordsRequest(defultAddress, mCurrentPage).doRequest(this, new CommonResultListener(this) {
            @Override
            public void onSuccess(JSONArray jsonArray) {
                if (jsonArray.get(2) != null) {
                    DividendRecordsBean recordsBean = JSON.parseObject(jsonArray.get(2).toString(), DividendRecordsBean.class);
                    isFirst = false;
                    if (pageNum == 1) {
                        mDataList.clear();
                        mContentView.setSmoothPosition(0);
                    }
                    if (CollectionUtil.isCollectionEmpty(recordsBean.getList())) {
                        mListAdapter.setIsNeedLoadMore(false);
                        mListAdapter.notifyDataSetChanged();
                    } else {
                        mTotalPages = recordsBean.getPages();
                        mDataList.addAll(recordsBean.getList());
                        mListAdapter.setIsNeedLoadMore(mCurrentPage < mTotalPages ? true : false);// 设置是否显示加载更多Item
                        mCurrentPage++;
                        mListAdapter.notifyDataSetChanged();
                    }
                    if (CollectionUtil.isCollectionEmpty(recordsBean.getList())) {
                        if (mLanguage == 1)
                            mContentView.setBackgroundResource(R.drawable.icon_list_empty_zh);
                        else
                            mContentView.setBackgroundResource(R.drawable.icon_list_empty_en);
                    } else {
                        mContentView.setBackgroundColor(getResources().getColor(R.color.white));
                    }
                }

                mRefreshLayout.refreshComplete();
                isLoading = false;
                dismissProgressDialog();
            }

            @Override
            public void onError(String error) {
                super.onError(error);
                mRefreshLayout.refreshComplete();
                isLoading = false;
                dismissProgressDialog();
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
}
