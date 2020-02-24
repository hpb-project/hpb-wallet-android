package com.zhaoxi.Open_source_Android.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhaoxi.Open_source_Android.Config;
import com.zhaoxi.Open_source_Android.DAppConstants;
import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.base.BaseTitleBarActivity;
import com.zhaoxi.Open_source_Android.common.base.DappApplication;
import com.zhaoxi.Open_source_Android.libs.tools.CreateWalletAsyncTask;
import com.zhaoxi.Open_source_Android.libs.tools.PsdTextWatcher;
import com.zhaoxi.Open_source_Android.libs.utils.StrUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 创建钱包
 *
 * @author zhutt on 2018-06-08
 */
public class CreateWalletActivity extends BaseTitleBarActivity {

    @BindView(R.id.create_wallet_edit_name)
    EditText mEditName;
    @BindView(R.id.create_wallet_edit_psd)
    EditText mEditPsd;
    @BindView(R.id.create_wallet_edit_psdtwo)
    EditText mEditPsdtwo;
    @BindView(R.id.create_wallet_img_eyes_one)
    ImageView mImgEyesOne;
    @BindView(R.id.create_wallet_img_eyes_two)
    ImageView mImgEyesTwo;
    @BindView(R.id.create_wallet_txt_psd_warn)
    TextView mTxtPsdWarn;
    @BindView(R.id.create_wallet_psd_stengh)
    TextView mTxtStrength;
    @BindView(R.id.import_mnemonic_checkbox)
    CheckBox mCheckbox;
    @BindView(R.id.import_mnemonic_btn_import)
    Button mBtnCreate;
    @BindView(R.id.import_mnemonic_btn_what)
    TextView mBtnWhat;
    @BindView(R.id.create_wallet_img_delete)
    ImageView mImgDelete;
    @BindView(R.id.import_mnemonic_checkbox_txt_url)
    TextView mTxtCheckUrl;

    private boolean showOnePassword = false;
    private boolean showTwoPassword = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_wallet);
        ButterKnife.bind(this);
        initViews();
    }

    private void initViews() {
        setTitle(R.string.create_wallet_title,true);
        mBtnCreate.setText(R.string.create_wallet_txt_01);
        mEditPsd.setOnFocusChangeListener(new psdFousChangeListener());
        mEditPsd.addTextChangedListener(new PsdTextWatcher(this, mEditPsd,mTxtPsdWarn, mImgEyesOne, mTxtStrength));
        mEditPsdtwo.addTextChangedListener(new psdTwoTextWatcher());
        mEditName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    mImgDelete.setVisibility(View.VISIBLE);
                }else{
                    mImgDelete.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    @OnClick({R.id.create_wallet_img_eyes_one,R.id.create_wallet_img_eyes_two,
            R.id.import_mnemonic_checkbox, R.id.import_mnemonic_checkbox_txt_url,
            R.id.import_mnemonic_btn_import, R.id.import_mnemonic_btn_what,
            R.id.create_wallet_img_delete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.create_wallet_img_delete:
                mEditName.setText("");
                break;
            case R.id.create_wallet_img_eyes_one://是否明文显示
                String curOnePsd = mEditPsd.getText().toString();
                if (!showOnePassword) {
                    mImgEyesOne.setImageResource(R.mipmap.icon_edit_psd_y);
                    mEditPsd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    mEditPsd.setSelection(curOnePsd.length());
                } else {
                    mEditPsd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    mImgEyesOne.setImageResource(R.mipmap.icon_edit_psd_g);
                    mEditPsd.setSelection(curOnePsd.length());
                }
                showOnePassword = !showOnePassword;
                break;
            case R.id.create_wallet_img_eyes_two://是否明文显示
                String curTwoPsd = mEditPsdtwo.getText().toString();
                if (!showTwoPassword) {
                    mImgEyesTwo.setImageResource(R.mipmap.icon_edit_psd_y);
                    mEditPsdtwo.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    mEditPsdtwo.setSelection(curTwoPsd.length());
                } else {
                    mImgEyesTwo.setImageResource(R.mipmap.icon_edit_psd_g);
                    mEditPsdtwo.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    mEditPsdtwo.setSelection(curTwoPsd.length());
                }
                showTwoPassword = !showTwoPassword;
                break;
            case R.id.import_mnemonic_checkbox:
                if (mCheckbox.isChecked()) {
                    mBtnCreate.setEnabled(true);
                    mBtnCreate.setTextColor(getResources().getColor(R.color.white));
                    mBtnCreate.setBackgroundResource(R.drawable.draw_btn_defult_bg_03);
                } else {
                    mBtnCreate.setEnabled(false);
                    mBtnCreate.setTextColor(getResources().getColor(R.color.color_2E2F47));
                    mBtnCreate.setBackgroundResource(R.drawable.draw_btn_defult_bg_01);
                }
                break;
            case R.id.import_mnemonic_checkbox_txt_url://查看协议
                Intent goto_webView = new Intent(this, CommonWebActivity.class);
                goto_webView.putExtra(CommonWebActivity.ACTIVITY_TITLE_INFO, getResources().getString(R.string.create_wallet_txt_right_01));
                goto_webView.putExtra(CommonWebActivity.WEBVIEW_LOAD_URL, Config.COMMON_WEB_URL+ DAppConstants.backUrlHou(this,4));
                startActivity(goto_webView);
                break;
            case R.id.import_mnemonic_btn_import://创建钱包
                createWallet();
                break;
            case R.id.import_mnemonic_btn_what://导入钱包
                startActivityForResult(new Intent(this,ImportWalletTwoActivity.class),0x230);
                break;
        }
    }

    /**
     * 创建钱包
     */
    private void createWallet() {
        String name = mEditName.getText().toString();
        if (StrUtil.isEmpty(name)) {
            DappApplication.getInstance().showToast(getResources().getString(R.string.create_wallet_edit_name_empty));
            mEditName.requestFocus();
            return;
        }

        String onePsd = mEditPsd.getText().toString();
        String twoPsd = mEditPsdtwo.getText().toString();
        if (StrUtil.isEmpty(onePsd)) {
            DappApplication.getInstance().showToast(getResources().getString(R.string.import_wallet_item_edit_psd_empty));
            mEditPsd.requestFocus();
            return;
        }

        if (onePsd.length() < 8) {
            DappApplication.getInstance().showToast(getResources().getString(R.string.import_wallet_item_edit_psd_length));
            return;
        }

        if (!onePsd.equals(twoPsd)) {
            mEditPsdtwo.setText("");
            DappApplication.getInstance().showToast(getResources().getString(R.string.import_wallet_item_edit_psd_and_other));
            return;
        }

        CreateWalletAsyncTask asyncTask = new CreateWalletAsyncTask(this, name, onePsd, "");
        asyncTask.execute();
    }

    /**
     * 密码焦点变化监听器
     */
    private class psdFousChangeListener implements View.OnFocusChangeListener {

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                mTxtPsdWarn.setVisibility(View.VISIBLE);
            }
        }
    }

    private class psdTwoTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String txt = s.toString();
            //注意返回值是char数组
            char[] stringArr = txt.toCharArray();
            for (int i = 0; i < stringArr.length; i++) {
                //转化为string
                String value = new String(String.valueOf(stringArr[i]));
                Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
                Matcher m = p.matcher(value);
                if (m.matches()) {
                    mEditPsdtwo.setText(mEditPsdtwo.getText().toString().substring(0, mEditPsdtwo.getText().toString().length() - 1));
                    mEditPsdtwo.setSelection(mEditPsdtwo.getText().toString().length());
                    return;
                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() > 0)
                mImgEyesTwo.setVisibility(View.VISIBLE);
            else mImgEyesTwo.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 0x230 && resultCode == RESULT_OK){
            finish();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        //在切换到后台或其他应用时 清空密码
        if(!"".equals(mEditPsd.getText().toString())){
            mEditPsd.setText("");
        }

        if(!"".equals(mEditPsdtwo.getText().toString())){
            mEditPsdtwo.setText("");
        }
    }
}
