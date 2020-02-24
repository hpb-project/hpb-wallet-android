package com.zhaoxi.Open_source_Android.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhaoxi.Open_source_Android.DAppConstants;
import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.base.BaseTitleBarActivity;
import com.zhaoxi.Open_source_Android.common.base.DappApplication;
import com.zhaoxi.Open_source_Android.common.bean.ShareAddressBean;
import com.zhaoxi.Open_source_Android.common.dialog.CommonWarnAddressDialog;
import com.zhaoxi.Open_source_Android.libs.tools.CreateQrAsyncTask;
import com.zhaoxi.Open_source_Android.libs.utils.ChangeLanguageUtil;
import com.zhaoxi.Open_source_Android.libs.utils.DrawAddressPictureUtil;
import com.zhaoxi.Open_source_Android.libs.utils.SharedPreferencesUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 收款
 * 根据地址生成二维码
 */
public class ReceivablesActivity extends BaseTitleBarActivity {

    @BindView(R.id.receivables_img_qr)
    ImageView mImgQr;
    @BindView(R.id.receivables_txt_address)
    TextView mTxtAddress;

    private int mDialogStatus;
    private Bitmap mBitmap;
    private DrawAddressPictureUtil drawLongPictureUtil;
    private int mLanguage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receivables);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initView() {
        setTitle(R.string.receive_activity_txt_title,true);
        showRightImgWithImgListener(R.mipmap.icon_share_right_01, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAutoPic(true);
            }
        });

        mDialogStatus = SharedPreferencesUtil.getSharePreInt(this, DAppConstants.RECEIVE_ADDRESS_WARN_STATUS);
        if (mDialogStatus == 0) {
            showGoCopyDialog();
        }
    }

    private void showGoCopyDialog() {
        CommonWarnAddressDialog.Builder builder = new CommonWarnAddressDialog.Builder(this);
        builder.setPositiveButton(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferencesUtil.setSharePreInt(ReceivablesActivity.this, DAppConstants.RECEIVE_ADDRESS_WARN_STATUS,1);
            }
        });
        CommonWarnAddressDialog dialog = builder.create();
        dialog.show();

        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = (int) (getWindowManager().getDefaultDisplay().getWidth() * 0.8);
        dialog.getWindow().setAttributes(params);
    }

    /**
     * 生成二维码
     */
    private void initData() {
        //step1.获取地址
        String address = SharedPreferencesUtil.getSharePreString(this, SharedPreferencesUtil.CHOOSE_WALLET_ADDRESS);
        mTxtAddress.setText(address);
        Bitmap logo = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_receive_qr_logo, null);
        CreateQrAsyncTask asyncTask = new CreateQrAsyncTask(this, mImgQr, address,logo);
        asyncTask.setOnResultListener(new CreateQrAsyncTask.OnResultListener() {
            @Override
            public void setOnResultListener(Bitmap bitmap) {
                mBitmap = bitmap;
            }
        });
        asyncTask.execute();//开始执行

        mLanguage = ChangeLanguageUtil.languageType(this);
    }

    @OnClick({R.id.receivables_btn_save_address, R.id.receivables_btn_copy_address})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.receivables_btn_save_address:
                createAutoPic(false);
                break;
            case R.id.receivables_btn_copy_address:
                // 创建普通字符型ClipData
                String content = mTxtAddress.getText().toString();
                copyLable(content);
                break;
        }
    }

    private void createAutoPic(boolean isShare){
        if(!isShare){
            showTextProgressDialog(getResources().getString(R.string.receive_activity_txt_top_05));
        }else{
            showTextProgressDialog(getResources().getString(R.string.receive_activity_txt_top_06));
        }
        drawLongPictureUtil = new DrawAddressPictureUtil(this);
        drawLongPictureUtil.setListener(new DrawAddressPictureUtil.Listener() {
            @Override
            public void onSuccess(String path,String thumbPath) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dismissTextProgressDialog();
                        if(isShare){//todo 分享

                        }else{
                            DappApplication.getInstance().showToast(getResources().getString(R.string.receive_activity_txt_top_07));
                        }
                    }
                });

            }

            @Override
            public void onFail() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dismissTextProgressDialog();
                    }
                });
            }
        });
        ShareAddressBean info = new ShareAddressBean();
        info.setBm(mBitmap);
        info.setAddress(mTxtAddress.getText().toString());
        info.setShowDes(mLanguage);
        info.setShare(isShare);

        drawLongPictureUtil.setData(info);
        drawLongPictureUtil.startDraw();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (drawLongPictureUtil != null) {
            drawLongPictureUtil.removeListener();
        }
    }
}
