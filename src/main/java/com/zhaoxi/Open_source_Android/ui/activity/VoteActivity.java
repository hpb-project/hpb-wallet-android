package com.zhaoxi.Open_source_Android.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.zhaoxi.Open_source_Android.Config;
import com.zhaoxi.Open_source_Android.DAppConstants;
import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.base.BaseActivity;
import com.zhaoxi.Open_source_Android.common.base.DappApplication;
import com.zhaoxi.Open_source_Android.common.view.recycler.LoadMoreRecyclerAdapter;
import com.zhaoxi.Open_source_Android.common.view.recycler.LoadMoreRecyclerView;
import com.zhaoxi.Open_source_Android.common.view.refresh.MyPtrFrameLayout;
import com.zhaoxi.Open_source_Android.libs.utils.ChangeLanguageUtil;
import com.zhaoxi.Open_source_Android.libs.utils.CollectionUtil;
import com.zhaoxi.Open_source_Android.net.BaseBet.NetResultCallBack;
import com.zhaoxi.Open_source_Android.net.Request.VoteRankingRequest;
import com.zhaoxi.Open_source_Android.net.bean.VoteBean;
import com.zhaoxi.Open_source_Android.ui.adapter.VoteAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.srain.cube.views.ptr.PtrFrameLayout;

public class VoteActivity extends BaseActivity {
    @BindView(R.id.activity_vote_centerrefresh_ptr_frame_layout)
    MyPtrFrameLayout mRefreshPtrFrameLayout;
    @BindView(R.id.recycler_vote_all)
    LoadMoreRecyclerView mRecyclerView;
    @BindView(R.id.activity_vote_top_static_title)
    LinearLayout mLayoutStaticTitle;
    @BindView(R.id.activity_vote_tab_txt_1)
    TextView mTxtTab1;
    @BindView(R.id.activity_vote_tab_txt_2)
    TextView mTxtTab2;
    @BindView(R.id.activity_vote_tab_txt_3)
    TextView mTxtTab3;
    @BindView(R.id.activity_vote_tab_txt_4)
    TextView mTxtTab4;
    @BindView(R.id.activity_vote_info_back)
    ImageView mImgBack;

    private LoadMoreRecyclerAdapter mListAdapter;
    private VoteAdapter mVoteAdapter;
    private List<VoteBean.VoteInfo> mListDatas = new ArrayList<>();
    private boolean isLoading = false; // 标注是否正在加载 防止多次加载
    private int mCurrentPage = 1;
    private int mTotalPages;

    private int mCurTab = 1,mCurType = -1;
    private boolean isVisibility = false;
    private boolean isFirstIn = true;
    private String mBonusIsOpen = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote);
        ButterKnife.bind(this);
        initViews();
    }

    private void initViews() {
        mRefreshPtrFrameLayout.setLastUpdateTimeRelateObject(this);
        mRefreshPtrFrameLayout.setUltraPullToRefresh(mRefreshListener, mRecyclerView);
        mRefreshPtrFrameLayout.changeFlshBackgroud(R.color.base_theme_color);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        int type = ChangeLanguageUtil.languageType(this);
        mVoteAdapter = new VoteAdapter(this, mListDatas, mCurTab,type,mBonusIsOpen);
        mListAdapter = new LoadMoreRecyclerAdapter(this, mVoteAdapter, false);

        mRecyclerView.setAdapter(mListAdapter);
        // 设置滑到底部加载更多
        mRecyclerView.setListener(mLoadMoreListener);

        initDatas(mCurrentPage, mCurType);
        // 设置上滑标题固定
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int firstVisibleItemPosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                if (firstVisibleItemPosition >= 1) {
                    mLayoutStaticTitle.setVisibility(View.VISIBLE);
                    changeOne(mCurTab);
                    mImgBack.setVisibility(View.GONE);
                    isVisibility = true;
                } else {
                    mLayoutStaticTitle.setVisibility(View.GONE);
                    mVoteAdapter.setCurTabChange(mCurTab);
                    mImgBack.setVisibility(View.VISIBLE);
                    isVisibility = false;
                }
            }
        });

        mVoteAdapter.setTabChangeLisener(new VoteAdapter.onTabChangeLisener() {
            @Override
            public void onChange(int curTab, int curType) {
//                MobclickAgent.onEvent(VoteActivity.this, "30330000", "我的-竞选投票-筛选节点类型");
                mCurTab = curTab;
                mVoteAdapter.setCurTab(curTab);
                mCurrentPage = 1;
                initDatas(mCurrentPage, curType);
            }

            @Override
            public void queryVote(String content) {
                Intent it_result = new Intent(VoteActivity.this, VoteQueryActivity.class);
                it_result.putExtra(VoteQueryActivity.KEYWORD_RESULT, content);
                startActivity(it_result);
            }

            @Override
            public void onLookRule() {
                Intent goto_webView = new Intent(VoteActivity.this, CommonWebActivity.class);
                goto_webView.putExtra(CommonWebActivity.ACTIVITY_TITLE_INFO, getResources().getString(R.string.activity_vote_txt_062));
                goto_webView.putExtra(CommonWebActivity.WEBVIEW_LOAD_URL, Config.COMMON_WEB_URL+ DAppConstants.backUrlHou(VoteActivity.this,10));
                startActivity(goto_webView);
            }
        });
    }

    private void initDatas(int pageNum, int curType) {
        isLoading = true;
        if (isFirstIn) {
            showProgressDialog();
            isFirstIn = false;
        }
        new VoteRankingRequest("", String.valueOf(curType), pageNum).doRequest(this, new NetResultCallBack() {
            @Override
            public void onSuccess(JSONArray jsonArray) {
                VoteBean voteBean = JSON.parseObject(jsonArray.get(2).toString(), VoteBean.class);
                if (pageNum == 1) {
                    mListDatas.clear();
                    mRecyclerView.setSmoothPosition(0);
                }
                if (CollectionUtil.isCollectionEmpty(voteBean.getList())) {
                    mListAdapter.setIsNeedLoadMore(false);
                    mListAdapter.notifyDataSetChanged();
                } else {
                    mTotalPages = voteBean.getPages();
                    mBonusIsOpen = voteBean.getBonusIsOpen();
                    mListDatas.addAll(voteBean.getList());
                    mListAdapter.setIsNeedLoadMore(mCurrentPage < mTotalPages ? true : false);// 设置是否显示加载更多Item
                    mCurrentPage++;
                    mVoteAdapter.setBonusIsOpen(mBonusIsOpen);
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

    private MyPtrFrameLayout.OnRefreshListener mRefreshListener = new MyPtrFrameLayout.OnRefreshListener() {
        @Override
        public void refresh(PtrFrameLayout frame) {
            if (!isLoading) {
                mCurrentPage = 1;
                initDatas(1, mCurType);
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
                initDatas(mCurrentPage, mCurType);
            }
        }
    };

    @OnClick({R.id.activity_vote_tab_txt_1, R.id.activity_vote_tab_txt_2,
            R.id.activity_vote_tab_txt_3, R.id.activity_vote_tab_txt_4,
            R.id.activity_vote_info_back, R.id.activity_vote_top_static_myvote})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.activity_vote_tab_txt_1:
                if (mCurTab != 1) {
                    changeOne(1);
                    mCurTab = 1;
                    mCurType = -1;
                    mVoteAdapter.setCurTab(mCurTab);
                    mCurrentPage = 1;
                    initDatas(mCurrentPage, mCurType);
                    mRecyclerView.smoothScrollToPosition(1);
                }
                break;
            case R.id.activity_vote_tab_txt_2:
                if (mCurTab != 2) {
                    changeOne(2);
                    mCurTab = 2;
                    mCurType = 1;
                    mVoteAdapter.setCurTab(mCurTab);
                    mCurrentPage = 1;
                    initDatas(mCurrentPage, mCurType);
                    mRecyclerView.smoothScrollToPosition(1);
                }
                break;
            case R.id.activity_vote_tab_txt_3:
                if (mCurTab != 3) {
                    changeOne(3);
                    mCurTab = 3;
                    mCurType = 0;
                    mVoteAdapter.setCurTab(mCurTab);
                    mCurrentPage = 1;
                    initDatas(mCurrentPage, mCurType);
                    mRecyclerView.smoothScrollToPosition(1);
                }
                break;
            case R.id.activity_vote_tab_txt_4:
                if (mCurTab != 4) {
                    changeOne(4);
                    mCurTab = 4;
                    mCurType = 2;
                    mVoteAdapter.setCurTab(mCurTab);
                    mCurrentPage = 1;
                    initDatas(mCurrentPage, mCurType);
                    mRecyclerView.smoothScrollToPosition(1);
                }
                break;
            case R.id.activity_vote_info_back:
                finish();
                break;
            case R.id.activity_vote_top_static_myvote:
                startActivity(new Intent(VoteActivity.this, MyVoteRecordActivity.class));
                break;
        }
    }

    private void changeOne(int curTab) {
        if (curTab == 1) {
            setSelect(getResources().getColor(R.color.color_2E2F47),
                    getResources().getColor(R.color.color_black_666),
                    getResources().getColor(R.color.color_black_666),
                    getResources().getColor(R.color.color_black_666),
                    getResources().getColor(R.color.white),
                    getResources().getColor(R.color.color_EEFDF4),
                    getResources().getColor(R.color.color_EEFDF4),
                    getResources().getColor(R.color.color_EEFDF4));
        } else if (curTab == 2) {
            setSelect(getResources().getColor(R.color.color_black_666),
                    getResources().getColor(R.color.color_2E2F47),
                    getResources().getColor(R.color.color_black_666),
                    getResources().getColor(R.color.color_black_666),
                    getResources().getColor(R.color.color_EEFDF4),
                    getResources().getColor(R.color.white),
                    getResources().getColor(R.color.color_EEFDF4),
                    getResources().getColor(R.color.color_EEFDF4));
        } else if (curTab == 3) {
            setSelect(getResources().getColor(R.color.color_black_666),
                    getResources().getColor(R.color.color_black_666),
                    getResources().getColor(R.color.color_2E2F47),
                    getResources().getColor(R.color.color_black_666),
                    getResources().getColor(R.color.color_EEFDF4),
                    getResources().getColor(R.color.color_EEFDF4),
                    getResources().getColor(R.color.white),
                    getResources().getColor(R.color.color_EEFDF4));
        } else {
            setSelect(getResources().getColor(R.color.color_black_666),
                    getResources().getColor(R.color.color_black_666),
                    getResources().getColor(R.color.color_black_666),
                    getResources().getColor(R.color.color_2E2F47),
                    getResources().getColor(R.color.color_EEFDF4),
                    getResources().getColor(R.color.color_EEFDF4),
                    getResources().getColor(R.color.color_EEFDF4),
                    getResources().getColor(R.color.white));
        }
    }

    private void setSelect(int color1, int color2, int color3, int color4,int bgcolor1, int bgcolor2, int bgcolor3, int bgcolor4) {
        mTxtTab1.setTextColor(color1);
        mTxtTab1.setBackgroundColor(bgcolor1);
        mTxtTab2.setTextColor(color2);
        mTxtTab2.setBackgroundColor(bgcolor2);
        mTxtTab3.setTextColor(color3);
        mTxtTab3.setBackgroundColor(bgcolor3);
        mTxtTab4.setTextColor(color4);
        mTxtTab4.setBackgroundColor(bgcolor4);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (!isVisibility) {
                finish();
            } else {
                mRecyclerView.smoothScrollToPosition(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
