package com.zhaoxi.Open_source_Android.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.base.BaseTitleBarActivity;
import com.zhaoxi.Open_source_Android.common.view.tabview.TabPageIndicator;
import com.zhaoxi.Open_source_Android.common.view.tabview.UnderlinePageIndicatorEx;
import com.zhaoxi.Open_source_Android.ui.adapter.CommonFragmentViewpager;
import com.zhaoxi.Open_source_Android.ui.fragment.ImportColdWalletFragment;
import com.zhaoxi.Open_source_Android.ui.fragment.ImportKeystoreFragment;
import com.zhaoxi.Open_source_Android.ui.fragment.ImportMnocieFragment;
import com.zhaoxi.Open_source_Android.ui.fragment.ImportPrivateKeyFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 导入钱包
 *
 * @author zhutt on 2018-06-11
 */
public class ImportWalletActivity extends BaseTitleBarActivity {
    public static final String RESOUCE_TYPE = "ImportWalletActivity.RESOUCE_TYPE";
    @BindView(R.id.import_wallet_activity_tabviews)
    TabPageIndicator mTabUndicator;
    @BindView(R.id.import_wallet_activity_viewpager)
    ViewPager mViewPager;
    @BindView(R.id.import_wallet_activity_tabviews_line)
    UnderlinePageIndicatorEx mUnderLineIndicator;

    private List<Fragment> mFragmentList;
    private static final int REQUEST_CODE = 0x222;

    private int mResouceType;//0 默认导入，1 映射导入

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_wallet);
        ButterKnife.bind(this);
        mResouceType = getIntent().getIntExtra(RESOUCE_TYPE,0);
        initViews();
    }

    private void initViews() {
        setTitle(R.string.create_wallet_btn_import,true);
        rightImgWithImgListener(R.mipmap.icon_main_qr_small_whait, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoScanQr();
            }
        });

        mFragmentList = new ArrayList<Fragment>();
        initFragmentList();
    }

    private void initFragmentList() {
        mFragmentList.clear();
        String[] catList;
        if(mResouceType == 1){
            catList = getResources().getStringArray(R.array.import_wallet_type_tabs1);
            hideOrShowRightImg(true);
        }else{
            ImportMnocieFragment loandetails1 = new ImportMnocieFragment();
            mFragmentList.add(loandetails1);
            catList = getResources().getStringArray(R.array.import_wallet_type_tabs);
            hideOrShowRightImg(false);
        }
        Bundle bundle = new Bundle();
        bundle.putInt(RESOUCE_TYPE,mResouceType);

        ImportKeystoreFragment loandetails2 = new ImportKeystoreFragment();
        loandetails2.setArguments(bundle);

        ImportPrivateKeyFragment loandetails3 = new ImportPrivateKeyFragment();
        loandetails3.setArguments(bundle);

        mFragmentList.add(loandetails2);
        mFragmentList.add(loandetails3);
        if(mResouceType != 1){
            ImportColdWalletFragment loandetails4 = new ImportColdWalletFragment();
            mFragmentList.add(loandetails4);
        }
        CommonFragmentViewpager mViewPagerAdapter = new CommonFragmentViewpager(
                getSupportFragmentManager(), catList, mFragmentList);
        mViewPager.setAdapter(mViewPagerAdapter);

        mTabUndicator.setViewPager(mViewPager);
//        //下标
        mUnderLineIndicator.setViewPager(mViewPager);
        mUnderLineIndicator.setFades(false);
        //设置指示器
        mTabUndicator.setOnPageChangeListener(mUnderLineIndicator);
        mTabUndicator.setonFragmentChangeListener(new TabPageIndicator.onFragmentChangeListener() {
            @Override
            public void onFragmentChange(int position) {
                if(mResouceType == 1){
                    if(position == 0){
                        hideOrShowRightImg(true);
                    }else{
                        hideOrShowRightImg(false);
                    }
                }else{
                    if(position == 1){
                        hideOrShowRightImg(true);
                    }else{
                        hideOrShowRightImg(false);
                    }
                }
            }
        });
        mViewPager.setOffscreenPageLimit(1);
    }

    private void gotoScanQr() {
        Intent goto_scan = new Intent(this, ScaningActivity.class);
        goto_scan.putExtra(ScaningActivity.RESURE_TYPE, 1);
        startActivityForResult(goto_scan,REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == ScaningActivity.RESULT_CODE && data != null) {
            String content = data.getStringExtra(ScaningActivity.RESULT_CONTENT);
            if(mResouceType == 1){
                ImportKeystoreFragment fragment = (ImportKeystoreFragment)mFragmentList.get(0);
                fragment.setContent(content);
                mViewPager.setCurrentItem(0);
            }else{
                ImportKeystoreFragment fragment = (ImportKeystoreFragment)mFragmentList.get(1);
                fragment.setContent(content);
                mViewPager.setCurrentItem(1);
            }
        }
    }
}
