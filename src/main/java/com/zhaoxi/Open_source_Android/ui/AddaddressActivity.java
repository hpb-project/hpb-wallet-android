package com.zhaoxi.Open_source_Android.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.base.BaseTitleBarActivity;
import com.zhaoxi.Open_source_Android.common.base.DappApplication;
import com.zhaoxi.Open_source_Android.common.bean.BandingAddressBean;
import com.zhaoxi.Open_source_Android.db.CreateDbWallet;
import com.zhaoxi.Open_source_Android.libs.utils.SharedPreferencesUtil;
import com.zhaoxi.Open_source_Android.libs.utils.StrUtil;
import com.zhaoxi.Open_source_Android.net.BaseBet.CommonResultListener;
import com.zhaoxi.Open_source_Android.net.Request.PushBookRequest;
import com.zhaoxi.Open_source_Android.ui.activity.ScaningActivity;
import com.zhaoxi.Open_source_Android.web3.utils.Numeric;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddaddressActivity extends BaseTitleBarActivity {
    public static final int REQUEST_CODE = 0x003;
    public static final int RECODE_DELETE_CODE = 0x013;
    public static final int RECODE_UPDATE_CODE = 0x023;
    public static final String ADD_SOURCE = "AddaddressActivity.ADD_SOURCE";
    public static final String ADD_ADDRESS_ID = "AddaddressActivity.ADD_ADDRESS_ID";
    public static final String ADD_ADDRESS = "AddaddressActivity.ADD_ADDRESS";

    @BindView(R.id.add_address_edit_name)
    EditText mEditName;
    @BindView(R.id.add_address_txt_name_num)
    TextView mTxtNameNum;
    @BindView(R.id.add_address_edit_address)
    EditText mEditAddress;
    @BindView(R.id.add_address_img_scan_address)
    ImageView addAddressImgScanAddress;
    @BindView(R.id.add_address_btn_delete_address)
    TextView mTxtDelete;
    @BindView(R.id.add_address_btn_save_address)
    Button mBtnSaveAddress;

    private CreateDbWallet mCreateDbWallet;
    private BandingAddressBean mBandingAddressBean;
    private int mSourceType = 0;//0 添加地址 1：编辑地址
    private int isOpenCloud = 0;//未开启
    private String mAddressName, mAddressContent,mNewAddress;
    private int mAddressId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addaddress);
        ButterKnife.bind(this);
        mSourceType = getIntent().getIntExtra(ADD_SOURCE, 0);
        initViews();
    }

    private void initViews() {
        mCreateDbWallet = new CreateDbWallet(this);
        mEditAddress.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        mEditAddress.setSingleLine(false);
        mEditAddress.setHorizontallyScrolling(false);
        mEditName.addTextChangedListener(new NameTextWatcher());
        StrUtil.setEditTextInhibitInputSpaChat(mEditName);
        StrUtil.setEditTextInputSpaChat(mEditAddress);
        if (mSourceType == 0) {
            setTitle(R.string.main_me_txt_edit_01,true);
            mTxtDelete.setVisibility(View.GONE);
            mBtnSaveAddress.setText(getResources().getString(R.string.dailog_psd_btn_ok));
            mNewAddress = getIntent().getStringExtra(ADD_ADDRESS);
            if(!StrUtil.isEmpty(mNewAddress)){
                mEditAddress.setText(mNewAddress);
            }
        } else {
            mAddressId = getIntent().getIntExtra(ADD_ADDRESS_ID, 0);
            if (mAddressId == 0) finish();
            BandingAddressBean bandingAddressBean = mCreateDbWallet.queryBandAddress(mAddressId);
            setTitle(R.string.main_me_txt_edit_02,true);
            mAddressName = bandingAddressBean.getMark();
            mAddressContent = bandingAddressBean.getAddressContact();
            mEditName.setText(mAddressName);
            mEditName.setSelection(mAddressName.length());
            mEditAddress.setText(mAddressContent);
            mTxtDelete.setVisibility(View.VISIBLE);
            mBtnSaveAddress.setText(getResources().getString(R.string.change_wallet_psd_btn_submit));
        }
    }

    @OnClick({R.id.add_address_img_scan_address, R.id.add_address_btn_save_address,
            R.id.add_address_btn_delete_address})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.add_address_img_scan_address:
                Intent it_gotoScan = new Intent(AddaddressActivity.this, ScaningActivity.class);
                it_gotoScan.putExtra(ScaningActivity.RESURE_TYPE, 2);
                startActivityForResult(it_gotoScan, REQUEST_CODE);
                break;
            case R.id.add_address_btn_save_address:
                saveAddress();
                break;
            case R.id.add_address_btn_delete_address://删除地址
                int result = mCreateDbWallet.deleteBandAddress(mAddressId);
                if (result == 0) {
                    DappApplication.getInstance().showToast(getResources().getString(R.string.main_me_txt_edit_14));
                    isOpenCloud = SharedPreferencesUtil.getSharePreInt(this, SharedPreferencesUtil.CHANGE_OPENCLOUD_STATUS);
                    if (isOpenCloud != 0) {
                        handleBookData(2);
                    }else{
                        setResult(RECODE_DELETE_CODE);
                        finish();
                    }
                } else {
                    DappApplication.getInstance().showToast(getResources().getString(R.string.main_me_txt_edit_15));
                }
                break;
        }
    }

    private void saveAddress() {
        String name = mEditName.getText().toString();
        if (StrUtil.isEmpty(name)) {
            DappApplication.getInstance().showToast(getResources().getString(R.string.main_me_txt_edit_06));
            return;
        }
        if (name.length() > 20) {
            DappApplication.getInstance().showToast(getResources().getString(R.string.main_me_txt_edit_07));
            return;
        }
        mBandingAddressBean = new BandingAddressBean();
        mBandingAddressBean.setMark(name);

        String address = mEditAddress.getText().toString();
        if (StrUtil.isEmpty(address)) {
            DappApplication.getInstance().showToast(getResources().getString(R.string.main_me_txt_edit_08));
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

        mBandingAddressBean.setAddressContact(address);
        isOpenCloud = SharedPreferencesUtil.getSharePreInt(this, SharedPreferencesUtil.CHANGE_OPENCLOUD_STATUS);
        if (mSourceType == 0) {
            int result = mCreateDbWallet.insertBandAddress(mBandingAddressBean);
            if (result == 0) {
                DappApplication.getInstance().showToast(getResources().getString(R.string.main_me_txt_edit_09));
                if (isOpenCloud != 0) {//上传数据
                    handleBookData(mSourceType);
                }else{
                    setResult(RESULT_OK);
                    finish();
                }
            } else if (result == 2) {
                DappApplication.getInstance().showToast(getResources().getString(R.string.main_me_txt_edit_10));
            } else {
                DappApplication.getInstance().showToast(getResources().getString(R.string.main_me_txt_edit_11));
            }
        } else {
            int result = mCreateDbWallet.updateColBandAddress(mAddressId, name, address);
            if (result == 0) {
                DappApplication.getInstance().showToast(getResources().getString(R.string.main_me_txt_edit_12));
                if (isOpenCloud != 0) {//上传数据
                    handleBookData(mSourceType);
                }else{
                    setResult(RECODE_UPDATE_CODE);
                    finish();
                }
            } else if (result == 2) {
                DappApplication.getInstance().showToast(getResources().getString(R.string.main_me_txt_edit_13));
            } else {
                DappApplication.getInstance().showToast(getResources().getString(R.string.main_me_txt_edit_11));
            }
        }
    }

    private void handleBookData(int type) {
        showProgressDialog();
        mCreateDbWallet = new CreateDbWallet(this);
        List<String> listAddress = mCreateDbWallet.queryAllAddress();
        List<BandingAddressBean> bookList = mCreateDbWallet.queryAllBandAddress();
        String addressList = JSON.toJSONString(bookList);
        new PushBookRequest(listAddress, addressList.replace("\"", "'"))
                .doRequest(this, new CommonResultListener(this) {
            @Override
            public void onSuccess(JSONArray jsonArray) {
                super.onSuccess(jsonArray);
                dismissProgressDialog();
                if (type == 0) {
                    setResult(RESULT_OK);
                } else if(type == 1) {
                    setResult(RECODE_UPDATE_CODE);
                }else{
                    setResult(RECODE_DELETE_CODE);
                }
                finish();
            }
        });
    }

    private class NameTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            int length = s.length();
            mTxtNameNum.setText("" + (20 - length));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == ScaningActivity.RESULT_CODE && data != null) {
            String resultContent = data.getStringExtra(ScaningActivity.RESULT_CONTENT);
            mEditAddress.setText(resultContent);
            mEditAddress.setSelection(resultContent.length());
        }
    }
}
