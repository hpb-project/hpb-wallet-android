package com.zhaoxi.Open_source_Android.ui.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.gyf.immersionbar.ImmersionBar;
import com.zhaoxi.Open_source_Android.DAppConstants;
import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.base.BaseTitleBarActivity;
import com.zhaoxi.Open_source_Android.common.base.DappApplication;
import com.zhaoxi.Open_source_Android.common.view.refresh.MyPtrFrameLayout;
import com.zhaoxi.Open_source_Android.db.CreateDbWallet;
import com.zhaoxi.Open_source_Android.libs.tools.SystemLog;
import com.zhaoxi.Open_source_Android.libs.utils.ChangeLanguageUtil;
import com.zhaoxi.Open_source_Android.libs.utils.CollectionUtil;
import com.zhaoxi.Open_source_Android.libs.utils.SharedPreferencesUtil;
import com.zhaoxi.Open_source_Android.net.BaseBet.NetResultCallBack;
import com.zhaoxi.Open_source_Android.net.Request.TokenIdDetailRequest;
import com.zhaoxi.Open_source_Android.net.bean.StockDetailBean;
import com.zhaoxi.Open_source_Android.net.bean.TokenIdDetailBean;
import com.zhaoxi.Open_source_Android.net.bean.TranferRecordBean;
import com.zhaoxi.Open_source_Android.ui.adapter.TokenIdRecordAdapter;
import com.zhaoxi.Open_source_Android.web3.bean.WalletBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import in.srain.cube.views.ptr.PtrFrameLayout;

public class TokenIdDetailActivity extends BaseTitleBarActivity implements MyPtrFrameLayout.OnRefreshListener {

    public static final String INVENTORY_BEAN = "TokenIdDetailActivity.inventoryBean";
    public static final String INVENTORY_TOKEN_SYMBOL = "TokenIdDetailActivity.tokenSymbol";
    public static final String INVENTORY_CONTRACT_ADDRESS = "TokenIdDetailActivity.contractAddress";

    @BindView(R.id.recycler_with_refresh_ptr_frame_layout)
    MyPtrFrameLayout mPtrFrameLayout;
    @BindView(R.id.last_ten_transfer_recycler_view)
    RecyclerView mLastTenTransferRecyclerView;

    private TokenIdRecordAdapter tokenIdRecordAdapter;
    private int mLanguage;
    private WalletBean mWalletBean;

    private boolean isLoading = false;
    private boolean isFirstLoad = true;

    private List<TokenIdDetailBean.TokenIdInfo> tokenIdInfos = new ArrayList<>();
    private StockDetailBean.TokenInfo tokenInfo;
    private String mContractAddress;
    private String mTokenSymbol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_token_id_detail);
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
        mTokenSymbol = getIntent().getStringExtra(INVENTORY_TOKEN_SYMBOL);
        mContractAddress = getIntent().getStringExtra(INVENTORY_CONTRACT_ADDRESS);
        setTitle(mTokenSymbol +" "+ getResources().getString(R.string.activity_token_detail_txt_1), true);
        mLanguage = ChangeLanguageUtil.languageType(this);
        mPtrFrameLayout.setLastUpdateTimeRelateObject(this);
        mPtrFrameLayout.setUltraPullToRefresh(this, mLastTenTransferRecyclerView);
        mPtrFrameLayout.changeWhiteBackgroud();
        tokenInfo = (StockDetailBean.TokenInfo) getIntent().getSerializableExtra(INVENTORY_BEAN);
        CreateDbWallet mCreateDbWallet = new CreateDbWallet(this);
        String walletAddress = SharedPreferencesUtil.getSharePreString(this, SharedPreferencesUtil.CHOOSE_WALLET_ADDRESS);
        mWalletBean = mCreateDbWallet.queryWallet(this, walletAddress);

        LinearLayoutManager lm = new LinearLayoutManager(this);
        mLastTenTransferRecyclerView.setLayoutManager(lm);

        tokenIdRecordAdapter = new TokenIdRecordAdapter(this, tokenInfo, tokenIdInfos);
        mLastTenTransferRecyclerView.setAdapter(tokenIdRecordAdapter);
        // 取消RecyclerView的动画效果
        ((SimpleItemAnimator) mLastTenTransferRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
    }

    private void initData() {
        if (!isLoading) {
            isLoading = true;
            if (isFirstLoad) {
                showProgressDialog();
            }
        }

        new TokenIdDetailRequest(1
                , tokenInfo.getTokenId()
                , mContractAddress).doRequest(this, new NetResultCallBack() {
            @Override
            public void onSuccess(JSONArray jsonArray) {
                if (jsonArray.get(2) != null) {
                    tokenIdInfos.clear();
                    mLastTenTransferRecyclerView.smoothScrollToPosition(0);
                    TokenIdDetailBean tokenIdDetailBean = JSON.parseObject(jsonArray.get(2).toString(), TokenIdDetailBean.class);
                    List<TokenIdDetailBean.TokenIdInfo> subList = tokenIdDetailBean.getList();
                    if (!CollectionUtil.isCollectionEmpty(subList)) {
                        if (subList.size() >= 10) {
                            tokenIdInfos.addAll(subList.subList(0, 10));
                        } else {
                            tokenIdInfos.addAll(subList.subList(0, subList.size()));
                        }
                    }

                    SystemLog.D("len", "size = " + tokenIdInfos.size());
                    mLastTenTransferRecyclerView.smoothScrollToPosition(0);
                    tokenIdRecordAdapter.notifyDataSetChanged();
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


    private void initListener() {
        tokenIdRecordAdapter.setOnItemClickListener(new TokenIdRecordAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(TokenIdDetailBean.TokenIdInfo tokenIdInfo) {
                TranferRecordBean.TransferInfo transferInfo = new TranferRecordBean.TransferInfo();
                transferInfo.setTTimestap(tokenIdInfo.getBlockTimestamp());
                transferInfo.setFromAccount(tokenIdInfo.getFromAddress());
                transferInfo.setToAccount(tokenIdInfo.getToAddress());
                transferInfo.setBlockNumber(tokenIdInfo.getBlockNumber()+"");
                transferInfo.setContractAddress(tokenIdInfo.getContractAddress());
                transferInfo.setTokenId(tokenIdInfo.getTokenId()+"");
                transferInfo.setTransactionHash(tokenIdInfo.getTxHash());
                transferInfo.setBlockHash(tokenIdInfo.getBlockHash());
                transferInfo.setTokenSymbol(mTokenSymbol);
                Intent data = new Intent(TokenIdDetailActivity.this, TransferRecodeDetailsActivity.class);
                data.putExtra(TransferRecodeDetailsActivity.TRANSFER_RECODE_DETAILS, transferInfo);
                data.putExtra(TransferRecodeDetailsActivity.TRANSFER_RECODE_TYPE, DAppConstants.TYPE_HRC_721);
                startActivity(data);
            }

            @Override
            public void onCollection() {
                // 收款
                startActivity(new Intent(TokenIdDetailActivity.this, ReceivablesActivity.class));
            }


            @Override
            public void onTransfer() {
                // 转账
                if (mWalletBean.getIsClodWallet() == 0) {// 判断是否是冷钱包
                    Intent goto_transfer = new Intent(TokenIdDetailActivity.this, TransferActivity.class);
                    goto_transfer.putExtra(TransferActivity.ADDRESS, "");
                    goto_transfer.putExtra(TransferActivity.TRANSFER_TYPE, getResources().getString(R.string.hrc_721));
                    goto_transfer.putExtra(TransferActivity.TOKEN_SYMBOL, mTokenSymbol);
                    startActivity(goto_transfer);
                } else {
                    startActivity(new Intent(TokenIdDetailActivity.this, ColdTransferActivity.class));
                }
            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onCatchHeader(View view, String url) {
                share((CircleImageView) view, url);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void share(CircleImageView iv, String url) {
        Intent intent = new Intent(this, ImgCatchActivity.class);
        intent.putExtra(ImgCatchActivity.IMG_URL, url);
        Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(this, iv, "share").toBundle();
        startActivity(intent, bundle);
    }


    @Override
    public void refresh(PtrFrameLayout frame) {
        if (!isLoading) {
            initData();
        }
    }

}
