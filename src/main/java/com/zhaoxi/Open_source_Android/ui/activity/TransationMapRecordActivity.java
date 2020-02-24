package com.zhaoxi.Open_source_Android.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SimpleItemAnimator;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.base.BaseTitleBarActivity;
import com.zhaoxi.Open_source_Android.common.base.DappApplication;
import com.zhaoxi.Open_source_Android.common.view.recycler.LoadMoreRecyclerAdapter;
import com.zhaoxi.Open_source_Android.common.view.recycler.LoadMoreRecyclerView;
import com.zhaoxi.Open_source_Android.common.view.refresh.MyPtrFrameLayout;
import com.zhaoxi.Open_source_Android.libs.utils.CollectionUtil;
import com.zhaoxi.Open_source_Android.net.BaseBet.NetResultCallBack;
import com.zhaoxi.Open_source_Android.net.Request.TransactionHistoryRequest;
import com.zhaoxi.Open_source_Android.net.UrlContext;
import com.zhaoxi.Open_source_Android.net.bean.TranferRecordBean;
import com.zhaoxi.Open_source_Android.ui.adapter.TransationMapRecordAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import in.srain.cube.views.ptr.PtrFrameLayout;

public class TransationMapRecordActivity extends BaseTitleBarActivity {
    public static final String RESOUCE_ADDRESS = "TransationRecordActivity.RESOUCE_ADDRESS";

    Unbinder unbinder;
    @BindView(R.id.transation_record_list)
    LoadMoreRecyclerView mContentView;
    @BindView(R.id.transation_record_refresh_ptr_frame_layout)
    MyPtrFrameLayout mRefreshPtrFrameLayout;

    private LoadMoreRecyclerAdapter mListAdapter;
    private List<TranferRecordBean.TransferInfo> mDataList = new ArrayList<>();
    private TransationMapRecordAdapter mTranAdapter;

    private boolean isLoading = false;
    private int mCurrentPage = 1;
    private int mTotalPages;
    private String mResouceAddress;
    public static String mChooseAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transation_record);
        ButterKnife.bind(this);

        mResouceAddress = getIntent().getStringExtra(RESOUCE_ADDRESS);
        mChooseAddress = mResouceAddress;
        initViews();
        initData(1);
    }

    private void initViews() {
        setTitle(R.string.main_map_txt_record_title,true);
        mRefreshPtrFrameLayout.setLastUpdateTimeRelateObject(this);
        mRefreshPtrFrameLayout.setUltraPullToRefresh(mRefreshListener, mContentView);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mContentView.setLayoutManager(mLayoutManager);
        mTranAdapter = new TransationMapRecordAdapter(this, mDataList);
        mListAdapter = new LoadMoreRecyclerAdapter(this, mTranAdapter, false);

        mContentView.setAdapter(mListAdapter);
        // 设置滑到底部加载更多
        mContentView.setListener(mLoadMoreListener);
        ((SimpleItemAnimator) mContentView.getItemAnimator()).setSupportsChangeAnimations(false); //取消RecyclerView的动画效果
    }

    private MyPtrFrameLayout.OnRefreshListener mRefreshListener = new MyPtrFrameLayout.OnRefreshListener() {
        @Override
        public void refresh(PtrFrameLayout frame) {
            if (!isLoading) {
                mCurrentPage = 1;
                initData(1);
            }
        }
    };


    private LoadMoreRecyclerView.OnLoadMoreListener mLoadMoreListener = new LoadMoreRecyclerView.OnLoadMoreListener() {
        @Override
        public void loadMore() {
            // 设置滑到底部加载更多
            if (!isLoading && mCurrentPage <= mTotalPages) {
                initData(mCurrentPage);
            }
        }
    };

    private void initData(int pageNum) {
        isLoading = true;
        if (pageNum == 1) {
            showProgressDialog();
        }
        new TransactionHistoryRequest(mResouceAddress, pageNum,
                UrlContext.Url_transferList.getContext(), 1)
                .doRequest(this,new NetResultCallBack() {
                    @Override
                    public void onSuccess(JSONArray jsonArray) {
                        TranferRecordBean tranferRecordBean = JSON.parseObject(jsonArray.get(2).toString(), TranferRecordBean.class);
                        if (pageNum == 1) {
                            mDataList.clear();
                            mContentView.setSmoothPosition(0);
                        }
                        if (CollectionUtil.isCollectionEmpty(tranferRecordBean.getList())) {
                            mListAdapter.setIsNeedLoadMore(false);
                            mListAdapter.notifyDataSetChanged();
                        } else {
                            mTotalPages = tranferRecordBean.getPages();
                            mDataList.addAll(tranferRecordBean.getList());
                            mListAdapter.setIsNeedLoadMore(mCurrentPage < mTotalPages ? true : false);// 设置是否显示加载更多Item
                            mCurrentPage++;
                            mListAdapter.notifyDataSetChanged();
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
}
