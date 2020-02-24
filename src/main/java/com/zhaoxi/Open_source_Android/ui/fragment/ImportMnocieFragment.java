package com.zhaoxi.Open_source_Android.ui.fragment;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
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
import com.zhaoxi.Open_source_Android.common.base.BaseFragment;
import com.zhaoxi.Open_source_Android.common.base.DappApplication;
import com.zhaoxi.Open_source_Android.libs.tools.ImportWalletAsyncTask;
import com.zhaoxi.Open_source_Android.libs.tools.PsdTextWatcher;
import com.zhaoxi.Open_source_Android.libs.utils.StrUtil;
import com.zhaoxi.Open_source_Android.ui.activity.CommonWebActivity;
import com.zhaoxi.Open_source_Android.ui.dialog.MnemonicDialog;
import com.zhaoxi.Open_source_Android.web3.crypto.Bip39WalletUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ImportMnocieFragment extends BaseFragment {
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
    @BindView(R.id.import_mnemonic_edit_content_01)
    EditText mEditContent01;
    @BindView(R.id.import_mnemonic_edit_content_02)
    EditText mEditContent02;
    @BindView(R.id.import_mnemonic_edit_content_03)
    EditText mEditContent03;
    @BindView(R.id.import_mnemonic_edit_content_04)
    EditText mEditContent04;
    @BindView(R.id.import_mnemonic_edit_content_05)
    EditText mEditContent05;
    @BindView(R.id.import_mnemonic_edit_content_06)
    EditText mEditContent06;
    @BindView(R.id.import_mnemonic_edit_content_07)
    EditText mEditContent07;
    @BindView(R.id.import_mnemonic_edit_content_08)
    EditText mEditContent08;
    @BindView(R.id.import_mnemonic_edit_content_09)
    EditText mEditContent09;
    @BindView(R.id.import_mnemonic_edit_content_10)
    EditText mEditContent10;
    @BindView(R.id.import_mnemonic_edit_content_11)
    EditText mEditContent11;
    @BindView(R.id.import_mnemonic_edit_content_12)
    EditText mEditContent12;
    private BaseActivity mActivity;
    private boolean showOnePassword = false;
    private boolean showTwoPassword = false;

    private MnemonicDialog mMnemonicDialog;
    private List<String> mMnemonicData;

    private EditText mCurEditText;
    private String mCurMnemonic;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_import_mnemonic, container, false);
        unbinder = ButterKnife.bind(this, contentView);
        mActivity = (BaseActivity) getActivity();
        initViews();
        initDatas();
        return contentView;
    }

    private void initViews() {
        mLayoutContent.setVisibility(View.VISIBLE);
        mEditContent.setVisibility(View.GONE);
        mBtnWhat.setText(R.string.import_wallet_item_zhujici_btn_what);

        mEditPsdOne.setOnFocusChangeListener(new psdOneFousChangeListener());
        mEditPsdOne.addTextChangedListener(new PsdTextWatcher(mActivity, mEditPsdOne, mTxtPsdWarn, mImgEyesOne, mImgStrength));
        mEditPsdTwo.addTextChangedListener(new psdTwoTextWatcher());
    }

    private void initDatas() {
        mMnemonicData = populateWordList(mActivity);

        mCurEditText = mEditContent01;
        addEditViewListener(mEditContent01);
        addEditViewListener(mEditContent02);
        addEditViewListener(mEditContent03);
        addEditViewListener(mEditContent04);
        addEditViewListener(mEditContent05);
        addEditViewListener(mEditContent06);
        addEditViewListener(mEditContent07);
        addEditViewListener(mEditContent08);
        addEditViewListener(mEditContent09);
        addEditViewListener(mEditContent10);
        addEditViewListener(mEditContent11);
        addEditViewListener(mEditContent12);

        ViewTreeObserver vto = mEditContent01.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mEditContent01.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                int width = mEditContent01.getWidth();
                mMnemonicDialog = new MnemonicDialog(mActivity, mCurEditText, width);
                mMnemonicDialog.setCheckMnemonicListener(new MnemonicDialog.CheckMnemonicListener() {
                    @Override
                    public void onCheckListener(EditText editText, String word) {
                        editText.setText(word);
                        editText.setSelection(word.length());
                    }
                });
            }
        });
    }

    @OnClick({R.id.import_mnemonic_img_eyes_one, R.id.import_mnemonic_img_eyes_two,
            R.id.import_mnemonic_btn_import, R.id.import_mnemonic_btn_what,
            R.id.import_mnemonic_checkbox, R.id.import_mnemonic_checkbox_txt_url})
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
                gotoWeb(getResources().getString(R.string.create_wallet_txt_right_01), DAppConstants.backUrlHou(mActivity, 4));
                break;
            case R.id.import_mnemonic_btn_import://开始导入
                submitMnemonic();
                break;
            case R.id.import_mnemonic_btn_what://什么是助记词
                gotoWeb(getResources().getString(R.string.import_wallet_item_zhujici_btn_what), DAppConstants.backUrlHou(mActivity, 3));
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

    private void gotoWeb(String title, String urlHou) {
        Intent goto_webView = new Intent(mActivity, CommonWebActivity.class);
        goto_webView.putExtra(CommonWebActivity.ACTIVITY_TITLE_INFO, title);
        goto_webView.putExtra(CommonWebActivity.WEBVIEW_LOAD_URL, Config.COMMON_WEB_URL + urlHou);
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

    private void submitMnemonic() {
        String m1 = mEditContent01.getText().toString();
        String m2 = mEditContent02.getText().toString();
        String m3 = mEditContent03.getText().toString();
        String m4 = mEditContent04.getText().toString();
        String m5 = mEditContent05.getText().toString();
        String m6 = mEditContent06.getText().toString();
        String m7 = mEditContent07.getText().toString();
        String m8 = mEditContent08.getText().toString();
        String m9 = mEditContent09.getText().toString();
        String m10 = mEditContent10.getText().toString();
        String m11 = mEditContent11.getText().toString();
        String m12 = mEditContent12.getText().toString();

        if (StrUtil.isEmpty(m1) || StrUtil.isEmpty(m2) || StrUtil.isEmpty(m3) || StrUtil.isEmpty(m4) || StrUtil.isEmpty(m5)
                || StrUtil.isEmpty(m6) || StrUtil.isEmpty(m7) || StrUtil.isEmpty(m8) || StrUtil.isEmpty(m9) || StrUtil.isEmpty(m10)
                || StrUtil.isEmpty(m11) || StrUtil.isEmpty(m12)) {
            DappApplication.getInstance().showToast(getResources().getString(R.string.import_wallet_item_edit_mnemonic_empty_01));
            return;
        }

        StringBuilder menmonic = new StringBuilder();
        menmonic.append(m1 + " " + m2 + " " + m3 + " " + m4 + " " + m5 + " " + m6 + " " + m7 + " " + m8 + " " + m9 + " " + m10 + " "
                + m11 + " " + m12);
        Bip39WalletUtil bip39WalletUtil = new Bip39WalletUtil(mActivity);
        mCurMnemonic = menmonic.toString();
        if (!bip39WalletUtil.isMnemonic(mCurMnemonic)) {
            DappApplication.getInstance().showToast(getResources().getString(R.string.import_wallet_item_edit_mnemonic_faile));
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

        ImportWalletAsyncTask asyncTask = new ImportWalletAsyncTask(mActivity, "", onePsd, mCurMnemonic, "", 1, 0);
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
                    case 3://助记词错误
                        DappApplication.getInstance().showErrorToast(mActivity.getResources().getString(R.string.import_wallet_item_edit_mnemonic_faile));
                        break;
                    case 4:
                        DappApplication.getInstance().showErrorToast(mActivity.getResources().getString(R.string.create_wallet_submit_faile_03));
                        break;
                }
            }
        });
        asyncTask.execute();
    }

    private void addEditViewListener(EditText editText) {
        editText.setOnFocusChangeListener(new MnemonicOnFocusChangeListener());
        editText.addTextChangedListener(new MnemonicTextWatcher());
    }

    private class MnemonicOnFocusChangeListener implements View.OnFocusChangeListener {

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                mCurEditText = (EditText) v;
                v.setBackgroundResource(R.drawable.bg_mnemonic_light);
            } else {
                v.setBackgroundResource(R.drawable.bg_mnemonic_defult);
            }
        }
    }

    private class MnemonicTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String txt = s.toString();
            if (txt.length() >= 2) {
                showPopWindow(txt);
            } else {
                if (mMnemonicDialog != null && mMnemonicDialog.isShow()) {
                    mMnemonicDialog.dismiss();
                }
            }
        }
    }

    private void showPopWindow(String start) {
        List<String> data;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            data = filterMnemonics(mMnemonicData, (String word) -> word.startsWith(start));
        } else {
            data = getWordList(start);
        }
        if (data.size() != 0) {
            if (mMnemonicDialog != null)
                mMnemonicDialog.show(data, mCurEditText);
        }
    }

    @TargetApi(Build.VERSION_CODES.N)
    public static List<String> filterMnemonics(List<String> inventory, Predicate<String> p) {
        List<String> result = new ArrayList<>();
        for (String m : inventory) {
            if (p.test(m)) {
                result.add(m);
            }
        }
        return result;
    }

    private List<String> getWordList(String start) {
        List<String> selectData = new ArrayList<>();
        for (int i = 0; i < mMnemonicData.size(); i++) {
            String word = mMnemonicData.get(i);
            if (mMnemonicData.get(i).startsWith(start)) {
                selectData.add(word);
            }
        }
        return selectData;
    }

    private static List<String> populateWordList(Context context) {
        InputStream inputStream = context.getResources().openRawResource(R.raw.en_mnemonic_word_list);
        try {
            return readAllLines(inputStream);
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    private static List<String> readAllLines(InputStream inputStream) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        List<String> data = new ArrayList<>();
        for (String line; (line = br.readLine()) != null; ) {
            data.add(line);
        }
        return data;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
