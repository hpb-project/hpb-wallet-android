package com.zhaoxi.Open_source_Android.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.view.CountDownTextView;
import com.zhaoxi.Open_source_Android.common.view.CountDownView;
import com.zhaoxi.Open_source_Android.common.view.PercentProgressBar;
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

public class IssueListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<VoteZLBean.VoteZlInfo> issueTestDatas;

    public IssueListAdapter(Context context, List<VoteZLBean.VoteZlInfo> issueTestDatas) {
        this.mContext = context;
        this.issueTestDatas = issueTestDatas;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.vote_issue_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, int i) {
        VoteZLBean.VoteZlInfo voteZlInfo = issueTestDatas.get(i);
        ViewHolder itemHodler = (ViewHolder) viewHolder;
        itemHodler.mTxtTimePro.setText(mContext.getResources().getString(R.string.activity_vote_gl_txt_06_02));

        switch (voteZlInfo.getVoteStatus()) {
            case "1":
                itemHodler.ivVoteStatus.setImageResource(R.mipmap.icon_vote_zl_underway);
                itemHodler.voteStatusContainer.setBackgroundResource(R.drawable.shape_card_dark_bg);
                itemHodler.tvCountDownTimer.setAlpha(0.9f);
                itemHodler.mTxtTimePro.setText(mContext.getResources().getString(R.string.activity_vote_gl_txt_06_01));
//                itemHodler.tvCountDownTimer.setTimes(DateUtilSL.ComputeCountdown(voteZlInfo.getEndTime() / 1000));
//                itemHodler.tvCountDownTimer.setRun(true);
//                itemHodler.tvCountDownTimer.setOnRunEndListener(new TimeTextView.OnRunEndListener() {
//                    @Override
//                    public void end() {
//                        String strTime = "<font color='#FFFFFF'>0</font><font color='#C0C0C0' >" + mContext.getResources().getString(R.string.activity_vote_gl_txt_06_03)
//                                + "</font><font color='#FFFFFF'>0</font><font color='#C0C0C0'>" + mContext.getResources().getString(R.string.activity_vote_gl_txt_06_04)
//                                + "</font><font color='#FFFFFF'>0</font><font color='#C0C0C0' >" + mContext.getResources().getString(R.string.activity_vote_gl_txt_06_05)
//                                + "</font>";
//                        itemHodler.tvCountDownTimer.setText(strTime);
//                    }
//                });
//                itemHodler.tvCountDownTimer.run();

                //itemHodler.tvCountDownTimer.setTimes(voteZlInfo.getEndTime() / 1000);
                itemHodler.viewCountDown.setVisibility(View.VISIBLE);
                itemHodler.tvCountDownTimer.setVisibility(View.GONE);
                itemHodler.viewCountDown.setTimes(voteZlInfo.getEndTime() / 1_000);
                itemHodler.tvVoteStatus.setText(mContext.getResources().getString(R.string.activity_vote_gl_txt_10_01));
                break;
            case "2":
                itemHodler.viewCountDown.setVisibility(View.GONE);
                itemHodler.tvCountDownTimer.setVisibility(View.VISIBLE);
                itemHodler.tvCountDownTimer.setAlpha(0.6f);
                itemHodler.ivVoteStatus.setImageResource(R.mipmap.icon_vote_zl_announce);
                itemHodler.voteStatusContainer.setBackgroundResource(R.drawable.shape_card_dark_bg);
                itemHodler.tvVoteStatus.setText(mContext.getResources().getString(R.string.activity_vote_gl_txt_10_02));
                itemHodler.tvCountDownTimer.setText(DateUtilSL.dateToStrymdhm2(DateUtilSL.getDateByLong(voteZlInfo.getEndTime(), 2)));
                break;
            case "3":
                itemHodler.viewCountDown.setVisibility(View.GONE);
                itemHodler.tvCountDownTimer.setVisibility(View.VISIBLE);
                itemHodler.tvCountDownTimer.setAlpha(0.6f);
                itemHodler.ivVoteStatus.setImageResource(R.mipmap.icon_vote_zl_pass);
                itemHodler.voteStatusContainer.setBackgroundResource(R.drawable.shape_card_light_bg);
                itemHodler.tvVoteStatus.setText(mContext.getResources().getString(R.string.activity_vote_gl_txt_10_03));
                itemHodler.tvCountDownTimer.setText(DateUtilSL.dateToStrymdhm2(DateUtilSL.getDateByLong(voteZlInfo.getEndTime(), 2)));
                break;
            case "4":
                itemHodler.viewCountDown.setVisibility(View.GONE);
                itemHodler.tvCountDownTimer.setVisibility(View.VISIBLE);
                itemHodler.tvCountDownTimer.setAlpha(0.6f);
                itemHodler.ivVoteStatus.setImageResource(R.mipmap.icon_vote_zl_vote_down);
                itemHodler.voteStatusContainer.setBackgroundResource(R.drawable.shape_card_light_bg);
                itemHodler.tvVoteStatus.setText(mContext.getResources().getString(R.string.activity_vote_gl_txt_10_04));
                itemHodler.tvCountDownTimer.setText(DateUtilSL.dateToStrymdhm2(DateUtilSL.getDateByLong(voteZlInfo.getEndTime(), 2)));
                break;
            case "5":
                itemHodler.viewCountDown.setVisibility(View.GONE);
                itemHodler.tvCountDownTimer.setVisibility(View.VISIBLE);
                itemHodler.tvCountDownTimer.setAlpha(0.6f);
                itemHodler.ivVoteStatus.setImageResource(R.mipmap.icon_vote_zl_cancellation);
                itemHodler.voteStatusContainer.setBackgroundResource(R.drawable.shape_card_light_bg);
                itemHodler.tvVoteStatus.setText(mContext.getResources().getString(R.string.activity_vote_gl_txt_10_05));
                itemHodler.tvCountDownTimer.setText(DateUtilSL.dateToStrymdhm2(DateUtilSL.getDateByLong(voteZlInfo.getEndTime(), 2)));
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
        BigDecimal suportNum = Convert.fromWei(new BigDecimal(voteZlInfo.getValue1Num()), Convert.Unit.ETHER);
        BigDecimal nosuportNum = Convert.fromWei(new BigDecimal(voteZlInfo.getValue2Num()), Convert.Unit.ETHER);
        itemHodler.tvSupportNumAndPercent.setText(Html.fromHtml("<font color='#FFFFFF'>" + ("0.".startsWith(SlinUtil.NumberFormat0(mContext, suportNum)) ? "0" : SlinUtil.NumberFormat0(mContext, suportNum)) + "</font> <font color='#E8E8E8'>(" + DateUtilSL.formatSPercent(voteZlInfo.getValue1Rate()) + ")</font>"));
        itemHodler.tvUnSupportNumAndPercent.setText(Html.fromHtml("<font color='#FFFFFF'>" + ("0.".startsWith(SlinUtil.NumberFormat0(mContext, nosuportNum)) ? "0" : SlinUtil.NumberFormat0(mContext, nosuportNum)) + "</font> <font color='#E8E8E8'>(" + DateUtilSL.formatSPercent(voteZlInfo.getValue2Rate()) + ")</font>"));


        itemHodler.tvSupport.setText(value1N);
        itemHodler.tvUnSupport.setText(value2N);

        itemHodler.percentProgressBar.setStyle(PercentProgressBar.Style.ALL_SHOW);
        itemHodler.percentProgressBar.setProgressSupport(DateUtilSL.formatPercent(voteZlInfo.getValue1Rate()));
        itemHodler.percentProgressBar.setProgressUnSupport(DateUtilSL.formatPercent(voteZlInfo.getValue2Rate()));

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
        return issueTestDatas.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_count_down_pro_time)
        TextView mTxtTimePro;
        @BindView(R.id.vote_status_container)
        LinearLayout voteStatusContainer;
        @BindView(R.id.iv_vote_status)
        ImageView ivVoteStatus;
        @BindView(R.id.tv_vote_status)
        TextView tvVoteStatus;
        @BindView(R.id.tv_vote_desc)
        TextView tvVoteDesc;
        @BindView(R.id.tv_support_num_and_percent)
        TextView tvSupportNumAndPercent;
        @BindView(R.id.tv_unsupport_num_and_percent)
        TextView tvUnSupportNumAndPercent;
        @BindView(R.id.vote_percent_view)
        PercentProgressBar percentProgressBar;

        @BindView(R.id.tv_support)
        TextView tvSupport;
        @BindView(R.id.tv_unsupport)
        TextView tvUnSupport;
        //         @BindView(R.id.tv_count_down_timer)
//        TimeTextView tvCountDownTimer;
        @BindView(R.id.tv_count_down_timer)
        CountDownTextView tvCountDownTimer;
        @BindView(R.id.view_count_down)
        CountDownView viewCountDown;
        @BindView(R.id.btn_vote_detail)
        LinearLayout btnVoteDetail;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
