package com.zhaoxi.Open_source_Android.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gyf.immersionbar.ImmersionBar;
import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.base.BaseTitleBarActivity;
import com.zhaoxi.Open_source_Android.common.base.DappApplication;
import com.zhaoxi.Open_source_Android.db.CreateDbWallet;
import com.zhaoxi.Open_source_Android.libs.utils.ChangeLanguageUtil;
import com.zhaoxi.Open_source_Android.libs.utils.SharedPreferencesUtil;
import com.zhaoxi.Open_source_Android.libs.utils.SlinUtil;
import com.zhaoxi.Open_source_Android.libs.utils.StrUtil;
import com.zhaoxi.Open_source_Android.net.bean.AssetsBean;
import com.zhaoxi.Open_source_Android.ui.adapter.Token721ViewPagerAdapter;
import com.zhaoxi.Open_source_Android.ui.fragment.FragmentInventory721;
import com.zhaoxi.Open_source_Android.ui.fragment.FragmentTransRecord721;
import com.zhaoxi.Open_source_Android.web3.bean.WalletBean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class TokenDetail721Activity extends BaseTitleBarActivity {

    public static final String ITEM_ASSETS = "com.zhaoxi.Open_source_Android.dapp.ITEM_ASSETS";

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
    @BindView(R.id.activity_token_detail_assets_container)
    LinearLayout mTokenAssetContainer;
    @BindView(R.id.tv_token_detail_assets_unit)
    TextView mAssetsUnit;
    @BindView(android.R.id.tabhost)
    TabHost mTabHost;
    @BindView(android.R.id.tabcontent)
    FrameLayout mTabContent;
    @BindView(R.id.viewpager)
    ViewPager mViewPager;

    private static final String[] titles = new String[2];
    private static final int[] ID = {R.id.tab1, R.id.tab2};

    public static String mWalletAddress;
    private WalletBean mWalletBean;
    public String mTokenSymbol;
    public String mContractAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_token_detail_721);
        ButterKnife.bind(this);
        initView();
        initData();
    }


    private void initView() {
        ImmersionBar.with(this)
                .statusBarDarkFont(true, 0.2f)
                .init();
        setTitleBgColor(R.color.white, true);
        titles[0] = getResources().getString(R.string.activity_transaction_record_title_01);
        titles[1] = getResources().getString(R.string.activity_transaction_record_title_02);
        mIvTokenDetailImg.setImageResource(R.mipmap.icon_hrc_721_b);
        mWalletAddress = SharedPreferencesUtil.getSharePreString(this, SharedPreferencesUtil.CHOOSE_WALLET_ADDRESS);
        CreateDbWallet createDbWallet = new CreateDbWallet(this);
        mWalletBean = createDbWallet.queryWallet(this, mWalletAddress);
        mTabHost.setup();
        for (int i = 0; i < titles.length; i++) {
            View view = LayoutInflater.from(this).inflate(R.layout.token_tab_title, null, false);
            final TextView tvTokenTabTitle = view.findViewById(R.id.tv_token_tab_title);
            tvTokenTabTitle.setText(titles[i]);
            mTabHost.addTab(mTabHost.newTabSpec("tab" + i).setContent(ID[i]).setIndicator(view));
        }

        List<Fragment> fragments = new ArrayList<>();
        Fragment fragmentInventory721 = new FragmentInventory721();
        fragments.add(fragmentInventory721);
        Fragment fragmentTransRecord721 = new FragmentTransRecord721();
        fragments.add(fragmentTransRecord721);
        mViewPager.setAdapter(new Token721ViewPagerAdapter(getSupportFragmentManager(), Arrays.asList(titles), fragments));
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                mTabHost.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                int position = mTabHost.getCurrentTab();
                mViewPager.setCurrentItem(position, true);
                for (int i = 0; i < mTabHost.getTabWidget().getChildCount(); i++) {
                    TextView tvTitle = mTabHost.getTabWidget().getChildAt(i).findViewById(R.id.tv_token_tab_title);
                    View viewLine = mTabHost.getTabWidget().getChildAt(i).findViewById(R.id.token_tab_underline);
                    if (mTabHost.getCurrentTab() == i) {
                        tvTitle.setTextColor(getResources().getColor(R.color.color_black_333));
                        viewLine.setVisibility(View.VISIBLE);
                    } else {
                        tvTitle.setTextColor(getResources().getColor(R.color.color_BABDC1));
                        viewLine.setVisibility(View.GONE);
                    }
                }
            }
        });

        TextView tvTitle = mTabHost.getTabWidget().getChildAt(0).findViewById(R.id.tv_token_tab_title);
        View tokenUnderline = mTabHost.getTabWidget().getChildAt(0).findViewById(R.id.token_tab_underline);
        tvTitle.setTextColor(getResources().getColor(R.color.color_black_333));
        tokenUnderline.setVisibility(View.VISIBLE);
        mTabHost.setCurrentTab(0);
        mViewPager.setCurrentItem(0);

    }

    @OnClick({R.id.ll_token_detail_collection, R.id.ll_token_detail_transfer})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_token_detail_collection:
                // 收款
                startActivity(new Intent(this, ReceivablesActivity.class));
                break;
            case R.id.ll_token_detail_transfer:
                // 转账
                if (mWalletBean.getIsClodWallet() == 0) {// 判断是否是冷钱包
                    Intent goto_transfer = new Intent(this, TransferActivity.class);
                    goto_transfer.putExtra(TransferActivity.TRANSFER_TYPE, getResources().getString(R.string.hrc_721));
                    goto_transfer.putExtra(TransferActivity.TOKEN_SYMBOL, mTokenSymbol);
                    goto_transfer.putExtra(TransferActivity.ADDRESS, "");
                    startActivity(goto_transfer);
                } else {
                    //冷钱包
                    startActivity(new Intent(this, ColdTransferActivity.class));
                }
                break;
        }
    }

    private void initData() {
        AssetsBean assetsBean = (AssetsBean) getIntent().getSerializableExtra(ITEM_ASSETS);
        int decimals = assetsBean.getDecimals();
        mTokenDetailAssets.setText(SlinUtil.formatNumFromWeiT(this, new BigDecimal(assetsBean.getBalanceOfToken()),decimals));
        mTokenDetailName.setText(assetsBean.getTokenSymbol());
        setTextSize(mTokenDetailAssets.getText().toString(),mTokenDetailAssets,mTokenDetailName);
        String tokenSymbolImageUrl = assetsBean.getTokenSymbolImageUrl();
        if (!TextUtils.isEmpty(tokenSymbolImageUrl)) {
            Glide.with(this)
                    .load(tokenSymbolImageUrl)
                    .centerCrop()
                    .placeholder(R.mipmap.icon_default_header)
                    .error(R.mipmap.icon_default_header)
                    .into(mIvTokenDetailHeader);
        }

        mTokenSymbol = assetsBean.getTokenSymbol();
        mContractAddress = assetsBean.getContractAddress();
        String cnyTotalValue = assetsBean.getCnyTotalValue();
        String usdTotalValue = assetsBean.getUsdTotalValue();
        // 当前金额
        BigDecimal currentMoney;
        // 当前金额折合单位
        int currentChoseUnit = SharedPreferencesUtil.getSharePreInt(DappApplication.getInstance().getApplicationContext(), SharedPreferencesUtil.CHANGE_COIN_UNIT);
        String currentAssetsUnit;
        if (currentChoseUnit == 0) {
            int language = SharedPreferencesUtil.getSharePreInt(DappApplication.getInstance().getApplicationContext(), SharedPreferencesUtil.CHANGE_LANGUAGE_NAME);
            if (language == 0) {
                String sysLanguage = Locale.getDefault().getLanguage();
                if ("zh".equals(sysLanguage)) {
                    currentAssetsUnit = DappApplication.getInstance().getApplicationContext().getResources().getString(R.string.activity_rmb_value);
                    currentMoney = new BigDecimal(cnyTotalValue);
                } else {
                    currentAssetsUnit = DappApplication.getInstance().getApplicationContext().getResources().getString(R.string.activity_dollar_value);
                    currentMoney = new BigDecimal(usdTotalValue);
                }
            } else {
                if (language == ChangeLanguageUtil.CHANGE_LANGUAGE_CHINA) {
                    currentAssetsUnit = DappApplication.getInstance().getApplicationContext().getResources().getString(R.string.activity_rmb_value);
                    currentMoney = new BigDecimal(cnyTotalValue);
                } else {
                    currentAssetsUnit = DappApplication.getInstance().getApplicationContext().getResources().getString(R.string.activity_dollar_value);
                    currentMoney = new BigDecimal(usdTotalValue);
                }
            }
        } else {
            if (currentChoseUnit == 1) {// 人民币
                currentAssetsUnit = DappApplication.getInstance().getApplicationContext().getResources().getString(R.string.activity_rmb_value);
                currentMoney = new BigDecimal(cnyTotalValue);
            } else {// 美元
                currentAssetsUnit = DappApplication.getInstance().getApplicationContext().getResources().getString(R.string.activity_dollar_value);
                currentMoney = new BigDecimal(usdTotalValue);
            }

        }
        mTokenValue.setText(SlinUtil.NumberFormat2(this, currentMoney));
        mAssetsUnit.setText(currentAssetsUnit);

        if (StrUtil.isNull(assetsBean.getCnyPrice())){
            mTokenValue.setVisibility(View.GONE);
            mAssetsUnit.setVisibility(View.GONE);
        }else {
            mTokenValue.setVisibility(View.VISIBLE);
            mAssetsUnit.setVisibility(View.VISIBLE);
        }

        setTitle(mTokenSymbol + " "+ getResources().getString(R.string.activity_token_detail_txt_1), true);
    }

}
