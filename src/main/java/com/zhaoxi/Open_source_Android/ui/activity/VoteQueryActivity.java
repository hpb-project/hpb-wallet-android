package com.zhaoxi.Open_source_Android.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.base.BaseTitleBarActivity;
import com.zhaoxi.Open_source_Android.common.view.recycler.LoadMoreRecyclerAdapter;
import com.zhaoxi.Open_source_Android.common.view.recycler.LoadMoreRecyclerView;
import com.zhaoxi.Open_source_Android.libs.utils.CollectionUtil;
import com.zhaoxi.Open_source_Android.libs.utils.StrUtil;
import com.zhaoxi.Open_source_Android.net.BaseBet.CommonResultListener;
import com.zhaoxi.Open_source_Android.net.Request.VoteRankingRequest;
import com.zhaoxi.Open_source_Android.net.bean.VoteBean;
import com.zhaoxi.Open_source_Android.ui.adapter.VoteQueryAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VoteQueryActivity extends BaseTitleBarActivity {
    public static final String KEYWORD_RESULT = "VoteQueryActivity.KEYWORD_RESULT";
    @BindView(R.id.activity_vote_query_list)
    LoadMoreRecyclerView mVoteQueryList;

    private VoteQueryAdapter mVoteQueryAdapter;
    private LoadMoreRecyclerAdapter mListAdapter;
    private List<VoteBean.VoteInfo> mListDatas = new ArrayList<>();

    private boolean isLoading = false;
    private int mCurrentPage = 1;
    private int mTotalPages;

    private String mKeyword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote_query);
        ButterKnife.bind(this);
        mKeyword = getIntent().getStringExtra(KEYWORD_RESULT);
        if(StrUtil.isEmpty(mKeyword)){
            finish();
        }
        initViews();
        initDatas(1);
    }

    private void initViews() {
        setTitleTransparent();
        setTitle(R.string.activity_vote_txt_091,true);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mVoteQueryList.setLayoutManager(mLayoutManager);
        mVoteQueryAdapter = new VoteQueryAdapter(this, mListDatas);
        mListAdapter = new LoadMoreRecyclerAdapter(this, mVoteQueryAdapter, false);
        mVoteQueryList.setAdapter(mListAdapter);
        // 设置滑到底部加载更多
        mVoteQueryList.setListener(mLoadMoreListener);
    }

    private void initDatas(int pageNum){
        new VoteRankingRequest(mKeyword,"-1",pageNum).doRequest(this,new CommonResultListener(this){
            @Override
            public void onSuccess(JSONArray jsonArray) {
                super.onSuccess(jsonArray);
                VoteBean voteBean = JSON.parseObject(jsonArray.get(2).toString(), VoteBean.class);
                if (pageNum == 1) {
                    mListDatas.clear();
                    mVoteQueryList.setSmoothPosition(0);
                }
                if (CollectionUtil.isCollectionEmpty(voteBean.getList())) {
                    mListAdapter.setIsNeedLoadMore(false);
                    mListAdapter.notifyDataSetChanged();
                } else {
                    mTotalPages = voteBean.getPages();
                    mListDatas.addAll(voteBean.getList());
                    mListAdapter.setIsNeedLoadMore(mCurrentPage < mTotalPages ? true : false);// 设置是否显示加载更多Item
                    mCurrentPage++;
                    mListAdapter.notifyDataSetChanged();
                }
                isLoading = false;
                dismissProgressDialog();
            }
        });
    }

    private LoadMoreRecyclerView.OnLoadMoreListener mLoadMoreListener = new LoadMoreRecyclerView.OnLoadMoreListener() {
        @Override
        public void loadMore() {
            // 设置滑到底部加载更多
            if (!isLoading && mCurrentPage <= mTotalPages) {
                initDatas(mCurrentPage);
            }
        }
    };
}
