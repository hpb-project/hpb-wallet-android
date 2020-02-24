package com.zhaoxi.Open_source_Android.common.drag;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.zhaoxi.Open_source_Android.ui.adapter.TokenManagerAdapter;

/**
 * create by fangz
 * create date:2019/8/24
 * create time:6:45
 */
public class ItemTouchCallback extends ItemTouchHelper.Callback {

    private TokenManagerAdapter.OnItemTouchCallbackListener onItemTouchCallbackListener;

    public ItemTouchCallback(TokenManagerAdapter.OnItemTouchCallbackListener onItemTouchCallbackListener) {
        this.onItemTouchCallbackListener = onItemTouchCallbackListener;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            int dragFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            int swipeFlags = 0;
            return makeMovementFlags(dragFlags, swipeFlags);
        } else if (layoutManager instanceof LinearLayoutManager) {
            int orientation = ((LinearLayoutManager) layoutManager).getOrientation();
            if (orientation == LinearLayoutManager.HORIZONTAL) {
                int dragFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
                int swipeFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                return makeMovementFlags(dragFlags, swipeFlags);
            } else if (orientation == LinearLayoutManager.VERTICAL) {
                int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
//                int swipeFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
                int swipeFlags = 0;
                return makeMovementFlags(dragFlags, swipeFlags);
            }
        }


        return 0;

    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
        if (onItemTouchCallbackListener != null) {
            onItemTouchCallbackListener.onMove(viewHolder.getAdapterPosition(), viewHolder1.getAdapterPosition());
        }
        return false;
    }


    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (onItemTouchCallbackListener != null) {
            onItemTouchCallbackListener.onSwiped(viewHolder.getAdapterPosition());
        }
    }

    @Override
    public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {

        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {

            if (viewHolder instanceof ItemTouchHelperViewHolder) {

                ItemTouchHelperViewHolder itemViewHolder = (ItemTouchHelperViewHolder) viewHolder;

                itemViewHolder.onItemSelected();
            }

        }
        super.onSelectedChanged(viewHolder, actionState);


    }

    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {

        super.clearView(recyclerView, viewHolder);

        if (viewHolder instanceof ItemTouchHelperViewHolder) {

            ItemTouchHelperViewHolder itemViewHolder = (ItemTouchHelperViewHolder) viewHolder;

            itemViewHolder.onItemClear();
        }


    }


    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }



}
