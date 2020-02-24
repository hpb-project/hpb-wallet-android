package com.zhaoxi.Open_source_Android.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.base.BaseTitleBarActivity;
import com.zhaoxi.Open_source_Android.common.view.TabView;
import com.zhaoxi.Open_source_Android.ui.adapter.CommonFragmentViewpager;
import com.zhaoxi.Open_source_Android.ui.fragment.RedRecordsReceiptFragment;
import com.zhaoxi.Open_source_Android.ui.fragment.RedRecordsSendFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RedRecordsActivity extends BaseTitleBarActivity {

    @BindView(R.id.red_records_activity_tab_send)
    TabView mTabSend;
    @BindView(R.id.red_records_activity_tab_receipt)
    TabView mTabReceipt;
    @BindView(R.id.red_records_activity_viewpager)
    ViewPager mViewpager;

    private List<Fragment> mFragmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_red_records);
        ButterKnife.bind(this);
        initViews();
    }

    private void initViews() {
        setTitle(getResources().getString(R.string.activity_red_record_txt),true);

        mFragmentList = new ArrayList<Fragment>();
        initFragmentList();
    }

    private void initFragmentList() {
        mFragmentList.clear();
        String[] catList = getResources().getStringArray(R.array.import_wallet_type_tabs);

        RedRecordsSendFragment fragmentSend = new RedRecordsSendFragment();
        RedRecordsReceiptFragment fragmentReceipet = new RedRecordsReceiptFragment();


        mFragmentList.add(fragmentSend);
        mFragmentList.add(fragmentReceipet);

        CommonFragmentViewpager mViewPagerAdapter = new CommonFragmentViewpager(
                getSupportFragmentManager(), catList, mFragmentList);
        mViewpager.setAdapter(mViewPagerAdapter);

        mTabSend.setTabSelected();

        mViewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        mTabSend.setTabSelected();
                        mTabReceipt.setTabUnSelected();
                        break;
                    case 1:
                        mTabSend.setTabUnSelected();
                        mTabReceipt.setTabSelected();
                        break;
                    default:
                        mTabSend.setTabSelected();
                        mTabReceipt.setTabUnSelected();
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @OnClick({R.id.red_records_activity_tab_send, R.id.red_records_activity_tab_receipt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.red_records_activity_tab_send:
                mTabSend.setTabSelected();
                mTabReceipt.setTabUnSelected();
                mViewpager.setCurrentItem(0);
                break;
            case R.id.red_records_activity_tab_receipt:
                mTabSend.setTabUnSelected();
                mTabReceipt.setTabSelected();
                mViewpager.setCurrentItem(1);
                break;
        }
    }
}
