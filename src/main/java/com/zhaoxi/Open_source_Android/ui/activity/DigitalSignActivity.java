package com.zhaoxi.Open_source_Android.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.gyf.immersionbar.ImmersionBar;
import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.base.BaseTitleBarActivity;
import com.zhaoxi.Open_source_Android.common.base.DappApplication;
import com.zhaoxi.Open_source_Android.common.bean.StrSignQrCodeBean;
import com.zhaoxi.Open_source_Android.common.dialog.CommonPopupWindowDialog;
import com.zhaoxi.Open_source_Android.db.CreateDbWallet;
import com.zhaoxi.Open_source_Android.libs.tools.CreateQrAsyncTask;
import com.zhaoxi.Open_source_Android.libs.tools.ExportWalletAsyncTask;
import com.zhaoxi.Open_source_Android.libs.tools.SystemLog;
import com.zhaoxi.Open_source_Android.libs.utils.ColdWalletUtil;
import com.zhaoxi.Open_source_Android.libs.utils.SharedPreferencesUtil;
import com.zhaoxi.Open_source_Android.libs.utils.SoftKeyboardUtil;
import com.zhaoxi.Open_source_Android.web3.bean.WalletBean;
import com.zhaoxi.Open_source_Android.web3.utils.TransferUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 数字签名
 */
public class DigitalSignActivity extends BaseTitleBarActivity {

    @BindView(R.id.et_sign_content)
    EditText mEtSignContent;
    @BindView(R.id.tv_current_char_num)
    TextView mTvCurrentCharNum;
    @BindView(R.id.btn_sign)
    Button mBtnSign;
    @BindView(R.id.root_view)
    LinearLayout mRootView;

    private String mDefaultAddress;
    /*用于判断是否为冷钱包时需要*/
    private CreateDbWallet mCreateDbWallet;
    private WalletBean mWalletBean;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_digital_sign);
        ButterKnife.bind(this);
        initView();
        initListener();
    }

    private void initView() {
        ImmersionBar.with(this)
                .statusBarDarkFont(true, 0.2f)
                .init();
        setTitleBgColor(R.color.white, true);
        setTitle(getResources().getString(R.string.fragment_main_news_digital_sign_txt), true);
        showRightTxtWithTextListener(getResources().getString(R.string.main_find_txt_04), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoftKeyboardUtil.hideSoftKeyboard(DigitalSignActivity.this);
                showMoreDialog();
            }
        });

        // 实例化创建钱包对象
        mCreateDbWallet = new CreateDbWallet(this);
        mDefaultAddress = SharedPreferencesUtil.getSharePreString(this, SharedPreferencesUtil.CHOOSE_WALLET_ADDRESS);
        mWalletBean = mCreateDbWallet.queryWallet(this, mDefaultAddress);
    }

    private void initListener() {
        mEtSignContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int charLen = s.length();
                if (charLen > 0) {
                    mBtnSign.setEnabled(true);
                } else {
                    mBtnSign.setEnabled(false);
                }

                mTvCurrentCharNum.setText(charLen + "/1000");
            }
        });
    }

    @OnClick(R.id.btn_sign)
    public void onClick(View view) {
        if (view.getId() == R.id.btn_sign) {
            if (mWalletBean.getIsClodWallet() != 0) {
                // 隐藏软键盘
                SoftKeyboardUtil.hideSoftKeyboard(this);
                StrSignQrCodeBean strSignQrCodeBean = new StrSignQrCodeBean();
                strSignQrCodeBean.setContent(mEtSignContent.getText().toString());
                strSignQrCodeBean.setFrom(mDefaultAddress);
                String digitalSignContent = ColdWalletUtil.toJson(ColdWalletUtil.TYPE_STR,strSignQrCodeBean);

                SystemLog.D("onClick", "digitalSignContent = " + digitalSignContent);
                showQrCodeDialog(digitalSignContent);
            }else {
                // 开始签名
                showDigitalSignPwdDialog();
            }
        }
    }

    private void showDigitalSignPwdDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.view_input_sign_password_layout, null, false);
        EditText etDigitalSignPwd = view.findViewById(R.id.et_digital_sign_password);
        Button btnSubmit = view.findViewById(R.id.btn_submit);
        TextView tvCancel = view.findViewById(R.id.tv_cancel);
        CommonPopupWindowDialog dialog = new CommonPopupWindowDialog(this, view);
        dialog.setFocusable(true);
        dialog.show(mRootView);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                // 确认,开始签名
                String pwd = etDigitalSignPwd.getText().toString();
                getPrivateKey(pwd);
            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 取消
                dialog.dismiss();
            }
        });

    }


    private void showMoreDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.view_digital_sign_more_layout, null, false);
        TextView tvSignRecord = view.findViewById(R.id.tv_sign_record);
//        TextView tvSignRule = view.findViewById(R.id.tv_digital_sign_rule);
        TextView tvDismiss = view.findViewById(R.id.tv_dismiss);

        CommonPopupWindowDialog dialog = new CommonPopupWindowDialog(this, view);
        dialog.show(mRootView);

        tvSignRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                // 签名记录
                startActivity(new Intent(DigitalSignActivity.this, DigitalSignRecordActivity.class));
            }
        });

        tvDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 取消
                dialog.dismiss();
            }
        });

    }

    private void getPrivateKey(String password) {
        //获取私钥
        ExportWalletAsyncTask asyncTask = new ExportWalletAsyncTask(DigitalSignActivity.this, mDefaultAddress, password, 10);
        asyncTask.setOnResultListener(new ExportWalletAsyncTask.OnResultExportListener() {
            @Override
            public void setOnResultListener(String result) {
                if (result.startsWith("Failed") || result.contains("失败")) {
                    DappApplication.getInstance().showToast(getResources().getString(R.string.transfer_activity_dialog_txt_06));
                } else {
                    // 密码验证成功后，生成签名信息
                    SystemLog.D("getPrivateKey", "result = " + result);
                    String content = mEtSignContent.getText().toString();
                    SharedPreferencesUtil.setSharePreString(DigitalSignActivity.this,SharedPreferencesUtil.DIGITAL_SIGN_STR_CONTENT,content);
                    String signContent = TransferUtils.sign(DigitalSignActivity.this,content, result);
                    SystemLog.D("getPrivateKey", "signContent = " + signContent);

                    Intent intent = new Intent(DigitalSignActivity.this, DigitalSignSucceedActivity.class);
                    intent.putExtra(DigitalSignSucceedActivity.SIGN_CONTENT,signContent);
                    startActivity(intent);
                }
            }
        });
        asyncTask.execute();
    }


    private void showQrCodeDialog(String strContent) {
        View view = LayoutInflater.from(this).inflate(R.layout.view_digital_sign_qrcode_layout, null, false);
        final PopupWindow popupWindow = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setAnimationStyle(R.style.VoteDialogAnim);
        popupWindow.setTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable());
        popupWindow.showAtLocation(mRootView, Gravity.CENTER, 0, 0);

        ImageView ivDigitalSignQrCode = view.findViewById(R.id.iv_qr_code_info);
        Bitmap logo = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_qrcode_logo, null);
        CreateQrAsyncTask asyncTask = new CreateQrAsyncTask(this, ivDigitalSignQrCode, strContent, logo);
        asyncTask.setOnResultListener(new CreateQrAsyncTask.OnResultListener() {
            @Override
            public void setOnResultListener(Bitmap bitmap) {
                // 生成的bitmap
            }
        });
        asyncTask.execute();//开始执行

        Button btnNextTip = view.findViewById(R.id.btn_next_tip);
        btnNextTip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 下一步
                popupWindow.dismiss();
                // 跳转到扫码页面
                SharedPreferencesUtil.setSharePreString(DigitalSignActivity.this,SharedPreferencesUtil.DIGITAL_SIGN_STR_CONTENT,mEtSignContent.getText().toString());
                Intent scanIntent = new Intent(DigitalSignActivity.this,ScaningActivity.class);
                scanIntent.putExtra(ScaningActivity.RESURE_TYPE,ScaningActivity.TYPE_DIGITAL_SIGN);
                startActivity(scanIntent);
                finish();
            }
        });

        // 设置背景透明度
        final WindowManager.LayoutParams lp = this.getWindow().getAttributes();
        lp.alpha = 0.7f;
        this.getWindow().setAttributes(lp);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                lp.alpha = 1f;
                getWindow().setAttributes(lp);
            }
        });

        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.BUTTON_BACK) {
                    popupWindow.dismiss();
                }
                return false;
            }
        });
    }

}
