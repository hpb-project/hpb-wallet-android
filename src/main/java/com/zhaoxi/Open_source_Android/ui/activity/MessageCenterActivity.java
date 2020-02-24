package com.zhaoxi.Open_source_Android.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.base.BaseTitleBarActivity;
import com.zhaoxi.Open_source_Android.common.base.DappApplication;
import com.zhaoxi.Open_source_Android.db.DBManager;
import com.zhaoxi.Open_source_Android.db.HistoryCache;
import com.zhaoxi.Open_source_Android.libs.utils.ChangeLanguageUtil;
import com.zhaoxi.Open_source_Android.libs.utils.CollectionUtil;
import com.zhaoxi.Open_source_Android.libs.utils.StrUtil;
import com.zhaoxi.Open_source_Android.libs.utils.SystemInfoUtil;
import com.zhaoxi.Open_source_Android.net.BaseBet.NetResultCallBack;
import com.zhaoxi.Open_source_Android.net.Request.GetMassegeRequest;
import com.zhaoxi.Open_source_Android.net.bean.MeassgeBean;
import com.zhaoxi.Open_source_Android.common.view.recycler.LoadMoreRecyclerAdapter;
import com.zhaoxi.Open_source_Android.common.view.recycler.LoadMoreRecyclerView;
import com.zhaoxi.Open_source_Android.common.view.refresh.MyPtrFrameLayout;
import com.zhaoxi.Open_source_Android.ui.adapter.MessageCenterAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrFrameLayout;

public class MessageCenterActivity extends BaseTitleBarActivity {

    @BindView(R.id.activity_message_center_list)
    LoadMoreRecyclerView mContentView;
    @BindView(R.id.activity_message_centerrefresh_ptr_frame_layout)
    MyPtrFrameLayout mRefreshPtrFrameLayout;

    private LoadMoreRecyclerAdapter mListAdapter;
    private List<MeassgeBean.MeassgeInfo> mListData = new ArrayList<>();

    private boolean isLoading = false;
    private int mCurrentPage = 1;
    private int mTotalPages;
    private boolean isFirstIn = true;
    private int mLanguage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_center);
        ButterKnife.bind(this);

        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initDatas(1);
    }

    private void initViews() {
        setTitle(R.string.activity_message_center_title,true);
        mLanguage = ChangeLanguageUtil.languageType(this);
        mRefreshPtrFrameLayout.setLastUpdateTimeRelateObject(this);
        mRefreshPtrFrameLayout.setUltraPullToRefresh(mRefreshListener, mContentView);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mContentView.setLayoutManager(mLayoutManager);
        MessageCenterAdapter adapter = new MessageCenterAdapter(this, mListData);
        mListAdapter = new LoadMoreRecyclerAdapter(this, adapter, false);
        mContentView.setAdapter(mListAdapter);
        // 设置滑到底部加载更多
        mContentView.setListener(mLoadMoreListener);
    }

    private void initDatas(int pageNum) {
        String datas = DBManager.queryHistory(HistoryCache.MessageList);
        if (!StrUtil.isEmpty(datas) && pageNum == 1 && isFirstIn) {
            mListData.clear();
            mContentView.setSmoothPosition(0);
            MeassgeBean meassgeBean = JSON.parseObject(datas, MeassgeBean.class);
            mListData.addAll(meassgeBean.getList());
            mListAdapter.notifyDataSetChanged();
            mRefreshPtrFrameLayout.refreshComplete();
        }
        isLoading = true;
        if (pageNum == 1) {
            showProgressDialog();
        }
        //获取设备id
        String deviceId = SystemInfoUtil.getDeviceId(this);
        int type = ChangeLanguageUtil.languageType(this);
        new GetMassegeRequest(deviceId, type, pageNum).doRequest(this, new NetResultCallBack() {
            @Override
            public void onSuccess(JSONArray jsonArray) {
                MeassgeBean meassgeBean = JSON.parseObject(jsonArray.get(2).toString(), MeassgeBean.class);
                if (pageNum == 1) {
                    mListData.clear();
                    mContentView.setSmoothPosition(0);
                }
                if (CollectionUtil.isCollectionEmpty(meassgeBean.getList())) {
                    mListAdapter.setIsNeedLoadMore(false);
                    mListAdapter.notifyDataSetChanged();
                } else {
                    mTotalPages = meassgeBean.getPages();
                    mListData.addAll(meassgeBean.getList());
                    mCurrentPage++;
                    mListAdapter.setIsNeedLoadMore(mCurrentPage < mTotalPages ? true : false);// 设置是否显示加载更多Item
                    mListAdapter.notifyDataSetChanged();
                    if (pageNum == 1) {
                        DBManager.insertHistory(HistoryCache.MessageList, jsonArray.get(2).toString());
                    }
                }
                if (CollectionUtil.isCollectionEmpty(meassgeBean.getList())) {
                    if (mLanguage == 1)
                        mContentView.setBackgroundResource(R.drawable.icon_message_empty_zh);
                    else mContentView.setBackgroundResource(R.drawable.icon_message_empty_en);

                } else {
                    mContentView.setBackgroundColor(getResources().getColor(R.color.color_F5F5F5));
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
                initDatas(1);
            }
        }
    };


    private LoadMoreRecyclerView.OnLoadMoreListener mLoadMoreListener = new LoadMoreRecyclerView.OnLoadMoreListener() {
        @Override
        public void loadMore() {
            // 设置滑到底部加载更多
            if (!isLoading && mCurrentPage < mTotalPages) {
                initDatas(mCurrentPage);
            }
        }
    };
}
