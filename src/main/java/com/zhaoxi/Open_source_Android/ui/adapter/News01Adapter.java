package com.zhaoxi.Open_source_Android.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;
import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.view.ExpandableTextView;
import com.zhaoxi.Open_source_Android.libs.utils.DateUtilSL;
import com.zhaoxi.Open_source_Android.libs.utils.StrUtil;
import com.zhaoxi.Open_source_Android.net.bean.NewsBean;

import java.util.List;

/**
 * des:
 * Created by ztt on 2018/6/6.
 */

public class News01Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements
        ExpandableTextView.OnExpandListener,StickyRecyclerHeadersAdapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<NewsBean.BaseNews> mDataList;
    private SparseArray<Integer> mPositionsAndStates = new SparseArray<>();
    //只要在getview时为其赋值为准确的宽度值即可，无论采用何种方法
    private int etvWidth;

    public News01Adapter(Context context, List<NewsBean.BaseNews> dataList) {
        this.mContext = context;
        this.mDataList = dataList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_main_find_kuaicun, viewGroup, false);
        return new ItemKXHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ItemKXHolder itemKXHolder = (ItemKXHolder) holder;
        NewsBean.BaseNews newsInfo = mDataList.get(position);
        String content = StrUtil.isEmpty(newsInfo.getContent()) ? newsInfo.getContent() : newsInfo.getContent().replace("<br />", "\n");
        if (etvWidth == 0) {
            itemKXHolder.mTxtContent.post(new Runnable() {
                @Override
                public void run() {
                    etvWidth = itemKXHolder.mTxtContent.getWidth();
                }
            });
        }
        itemKXHolder.mTxtContent.setTag(position);
        itemKXHolder.mTxtContent.setExpandListener(this);
        Integer state = mPositionsAndStates.get(position);

        itemKXHolder.mTxtContent.updateForRecyclerView(content.toString(), etvWidth, state == null ? 0 : state);//第一次getview时肯定为etvWidth为0

        itemKXHolder.mTxtTime.setText(DateUtilSL.dateToStryhm(DateUtilSL.getDateByLong(newsInfo.getPublishTime(), 2)));

        itemKXHolder.mTxtShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String time = DateUtilSL.dateToStrymdhms3(DateUtilSL.getDateByLong(newsInfo.getPublishTime(), 2));
                mShareKxListener.onHandleShare(String.valueOf(newsInfo.getArticleId()), content, time);
            }
        });
    }


    @Override
    public long getHeaderId(int position) {
        return getSortType(position);
    }

    //获取当前球队的类型
    public int getSortType(int position) {
        NewsBean.BaseNews newsInfo  = mDataList.get(position);
        return Integer.valueOf(DateUtilSL.dateToStrymd(DateUtilSL.getDateByLong(newsInfo.getPublishTime(), 2)).replace("-", ""));
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup viewGroup) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_main_find_kuaicun_header, viewGroup, false);
        return new HeaderHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        final NewsBean.BaseNews entity = mDataList.get(position);
        HeaderHolder viewHolder1 = (HeaderHolder) viewHolder;
        String week = DateUtilSL.getKuaixunTime(mContext,entity.getPublishTime());
        viewHolder1.headerTime.setText(week);
    }

    public class HeaderHolder extends RecyclerView.ViewHolder {
        public TextView headerTime;

        public HeaderHolder(View itemView) {
            super(itemView);
            headerTime = itemView.findViewById(R.id.news_kx_header_time);
        }
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    @Override
    public void onExpand(ExpandableTextView view) {
        Object obj = view.getTag();
        if (obj != null && obj instanceof Integer) {
            mPositionsAndStates.put((Integer) obj, view.getExpandState());
        }
    }

    @Override
    public void onShrink(ExpandableTextView view) {
        Object obj = view.getTag();
        if (obj != null && obj instanceof Integer) {
            mPositionsAndStates.put((Integer) obj, view.getExpandState());
        }
    }

    public class ItemKXHolder extends RecyclerView.ViewHolder {
        private ExpandableTextView mTxtContent;
        private TextView mTxtTime, mTxtShare;

        public ItemKXHolder(View itemView) {
            super(itemView);
            mTxtTime = itemView.findViewById(R.id.main_find_news_kuaixun_time);
            mTxtContent = itemView.findViewById(R.id.main_find_news_kuaixun_content);
            mTxtShare = itemView.findViewById(R.id.main_find_news_kuaixun_share);
        }
    }

    public interface onShareKxListener {
        void onHandleShare(String kid, String content, String time);
    }

    public void setShareKxListener(onShareKxListener mShareKxListener) {
        this.mShareKxListener = mShareKxListener;
    }

    private onShareKxListener mShareKxListener;


}
