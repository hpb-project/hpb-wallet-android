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
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.zhaoxi.Open_source_Android.Config;
import com.zhaoxi.Open_source_Android.DAppConstants;
import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.base.BaseActivity;
import com.zhaoxi.Open_source_Android.common.base.DappApplication;
import com.zhaoxi.Open_source_Android.common.base.BaseFragment;
import com.zhaoxi.Open_source_Android.common.bean.KeystoreBean;
import com.zhaoxi.Open_source_Android.libs.tools.ChangeWalletTypeAsyncTask;
import com.zhaoxi.Open_source_Android.libs.tools.CommonDilogTool;
import com.zhaoxi.Open_source_Android.libs.tools.ImportWalletAsyncTask;
import com.zhaoxi.Open_source_Android.libs.utils.StrUtil;
import com.zhaoxi.Open_source_Android.ui.activity.CommonWebActivity;
import com.zhaoxi.Open_source_Android.ui.activity.ImportWalletActivity;
import com.zhaoxi.Open_source_Android.ui.activity.MainmapTwoActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ImportKeystoreFragment extends BaseFragment {
    Unbinder unbinder;
    @BindView(R.id.import_keystore_edit_content)
    EditText mEditContent;
    @BindView(R.id.import_keystore_edit_psd)
    EditText mEditPsd;
    @BindView(R.id.import_mnemonic_checkbox)
    CheckBox mCheckbox;
    @BindView(R.id.import_mnemonic_btn_import)
    Button mBtnImport;
    @BindView(R.id.import_mnemonic_btn_what)
    TextView mBtnWhat;
    @BindView(R.id.import_keystore_img_eyes)
    ImageView mImgEye;
    @BindView(R.id.import_mnemonic_checkbox_txt_url)
    TextView mTxtCheckUrl;

    private BaseActivity mActivity;
    private boolean showOnePassword = false;
    private int mResouceType;//0 默认导入，1 映射导入
    private boolean useFullScrypt = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_import_keystore, container, false);
        unbinder = ButterKnife.bind(this, contentView);
        mActivity = (BaseActivity) getActivity();
        mResouceType = getArguments().getInt(ImportWalletActivity.RESOUCE_TYPE, 0);
        initViews();
        return contentView;
    }

    private void initViews() {
        mBtnWhat.setText(R.string.import_wallet_item_keystore_btn_what);

        mEditPsd.addTextChangedListener(new TextWatcher() {
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
                        mEditPsd.setText(mEditPsd.getText().toString().substring(0, mEditPsd.getText().toString().length() - 1));
                        mEditPsd.setSelection(mEditPsd.getText().toString().length());
                        return;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    mImgEye.setVisibility(View.VISIBLE);
                } else {
                    mImgEye.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    /**
     * 其他地方给keystore输入框赋值
     *
     * @param content
     */
    public void setContent(String content) {
        mEditContent.setText(content);
        mEditContent.setSelection(content.length());
    }

    @OnClick({R.id.import_mnemonic_checkbox, R.id.import_mnemonic_checkbox_txt_url,
            R.id.import_mnemonic_btn_import, R.id.import_mnemonic_btn_what,
            R.id.import_keystore_img_eyes})
    public void onViewClicked(View view) {
        switch (view.getId()) {
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
            case R.id.import_mnemonic_checkbox_txt_url://服务协议
                gotoWeb(getResources().getString(R.string.create_wallet_txt_right_01), DAppConstants.backUrlHou(mActivity, 4));
                break;
            case R.id.import_mnemonic_btn_import://开始导入
                submitKeystore();
                break;
            case R.id.import_mnemonic_btn_what://什么是keystore
                gotoWeb(getResources().getString(R.string.import_wallet_item_keystore_btn_what), DAppConstants.backUrlHou(mActivity, 2));
                break;
            case R.id.import_keystore_img_eyes:
                String curPsd = mEditPsd.getText().toString();
                if (!showOnePassword) {
                    mImgEye.setImageResource(R.mipmap.icon_edit_psd_y);
                    mEditPsd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    mEditPsd.setSelection(curPsd.length());
                } else {
                    mEditPsd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    mImgEye.setImageResource(R.mipmap.icon_edit_psd_g);
                    mEditPsd.setSelection(curPsd.length());
                }
                showOnePassword = !showOnePassword;
                break;
        }
    }

    private void gotoWeb(String title, String urlHou) {
        Intent goto_webView = new Intent(mActivity, CommonWebActivity.class);
        goto_webView.putExtra(CommonWebActivity.ACTIVITY_TITLE_INFO, title);
        goto_webView.putExtra(CommonWebActivity.WEBVIEW_LOAD_URL, Config.COMMON_WEB_URL + urlHou);
        startActivity(goto_webView);
    }

    private void submitKeystore() {
        String content = mEditContent.getText().toString();
        if (StrUtil.isEmpty(content)) {
            DappApplication.getInstance().showToast(getResources().getString(R.string.import_wallet_item_keystore_empty));
            mEditContent.requestFocus();
            return;
        }

        if (isJSONValid(content)) {
            //验证keystore文件是否可以导入
            KeystoreBean KeystoreInfo = JSON.parseObject(content, KeystoreBean.class);
            if (KeystoreInfo.getCrypto().getKdfparams().getN() > 4096) {
//                useFullScrypt = true;
                DappApplication.getInstance().showToast(getResources().getString(R.string.import_wallet_item_edit_keystore_error_01));
                return;
            }
        } else {
            DappApplication.getInstance().showToast(getResources().getString(R.string.import_wallet_item_edit_keystore_error_01));
            mEditContent.requestFocus();
            return;
        }

        String psd = mEditPsd.getText().toString();
        if (StrUtil.isEmpty(psd)) {
            DappApplication.getInstance().showToast(getResources().getString(R.string.import_wallet_item_edit_psd_empty));
            mEditPsd.requestFocus();
            return;
        }

        ImportWalletAsyncTask asyncTask = new ImportWalletAsyncTask(mActivity, "", psd, content, "", 2, mResouceType, useFullScrypt);
        asyncTask.setOnResultListener(new ImportWalletAsyncTask.OnResultListener() {
            @Override
            public void setOnResultListener(int result) {//0:成功 1：数据库已存在 2：失败 3:密码错误或其他错误
                switch (result) {
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
                        if (mResouceType == 1) {
                            showCreateWalletDilaog();
                        } else {
                            DappApplication.getInstance().showErrorToast(mActivity.getResources().getString(R.string.import_wallet_item_submit_faile_2));
                        }
                        break;
                    case 2://失败
                        DappApplication.getInstance().showErrorToast(mActivity.getResources().getString(R.string.import_wallet_item_submit_faile_3));
                        break;
                    case 3://密码错误或其他错误
                        DappApplication.getInstance().showErrorToast(mActivity.getResources().getString(R.string.export_keystore_item_faile));
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
                        String psd = mEditPsd.getText().toString();
                        ChangeWalletTypeAsyncTask asyncTask = new ChangeWalletTypeAsyncTask(mActivity, content, psd, 1, useFullScrypt);
                        asyncTask.execute();
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    /**
     * 是否是合法的json字符串
     *
     * @param jsonInString
     * @return
     */
    public final static boolean isJSONValid(String jsonInString) {
        if (StrUtil.isCheckAllNum(jsonInString)) {
            return false;
        }
        try {
            JSONObject.parseObject(jsonInString);
        } catch (JSONException ex) {
            try {
                JSONObject.parseArray(jsonInString);
            } catch (JSONException ex1) {
                return false;
            }
            return false;
        }
        return true;
    }

    @Override
    public void onStop() {
        super.onStop();
        if (!"".equals(mEditPsd.getText().toString())) {
            mEditPsd.setText("");
        }
    }
}
