package com.zhaoxi.Open_source_Android.ui.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.ui.adapter.MenmonicDailogAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * des:映射确认页面
 * Created by ztt on 2018/6/21.
 */

public class MnemonicDialog extends LinearLayout {
    private EditText mCurrentFocusView;
    private PopupWindow mPopupWindow;
    private Context mContext;

    private List<String> mWordData;
    private ListView mListView;
    private MenmonicDailogAdapter mAdapter;


    public MnemonicDialog(Context context, EditText currentFocusView, int width) {
        super(context);
        mContext = context;
        mCurrentFocusView = currentFocusView;
        mWordData = new ArrayList<>();
        LayoutInflater.from(context).inflate(R.layout.edit_show_select_word, this);
        mListView = findViewById(R.id.select_list);
        mAdapter= new MenmonicDailogAdapter(mContext, mWordData);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mCheckMnemonicListener.onCheckListener(mCurrentFocusView,mWordData.get(position));
                dismiss();
            }
        });

        LinearLayout.LayoutParams linearParams =(LinearLayout.LayoutParams) mListView.getLayoutParams(); //取控件textView当前的布局参数 linearParams.height = 20;// 控件的高强制设成20
        linearParams.width = width;// 控件的宽强制设成30
        mListView.setLayoutParams(linearParams); //使设置好的布局参数应用到控件

        initDialogPopupWindow();
    }



    private void initDialogPopupWindow() {
        mPopupWindow = new PopupWindow(this, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setOutsideTouchable(true);

        ColorDrawable dw = new ColorDrawable(Color.TRANSPARENT);
        mPopupWindow.setBackgroundDrawable(dw);
    }

    @SuppressLint("WrongConstant")
    public void show(List<String> listDate, EditText currentFocusView) {
        changeData(listDate);
        mCurrentFocusView = currentFocusView;
        if(!mPopupWindow.isShowing()){
            mPopupWindow.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
            mPopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            mPopupWindow.showAsDropDown(mCurrentFocusView);
        }
    }

    public void changeData(List<String> listDate){
        mWordData.clear();
        mWordData.addAll(listDate);
        mAdapter.notifyDataSetChanged();
    }

    public boolean isShow(){
        return mPopupWindow.isShowing();
    }

    public void dismiss() {
        mPopupWindow.dismiss();
    }


    public interface CheckMnemonicListener{
        void onCheckListener(EditText editText, String word);
    }

    public void setCheckMnemonicListener(CheckMnemonicListener checkMnemonicListener) {
        this.mCheckMnemonicListener = checkMnemonicListener;
    }

    private CheckMnemonicListener mCheckMnemonicListener;


}
