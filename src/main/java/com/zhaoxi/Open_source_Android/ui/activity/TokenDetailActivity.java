package com.zhaoxi.Open_source_Android.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.bumptech.glide.Glide;
import com.gyf.immersionbar.ImmersionBar;
import com.zhaoxi.Open_source_Android.DAppConstants;
import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.base.BaseTitleBarActivity;
import com.zhaoxi.Open_source_Android.common.base.DappApplication;
import com.zhaoxi.Open_source_Android.common.view.recycler.LoadMoreRecyclerAdapter;
import com.zhaoxi.Open_source_Android.common.view.recycler.LoadMoreRecyclerView;
import com.zhaoxi.Open_source_Android.common.view.refresh.MyPtrFrameLayout;
import com.zhaoxi.Open_source_Android.db.CreateDbWallet;
import com.zhaoxi.Open_source_Android.libs.utils.ChangeLanguageUtil;
import com.zhaoxi.Open_source_Android.libs.utils.CollectionUtil;
import com.zhaoxi.Open_source_Android.libs.utils.SharedPreferencesUtil;
import com.zhaoxi.Open_source_Android.libs.utils.SlinUtil;
import com.zhaoxi.Open_source_Android.libs.utils.StrUtil;
import com.zhaoxi.Open_source_Android.net.BaseBet.NetResultCallBack;
import com.zhaoxi.Open_source_Android.net.Request.TransactionListRequest;
import com.zhaoxi.Open_source_Android.net.bean.AssetsBean;
import com.zhaoxi.Open_source_Android.net.bean.TranferRecordBean;
import com.zhaoxi.Open_source_Android.ui.adapter.TokenDetailAdapter;
import com.zhaoxi.Open_source_Android.web3.bean.WalletBean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import in.srain.cube.views.ptr.PtrFrameLayout;

public class TokenDetailActivity extends BaseTitleBarActivity implements MyPtrFrameLayout.OnRefreshListener, LoadMoreRecyclerView.OnLoadMoreListener {

    public static final String ITEM_ASSETS = "com.zhaoxi.Open_source_Android.dapp.ITEM_ASSETS";

    @BindView(R.id.recycler_with_refresh_ptr_frame_layout)
    MyPtrFrameLayout mPtrFrameLayout;
    @BindView(R.id.token_detail_list_recycler_view)
    LoadMoreRecyclerView mTokenDetailRecyclerView;
    @BindView(R.id.iv_token_detail_img)
    ImageView mIvTokenDetailImg;
    @BindView(R.id.iv_token_detail_header)
    CircleImageView mIvTokenDetailHeader;
    @BindView(R.id.ll_token_detail_collection)
    LinearLayout mTokenCollection;
    @BindView(R.id.ll_token_detail_transfer)
    LinearLayout mTokenDetailTransfer;
    @BindView(R.id.tv_token_detail_assets)
    TextView mTokenDetailAssets;
    @BindView(R.id.tv_token_detail_name)
    TextView mTokenDetailName;
    @BindView(R.id.item_main_assets_list_token_value)
    TextView mTokenValue;
    @BindView(R.id.iv_default_empty)
    ImageView mDefaultEmpty;
    @BindView(R.id.tv_token_detail_assets_unit)
    TextView mAssetsUnit;
    @BindView(R.id.tv_token_title)
    TextView mTokenTitle;

    private TokenDetailAdapter mTokenDetailAdapter;
    private LoadMoreRecyclerAdapter loadMoreRecyclerAdapter;
    private List<TranferRecordBean.TransferInfo> mDataList = new ArrayList<>();

    private boolean isLoading = false;
    private int mCurrentPage = 1;
    private int mTotalPages;
    //判断是否是首次加载，控制转圈进度条只在第一次加载时显示，在下拉刷新时不显示
    private boolean isFirstLoad = true;
    private int mLanguage;
    public static String mWalletAddress = "";
    private WalletBean mWalletBean;
    private String mContractAddress;
    private String mContractType;
    private String mTokenSymbol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_token_detail);
        ButterKnife.bind(this);
        initView();
        initData();
        initListener();
        mPtrFrameLayout.autoRefresh();
    }


    private void initView() {
        ImmersionBar.with(this)
                .statusBarDarkFont(true, 0.2f)
                .init();
        setTitleBgColor(R.color.white, true);
        setTitle(getResources().getString(R.string.activity_token_detail_txt_1), true);
        mLanguage = ChangeLanguageUtil.languageType(this);
        mWalletAddress = SharedPreferencesUtil.getSharePreString(this, SharedPreferencesUtil.CHOOSE_WALLET_ADDRESS);
        CreateDbWallet mCreateDbWallet = new CreateDbWallet(this);
        mWalletBean = mCreateDbWallet.queryWallet(this, mWalletAddress);
        mPtrFrameLayout.setLastUpdateTimeRelateObject(this);
        mPtrFrameLayout.setUltraPullToRefresh(this, mTokenDetailRecyclerView);
        mPtrFrameLayout.changeWhiteBackgroud();
        LinearLayoutManager lm = new LinearLayoutManager(this);
        mTokenDetailRecyclerView.setLayoutManager(lm);

        mTokenDetailAdapter = new TokenDetailAdapter(this, mDataList);
        loadMoreRecyclerAdapter = new LoadMoreRecyclerAdapter(this, mTokenDetailAdapter, false);
        mTokenDetailRecyclerView.setAdapter(loadMoreRecyclerAdapter);
        // 设置加载更多监听
        mTokenDetailRecyclerView.setListener(this);
    }

    private void initData() {
        AssetsBean assetsBean = (AssetsBean) getIntent().getSerializableExtra(ITEM_ASSETS);
        String mCurrentAssetsUnit;
        String cnyTotalValue = assetsBean.getCnyTotalValue();
        String usdTotalValue = assetsBean.getUsdTotalValue();
        BigDecimal currentMoney;
        // 当前金额折合单位
        int currentChoseUnit = SharedPreferencesUtil.getSharePreInt(DappApplication.getInstance().getApplicationContext(), SharedPreferencesUtil.CHANGE_COIN_UNIT);
        if (currentChoseUnit == 0) {
            int language = SharedPreferencesUtil.getSharePreInt(DappApplication.getInstance().getApplicationContext(), SharedPreferencesUtil.CHANGE_LANGUAGE_NAME);
            if (language == 0) {
                String sysLanguage = Locale.getDefault().getLanguage();
                if ("zh".equals(sysLanguage)) {
                    mCurrentAssetsUnit = DappApplication.getInstance().getApplicationContext().getResources().getString(R.string.activity_rmb_value);
                    currentMoney = new BigDecimal(cnyTotalValue);
                } else {
                    mCurrentAssetsUnit = DappApplication.getInstance().getApplicationContext().getResources().getString(R.string.activity_dollar_value);
                    currentMoney = new BigDecimal(usdTotalValue);
                }
            } else {
                if (language == ChangeLanguageUtil.CHANGE_LANGUAGE_CHINA) {
                    mCurrentAssetsUnit = DappApplication.getInstance().getApplicationContext().getResources().getString(R.string.activity_rmb_value);
                    currentMoney = new BigDecimal(cnyTotalValue);
                } else {
                    mCurrentAssetsUnit = DappApplication.getInstance().getApplicationContext().getResources().getString(R.string.activity_dollar_value);
                    currentMoney = new BigDecimal(usdTotalValue);
                }
            }
        } else {
            if (currentChoseUnit == 1) {// 人民币
                mCurrentAssetsUnit = DappApplication.getInstance().getApplicationContext().getResources().getString(R.string.activity_rmb_value);
                currentMoney = new BigDecimal(cnyTotalValue);
            } else {// 美元
                mCurrentAssetsUnit = DappApplication.getInstance().getApplicationContext().getResources().getString(R.string.activity_dollar_value);
                currentMoney = new BigDecimal(usdTotalValue);
            }

        }
        mAssetsUnit.setText(mCurrentAssetsUnit);
        int decimals = assetsBean.getDecimals();
        mTokenDetailAssets.setText(SlinUtil.formatNumFromWeiT(this, new BigDecimal(assetsBean.getBalanceOfToken()), decimals));
        setTextSize(mTokenDetailAssets.getText().toString(), mTokenDetailAssets, mTokenDetailName);
        mTokenDetailName.setText(assetsBean.getTokenSymbol());
        mTokenValue.setText(SlinUtil.NumberFormat2(this, currentMoney));
        mContractAddress = assetsBean.getContractAddress();
        mContractType = assetsBean.getContractType();
        mTokenSymbol = assetsBean.getTokenSymbol();
        switch (mContractType) {
            case DAppConstants.TYPE_HRC_20:
                mIvTokenDetailImg.setImageResource(R.mipmap.icon_hrc_20_b);
                mTokenTitle.setText(getResources().getString(R.string.activity_transaction_record_title_02));
                break;
            case DAppConstants.TYPE_HPB:
                mTokenTitle.setText(getResources().getString(R.string.main_me_manager_txt_transfer));
                mIvTokenDetailImg.setVisibility(View.GONE);
                break;
        }

        if (StrUtil.isNull(assetsBean.getCnyPrice())) {
            mTokenValue.setVisibility(View.GONE);
            mAssetsUnit.setVisibility(View.GONE);
        } else {
            mTokenValue.setVisibility(View.VISIBLE);
            mAssetsUnit.setVisibility(View.VISIBLE);
        }

        String tokenSymbolImageUrl = assetsBean.getTokenSymbolImageUrl();
        if (!TextUtils.isEmpty(tokenSymbolImageUrl)) {
            Glide.with(this)
                    .load(tokenSymbolImageUrl)
                    .centerCrop()
                    .placeholder(R.mipmap.icon_default_header)
                    .error(R.mipmap.icon_default_header)
                    .into(mIvTokenDetailHeader);
        }
        setTitle(assetsBean.getTokenSymbol() + " " + getResources().getString(R.string.activity_token_detail_txt_1), true);
    }

    public void initListener() {
        mTokenDetailAdapter.setOnItemClickListener(new TokenDetailAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(TranferRecordBean.TransferInfo entity) {
                Intent it_details = new Intent(TokenDetailActivity.this, TransferRecodeDetailsActivity.class);
                it_details.putExtra(TransferRecodeDetailsActivity.TRANSFER_RECODE_ADDRESS, mWalletAddress);
                it_details.putExtra(TransferRecodeDetailsActivity.TRANSFER_RECODE_DETAILS, entity);
                it_details.putExtra(TransferRecodeDetailsActivity.TRANSFER_RECODE_TYPE, mContractType);
                startActivity(it_details);
            }
        });
    }

    @OnClick({R.id.ll_token_detail_collection, R.id.ll_token_detail_transfer})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_token_detail_collection:
                startActivity(new Intent(this, ReceivablesActivity.class));
                break;
            case R.id.ll_token_detail_transfer:
                // 转账
                if (mWalletBean.getIsClodWallet() == 0) {// 判断是否是冷钱包
                    Intent goto_transfer = new Intent(this, TransferActivity.class);
                    goto_transfer.putExtra(TransferActivity.TRANSFER_TYPE, mContractType);
                    goto_transfer.putExtra(TransferActivity.ADDRESS, "");
                    goto_transfer.putExtra(TransferActivity.TOKEN_SYMBOL, mTokenSymbol);
                    startActivity(goto_transfer);
                } else {
                    startActivity(new Intent(this, ColdTransferActivity.class));
                }
                break;
        }
    }

    private void initData(int pageNum) {
        if (!isLoading) {
            isLoading = true;
            if (isFirstLoad) {
                isFirstLoad = false;
                showProgressDialog();
            }
        }
        new TransactionListRequest(mWalletAddress
                , mContractType
                , mContractAddress
                , mTokenSymbol
                , pageNum
                , "0").doRequest(this, new NetResultCallBack() {
            @Override
            public void onSuccess(JSONArray jsonArray) {
                if (jsonArray.get(2) != null) {
                    TranferRecordBean transRecordBean = JSON.parseObject(jsonArray.get(2).toString(), TranferRecordBean.class);
                    if (CollectionUtil.isCollectionEmpty(transRecordBean.getList())) {
                        loadMoreRecyclerAdapter.setIsNeedLoadMore(false);
                        loadMoreRecyclerAdapter.notifyDataSetChanged();
                    } else {
                        if (pageNum == 1) {
                            mDataList.clear();
                            mTokenDetailRecyclerView.setSmoothPosition(0);
                        }
                        mTotalPages = transRecordBean.getPages();
                        ++mCurrentPage;
                        mDataList.addAll(transRecordBean.getList());
                        loadMoreRecyclerAdapter.setIsNeedLoadMore(mCurrentPage < mTotalPages);// 设置是否显示加载更多Item
                        loadMoreRecyclerAdapter.notifyDataSetChanged();
                    }
                }

                if (CollectionUtil.isCollectionEmpty(mDataList)) {
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
