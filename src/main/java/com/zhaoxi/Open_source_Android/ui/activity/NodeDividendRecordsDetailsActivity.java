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
import com.zhaoxi.Open_source_Android.libs.tools.CreateTxtRecordDetailsAsyncTask;
import com.zhaoxi.Open_source_Android.libs.utils.ChangeLanguageUtil;
import com.zhaoxi.Open_source_Android.libs.utils.CollectionUtil;
import com.zhaoxi.Open_source_Android.libs.utils.DateUtilSL;
import com.zhaoxi.Open_source_Android.libs.utils.FileManager;
import com.zhaoxi.Open_source_Android.libs.utils.ShareUtil;
import com.zhaoxi.Open_source_Android.libs.utils.SharedPreferencesUtil;
import com.zhaoxi.Open_source_Android.libs.utils.StrUtil;
import com.zhaoxi.Open_source_Android.net.BaseBet.CommonResultListener;
import com.zhaoxi.Open_source_Android.net.Request.GetNodeRecordDetailsRequest;
import com.zhaoxi.Open_source_Android.net.bean.DividendRecordsBean;
import com.zhaoxi.Open_source_Android.ui.adapter.NodeDividendRecordsDetilasAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrFrameLayout;

public class NodeDividendRecordsDetailsActivity extends BaseTitleBarActivity {
    public final static String NODE_RECORDS_ID = "NodeDividendRecordsDetailsActivity.NODE_RECORDS_ID";
    public final static String NODE_RECORDS_COINUNIT = "NodeDividendRecordsDetailsActivity.NODE_RECORDS_COINUNIT";
    public final static String NODE_RECORDS_HAX = "NodeDividendRecordsDetailsActivity.NODE_RECORDS_HAX";

    @BindView(R.id.nodedividend_root_view)
    LinearLayout mRootView;
    @BindView(R.id.activity_dividend_records_details_list_cntent)
    LoadMoreRecyclerView mContentView;
    @BindView(R.id.activity_dividend_records_details_ptrfrsh_layout)
    MyPtrFrameLayout mRefreshLayout;

    private LoadMoreRecyclerAdapter mListAdapter;
    private NodeDividendRecordsDetilasAdapter mAdapter;
    private List<DividendRecordsBean.RecordsBean> mDataList = new ArrayList<>();
    private boolean isLoading = false,isFirst = true;
    private int mCurrentPage = 1;
    private int mTotalPages;
    private String mNodeId,mCoinSymbol,mTxHax;
    private int mLanguage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_node_dividend_records_details);
        ButterKnife.bind(this);
        mNodeId = getIntent().getStringExtra(NODE_RECORDS_ID);
        mCoinSymbol = getIntent().getStringExtra(NODE_RECORDS_COINUNIT);
        mTxHax = getIntent().getStringExtra(NODE_RECORDS_HAX);
        initViews();
    }

    private void initViews(){
        ImmersionBar.with(this)
                .statusBarDarkFont(true, 0.2f)
                .init();
        setTitleBgColor(R.color.white,true);
        setTitle(getResources().getString(R.string.activity_cion_fh_txt_15), true);
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
        mAdapter = new NodeDividendRecordsDetilasAdapter(this, mDataList,mCoinSymbol);
        mListAdapter = new LoadMoreRecyclerAdapter(this, mAdapter, false);

        mContentView.setAdapter(mListAdapter);
        // 设置滑到底部加载更多
        mContentView.setListener(mLoadMoreListener);
        initData(1);
    }

    private void initData(int pageNum) {
        isLoading = true;
        if (isFirst) {
            showProgressDialog();
        }
        if(StrUtil.isEmpty(mNodeId)){
            return;
        }
        String defultAddress = SharedPreferencesUtil.getSharePreString(this, SharedPreferencesUtil.CHOOSE_WALLET_ADDRESS);
        new GetNodeRecordDetailsRequest(mTxHax,defultAddress, mCurrentPage).doRequest(this, new CommonResultListener(this) {
            @Override
            public void onSuccess(JSONArray jsonArray) {
                if (jsonArray.get(2) != null) {
                    DividendRecordsBean recordsBean = JSON.parseObject(jsonArray.get(2).toString(), DividendRecordsBean.class);
                    isFirst = false;
                    if (pageNum == 1) {
                        mDataList.clear();
                        mContentView.setSmoothPosition(0);
                    }
                    if (CollectionUtil.isCollectionEmpty(recordsBean.getList())) {
                        mListAdapter.setIsNeedLoadMore(false);
                        mListAdapter.notifyDataSetChanged();
                    } else {
                        mTotalPages = recordsBean.getPages();
                        mDataList.addAll(recordsBean.getList());
                        mListAdapter.setIsNeedLoadMore(mCurrentPage < mTotalPages ? true : false);// 设置是否显示加载更多Item
                        mCurrentPage++;
                        mListAdapter.notifyDataSetChanged();
                    }
                    if (CollectionUtil.isCollectionEmpty(recordsBean.getList())) {
                        if (mLanguage == 1)
                            mContentView.setBackgroundResource(R.drawable.icon_list_empty_zh);
                        else
                            mContentView.setBackgroundResource(R.drawable.icon_list_empty_en);
                    } else {
                        mContentView.setBackgroundColor(getResources().getColor(R.color.white));
                    }
                }

                mRefreshLayout.refreshComplete();
                isLoading = false;
                dismissProgressDialog();
            }

            @Override
            public void onError(String error) {
                super.onError(error);
                mRefreshLayout.refreshComplete();
                isLoading = false;
                dismissProgressDialog();
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

    private void showPopWindow(){
        View view = LayoutInflater.from(this).inflate(R.layout.node_dividend_records_d_more_layout, null, false);
        final PopupWindow popupWindow = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setAnimationStyle(R.style.VoteDialogAnim);
        popupWindow.setTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable());
        popupWindow.showAtLocation(mRootView, Gravity.BOTTOM, 0, 0);

        TextView tvExport = view.findViewById(R.id.tv_node_dividend_rd_record);
        TextView tvRule = view.findViewById(R.id.tv_node_dividend_rd_rule);
        TextView tvDismiss = view.findViewById(R.id.tv_node_dividend_rd_dismiss);

        tvExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                //导出数据
                String txtName = "Dividend Details"+ DateUtilSL.getCurrentDate(6)+".txt";
                CreateTxtRecordDetailsAsyncTask asyncTask = new CreateTxtRecordDetailsAsyncTask(NodeDividendRecordsDetailsActivity.this, mDataList,txtName);
                asyncTask.setOnResultListener(new CreateTxtRecordDetailsAsyncTask.OnResultExportListener() {
                    @Override
                    public void setOnResultListener(boolean result) {
                        if(result){
                            String path = FileManager.createPic(DAppConstants.PATH_EXPROT_FILE)+txtName;
                            ShareUtil.shareTxtMore(NodeDividendRecordsDetailsActivity.this,path);
                        } else DappApplication.getInstance().showToast(getResources().getString(R.string.activity_cion_fh_txt_22_04));

                    }
                });
                asyncTask.execute();
            }
        });

        tvRule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                Intent goto_webView = new Intent(NodeDividendRecordsDetailsActivity.this, CommonWebActivity.class);
                goto_webView.putExtra(CommonWebActivity.ACTIVITY_TITLE_INFO, getResources().getString(R.string.activity_cion_fh_txt_30));
                goto_webView.putExtra(CommonWebActivity.WEBVIEW_LOAD_URL, Config.COMMON_WEB_URL + DAppConstants.backUrlHou(NodeDividendRecordsDetailsActivity.this, 13));
                startActivity(goto_webView);
            }
        });

        tvDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

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
