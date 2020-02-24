package com.zhaoxi.Open_source_Android.ui.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.base.BaseTitleBarActivity;
import com.zhaoxi.Open_source_Android.libs.utils.DateUtilSL;
import com.zhaoxi.Open_source_Android.libs.utils.StrUtil;
import com.zhaoxi.Open_source_Android.net.bean.MeassgeBean;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MessageCenterDetailsActivity extends BaseTitleBarActivity {
    public static final String MESSGE_DATA = "MessageCenterDetailsActivity.data";
    @BindView(R.id.activity_message_center_details_txt_top)
    TextView mTxtTopTitle;
    @BindView(R.id.activity_message_center_details_txt_time)
    TextView mTxtTime;
    @BindView(R.id.activity_message_center_details_txt_content)
    TextView mTxtContent;

    private MeassgeBean.MeassgeInfo mMeassgeInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_center_details);
        ButterKnife.bind(this);
        mMeassgeInfo = (MeassgeBean.MeassgeInfo) getIntent().getSerializableExtra(MESSGE_DATA);
        if(mMeassgeInfo == null){
            finish();
        }
        initViews();
    }

    private void initViews() {
        setTitle(R.string.activity_message_center_details_title,true);
        mTxtTopTitle.setText(mMeassgeInfo.getTitle());
        mTxtTime.setText(DateUtilSL.dateToStrymdhms2(DateUtilSL.getDateByLong(mMeassgeInfo.getUpdatedAt(),2)));
        String content = StrUtil.isEmpty(mMeassgeInfo.getContent())?mMeassgeInfo.getContent():mMeassgeInfo.getContent().replace("<br />","\n");
        mTxtContent.setText(content);
    }
}
