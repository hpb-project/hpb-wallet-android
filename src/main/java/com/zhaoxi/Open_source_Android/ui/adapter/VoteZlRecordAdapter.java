package com.zhaoxi.Open_source_Android.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.view.TimeTextView;
import com.zhaoxi.Open_source_Android.libs.utils.ChangeLanguageUtil;
import com.zhaoxi.Open_source_Android.libs.utils.DateUtilSL;
import com.zhaoxi.Open_source_Android.libs.utils.SlinUtil;
import com.zhaoxi.Open_source_Android.net.bean.VoteZLBean;
import com.zhaoxi.Open_source_Android.ui.activity.VoteAlDetailsActivity;
import com.zhaoxi.Open_source_Android.web3.utils.Convert;

import java.math.BigDecimal;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VoteZlRecordAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<VoteZLBean.VoteZlInfo> issueDatas;
    private Context mContext;

    public VoteZlRecordAdapter(Context context, List<VoteZLBean.VoteZlInfo> issueDatas) {
        this.issueDatas = issueDatas;
        this.mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.vote_record_item, viewGroup, false);
        return new ViewRHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        VoteZLBean.VoteZlInfo voteZlInfo = issueDatas.get(i);
        ViewRHolder itemHodler = (ViewRHolder) viewHolder;
        itemHodler.tvVotePro.setText(mContext.getResources().getString(R.string.activity_vote_gl_txt_06_02));
        itemHodler.tvCountDownTimer.setText(DateUtilSL.dateToStrymdhm2(DateUtilSL.getDateByLong(voteZlInfo.getEndTime(), 2)));
        switch (voteZlInfo.getVoteStatus()) {//投票中、已通过、被否决
            case "1":
                itemHodler.ivVoteStatus.setImageResource(R.mipmap.icon_vote_zl_underway);
                itemHodler.mLayoutBg.setBackgroundResource(R.drawable.shape_card_dark_bg);
                itemHodler.tvVotePro.setText(mContext.getResources().getString(R.string.activity_vote_gl_txt_06_01));
                itemHodler.tvCountDownTimer.setTimes(DateUtilSL.ComputeCountdown(voteZlInfo.getEndTime() / 1000));
                itemHodler.tvCountDownTimer.setRun(true);
                itemHodler.tvCountDownTimer.setOnRunEndListener(new TimeTextView.OnRunEndListener() {
                    @Override
                    public void end() {
                        String strTime = "<font color='#FFFFFF'>0</font><font color='#C0C0C0' >" + mContext.getResources().getString(R.string.activity_vote_gl_txt_06_03)
                                + "</font><font color='#FFFFFF'>0</font><font color='#C0C0C0'>" + mContext.getResources().getString(R.string.activity_vote_gl_txt_06_04)
                                + "</font><font color='#FFFFFF'>0</font><font color='#C0C0C0' >" + mContext.getResources().getString(R.string.activity_vote_gl_txt_06_05)
                                + "</font>";
                        itemHodler.tvCountDownTimer.setText(strTime);
                    }
                });
                itemHodler.tvCountDownTimer.run();
                itemHodler.tvVoteStatus.setText(mContext.getResources().getString(R.string.activity_vote_gl_txt_10_01));
                itemHodler.ivVoteStatusBg.setImageResource(R.mipmap.icon_vote_zl_toupiao);
                break;
            case "2":
                itemHodler.tvCountDownTimer.setAlpha(0.6f);
                itemHodler.ivVoteStatus.setImageResource(R.mipmap.icon_vote_zl_announce);
                itemHodler.mLayoutBg.setBackgroundResource(R.drawable.shape_card_dark_bg);
                itemHodler.tvVoteStatus.setText(mContext.getResources().getString(R.string.activity_vote_gl_txt_10_02));
                itemHodler.ivVoteStatusBg.setImageResource(R.mipmap.icon_vote_zl_jiexiao);
                break;
            case "3":
                itemHodler.tvCountDownTimer.setAlpha(0.6f);
                itemHodler.ivVoteStatus.setImageResource(R.mipmap.icon_vote_zl_pass);
                itemHodler.mLayoutBg.setBackgroundResource(R.drawable.shape_card_light_bg);
                itemHodler.tvVoteStatus.setText(mContext.getResources().getString(R.string.activity_vote_gl_txt_10_03));
                itemHodler.ivVoteStatusBg.setImageResource(R.mipmap.icon_vote_zl_tongguo);
                break;
            case "4":
                itemHodler.tvCountDownTimer.setAlpha(0.6f);
                itemHodler.ivVoteStatus.setImageResource(R.mipmap.icon_vote_zl_vote_down);
                itemHodler.mLayoutBg.setBackgroundResource(R.drawable.shape_card_light_bg);
                itemHodler.tvVoteStatus.setText(mContext.getResources().getString(R.string.activity_vote_gl_txt_10_04));
                itemHodler.ivVoteStatusBg.setImageResource(R.mipmap.icon_vote_zl_foujue);
                break;
            case "5":
                itemHodler.tvCountDownTimer.setAlpha(0.6f);
                itemHodler.ivVoteStatus.setImageResource(R.mipmap.icon_vote_zl_cancellation);
                itemHodler.mLayoutBg.setBackgroundResource(R.drawable.shape_card_light_bg);
                itemHodler.tvVoteStatus.setText(mContext.getResources().getString(R.string.activity_vote_gl_txt_10_05));
                itemHodler.ivVoteStatusBg.setImageResource(R.mipmap.icon_vote_zl_zuofei);
                break;
        }

        int mLangusge = ChangeLanguageUtil.languageType(mContext);
        String title = voteZlInfo.getNameZh();
        String value1N = voteZlInfo.getValue1Zh();
        String value2N = voteZlInfo.getValue2Zh();
        if (mLangusge != 1) {//en
            title = voteZlInfo.getNameEn();
            value1N = voteZlInfo.getValue1En();
            value2N = voteZlInfo.getValue2En();
        }

        itemHodler.tvVoteDesc.setText(title);

        String peragreeNum = voteZlInfo.getPeragreeNum();
        if (!TextUtils.isEmpty(peragreeNum) && !peragreeNum.equals("0")) {
            BigDecimal suportNum = Convert.fromWei(new BigDecimal(voteZlInfo.getPeragreeNum()), Convert.Unit.ETHER);
            itemHodler.tvSupport.setText(value1N + " " + SlinUtil.NumberFormat0(mContext, suportNum) + " " + mContext.getResources().getString(R.string.activity_vote_gl_txt_12));
        } else {
            itemHodler.llSupport.setVisibility(View.GONE);
        }

        String perdisagreeNum = voteZlInfo.getPerdisagreeNum();
        if (!TextUtils.isEmpty(perdisagreeNum) && !perdisagreeNum.equals("0")) {
            BigDecimal nosuportNum = Convert.fromWei(new BigDecimal(voteZlInfo.getPerdisagreeNum()), Convert.Unit.ETHER);
            itemHodler.tvUnSupport.setText(value2N + " " + SlinUtil.NumberFormat0(mContext, nosuportNum) + " " + mContext.getResources().getString(R.string.activity_vote_gl_txt_12));
        } else {
            itemHodler.llUnSupport.setVisibility(View.GONE);
        }

        itemHodler.voteStatusContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, VoteAlDetailsActivity.class);
                intent.putExtra(VoteAlDetailsActivity.ISSUE_NO, voteZlInfo.getIssueNo());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return issueDatas.size();
    }

    class ViewRHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_vote_status)
        ImageView ivVoteStatus;
        @BindView(R.id.tv_vote_status)
        TextView tvVoteStatus;
        @BindView(R.id.tv_vote_desc)
        TextView tvVoteDesc;

        @BindView(R.id.tv_count_down_timer_pro)
        TextView tvVotePro;
        @BindView(R.id.tv_count_down_timer)
        TimeTextView tvCountDownTimer;

        @BindView(R.id.ll_support)
        LinearLayout llSupport;
        @BindView(R.id.tv_support)
        TextView tvSupport;
        @BindView(R.id.ll_unsupport)
        LinearLayout llUnSupport;
        @BindView(R.id.tv_unsupport)
        TextView tvUnSupport;
        @BindView(R.id.iv_vote_status_bg)
        ImageView ivVoteStatusBg;
        @BindView(R.id.vote_status_container)
        LinearLayout voteStatusContainer;
        @BindView(R.id.vote_status_container_layout)
        RelativeLayout mLayoutBg;

        ViewRHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
