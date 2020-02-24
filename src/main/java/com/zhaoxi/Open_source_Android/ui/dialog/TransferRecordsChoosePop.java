package com.zhaoxi.Open_source_Android.ui.dialog;

import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.zhaoxi.Open_source_Android.DAppConstants;
import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.base.BaseActivity;
import com.zhaoxi.Open_source_Android.libs.utils.CollectionUtil;
import com.zhaoxi.Open_source_Android.libs.utils.SharedPreferencesUtil;
import com.zhaoxi.Open_source_Android.net.BaseBet.CommonResultListener;
import com.zhaoxi.Open_source_Android.net.Request.GetTokenTypelistRequest;
import com.zhaoxi.Open_source_Android.net.bean.TupCoinBean;
import com.zhaoxi.Open_source_Android.ui.adapter.ChooseRecordsDbAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ztt
 * @des 输入密码框
 * @date 2019/9/11.
 */

public class TransferRecordsChoosePop {
    private BaseActivity activity;
    private PopupWindow popupWindow;
    private View mCurrentFocusView;
    private HandleChoose mHandleChoose;

    private TextView mBtnCancle, mTxtB01, mTxtB02, mTxtB03;
    private ListView mListView;
    private List<TupCoinBean.TupBean> mListDaiBi = new ArrayList<>();
    private ChooseRecordsDbAdapter mChooseAdapter;
    private int mCurType = 0;
    private String contractType = DAppConstants.TYPE_HPB;
    private String mCurAddress;

    public TransferRecordsChoosePop(BaseActivity activity, View currentFocusView) {
        this.activity = activity;
        this.mCurrentFocusView = currentFocusView;
        initPopupWindow();
    }

    private void initPopupWindow() {
        View view = LayoutInflater.from(activity).inflate(R.layout.view_transfer_reords_choose_dialog, null, false);
        mTxtB01 = view.findViewById(R.id.transfer_records_choose_dailog_btn_01);
        mTxtB02 = view.findViewById(R.id.transfer_records_choose_dailog_btn_02);
        mTxtB03 = view.findViewById(R.id.transfer_records_choose_dailog_btn_03);
        mListView = view.findViewById(R.id.transfer_records_choose_dailog_list);
        mBtnCancle = view.findViewById(R.id.transfer_records_choose_dailog_btn_cancle);
        mChooseAdapter = new ChooseRecordsDbAdapter(activity, mListDaiBi);
        mListView.setAdapter(mChooseAdapter);

        popupWindow = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setAnimationStyle(R.style.VoteDialogAnim);
        popupWindow.setTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable());
        popupWindow.setOutsideTouchable(true);

        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = 0.85f;
        activity.getWindow().setAttributes(lp);

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                lp.alpha = 1f;
                activity.getWindow().setAttributes(lp);
            }
        });

        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.BUTTON_BACK) {
                    popupWindow.dismiss();
                }
                return false;
            }
        });

        mTxtB01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//Open_source_Android
                setChooseDZ(1);
            }
        });
        mTxtB02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//HRC-20
                setChooseDZ(2);
            }
        });
        mTxtB03.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//HRC-721
                setChooseDZ(3);
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mHandleChoose.getChooseData(mCurType,contractType, mListDaiBi.get(position).getContractAddress(),mListDaiBi.get(position).getTokenSymbol());
                popupWindow.dismiss();
            }
        });

        mBtnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }

    private void setChooseDZ(int type) {
        mCurAddress = SharedPreferencesUtil.getSharePreString(activity, SharedPreferencesUtil.CHOOSE_WALLET_ADDRESS);
        if (mListDaiBi.size() != 0) mListDaiBi.clear();
        mCurType = type;
        contractType = DAppConstants.TYPE_HPB;
        String cType = "HPB";
        switch (type) {
            case 1:
                TupCoinBean.TupBean tupBean = new TupCoinBean.TupBean();
                tupBean.setContractAddress("");
                tupBean.setTokenSymbol("HPB");
                mListDaiBi.add(tupBean);
                mChooseAdapter.notifyDataSetChanged();
                break;
            case 2:
                contractType = DAppConstants.TYPE_HRC_20;
                cType = "HRC-20";
                break;
            case 3:
                contractType = DAppConstants.TYPE_HRC_721;
                cType = "HRC-721";
                break;
        }
        if (type != 1) {
            activity.showProgressDialog();
            new GetTokenTypelistRequest(mCurAddress,cType).doRequest(activity, new CommonResultListener(activity) {
                @Override
                public void onSuccess(JSONArray jsonArray) {
                    if(jsonArray.get(2) != null){
                        TupCoinBean tupCoinBean = JSON.parseObject(jsonArray.get(2).toString(), TupCoinBean.class);
                        if(!CollectionUtil.isCollectionEmpty(tupCoinBean.getList())){
                            mListDaiBi.addAll(tupCoinBean.getList());
                        }
                    }
                    activity.dismissProgressDialog();
                    mChooseAdapter.notifyDataSetChanged();
                }
            });
        }
        clearTextView(type);
    }

    private void clearTextView(int type) {
        mTxtB01.setBackgroundColor(activity.getResources().getColor(R.color.color_F5F6F8));
        mTxtB01.setTextColor(activity.getResources().getColor(R.color.color_black_333));
        mTxtB02.setBackgroundColor(activity.getResources().getColor(R.color.color_F5F6F8));
        mTxtB02.setTextColor(activity.getResources().getColor(R.color.color_black_333));
        mTxtB03.setBackgroundColor(activity.getResources().getColor(R.color.color_F5F6F8));
        mTxtB03.setTextColor(activity.getResources().getColor(R.color.color_black_333));
        switch (type){
            case 1:
                mTxtB01.setBackgroundColor(activity.getResources().getColor(R.color.white));
                mTxtB01.setTextColor(activity.getResources().getColor(R.color.color_54658B));
                break;
            case 2:
                mTxtB02.setBackgroundColor(activity.getResources().getColor(R.color.white));
                mTxtB02.setTextColor(activity.getResources().getColor(R.color.color_54658B));
                break;
            case 3:
                mTxtB03.setBackgroundColor(activity.getResources().getColor(R.color.white));
                mTxtB03.setTextColor(activity.getResources().getColor(R.color.color_54658B));
                break;
        }
    }

    public interface HandleChoose {
        void getChooseData(int type, String contractType,String contractAddress,String name);
    }

    public void setHandleChoose(HandleChoose mHandleChoose) {
        this.mHandleChoose = mHandleChoose;
    }

    public void show(int type) {
        setChooseDZ(type);
        popupWindow.showAtLocation(mCurrentFocusView, Gravity.BOTTOM, 0, 0);
    }

    public void dismiss() {
        popupWindow.dismiss();
    }
}
