package com.zhaoxi.Open_source_Android.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.zhaoxi.Open_source_Android.DAppConstants;
import com.zhaoxi.Open_source_Android.dapp.R;

import java.util.List;

/**
 * Created by ztt on 2016/8/31.
 */
public class GridPhotoAdapter extends BaseAdapter {
    protected LayoutInflater mInflater;
    protected Context mContext;
    protected List<String> mDatas;

    public GridPhotoAdapter(Context mContext, List<String> mDatas) {
        this.mContext = mContext;
        this.mDatas = mDatas;
        this.mInflater = LayoutInflater.from(mContext);
    }

    public void refresh(List<String> data) {
        this.mDatas = data;
        super.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (mDatas.size() == 0) {
            return 1;
        } else if (mDatas.size() > 0 && mDatas.size() < DAppConstants.DEFULT_SELECT_PHOTO_NUM) {
            return mDatas.size() + 1;
        } else {
            return mDatas.size();
        }
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.activity_select_photo_select_grid_item, null);
            viewHolder.img = (ImageView) convertView.findViewById(R.id.select_item_image);
            viewHolder.imgdel = (ImageView) convertView.findViewById(R.id.select_item_del);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.imgdel.setVisibility(View.GONE);
        viewHolder.img.setEnabled(false);
        setImgRight(viewHolder.img, 15, 30);
        if (mDatas.size() == 0) {
            setImgRight(viewHolder.img, 0, 0);
            viewHolder.img.setEnabled(true);
            viewHolder.img.setImageResource(R.mipmap.icon_auth_add_bg);
            viewHolder.img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectDD.onGoSelect();
                }
            });
        } else if (mDatas.size() > 0 && mDatas.size() <= DAppConstants.DEFULT_SELECT_PHOTO_NUM) {
            if (position < mDatas.size()) {
                Bitmap bitmap = BitmapFactory.decodeFile(mDatas.get(position));
                viewHolder.img.setImageBitmap(bitmap);
                viewHolder.imgdel.setVisibility(View.VISIBLE);
                viewHolder.imgdel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        v.setVisibility(View.GONE);
                        mDatas.remove(mDatas.get(position));
                        notifyDataSetChanged();
                    }
                });
            } else {
                viewHolder.img.setTag(null);
                viewHolder.img.setEnabled(true);
                viewHolder.img.setImageResource(R.mipmap.icon_auth_add_bg);
                viewHolder.img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectDD.onGoSelect();
                    }
                });
            }
        } else {
            viewHolder.imgdel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.setVisibility(View.GONE);
                    mDatas.remove(mDatas.get(position));
                    notifyDataSetChanged();
                }
            });
        }
        return convertView;
    }

    class ViewHolder {
        public ImageView img, imgdel;
    }

    public interface SelectImageGo {
        void onGoSelect();
    }

    private SelectImageGo selectDD;

    public void setSelectDD(SelectImageGo selectDD) {
        this.selectDD = selectDD;
    }

    private void setImgRight(ImageView img, int marignTopValue, int marginRightVale) {
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(img.getLayoutParams());
        lp.setMargins(0, marignTopValue, marginRightVale, 0);
        img.setLayoutParams(lp);
    }
}
