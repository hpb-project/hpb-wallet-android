package com.zhaoxi.Open_source_Android.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.gyf.immersionbar.ImmersionBar;
import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.base.BaseTitleBarActivity;
import com.zhaoxi.Open_source_Android.common.base.DappApplication;
import com.zhaoxi.Open_source_Android.common.view.recycler.LoadMoreRecyclerAdapter;
import com.zhaoxi.Open_source_Android.common.view.recycler.LoadMoreRecyclerView;
import com.zhaoxi.Open_source_Android.common.view.refresh.MyPtrFrameLayout;
import com.zhaoxi.Open_source_Android.libs.tools.SystemLog;
import com.zhaoxi.Open_source_Android.libs.utils.ChangeLanguageUtil;
import com.zhaoxi.Open_source_Android.libs.utils.CollectionUtil;
import com.zhaoxi.Open_source_Android.libs.utils.SharedPreferencesUtil;
import com.zhaoxi.Open_source_Android.net.BaseBet.NetResultCallBack;
import com.zhaoxi.Open_source_Android.net.Request.StockDetailRequest;
import com.zhaoxi.Open_source_Android.net.bean.StockDetailBean;
import com.zhaoxi.Open_source_Android.net.bean.TokenIdBean;
import com.zhaoxi.Open_source_Android.ui.adapter.TokenIdAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.srain.cube.views.ptr.PtrFrameLayout;

public class SelectTokenIdActivity extends BaseTitleBarActivity implements MyPtrFrameLayout.OnRefreshListener, LoadMoreRecyclerView.OnLoadMoreListener {

    public static final String RESULT_TOKEN_IDS = "SelectTokenIdActivity.tokenIds";
    public static final String CONTRACT_ADDRESS = "SelectTokenIdActivity.contractAddress";

    @BindView(R.id.recycler_with_refresh_ptr_frame_layout)
    MyPtrFrameLayout mPtrFrameLayout;
    @BindView(R.id.token_id_list_recycler_view)
    LoadMoreRecyclerView mTokenIdRecyclerView;
    @BindView(R.id.tv_select_token_id_ok)
    TextView mTokenIdOk;
    @BindView(R.id.iv_default_empty)
    ImageView mDefaultEmpty;

    private TokenIdAdapter tokenIdAdapter;
    private LoadMoreRecyclerAdapter loadMoreRecyclerAdapter;

    private int mLanguage;
    public static String address;

    private List<TokenIdBean> tokenIdBeans = new ArrayList<>();
    private List<String> selectedTokenIds = new ArrayList<>();

    private int mTotalPages;
    private int mCurrentPage;
    private boolean isLoading;
    private boolean isFirstLoad = true;//首次加载
    private String mContractAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_token_id);
        ButterKnife.bind(this);
        initView();
        initData(1);
        initListener();
    }

    private void initView() {
        ImmersionBar.with(this)
                .statusBarDarkFont(true, 0.2f)
                .init();
        setTitleBgColor(R.color.white, true);
        setTitle(getResources().getString(R.string.select_token_id), true);
        mLanguage = ChangeLanguageUtil.languageType(this);
        mPtrFrameLayout.setLastUpdateTimeRelateObject(this);
        mPtrFrameLayout.setUltraPullToRefresh(this, mTokenIdRecyclerView);
        mContractAddress = getIntent().getStringExtra(CONTRACT_ADDRESS);

        LinearLayoutManager lm = new LinearLayoutManager(this);
        mTokenIdRecyclerView.setLayoutManager(lm);

        tokenIdAdapter = new TokenIdAdapter(this, tokenIdBeans);
        loadMoreRecyclerAdapter = new LoadMoreRecyclerAdapter(this, tokenIdAdapter, false);
        mTokenIdRecyclerView.setAdapter(loadMoreRecyclerAdapter);
        // 设置加载更多监听
        mTokenIdRecyclerView.setListener(this);
        ((SimpleItemAnimator) mTokenIdRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false); //取消RecyclerView的动画效果
    }

    private void initListener() {
        tokenIdAdapter.setOnItemClickListener(new TokenIdAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(TokenIdBean inventoryBean) {
                if (inventoryBean.isCheck()) {
                    if (!selectedTokenIds.contains(inventoryBean.getTokenId()))
                        selectedTokenIds.add(inventoryBean.getTokenId());
                } else {
                    selectedTokenIds.remove(inventoryBean.getTokenId());
                }
            }
        });

    }

    private void initData(int pageNum) {
        if (!isLoading) {
            isLoading = true;
            if (isFirstLoad) {
                showProgressDialog();
            }
        }

        // 选择代币
        new StockDetailRequest(pageNum
                , mContractAddress
                , SharedPreferencesUtil.getSharePreString(this, SharedPreferencesUtil.CHOOSE_WALLET_ADDRESS)).doRequest(this, new NetResultCallBack() {
            @Override
            public void onSuccess(JSONArray jsonArray) {
                if (jsonArray.get(2) != null) {
                    StockDetailBean stockDetailBean = JSON.parseObject(jsonArray.get(2).toString(), StockDetailBean.class);
                    if (CollectionUtil.isCollectionEmpty(stockDetailBean.getList())) {
                        loadMoreRecyclerAdapter.setIsNeedLoadMore(false);
                        loadMoreRecyclerAdapter.notifyDataSetChanged();
                    } else {
                        if (pageNum == 1) {
                            tokenIdBeans.clear();
                            mTokenIdRecyclerView.setSmoothPosition(0);
                        }
                        mTotalPages = stockDetailBean.getPages();
                        ++mCurrentPage;
                        for (StockDetailBean.TokenInfo tokenIdInfo : stockDetailBean.getList()) {
                            TokenIdBean tokenIdBean = new TokenIdBean(tokenIdInfo.getTokenURI(), tokenIdInfo.getTokenId(), String.valueOf(tokenIdInfo.getCount()), false);
                            tokenIdBeans.add(tokenIdBean);
                        }
                        loadMoreRecyclerAdapter.setIsNeedLoadMore(mCurrentPage < mTotalPages);// 设置是否显示加载更多Item
                        loadMoreRecyclerAdapter.notifyDataSetChanged();
                    }
                }

                if (CollectionUtil.isCollectionEmpty(tokenIdBeans)) {
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
                if (isFirstLoad) {
                    isFirstLoad = false;
                    dismissProgressDialog();
                }
            }

            @Override
            public void onError(String error) {
                mPtrFrameLayout.refreshComplete();
                isLoading = false;
                DappApplication.getInstance().showToast(error);
                if (isFirstLoad) {
                    isFirstLoad = false;
                    dismissProgressDialog();
                }
            }
        });
    }

    @OnClick({R.id.tv_select_token_id_ok, R.id.tv_select_token_id_cancel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_select_token_id_ok:
                String tokenId = "";
                if (!CollectionUtil.isCollectionEmpty(selectedTokenIds)) {
                    tokenId = selectedTokenIds.get(0);
                }
                SystemLog.D("TAG", tokenId);
                Intent data = new Intent();
                data.putExtra(RESULT_TOKEN_IDS, tokenId);
                setResult(RESULT_OK, data);
                finish();
                break;
            case R.id.tv_select_token_id_cancel:
                // 取消
                finish();
                break;
        }
    }


    @Override
    public void refresh(PtrFrameLayout frame) {
        if (!isLoading) {
            mCurrentPage = 1;
            initData(mCurrentPage);
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
