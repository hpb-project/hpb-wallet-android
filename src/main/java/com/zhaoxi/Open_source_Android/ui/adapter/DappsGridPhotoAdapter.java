package com.zhaoxi.Open_source_Android.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.view.RoundAngleImageView;
import com.zhaoxi.Open_source_Android.libs.utils.ChangeLanguageUtil;
import com.zhaoxi.Open_source_Android.net.bean.NewsBean;

import java.util.List;

/**
 * des:Dapps 适配器
 * Created by ztt on 2018/12/25.
 */
@Deprecated
public class DappsGridPhotoAdapter extends BaseAdapter {
    private Context mContext;
    private List<NewsBean.BaseNews> mDatas;
    protected LayoutInflater mInflater;

    public DappsGridPhotoAdapter(Context context, List<NewsBean.BaseNews> mDatas) {
        this.mContext = context;
        this.mDatas = mDatas;
        this.mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mDatas.size();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_dapps_view, null);
            viewHolder.mDappsName = convertView.findViewById(R.id.dapps_item_txt_name);
            viewHolder.mImgHeadr = convertView.findViewById(R.id.dapps_item_img_header);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Glide.with(mContext)
                .load(mDatas.get(position).getLogo())
                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.mipmap.icon_img_defult))
                .into(viewHolder.mImgHeadr);
        int type = ChangeLanguageUtil.languageType(mContext);
        String title = mDatas.get(position).getNameCn();
        if (type != 1) {
            title = mDatas.get(position).getNameEn();
        }
        viewHolder.mDappsName.setText("" + title);
        return convertView;
    }

    class ViewHolder {
        private TextView mDappsName;
        public RoundAngleImageView mImgHeadr;
    }
}
