package com.zhaoxi.Open_source_Android.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.view.wheel.OnWheelChangedListener;
import com.zhaoxi.Open_source_Android.common.view.wheel.WheelView;
import com.zhaoxi.Open_source_Android.net.bean.NodeDividendBean;
import com.zhaoxi.Open_source_Android.ui.adapter.ChooseRangeWheelAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ztt on 2017/9/8.
 */
public class DevidendChooseRangeDialog extends Dialog{
    public DevidendChooseRangeDialog(Context context) {
        super(context);
    }

    public DevidendChooseRangeDialog(Context context, int theme) {
        super(context, theme);
    }
    public static class Builder {
        private int mMaxTextSize = 15;//滑轮的最大字体
        private int mMinTextSize = 12;//滑轮的最小字体
        private Context mContext;
        private OnSureClickedListener mListener;

        private TextView mTxtSure;
        private WheelView mWheelView;
        private ChooseRangeWheelAdapter mProNameWheelAdapter;

        private List<NodeDividendBean> items = new ArrayList<>();
        private int currentCheckedPosition = 0;

        public Builder(Context context) {
            this.mContext = context;
        }

        public RedHasKLDialog create(OnSureClickedListener listener,
                                     List<NodeDividendBean> items, int initChecked) {
            mListener = listener;
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final RedHasKLDialog dialog = new RedHasKLDialog(mContext,
                    R.style.Dialog);
            View layout = inflater.inflate(R.layout.dialog_devidend_choose_range,
                    null);
            dialog.addContentView(layout, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

            mTxtSure = layout.findViewById(R.id.devidend_choose_range_txt_sure);
            mWheelView =  layout.findViewById(R.id.devidend_choose_range_wheel);

            mTxtSure.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null)
                        mListener.sure(currentCheckedPosition);
                    dialog.dismiss();
                }
            });
            layout.findViewById(R.id.devidend_choose_range_base_layout).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            currentCheckedPosition = initChecked == -1 ? 0 : initChecked;

            mProNameWheelAdapter = new ChooseRangeWheelAdapter(mContext, items, 0, mMaxTextSize, mMinTextSize);
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

            dialog.setContentView(layout);
            return dialog;
        }
    }

    /**
     * 设置字体大小
     *
     * @param curriteItemText
     * @param adapter
     */
    public static void setTextviewSize(String curriteItemText, ChooseRangeWheelAdapter adapter) {
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

