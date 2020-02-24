package com.zhaoxi.Open_source_Android.ui.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.base.BaseTitleBarActivity;
import com.zhaoxi.Open_source_Android.common.base.DappApplication;
import com.zhaoxi.Open_source_Android.db.CreateDbWallet;
import com.zhaoxi.Open_source_Android.libs.tools.ChangeWalletAsyncTask;
import com.zhaoxi.Open_source_Android.libs.utils.CheckPswMeter;
import com.zhaoxi.Open_source_Android.libs.utils.StrUtil;
import com.zhaoxi.Open_source_Android.web3.bean.WalletBean;
import com.zhaoxi.Open_source_Android.web3.utils.AESUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChangeWalletPsdActivity extends BaseTitleBarActivity {

    @BindView(R.id.change_wallet_psd_edit_old)
    EditText mPsdEditOld;
    @BindView(R.id.change_wallet_psd_edit_new1)
    EditText mPsdEditNew1;
    @BindView(R.id.change_wallet_psd_edit_new2)
    EditText mPsdEditNew2;
    @BindView(R.id.change_wallet_psd_img_eyes_one)
    ImageView mImgOne;
    @BindView(R.id.change_wallet_psd_img_eyes_two)
    ImageView mImgTwo;
    @BindView(R.id.change_wallet_psd_img_eyes_three)
    ImageView mImgThree;
    @BindView(R.id.change_wallet_psd_img_strength)
    TextView mImgStrength;
    @BindView(R.id.change_wallet_psd_btn_submit)
    Button mBtnSubmit;

    private String address;
    private boolean isOld = false;
    private boolean isNewOne = false;
    private boolean isNewTwo = false;
    private boolean mShowOldPsd = false, mShowOnepsd = false, mShowTwopsd = false;
    private CreateDbWallet mCreateDbWallet;
    private WalletBean mWalletBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_wallet_psd);
        ButterKnife.bind(this);
        address = getIntent().getStringExtra(WalletDetailsActivity.WALLET_ADDRESS);
        if (StrUtil.isEmpty(address)) {
            finish();
            return;
        }

        mPsdEditOld.addTextChangedListener(new psdOldTextWatcher());
        mPsdEditNew1.addTextChangedListener(new psdNew1TextWatcher());
        mPsdEditNew2.addTextChangedListener(new psdNew2TextWatcher());

        setTitle(R.string.change_wallet_txt_title,true);
    }

    private void submitChange() {
        String oldPsd = mPsdEditOld.getText().toString();
        String newOne = mPsdEditNew1.getText().toString();
        String newTwo = mPsdEditNew2.getText().toString();
        if(oldPsd.equals(newOne)){
            DappApplication.getInstance().showToast(getResources().getString(R.string.change_wallet_psd_toast_04));
            return;
        }
        if (oldPsd.length() < 8 || newOne.length() < 8 || newTwo.length() < 8) {
            DappApplication.getInstance().showToast(getResources().getString(R.string.import_wallet_item_edit_psd_length));
            return;
        }
        if (!newOne.equals(newTwo)) {
            DappApplication.getInstance().showToast(getResources().getString(R.string.import_wallet_item_edit_psd_and_other));
            return;
        }

        ChangeWalletAsyncTask asyncTask = new ChangeWalletAsyncTask(ChangeWalletPsdActivity.this, address, oldPsd, newOne, 1);
        asyncTask.setOnResultListener(new ChangeWalletAsyncTask.OnResultChangeListener() {
            @Override
            public void setOnResultListener(int result) {
                if (result == 0) {
                    // 判断是否有助记词
                    mCreateDbWallet = new CreateDbWallet(ChangeWalletPsdActivity.this);
                    mWalletBean = mCreateDbWallet.queryWallet(ChangeWalletPsdActivity.this,address);
                    if (!StrUtil.isEmpty(mWalletBean.getMnemonic())) {//存在助记词
                        String mnemonic = AESUtil.decrypt(oldPsd,mWalletBean.getMnemonic());
                        // 修改助记词密码
                        String addMnemonic = AESUtil.encrypt(newOne,mnemonic);
                        //插入助记词
                        int resultaa = mCreateDbWallet.updateMnemonicstore(address,addMnemonic);
                    }
                    DappApplication.getInstance().showSucceseToast(getResources().getString(R.string.change_wallet_psd_toast_01));
                    finish();
                } else if (result == 3) {
                    DappApplication.getInstance().showErrorToast(getResources().getString(R.string.change_wallet_psd_toast_02));
                    return;
                } else {
                    DappApplication.getInstance().showErrorToast(getResources().getString(R.string.change_wallet_psd_toast_03));
                    return;
                }
            }
        });
        asyncTask.execute();
    }

    @OnClick({R.id.change_wallet_psd_btn_submit, R.id.change_wallet_psd_img_eyes_one,
            R.id.change_wallet_psd_img_eyes_two, R.id.change_wallet_psd_img_eyes_three})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.change_wallet_psd_btn_submit:
                submitChange();
                break;
            case R.id.change_wallet_psd_img_eyes_one:
                String curOld = mPsdEditOld.getText().toString();
                if (!mShowOldPsd) {
                    mImgOne.setImageResource(R.mipmap.icon_edit_psd_y);
                    mPsdEditOld.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    mPsdEditOld.setSelection(curOld.length());
                } else {
                    mPsdEditOld.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    mImgOne.setImageResource(R.mipmap.icon_edit_psd_g);
                    mPsdEditOld.setSelection(curOld.length());
                }
                mShowOldPsd = !mShowOldPsd;
                break;
            case R.id.change_wallet_psd_img_eyes_two:
                String curOne = mPsdEditNew1.getText().toString();
                if (!mShowOnepsd) {
                    mImgTwo.setImageResource(R.mipmap.icon_edit_psd_y);
                    mPsdEditNew1.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    mPsdEditNew1.setSelection(curOne.length());
                } else {
                    mPsdEditNew1.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    mImgTwo.setImageResource(R.mipmap.icon_edit_psd_g);
                    mPsdEditNew1.setSelection(curOne.length());
                }
                mShowOnepsd = !mShowOnepsd;
                break;
            case R.id.change_wallet_psd_img_eyes_three:
                String curTwo = mPsdEditNew2.getText().toString();
                if (!mShowTwopsd) {
                    mImgThree.setImageResource(R.mipmap.icon_edit_psd_y);
                    mPsdEditNew2.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    mPsdEditNew2.setSelection(curTwo.length());
                } else {
                    mPsdEditNew2.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    mImgThree.setImageResource(R.mipmap.icon_edit_psd_g);
                    mPsdEditNew2.setSelection(curTwo.length());
                }
                mShowTwopsd = !mShowTwopsd;
                break;
        }
    }

    private class psdOldTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() > 0) {
                isOld = true;
                mImgOne.setVisibility(View.VISIBLE);
            } else {
                isOld = false;
                mImgOne.setVisibility(View.INVISIBLE);
            }

            if (isOld && isNewOne && isNewTwo) {
                mBtnSubmit.setBackgroundResource(R.drawable.draw_btn_defult_bg_03);
                mBtnSubmit.setTextColor(getResources().getColor(R.color.white));
                mBtnSubmit.setEnabled(true);
            } else {
                mBtnSubmit.setBackgroundResource(R.drawable.draw_btn_defult_bg_01);
                mBtnSubmit.setTextColor(getResources().getColor(R.color.color_2E2F47));
                mBtnSubmit.setEnabled(false);
            }
        }
    }

    private class psdNew1TextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() > 0) {
                isNewOne = true;
                mImgTwo.setVisibility(View.VISIBLE);
                mImgStrength.setVisibility(View.VISIBLE);
            } else {
                isNewOne = false;
                mImgTwo.setVisibility(View.INVISIBLE);
                mImgStrength.setVisibility(View.GONE);
            }

            int result = CheckPswMeter.checkStrong(s.toString());
            if(result == 1){//弱
                mImgStrength.setTextColor(getResources().getColor(R.color.color_FF4465));
                mImgStrength.setText(getResources().getString(R.string.create_wallet_txt_strgenth_01));
            }else if(result == 2){//中
                mImgStrength.setTextColor(getResources().getColor(R.color.color_FF7416));
                mImgStrength.setText(getResources().getString(R.string.create_wallet_txt_strgenth_02));
            }else{//强
                mImgStrength.setTextColor(getResources().getColor(R.color.color_29CF7D));
                mImgStrength.setText(getResources().getString(R.string.create_wallet_txt_strgenth_03));
            }

            if (isOld && isNewOne && isNewTwo) {
                mBtnSubmit.setBackgroundResource(R.drawable.draw_btn_defult_bg_03);
                mBtnSubmit.setTextColor(getResources().getColor(R.color.white));
                mBtnSubmit.setEnabled(true);
            } else {
                mBtnSubmit.setBackgroundResource(R.drawable.draw_btn_defult_bg_01);
                mBtnSubmit.setTextColor(getResources().getColor(R.color.color_2E2F47));
                mBtnSubmit.setEnabled(false);
            }
        }
    }

    private class psdNew2TextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() > 0) {
                isNewTwo = true;
                mImgThree.setVisibility(View.VISIBLE);
            } else {
                isNewTwo = false;
                mImgThree.setVisibility(View.INVISIBLE);
            }

            if (isOld && isNewOne && isNewTwo) {
                mBtnSubmit.setBackgroundResource(R.drawable.draw_btn_defult_bg_03);
                mBtnSubmit.setTextColor(getResources().getColor(R.color.white));
                mBtnSubmit.setEnabled(true);
            } else {
                mBtnSubmit.setBackgroundResource(R.drawable.draw_btn_defult_bg_01);
                mBtnSubmit.setTextColor(getResources().getColor(R.color.color_2E2F47));
                mBtnSubmit.setEnabled(false);
            }
        }
    }
}
