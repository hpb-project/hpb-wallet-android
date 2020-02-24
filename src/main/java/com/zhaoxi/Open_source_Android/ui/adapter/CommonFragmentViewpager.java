package com.zhaoxi.Open_source_Android.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.zhaoxi.Open_source_Android.libs.utils.StrUtil;

import java.io.Serializable;
import java.util.List;

public class CommonFragmentViewpager extends FragmentPagerAdapter implements
        Serializable {
    private static final long serialVersionUID = 1L;
    private List<Fragment> mListFragments;
    private String[] mCatList;

    public CommonFragmentViewpager(FragmentManager fm, String[] catList,
                                   List<Fragment> fragmentList) {
        super(fm);
        mListFragments = fragmentList;
        mCatList = catList;
    }

    @Override
    public int getCount() {
        return mListFragments.size();
    }

    @Override
    public Fragment getItem(int position) {
        return mListFragments.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String cat = mCatList[position];
        if (!StrUtil.isEmpty(cat)) {
            return cat;
        } else {
            return "";
        }
    }
}