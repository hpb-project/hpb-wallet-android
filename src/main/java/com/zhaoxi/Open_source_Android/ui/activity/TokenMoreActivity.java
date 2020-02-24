package com.zhaoxi.Open_source_Android.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.View;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.gyf.immersionbar.ImmersionBar;
import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.base.BaseTitleBarActivity;
import com.zhaoxi.Open_source_Android.common.base.DappApplication;
import com.zhaoxi.Open_source_Android.common.view.RecyclerViewItemDecoration;
import com.zhaoxi.Open_source_Android.common.view.recycler.LoadMoreRecyclerAdapter;
import com.zhaoxi.Open_source_Android.common.view.recycler.LoadMoreRecyclerView;
import com.zhaoxi.Open_source_Android.common.view.refresh.MyPtrFrameLayout;
import com.zhaoxi.Open_source_Android.libs.tools.SystemLog;
import com.zhaoxi.Open_source_Android.libs.utils.ChangeLanguageUtil;
import com.zhaoxi.Open_source_Android.libs.utils.CollectionUtil;
import com.zhaoxi.Open_source_Android.net.BaseBet.NetResultCallBack;
import com.zhaoxi.Open_source_Android.net.Request.TokenMoreRequest;
import com.zhaoxi.Open_source_Android.net.bean.TokenIdMoreBean;
import com.zhaoxi.Open_source_Android.ui.adapter.TokenMoreAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrFrameLayout;

public class TokenMoreActivity extends BaseTitleBarActivity implements MyPtrFrameLayout.OnRefreshListener, LoadMoreRecyclerView.OnLoadMoreListener {

    public static final String CONTRACT_ADDRESS = "TokenMoreActivity.contractAddress";
    public static final String TRANS_HASH = "TokenMoreActivity.transHash";


    @BindView(R.id.recycler_with_refresh_ptr_frame_layout)
    MyPtrFrameLayout mPtrFrameLayout;
    @BindView(R.id.token_more_list_recycler_view)
    LoadMoreRecyclerView mTokenMoreRecyclerView;
    @BindView(R.id.iv_default_empty)
    ImageView mDefaultEmpty;

    private LoadMoreRecyclerAdapter loadMoreRecyclerAdapter;

    private int mTotalPages;
    private int mCurrentPage;
    private boolean isLoading;
    private boolean isFirstLoad = true;
    private int mLanguage;

    private List<TokenIdMoreBean.TokenIdInfo> tokenIdInfos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_token_more);
        ButterKnife.bind(this);
        initView();
        initData(1);
    }

    private void initView() {
        ImmersionBar.with(this)
                .statusBarDarkFont(true, 0.2f)
                .init();
        setTitleBgColor(R.color.white, true);
        setTitle(getResources().getString(R.string.tansfer_recode_details_txt_title), true);
        mLanguage = ChangeLanguageUtil.languageType(this);
        mPtrFrameLayout.setLastUpdateTimeRelateObject(this);
        mPtrFrameLayout.setUltraPullToRefresh(this, mTokenMoreRecyclerView);

        LinearLayoutManager lm = new LinearLayoutManager(this);
        mTokenMoreRecyclerView.setLayoutManager(lm);
        mTokenMoreRecyclerView.addItemDecoration(new RecyclerViewItemDecoration(this, LinearLayoutManager.VERTICAL, R.drawable.shape_1dp_divider_eaecee));

        TokenMoreAdapter tokenMoreAdapter = new TokenMoreAdapter(this, tokenIdInfos);
        loadMoreRecyclerAdapter = new LoadMoreRecyclerAdapter(this, tokenMoreAdapter, false);
        mTokenMoreRecyclerView.setAdapter(loadMoreRecyclerAdapter);
        // 设置加载更多监听
        mTokenMoreRecyclerView.setListener(this);
        ((SimpleItemAnimator) mTokenMoreRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false); //取消RecyclerView的动画效果
    }

    private void initData(int pageNum) {
        String contractAddress = getIntent().getStringExtra(CONTRACT_ADDRESS);
        String transHash = getIntent().getStringExtra(TRANS_HASH);
        SystemLog.D("initData", "contractAddress = " + contractAddress);
        if (!isLoading) {
            isLoading = true;
            if (isFirstLoad) {
                isFirstLoad = false;
                showProgressDialog();
            }
        }

        new TokenMoreRequest(pageNum
                , transHash
                , contractAddress).doRequest(this, new NetResultCallBack() {
            @Override
            public void onSuccess(JSONArray jsonArray) {
                if (jsonArray.get(2) != null) {
                    TokenIdMoreBean tokenAllIdBean = JSON.parseObject(jsonArray.get(2).toString(), TokenIdMoreBean.class);
                    if (CollectionUtil.isCollectionEmpty(tokenAllIdBean.getList())) {
                        loadMoreRecyclerAdapter.setIsNeedLoadMore(false);
                        loadMoreRecyclerAdapter.notifyDataSetChanged();
                    } else {
                        if (pageNum == 1) {
                            tokenIdInfos.clear();
                            mTokenMoreRecyclerView.setSmoothPosition(0);
                        }
                        mTotalPages = tokenAllIdBean.getPages();
                        ++mCurrentPage;
                        loadMoreRecyclerAdapter.setIsNeedLoadMore(mCurrentPage < mTotalPages);// 设置是否显示加载更多Item
                        tokenIdInfos.addAll(tokenAllIdBean.getList());
                        loadMoreRecyclerAdapter.notifyDataSetChanged();
                    }
                }

                if (CollectionUtil.isCollectionEmpty(tokenIdInfos)) {
                    mDefaultEmpty.setVisibility(View.VISIBLE);
                    if (mLanguage == 1)
                        mDefaultEmpty.setImageResource(R.drawable.icon_list_empty_zh);
                    else
                        mDefaultEmpty.setBackgroundResource(R.drawable.icon_list_empty_en);
                } else {
                    mDefaultEmpty.setVisibility(View.GONE);
                }

                mPtrFrameLayout.refreshComplete();
                isLoading = false;
                dismissProgressDialog();
            }

            @Override
            public void onError(String error) {
                mPtrFrameLayout.refreshComplete();
                isLoading = false;
                DappApplication.getInstance().showToast(error);
                dismissProgressDialog();
            }
        });
    }

    @Override
    public void refresh(PtrFrameLayout frame) {
        if (!isLoading) {
            mCurrentPage = 1;
            initData(1);
        }

    }

    @Override
    public void loadMore() {
        // 设置滑到底部加载更多
        if (!isLoading && mCurrentPage <= mTotalPages) {
            initData(mCurrentPage);
        }
    }

}
