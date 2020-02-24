package com.zhaoxi.Open_source_Android.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.zhaoxi.Open_source_Android.DAppConstants;
import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.base.DappApplication;
import com.zhaoxi.Open_source_Android.common.view.recycler.LoadMoreRecyclerAdapter;
import com.zhaoxi.Open_source_Android.common.view.recycler.LoadMoreRecyclerView;
import com.zhaoxi.Open_source_Android.common.view.refresh.MyPtrFrameLayout;
import com.zhaoxi.Open_source_Android.libs.utils.ChangeLanguageUtil;
import com.zhaoxi.Open_source_Android.libs.utils.CollectionUtil;
import com.zhaoxi.Open_source_Android.net.BaseBet.NetResultCallBack;
import com.zhaoxi.Open_source_Android.net.Request.TransactionListRequest;
import com.zhaoxi.Open_source_Android.net.bean.TranferRecordBean;
import com.zhaoxi.Open_source_Android.ui.activity.TokenDetail721Activity;
import com.zhaoxi.Open_source_Android.ui.activity.TransferRecodeDetailsActivity;
import com.zhaoxi.Open_source_Android.ui.adapter.TransRecord721Adapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrFrameLayout;


/**
 * create by fangz
 * create date:2019/8/25
 * create time:8:20
 */
public class FragmentTransRecord721 extends Fragment implements MyPtrFrameLayout.OnRefreshListener, LoadMoreRecyclerView.OnLoadMoreListener {

    @BindView(R.id.recycler_with_refresh_ptr_frame_layout)
    MyPtrFrameLayout mPtrFrameLayout;
    @BindView(R.id.token_trans_record_list_recycler_view)
    LoadMoreRecyclerView mLoadMoreRecyclerView;
    @BindView(R.id.iv_default_empty)
    ImageView mDefaultEmpty;

    private TokenDetail721Activity mActivity = null;
    private List<TranferRecordBean.TransferInfo> mDataList = new ArrayList<>();
    private LoadMoreRecyclerAdapter loadMoreRecyclerAdapter;
    private TransRecord721Adapter transRecord721Adapter;

    private int mTotalPages;
    private int mCurrentPage = 1;
    private boolean isLoading;
    //判断是否是首次加载，控制转圈进度条只在第一次加载时显示，在下拉刷新时不显示
    private boolean isFirstLoad = true;
    // 当前语言类型
    private int mLanguage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trans_record_721, container, false);
        ButterKnife.bind(this, view);
        mActivity = (TokenDetail721Activity) getActivity();
        initView();
        initListener();
        //刷新数据
        mPtrFrameLayout.autoRefresh();
        return view;
    }

    private void initView() {
        mLanguage = ChangeLanguageUtil.languageType(mActivity);
        mPtrFrameLayout.setLastUpdateTimeRelateObject(this);
        mPtrFrameLayout.setUltraPullToRefresh(this, mLoadMoreRecyclerView);
        LinearLayoutManager lm = new LinearLayoutManager(mActivity);
        mLoadMoreRecyclerView.setLayoutManager(lm);
        transRecord721Adapter = new TransRecord721Adapter(mActivity, mDataList);
        loadMoreRecyclerAdapter = new LoadMoreRecyclerAdapter(mActivity, transRecord721Adapter, false);
        mLoadMoreRecyclerView.setAdapter(loadMoreRecyclerAdapter);

        // 设置加载更多监听
        mLoadMoreRecyclerView.setListener(this);
        // 取消RecyclerView的动画效果
        ((SimpleItemAnimator) mLoadMoreRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
    }

    private void initData(int pageNum) {
        if (!isLoading) {
            isLoading = true;
            if (isFirstLoad) {
                isFirstLoad = false;
               mActivity.showProgressDialog();
            }
        }
        new TransactionListRequest(TokenDetail721Activity.mWalletAddress
                , mActivity.getResources().getString(R.string.hrc_721)
                , mActivity.mContractAddress
                , ""
                , pageNum
                , "").doRequest(mActivity, new NetResultCallBack() {
            @Override
            public void onSuccess(JSONArray jsonArray) {
                if (jsonArray.get(2) != null) {
                    TranferRecordBean transRecordBean = JSON.parseObject(jsonArray.get(2).toString(), TranferRecordBean.class);
                    if (CollectionUtil.isCollectionEmpty(transRecordBean.getList())) {
                        loadMoreRecyclerAdapter.setIsNeedLoadMore(false);
                        loadMoreRecyclerAdapter.notifyDataSetChanged();
                    } else {
                        if (pageNum == 1) {
                            mDataList.clear();
                            mLoadMoreRecyclerView.setSmoothPosition(0);
                        }
                        mTotalPages = transRecordBean.getPages();
                        ++mCurrentPage;
                        mDataList.addAll(transRecordBean.getList());
                        loadMoreRecyclerAdapter.setIsNeedLoadMore(mCurrentPage < mTotalPages);// 设置是否显示加载更多Item
                        loadMoreRecyclerAdapter.notifyDataSetChanged();

                    }
                }

                if (CollectionUtil.isCollectionEmpty(mDataList)) {
                    mDefaultEmpty.setVisibility(View.VISIBLE);
                    if (mLanguage == 1)
                        mDefaultEmpty.setImageResource(R.drawable.icon_list_empty_zh);
                    else
                        mDefaultEmpty.setImageResource(R.drawable.icon_list_empty_en);
                } else {
                    mDefaultEmpty.setVisibility(View.GONE);
                }

                isLoading = false;
                mPtrFrameLayout.refreshComplete();
                mActivity.dismissProgressDialog();
            }

            @Override
            public void onError(String error) {
                isLoading = false;
                mPtrFrameLayout.refreshComplete();
                DappApplication.getInstance().showToast(error);
                mActivity.dismissProgressDialog();
            }
        });
    }

    private void initListener() {
        transRecord721Adapter.setOnItemClickListener(new TransRecord721Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(TranferRecordBean.TransferInfo transInfo) {
                Intent data = new Intent(mActivity, TransferRecodeDetailsActivity.class);
                data.putExtra(TransferRecodeDetailsActivity.TRANSFER_RECODE_DETAILS, transInfo);
                data.putExtra(TransferRecodeDetailsActivity.TRANSFER_RECODE_TYPE, DAppConstants.TYPE_HRC_721);
                mActivity.startActivity(data);
            }
        });
    }


    @Override
    public void loadMore() {
        // 设置滑到底部加载更多
        if (!isLoading && mCurrentPage <= mTotalPages) {
            initData(mCurrentPage);
        }
    }

    @Override
    public void refresh(PtrFrameLayout frame) {
        if (!isLoading) {
            mCurrentPage = 1;
            initData(mCurrentPage);
        }
    }
}
