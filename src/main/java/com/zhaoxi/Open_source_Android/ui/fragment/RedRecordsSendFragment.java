package com.zhaoxi.Open_source_Android.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.base.BaseActivity;
import com.zhaoxi.Open_source_Android.common.base.BaseFragment;
import com.zhaoxi.Open_source_Android.common.base.DappApplication;
import com.zhaoxi.Open_source_Android.common.view.recycler.LoadMoreRecyclerAdapter;
import com.zhaoxi.Open_source_Android.common.view.recycler.LoadMoreRecyclerView;
import com.zhaoxi.Open_source_Android.common.view.refresh.MyPtrFrameLayout;
import com.zhaoxi.Open_source_Android.libs.utils.CollectionUtil;
import com.zhaoxi.Open_source_Android.libs.utils.SharedPreferencesUtil;
import com.zhaoxi.Open_source_Android.net.BaseBet.NetResultCallBack;
import com.zhaoxi.Open_source_Android.net.Request.GetRedRecordsRequest;
import com.zhaoxi.Open_source_Android.net.bean.RedRecordBean;
import com.zhaoxi.Open_source_Android.ui.adapter.RedRecordsSendAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import in.srain.cube.views.ptr.PtrFrameLayout;

public class RedRecordsSendFragment extends BaseFragment {
    Unbinder unbinder;
    @BindView(R.id.redrecords_send_refresh_layout)
    MyPtrFrameLayout mRefreshPtrFrameLayout;
    @BindView(R.id.redrecords_send_record_list)
    LoadMoreRecyclerView mContentView;

    private BaseActivity mActivity;
    public String mDefultAddress;
    private LoadMoreRecyclerAdapter mListAdapter;
    private RedRecordsSendAdapter mSendRecordsAdapter;

    private boolean isLoading = false,isFisrt = true;
    private int mCurrentPage = 1;
    private int mTotalPages;

    private String mCurSendRedNum = "0";
    private List<RedRecordBean.RedBean> mDataList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_redrecords_send, container, false);
        unbinder = ButterKnife.bind(this, contentView);
        mActivity = (BaseActivity) getActivity();
        initViews();
        return contentView;
    }

    private void initViews() {
        mDefultAddress = SharedPreferencesUtil.getSharePreString(mActivity, SharedPreferencesUtil.CHOOSE_WALLET_ADDRESS);

        mRefreshPtrFrameLayout.setLastUpdateTimeRelateObject(this);
        mRefreshPtrFrameLayout.setUltraPullToRefresh(mRefreshListener, mContentView);
        mRefreshPtrFrameLayout.changeFlshBackgroud(R.color.color_F7F7F7);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false);
        mContentView.setLayoutManager(mLayoutManager);
        mSendRecordsAdapter = new RedRecordsSendAdapter(mActivity, mDataList, mCurSendRedNum);

        mListAdapter = new LoadMoreRecyclerAdapter(mActivity, mSendRecordsAdapter, false);

        mContentView.setAdapter(mListAdapter);
        // 设置滑到底部加载更多
        mContentView.setListener(mLoadMoreListener);
        initData(1);
    }

    private void initData(int pageNum) {
        isLoading = true;
        if (isFisrt) {
            mActivity.showProgressDialog();
        }
        new GetRedRecordsRequest("1", mDefultAddress, pageNum).doRequest(mActivity,
                new NetResultCallBack() {
                    @Override
                    public void onSuccess(JSONArray jsonArray) {
                        isFisrt = false;
                        RedRecordBean redRecordBean = JSON.parseObject(jsonArray.get(2).toString(), RedRecordBean.class);
                        mSendRecordsAdapter.setTopData("" + redRecordBean.getTotal());
                        if (pageNum == 1) {
                            mDataList.clear();
                            mContentView.setSmoothPosition(0);
                        }
                        if (CollectionUtil.isCollectionEmpty(redRecordBean.getList())) {
                            mListAdapter.setIsNeedLoadMore(false);
                            mListAdapter.notifyDataSetChanged();
                        } else {
                            mTotalPages = redRecordBean.getPages();
                            mDataList.addAll(redRecordBean.getList());
                            mListAdapter.setIsNeedLoadMore(mCurrentPage < mTotalPages ? true : false);// 设置是否显示加载更多Item
                            mCurrentPage++;
                            mListAdapter.notifyDataSetChanged();
                        }

                        mRefreshPtrFrameLayout.refreshComplete();
                        isLoading = false;
                        mActivity.dismissProgressDialog();
                    }

                    @Override
                    public void onError(String error) {
                        mActivity.dismissProgressDialog();
                        mRefreshPtrFrameLayout.refreshComplete();
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
