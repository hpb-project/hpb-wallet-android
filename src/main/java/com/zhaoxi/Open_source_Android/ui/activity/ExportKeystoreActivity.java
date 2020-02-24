package com.zhaoxi.Open_source_Android.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.base.BaseTitleBarActivity;
import com.zhaoxi.Open_source_Android.common.dialog.CommonWarnDialog;
import com.zhaoxi.Open_source_Android.common.view.tabview.TabPageIndicator;
import com.zhaoxi.Open_source_Android.common.view.tabview.UnderlinePageIndicatorEx;
import com.zhaoxi.Open_source_Android.libs.utils.StrUtil;
import com.zhaoxi.Open_source_Android.ui.adapter.CommonFragmentViewpager;
import com.zhaoxi.Open_source_Android.ui.fragment.ExportKeystoreFileFragment;
import com.zhaoxi.Open_source_Android.ui.fragment.ExportKeystoreQrFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 导出keysotre
 *
 * @author zhutt on 2018-06-11
 */
public class ExportKeystoreActivity extends BaseTitleBarActivity {
    public static final String EXPORT_WALLET_KEYSTORE = "ExportKeystoreActivity.EXPORT_WALLET_KEYSTORE";

    @BindView(R.id.export_keystore_activity_tabviews)
    TabPageIndicator mTabUndicator;
    @BindView(R.id.export_keystore_activity_tabviews_line)
    UnderlinePageIndicatorEx mUnderLineIndicator;
    @BindView(R.id.export_keystore_activity_viewpager)
    ViewPager mViewPager;

    private List<Fragment> mFragmentList;
    private String keystore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export_keystore);
        ButterKnife.bind(this);
        keystore = getIntent().getStringExtra(WalletDetailsActivity.WALLET_RESULT);
        if(StrUtil.isEmpty(keystore)){
            finish();
        }
        setTitle(R.string.wallet_manager_details_txt_export_keystore,true);
        mFragmentList = new ArrayList<>();
        initFragmentList();
        //显示弹出框
        showWarnDialog();
    }

    private void initFragmentList() {
        mFragmentList.clear();
        ExportKeystoreFileFragment loandetails1 = new ExportKeystoreFileFragment();
        Bundle bundle1 = new Bundle();
        bundle1.putString(EXPORT_WALLET_KEYSTORE,keystore);
        loandetails1.setArguments(bundle1);

        ExportKeystoreQrFragment loandetails2 = new ExportKeystoreQrFragment();
        Bundle bundle2 = new Bundle();
        bundle2.putString(EXPORT_WALLET_KEYSTORE,keystore);
        loandetails2.setArguments(bundle2);

        mFragmentList.add(loandetails1);
        mFragmentList.add(loandetails2);


        CommonFragmentViewpager mViewPagerAdapter = new CommonFragmentViewpager(
                getSupportFragmentManager(), getResources().getStringArray(
                R.array.export_keystore_type_tabs), mFragmentList);
        mViewPager.setAdapter(mViewPagerAdapter);

        mTabUndicator.setViewPager(mViewPager);
        //下标
        mUnderLineIndicator.setViewPager(mViewPager);
        mUnderLineIndicator.setFades(false);
        //设置指示器
        mTabUndicator.setOnPageChangeListener(mUnderLineIndicator);
        mViewPager.setOffscreenPageLimit(3);
    }

    private void showWarnDialog(){
        CommonWarnDialog dialog = new CommonWarnDialog.Builder(this).create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
    }
}
