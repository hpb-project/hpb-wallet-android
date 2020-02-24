package com.zhaoxi.Open_source_Android.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.bean.BandingAddressBean;

import java.util.List;

/**
 * @author J 适配器
 */
public class SortAdapter extends BaseAdapter implements SectionIndexer {
	private List<BandingAddressBean> list = null;
	private Context mContext;
	private boolean isNeedCheck;

	public boolean isNeedCheck() {
		return isNeedCheck;
	}

	public void setNeedCheck(boolean isNeedCheck) {
		this.isNeedCheck = isNeedCheck;
	}

	public SortAdapter(Context mContext, List<BandingAddressBean> list) {
		this.mContext = mContext;
		this.list = list;
	}

	public void updateListView(List<BandingAddressBean> list) {
		this.list = list;
		notifyDataSetChanged();
	}

	public int getCount() {
		return this.list.size();
	}

	public Object getItem(int position) {
		return list.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View view, ViewGroup arg2) {
		ViewHolder viewHolder = null;
		final BandingAddressBean mContent = list.get(position);
		if (view == null) {
			viewHolder = new ViewHolder();
			view = LayoutInflater.from(mContext).inflate(R.layout.item_address_book_view, null);
			viewHolder.tvTitle = view.findViewById(R.id.txt_address_book_user_name);
			viewHolder.tvLetter = view.findViewById(R.id.item_address_book_catalog);
			viewHolder.viewLine = view.findViewById(R.id.item_address_book_line);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}

		int section = getSectionForPosition(position);

		if (position == getPositionForSection(section)) {
			viewHolder.tvLetter.setVisibility(View.VISIBLE);
			viewHolder.viewLine.setVisibility(View.VISIBLE);
			viewHolder.tvLetter.setText(mContent.getSortLetters());
		} else {
			viewHolder.tvLetter.setVisibility(View.GONE);
			viewHolder.viewLine.setVisibility(View.GONE);
		}
		BandingAddressBean model = list.get(position);

		viewHolder.tvTitle.setText(model.getMark());

		return view;

	}

	final static class ViewHolder {
		TextView tvLetter;
		TextView tvTitle;
		View viewLine;
	}

	/**
	 * 得到首字母的ascii值
	 */
	public int getSectionForPosition(int position) {
		return list.size() == 0?'A':list.get(position).getSortLetters().charAt(0);
	}

	public int getPositionForSection(int section) {
		for (int i = 0; i < getCount(); i++) {
			String sortStr = list.get(i).getSortLetters();
			char firstChar = sortStr.toUpperCase().charAt(0);
			if (firstChar == section) {
				return i;
			}
		}

		return -1;
	}

	public String getAlpha(String str) {
		String sortStr = str.trim().substring(0, 1).toUpperCase();
		if (sortStr.matches("[A-Z]")) {
			return sortStr;
		} else {
			return "#";
		}
	}

	@Override
	public Object[] getSections() {
		return null;
	}
}