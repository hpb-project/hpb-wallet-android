package com.zhaoxi.Open_source_Android.ui.fragment;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.base.BaseActivity;
import com.zhaoxi.Open_source_Android.common.base.DappApplication;
import com.zhaoxi.Open_source_Android.common.base.BaseFragment;
import com.zhaoxi.Open_source_Android.ui.activity.ExportKeystoreActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ExportKeystoreFileFragment extends BaseFragment {
    @BindView(R.id.export_keystore_file_content)
    TextView mFileContent;
    Unbinder unbinder;
    private BaseActivity mActivity;
    private String keystore;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_export_keystore_file, container, false);
        keystore = getArguments().getString(ExportKeystoreActivity.EXPORT_WALLET_KEYSTORE);
        mActivity = (BaseActivity) getActivity();
        unbinder = ButterKnife.bind(this, contentView);
        mFileContent.setText(keystore);
        return contentView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.export_keystore_file_copy)
    public void onViewClicked() {
        //复制地址
        //获取剪贴板管理器：
        ClipboardManager cm = (ClipboardManager)mActivity.getSystemService(Context.CLIPBOARD_SERVICE);
        // 创建普通字符型ClipData
        ClipData mClipData = ClipData.newPlainText("Label", keystore);
        // 将ClipData内容放到系统剪贴板里。
        cm.setPrimaryClip(mClipData);
        DappApplication.getInstance().showToast(getResources().getString(R.string.export_private_key_toast_copy));
    }
}
