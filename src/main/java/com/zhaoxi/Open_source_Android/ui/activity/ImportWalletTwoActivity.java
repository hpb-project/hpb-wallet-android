package com.zhaoxi.Open_source_Android.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.base.BaseTitleBarActivity;
import com.zhaoxi.Open_source_Android.common.view.TabView;
import com.zhaoxi.Open_source_Android.ui.adapter.CommonFragmentViewpager;
import com.zhaoxi.Open_source_Android.ui.fragment.ImportColdWalletFragment;
import com.zhaoxi.Open_source_Android.ui.fragment.ImportKeystoreFragment;
import com.zhaoxi.Open_source_Android.ui.fragment.ImportMnocieFragment;
import com.zhaoxi.Open_source_Android.ui.fragment.ImportPrivateKeyFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ImportWalletTwoActivity extends BaseTitleBarActivity {
    public static final String RESOUCE_TYPE = "ImportWalletActivity.RESOUCE_TYPE";
    private static final int REQUEST_CODE = 0x222;
    private static final int REQUEST_CODE_TWO = 0x223;

    @BindView(R.id.import_wallet_two_activity_tab_mnemonic)
    TabView mTabMnemonic;
    @BindView(R.id.import_wallet_two_activity_tab_keystore)
    TabView mTabKeystore;
    @BindView(R.id.import_wallet_two_activity_tab_privatekey)
    TabView mTabPrivatekey;
    @BindView(R.id.import_wallet_two_activity_tab_clodwallet)
    TabView mTabClodwallet;
    @BindView(R.id.import_wallet_two_activity_viewpager)
    ViewPager mViewpager;

    private List<Fragment> mFragmentList;
    private int mResouceType;//0 默认导入，1 映射导入
    private int mCheckType = REQUEST_CODE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_wallet_two);
        ButterKnife.bind(this);
        mResouceType = getIntent().getIntExtra(RESOUCE_TYPE, 0);
        initViews();
    }

    private void initViews() {
        setTitle(R.string.create_wallet_btn_import,true);
        rightImgWithImgListener(R.mipmap.icon_main_qr_small_whait, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoScanQr(mCheckType);
            }
        });

        mFragmentList = new ArrayList<Fragment>();
        initFragmentList();
    }

    private void initFragmentList() {
        mFragmentList.clear();
        String[] catList = getResources().getStringArray(R.array.import_wallet_type_tabs);

        ImportMnocieFragment loandetails1 = new ImportMnocieFragment();

        Bundle bundle = new Bundle();
        bundle.putInt(RESOUCE_TYPE, mResouceType);

        ImportKeystoreFragment loandetails2 = new ImportKeystoreFragment();
        loandetails2.setArguments(bundle);

        ImportPrivateKeyFragment loandetails3 = new ImportPrivateKeyFragment();
        loandetails3.setArguments(bundle);

        ImportColdWalletFragment loandetails4 = new ImportColdWalletFragment();

        mFragmentList.add(loandetails1);
        mFragmentList.add(loandetails2);
        mFragmentList.add(loandetails3);
        mFragmentList.add(loandetails4);

        CommonFragmentViewpager mViewPagerAdapter = new CommonFragmentViewpager(
                getSupportFragmentManager(), catList, mFragmentList);
        mViewpager.setAdapter(mViewPagerAdapter);

        mTabMnemonic.setTabSelected();

        mViewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 1 || position == 3) {
                    hideOrShowRightImg(true);
                } else {
                    hideOrShowRightImg(false);
                }
                switch (position) {
                    case 0:
                        mTabMnemonic.setTabSelected();
                        mTabKeystore.setTabUnSelected();
                        mTabPrivatekey.setTabUnSelected();
                        mTabClodwallet.setTabUnSelected();
                        break;
                    case 1:
                        mTabMnemonic.setTabUnSelected();
                        mTabKeystore.setTabSelected();
                        mTabPrivatekey.setTabUnSelected();
                        mTabClodwallet.setTabUnSelected();
                        mCheckType = REQUEST_CODE;
                        break;
                    case 2:
                        mTabMnemonic.setTabUnSelected();
                        mTabKeystore.setTabUnSelected();
                        mTabPrivatekey.setTabSelected();
                        mTabClodwallet.setTabUnSelected();
                        break;
                    case 3:
                        mTabMnemonic.setTabUnSelected();
                        mTabKeystore.setTabUnSelected();
                        mTabPrivatekey.setTabUnSelected();
                        mTabClodwallet.setTabSelected();
                        mCheckType = REQUEST_CODE_TWO;
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @OnClick({R.id.import_wallet_two_activity_tab_mnemonic, R.id.import_wallet_two_activity_tab_keystore,
            R.id.import_wallet_two_activity_tab_privatekey, R.id.import_wallet_two_activity_tab_clodwallet})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.import_wallet_two_activity_tab_mnemonic:
                mTabMnemonic.setTabSelected();
                mTabKeystore.setTabUnSelected();
                mTabPrivatekey.setTabUnSelected();
                mTabClodwallet.setTabUnSelected();
                mViewpager.setCurrentItem(0);
                break;
            case R.id.import_wallet_two_activity_tab_keystore:
                hideOrShowRightImg(true);
                mTabMnemonic.setTabUnSelected();
                mTabKeystore.setTabSelected();
                mTabPrivatekey.setTabUnSelected();
                mTabClodwallet.setTabUnSelected();
                mViewpager.setCurrentItem(1);
                break;
            case R.id.import_wallet_two_activity_tab_privatekey:
                mTabMnemonic.setTabUnSelected();
                mTabKeystore.setTabUnSelected();
                mTabPrivatekey.setTabSelected();
                mTabClodwallet.setTabUnSelected();
                mViewpager.setCurrentItem(2);
                break;
            case R.id.import_wallet_two_activity_tab_clodwallet:
                mTabMnemonic.setTabUnSelected();
                mTabKeystore.setTabUnSelected();
                mTabPrivatekey.setTabUnSelected();
                mTabClodwallet.setTabSelected();
                mViewpager.setCurrentItem(3);
                break;
        }
    }

    private void gotoScanQr(int type) {
        Intent goto_scan = new Intent(this, ScaningActivity.class);
        if(type == REQUEST_CODE){
            goto_scan.putExtra(ScaningActivity.RESURE_TYPE, 10);
        }else{
            goto_scan.putExtra(ScaningActivity.RESURE_TYPE, 11);
        }

        startActivityForResult(goto_scan, type);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == ScaningActivity.RESULT_CODE && data != null) {
            String content = data.getStringExtra(ScaningActivity.RESULT_CONTENT);
            ImportKeystoreFragment fragment = (ImportKeystoreFragment) mFragmentList.get(1);
            fragment.setContent(content);
            mViewpager.setCurrentItem(1);
        }else if(requestCode == REQUEST_CODE_TWO && resultCode == ScaningActivity.RESULT_CODE && data != null){
            String content = data.getStringExtra(ScaningActivity.RESULT_CONTENT);
            ImportColdWalletFragment fragment = (ImportColdWalletFragment) mFragmentList.get(3);
            fragment.setContent(content);
            mViewpager.setCurrentItem(3);
        }
    }
}
