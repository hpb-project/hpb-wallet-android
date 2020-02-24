package com.zhaoxi.Open_source_Android.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.net.bean.NodeDividendBean;

import java.util.List;

/**
 * Created by xionglh on 2016/2/29.
 */
public class ChooseRangeWheelAdapter extends AbstractWheelTextAdapter {
    List<NodeDividendBean> list;

    public ChooseRangeWheelAdapter(Context context, List<NodeDividendBean> list, int currentItem, int maxsize, int minsize) {
        super(context, R.layout.view_wheel_list_item, NO_RESOURCE, currentItem, maxsize, minsize);
        this.list = list;
        setItemTextResource(R.id.tempValue);
    }

    @Override
    public View getItem(int index, View cachedView, ViewGroup parent) {
        View view = super.getItem(index, cachedView, parent);
        return view;
    }

    @Override
    public int getItemsCount() {
        return list.size();
    }

    @Override
    public CharSequence getItemText(int index) {
        NodeDividendBean walletBean = list.get(index);
        return walletBean.getRange();
    }
}
