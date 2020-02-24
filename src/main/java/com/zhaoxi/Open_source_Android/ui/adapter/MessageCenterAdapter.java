package com.zhaoxi.Open_source_Android.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.base.DappApplication;
import com.zhaoxi.Open_source_Android.libs.utils.StrUtil;
import com.zhaoxi.Open_source_Android.libs.utils.SystemInfoUtil;
import com.zhaoxi.Open_source_Android.net.BaseBet.NetResultCallBack;
import com.zhaoxi.Open_source_Android.net.Request.UpdateMassegeStatusRequest;
import com.zhaoxi.Open_source_Android.net.bean.MeassgeBean;
import com.zhaoxi.Open_source_Android.libs.utils.CollectionUtil;
import com.zhaoxi.Open_source_Android.libs.utils.DateUtilSL;
import com.zhaoxi.Open_source_Android.ui.activity.MessageCenterDetailsActivity;

import java.util.List;

/**
 * 消息中心
 * Created by zhutt on 2018/6/25.
 */
public class MessageCenterAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<MeassgeBean.MeassgeInfo> mDataList;

    public MessageCenterAdapter(Context context, List<MeassgeBean.MeassgeInfo> datas) {
        mContext = context;
        mDataList = datas;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_message_center, parent, false);
        return new ItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final MeassgeBean.MeassgeInfo entity = mDataList.get(position);
        ItemHolder itemHolder = (ItemHolder) holder;
        itemHolder.mTxtTitle.setText(entity.getTitle());
        String content = StrUtil.isEmpty(entity.getSummary()) ? entity.getSummary() : entity.getSummary().replace("<br />", "\n");
        itemHolder.mTxtContent.setText(content);
        itemHolder.mTxtTime.setText(DateUtilSL.dateToStrymdhm(DateUtilSL.getDateByLong(entity.getUpdatedAt(), 2)));
        itemHolder.mTxtIsRead.setVisibility(entity.getReadState() == 0 ? View.VISIBLE : View.GONE);
        itemHolder.mLayoutBase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String deviceId = SystemInfoUtil.getDeviceId(mContext);
                new UpdateMassegeStatusRequest(deviceId, entity.getId()).doRequest(mContext, new NetResultCallBack() {
                    @Override
                    public void onSuccess(JSONArray jsonArray) {
                        //跳转到详情
                        Intent it_details = new Intent(mContext, MessageCenterDetailsActivity.class);
                        it_details.putExtra(MessageCenterDetailsActivity.MESSGE_DATA, entity);
                        mContext.startActivity(it_details);
                    }

                    @Override
                    public void onError(String error) {
                        DappApplication.getInstance().showToast(error);
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return CollectionUtil.isCollectionEmpty(mDataList) ? 0 : mDataList.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        private TextView mTxtTitle, mTxtContent, mTxtIsRead, mTxtTime;
        private LinearLayout mLayoutBase;

        public ItemHolder(View itemView) {
            super(itemView);
            mLayoutBase = itemView.findViewById(R.id.item_message_center_layout);
            mTxtTitle = itemView.findViewById(R.id.item_message_center_title);
            mTxtContent = itemView.findViewById(R.id.item_message_center_content);
            mTxtIsRead = itemView.findViewById(R.id.item_message_center_isread);
            mTxtTime = itemView.findViewById(R.id.item_message_center_time);
        }
    }
}

