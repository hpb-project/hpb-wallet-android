package com.zhaoxi.Open_source_Android.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.zhaoxi.Open_source_Android.Config;
import com.zhaoxi.Open_source_Android.DAppConstants;
import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.base.BaseTitleBarActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HelpCenterActivity extends BaseTitleBarActivity {

    @BindView(R.id.common_toolbar_tv_title)
    TextView mTxtTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_center);
        ButterKnife.bind(this);

        setTitle(R.string.activity_help_center_title,true);
    }

    @OnClick({R.id.activity_help_center_layout_question, R.id.activity_help_center_layout_callus, R.id.activity_help_center_layout_feekback})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.activity_help_center_layout_question://常见问题
                gotoWeb(getResources().getString(R.string.activity_help_center_txt_01),DAppConstants.backUrlHou(this,5));
                break;
            case R.id.activity_help_center_layout_callus:
                gotoWeb(getResources().getString(R.string.activity_help_center_txt_02),DAppConstants.backUrlHou(this,6));
                break;
            case R.id.activity_help_center_layout_feekback:
                startActivity(new Intent(this,FeekBackActivity.class));
                break;
        }
    }

    private void gotoWeb(String title,String urlHou){
        Intent goto_webView = new Intent(this, CommonWebActivity.class);
        goto_webView.putExtra(CommonWebActivity.ACTIVITY_TITLE_INFO, title);
        goto_webView.putExtra(CommonWebActivity.WEBVIEW_LOAD_URL, Config.COMMON_WEB_URL+ urlHou);
        startActivity(goto_webView);
    }
}
