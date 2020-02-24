package com.zhaoxi.Open_source_Android.ui.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.umeng.analytics.MobclickAgent;
import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.base.BaseFragment;
import com.zhaoxi.Open_source_Android.db.CreateDbWallet;
import com.zhaoxi.Open_source_Android.libs.tools.CommonDilogTool;
import com.zhaoxi.Open_source_Android.libs.tools.MainChangePagerMeListener;
import com.zhaoxi.Open_source_Android.libs.utils.ChangeLanguageUtil;
import com.zhaoxi.Open_source_Android.libs.utils.SharedPreferencesUtil;
import com.zhaoxi.Open_source_Android.libs.utils.StrUtil;
import com.zhaoxi.Open_source_Android.libs.utils.SystemInfoUtil;
import com.zhaoxi.Open_source_Android.net.BaseBet.CommonResultListener;
import com.zhaoxi.Open_source_Android.net.Request.GetMsgLastIdRequest;
import com.zhaoxi.Open_source_Android.ui.activity.AboutMeActivity;
import com.zhaoxi.Open_source_Android.ui.activity.AddressBookActivity;
import com.zhaoxi.Open_source_Android.ui.activity.CreateWalletActivity;
import com.zhaoxi.Open_source_Android.ui.activity.HelpCenterActivity;
import com.zhaoxi.Open_source_Android.ui.activity.MainActivity;
import com.zhaoxi.Open_source_Android.ui.activity.MessageCenterActivity;
import com.zhaoxi.Open_source_Android.ui.activity.ScaningActivity;
import com.zhaoxi.Open_source_Android.ui.activity.SystemActivity;
import com.zhaoxi.Open_source_Android.ui.activity.TransationRecordActivity;
import com.zhaoxi.Open_source_Android.ui.activity.WalletManagerActivity;
import com.zhaoxi.Open_source_Android.web3.bean.WalletBean;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.zhaoxi.Open_source_Android.ui.activity.MainActivity.mHandler;

public class MainMeFragment extends BaseFragment {
    Unbinder unbinder;
    @BindView(R.id.main_me_img_share_app)
    ImageView mImgShare;
    @BindView(R.id.main_me_txt_wallet_name)
    TextView mTxtWalletName;
    @BindView(R.id.main_me_txt_msg_spot)
    TextView mTxtSpot;

    private MainActivity mActivity;
    private String mDefultAddress = "";
    private CreateDbWallet mCreateDbWallet;
    private final String mPageName = "MainMeFragment";
    private String mNewLadtMsgId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_main_me, container, false);
        mActivity = (MainActivity) getActivity();
        unbinder = ButterKnife.bind(this, contentView);
        MobclickAgent.openActivityDurationTrack(false);
        mCreateDbWallet = new CreateDbWallet(mActivity);
        mActivity.setChangeViewPager(new MainChangePagerMeListener() {
            @Override
            public void changePager() {
                setWalletName();
            }
        });
        getMsgLastId();
        return contentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(mPageName);
        setWalletName();
    }

    private void setWalletName() {
        mDefultAddress = SharedPreferencesUtil.getSharePreString(mActivity, SharedPreferencesUtil.CHOOSE_WALLET_ADDRESS);
        if (!StrUtil.isEmpty(mDefultAddress)) {
            WalletBean walletBean = mCreateDbWallet.queryWallet(mActivity, mDefultAddress);
            mTxtWalletName.setText(walletBean.getWalletBName());
        } else {
            mTxtWalletName.setText("");
        }
    }

    @OnClick({R.id.main_me_layout_wallet, R.id.main_me_layout_setting,
            R.id.main_me_layout_transaton_reocrd, R.id.main_me_layout_msg,
            R.id.main_me_layout_help, R.id.main_me_layout_about_me,
            R.id.main_me_layout_coldwallet, R.id.main_me_txt_share_app,
            R.id.main_me_layout_address})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.main_me_layout_wallet://管理钱包
                startActivity(new Intent(mActivity, WalletManagerActivity.class));
                break;
            case R.id.main_me_layout_setting://系统管理
                startActivityForResult(new Intent(mActivity, SystemActivity.class), 0x167);
                break;
            case R.id.main_me_layout_transaton_reocrd://交易记录
                //判断是否有钱包
                mDefultAddress = SharedPreferencesUtil.getSharePreString(mActivity, SharedPreferencesUtil.CHOOSE_WALLET_ADDRESS);
                if (StrUtil.isEmpty(mDefultAddress)) {
                    showCreateWalletDilaog();
                    return;
                }
                Intent it_recode = new Intent(mActivity, TransationRecordActivity.class);
                startActivity(it_recode);
                break;
            case R.id.main_me_layout_msg://消息中心
                startActivity(new Intent(mActivity, MessageCenterActivity.class));
                mTxtSpot.setVisibility(View.GONE);
                int type = ChangeLanguageUtil.languageType(mActivity);
                if (mHandler != null) {
                    Message msg = new Message();
                    msg.what = 1;
                    mHandler.sendMessage(msg);
                }
                if (type == 1) {
                    SharedPreferencesUtil.setSharePreString(mActivity, SharedPreferencesUtil.MESSAGE_READ_LASTID_TH, mNewLadtMsgId);
                } else {
                    SharedPreferencesUtil.setSharePreString(mActivity, SharedPreferencesUtil.MESSAGE_READ_LASTID_EN, mNewLadtMsgId);
                }
                break;
            case R.id.main_me_layout_help://帮助中心
                startActivity(new Intent(mActivity, HelpCenterActivity.class));
                break;
            case R.id.main_me_layout_about_me://关于我们
                startActivity(new Intent(mActivity, AboutMeActivity.class));
                break;
            case R.id.main_me_layout_coldwallet://冷钱包
                Intent it_gotoScan = new Intent(mActivity, ScaningActivity.class);
                it_gotoScan.putExtra(ScaningActivity.RESURE_TYPE, 3);
                startActivity(it_gotoScan);
                break;
            case R.id.main_me_txt_share_app://todo 分享
                break;
            case R.id.main_me_layout_address:
                mDefultAddress = SharedPreferencesUtil.getSharePreString(mActivity, SharedPreferencesUtil.CHOOSE_WALLET_ADDRESS);
                if (StrUtil.isEmpty(mDefultAddress)) {
                    showCreateWalletDilaog();
                    return;
                }
                startActivity(new Intent(mActivity, AddressBookActivity.class));
                break;
        }
    }

    private void getMsgLastId() {
        String deviceId = SystemInfoUtil.getDeviceId(mActivity);
        int type = ChangeLanguageUtil.languageType(mActivity);
        new GetMsgLastIdRequest(mActivity, deviceId, type).doRequest(mActivity, new CommonResultListener(mActivity) {
            @Override
            public void onSuccess(JSONArray jsonArray) {
                super.onSuccess(jsonArray);
                //获取本地保存的
                String oldLastId;
                if (type == 1) {
                    oldLastId = SharedPreferencesUtil.getSharePreString(mActivity, SharedPreferencesUtil.MESSAGE_READ_LASTID_TH);
                } else {
                    oldLastId = SharedPreferencesUtil.getSharePreString(mActivity, SharedPreferencesUtil.MESSAGE_READ_LASTID_EN);
                }
                mNewLadtMsgId = jsonArray.get(2).toString();
                if(mTxtSpot!=null){
                    if (StrUtil.isEmpty(oldLastId)) {
                        if(!mNewLadtMsgId.equals("0")){
                            mTxtSpot.setVisibility(View.VISIBLE);
                        }else{
                            mTxtSpot.setVisibility(View.GONE);
                        }
                        return;
                    }
                    if (Integer.valueOf(mNewLadtMsgId) > Integer.valueOf(oldLastId)) {
                        mTxtSpot.setVisibility(View.VISIBLE);
                    } else {
                        mTxtSpot.setVisibility(View.GONE);
                    }
                }
            }
        });
    }

    /**
     * 创建钱包对话框
     */
    private void showCreateWalletDilaog() {
        CommonDilogTool dialogTool = new CommonDilogTool(mActivity);
        dialogTool.show(null, mActivity.getResources().getString(R.string.dialog_common_title_message_nowallet), null,
                mActivity.getResources().getString(R.string.dailog_psd_btn_cancle), null,
                mActivity.getResources().getString(R.string.dialog_common_title_message_go_create_wallet), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(mActivity, CreateWalletActivity.class);
                        startActivity(intent);
                        dialog.dismiss();
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0x167 && resultCode == 0x267) {
            mActivity.finish();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(mPageName);
    }

}
