package com.zhaoxi.Open_source_Android.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.gyf.immersionbar.ImmersionBar;
import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.base.BaseTitleBarActivity;
import com.zhaoxi.Open_source_Android.common.base.DappApplication;
import com.zhaoxi.Open_source_Android.common.bean.DigitalSignBean;
import com.zhaoxi.Open_source_Android.db.greendao.DigitalSignBeanDao;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DigitalSignDetailActivity extends BaseTitleBarActivity {

    public static final String SIGN_DATA = "signData";

    @BindView(R.id.et_txt_content)
    EditText mEditTxtContent;
    @BindView(R.id.et_sign_content)
    EditText mEditSignContent;
    @BindView(R.id.root_view)
    LinearLayout mRootView;
    private DigitalSignBeanDao mDigitalSignBeanDao;
    private DigitalSignBean mDigitalSignBean;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_digital_sign_detail);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initView() {
        ImmersionBar.with(this)
                .statusBarDarkFont(true, 0.2f)
                .init();
        setTitleBgColor(R.color.white, true);
        setTitle(getResources().getString(R.string.digital_sign_more_sign_record_txt), true);
        mDigitalSignBeanDao = DappApplication.getInstance().getDaoSession().getDigitalSignBeanDao();
    }

    private void initData() {
        mDigitalSignBean = (DigitalSignBean) getIntent().getSerializableExtra(SIGN_DATA);
        if (mDigitalSignBean != null) {
            mEditTxtContent.setText(mDigitalSignBean.getStrContent());
            mEditSignContent.setText(mDigitalSignBean.getSignContent());
        }
    }

    @OnClick({R.id.btn_copy_sign_content, R.id.btn_delete_sign_record})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_copy_sign_content:
                // 复制签名内容
                String digitalSignContent = mEditSignContent.getText().toString();
                copyLable(digitalSignContent);
                break;
            case R.id.btn_delete_sign_record:
                // 删除记录
                if (mDigitalSignBean != null){
                    mDigitalSignBeanDao.delete(mDigitalSignBean);
                    DappApplication.getInstance().showToast(getResources().getString(R.string.activity_digital_sign_detail_delete));
                    finish();
                }
                break;
        }
    }
}
