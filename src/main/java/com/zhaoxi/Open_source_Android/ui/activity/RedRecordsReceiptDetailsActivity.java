package com.zhaoxi.Open_source_Android.ui.activity;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.base.BaseTitleBarActivity;
import com.zhaoxi.Open_source_Android.common.view.MyTextView;
import com.zhaoxi.Open_source_Android.libs.utils.SlinUtil;
import com.zhaoxi.Open_source_Android.libs.utils.StrUtil;
import com.zhaoxi.Open_source_Android.net.bean.RedRecordBean;

import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RedRecordsReceiptDetailsActivity extends BaseTitleBarActivity {
    public static final String RED_RECORD_BEAN = "RED_RECORD_BEAN";

    @BindView(R.id.item_redrecords_details_txt_top_type)
    TextView mTxtRedtype;
    @BindView(R.id.item_redrecords_details_txt_top_address)
    MyTextView mTxtTopAddress;
    @BindView(R.id.redrecords_receipt_detials_txt_top_des)
    TextView mTxtTopDes;
    @BindView(R.id.redrecords_receipt_detials_txt_top_money)
    TextView mTxtTopMoney;
    @BindView(R.id.redrecords_receipt_detials_txt_status)
    TextView mTxtStatus;
    @BindView(R.id.redrecords_receipt_detials_layout_content)
    LinearLayout mLayoutContent;

    private RedRecordBean.RedBean mRedBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_red_records_receipt_details);
        ButterKnife.bind(this);
        setTitle(getResources().getString(R.string.activity_red_record_txt_05), true);
        mRedBean = (RedRecordBean.RedBean) getIntent().getSerializableExtra(RED_RECORD_BEAN);
        if (mRedBean == null) {
            return;
        }
        initDatas();
    }

    private void initDatas() {
        mTxtRedtype.setText(mRedBean.getRedPacketType());

        mTxtRedtype.setText("1".equals(mRedBean.getRedPacketType()) ? getResources().getString(R.string.activity_red_record_txt_09_2) :
                getResources().getString(R.string.activity_red_record_txt_09_1));//1-普通，2-拼手气红包
        mTxtTopAddress.setText(getResources().getString(R.string.activity_red_record_txt_06,
                StrUtil.addressFilte10r(mRedBean.getFromAddr())));
        BigDecimal money = new BigDecimal("" + mRedBean.getCoinValue());
        mTxtTopMoney.setText(SlinUtil.tailClearAll(this,SlinUtil.formatNumFromWeiS(this, money)));
        mTxtTopDes.setText(mRedBean.getTitle());
        mTxtStatus.setText(getResources().getString(R.string.activity_red_send_txt_21) + " " + StrUtil.addressFilte10r(mRedBean.getToAddr()));
    }
}
