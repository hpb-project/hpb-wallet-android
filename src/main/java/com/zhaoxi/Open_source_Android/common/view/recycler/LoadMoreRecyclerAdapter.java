package com.zhaoxi.Open_source_Android.common.view.recycler;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhaoxi.Open_source_Android.dapp.R;

public class LoadMoreRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int LOAD_MORE_ITEM_TYPE = 99;

    private RecyclerView.Adapter<RecyclerView.ViewHolder> mAdapter;
    private boolean isNeedLoadMore;
    private boolean isGridLayoutManager;
    private Context mContext;

    public LoadMoreRecyclerAdapter(Context context, RecyclerView.Adapter<RecyclerView.ViewHolder> adapter, boolean isNeedLoadMore) {
        mAdapter = adapter;
        mContext = context;
        this.isNeedLoadMore = isNeedLoadMore;
    }

    public LoadMoreRecyclerAdapter(Context context, RecyclerView.Adapter<RecyclerView.ViewHolder> adapter, boolean isNeedLoadMore, boolean isGridLayoutManager) {
        mAdapter = adapter;
        mContext = context;
        this.isNeedLoadMore = isNeedLoadMore;
        this.isGridLayoutManager = isGridLayoutManager;
    }

    public boolean isNeedLoadMore() {
        return isNeedLoadMore;
    }

    public void setIsNeedLoadMore(boolean isNeedLoadMore) {
        this.isNeedLoadMore = isNeedLoadMore;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == LOAD_MORE_ITEM_TYPE) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.view_recyclerview_bottom_more, parent, false);
            if (isGridLayoutManager) {
                view = LayoutInflater.from(mContext).inflate(R.layout.view_recyclerview_load_more_translation, parent, false);
            }
            return new RViewHolder(view);
        }
        return mAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position == mAdapter.getItemCount()) return;
        mAdapter.onBindViewHolder(holder, position);
    }

    @Override
    public void onAttachedToRecyclerView(final RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int type = getItemViewType(position);
                    if (type == LOAD_MORE_ITEM_TYPE) {
                        return 2;
                    }
                    return 1;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (isNeedLoadMore) {
            return mAdapter.getItemCount() + 1;
        }
        return mAdapter.getItemCount();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == mAdapter.getItemCount())
            return LOAD_MORE_ITEM_TYPE;
        return mAdapter.getItemViewType(position);
    }

    public RecyclerView.Adapter<RecyclerView.ViewHolder> getWrappedAdapter() {
        return mAdapter;
    }

    public static class RViewHolder extends RecyclerView.ViewHolder {
        public RViewHolder(View itemView) {
            super(itemView);
        }
    }
}
