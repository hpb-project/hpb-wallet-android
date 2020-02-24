package com.zhaoxi.Open_source_Android.common.dialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.base.BaseActivity;
import com.zhaoxi.Open_source_Android.common.base.DappApplication;
import com.zhaoxi.Open_source_Android.common.view.ListViewShowAll;
import com.zhaoxi.Open_source_Android.db.CreateDbWallet;
import com.zhaoxi.Open_source_Android.libs.utils.NetUtil;
import com.zhaoxi.Open_source_Android.libs.utils.SharedPreferencesUtil;
import com.zhaoxi.Open_source_Android.ui.adapter.ChooseWalletAdapter;
import com.zhaoxi.Open_source_Android.web3.bean.WalletBean;

import java.util.ArrayList;
import java.util.List;

/**
 * des:切换钱包
 * Created by ztt on 2018/6/14.
 */

public class ChooseWalletDialog extends LinearLayout {
    private BaseActivity mContext;
    private ListViewShowAll mListView;
    private View mCurrentFocusView;
    private LinearLayout mEmptyLayout;

    private PopupWindow mPopupWindow;
    private List<WalletBean> mDbListData;
    private List<WalletBean> mListData;
    private CreateDbWallet mCreateDbWallet;
    private ChooseWalletAdapter mAdapter;
    private boolean mIsShowCreateWallet;
    /*默认为头像切图*/
    public ChooseWalletDialog(BaseActivity activity, View currentFocusView,boolean isShowCreateWallet) {
        super(activity);
        mContext = activity;
        mCurrentFocusView = currentFocusView;
        mIsShowCreateWallet = isShowCreateWallet;
        mListData = new ArrayList<>();
        mDbListData = new ArrayList<>();
        mCreateDbWallet = new CreateDbWallet(mContext);
        LayoutInflater.from(activity).inflate(R.layout.view_choose_wallet_dialog, this);
        mListView = findViewById(R.id.item_choose_wallet_list);
        mEmptyLayout = findViewById(R.id.item_choose_wallet_layout_emppty);
        mEmptyLayout.setOnClickListener(new EmptyClickListener());
        mAdapter= new ChooseWalletAdapter(mContext, mListData);
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

    public void setData(List<WalletBean> listData){
        mListData = listData;
        mAdapter.notifyDataSetChanged();
    }

    private void initData(String address){
        if(mDbListData.size() != 0){
            mDbListData.clear();
        }
        if(mListData.size() != 0){
            mListData.clear();
        }
        String defultAddress = null;
        if(mIsShowCreateWallet){
            defultAddress = SharedPreferencesUtil.getSharePreString(mContext, SharedPreferencesUtil.CHOOSE_WALLET_ADDRESS);
        }else{
            defultAddress = address;
        }

        mDbListData = mCreateDbWallet.queryAllWallet(mContext);
        for (int i = 0; i < mDbListData.size(); i++) {
            WalletBean walletBean = mDbListData.get(i);
            if(defultAddress.equals(walletBean.getAddress())){
                walletBean.setChooseWallet(true);
            }
            mListData.add(walletBean);
        }
        mAdapter.notifyDataSetChanged();
    }

    private class EmptyClickListener implements OnClickListener {
        @Override
        public void onClick(View arg0) {
            dismiss();
        }
    }

    private void initDialogPopupWindow() {
        mPopupWindow = new PopupWindow(this,
                android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        mPopupWindow.setAnimationStyle(R.style.popwin_anim_style);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        ColorDrawable dw = new ColorDrawable(Color.TRANSPARENT);
        mPopupWindow.setBackgroundDrawable(dw);
    }

    public void show(String address) {
        initData(address);
        mPopupWindow.showAtLocation(mCurrentFocusView, Gravity.BOTTOM, 0, 0);
    }

    public void dismiss() {
        mPopupWindow.dismiss();
    }

    public interface OnChooseWalletListener{
        void setOnChooseWalletListener(WalletBean walletBean);
    }

    public void setOnChooseWalletListener(OnChooseWalletListener mOnChooseWalletListener) {
        this.mOnChooseWalletListener = mOnChooseWalletListener;
    }

    private OnChooseWalletListener mOnChooseWalletListener;


}
