package com.zhaoxi.Open_source_Android.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.View;
import android.widget.ImageView;

import com.gyf.immersionbar.ImmersionBar;
import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.base.BaseTitleBarActivity;
import com.zhaoxi.Open_source_Android.common.base.DappApplication;
import com.zhaoxi.Open_source_Android.common.bean.DigitalSignBean;
import com.zhaoxi.Open_source_Android.common.view.recycler.LoadMoreRecyclerAdapter;
import com.zhaoxi.Open_source_Android.common.view.recycler.LoadMoreRecyclerView;
import com.zhaoxi.Open_source_Android.common.view.refresh.MyPtrFrameLayout;
import com.zhaoxi.Open_source_Android.db.greendao.DigitalSignBeanDao;
import com.zhaoxi.Open_source_Android.libs.utils.ChangeLanguageUtil;
import com.zhaoxi.Open_source_Android.net.bean.DigitalSignRecordBean;
import com.zhaoxi.Open_source_Android.ui.adapter.DigitalSignRecordAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrFrameLayout;

public class DigitalSignRecordActivity extends BaseTitleBarActivity implements MyPtrFrameLayout.OnRefreshListener {

    @BindView(R.id.recycler_with_refresh_ptr_frame_layout)
    MyPtrFrameLayout mPtrFrameLayout;
    @BindView(R.id.token_more_list_recycler_view)
    LoadMoreRecyclerView mTokenMoreRecyclerView;
    @BindView(R.id.iv_default_empty)
    ImageView mDefaultEmpty;

    private LoadMoreRecyclerAdapter loadMoreRecyclerAdapter;

    private int mTotalPages;
    private int mCurrentPage;
    private boolean isLoading;
    private boolean isFirstLoad = true;
    private int mLanguage;

    private List<DigitalSignRecordBean> digitalSignRecordBeans = new ArrayList<>();
    private List<DigitalSignBean> digitalSignBeans = new ArrayList<>();
    private DigitalSignBeanDao mDigitalSignBeanDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_digital_sign_record);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        ImmersionBar.with(this)
                .statusBarDarkFont(true, 0.2f)
                .init();
        setTitleBgColor(R.color.white, true);
        setTitle(getResources().getString(R.string.digital_sign_more_sign_record_txt), true);
        mLanguage = ChangeLanguageUtil.languageType(this);
        mPtrFrameLayout.setLastUpdateTimeRelateObject(this);
        mPtrFrameLayout.setUltraPullToRefresh(this, mTokenMoreRecyclerView);
        mPtrFrameLayout.changeWhiteBackgroud();
        mDefaultEmpty.setVisibility(View.GONE);

        mDigitalSignBeanDao = DappApplication.getInstance().getDaoSession().getDigitalSignBeanDao();

        LinearLayoutManager lm = new LinearLayoutManager(this);
        mTokenMoreRecyclerView.setLayoutManager(lm);

        DigitalSignRecordAdapter adapter = new DigitalSignRecordAdapter(this, digitalSignBeans);
        loadMoreRecyclerAdapter = new LoadMoreRecyclerAdapter(this, adapter, false);
        mTokenMoreRecyclerView.setAdapter(loadMoreRecyclerAdapter);
        ((SimpleItemAnimator) mTokenMoreRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false); //取消RecyclerView的动画效果

        adapter.setOnItemClickListener(new DigitalSignRecordAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DigitalSignBean digitalSignBean) {
                Intent intent = new Intent(DigitalSignRecordActivity.this, DigitalSignDetailActivity.class);
                intent.putExtra(DigitalSignDetailActivity.SIGN_DATA,digitalSignBean);
                startActivity(intent);
            }
        });
    }

    private void initData() {
        digitalSignBeans.clear();
        digitalSignBeans.addAll(mDigitalSignBeanDao.queryBuilder().list());
        mPtrFrameLayout.refreshComplete();
        loadMoreRecyclerAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPtrFrameLayout.autoRefresh();
    }

    @Override
    public void refresh(PtrFrameLayout frame) {
        initData();
    }

}
