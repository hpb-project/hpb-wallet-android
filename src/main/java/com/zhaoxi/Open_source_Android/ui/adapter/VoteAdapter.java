package com.zhaoxi.Open_source_Android.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.base.DappApplication;
import com.zhaoxi.Open_source_Android.libs.utils.CollectionUtil;
import com.zhaoxi.Open_source_Android.libs.utils.SlinUtil;
import com.zhaoxi.Open_source_Android.libs.utils.StrUtil;
import com.zhaoxi.Open_source_Android.net.bean.VoteBean;
import com.zhaoxi.Open_source_Android.ui.activity.VoteDetailsActivity;
import com.zhaoxi.Open_source_Android.web3.utils.Convert;

import java.math.BigDecimal;
import java.util.List;

/**
 * des:
 * Created by ztt on 2018/6/6.
 */

public class VoteAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int ITEM_TYPE_01 = 1;
    private static final int ITEM_TYPE_02 = 2;
    private static final int ITEM_TYPE_03 = 3;

    private Context mContext;
    private List<VoteBean.VoteInfo> mDataList;
    private int mCurTab = 1, mCurType = -1;
    private int mCurLaguage;
    private String mBonusIsOpen;

    public VoteAdapter(Context context, List<VoteBean.VoteInfo> listData, int curTab,int curlaugage,String bonusIsOpen) {
        this.mContext = context;
        this.mDataList = listData;
        this.mCurTab = curTab;
        this.mCurLaguage = curlaugage;
        this.mBonusIsOpen = bonusIsOpen;
    }

    public void setBonusIsOpen(String bonusIsOpen){
        this.mBonusIsOpen = bonusIsOpen;
        notifyDataSetChanged();
    }

    public void setCurTab(int curTab) {
        this.mCurTab = curTab;
    }

    public void setCurTabChange(int curTab) {
        this.mCurTab = curTab;
        notifyDataSetChanged();
    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView;
        if (viewType == ITEM_TYPE_01) {
            itemView = LayoutInflater.from(mContext).inflate(R.layout.view_vote_info_top, viewGroup, false);
            return new ItemTopHolder(itemView);
        } else if (viewType == ITEM_TYPE_02) {
            itemView = LayoutInflater.from(mContext).inflate(R.layout.view_vote_info_middle, viewGroup, false);
            return new ItemMiddleHolder(itemView);
        } else {//news
            itemView = LayoutInflater.from(mContext).inflate(R.layout.view_vote_info_item, viewGroup, false);
            return new ItemHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position == 0) {
            ItemTopHolder topHolder = (ItemTopHolder) holder;
            if(mCurLaguage == 1){
                topHolder.mLayoutDes.setBackgroundResource(R.mipmap.icon_vote_info_top_bg_1);
            }else{
                topHolder.mLayoutDes.setBackgroundResource(R.mipmap.icon_vote_info_top_bg_2);
            }
            topHolder.mTxtQuery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String keyword = topHolder.mKeyword.getText().toString();
                    if (StrUtil.isEmpty(keyword)) {
                        DappApplication.getInstance().showToast(mContext.getResources().getString(R.string.activity_vote_search_toast_01));
                        return;
                    }
                    mTabChangeLisener.queryVote(keyword);
                }
            });
            topHolder.mTxtLookRule.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mTabChangeLisener.onLookRule();
                }
            });
        } else if (position == 1) {
            ItemMiddleHolder itemHolder = (ItemMiddleHolder) holder;
            setTanChange(itemHolder, mCurTab);
            itemHolder.mTxtTab1.setOnClickListener(new TabOnClickListener(itemHolder));
            itemHolder.mTxtTab2.setOnClickListener(new TabOnClickListener(itemHolder));
            itemHolder.mTxtTab3.setOnClickListener(new TabOnClickListener(itemHolder));
            itemHolder.mTxtTab4.setOnClickListener(new TabOnClickListener(itemHolder));

            if("1".equals(mBonusIsOpen)){
                itemHolder.mTxtFhbl.setVisibility(View.VISIBLE);
            }else itemHolder.mTxtFhbl.setVisibility(View.GONE);

        } else {
            setNewsData(holder, position, 2);
        }
    }

    private class TabOnClickListener implements View.OnClickListener {
        private ItemMiddleHolder itemHolder;

        public TabOnClickListener(ItemMiddleHolder itemHolder) {
            this.itemHolder = itemHolder;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.txt_vote_tab_1:
                    if (mCurTab != 1) {
                        setTanChange(itemHolder, 1);
                        mCurTab = 1;
                        mCurType = -1;
                    }
                    break;
                case R.id.txt_vote_tab_2:
                    if (mCurTab != 2) {
                        setTanChange(itemHolder, 2);
                        mCurTab = 2;
                        mCurType = 1;
                    }
                    break;
                case R.id.txt_vote_tab_3:
                    if (mCurTab != 3) {
                        setTanChange(itemHolder, 3);
                        mCurTab = 3;
                        mCurType = 0;
                    }
                    break;
                case R.id.txt_vote_tab_4:
                    if (mCurTab != 4) {
                        setTanChange(itemHolder, 4);
                        mCurTab = 4;
                        mCurType = 2;
                    }
                    break;
            }
            mTabChangeLisener.onChange(mCurTab, mCurType);
        }
    }

    private void setTanChange(ItemMiddleHolder itemHolder, int curTab) {
        if (curTab == 1) {
            setSelect(itemHolder, mContext.getResources().getColor(R.color.color_2E2F47),
                    mContext.getResources().getColor(R.color.color_black_666),
                    mContext.getResources().getColor(R.color.color_black_666),
                    mContext.getResources().getColor(R.color.color_black_666),
                    mContext.getResources().getColor(R.color.white),
                    mContext.getResources().getColor(R.color.color_EEFDF4),
                    mContext.getResources().getColor(R.color.color_EEFDF4),
                    mContext.getResources().getColor(R.color.color_EEFDF4));
        } else if (curTab == 2) {
            setSelect(itemHolder, mContext.getResources().getColor(R.color.color_black_666),
                    mContext.getResources().getColor(R.color.color_2E2F47),
                    mContext.getResources().getColor(R.color.color_black_666),
                    mContext.getResources().getColor(R.color.color_black_666),
                    mContext.getResources().getColor(R.color.color_EEFDF4),
                    mContext.getResources().getColor(R.color.white),
                    mContext.getResources().getColor(R.color.color_EEFDF4),
                    mContext.getResources().getColor(R.color.color_EEFDF4));
        } else if (curTab == 3) {
            setSelect(itemHolder, mContext.getResources().getColor(R.color.color_black_666),
                    mContext.getResources().getColor(R.color.color_black_666),
                    mContext.getResources().getColor(R.color.color_2E2F47),
                    mContext.getResources().getColor(R.color.color_black_666),
                    mContext.getResources().getColor(R.color.color_EEFDF4),
                    mContext.getResources().getColor(R.color.color_EEFDF4),
                    mContext.getResources().getColor(R.color.white),
                    mContext.getResources().getColor(R.color.color_EEFDF4));
        } else {
            setSelect(itemHolder, mContext.getResources().getColor(R.color.color_black_666),
                    mContext.getResources().getColor(R.color.color_black_666),
                    mContext.getResources().getColor(R.color.color_black_666),
                    mContext.getResources().getColor(R.color.color_2E2F47),
                    mContext.getResources().getColor(R.color.color_EEFDF4),
                    mContext.getResources().getColor(R.color.color_EEFDF4),
                    mContext.getResources().getColor(R.color.color_EEFDF4),
                    mContext.getResources().getColor(R.color.white));
        }
    }

    private void setSelect(ItemMiddleHolder itemHolder, int color1, int color2, int color3, int color4,
                           int bgcolor1, int bgcolor2, int bgcolor3, int bgcolor4) {
        itemHolder.mTxtTab1.setTextColor(color1);
        itemHolder.mTxtTab1.setBackgroundColor(bgcolor1);
        itemHolder.mTxtTab2.setTextColor(color2);
        itemHolder.mTxtTab2.setBackgroundColor(bgcolor2);
        itemHolder.mTxtTab3.setTextColor(color3);
        itemHolder.mTxtTab3.setBackgroundColor(bgcolor3);
        itemHolder.mTxtTab4.setTextColor(color4);
        itemHolder.mTxtTab4.setBackgroundColor(bgcolor4);
    }

    private void setNewsData(RecyclerView.ViewHolder holder, int position, int reduce) {
        ItemHolder itemHolder = (ItemHolder) holder;
        VoteBean.VoteInfo voteInfo = mDataList.get(position - reduce);
        if ("1".equals(voteInfo.getRank())) {
            itemHolder.mTxtRank.setBackgroundResource(R.drawable.icon_vote_info_item_one);
            itemHolder.mTxtRank.setText("");
        } else if ("2".equals(voteInfo.getRank())) {
            itemHolder.mTxtRank.setBackgroundResource(R.drawable.icon_vote_info_item_two);
            itemHolder.mTxtRank.setText("");
        } else if ("3".equals(voteInfo.getRank())) {
            itemHolder.mTxtRank.setBackgroundResource(R.drawable.icon_vote_info_item_three);
            itemHolder.mTxtRank.setText("");
        } else {
            itemHolder.mTxtRank.setBackgroundColor(Color.argb(0, 0, 0, 0));
            itemHolder.mTxtRank.setText(voteInfo.getRank());
        }
        itemHolder.mTxtName.setText(voteInfo.getName());
        if(mBonusIsOpen.equals("1")){
            switch (voteInfo.getBonusFlag()){
                case "1":
                    itemHolder.mTxtBilie.setText(mContext.getResources().getString(R.string.activity_cion_fh_txt_29));
                    itemHolder.mImgJinGao.setVisibility(View.GONE);
                    itemHolder.mTxtBilie.setVisibility(View.VISIBLE);
                    break;
                case "2":
                case "5":
                    itemHolder.mTxtBilie.setText(voteInfo.getBonusRate()+"%");
                    itemHolder.mImgJinGao.setVisibility(View.GONE);
                    itemHolder.mTxtBilie.setVisibility(View.VISIBLE);
                    break;
                case "3":
                case "4":
                case "6":
                    itemHolder.mTxtBilie.setText("");
                    itemHolder.mImgJinGao.setVisibility(View.VISIBLE);
                    itemHolder.mTxtBilie.setVisibility(View.GONE);
                    break;
            }
            itemHolder.mLayoutBl.setVisibility(View.VISIBLE);
        }else{
            itemHolder.mLayoutBl.setVisibility(View.GONE);
        }

        BigDecimal money = Convert.fromWei(voteInfo.getCount(), Convert.Unit.ETHER);
        itemHolder.mTxtPoll.setText("" + SlinUtil.NumberFormat0(mContext, SlinUtil.ValueScale(money, 0)));
        if ((position - reduce) % 2 != 0) {
            itemHolder.mLayoutBase.setBackgroundColor(mContext.getResources().getColor(R.color.color_F9FAFF));
        } else {
            itemHolder.mLayoutBase.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        }
        itemHolder.mLayoutBase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it_deatisl = new Intent(mContext, VoteDetailsActivity.class);
                it_deatisl.putExtra(VoteDetailsActivity.VOTE_DETAILS, voteInfo);
                mContext.startActivity(it_deatisl);
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
        int count = CollectionUtil.isCollectionEmpty(mDataList) ? 2 : mDataList.size() + 2;
        return count;
    }


    public class ItemTopHolder extends RecyclerView.ViewHolder {
        private TextView mTxtQuery, mTxtLookRule;
        private EditText mKeyword;
        private LinearLayout mLayoutDes;

        public ItemTopHolder(View itemView) {
            super(itemView);
            mTxtQuery = itemView.findViewById(R.id.vote_item_txt_query);
            mKeyword = itemView.findViewById(R.id.vote_item_txt_keyword);
            mTxtLookRule = itemView.findViewById(R.id.vote_item_txt_look_rule);
            mLayoutDes = itemView.findViewById(R.id.vote_item_layout_des);
        }
    }

    public class ItemMiddleHolder extends RecyclerView.ViewHolder {
        private TextView mTxtTab1, mTxtTab2, mTxtTab3, mTxtTab4;
        private TextView mTxtFhbl;

        public ItemMiddleHolder(View itemView) {
            super(itemView);
            mTxtTab1 = itemView.findViewById(R.id.txt_vote_tab_1);
            mTxtTab2 = itemView.findViewById(R.id.txt_vote_tab_2);
            mTxtTab3 = itemView.findViewById(R.id.txt_vote_tab_3);
            mTxtTab4 = itemView.findViewById(R.id.txt_vote_tab_4);
            mTxtFhbl = itemView.findViewById(R.id.view_vote_info_middle_fhbl);
        }
    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        private TextView mTxtRank;
        private TextView mTxtName;
        private TextView mTxtVote;
        private TextView mTxtPoll;
        private TextView mTxtBilie;
        private ImageView mImgJinGao;
        private LinearLayout mLayoutBase;
        private LinearLayout mLayoutBl;

        public ItemHolder(View itemView) {
            super(itemView);
            mTxtRank = itemView.findViewById(R.id.txt_vote_item_rank);
            mTxtName = itemView.findViewById(R.id.txt_vote_item_name);
            mTxtVote = itemView.findViewById(R.id.txt_vote_item_vote);
            mTxtPoll = itemView.findViewById(R.id.txt_vote_item_poll);
            mTxtBilie = itemView.findViewById(R.id.txt_vote_item_bilie);
            mImgJinGao = itemView.findViewById(R.id.img_vote_item_jingao);
            mLayoutBase = itemView.findViewById(R.id.vote_item_layout_base);
            mLayoutBl = itemView.findViewById(R.id.txt_vote_item_bilie_layout);
        }
    }

    public interface onTabChangeLisener {
        void onChange(int curTab, int curType);

        void queryVote(String content);

        void onLookRule();
    }

    public void setTabChangeLisener(onTabChangeLisener mTabChangeLisener) {
        this.mTabChangeLisener = mTabChangeLisener;
    }

    private onTabChangeLisener mTabChangeLisener;


}
