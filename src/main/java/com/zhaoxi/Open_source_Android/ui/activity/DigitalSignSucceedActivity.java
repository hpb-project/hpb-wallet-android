package com.zhaoxi.Open_source_Android.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gyf.immersionbar.ImmersionBar;
import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.base.BaseTitleBarActivity;
import com.zhaoxi.Open_source_Android.common.base.DappApplication;
import com.zhaoxi.Open_source_Android.common.bean.DigitalSignBean;
import com.zhaoxi.Open_source_Android.common.dialog.CommonPopupWindowDialog;
import com.zhaoxi.Open_source_Android.db.greendao.DigitalSignBeanDao;
import com.zhaoxi.Open_source_Android.libs.utils.DateUtilSL;
import com.zhaoxi.Open_source_Android.libs.utils.SharedPreferencesUtil;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 数字签名
 */
public class DigitalSignSucceedActivity extends BaseTitleBarActivity {

    public static final String SIGN_CONTENT = "signContent";
    public static final String SIGN_STR_TYPE = "signType";

    @BindView(R.id.et_sign_content)
    EditText mEtSignContent;
    @BindView(R.id.root_view)
    LinearLayout mRootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_digital_sign_succeed);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        ImmersionBar.with(this)
                .statusBarDarkFont(true, 0.2f)
                .init();
        setTitleBgColor(R.color.white, true);
        setTitle(getResources().getString(R.string.fragment_main_news_digital_sign_txt), true);
        showRightTxtWithTextListener(getResources().getString(R.string.main_find_txt_04), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 更多
                showMoreDialog();
            }
        });

        DigitalSignBeanDao digitalSignBeanDao = DappApplication.getInstance().getDaoSession().getDigitalSignBeanDao();
        String strContent = SharedPreferencesUtil.getSharePreString(this, SharedPreferencesUtil.DIGITAL_SIGN_STR_CONTENT);
        String result = getIntent().getStringExtra(SIGN_CONTENT);
        DigitalSignBean digitalSignBean = new DigitalSignBean();
        digitalSignBean.setStrContent(strContent);
        digitalSignBean.setSignContent(result);
        mEtSignContent.setText(result);
        digitalSignBean.setSignDateTime(DateUtilSL.dateToStrymdhm(new Date()));
        digitalSignBeanDao.save(digitalSignBean);
    }

    @OnClick(R.id.btn_copy_sign_content)
    public void onClick(View view) {
        if (view.getId() == R.id.btn_copy_sign_content) {
            // 复制签名内容
            copyLable(mEtSignContent.getText().toString());
        }
    }


    private void showMoreDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.view_digital_sign_more_layout, null, false);
        TextView tvSignRecord = view.findViewById(R.id.tv_sign_record);
        TextView tvDismiss = view.findViewById(R.id.tv_dismiss);

        CommonPopupWindowDialog dialog = new CommonPopupWindowDialog(this, view);
        dialog.show(mRootView);

        tvSignRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                // 签名记录
                startActivity(new Intent(DigitalSignSucceedActivity.this, DigitalSignRecordActivity.class));
            }
        });

        tvDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 取消
                dialog.dismiss();
            }
        });

    }


}
