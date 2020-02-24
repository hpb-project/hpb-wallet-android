package com.zhaoxi.Open_source_Android.ui.activity;

import android.os.Bundle;

import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.base.BaseActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class VoteWaitActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote_wait);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.activity_vote_Wait_back)
    public void onViewClicked() {
        finish();
    }
}
