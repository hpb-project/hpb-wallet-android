package com.zhaoxi.Open_source_Android.ui.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhaoxi.Open_source_Android.Config;
import com.zhaoxi.Open_source_Android.DAppConstants;
import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.base.BaseActivity;
import com.zhaoxi.Open_source_Android.common.base.DappApplication;
import com.zhaoxi.Open_source_Android.common.base.BaseFragment;
import com.zhaoxi.Open_source_Android.libs.tools.ChangeWalletTypeAsyncTask;
import com.zhaoxi.Open_source_Android.libs.tools.CommonDilogTool;
import com.zhaoxi.Open_source_Android.libs.tools.ImportWalletAsyncTask;
import com.zhaoxi.Open_source_Android.libs.tools.PsdTextWatcher;
import com.zhaoxi.Open_source_Android.libs.utils.StrUtil;
import com.zhaoxi.Open_source_Android.ui.activity.CommonWebActivity;
import com.zhaoxi.Open_source_Android.ui.activity.ImportWalletActivity;
import com.zhaoxi.Open_source_Android.ui.activity.MainmapTwoActivity;
import com.zhaoxi.Open_source_Android.web3.crypto.Bip39WalletUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 导入私钥
 * @author zhutt on 2018-06-13
 */
public class ImportPrivateKeyFragment extends BaseFragment {
    Unbinder unbinder;
    @BindView(R.id.import_mnemonic_layout_content)
    LinearLayout mLayoutContent;
    @BindView(R.id.import_mnemonic_edit_content)
    EditText mEditContent;
    @BindView(R.id.import_mnemonic_img_eyes_one)
    ImageView mImgEyesOne;
    @BindView(R.id.import_mnemonic_edit_psd_one)
    EditText mEditPsdOne;
    @BindView(R.id.import_mnemonic_txt_psd_warn)
    TextView mTxtPsdWarn;
    @BindView(R.id.import_mnemonic_img_eyes_two)
    ImageView mImgEyesTwo;
    @BindView(R.id.import_mnemonic_edit_psd_two)
    EditText mEditPsdTwo;
    @BindView(R.id.import_mnemonic_checkbox)
    CheckBox mCheckbox;
    @BindView(R.id.import_mnemonic_checkbox_txt_url)
    TextView mCheckboxTxtUrl;
    @BindView(R.id.import_mnemonic_btn_import)
    Button mBtnImport;
    @BindView(R.id.import_mnemonic_btn_what)
    TextView mBtnWhat;
    @BindView(R.id.import_mnemonic_img_strength)
    TextView mImgStrength;

    private BaseActivity mActivity;
    private boolean showOnePassword = false;
    private boolean showTwoPassword = false;
    private int mResouceType;//0 默认导入，1 映射导入

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_import_mnemonic, container, false);
        unbinder = ButterKnife.bind(this, contentView);
        mActivity = (BaseActivity) getActivity();
        mResouceType = getArguments().getInt(ImportWalletActivity.RESOUCE_TYPE, 0);
        initViews();
        return contentView;
    }

    private void initViews() {
        mLayoutContent.setVisibility(View.GONE);
        mEditContent.setVisibility(View.VISIBLE);
        mEditContent.setHint(R.string.import_wallet_item_privatekey_input_hint);
        mBtnWhat.setText(R.string.import_wallet_item_privatekey_btn_what);

        mEditPsdOne.setOnFocusChangeListener(new psdOneFousChangeListener());
        mEditPsdOne.addTextChangedListener(new PsdTextWatcher(mActivity,mEditPsdOne, mTxtPsdWarn, mImgEyesOne, mImgStrength));
        mEditPsdTwo.addTextChangedListener(new psdTwoTextWatcher());
    }

    @OnClick({R.id.import_mnemonic_img_eyes_one, R.id.import_mnemonic_img_eyes_two,
            R.id.import_mnemonic_btn_import, R.id.import_mnemonic_btn_what,
            R.id.import_mnemonic_checkbox,R.id.import_mnemonic_checkbox_txt_url})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.import_mnemonic_img_eyes_one://是否明文显示
                String curOnePsd = mEditPsdOne.getText().toString();
                if (!showOnePassword) {
                    mImgEyesOne.setImageResource(R.mipmap.icon_edit_psd_y);
                    mEditPsdOne.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    mEditPsdOne.setSelection(curOnePsd.length());
                } else {
                    mEditPsdOne.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    mImgEyesOne.setImageResource(R.mipmap.icon_edit_psd_g);
                    mEditPsdOne.setSelection(curOnePsd.length());
                }
                showOnePassword = !showOnePassword;
                break;
            case R.id.import_mnemonic_img_eyes_two://是否明文显示
                String curTwoPsd = mEditPsdTwo.getText().toString();
                if (!showTwoPassword) {
                    mImgEyesTwo.setImageResource(R.mipmap.icon_edit_psd_y);
                    mEditPsdTwo.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    mEditPsdTwo.setSelection(curTwoPsd.length());
                } else {
                    mImgEyesTwo.setImageResource(R.mipmap.icon_edit_psd_g);
                    mEditPsdTwo.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    mEditPsdTwo.setSelection(curTwoPsd.length());
                }
                showTwoPassword = !showTwoPassword;
                break;
            case R.id.import_mnemonic_checkbox_txt_url://协议
                gotoWeb(getResources().getString(R.string.create_wallet_txt_right_01), DAppConstants.backUrlHou(mActivity,4));
                break;
            case R.id.import_mnemonic_btn_import://开始导入
                submitPrivateKey();
                break;
            case R.id.import_mnemonic_btn_what://什么是私钥
                gotoWeb(getResources().getString(R.string.import_wallet_item_privatekey_btn_what),DAppConstants.backUrlHou(mActivity,1));
                break;
            case R.id.import_mnemonic_checkbox:
                if (mCheckbox.isChecked()) {
                    mBtnImport.setEnabled(true);
                    mBtnImport.setTextColor(getResources().getColor(R.color.white));
                    mBtnImport.setBackgroundResource(R.drawable.draw_btn_defult_bg_03);
                } else {
                    mBtnImport.setEnabled(false);
                    mBtnImport.setTextColor(getResources().getColor(R.color.color_2E2F47));
                    mBtnImport.setBackgroundResource(R.drawable.draw_btn_defult_bg_01);
                }
                break;
        }
    }

    private void gotoWeb(String title,String urlHou){
        Intent goto_webView = new Intent(mActivity, CommonWebActivity.class);
        goto_webView.putExtra(CommonWebActivity.ACTIVITY_TITLE_INFO, title);
        goto_webView.putExtra(CommonWebActivity.WEBVIEW_LOAD_URL, Config.COMMON_WEB_URL+ urlHou);
        startActivity(goto_webView);
    }

    /**
     * 密码焦点变化监听器
     */
    private class psdOneFousChangeListener implements View.OnFocusChangeListener {

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
                    mEditPsdTwo.setText(mEditPsdTwo.getText().toString().substring(0, mEditPsdTwo.getText().toString().length() - 1));
                    mEditPsdTwo.setSelection(mEditPsdTwo.getText().toString().length());
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

    private void submitPrivateKey() {
        String content = mEditContent.getText().toString();
        if (StrUtil.isEmpty(content)) {
            DappApplication.getInstance().showToast(getResources().getString(R.string.import_wallet_item_privatekey_empty));
            return;
        }

        if (!Bip39WalletUtil.isValidPrivateKey(content)) {
            DappApplication.getInstance().showToast(getResources().getString(R.string.import_wallet_item_privatekey_input_hint_faile));
            return;
        }

        String onePsd = mEditPsdOne.getText().toString();
        String twoPsd = mEditPsdTwo.getText().toString();
        if (StrUtil.isEmpty(onePsd)) {
            DappApplication.getInstance().showToast(getResources().getString(R.string.import_wallet_item_edit_psd_empty));
            mEditPsdOne.requestFocus();
            return;
        }

        if (onePsd.length() < 8) {
            DappApplication.getInstance().showToast(getResources().getString(R.string.import_wallet_item_edit_psd_length));
            return;
        }

        if (!onePsd.equals(twoPsd)) {
            mEditPsdTwo.setText("");
            DappApplication.getInstance().showToast(getResources().getString(R.string.import_wallet_item_edit_psd_and_other));
            return;
        }

        ImportWalletAsyncTask asyncTask = new ImportWalletAsyncTask(mActivity,"",onePsd,content,"",3,mResouceType);
        asyncTask.setOnResultListener(new ImportWalletAsyncTask.OnResultListener() {
            @Override
            public void setOnResultListener(int result) {
                switch (result){
                    case 0://成功
                        DappApplication.getInstance().showSucceseToast(mActivity.getResources().getString(R.string.import_wallet_item_submit_suceese));
                        if (mResouceType == 1) {
                            Intent it_map = new Intent(mActivity, MainmapTwoActivity.class);
                            mActivity.startActivity(it_map);
                        }
                        mActivity.setResult(mActivity.RESULT_OK);
                        mActivity.finish();
                        break;
                    case 1://数据库已存在
                        if(mResouceType == 1){//询问是否将此钱包设置为映射钱包
                            showCreateWalletDilaog();
                        }else{
                            DappApplication.getInstance().showErrorToast(mActivity.getResources().getString(R.string.import_wallet_item_submit_faile_2));
                        }
                        break;
                    case 2://失败
                        DappApplication.getInstance().showErrorToast(mActivity.getResources().getString(R.string.import_wallet_item_submit_faile_3));
                        break;
                    case 3://失败
                        DappApplication.getInstance().showErrorToast(mActivity.getResources().getString(R.string.import_wallet_item_privatekey_input_hint_faile));
                        break;
                    case 4:
                        DappApplication.getInstance().showErrorToast(mActivity.getResources().getString(R.string.create_wallet_submit_faile_03));
                        break;
                    case 5://映射钱包已存在
                        DappApplication.getInstance().showErrorToast(mActivity.getResources().getString(R.string.import_wallet_item_submit_faile_2));
                        break;
                }
            }
        });
        asyncTask.execute();
    }

    private void showCreateWalletDilaog() {
        CommonDilogTool dialogTool = new CommonDilogTool(mActivity);
        dialogTool.show(null, getResources().getString(R.string.main_map_dialog_has_wallet_msg), null,
                getResources().getString(R.string.dailog_psd_btn_cancle), null,
                getResources().getString(R.string.dailog_psd_btn_ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialogTool.dismiss();
                        //修改walletType值 修改为1
                        String content = mEditContent.getText().toString();
                        String onePsd = mEditPsdOne.getText().toString();
                        ChangeWalletTypeAsyncTask asyncTask = new ChangeWalletTypeAsyncTask(mActivity, content, onePsd,2);
                        asyncTask.execute();
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
