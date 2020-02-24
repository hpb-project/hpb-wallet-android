package com.zhaoxi.Open_source_Android.ui.activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.gyf.immersionbar.ImmersionBar;
import com.zhaoxi.Open_source_Android.Config;
import com.zhaoxi.Open_source_Android.DAppConstants;
import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.base.BaseTitleBarActivity;
import com.zhaoxi.Open_source_Android.common.base.DappApplication;
import com.zhaoxi.Open_source_Android.common.view.recycler.LoadMoreRecyclerAdapter;
import com.zhaoxi.Open_source_Android.common.view.recycler.LoadMoreRecyclerView;
import com.zhaoxi.Open_source_Android.common.view.refresh.MyPtrFrameLayout;
import com.zhaoxi.Open_source_Android.libs.utils.ChangeLanguageUtil;
import com.zhaoxi.Open_source_Android.libs.utils.CollectionUtil;
import com.zhaoxi.Open_source_Android.net.BaseBet.CommonResultListener;
import com.zhaoxi.Open_source_Android.net.Request.ProposalListRequest;
import com.zhaoxi.Open_source_Android.net.bean.VoteZLBean;
import com.zhaoxi.Open_source_Android.ui.adapter.IssueListAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrFrameLayout;

public class VoteALActivity extends BaseTitleBarActivity {
    @BindView(R.id.activity_vote_zl_root)
    LinearLayout mLayoutRoot;
    @BindView(R.id.activity_vote_zl_list_cntent)
    LoadMoreRecyclerView mContentView;
    @BindView(R.id.activity_vote_zl_ptrfrsh_layout)
    MyPtrFrameLayout mRefreshLayout;

    private LoadMoreRecyclerAdapter mListAdapter;
    private IssueListAdapter mAdapter;
    private List<VoteZLBean.VoteZlInfo> mDataList = new ArrayList<>();
    private boolean isFisrt = true;
    private boolean isLoading = false;
    private int mCurrentPage = 1;
    private int mTotalPages;
    private int mLanguage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote_al);
        ButterKnife.bind(this);
        initViews();
    }

    private void initViews() {
        ImmersionBar.with(this)
                .statusBarDarkFont(true, 0.2f)
                .init();
        setTitleBgColor(R.color.white, true);
        setTitle(getResources().getString(R.string.activity_vote_gl_txt_02), true);
        showRightImgWithImgListener(R.mipmap.icon_title_right_more, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopWindow();
            }
        });
        mLanguage = ChangeLanguageUtil.languageType(this);

        mRefreshLayout.setLastUpdateTimeRelateObject(this);
        mRefreshLayout.setUltraPullToRefresh(mRefreshListener, mContentView);
        mRefreshLayout.changeWhiteBackgroud();

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mContentView.setLayoutManager(mLayoutManager);
        mAdapter = new IssueListAdapter(this, mDataList);
        mListAdapter = new LoadMoreRecyclerAdapter(this, mAdapter, false);

        mContentView.setAdapter(mListAdapter);
        // 设置滑到底部加载更多
        mContentView.setListener(mLoadMoreListener);

        initData(mCurrentPage);
    }

    private void initData(int pageNum) {
        isLoading = true;
        if (isFisrt) {
            showProgressDialog();
        }
        new ProposalListRequest(pageNum).doRequest(this, new CommonResultListener(this) {
            @Override
            public void onSuccess(JSONArray jsonArray) {
                VoteZLBean voteZLBean = JSON.parseObject(jsonArray.get(2).toString(), VoteZLBean.class);
                isFisrt = false;
                if (pageNum == 1) {
                    mDataList.clear();
                    mContentView.setSmoothPosition(0);
                }
                if (CollectionUtil.isCollectionEmpty(voteZLBean.getList())) {
                    mListAdapter.setIsNeedLoadMore(false);
                    mListAdapter.notifyDataSetChanged();
                } else {
                    mTotalPages = voteZLBean.getPages();
                    mDataList.addAll(voteZLBean.getList());
                    mListAdapter.setIsNeedLoadMore(mCurrentPage < mTotalPages ? true : false);// 设置是否显示加载更多Item
                    mCurrentPage++;
                    mListAdapter.notifyDataSetChanged();
                }
                if (CollectionUtil.isCollectionEmpty(voteZLBean.getList())) {
                    if (mLanguage == 1)
                        mContentView.setBackgroundResource(R.drawable.icon_list_empty_zh);
                    else
                        mContentView.setBackgroundResource(R.drawable.icon_list_empty_en);
                } else {
                    mContentView.setBackgroundColor(getResources().getColor(R.color.white));
                }
                mRefreshLayout.refreshComplete();
                isLoading = false;
                dismissProgressDialog();
            }

            @Override
            public void onError(String error) {
                isFisrt = false;
                dismissProgressDialog();
                mRefreshLayout.refreshComplete();
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

    private void showPopWindow() {
        View view = LayoutInflater.from(this).inflate(R.layout.vote_more_layout, null, false);
        final PopupWindow popupWindow = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setAnimationStyle(R.style.VoteDialogAnim);
        popupWindow.setTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable());
        popupWindow.showAtLocation(mLayoutRoot, Gravity.BOTTOM, 0, 0);

        TextView tvVoteRecord = view.findViewById(R.id.tv_vote_record);
        TextView tvVoteRule = view.findViewById(R.id.tv_vote_rule);
        TextView tvDismiss = view.findViewById(R.id.tv_dismiss);

        tvVoteRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                startActivity(new Intent(VoteALActivity.this, VoteZlRecordActivity.class));
            }
        });

        tvVoteRule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                Intent goto_webView = new Intent(VoteALActivity.this, CommonWebActivity.class);
                goto_webView.putExtra(CommonWebActivity.ACTIVITY_TITLE_INFO, getResources().getString(R.string.activity_vote_txt_062));
                goto_webView.putExtra(CommonWebActivity.WEBVIEW_LOAD_URL, Config.COMMON_WEB_URL + DAppConstants.backUrlHou(VoteALActivity.this, 12));
                startActivity(goto_webView);
            }
        });

        tvDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        // 璁剧疆鑳屾櫙棰滆壊鍙樻殫
        final WindowManager.LayoutParams lp = this.getWindow().getAttributes();
        lp.alpha = 0.7f;
        this.getWindow().setAttributes(lp);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                lp.alpha = 1f;
                getWindow().setAttributes(lp);
            }
        });

        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        popupWindow.setOutsideTouchable(true);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.BUTTON_BACK) {
                    popupWindow.dismiss();
                }
                return false;
            }
        });
    }
}
