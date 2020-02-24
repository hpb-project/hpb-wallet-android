package com.zhaoxi.Open_source_Android.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.base.BaseTitleBarActivity;
import com.zhaoxi.Open_source_Android.common.bean.FlowBean;
import com.zhaoxi.Open_source_Android.common.dialog.CommonWarnDialog;
import com.zhaoxi.Open_source_Android.common.view.AutoWrapViewGroup;
import com.zhaoxi.Open_source_Android.libs.utils.StrUtil;
import com.zhaoxi.Open_source_Android.ui.adapter.FlowViewOneAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 备份助记词 第一步
 *
 * @author zhutt on 2018-06-12
 */
public class ExportMnemonicOneActivity extends BaseTitleBarActivity {
    public static final String MNEMONIC_RESULT = "ExportMnemonicOneActivity.MNEMONIC_RESULT";
    public static final String ADDRESS_RESULT = "ExportMnemonicOneActivity.ADDRESS_RESULT";

    @BindView(R.id.export_mnemonic_txt_content)
    AutoWrapViewGroup mTxtContent;

    private List<FlowBean> mListData = new ArrayList<>();
    private FlowViewOneAdapter mFlowViewAdapter;
    private String mMnemonic;
    private String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_back_up_one);
        ButterKnife.bind(this);
        mMnemonic = getIntent().getStringExtra(MNEMONIC_RESULT);
        address = getIntent().getStringExtra(ADDRESS_RESULT);
        if(StrUtil.isEmpty(mMnemonic)){
            finish();
        }
        initViews();
    }

    private void initViews(){
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);//防止截屏
        setTitle(R.string.wallet_manager_details_txt_export_zhjici,true);

        //将String 转化为List
        String[] array = mMnemonic.split(" ");
        for (int i = 0; i < array.length; i++) {
            FlowBean flowBean = new FlowBean();
            flowBean.setContent(array[i]);
            flowBean.setChecked(false);
            mListData.add(flowBean);
        }

        mFlowViewAdapter = new FlowViewOneAdapter(this, mTxtContent, mListData);
        showWarnDialog();
    }

    private void showWarnDialog() {
        CommonWarnDialog.Builder Bdialog = new CommonWarnDialog.Builder(this);
        Bdialog.setMassage(this.getResources().getString(R.string.salf_diallog_txt_export_mnemonic));
        CommonWarnDialog dialog = Bdialog.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
    }

    @OnClick(R.id.export_mnemonic_txt_next)
    public void onViewClicked() {
        Intent goto_two = new Intent(this, ExportMnemonicTwoActivity.class);
        goto_two.putExtra(MNEMONIC_RESULT, mMnemonic);
        goto_two.putExtra(ADDRESS_RESULT,address);
        startActivityForResult(goto_two,0x890);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 0x090){
            finish();
        }
    }
}
