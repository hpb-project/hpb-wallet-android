package com.zhaoxi.Open_source_Android.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;

import com.alibaba.fastjson.JSONArray;
import com.zhaoxi.Open_source_Android.DAppConstants;
import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.base.BaseTitleBarActivity;
import com.zhaoxi.Open_source_Android.common.base.DappApplication;
import com.zhaoxi.Open_source_Android.common.view.SelectGridView;
import com.zhaoxi.Open_source_Android.libs.utils.FileManager;
import com.zhaoxi.Open_source_Android.libs.utils.PictureUtil;
import com.zhaoxi.Open_source_Android.libs.utils.StrUtil;
import com.zhaoxi.Open_source_Android.net.BaseBet.NetResultCallBack;
import com.zhaoxi.Open_source_Android.net.Request.PostFeekbackRequest;
import com.zhaoxi.Open_source_Android.ui.adapter.GridPhotoAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerActivity;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class FeekBackActivity extends BaseTitleBarActivity implements CompoundButton.OnCheckedChangeListener
        , EasyPermissions.PermissionCallbacks {
    private static final int REQUEST_CODE_QRCODE_PERMISSIONS = 1;
    private static final int REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY = 666;

    @BindView(R.id.activity_feekbac_tab_01)
    RadioButton mFeekbacTab01;
    @BindView(R.id.activity_feekbac_tab_02)
    RadioButton mFeekbacTab02;
    @BindView(R.id.activity_feekbac_tab_03)
    RadioButton mFeekbacTab03;
    @BindView(R.id.activity_feekback_edit_theme)
    EditText mEditTheme;
    @BindView(R.id.activity_feekback_edit_content)
    EditText mEditContent;
    @BindView(R.id.activity_feekback_select_photo)
    SelectGridView mGirdView;
    @BindView(R.id.activity_feekback_edit_lianxiway)
    EditText mEditLianxiway;
    @BindView(R.id.activity_feekback_content_submit)
    Button activityFeekbackContentSubmit;

    private int mFeekbackType = 0;
    private GridPhotoAdapter mAdapter;
    private ArrayList<String> mSelectedImage = new ArrayList<>();//图片地址
    private ArrayList<String> mFilePaths;//上传地址

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feek_back);
        ButterKnife.bind(this);
        initViews();
    }

    private void initViews() {
        setTitle(R.string.activity_feekcack_title,true);
        mFeekbacTab01.setOnCheckedChangeListener(this);
        mFeekbacTab02.setOnCheckedChangeListener(this);
        mFeekbacTab03.setOnCheckedChangeListener(this);
        mAdapter = new GridPhotoAdapter(this, mSelectedImage);
        mAdapter.setSelectDD(new GridPhotoAdapter.SelectImageGo() {
            @Override
            public void onGoSelect() {
                gotoPhotos();
            }
        });
        mGirdView.setAdapter(mAdapter);
    }

    @OnClick(R.id.activity_feekback_content_submit)
    public void onViewClicked() {
        String content = mEditContent.getText().toString();
        if (StrUtil.isEmpty(content)) {
            DappApplication.getInstance().showToast(getResources().getString(R.string.activity_feekcack_txt_09));
            return;
        }

        String xiWay = mEditLianxiway.getText().toString();
        if (StrUtil.isEmpty(xiWay)) {
            DappApplication.getInstance().showToast(getResources().getString(R.string.activity_feekcack_txt_10));
            return;
        }

        String title = mEditTheme.getText().toString();
        mFilePaths = new ArrayList<>();
        for (int i = 0; i < mSelectedImage.size(); i++) {
            String path = mSelectedImage.get(i);
            String compressFileName = DAppConstants.IMAGE_FILE_NAME_NEW_RRPORT + i + ".jpg";
            String depath = FileManager.compressImagePath(compressFileName);
            final String compressImage = PictureUtil.compressImage(path, depath, 30);

            final File compressedPic = new File(compressImage);
            if (compressedPic.exists()) {
                mFilePaths.add(compressImage);
            } else {//直接上传
                mFilePaths.add(depath);
            }
        }
        showProgressDialog();
        new PostFeekbackRequest(mFeekbackType, title, content, xiWay, mFilePaths).doRequestPairs(this,new NetResultCallBack() {
            @Override
            public void onSuccess(JSONArray jsonArray) {
                dismissProgressDialog();
                //删除指定的文件
                for (int k = 0; k < mFilePaths.size(); k++) {
                    FileManager.deleteComFile(mFilePaths.get(k));
                }
                DappApplication.getInstance().showToast(getResources().getString(R.string.activity_feekcack_txt_11));
                finish();
            }

            @Override
            public void onError(String error) {
                dismissProgressDialog();
                //删除指定的文件
                for (int k = 0; k < mFilePaths.size(); k++) {
                    FileManager.deleteComFile(mFilePaths.get(k));
                }
                DappApplication.getInstance().showToast(error);
            }
        });
    }

    /**
     * 选择照片
     */
    private void gotoPhotos() {
        Intent photoPickerIntent = new BGAPhotoPickerActivity.IntentBuilder(this)
                .cameraFileDir(null)
                .maxChooseCount(DAppConstants.DEFULT_SELECT_PHOTO_NUM)
                .selectedPhotos(mSelectedImage)
                .pauseOnScroll(false)
                .build();
        startActivityForResult(photoPickerIntent, REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY) {
            mSelectedImage.clear();
            List<String> imagesPath = BGAPhotoPickerActivity.getSelectedPhotos(data);
            mSelectedImage.addAll(imagesPath);
            mAdapter.refresh(mSelectedImage);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            switch (buttonView.getId()) {
                case R.id.activity_feekbac_tab_01:
                    mFeekbackType = 0;
                    break;
                case R.id.activity_feekbac_tab_02:
                    mFeekbackType = 1;
                    break;
                case R.id.activity_feekbac_tab_03:
                    mFeekbackType = 2;
                    break;
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        requestCodeQRCodePermissions();
    }

    /* 设置权限开始   */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
    }

    @AfterPermissionGranted(REQUEST_CODE_QRCODE_PERMISSIONS)
    private void requestCodeQRCodePermissions() {
        String[] perms = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE};
        if (!EasyPermissions.hasPermissions(this, perms)) {
            EasyPermissions.requestPermissions(this, getResources().getString(R.string.permissions_txt_photo), REQUEST_CODE_QRCODE_PERMISSIONS, perms);
        }
    }
    /* 设置权限结束   */
}
