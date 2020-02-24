package com.zhaoxi.Open_source_Android.ui.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.base.BaseTitleBarActivity;
import com.zhaoxi.Open_source_Android.common.base.DappApplication;
import com.zhaoxi.Open_source_Android.common.dialog.CommonWarnDialog;
import com.zhaoxi.Open_source_Android.libs.utils.StrUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 导出私钥
 *
 * @author zhutt on 2018-06-12
 */
public class ExportPrivateKeyActivity extends BaseTitleBarActivity {
    @BindView(R.id.export_private_key_txt_content)
    TextView mTxtContent;
    private String privateKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export_private_key);
        ButterKnife.bind(this);
        privateKey = getIntent().getStringExtra(WalletDetailsActivity.WALLET_KEY_RESULT);
        if (StrUtil.isEmpty(privateKey)) {
            finish();
        }
        setTitle(R.string.wallet_manager_details_txt_export_key,true);
        mTxtContent.setText(privateKey);
        showWarnDialog();
    }

    @OnClick(R.id.export_private_key_btn_submit)
    public void onViewClicked() {
        //复制地址
        //获取剪贴板管理器：
        ClipboardManager cm = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
        // 创建普通字符型ClipData
        ClipData mClipData = ClipData.newPlainText("Label", privateKey);
        // 将ClipData内容放到系统剪贴板里。
        cm.setPrimaryClip(mClipData);
        DappApplication.getInstance().showToast(getResources().getString(R.string.export_private_key_toast_copy));
    }

    private void showWarnDialog() {
        CommonWarnDialog.Builder Bdialog = new CommonWarnDialog.Builder(this);
        Bdialog.setMassage(getResources().getString(R.string.salf_diallog_txt_export_privatekey));
        CommonWarnDialog dialog = Bdialog.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
    }
}
