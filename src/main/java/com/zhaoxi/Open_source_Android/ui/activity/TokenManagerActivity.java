package com.zhaoxi.Open_source_Android.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.gyf.immersionbar.ImmersionBar;
import com.zhaoxi.Open_source_Android.DAppConstants;
import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.base.BaseTitleBarActivity;
import com.zhaoxi.Open_source_Android.common.base.DappApplication;
import com.zhaoxi.Open_source_Android.common.drag.ItemTouchCallback;
import com.zhaoxi.Open_source_Android.common.view.lrecyclerview.interfaces.OnRefreshListener;
import com.zhaoxi.Open_source_Android.common.view.lrecyclerview.recyclerview.LRecyclerView;
import com.zhaoxi.Open_source_Android.common.view.lrecyclerview.recyclerview.LRecyclerViewAdapter;
import com.zhaoxi.Open_source_Android.db.greendao.AssetsBeanDao;
import com.zhaoxi.Open_source_Android.db.greendao.DaoSession;
import com.zhaoxi.Open_source_Android.libs.tools.SystemLog;
import com.zhaoxi.Open_source_Android.libs.utils.ChangeLanguageUtil;
import com.zhaoxi.Open_source_Android.libs.utils.CollectionUtil;
import com.zhaoxi.Open_source_Android.libs.utils.SharedPreferencesUtil;
import com.zhaoxi.Open_source_Android.net.BaseBet.NetResultCallBack;
import com.zhaoxi.Open_source_Android.net.Request.TokenManageRequest;
import com.zhaoxi.Open_source_Android.net.bean.AssetsBean;
import com.zhaoxi.Open_source_Android.net.bean.TokenInfo;
import com.zhaoxi.Open_source_Android.net.bean.TokenManageBean;
import com.zhaoxi.Open_source_Android.ui.adapter.TokenManagerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TokenManagerActivity extends BaseTitleBarActivity implements /*MyPtrFrameLayout.OnRefreshListener,*/ TokenManagerAdapter.OnItemTouchCallbackListener, OnRefreshListener {

    //    @BindView(R.id.recycler_with_refresh_ptr_frame_layout)
//    MyPtrFrameLayout mPtrFrameLayout;
    @BindView(R.id.token_manager_recycler_view)
    LRecyclerView mTokenManagerRecyclerView;
    @BindView(R.id.iv_default_empty)
    ImageView mDefaultEmpty;

    private TokenManagerAdapter mTokenManagerAdapter;
    private LRecyclerViewAdapter lRecyclerViewAdapter;

    private List<TokenInfo> tokenInfos = new ArrayList<>();
    private List<AssetsBean> assetsBeans = new ArrayList<>();
    private List<AssetsBean> mDataList = new ArrayList<>();
    private List<AssetsBean> tempList = new ArrayList<>();

    private AssetsBeanDao assetsBeanDao;
    private boolean isLoading;
    private boolean isFirstLoad = true;//首次加载
    private int mLastIndex;
    private int mLanguage;
    private String mDefaultAddress;
    private AssetsBean currentAssets;
    private DaoSession daoSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_token_manager);
        ButterKnife.bind(this);
        initView();
        initData();
        initListener();
    }

    private void initView() {
        ImmersionBar.with(this)
                .statusBarDarkFont(true, 0.2f)
                .init();
        setTitleBgColor(R.color.white, true);
        setTitle(getResources().getString(R.string.activity_token_manager_txt_1), true);
        mLanguage = ChangeLanguageUtil.languageType(this);
//        mPtrFrameLayout.setLastUpdateTimeRelateObject(this);
//        mPtrFrameLayout.setUltraPullToRefresh(this, mTokenManagerRecyclerView);
//        mPtrFrameLayout.changeWhiteBackgroud();
        mDefaultAddress = SharedPreferencesUtil.getSharePreString(this, SharedPreferencesUtil.CHOOSE_WALLET_ADDRESS);
        assetsBeanDao = DappApplication.getInstance().getDaoSession().getAssetsBeanDao();
        daoSession = DappApplication.getInstance().getDaoSession();
        LinearLayoutManager lm = new LinearLayoutManager(this);
        //mTokenManagerRecyclerView.addItemDecoration(new RecyclerViewItemDecoration(this, LinearLayoutManager.VERTICAL, R.drawable.shape_under_line));
        mTokenManagerRecyclerView.setLayoutManager(lm);
        mTokenManagerRecyclerView.setLastUpdateTimeRelateObject(this);
        mTokenManagerRecyclerView.setOnRefreshListener(this);
        mTokenManagerAdapter = new TokenManagerAdapter(this, assetsBeans);
        lRecyclerViewAdapter = new LRecyclerViewAdapter(mTokenManagerAdapter);
        mTokenManagerRecyclerView.setAdapter(lRecyclerViewAdapter);

        // 手指长按移动
        ItemTouchHelper touchHelper = new ItemTouchHelper(new ItemTouchCallback(this));
        touchHelper.attachToRecyclerView(mTokenManagerRecyclerView);
        mTokenManagerAdapter.setOnStartDragListener(new TokenManagerAdapter.OnStartDragListener() {
            @Override
            public void startDrag(RecyclerView.ViewHolder viewHolder, AssetsBean assetsBean) {
                currentAssets = assetsBean;
                SystemLog.D("startDrag", "currentAssets = " + currentAssets);
                touchHelper.startDrag(viewHolder);
            }
        });

        mTokenManagerAdapter.setOnTouchStatusListener(new TokenManagerAdapter.OnTouchStatusListener() {
            @Override
            public void onTouch() {
                mTokenManagerRecyclerView.setPullRefreshEnabled(false);
            }

            @Override
            public void onRelease() {
                mTokenManagerRecyclerView.setPullRefreshEnabled(true);
            }
        });
    }

    private void initData() {
        if (!isLoading) {
            isLoading = true;
            if (isFirstLoad) {
                isFirstLoad = false;
                showProgressDialog();
            }
        }

        new TokenManageRequest(mDefaultAddress).doRequest(this, new NetResultCallBack() {
            @Override
            public void onSuccess(JSONArray jsonArray) {
                if (jsonArray.get(2) != null) {
                    TokenManageBean tokenManageBean = JSON.parseObject(jsonArray.get(2).toString(), TokenManageBean.class);
                    SystemLog.D("TokenManageRequest", "tokenManageBean = " + tokenManageBean.toString());
                    tokenInfos.clear();
                    assetsBeans.clear();
                    tempList.clear();
                    //mTokenManagerRecyclerView.smoothScrollToPosition(1);
                    if (!CollectionUtil.isCollectionEmpty(tokenManageBean.getList())) {
                        tokenInfos.addAll(tokenManageBean.getList());
                        for (TokenInfo tokenInfo : tokenInfos) {
                            AssetsBean assetsBean = new AssetsBean(
                                    tokenInfo.getId()
                                    , tokenInfo.getTokenSymbol()
                                    , tokenInfo.getTokenSymbolImageUrl()
                                    , tokenInfo.getTokenName()
                                    , tokenInfo.getContractCreater()
                                    , tokenInfo.getContractAddress()
                                    , tokenInfo.getTokenTotalSupply()
                                    , tokenInfo.getContractType()
                                    , tokenInfo.getTransfersNum()
                                    , tokenInfo.getCnyPrice()
                                    , tokenInfo.getUsdPrice()
                                    , tokenInfo.getCnyTotalValue()
                                    , tokenInfo.getUsdTotalValue()
                                    , true
                                    , tokenInfo.getBalanceOfToken()
                                    , tokenInfo.getDecimals()
                            );

                            tempList.add(assetsBean);
                            daoSession.clear();
                            mDataList.clear();
                            mDataList.addAll(assetsBeanDao.queryBuilder().where(AssetsBeanDao.Properties.Id.eq(assetsBean.getId())).list());
                            if (mDataList.size() > 0) {
                                AssetsBean bean = mDataList.get(0);
                                bean.setBalanceOfToken(assetsBean.getBalanceOfToken());
                                bean.setCnyTotalValue(assetsBean.getCnyTotalValue());
                                bean.setUsdTotalValue(assetsBean.getUsdTotalValue());
                                bean.setTokenSymbolImageUrl(assetsBean.getTokenSymbolImageUrl());
                                bean.setCnyPrice(assetsBean.getCnyPrice());
                                bean.setUsdPrice(assetsBean.getUsdPrice());
                                bean.setDecimals(assetsBean.getDecimals());
                                bean.setTokenSymbol(assetsBean.getTokenSymbol());
                                bean.setId(assetsBean.getId());
                                bean.setTransfersNum(assetsBean.getTransfersNum());
                                bean.setContractType(assetsBean.getContractType());
                                bean.setTokenTotalSupply(assetsBean.getTokenTotalSupply());
                                bean.setContractAddress(assetsBean.getContractAddress());
                                bean.setContractCreater(assetsBean.getContractCreater());
                                // 更新数据库中的数据
                                assetsBeanDao.update(bean);
                            } else {
                                // 如果在数据库中不存在，则插入数据库
                                assetsBeanDao.insert(assetsBean);
                            }
                        }


                        // 同步后台配置的数据
                        daoSession.clear();
                        mDataList.clear();
                        mDataList.addAll(assetsBeanDao.queryBuilder().list());
                        for (AssetsBean bean : mDataList) {
                            for (int i = 0; i < tempList.size(); i++) {
                                if (bean.getId().equals(tempList.get(i).getId())) {
                                    // 匹配到
                                    break;
                                }

                                if (i == tempList.size() - 1) {
                                    // 未匹配到
                                    assetsBeanDao.delete(bean);
                                }

                            }

                        }

                        // 查询所有的代币数据，能否可以查到该条数据
                        daoSession.clear();
                        mDataList.clear();
                        mDataList.addAll(assetsBeanDao.queryBuilder().list());
                        for (AssetsBean bean : mDataList) {
                            if (!bean.getId().contains(DAppConstants.TYPE_HPB)) {
                                assetsBeans.add(bean);
                            }
                        }

                        onItemTopping();
                        onLastIndex();

                    }
                }
                if (CollectionUtil.isCollectionEmpty(assetsBeans)) {
                    mDefaultEmpty.setVisibility(View.VISIBLE);
                    if (mLanguage == 1)
                        mDefaultEmpty.setImageResource(R.drawable.icon_list_empty_zh);
                    else
                        mDefaultEmpty.setBackgroundResource(R.drawable.icon_list_empty_en);
                } else {
                    mDefaultEmpty.setVisibility(View.GONE);
                }

//                mPtrFrameLayout.refreshComplete();

                lRecyclerViewAdapter.notifyDataSetChanged();
                mTokenManagerRecyclerView.refreshComplete(1, assetsBeans.size());

                isLoading = false;
                dismissProgressDialog();
            }

            @Override
            public void onError(String error) {
//                mPtrFrameLayout.refreshComplete();
                mTokenManagerRecyclerView.refreshComplete(1, assetsBeans.size());
                isLoading = false;
                DappApplication.getInstance().showToast(error);
                dismissProgressDialog();
            }
        });

    }

    public void initListener() {
        mTokenManagerAdapter.setOnItemClickListener(new TokenManagerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(AssetsBean assetsBean, boolean isChecked, int currentPosition) {
                SystemLog.D("setOnItemClickListener", "onItemChecked");
                String id = assetsBean.getId();
                daoSession.clear();
                mDataList.clear();
                mDataList.addAll(assetsBeanDao.queryBuilder().where(AssetsBeanDao.Properties.Id.eq(id)).list());
                assetsBean.setSelected(isChecked);
                if (mDataList.size() == 0) {
                    assetsBeanDao.insert(assetsBean);
                } else {
                    assetsBeanDao.update(assetsBean);
                }

                // 将选中的往上移，未选中的往后面移
                if (isChecked) {
                    if (mLastIndex <= assetsBeans.size())
                        mLastIndex++;
                    onSwap(currentPosition, mLastIndex);
                } else {
                    onSwap(currentPosition, mLastIndex);
                }
            }
        });


        mTokenManagerAdapter.setOnItemLongClickListener(new TokenManagerAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(AssetsBean assetsBean) {
                currentAssets = assetsBean;
                SystemLog.D("onItemLongClick", "currentAssets = " + currentAssets);
            }
        });


        lRecyclerViewAdapter.registerAdapterDataObserver(adapterDataObserver);
    }


    RecyclerView.AdapterDataObserver adapterDataObserver = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            super.onItemRangeMoved(fromPosition, toPosition, itemCount);
            for (int i = 0; i < assetsBeans.size(); i++) {
                assetsBeans.get(i).setIds((long) (i + 1));
            }
            SystemLog.D("onItemRangeMoved", "assetsBeans = " + assetsBeans.toString());
            // 交换数据库中的位置
            assetsBeanDao.deleteAll();
            assetsBeanDao.insertOrReplaceInTx(assetsBeans, true);
            List<AssetsBean> after = assetsBeanDao.queryBuilder().list();
            SystemLog.D("onItemRangeMoved", "after = " + after.toString());

            onLastIndex();
        }
    };


    private void onSwap(int srcPosition, int targetPosition) {
        SystemLog.D("onSwap", "targetPosition = " + targetPosition);
        SystemLog.D("onSwap", "mTokenManagerRecyclerView.isComputingLayout() = " + mTokenManagerRecyclerView.isComputingLayout());
        int srcP = srcPosition - 1;
        int targetP = targetPosition - 1;
        AssetsBean remove = assetsBeans.remove(srcP);
        assetsBeans.add(targetP, remove);

        mTokenManagerRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                lRecyclerViewAdapter.notifyItemMoved(srcPosition, targetPosition);
                // 移动到指定位置
                mTokenManagerRecyclerView.scrollToPosition(targetPosition);
            }
        });
    }


    /**
     * 每次交换位置后都对上次的数据的位置做记录
     */
    private void onLastIndex() {
        mLastIndex = 0;
        for (AssetsBean bean : assetsBeans) {
            if (bean.isSelected()) {
                mLastIndex = assetsBeans.indexOf(bean) + 1;
            }
        }

        SystemLog.D("onLastIndex", "mLastIndex = " + mLastIndex);
    }

    /**
     * 将选中的条目置顶
     */
    private void onItemTopping() {
        List<AssetsBean> tempList = new ArrayList<>(assetsBeans);
        List<AssetsBean> unSelects = new ArrayList<>();
        assetsBeans.clear();
        for (AssetsBean bean : tempList) {
            if (bean.isSelected()) {
                assetsBeans.add(bean);
            } else {
                unSelects.add(bean);
            }
        }

        assetsBeans.addAll(unSelects);

        tempList.clear();
        unSelects.clear();

        SystemLog.D("onItemTopping", "size = " + assetsBeans.size());

    }

    @Override
    public void onSwiped(int adapterPosition) {
        // 侧滑删除
    }

    @Override
    public boolean onMove(int srcPosition, int targetPosition) {
        if (assetsBeans == null || assetsBeans.size() == 0) {
            return false;
        }

        SystemLog.D("onMove", "targetPosition = " + targetPosition);
        SystemLog.D("onMove", "mLastIndex = " + mLastIndex);

        if (targetPosition > mLastIndex) {
            targetPosition = mLastIndex;
        }

        if ((srcPosition >= 0 && srcPosition <= assetsBeans.size()) && (targetPosition >= 0 && targetPosition <= assetsBeans.size())) {
            if (currentAssets.isSelected()) {
                int srcP = srcPosition - 1;
                int targetP = targetPosition - 1;
                AssetsBean remove = assetsBeans.remove(srcP);
                assetsBeans.add(targetP, remove);
                lRecyclerViewAdapter.notifyItemMoved(srcPosition, targetPosition);
            }

            return false;
        }

        return false;
    }

//    @Override
//    public void refresh(PtrFrameLayout frame) {
//        if (!isLoading) {
//            initData();
//        }
//    }


    @Override
    public void onRefresh() {
        if (!isLoading) {
            SystemLog.D("onRefresh", "initData");
            initData();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        lRecyclerViewAdapter.unregisterAdapterDataObserver(adapterDataObserver);
    }
}
