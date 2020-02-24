package com.zhaoxi.Open_source_Android.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.base.BaseTitleBarActivity;
import com.zhaoxi.Open_source_Android.common.base.DappApplication;
import com.zhaoxi.Open_source_Android.common.dialog.CommonPsdPop;
import com.zhaoxi.Open_source_Android.db.CreateDbWallet;
import com.zhaoxi.Open_source_Android.db.DBManager;
import com.zhaoxi.Open_source_Android.libs.tools.ChangeWalletAsyncTask;
import com.zhaoxi.Open_source_Android.libs.tools.CommonDilogTool;
import com.zhaoxi.Open_source_Android.libs.tools.ExportWalletAsyncTask;
import com.zhaoxi.Open_source_Android.libs.utils.SharedPreferencesUtil;
import com.zhaoxi.Open_source_Android.libs.utils.StrUtil;
import com.zhaoxi.Open_source_Android.web3.bean.WalletBean;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 钱包详情
 *
 * @author zhutt on 2018-06-08
 */
public class WalletDetailsActivity extends BaseTitleBarActivity {
    public static final String WALLET_SHOW_DAILOG = "WalletDetailsActivity.dialog";
    public static final String WALLET_TITLE = "WalletDetailsActivity.title";
    public static final String WALLET_ADDRESS = "WalletDetailsActivity.address";
    public static final String WALLET_RESULT = "WalletDetailsActivity.WALLET_RESULT";
    public static final String WALLET_MNEMONIC_RESULT = "WalletDetailsActivity.WALLET_MNEMONIC_RESULT";
    public static final String WALLET_KEY_RESULT = "WalletDetailsActivity.WALLET_KEY_RESULT";

    @BindView(R.id.wallet_details_edit_name)
    EditText mEditName;
    @BindView(R.id.wallet_details_btn_monc_wallet)
    RelativeLayout mBtnExportMnemonic;
    @BindView(R.id.wallet_details_txt_no_copy)
    TextView mTxtNoCopy;
    @BindView(R.id.wallet_details_txt_address)
    TextView mTxtAddress;
    @BindView(R.id.view_line_horizontal)
    View mViewLine;
    @BindView(R.id.wallet_details_layout_cold_wallet)
    LinearLayout mLayoutColdWallet;
    @BindView(R.id.wallet_details_layout_hold_wallet)
    LinearLayout mLayoutHoldWallet;
    @BindView(R.id.wallet_details_txt_wallet_status)
    TextView mTxtColdStatus;
    @BindView(R.id.wallet_details_txt_wallet_header)
    TextView mTxtHeaderName;
    @BindView(R.id.root_wallet_details)
    RelativeLayout mRootView;

    private String title;
    private String address;
    private boolean isShowDialog = false;
    private CreateDbWallet mCreateDbWallet;
    private WalletBean mWalletBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_details);
        ButterKnife.bind(this);
        initDatas();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCreateDbWallet = new CreateDbWallet(this);
        //根据address获取相关钱包数据
        mWalletBean = mCreateDbWallet.queryWallet(this, address);

        if (mWalletBean.getIsClodWallet() == 0) {//热钱包
            mLayoutHoldWallet.setVisibility(View.VISIBLE);
            mLayoutColdWallet.setVisibility(View.GONE);
            mTxtColdStatus.setVisibility(View.GONE);
        } else {
            mLayoutHoldWallet.setVisibility(View.GONE);
            mLayoutColdWallet.setVisibility(View.VISIBLE);
            mTxtColdStatus.setVisibility(View.VISIBLE);
        }

        //判断是否有助记词
        if (!StrUtil.isEmpty(mWalletBean.getMnemonic())) {
            mBtnExportMnemonic.setVisibility(View.VISIBLE);
            mTxtNoCopy.setVisibility(View.VISIBLE);
            mViewLine.setVisibility(View.VISIBLE);
        } else {
            mBtnExportMnemonic.setVisibility(View.GONE);
            mTxtNoCopy.setVisibility(View.GONE);
            mViewLine.setVisibility(View.GONE);
        }
        mTxtHeaderName.setText(StrUtil.getWalletHeaderName(mWalletBean.getWalletBName()));
    }

    private void initDatas() {
        title = getIntent().getStringExtra(WALLET_TITLE);
        address = getIntent().getStringExtra(WALLET_ADDRESS);
        isShowDialog = getIntent().getBooleanExtra(WALLET_SHOW_DAILOG, false);
        if (StrUtil.isEmpty(address)) {
            finish();
            return;
        }

        setTitle(R.string.wallet_manager_details_txt_title, true);
        mEditName.setText(title);
        mEditName.setSelection(title.length());
        mTxtAddress.setText(address);
        if (isShowDialog) {
            final View mView = getWindow().getDecorView();
            try {
                mView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showMnemonicDialog();
                    }
                }, 500);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }


    }

    @OnClick({R.id.wallet_details_btn_monc_wallet, R.id.wallet_details_btn_delete_wallet,
            R.id.wallet_details_layout_change_psd, R.id.wallet_details_layout_exprot_privatekey,
            R.id.wallet_details_layout_exprot_keystore, R.id.wallet_details_btn_name,
            R.id.wallet_details_txt_address, R.id.wallet_details_btn_delete_coldwallet})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.wallet_details_btn_monc_wallet://备注助记词
                showMnemonicDialog();
                break;
            case R.id.wallet_details_btn_delete_wallet://删除钱包
                showDeleteWalletDialog();
                break;
            case R.id.wallet_details_layout_change_psd://修改密码
                Intent goto_changePsd = new Intent(this, ChangeWalletPsdActivity.class);
                goto_changePsd.putExtra(WALLET_ADDRESS, address);
                startActivity(goto_changePsd);
                break;
            case R.id.wallet_details_layout_exprot_privatekey://导出私钥
                showprivateKeyDialog();
                break;
            case R.id.wallet_details_layout_exprot_keystore://导出keystore
                showKeystoreDialog();
                break;
            case R.id.wallet_details_btn_name:
                changeName();
                break;
            case R.id.wallet_details_txt_address:
                copyLable(address);
                break;
            case R.id.wallet_details_btn_delete_coldwallet://删除冷钱包
                showDeleteColdWallet();
                break;
        }
    }

    private void changeName() {
        //保存
        String walletName = mEditName.getText().toString();
        if (StrUtil.isEmpty(walletName)) {
            DappApplication.getInstance().showToast(getResources().getString(R.string.wallet_manager_details_toast_tip_03));
            return;
        }

        if (walletName.equals(title)) {
            DappApplication.getInstance().showToast(getResources().getString(R.string.wallet_manager_details_toast_tip_04));
            return;
        }

        int result = mCreateDbWallet.updateWalletName(address, walletName);
        if (result == 0) {
            DappApplication.getInstance().showToast(getResources().getString(R.string.wallet_manager_details_toast_tip_05));
            finish();
        } else {
            DappApplication.getInstance().showToast(getResources().getString(R.string.wallet_manager_details_toast_tip_01));
        }
    }

    /**
     * 导出私钥--弹出输入密码框
     */
    private void showprivateKeyDialog() {
        CommonPsdPop commonPsdPop = new CommonPsdPop(this, mRootView);
        commonPsdPop.setHandlePsd(new CommonPsdPop.HandlePsd() {
            @Override
            public void getInputPsd(String psd) {
                ExportWalletAsyncTask asyncTask = new ExportWalletAsyncTask(WalletDetailsActivity.this, address, psd, 3);
                asyncTask.setOnResultListener(new ExportWalletAsyncTask.OnResultExportListener() {
                    @Override
                    public void setOnResultListener(String result) {
                        if (result.startsWith("Failed") || result.contains("失败")) {
                            DappApplication.getInstance().showToast(result);
                        } else {
                            Intent goto_keystore = new Intent(WalletDetailsActivity.this, ExportPrivateKeyActivity.class);
                            goto_keystore.putExtra(WALLET_KEY_RESULT, result);
                            startActivity(goto_keystore);
                        }
                    }
                });
                asyncTask.execute();
            }
        });
        commonPsdPop.show(getResources().getString(R.string.wallet_manager_details_txt_export_key), null);
    }

    /**
     * 导出keyStore--弹出输入密码框
     */
    private void showKeystoreDialog() {
        CommonPsdPop commonPsdPop = new CommonPsdPop(this, mRootView);
        commonPsdPop.setHandlePsd(new CommonPsdPop.HandlePsd() {
            @Override
            public void getInputPsd(String psd) {
                ExportWalletAsyncTask asyncTask = new ExportWalletAsyncTask(WalletDetailsActivity.this, address, psd, 2);
                asyncTask.setOnResultListener(new ExportWalletAsyncTask.OnResultExportListener() {
                    @Override
                    public void setOnResultListener(String result) {
                        if (result.startsWith("Failed") || result.contains("失败")) {
                            DappApplication.getInstance().showToast(result);
                        } else {
                            Intent goto_keystore = new Intent(WalletDetailsActivity.this, ExportKeystoreActivity.class);
                            goto_keystore.putExtra(WALLET_RESULT, result);
                            startActivity(goto_keystore);
                        }
                    }
                });
                asyncTask.execute();
            }
        });
        commonPsdPop.show(getResources().getString(R.string.wallet_manager_details_txt_export_keystore), null);
    }

    /**
     * 导出助记词--弹出输入密码框
     */
    private void showMnemonicDialog() {
        CommonPsdPop commonPsdPop = new CommonPsdPop(this, mRootView);
        commonPsdPop.setHandlePsd(new CommonPsdPop.HandlePsd() {
            @Override
            public void getInputPsd(String psd) {
                ExportWalletAsyncTask asyncTask = new ExportWalletAsyncTask(WalletDetailsActivity.this, address, psd, 1);
                asyncTask.setOnResultListener(new ExportWalletAsyncTask.OnResultExportListener() {
                    @Override
                    public void setOnResultListener(String result) {
                        if (result.startsWith("Failed") || result.contains("失败")) {
                            DappApplication.getInstance().showToast(result);
                        } else {
                            Intent goto_backup = new Intent(WalletDetailsActivity.this, ExportMnemonicOneActivity.class);
                            goto_backup.putExtra(ExportMnemonicOneActivity.MNEMONIC_RESULT, result);
                            goto_backup.putExtra(ExportMnemonicOneActivity.ADDRESS_RESULT, address);
                            startActivity(goto_backup);
                        }
                    }
                });
                asyncTask.execute();
            }
        });
        commonPsdPop.show(getResources().getString(R.string.wallet_manager_details_txt_export_zhjici), null);
    }

    /**
     * 删除钱包--弹出输入密码框
     */
    private void showDeleteWalletDialog() {
        CommonPsdPop commonPsdPop = new CommonPsdPop(this, mRootView);
        commonPsdPop.setHandlePsd(new CommonPsdPop.HandlePsd() {
            @Override
            public void getInputPsd(String psd) {
                ChangeWalletAsyncTask asyncTask = new ChangeWalletAsyncTask(WalletDetailsActivity.this, address, mWalletBean.getImgId(), psd, 2);
                asyncTask.setOnResultListener(new ChangeWalletAsyncTask.OnResultChangeListener() {
                    @Override
                    public void setOnResultListener(int result) {
                        if (result == 0) {
                            //判断是否是默认钱包
                            String mDefultAddress = SharedPreferencesUtil.getSharePreString(WalletDetailsActivity.this, SharedPreferencesUtil.CHOOSE_WALLET_ADDRESS);
                            if (mDefultAddress.equals(address)) {//如果删除为默认钱包 那么自动将默认钱包跟改为最新钱包
                                //将最新的钱包设置为默认钱包
                                WalletBean walletBean = mCreateDbWallet.queryNewWallet();
                                if (walletBean != null) {
                                    SharedPreferencesUtil.setSharePreString(WalletDetailsActivity.this, SharedPreferencesUtil.CHOOSE_WALLET_ADDRESS, walletBean.getAddress());
                                } else {
                                    SharedPreferencesUtil.setSharePreString(WalletDetailsActivity.this, SharedPreferencesUtil.CHOOSE_WALLET_ADDRESS, "");
                                }
                                //删除相关的交易记录数据
                                DBManager.deleteHistory(mDefultAddress);
                            }
                            finish();
                        } else if (result == 1) {
                            DappApplication.getInstance().showToast(getResources().getString(R.string.export_keystore_item_faile));
                        } else {
                            DappApplication.getInstance().showToast(getResources().getString(R.string.wallet_manager_details_toast_tip_02));
                        }
                    }
                });
                asyncTask.execute();
            }
        });
        commonPsdPop.show(null, getResources().getString(R.string.wallet_manager_details_dialog_tip_01));
    }

    private void showDeleteColdWallet() {
        CommonDilogTool dialogTool = new CommonDilogTool(this);
        dialogTool.show(null, getResources().getString(R.string.cold_wallet_txt_13), null,
                getResources().getString(R.string.dailog_psd_btn_cancle), null,
                getResources().getString(R.string.dailog_psd_btn_ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ChangeWalletAsyncTask asyncTask = new ChangeWalletAsyncTask(WalletDetailsActivity.this, address, mWalletBean.getImgId(), 3);
                        asyncTask.setOnResultListener(new ChangeWalletAsyncTask.OnResultChangeListener() {
                            @Override
                            public void setOnResultListener(int result) {
                                if (result == 0) {
                                    //判断是否是默认钱包
                                    String mDefultAddress = SharedPreferencesUtil.getSharePreString(WalletDetailsActivity.this, SharedPreferencesUtil.CHOOSE_WALLET_ADDRESS);
                                    if (mDefultAddress.equals(address)) {//如果删除为默认钱包 那么自动将默认钱包跟改为最新钱包
                                        //将最新的钱包设置为默认钱包
                                        WalletBean walletBean = mCreateDbWallet.queryNewWallet();
                                        if (walletBean != null) {
                                            SharedPreferencesUtil.setSharePreString(WalletDetailsActivity.this, SharedPreferencesUtil.CHOOSE_WALLET_ADDRESS, walletBean.getAddress());
                                        } else {
                                            SharedPreferencesUtil.setSharePreString(WalletDetailsActivity.this, SharedPreferencesUtil.CHOOSE_WALLET_ADDRESS, "");
                                        }
                                        //删除相关的交易记录数据
                                        DBManager.deleteHistory(mDefultAddress);
                                    }
                                    finish();
                                } else {
                                    DappApplication.getInstance().showToast(getResources().getString(R.string.wallet_manager_details_toast_tip_02));
                                }
                            }
                        });
                        asyncTask.execute();
                    }
                });

    }
}
