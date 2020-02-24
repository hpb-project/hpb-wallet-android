package com.zhaoxi.Open_source_Android.common.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhaoxi.Open_source_Android.dapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 投票结果
 */
public class VoteResultView extends LinearLayout {

    @BindView(R.id.iv_support_status)
    ImageView ivSupportStatus;
    @BindView(R.id.tv_support_status)
    TextView tvSupportStatus;
    @BindView(R.id.percent_progress_bar)
    PercentProgressBar percentProgressBar;
    @BindView(R.id.tv_vote_num)
    TextView mtvVoteNum;
    @BindView(R.id.tv_support_status_percent_bai)
    TextView tvPercent;
    @BindView(R.id.tv_voted)
    TextView mTvVotedNum;
    @BindView(R.id.btn_vote)
    Button mBtnVote;

    private boolean mSupportFlag = false;


    private OnVoteClickListener l;

    public VoteResultView(Context context) {
        super(context);
        initView(context);
    }

    public VoteResultView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public VoteResultView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.vote_result_layout, this);
        ButterKnife.bind(this, view);
    }

    /**
     * 模式可选为：PercentProgressBar.Style.ONLY_SHOW_SUPPORT ,PercentProgressBar.Style.ONLY_SHOW_UN_SUPPORT
     *
     * @param style
     */
    public void setPercentProgressBarStyle(int style) {
        percentProgressBar.setStyle(style);
    }

    /**
     * 设置百分比进度条支持进度
     *
     * @param progress 进度
     */
    public void setPercentProgressBarSupportProgress(int progress) {
        percentProgressBar.setProgressSupport(progress);
    }

    /**
     * 设置百分比进度条不支持进度
     *
     * @param progress 进度
     */
    public void setPercentProgressBarUnSupportProgress(int progress) {
        percentProgressBar.setProgressUnSupport(progress);
    }


    @OnClick(R.id.btn_vote)
    public void onClick(View view) {
        if (view.getId() == R.id.btn_vote) {
            // 投票
            if (VoteResultView.this.l != null) {
                VoteResultView.this.l.onVote(mSupportFlag);
            }

        }
    }

    /**
     * 设置支持状态
     *
     * @param supportFlag 是否支持标识
     */
    public void setSupportStatus(boolean supportFlag, String valueTxt) {
        mSupportFlag = supportFlag;
        if (supportFlag) {
            this.ivSupportStatus.setImageResource(R.drawable.shape_support_point);
            this.tvSupportStatus.setText(valueTxt);
            this.mtvVoteNum.setTextColor(Color.parseColor("#02B8F7"));
        } else {
            this.ivSupportStatus.setImageResource(R.drawable.shape_unsupport_point);
            this.tvSupportStatus.setText(valueTxt);
            this.mtvVoteNum.setTextColor(Color.parseColor("#FFB428"));
        }
    }

    /**
     * 设置是否可投票
     * @param enable
     */
    public void setVoteEnable(boolean enable) {
        if (enable)
            mBtnVote.setVisibility(VISIBLE);
        else
            mBtnVote.setVisibility(GONE);
    }


    /**
     * 设置剩余票数
     *
     * @param voteNum 剩余票数
     */
    public void setVoteNum(CharSequence voteNum) {
        this.mtvVoteNum.setText(voteNum);
    }

    /**
     * 设置已投票数
     * @param votedNum 已投票数
     */
    public void setVotedNum(CharSequence votedNum){this.mTvVotedNum.setText(votedNum);}

    /**
     * 设置投票数量百分比
     *
     * @param percent 百分比
     */
    public void setPercent(CharSequence percent) {
        this.tvPercent.setText(percent);
    }

    public void setOnVoteClickListener(OnVoteClickListener l) {
        this.l = l;
    }

    public interface OnVoteClickListener {
        void onVote(boolean isSupport);
    }

}
