package com.zhaoxi.Open_source_Android.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhaoxi.Open_source_Android.dapp.R;

import java.util.List;

/**
 * des:
 * Created by ztt on 2018/6/14.
 */

public class MenmonicDailogAdapter extends BaseAdapter {
    private Context mContext;
    private List<String> mListData;

    public MenmonicDailogAdapter(Context context, List<String> listData) {
        mContext = context;
        mListData = listData;
    }

    @Override
    public int getCount() {
        return mListData.size();
    }

    @Override
    public Object getItem(int position) {
        return mListData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String word = mListData.get(position);
        ItemHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.edit_item_select_word, null);
            holder = new ItemHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ItemHolder) convertView.getTag();
        }

        holder.mTxtWord.setText(word);

        return convertView;
    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        private TextView mTxtWord;

        public ItemHolder(View itemView) {
            super(itemView);
            mTxtWord = itemView.findViewById(R.id.txt_mnemonic_word);
        }
    }
}
