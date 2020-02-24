package com.zhaoxi.Open_source_Android.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.base.BaseTitleBarActivity;
import com.zhaoxi.Open_source_Android.common.bean.BandingAddressBean;
import com.zhaoxi.Open_source_Android.db.CreateDbWallet;
import com.zhaoxi.Open_source_Android.libs.utils.SharedPreferencesUtil;
import com.zhaoxi.Open_source_Android.ui.AddaddressActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BandAddressDetailsActivity extends BaseTitleBarActivity {
    public static final String ADD_ADDRESS_ID = "BandAddressDetailsActivity.ADD_ADDRESS_ID";
    private static final int REQUEST_CLOUD = 0x005;
    private static final int REQUEST_EDIT = 0x006;

    @BindView(R.id.band_address_details_txt_name)
    TextView mTxtName;
    @BindView(R.id.band_address_details_txt_address)
    TextView mTxtAddress;
    @BindView(R.id.band_address_details_txt_warn_01)
    TextView mTxtWarn01;
    @BindView(R.id.band_address_details_txt_warn_02)
    TextView mTxtWarn02;

    private int mAddressId;
    private CreateDbWallet mCreateDbWallet;
    private boolean isChangeData = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_band_address_details);
        ButterKnife.bind(this);
        mAddressId = getIntent().getIntExtra(ADD_ADDRESS_ID,0);
        if(mAddressId == 0){
            finish();
        }
        mCreateDbWallet = new CreateDbWallet(this);
        setTitle(R.string.main_me_txt_deatils_01);
        setImgBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isChangeData){
                    setResult(RESULT_OK);
                    finish();
                }else{
                    finish();
                }
            }
        });
        showRightImgWithImgListener(R.mipmap.icon_cloud_details_edit, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it_add = new Intent(BandAddressDetailsActivity.this,AddaddressActivity.class);
                it_add.putExtra(AddaddressActivity.ADD_SOURCE,1);
                it_add.putExtra(AddaddressActivity.ADD_ADDRESS_ID,mAddressId);
                startActivityForResult(it_add,REQUEST_EDIT);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //获取数据
        int isOpenColud = SharedPreferencesUtil.getSharePreInt(this,SharedPreferencesUtil.CHANGE_OPENCLOUD_STATUS);
        BandingAddressBean bandingAddressBean;
        if(isOpenColud == 0){//no
            mTxtWarn01.setText(getResources().getString(R.string.main_me_txt_deatils_04));
            mTxtWarn02.setVisibility(View.VISIBLE);
        }else{
            mTxtWarn01.setText(getResources().getString(R.string.main_me_txt_deatils_05));
            mTxtWarn02.setVisibility(View.GONE);
        }
        bandingAddressBean = mCreateDbWallet.queryBandAddress(mAddressId);
        mTxtName.setText(bandingAddressBean.getMark());
        mTxtAddress.setText(bandingAddressBean.getAddressContact());
    }

    @OnClick({R.id.band_address_details_img_copy, R.id.band_address_details_txt_warn_02})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.band_address_details_img_copy:
                String address = mTxtAddress.getText().toString();
                copyLable(address);
                break;
            case R.id.band_address_details_txt_warn_02:
                startActivityForResult(new Intent(this,CloudToggleActivity.class),REQUEST_CLOUD);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CLOUD && resultCode == RESULT_OK){
            setResult(RESULT_OK);
            finish();
        }else if(requestCode == REQUEST_EDIT && resultCode == AddaddressActivity.RECODE_DELETE_CODE){
            setResult(RESULT_OK);
            finish();
        }else if(requestCode == REQUEST_EDIT && resultCode == AddaddressActivity.RECODE_UPDATE_CODE){
            isChangeData = true;
        }
    }
}
