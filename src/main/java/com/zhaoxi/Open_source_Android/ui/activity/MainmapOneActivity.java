package com.zhaoxi.Open_source_Android.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;

import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.base.BaseTitleBarActivity;
import com.zhaoxi.Open_source_Android.db.CreateDbWallet;
import com.zhaoxi.Open_source_Android.libs.tools.CommonDilogTool;
import com.zhaoxi.Open_source_Android.libs.utils.CollectionUtil;
import com.zhaoxi.Open_source_Android.web3.bean.WalletBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainmapOneActivity extends BaseTitleBarActivity {

    @BindView(R.id.activity_mianmap_txt_map_des)
    TextView mTxtMapDes;

    private CreateDbWallet mCreateDbWallet;
    private List<WalletBean> mDbListData = new ArrayList<>();
    private String mHtmldes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainmap);
        ButterKnife.bind(this);
        mCreateDbWallet = new CreateDbWallet(this);
        setTitle(R.string.activity_main_map_txt_title_01,true);
        mHtmldes = "<font size='14px'>"+ getResources().getString(R.string.activity_map_one_des_01) +"</font></p >" +
                "<p><font size='14px'>"+getResources().getString(R.string.activity_map_one_des_02)+"</font></p >" +
                "<p><strong><font size='14px' color='red'>"+getResources().getString(R.string.activity_map_one_des_03)+"</font></strong></p >" +
                "<p><font size='14px'>"+getResources().getString(R.string.activity_map_one_des_04)+"</font></p >" +
                "<p><font size='14px'>"+getResources().getString(R.string.activity_map_one_des_05)+"</font></p >" +
                "<p><font size='14px'>"+getResources().getString(R.string.activity_map_one_des_06)+"</font></p >" +
                "<p><font size='14px'>"+getResources().getString(R.string.activity_map_one_des_07)+"</font></p >" +
                "<p><font size='14px'>"+getResources().getString(R.string.activity_map_one_des_08)+"</font></p >";
        mTxtMapDes.setText(Html.fromHtml(mHtmldes));
        mTxtMapDes.setMovementMethod(ScrollingMovementMethod.getInstance());
    }

    @OnClick({R.id.activity_mianmap_btn_import, R.id.activity_mianmap_txt_gomap})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.activity_mianmap_btn_import:
                Intent go_import = new Intent(this, ImportWalletActivity.class);
                go_import.putExtra(ImportWalletActivity.RESOUCE_TYPE, 1);
                startActivity(go_import);
                break;
            case R.id.activity_mianmap_txt_gomap:
                //判断是否钱包
                mDbListData = mCreateDbWallet.queryAllMapWallet(this);
                if (CollectionUtil.isCollectionEmpty(mDbListData)) {//没有钱包
                    showErrorWallet();
                } else {
                    startActivity(new Intent(this, MainmapTwoActivity.class));
                }
                break;
        }
    }

    private void showErrorWallet() {
        CommonDilogTool dialogTool = new CommonDilogTool(this);
        dialogTool.show(null, getResources().getString(R.string.activity_main_map_txt_01), null,
                null, null,
                getResources().getString(R.string.dailog_psd_btn_ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
    }
}
