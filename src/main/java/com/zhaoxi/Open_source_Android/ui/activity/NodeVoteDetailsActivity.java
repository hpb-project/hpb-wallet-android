package com.zhaoxi.Open_source_Android.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.gyf.immersionbar.ImmersionBar;
import com.zhaoxi.Open_source_Android.DAppConstants;
import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.base.BaseTitleBarActivity;
import com.zhaoxi.Open_source_Android.common.base.DappApplication;
import com.zhaoxi.Open_source_Android.common.view.recycler.LoadMoreRecyclerAdapter;
import com.zhaoxi.Open_source_Android.common.view.recycler.LoadMoreRecyclerView;
import com.zhaoxi.Open_source_Android.common.view.refresh.MyPtrFrameLayout;
import com.zhaoxi.Open_source_Android.libs.tools.CreateTxtAsyncTask;
import com.zhaoxi.Open_source_Android.libs.utils.DateUtilSL;
import com.zhaoxi.Open_source_Android.libs.utils.FileManager;
import com.zhaoxi.Open_source_Android.libs.utils.ShareUtil;
import com.zhaoxi.Open_source_Android.libs.utils.SharedPreferencesUtil;
import com.zhaoxi.Open_source_Android.libs.utils.StrUtil;
import com.zhaoxi.Open_source_Android.net.BaseBet.CommonResultListener;
import com.zhaoxi.Open_source_Android.net.Request.GetNodeBonusRequest;
import com.zhaoxi.Open_source_Android.net.UrlContext;
import com.zhaoxi.Open_source_Android.net.bean.NodeDividenBean;
import com.zhaoxi.Open_source_Android.ui.adapter.NoteVoteDetailsAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrFrameLayout;

public class NodeVoteDetailsActivity extends BaseTitleBarActivity {
    public static final String DEFULT_BILE = "NodeVoteDetailsActivity.DEFULT_BILE";
    @BindView(R.id.activty_node_vote_details_content)
    LoadMoreRecyclerView mLayoutContent;
    @BindView(R.id.activty_node_vote_details_refresh_layout)
    MyPtrFrameLayout mRefreshLayout;

    private LoadMoreRecyclerAdapter mListAdapter;
    private NoteVoteDetailsAdapter mAdapter;
    private boolean isLoading = false; // 标注是否正在加载 防止多次加载
    private int mCurrentPage = 1;
    private int mTotalPages;
    private String mBonusContractAddr,mFenHBl,mIsCanSet = "0";//合约地址

    private NodeDividenBean mNodeDividendBean = new NodeDividenBean();
    private List<NodeDividenBean.VoteDetailsBean> mData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_node_vote_details);
        ButterKnife.bind(this);
        mFenHBl = getIntent().getStringExtra(DEFULT_BILE);
        initViews();
    }

    private void initViews() {
        ImmersionBar.with(this)
                .statusBarDarkFont(false, 0.2f)
                .init();
        setTitleBgColor(R.color.base_new_theme_color, false);
        setTitle(getResources().getString(R.string.activity_cion_fh_txt_21_01), true);

        showRightTxtWithTextListener(getResources().getString(R.string.activity_cion_fh_txt_12), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //分红记录
                startActivity(new Intent(NodeVoteDetailsActivity.this, NodeDividendRecordsActivity.class));
            }
        });

        mRefreshLayout.setLastUpdateTimeRelateObject(this);
        mRefreshLayout.setUltraPullToRefresh(mRefreshListener, mLayoutContent);
        mRefreshLayout.changeFlshBackgroud(R.color.base_new_theme_color);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mLayoutContent.setLayoutManager(mLayoutManager);
        mAdapter = new NoteVoteDetailsAdapter(this, mNodeDividendBean, mData);
        mListAdapter = new LoadMoreRecyclerAdapter(this, mAdapter, false);
        mAdapter.setOnReadListenner(new NoteVoteDetailsAdapter.OnReadListenner() {
            @Override
            public void onSendDividendlistenner() {
                Intent it_set = new Intent(NodeVoteDetailsActivity.this, SendDevidendActivity.class);
                it_set.putExtra(SendDevidendActivity.CONTRACTADDR,mBonusContractAddr);
                it_set.putExtra(SendDevidendActivity.BILI,mFenHBl);
                it_set.putExtra(SendDevidendActivity.IS_CAN_SET, !StrUtil.isEmpty(mIsCanSet) && mIsCanSet.equals("1")?true:false);
                startActivity(it_set);
            }

            @Override
            public void onExprotDatalistenner() {
                String txtName = "Voting Details"+ DateUtilSL.getCurrentDate(6)+".txt";
                CreateTxtAsyncTask asyncTask = new CreateTxtAsyncTask(NodeVoteDetailsActivity.this, mData, txtName);
                asyncTask.setOnResultListener(new CreateTxtAsyncTask.OnResultExportListener() {
                    @Override
                    public void setOnResultListener(boolean result) {
                        if (result) {
                            String path = FileManager.createPic(DAppConstants.PATH_EXPROT_FILE) + txtName;
                            ShareUtil.shareTxtMore(NodeVoteDetailsActivity.this, path);
                        } else DappApplication.getInstance().showToast(getResources().getString(R.string.activity_cion_fh_txt_22_04));

                    }
                });
                asyncTask.execute();
            }
        });
        mLayoutContent.setAdapter(mListAdapter);
        // 设置滑到底部加载更多
        mLayoutContent.setListener(mLoadMoreListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private void initData() {
        isLoading = true;
        String defultAddress = SharedPreferencesUtil.getSharePreString(this, SharedPreferencesUtil.CHOOSE_WALLET_ADDRESS);
        new GetNodeBonusRequest(defultAddress, UrlContext.Url_NodeVoteDetail.getContext()).doRequest(this, new CommonResultListener(this) {
            @Override
            public void onSuccess(JSONArray jsonArray) {
                if (jsonArray.get(2) != null) {
                    NodeDividenBean bean = JSON.parseObject(jsonArray.get(2).toString(), NodeDividenBean.class);
                    mBonusContractAddr = bean.getBonusContractAddr();
                    mFenHBl = bean.getBonusRate();
                    mIsCanSet = bean.getIsCanSet();
                    mNodeDividendBean.setVotersNum(bean.getVotersNum());
                    mNodeDividendBean.setPoolsNum(bean.getPoolsNum());
                    if(mData.size()>0){
                        mData.clear();
                    }
                    mData.addAll(bean.getList());

                    mListAdapter.notifyDataSetChanged();
                    mRefreshLayout.refreshComplete();
                    isLoading = false;
                }
            }

            @Override
            public void onError(String error) {
                super.onError(error);
                mRefreshLayout.refreshComplete();
                isLoading = false;
            }
        });
    }


    /**
     * 下拉刷新监听事件
     */
    private MyPtrFrameLayout.OnRefreshListener mRefreshListener = new MyPtrFrameLayout.OnRefreshListener() {
        @Override
        public void refresh(PtrFrameLayout frame) {
            if (!isLoading) {
                mCurrentPage = 1;
                initData();
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
                initData();
            }
        }
    };
}
