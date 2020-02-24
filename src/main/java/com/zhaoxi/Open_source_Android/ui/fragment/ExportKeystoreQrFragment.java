package com.zhaoxi.Open_source_Android.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.base.BaseActivity;
import com.zhaoxi.Open_source_Android.common.base.BaseFragment;
import com.zhaoxi.Open_source_Android.libs.tools.ExportKeysotreQrAsyncTask;
import com.zhaoxi.Open_source_Android.ui.activity.ExportKeystoreActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ExportKeystoreQrFragment extends BaseFragment {
    @BindView(R.id.export_keystore_qr_img)
    ImageView mQrImg;
    @BindView(R.id.export_keystore_qr_layout)
    LinearLayout mLayoutBtn;
    @BindView(R.id.export_keystore_qr_btn_show)
    Button mBtnShowQr;

    Unbinder unbinder;
    private BaseActivity mActivity;
    private String keystore;
    private boolean isShowQr = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_export_keystore_qr, container, false);
        keystore = getArguments().getString(ExportKeystoreActivity.EXPORT_WALLET_KEYSTORE);
        mActivity = (BaseActivity) getActivity();
        unbinder = ButterKnife.bind(this, contentView);
        return contentView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.export_keystore_qr_btn_show)
    public void onViewClicked() {
        if (!isShowQr) {
            ExportKeysotreQrAsyncTask asyncTask = new ExportKeysotreQrAsyncTask(mActivity, mQrImg, mLayoutBtn, keystore);
            asyncTask.execute();
            mBtnShowQr.setText(getResources().getString(R.string.export_keystore_tip_03));
            isShowQr = true;
        } else {
            mQrImg.setVisibility(View.GONE);
            mLayoutBtn.setVisibility(View.VISIBLE);
            mBtnShowQr.setText(getResources().getString(R.string.export_keystore_tip_02));
            isShowQr = false;
        }
    }
}
