package com.zhaoxi.Open_source_Android.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import com.zhaoxi.Open_source_Android.Config;
import com.zhaoxi.Open_source_Android.DAppConstants;
import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.base.BaseActivity;
import com.zhaoxi.Open_source_Android.libs.utils.ChangeLanguageUtil;
import com.zhaoxi.Open_source_Android.libs.utils.StrUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CreateWalletSucceseActivity extends BaseActivity {
    public static final String WALLET_VALUE = "CreateWalletSucceseActivity.WALLET_VALUE";
    public static final String WALLET_ADDRESS = "CreateWalletSucceseActivity.WALLET_ADDRESS";
    @BindView(R.id.create_wallet_succese_btn_what)
    TextView mBtnWhat;

    private String mValue;
    private String mAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_succese);
        ButterKnife.bind(this);
        mValue = getIntent().getStringExtra(WALLET_VALUE);
        mAddress = getIntent().getStringExtra(WALLET_ADDRESS);
        if (StrUtil.isEmpty(mValue) || StrUtil.isEmpty(mAddress)) {
            finish();
        }

        mBtnWhat.setText(changeTextColor(getResources().getString(R.string.create_wallet_succese_02)));
        mBtnWhat.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @OnClick({R.id.create_wallet_succese_btn_next, R.id.create_wallet_succese_btn_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.create_wallet_succese_btn_next:
                Intent goto_backup = new Intent(this, ExportMnemonicOneActivity.class);
                goto_backup.putExtra(ExportMnemonicOneActivity.MNEMONIC_RESULT, mValue);
                goto_backup.putExtra(ExportMnemonicOneActivity.ADDRESS_RESULT, mAddress);
                startActivityForResult(goto_backup, 0x345);
                break;
            case R.id.create_wallet_succese_btn_back:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    /**
     * 修改部分字体颜色
     */
    private SpannableString changeTextColor(String message) {
        SpannableString builder = new SpannableString(message);
        int language = ChangeLanguageUtil.languageType(this);
        int start;
        if (language == 1) {
            start = message.length() - 6;
        } else {
            start = message.indexOf("View tutorial in detail");
        }
        if (start < 0) {
            start = 0;
        }
        int end = message.length();
        builder.setSpan(new ForegroundColorSpan(Color.parseColor("#4A5FE2")), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent it = new Intent(CreateWalletSucceseActivity.this, CommonWebActivity.class);
                it.putExtra(CommonWebActivity.WEBVIEW_LOAD_URL, Config.COMMON_WEB_URL + DAppConstants.backUrlHou(CreateWalletSucceseActivity.this, 7));
                it.putExtra(CommonWebActivity.ACTIVITY_TITLE_INFO, getResources().getString(R.string.create_wallet_succese_04));
                startActivity(it);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setUnderlineText(false);
            }
        };

        builder.setSpan(clickableSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return builder;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0x345 && resultCode == RESULT_OK) {
            finish();
        }
    }
}
