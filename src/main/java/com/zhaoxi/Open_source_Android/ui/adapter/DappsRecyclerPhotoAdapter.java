package com.zhaoxi.Open_source_Android.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
 * des:
 * Created by ztt on 2018/6/7.
 */
public class DappsRecyclerPhotoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<NewsBean.BaseNews> mDatas;

    public DappsRecyclerPhotoAdapter(Context context, List<NewsBean.BaseNews> mDatas) {
        this.mContext = context;
        this.mDatas = mDatas;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_dapps_view, viewGroup, false);
        return new ItemHodler(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ItemHodler itemHodler = (ItemHodler) holder;

        Glide.with(mContext)
                .load(mDatas.get(position).getLogo())
                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.mipmap.icon_img_defult))
                .into(itemHodler.mImgHeadr);

        int type = ChangeLanguageUtil.languageType(mContext);
        String title = mDatas.get(position).getNameCn();
        if (type != 1) {
            title = mDatas.get(position).getNameEn();
        }
        itemHodler.mDappsName.setText("" + title);

        itemHodler.mBaseLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDappsListenner.onClick(mDatas.get(position).getId(), mDatas.get(position).isAuthorize(), mDatas.get(position).getNameCn(), mDatas.get(position).getNameEn(),
                        mDatas.get(position).getUrl(), mDatas.get(position).isOuterBrowserOpen());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    private class ItemHodler extends RecyclerView.ViewHolder {
        private LinearLayout mBaseLayout;
        private TextView mDappsName;
        public RoundAngleImageView mImgHeadr;

        public ItemHodler(View itemView) {
            super(itemView);
            mBaseLayout = itemView.findViewById(R.id.dapps_item_layout_base);
            mDappsName = itemView.findViewById(R.id.dapps_item_txt_name);
            mImgHeadr = itemView.findViewById(R.id.dapps_item_img_header);
        }
    }

    public interface DappsListenner{
        void onClick(int id, boolean isAuthorize, String titleCn, String titleEn, String url, boolean isIntentOut);
    }

    public void setDappsListenner(DappsListenner dappsListenner) {
        this.mDappsListenner = dappsListenner;
    }

    private DappsListenner mDappsListenner;

}
