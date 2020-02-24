package com.zhaoxi.Open_source_Android.ui.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.transition.Fade;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.base.BaseTitleBarActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ImgCatchActivity extends BaseTitleBarActivity {

    public static final String IMG_URL = "ImgCatchActivity.imgUrl";

    @BindView(R.id.iv_token_id_header)
    ImageView mIvTokenIdHeader;
    @BindView(R.id.iv_img_catch_close)
    ImageView mImgClose;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setExitTransition(new Fade());
        getWindow().setEnterTransition(new Fade());
        setContentView(R.layout.activity_img_catch);
        ButterKnife.bind(this);
        initView();
    }

    @OnClick({R.id.iv_img_catch_close, R.id.iv_token_id_header})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_img_catch_close:
            case R.id.iv_token_id_header:
                onBackPressed();
                break;
        }
    }

    private void initView() {
        String url = getIntent().getStringExtra(IMG_URL);
        Glide.with(this).load(url).centerCrop().placeholder(R.mipmap.icon_default_header).error(R.mipmap.icon_default_header).into(mIvTokenIdHeader);
    }

}
