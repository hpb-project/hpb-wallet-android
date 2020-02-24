package com.zhaoxi.Open_source_Android.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.View;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.gyf.immersionbar.ImmersionBar;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;
import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.base.BaseTitleBarActivity;
import com.zhaoxi.Open_source_Android.common.base.DappApplication;
import com.zhaoxi.Open_source_Android.common.view.recycler.LoadMoreRecyclerAdapter;
import com.zhaoxi.Open_source_Android.common.view.recycler.LoadMoreRecyclerView;
import com.zhaoxi.Open_source_Android.common.view.refresh.MyPtrFrameLayout;
import com.zhaoxi.Open_source_Android.db.DBManager;
import com.zhaoxi.Open_source_Android.libs.utils.ChangeLanguageUtil;
import com.zhaoxi.Open_source_Android.libs.utils.CollectionUtil;
import com.zhaoxi.Open_source_Android.libs.utils.SharedPreferencesUtil;
import com.zhaoxi.Open_source_Android.net.BaseBet.NetResultCallBack;
import com.zhaoxi.Open_source_Android.net.Request.TransactionListRequest;
import com.zhaoxi.Open_source_Android.net.bean.TranferRecordBean;
import com.zhaoxi.Open_source_Android.ui.adapter.TransationRecordAdapter;
import com.zhaoxi.Open_source_Android.ui.dialog.TransferRecordsChoosePop;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import in.srain.cube.views.ptr.PtrFrameLayout;



/**
 * 交易记录
 *
 * @author zhutt on 2018-06-15
 */
public class TransationRecordActivity extends BaseTitleBarActivity {
    public static final String RESOUCE_ADDRESS = "TransationRecordActivity.RESOUCE_ADDRESS";

    Unbinder unbinder;
    @BindView(R.id.transation_record_list)
    LoadMoreRecyclerView mContentView;
    @BindView(R.id.transation_record_refresh_ptr_frame_layout)
    MyPtrFrameLayout mRefreshPtrFrameLayout;
    @BindView(R.id.transation_record_root)
    LinearLayout mRootView;

    private LoadMoreRecyclerAdapter mListAdapter;
    private List<TranferRecordBean.TransferInfo> mDataList = new ArrayList<>();
    private TransationRecordAdapter mTranAdapter;
    public static String mChooseAddress;

    private boolean isLoading = false;
    private int mCurrentPage = 1;
    private int mTotalPages;
    private boolean isFirstIn = true;
    private int mLanguage,mCurCoinType = 1;
    private String mContractType = "HPB",mContractAddress,mTokenSymbol;//合约类型,合约地址,代币简称

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transation_record);
        ButterKnife.bind(this);

        initViews();
        initData(1);
    }

    private void initViews() {
        mChooseAddress = SharedPreferencesUtil.getSharePreString(this, SharedPreferencesUtil.CHOOSE_WALLET_ADDRESS);
        ImmersionBar.with(this)
                .statusBarDarkFont(true, 0.2f)
                .init();
        setTitleBgColor(R.color.white,true);

        setTitle(R.string.main_me_manager_txt_transfer,true);
        showRightTxtWithTextListener(getResources().getString(R.string.activity_records_txt_01), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRightDialog();
            }
        });
        mLanguage = ChangeLanguageUtil.languageType(this);


        mRefreshPtrFrameLayout.setLastUpdateTimeRelateObject(this);
        mRefreshPtrFrameLayout.setUltraPullToRefresh(mRefreshListener, mContentView);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mContentView.setLayoutManager(mLayoutManager);
        mTranAdapter = new TransationRecordAdapter(this, mDataList);
        final StickyRecyclerHeadersDecoration headersDecor = new StickyRecyclerHeadersDecoration(mTranAdapter);
        mContentView.addItemDecoration(headersDecor);
        mListAdapter = new LoadMoreRecyclerAdapter(this, mTranAdapter, false);
        mListAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                headersDecor.invalidateHeaders();
            }
        });
        mContentView.setAdapter(mListAdapter);
        // 设置滑到底部加载更多
        mContentView.setListener(mLoadMoreListener);
        ((SimpleItemAnimator) mContentView.getItemAnimator()).setSupportsChangeAnimations(false); //取消RecyclerView的动画效果
    }

    private void showRightDialog() {
        TransferRecordsChoosePop choosePop = new TransferRecordsChoosePop(this, mRootView);
        choosePop.setHandleChoose(new TransferRecordsChoosePop.HandleChoose() {
            @Override
            public void getChooseData(int type, String contractType, String contractAddress, String name) {
                //查询交易记录
                mCurCoinType = type;
                mContractType = contractType;
                mContractAddress = contractAddress;
                mTokenSymbol = name;
                mCurrentPage = 1;
                isFirstIn = true;
                initData(mCurrentPage);
            }
        });
        choosePop.show(mCurCoinType);
    }

    private MyPtrFrameLayout.OnRefreshListener mRefreshListener = new MyPtrFrameLayout.OnRefreshListener() {
        @Override
        public void refresh(PtrFrameLayout frame) {
            if (!isLoading) {
                mCurrentPage = 1;
                initData(1);
            }
        }
    };


    private LoadMoreRecyclerView.OnLoadMoreListener mLoadMoreListener = new LoadMoreRecyclerView.OnLoadMoreListener() {
        @Override
        public void loadMore() {
            // 设置滑到底部加载更多
            if (!isLoading && mCurrentPage <= mTotalPages) {
                initData(mCurrentPage);
            }
        }
    };

    private void initData(int pageNum) {
        String address = SharedPreferencesUtil.getSharePreString(this, SharedPreferencesUtil.CHOOSE_WALLET_ADDRESS);
        isLoading = true;
        if (pageNum == 1 && isFirstIn) {
            showProgressDialog();
        }
        new TransactionListRequest(mChooseAddress,mContractType,mContractType.equals("HPB")?"":mContractAddress
                ,mContractType.equals("HPB")?"":mTokenSymbol,pageNum, "0")
                .doRequest(this,new NetResultCallBack() {
                    @Override
                    public void onSuccess(JSONArray jsonArray) {
                        isFirstIn = false;
                        if(jsonArray.get(2) != null){
                            TranferRecordBean tranferRecordBean = JSON.parseObject(jsonArray.get(2).toString(), TranferRecordBean.class);
                            if (pageNum == 1) {
                                mDataList.clear();
                                mContentView.setSmoothPosition(0);
                            }
                            if (CollectionUtil.isCollectionEmpty(tranferRecordBean.getList())) {
                                mListAdapter.setIsNeedLoadMore(false);
                                mListAdapter.notifyDataSetChanged();
                            } else {
                                mTotalPages = tranferRecordBean.getPages();
                                mDataList.addAll(tranferRecordBean.getList());
                                mListAdapter.setIsNeedLoadMore(mCurrentPage < mTotalPages ? true : false);// 设置是否显示加载更多Item
                                mCurrentPage++;
                                mListAdapter.notifyDataSetChanged();
                                isFirstIn = false;
                                if (pageNum == 1 && address.equals(mChooseAddress)) {
                                    DBManager.insertHistory(address, jsonArray.get(2).toString());
                                }
                            }

                            if (CollectionUtil.isCollectionEmpty(tranferRecordBean.getList())) {
                                if (mLanguage == 1)
                                    mContentView.setBackgroundResource(R.drawable.icon_transfer_record_empty_zh);
                                else mContentView.setBackgroundResource(R.drawable.icon_transfer_record_empty_en);

                            } else {
                                mContentView.setBackgroundColor(getResources().getColor(R.color.white));
                            }
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
                        isFirstIn = false;
                        DappApplication.getInstance().showToast(error);
                    }
                });
    }
}
