package com.zhaoxi.Open_source_Android.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhaoxi.Open_source_Android.Config;
import com.zhaoxi.Open_source_Android.DAppConstants;
import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.base.BaseActivity;
import com.zhaoxi.Open_source_Android.common.base.BaseFragment;
import com.zhaoxi.Open_source_Android.common.base.DappApplication;
import com.zhaoxi.Open_source_Android.libs.tools.ImportWalletAsyncTask;
import com.zhaoxi.Open_source_Android.libs.utils.StrUtil;
import com.zhaoxi.Open_source_Android.ui.activity.CommonWebActivity;
import com.zhaoxi.Open_source_Android.web3.utils.Numeric;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ImportColdWalletFragment extends BaseFragment {
    Unbinder unbinder;
    @BindView(R.id.import_coldwallet_edit_address)
    EditText mEditAddress;
    @BindView(R.id.import_coldwallet_edit_name)
    EditText mEditName;
    @BindView(R.id.import_coldwallet_img_delete)
    ImageView mImgDelete;
    @BindView(R.id.import_mnemonic_btn_import)
    Button mBtnImport;
    @BindView(R.id.import_mnemonic_btn_what)
    TextView mBtnWhat;
    @BindView(R.id.import_mnemonic_checkbox)
    CheckBox mCheckbox;

    private BaseActivity mActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_import_coldwallet, container, false);
        unbinder = ButterKnife.bind(this, contentView);
        mActivity = (BaseActivity) getActivity();
        initViews();
        return contentView;
    }

    private void initViews() {
        mBtnWhat.setText(getResources().getString(R.string.import_wallet_item_coldwallet_btn_what));
        mBtnImport.setText(getResources().getString(R.string.import_wallet_item_coldwallet_btn_import));

        mEditAddress.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        mEditAddress.setSingleLine(false);
        mEditAddress.setHorizontallyScrolling(false);
        StrUtil.setEditTextInputSpaChat(mEditAddress);
//        mEditName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if(hasFocus){
//                    mImgDelete.setVisibility(View.VISIBLE);
//                }else{
//                    mImgDelete.setVisibility(View.INVISIBLE);
//                }
//            }
//        });
    }

    /**
     * 其他地方给keystore输入框赋值
     *
     * @param content
     */
    public void setContent(String content) {
        mEditAddress.setText(content);
        mEditAddress.setSelection(content.length());
    }

    @OnClick({R.id.import_coldwallet_img_delete, R.id.import_mnemonic_checkbox,
            R.id.import_mnemonic_btn_import, R.id.import_mnemonic_btn_what,R.id.import_mnemonic_checkbox_txt_url})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.import_coldwallet_img_delete:
                mEditName.setText("");
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
            case R.id.import_mnemonic_btn_import:
                submitColdWallet();
                break;
            case R.id.import_mnemonic_btn_what:
                gotoWeb(getResources().getString(R.string.import_wallet_item_coldwallet_btn_what), DAppConstants.backUrlHou(mActivity, 11));
                break;
            case R.id.import_mnemonic_checkbox_txt_url:
                gotoWeb(getResources().getString(R.string.create_wallet_txt_right_01), DAppConstants.backUrlHou(mActivity, 4));
                break;
        }
    }

    private void submitColdWallet(){
        String address = mEditAddress.getText().toString();
        if (StrUtil.isEmpty(address)) {
            DappApplication.getInstance().showToast(getResources().getString(R.string.import_wallet_item_coldwallet_toast_01));
            return;
        }

        if (!address.startsWith("0x") || address.length() != 42) {
            DappApplication.getInstance().showToast(getResources().getString(R.string.transfer_activity_toast_02));
            return;
        }

        if(!Numeric.isValidAddress(address)){
            DappApplication.getInstance().showToast(getResources().getString(R.string.transfer_activity_toast_02));
            return;
        }

        String name = mEditName.getText().toString();
        if (StrUtil.isEmpty(name)) {
            DappApplication.getInstance().showToast(getResources().getString(R.string.create_wallet_edit_name_empty));
            mEditName.requestFocus();
            return;
        }

        ImportWalletAsyncTask asyncTask = new ImportWalletAsyncTask(mActivity, name, address,4);
        asyncTask.setOnResultListener(new ImportWalletAsyncTask.OnResultListener() {
            @Override
            public void setOnResultListener(int result) {
                switch (result) {
                    case 0://成功
                        DappApplication.getInstance().showSucceseToast(mActivity.getResources().getString(R.string.import_wallet_item_submit_suceese));
                        mActivity.setResult(mActivity.RESULT_OK);
                        mActivity.finish();
                        break;
                    case 1://数据库已存在
                        DappApplication.getInstance().showErrorToast(mActivity.getResources().getString(R.string.import_wallet_item_submit_faile_2));
                        break;
                    case 2://失败
                        DappApplication.getInstance().showErrorToast(mActivity.getResources().getString(R.string.import_wallet_item_submit_faile_3));
                        break;
                    case 4:
                        DappApplication.getInstance().showErrorToast(mActivity.getResources().getString(R.string.create_wallet_submit_faile_03));
                        break;
                }
            }
        });
        asyncTask.execute();
    }

    private void gotoWeb(String title, String urlHou) {
        Intent goto_webView = new Intent(mActivity, CommonWebActivity.class);
        goto_webView.putExtra(CommonWebActivity.ACTIVITY_TITLE_INFO, title);
        goto_webView.putExtra(CommonWebActivity.WEBVIEW_LOAD_URL, Config.COMMON_WEB_URL + urlHou);
        startActivity(goto_webView);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
