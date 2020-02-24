package com.zhaoxi.Open_source_Android.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.zhaoxi.Open_source_Android.Config;
import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.base.BaseTitleBarActivity;
import com.zhaoxi.Open_source_Android.common.base.DappApplication;
import com.zhaoxi.Open_source_Android.common.view.recycler.LoadMoreRecyclerAdapter;
import com.zhaoxi.Open_source_Android.common.view.recycler.LoadMoreRecyclerView;
import com.zhaoxi.Open_source_Android.common.view.refresh.MyPtrFrameLayout;
import com.zhaoxi.Open_source_Android.libs.utils.ChangeLanguageUtil;
import com.zhaoxi.Open_source_Android.libs.utils.CollectionUtil;
import com.zhaoxi.Open_source_Android.libs.utils.SharedPreferencesUtil;
import com.zhaoxi.Open_source_Android.libs.utils.StrUtil;
import com.zhaoxi.Open_source_Android.net.BaseBet.NetResultCallBack;
import com.zhaoxi.Open_source_Android.net.Request.GetRedDetailsRequest;
import com.zhaoxi.Open_source_Android.net.bean.RedDetailsBean;
import com.zhaoxi.Open_source_Android.ui.adapter.RedRecordsSendDetailsAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrFrameLayout;

public class RedRecordsSendDetailsActivity extends BaseTitleBarActivity {
    public static final String RED_RECORD_ID = "RED_RECORD_ID";
    public static final String RED_RECORD_TITLE = "RED_RECORD_TITLE";
    public static final String RED_RECORD_STATUS = "RED_RECORD_STATUS";
    @BindView(R.id.redrecords_send_detials_record_list)
    LoadMoreRecyclerView mContentView;
    @BindView(R.id.redrecords_send_detials_refresh_layout)
    MyPtrFrameLayout mRefreshLayout;
    @BindView(R.id.redrecords_send_detials_txt_toast)
    TextView mTxtToast;

    private LoadMoreRecyclerAdapter mListAdapter;
    private boolean isLoading = false, isFisrt = true;
    private int mCurrentPage = 1;
    private int mTotalPages;
    public String mDefultAddress;
    private List<RedDetailsBean.RedDetails> mDataList = new ArrayList<>();
    private RedDetailsBean mRedBean = new RedDetailsBean();
    private RedRecordsSendDetailsAdapter mRecordsDetailsAdapter;

    private String mRedId, mTitle, mLoadUrl = "http://www.baidu.com", mStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_red_records_send_details);
        ButterKnife.bind(this);
        mRedId = getIntent().getStringExtra(RED_RECORD_ID);
        mTitle = getIntent().getStringExtra(RED_RECORD_TITLE);
        mStatus = getIntent().getStringExtra(RED_RECORD_STATUS);
        initViews();
    }

    private void initViews() {
        setTitle(getResources().getString(R.string.activity_red_record_txt_05), true);
        mDefultAddress = SharedPreferencesUtil.getSharePreString(this, SharedPreferencesUtil.CHOOSE_WALLET_ADDRESS);
        if (getResources().getString(R.string.activity_red_get_txt_09_03).equals(mStatus)) {
            showRightTxtWithTextListener(getResources().getString(R.string.activity_red_record_txt_07), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //todo 分享
                }
            });
        }
        mRefreshLayout.setLastUpdateTimeRelateObject(this);
        mRefreshLayout.setUltraPullToRefresh(mRefreshListener, mContentView);
        mRefreshLayout.changeFlshBackgroud(R.color.base_new_theme_color);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mContentView.setLayoutManager(mLayoutManager);
        mRecordsDetailsAdapter = new RedRecordsSendDetailsAdapter(this, mDataList, mRedBean);

        mListAdapter = new LoadMoreRecyclerAdapter(this, mRecordsDetailsAdapter, false);

        mContentView.setAdapter(mListAdapter);
        // 设置滑到底部加载更多
        mContentView.setListener(mLoadMoreListener);
        initData(1);
    }

    private void initData(int pageNum) {
        isLoading = true;
        if (isFisrt) {
            showProgressDialog();
        }
        new GetRedDetailsRequest("1", mRedId, mDefultAddress, pageNum).doRequest(this, new NetResultCallBack() {
            @Override
            public void onSuccess(JSONArray jsonArray) {
                isFisrt = false;
                RedDetailsBean redDetailsBean = JSON.parseObject(jsonArray.get(2).toString(), RedDetailsBean.class);
                mRedBean.setType(redDetailsBean.getType());
                mRedBean.setIsOver(redDetailsBean.getIsOver());
                if ("2".equals(redDetailsBean.getIsOver()) || redDetailsBean.getUsedNum() == redDetailsBean.getTotalPacketNum()) {
                    mTxtToast.setVisibility(View.GONE);
                } else {
                    mTxtToast.setVisibility(View.VISIBLE);
                }

                mRedBean.setFrom(redDetailsBean.getFrom());
                mRedBean.setTotalCoin(redDetailsBean.getTotalCoin());
                mRedBean.setTitle(redDetailsBean.getTitle());
                mRedBean.setUsedNum(redDetailsBean.getUsedNum());
                mRedBean.setTotalPacketNum(redDetailsBean.getTotalPacketNum());
                mRedBean.setRedStatus(mStatus);

                mRecordsDetailsAdapter.setTopData(mRedBean);
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
                dismissProgressDialog();
                mRefreshLayout.refreshComplete();
                isLoading = false;
                isFisrt = false;
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
}
