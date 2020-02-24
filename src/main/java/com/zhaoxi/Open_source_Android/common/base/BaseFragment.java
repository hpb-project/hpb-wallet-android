package com.zhaoxi.Open_source_Android.common.base;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhaoxi.Open_source_Android.dapp.R;

public class BaseFragment extends Fragment {
    protected TextView mTextViewTitle, mTextViewLeft, mTextViewRight;
    protected RelativeLayout mLayoutTitleBar;
    private FrameLayout mContentLayout;

    // 带title 的 fragment
    public View onInflaterViewWithTitle(LayoutInflater inflater, ViewGroup container,
                                        int layoutId) {
        View mView = inflater.inflate(R.layout.fragment_base_layout, container,
                false);
//        mTextViewTitle = (TextView) mView.findViewById(R.id.title_bar_center_text_title);
//        mTextViewLeft = (TextView) mView.findViewById(R.id.title_bar_left_button_back);
//        mTextViewLeft.setCompoundDrawables(null, null, null, null);
//        mTextViewRight = (TextView) mView.findViewById(R.id.title_bar_right_button);
//        mLayoutTitleBar = (RelativeLayout) mView.findViewById(R.id.title_bar_layout);
        mContentLayout = (FrameLayout) mView.findViewById(R.id.base_fragment_content_layout);
        inflater.inflate(layoutId, mContentLayout, true);
        return mView;
    }

    
}
