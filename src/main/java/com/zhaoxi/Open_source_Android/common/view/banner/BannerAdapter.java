package com.zhaoxi.Open_source_Android.common.view.banner;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.view.RoundAngleImageView;
import com.zhaoxi.Open_source_Android.libs.utils.CollectionUtil;
import com.zhaoxi.Open_source_Android.net.bean.BannerInfo;

import java.util.ArrayList;
import java.util.List;

public class BannerAdapter extends PagerAdapter {
    private Context mContext;
    private List<BannerInfo.BaseInfo> mDatas = new ArrayList<>();
    private BannerView.BanviewImageClick mBannerViewImageClick;

    public BannerAdapter(Context context, List<BannerInfo.BaseInfo> bannerResponses
            , BannerView.BanviewImageClick banviewImageClick) {
        mContext = context;
        mDatas = bannerResponses;
        mBannerViewImageClick = banviewImageClick;
    }

    @Override
    public int getCount() {
        int max = Integer.MAX_VALUE;
        if (mDatas.size() == 1) {
            max = 1;
        }
        return CollectionUtil.isCollectionEmpty(mDatas) ? 1 : max;
    }

    public int getRealCount() {
        return CollectionUtil.isCollectionEmpty(mDatas) ? 0 : mDatas.size();
    }


    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        if (getRealCount() == 0)
            return null;
        final int virtualPosition = position % getRealCount();
        View convertView = View.inflate(mContext, R.layout.item_banner_layout, null);
        RelativeLayout recyclerView = convertView.findViewById(R.id.banner_view_base_layout);
        RoundAngleImageView image = convertView.findViewById(R.id.banner_view_img);
        TextView textView = convertView.findViewById(R.id.banner_view_txt_content);
        textView.setText("" + mDatas.get(virtualPosition).getTitle());
        Glide.with(mContext)
                .load(mDatas.get(virtualPosition).getImages())
                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
                .into(image);
        recyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBannerViewImageClick.setImageOnClick(v, mDatas.get(virtualPosition));
            }
        });
        container.addView(convertView);
        return convertView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        RelativeLayout relativeLayout = (RelativeLayout)object;
        relativeLayout.destroyDrawingCache();
        container.removeView(relativeLayout);
    }
}
