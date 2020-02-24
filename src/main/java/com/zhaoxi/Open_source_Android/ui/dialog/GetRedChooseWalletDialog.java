package com.zhaoxi.Open_source_Android.ui.dialog;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.base.BaseActivity;
import com.zhaoxi.Open_source_Android.common.view.wheel.OnWheelChangedListener;
import com.zhaoxi.Open_source_Android.common.view.wheel.WheelView;
import com.zhaoxi.Open_source_Android.ui.adapter.ChooseWheelAdapter;
import com.zhaoxi.Open_source_Android.web3.bean.WalletBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 通用选择对话框
 * Created by wangyy on 2017/9/8.
 */
public class GetRedChooseWalletDialog extends LinearLayout implements View.OnClickListener {
    private int mMaxTextSize = 15;//滑轮的最大字体
    private int mMinTextSize = 12;//滑轮的最小字体

    private Context mContext;
    private View mCurrentFocusView;
    private OnSureClickedListener mListener;

    private ImageView mImgCancel;
    private Button mBtnSure;
    private WheelView mWheelView;
    private ChooseWheelAdapter mProNameWheelAdapter;

    private List<WalletBean> items = new ArrayList<>();
    private int currentCheckedPosition = 0;

    private PopupWindow mPopupWindow;

    public GetRedChooseWalletDialog(BaseActivity activity, View currentFocusView, OnSureClickedListener listener,
                                    List<WalletBean> items, int initChecked) {
        super(activity);
        mContext = activity;
        mCurrentFocusView = currentFocusView;
        mListener = listener;
        this.items.addAll(items);

        LayoutInflater.from(activity).inflate(R.layout.dialog_common_choose_info, this);
        mImgCancel = findViewById(R.id.common_choose_info_cancel);
        mBtnSure = findViewById(R.id.common_choose_info_sure);
        mWheelView =  findViewById(R.id.common_choose_info_wheel);

        mImgCancel.setOnClickListener(this);
        mBtnSure.setOnClickListener(this);
        findViewById(R.id.common_choose_info_base_layout).setOnClickListener(this);
        currentCheckedPosition = initChecked == -1 ? 0 : initChecked;

        initWheel();
        initDialogPopupWindow();
    }

    private void initWheel() {
        mProNameWheelAdapter = new ChooseWheelAdapter(mContext, items, 0, mMaxTextSize, mMinTextSize);
        mWheelView.setViewAdapter(mProNameWheelAdapter);
        mWheelView.setVisibleItems(8);
        mWheelView.setCurrentItem(currentCheckedPosition);

        mWheelView.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                String currentText = (String) mProNameWheelAdapter.getItemText(wheel.getCurrentItem());//获取当前列表内容
                setTextviewSize(currentText, mProNameWheelAdapter);//设置滚动最前面的字体大小
                currentCheckedPosition = newValue;
            }
        });
    }

    private void initDialogPopupWindow() {
        mPopupWindow = new PopupWindow(this,
                android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                android.view.ViewGroup.LayoutParams.MATCH_PARENT);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setFocusable(true);

        ColorDrawable dw = new ColorDrawable(Color.TRANSPARENT);
        mPopupWindow.setBackgroundDrawable(dw);
    }

    public void show() {
        mPopupWindow.showAsDropDown(mCurrentFocusView);
    }

    public void dismiss() {
        mPopupWindow.dismiss();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.common_choose_info_cancel:
            case R.id.common_choose_info_base_layout:
                dismiss();
                break;
            case R.id.common_choose_info_sure:
                if (mListener != null)
                    mListener.sure(currentCheckedPosition);
                dismiss();
                break;
        }
    }

    /**
     * 设置字体大小
     *
     * @param curriteItemText
     * @param adapter
     */
    public void setTextviewSize(String curriteItemText, ChooseWheelAdapter adapter) {
        ArrayList<View> arrayList = adapter.getTestViews();
        int size = arrayList.size();
        String currentText;
        for (int i = 0; i < size; i++) {
            TextView textvew = (TextView) arrayList.get(i);
            currentText = textvew.getText().toString();
            if (curriteItemText.equals(currentText)) {
                textvew.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            } else {
                textvew.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
            }
        }
    }

    public interface OnSureClickedListener {
        void sure(int curretPosition);
    }
}

