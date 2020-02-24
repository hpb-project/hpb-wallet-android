package com.zhaoxi.Open_source_Android.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.bean.DigitalSignBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * create by fangz
 * create date:2019/9/5
 * create time:9:58
 * 数字签名记录Adapter
 */
public class DigitalSignRecordAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<DigitalSignBean> digitalSignBeans;
    private Context mContext;

    public DigitalSignRecordAdapter(Context context, List<DigitalSignBean> digitalSignBeans) {
        this.digitalSignBeans = digitalSignBeans;
        this.mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_digital_sign_record_item, parent, false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        DigitalSignBean digitalSignBean = digitalSignBeans.get(position);

        itemViewHolder.mDateTime.setText(digitalSignBean.getSignDateTime());
        itemViewHolder.mContent.setText(digitalSignBean.getSignContent());
        itemViewHolder.mRootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null){
                    listener.onItemClick(digitalSignBean);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return digitalSignBeans.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_digital_sign_date_time)
        TextView mDateTime;
        @BindView(R.id.tv_digital_sign_content)
        TextView mContent;
        @BindView(R.id.sign_record_root)
        LinearLayout mRootView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(DigitalSignBean digitalSignBean);
    }
}
