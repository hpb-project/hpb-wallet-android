package com.zhaoxi.Open_source_Android.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.zhaoxi.Open_source_Android.Config;
import com.zhaoxi.Open_source_Android.DAppConstants;
import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.base.DappApplication;
import com.zhaoxi.Open_source_Android.common.view.banner.BannerView;
import com.zhaoxi.Open_source_Android.libs.utils.CollectionUtil;
import com.zhaoxi.Open_source_Android.libs.utils.DateUtilSL;
import com.zhaoxi.Open_source_Android.libs.utils.StrUtil;
import com.zhaoxi.Open_source_Android.net.bean.BannerInfo;
import com.zhaoxi.Open_source_Android.net.bean.NewsBean;
import com.zhaoxi.Open_source_Android.ui.activity.NewsWebActivity;

import java.util.List;

/**
 * des:
 * Created by ztt on 2018/6/6.
 */

public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int ITEM_TYPE_01 = 1;
    private static final int ITEM_TYPE_02 = 2;
    private static final int ITEM_TYPE_03 = 3;

    private Context mContext;
    private List<NewsBean.BaseNews> mDataList;
    private List<BannerInfo.BaseInfo> mBannerInfos;
    private List<NewsBean.BaseNews> mDappsInfos;

    public NewsAdapter(Context context, List<BannerInfo.BaseInfo> bannerInfos, List<NewsBean.BaseNews> dataList, List<NewsBean.BaseNews> dappsList) {
        this.mContext = context;
        this.mBannerInfos = bannerInfos;
        this.mDataList = dataList;
        this.mDappsInfos = dappsList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView;
        if (viewType == ITEM_TYPE_01) {//banner
            itemView = LayoutInflater.from(mContext).inflate(R.layout.item_main_find_banner, viewGroup, false);
            return new ItemBannerHolder(itemView);
        } else if (viewType == ITEM_TYPE_02) {//DApps
            itemView = LayoutInflater.from(mContext).inflate(R.layout.item_main_find_dapps, viewGroup, false);
            return new ItemDappsHolder(itemView);
        } else {//news
            itemView = LayoutInflater.from(mContext).inflate(R.layout.item_main_find_news, viewGroup, false);
            return new ItemNewsHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position == 0) {
            ItemBannerHolder itemBannerHolder = (ItemBannerHolder) holder;
            itemBannerHolder.mTxtRedBag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DappApplication.disableDoubleClick(new DappApplication.OnPerformClickListener() {
                        @Override
                        public void onClick(View... view) {
                            mOnReadListenner.onRedBagListenner();
                        }
                    });

                }
            });
            itemBannerHolder.mTxtMap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DappApplication.disableDoubleClick(new DappApplication.OnPerformClickListener() {
                        @Override
                        public void onClick(View... view) {
                            mOnReadListenner.onMaplistenner();
                        }
                    });
                }
            });

            itemBannerHolder.mTxtVote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DappApplication.disableDoubleClick(new DappApplication.OnPerformClickListener() {
                        @Override
                        public void onClick(View... view) {
                            mOnReadListenner.onVoteListenner();
                        }
                    });
                }
            });
            itemBannerHolder.mTxtVoteGl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DappApplication.disableDoubleClick(new DappApplication.OnPerformClickListener() {
                        @Override
                        public void onClick(View... view) {
                            mOnReadListenner.onVoteGllistenner();
                        }
                    });
                }
            });
            itemBannerHolder.mTxtCoinfh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DappApplication.disableDoubleClick(new DappApplication.OnPerformClickListener() {
                        @Override
                        public void onClick(View... view) {
                            mOnReadListenner.onCionFhlistenner();
                        }
                    });
                }
            });

            itemBannerHolder.mTxtDigitalSign.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DappApplication.disableDoubleClick(new DappApplication.OnPerformClickListener() {
                        @Override
                        public void onClick(View... view) {
                            if (mOnReadListenner != null){
                                mOnReadListenner.onDigitalSignListener();
                            }
                        }
                    });
                }
            });

            if (mBannerInfos.size() < 1) return;
            itemBannerHolder.mBannerView.setBannerData(mBannerInfos, mBannerImageClick);

        } else if (position == 1) {
            if (mDappsInfos.size() < 1) return;
            ItemDappsHolder itemDappsHolder = (ItemDappsHolder) holder;
            if (mDappsInfos.size() >= 1) {
                LinearLayoutManager mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
                itemDappsHolder.mDappsRecyclerView.setLayoutManager(mLayoutManager);

                DappsRecyclerPhotoAdapter dappsGridPhotoAdapter = new DappsRecyclerPhotoAdapter(mContext, mDappsInfos);
                dappsGridPhotoAdapter.setDappsListenner(new DappsRecyclerPhotoAdapter.DappsListenner() {
                    @Override
                    public void onClick(int id, boolean isAuthorize, String titleCn, String titleEn, String url, boolean isIntentOut) {
                        mOnReadListenner.OnDappsClickListenner(id, isAuthorize, titleCn, titleEn, url, isIntentOut);
                    }
                });
                itemDappsHolder.mDappsRecyclerView.setAdapter(dappsGridPhotoAdapter);
            }
        } else {
            setNewsData(holder, position, 2);
        }
    }

    private void setNewsData(RecyclerView.ViewHolder holder, int position, int reduce) {
        ItemNewsHolder itemHolder = (ItemNewsHolder) holder;

        itemHolder.baseLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {//第一行
            return ITEM_TYPE_01;
        } else if (position == 1) {
            return ITEM_TYPE_02;
        } else {
            return ITEM_TYPE_03;
        }
    }

    @Override
    public int getItemCount() {
        int count = 1;
        count = CollectionUtil.isCollectionEmpty(mDataList) ? 2 : mDataList.size() + 2;
        return count;
    }

    /**
     * banner
     */
    public class ItemBannerHolder extends RecyclerView.ViewHolder {
        private BannerView mBannerView;
        private TextView mTxtRedBag, mTxtVote, mTxtMap, mTxtVoteGl, mTxtCoinfh, mTxtDigitalSign;

        public ItemBannerHolder(View itemView) {
            super(itemView);
            mBannerView = itemView.findViewById(R.id.main_find_cycle_viewpager_content);
            mTxtRedBag = itemView.findViewById(R.id.main_find_text_redbag);
            mTxtVote = itemView.findViewById(R.id.main_find_text_vote);
            mTxtMap = itemView.findViewById(R.id.main_find_text_map);
            mTxtVoteGl = itemView.findViewById(R.id.main_find_text_vote_gl);
            mTxtCoinfh = itemView.findViewById(R.id.main_find_text_cion_fh);
            // 数字签名
            mTxtDigitalSign = itemView.findViewById(R.id.main_find_text_digital_sign);
        }
    }

    public class ItemDappsHolder extends RecyclerView.ViewHolder {
        private RecyclerView mDappsRecyclerView;

        public ItemDappsHolder(View itemView) {
            super(itemView);
            mDappsRecyclerView = itemView.findViewById(R.id.fragment_find_dapps_recyclerview);
        }
    }

    /**
     * news
     */
    public class ItemNewsHolder extends RecyclerView.ViewHolder {
        private RelativeLayout baseLayout;
        private TextView mTxtContent;
        private TextView mTxtTimeResouce, mTxtReadNum;
        private ImageView mImgRight;

        public ItemNewsHolder(View itemView) {
            super(itemView);
            baseLayout = itemView.findViewById(R.id.item_main_find_news_layout);
            mTxtContent = itemView.findViewById(R.id.item_main_find_news_content);
            mTxtTimeResouce = itemView.findViewById(R.id.item_main_find_news_reouce);
            mTxtReadNum = itemView.findViewById(R.id.item_main_find_news_read_num);
            mImgRight = itemView.findViewById(R.id.item_main_find_news_img);
        }
    }

    // 第二种的监听事件banner 点击相应不同的配置
    private BannerView.BanviewImageClick mBannerImageClick = new BannerView.BanviewImageClick() {
        @Override
        public void setImageOnClick(View view, BannerInfo.BaseInfo bannerInfo) {
            Intent goto_webView = new Intent(mContext, NewsWebActivity.class);
            goto_webView.putExtra(NewsWebActivity.ACTIVITY_TITLE_INFO, bannerInfo.getTitle());
            goto_webView.putExtra(NewsWebActivity.ACTIVITY_DES_INFO, bannerInfo.getTitle());
            goto_webView.putExtra(NewsWebActivity.WEBVIEW_LOAD_URL, Config.COMMON_WEB_URL + DAppConstants.NEWS_DETAILS + bannerInfo.getArticleId());
            mContext.startActivity(goto_webView);
        }
    };

    public interface OnReadListenner {
        void OnReadListenner(int position, String title, String des, String url);

        void OnDappsClickListenner(int id, boolean isAuthorize, String titleCn, String titleEn, String url, boolean isIntentOut);

        void onRedBagListenner();

        void onVoteListenner();

        void onMaplistenner();

        void onVoteGllistenner();

        void onCionFhlistenner();

        /**
         * 数字签名
         */
        void onDigitalSignListener();
    }

    public void setOnReadListenner(OnReadListenner mOnReadListenner) {
        this.mOnReadListenner = mOnReadListenner;
    }

    private OnReadListenner mOnReadListenner;

}
