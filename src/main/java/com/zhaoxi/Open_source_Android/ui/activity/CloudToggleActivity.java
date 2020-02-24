package com.zhaoxi.Open_source_Android.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.base.BaseTitleBarActivity;
import com.zhaoxi.Open_source_Android.common.bean.BandingAddressBean;
import com.zhaoxi.Open_source_Android.db.CreateDbWallet;
import com.zhaoxi.Open_source_Android.libs.utils.SharedPreferencesUtil;
import com.zhaoxi.Open_source_Android.net.BaseBet.CommonResultListener;
import com.zhaoxi.Open_source_Android.net.Request.PullBookRequest;
import com.zhaoxi.Open_source_Android.net.Request.PushBookRequest;
import com.zhaoxi.Open_source_Android.net.bean.BookBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CloudToggleActivity extends BaseTitleBarActivity {

    @BindView(R.id.cloud_toggle_img_change)
    ImageView mImgChange;

    private int isOpenCloud = 0;//未开启
    private int mCloudStuats = 0;
    private boolean isCaozuo = false;
    private CreateDbWallet mCreateDbWallet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cloud_toggle);
        ButterKnife.bind(this);
        initViews();
    }

    private void initViews(){
        setTitle(R.string.main_me_txt_address_01);
        setImgBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isCaozuo){
                    setResult(RESULT_OK);
                    finish();
                }else finish();
            }
        });

        isOpenCloud = SharedPreferencesUtil.getSharePreInt(this,SharedPreferencesUtil.CHANGE_OPENCLOUD_STATUS);
        if(isOpenCloud == 0){
            mCloudStuats = 0;
            mImgChange.setImageResource(R.mipmap.icon_hand_toggle_close);
        }else{
            mCloudStuats = 1;
            mImgChange.setImageResource(R.mipmap.icon_hand_toggle_open);
        }
    }

    @OnClick(R.id.cloud_toggle_img_change)
    public void onViewClicked() {
        if(mCloudStuats == 0){//开启  //先拉去重在push
            mCloudStuats = 1;
            mImgChange.setImageResource(R.mipmap.icon_hand_toggle_open);
            handleBookData();
        }else{//关闭
            isCaozuo = true;
            mCloudStuats = 0;
            mImgChange.setImageResource(R.mipmap.icon_hand_toggle_close);
        }
        SharedPreferencesUtil.setSharePreInt(this,SharedPreferencesUtil.CHANGE_OPENCLOUD_STATUS,mCloudStuats);
    }

    private void handleBookData(){
        showProgressDialog();
        mCreateDbWallet = new CreateDbWallet(this);
        List<String> listAddress = mCreateDbWallet.queryAllAddress();
        new PullBookRequest(listAddress).doRequest(this, new CommonResultListener(this) {
            @Override
            public void onSuccess(JSONArray jsonArray) {
                BookBean addressBean = JSON.parseObject(jsonArray.get(2).toString(), BookBean.class);
                List<BandingAddressBean> dateList = addressBean.getData();
                int result = mCreateDbWallet.insertAllBandAddress(dateList);
                if(result == 0){
                    dismissProgressDialog();
                    List<BandingAddressBean> bookList = mCreateDbWallet.queryAllBandAddress();
                    String addressList = JSON.toJSONString(bookList);
                    pushBookData(listAddress,addressList.replace("\"", "'"));
                }
            }
        });
    }

    private void pushBookData(List<String> addresss, String bookList){
        new PushBookRequest(addresss,bookList).doRequest(this, new CommonResultListener(this){
            @Override
            public void onSuccess(JSONArray jsonArray) {
                super.onSuccess(jsonArray);
                setResult(RESULT_OK);
                finish();
            }
        });
    }
}
