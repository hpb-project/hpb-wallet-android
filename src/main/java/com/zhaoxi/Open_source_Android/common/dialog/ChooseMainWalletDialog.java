package com.zhaoxi.Open_source_Android.common.dialog;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zhaoxi.Open_source_Android.DAppConstants;
import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.base.BaseActivity;
import com.zhaoxi.Open_source_Android.common.base.DappApplication;
import com.zhaoxi.Open_source_Android.libs.utils.NetUtil;
import com.zhaoxi.Open_source_Android.libs.utils.SharedPreferencesUtil;
import com.zhaoxi.Open_source_Android.libs.utils.SlinUtil;
import com.zhaoxi.Open_source_Android.ui.activity.CreateWalletActivity;
import com.zhaoxi.Open_source_Android.ui.adapter.ChooseMainWalletAdapter;
import com.zhaoxi.Open_source_Android.web3.bean.WalletBean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * des:切换钱包
 * Created by ztt on 2018/6/14.
 */
@Deprecated
public class ChooseMainWalletDialog extends LinearLayout {
    private BaseActivity mContext;
    private ListView mListView;
    private View mCurrentFocusView;
    private LinearLayout mCreateLayout,mEmptyLayout;
    private ImageView mImgEyesStatus;
    private TextView mTxtAllMoney;
    private LinearLayout mLayoutEyes;

    private PopupWindow mPopupWindow;
    private List<WalletBean> mListData;
    private ChooseMainWalletAdapter mAdapter;
    private int mEyesStatus;
    private BigDecimal mAllMoney =new BigDecimal("0");
    /*默认为头像切图*/
    public ChooseMainWalletDialog(BaseActivity activity, View currentFocusView, boolean isShowCreateWallet) {
        super(activity);
        mContext = activity;
        mCurrentFocusView = currentFocusView;
        mListData = new ArrayList<>();
        LayoutInflater.from(activity).inflate(R.layout.view_choose_main_wallet_dialog, this);

        mListView = findViewById(R.id.item_choose_main_wallet_list);
//        mImgEyesStatus = findViewById(R.id.item_choose_main_wallet_eyes);
//        mEmptyLayout = findViewById(R.id.item_choose_main_wallet_layout_emppty);
        mEmptyLayout.setOnClickListener(new EmptyClickListener());
        mCreateLayout = findViewById(R.id.item_choose_main_wallet_create_wallet);

        mTxtAllMoney = findViewById(R.id.item_choose_main_wallet_txt_allmoney);
        mLayoutEyes = findViewById(R.id.item_choose_main_wallet_layout_eyes);

        if(!isShowCreateWallet){
            mCreateLayout.setVisibility(View.GONE);
        }else{
            mCreateLayout.setVisibility(View.VISIBLE);
        }
        mLayoutEyes.setOnClickListener(new CreateClickListener());
        mCreateLayout.setOnClickListener(new CreateClickListener());
        mAdapter= new ChooseMainWalletAdapter(mContext, mListData);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(!NetUtil.isNetworkAvalible(DappApplication.getInstance())){
                    DappApplication.getInstance().showToast(mContext.getResources().getString(R.string.exception_netword_error));
                }else{
                    mOnChooseWalletListener.setOnChooseWalletListener(mListData.get(position));
                }
                dismiss();
            }
        });
        initDialogPopupWindow();
    }

    private void initData(List<WalletBean> listData){
        mAllMoney =new BigDecimal("0");
        mEyesStatus = SharedPreferencesUtil.getSharePreInt(mContext, DAppConstants.MONEY_EYES_STATUS);
        if(mListData.size() != 0){
            mListData.clear();
        }
        for (int i = 0; i < listData.size(); i++) {
            WalletBean walletBean = listData.get(i);
            mAllMoney = mAllMoney.add(new BigDecimal(walletBean.getMoney()));
        }
        if(mEyesStatus == 0){//显示
            mTxtAllMoney.setText(SlinUtil.formatNumFromWeiS(mContext, mAllMoney)+" "+mContext.getResources().getString(R.string.wallet_manager_txt_money_unit_01));
            mImgEyesStatus.setBackgroundResource(R.mipmap.icon_main_left_eyes_open);
        }else{
            mTxtAllMoney.setText(mContext.getResources().getString(R.string.main_home_money_eyes_close));
            mImgEyesStatus.setBackgroundResource(R.mipmap.icon_main_left_eyes_close);
        }
        mListData.addAll(listData);
        mAdapter.setEyesStatus(mEyesStatus);
        mAdapter.notifyDataSetChanged();
    }

    private class CreateClickListener implements OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.item_choose_main_wallet_create_wallet:
                    mContext.startActivity(new Intent(mContext, CreateWalletActivity.class));
                    dismiss();
                    break;
                case R.id.item_choose_main_wallet_layout_eyes:
                    mEyesStatus = SharedPreferencesUtil.getSharePreInt(mContext, DAppConstants.MONEY_EYES_STATUS);
                    if(mEyesStatus == 0){
                        mEyesStatus = 1;
                        mImgEyesStatus.setBackgroundResource(R.mipmap.icon_main_left_eyes_close);
                        mTxtAllMoney.setText(mContext.getResources().getString(R.string.main_home_money_eyes_close));
                    }else{
                        mEyesStatus = 0;
                        mImgEyesStatus.setBackgroundResource(R.mipmap.icon_main_left_eyes_open);
                        mTxtAllMoney.setText(SlinUtil.formatNumFromWeiS(mContext, mAllMoney)+" "+mContext.getResources().getString(R.string.wallet_manager_txt_money_unit_01));
                    }
                    SharedPreferencesUtil.setSharePreInt(mContext, DAppConstants.MONEY_EYES_STATUS,mEyesStatus);
                    mAdapter.setEyesStatus(mEyesStatus);
                    mOnChooseWalletListener.changeEyesStatus(mEyesStatus);
                    break;
            }

        }
    }

    private class EmptyClickListener implements OnClickListener {
        @Override
        public void onClick(View arg0) {
            dismiss();
        }
    }

    private void initDialogPopupWindow() {
        mPopupWindow = new PopupWindow(this,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        mPopupWindow.setAnimationStyle(R.style.popwin_anim_style_left);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        ColorDrawable dw = new ColorDrawable(Color.TRANSPARENT);
        mPopupWindow.setBackgroundDrawable(dw);
    }

    public void show(List<WalletBean> listDate) {
        initData(listDate);
        mPopupWindow.showAtLocation(mCurrentFocusView, Gravity.BOTTOM, 0, 0);
    }

    public void dismiss() {
        mPopupWindow.dismiss();
    }

    public interface OnChooseWalletListener{
        void setOnChooseWalletListener(WalletBean walletBean);
        void changeEyesStatus(int eyesStatus);
    }

    public void setOnChooseWalletListener(OnChooseWalletListener mOnChooseWalletListener) {
        this.mOnChooseWalletListener = mOnChooseWalletListener;
    }

    private OnChooseWalletListener mOnChooseWalletListener;


}
