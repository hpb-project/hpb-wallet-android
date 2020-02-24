package com.zhaoxi.Open_source_Android.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Html;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.gyf.immersionbar.ImmersionBar;
import com.zhaoxi.Open_source_Android.Config;
import com.zhaoxi.Open_source_Android.DAppConstants;
import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.base.BaseTitleBarActivity;
import com.zhaoxi.Open_source_Android.common.base.DappApplication;
import com.zhaoxi.Open_source_Android.common.view.CountDownTextView;
import com.zhaoxi.Open_source_Android.common.view.CountDownView;
import com.zhaoxi.Open_source_Android.common.view.PercentProgressBar;
import com.zhaoxi.Open_source_Android.common.view.VoteResultView;
import com.zhaoxi.Open_source_Android.common.view.refresh.MyPtrFrameLayout;
import com.zhaoxi.Open_source_Android.db.CreateDbWallet;
import com.zhaoxi.Open_source_Android.libs.tools.CreateQrAsyncTask;
import com.zhaoxi.Open_source_Android.libs.tools.ExportWalletAsyncTask;
import com.zhaoxi.Open_source_Android.libs.tools.SystemLog;
import com.zhaoxi.Open_source_Android.libs.utils.ChangeLanguageUtil;
import com.zhaoxi.Open_source_Android.libs.utils.ColdWalletUtil;
import com.zhaoxi.Open_source_Android.libs.utils.DateUtilSL;
import com.zhaoxi.Open_source_Android.libs.utils.SharedPreferencesUtil;
import com.zhaoxi.Open_source_Android.libs.utils.SlinUtil;
import com.zhaoxi.Open_source_Android.libs.utils.StrUtil;
import com.zhaoxi.Open_source_Android.net.BaseBet.CommonResultListener;
import com.zhaoxi.Open_source_Android.net.Request.GetHpbFeeRequest;
import com.zhaoxi.Open_source_Android.net.Request.ProposalDetailRequest;
import com.zhaoxi.Open_source_Android.net.Request.ProposalVoteRequest;
import com.zhaoxi.Open_source_Android.net.Request.VoteTransationReceiptRequest;
import com.zhaoxi.Open_source_Android.net.bean.MapEthBean;
import com.zhaoxi.Open_source_Android.net.bean.VotePersonBean;
import com.zhaoxi.Open_source_Android.net.bean.VoteZLBean;
import com.zhaoxi.Open_source_Android.web3.bean.WalletBean;
import com.zhaoxi.Open_source_Android.web3.utils.Convert;
import com.zhaoxi.Open_source_Android.web3.utils.TransferUtils;

import java.math.BigDecimal;
import java.math.BigInteger;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrFrameLayout;

public class VoteAlDetailsActivity extends BaseTitleBarActivity {
    public static final String ISSUE_NO = "VoteAlDetailsActivity.IssueNo";
    public static final String SIGN_CONTENT = "signContent";
    @BindView(R.id.activity_vote_zl_details_ptrfrsh_layout)
    MyPtrFrameLayout mRefreshLayout;
    @BindView(R.id.activity_vote_zl_details_ptrfrsh_content)
    ScrollView mContentView;
    @BindView(R.id.activity_vote_zl_details_title)
    TextView mTxtVoteTitle;
    @BindView(R.id.activity_vote_zl_details_des)
    TextView mTxtVoteDes;
    @BindView(R.id.vote_support)
    VoteResultView voteSupport;
    @BindView(R.id.vote_un_support)
    VoteResultView voteUnSupport;
    @BindView(R.id.vote_zuohui_yuanyin)
    FrameLayout mFrameLayout;
    @BindView(R.id.vote_zuohui_yuanyin_txt_des)
    TextView mTxtZfDes;
    @BindView(R.id.issue_detail_container)
    RelativeLayout issueDetailContainer;
    @BindView(R.id.iv_vote_status)
    ImageView ivVoteStatus;
    @BindView(R.id.tv_vote_status)
    TextView tvVoteStatus;
    @BindView(R.id.tv_issue_start_time)
    TextView tvIssueStartTime;
    @BindView(R.id.tv_count_down_time_pro)
    TextView tvTimePro;
    @BindView(R.id.tv_count_down_timer)
//    TimeTextView tvCountDownTimer;
            CountDownTextView tvCountDownTimer;
    @BindView(R.id.view_count_down)
    CountDownView viewCountDown;
    @BindView(R.id.tv_vote_total)
    TextView tvVoteTotal;
    @BindView(R.id.iv_vote_status_bg)
    ImageView ivVoteStatusBg;

    @BindView(R.id.ll_root)
    LinearLayout llRoot;

    private boolean isLoading = false;
    private String mIssuseNo;
    private VoteZLBean.VoteZlInfo mVoteZlInfo;
    private BigInteger mGasPrice;
    private BigInteger mGasLimit;
    private BigInteger mNonce;
    private Dialog mDialog;
    private String mTransationHax = "";
    private CountDownTimer mTimer;
    private boolean isVoteSuccese = false, isTimeEnd = false;
    private CreateDbWallet mCreateDbWallet;
    private WalletBean mWalletBean;
    private int mWalletType;
    private String mDefultAddress = "";
    private boolean isFisrt = true;
    private String value1N;
    private String value2N;
    private String des;
    // 扫码
    private static final int REQUEST_CODE_SCAN = 0x00;
    private int mSupportState;
    private String mPollNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote_al_details);
        ButterKnife.bind(this);
        initViews();
    }

    private void initViews() {
        ImmersionBar.with(this)
                .statusBarDarkFont(true, 0.2f)
                .init();
        setTitleBgColor(R.color.white, true);
        setTitle(getResources().getString(R.string.activity_vote_gl_txt_07), true);
        showRightTxtWithTextListener(getResources().getString(R.string.activity_vote_gl_txt_20), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goto_webView = new Intent(VoteAlDetailsActivity.this, CommonWebActivity.class);
                goto_webView.putExtra(CommonWebActivity.ACTIVITY_TITLE_INFO, getResources().getString(R.string.activity_vote_txt_062));
                goto_webView.putExtra(CommonWebActivity.WEBVIEW_LOAD_URL, Config.COMMON_WEB_URL + DAppConstants.backUrlHou(VoteAlDetailsActivity.this, 12));
                startActivity(goto_webView);
            }
        });

        mRefreshLayout.setLastUpdateTimeRelateObject(this);
        mRefreshLayout.setUltraPullToRefresh(mRefreshListener, mContentView);
        mRefreshLayout.changeWhiteBackgroud();

        mIssuseNo = getIntent().getStringExtra(ISSUE_NO);
        if (!StrUtil.isEmpty(mIssuseNo)) {
            initData();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mCreateDbWallet = new CreateDbWallet(this);
        mDefultAddress = SharedPreferencesUtil.getSharePreString(this, SharedPreferencesUtil.CHOOSE_WALLET_ADDRESS);
        if (!StrUtil.isEmpty(mDefultAddress)) {
            mWalletBean = mCreateDbWallet.queryWallet(this, mDefultAddress);
            mWalletType = mWalletBean.getIsClodWallet();
        }
    }

    private void initData() {
        isLoading = true;
        String address = SharedPreferencesUtil.getSharePreString(this, SharedPreferencesUtil.CHOOSE_WALLET_ADDRESS);
        if (isFisrt) {
            showProgressDialog();
        }
        new ProposalDetailRequest(mIssuseNo, address).doRequest(this, new CommonResultListener(this) {
            @Override
            public void onSuccess(JSONArray jsonArray) {
                isFisrt = false;
                mVoteZlInfo = JSON.parseObject(jsonArray.get(2).toString(), VoteZLBean.VoteZlInfo.class);
                if (mVoteZlInfo != null) {
                    tvTimePro.setText(getResources().getString(R.string.activity_vote_gl_txt_06_02));
                    voteSupport.setVoteEnable(false);
                    voteUnSupport.setVoteEnable(false);
                    switch (mVoteZlInfo.getVoteStatus()) {
                        case "1":
                            tvVoteStatus.setText(getResources().getString(R.string.activity_vote_gl_txt_10_01));
                            ivVoteStatus.setImageResource(R.mipmap.icon_vote_zl_underway);
                            issueDetailContainer.setBackgroundResource(R.drawable.shape_card_dark_bg);
                            tvTimePro.setText(getResources().getString(R.string.activity_vote_gl_txt_06_01));
//                            tvCountDownTimer.setTimes(DateUtilSL.ComputeCountdown(mVoteZlInfo.getEndTime() / 1000));
//                            tvCountDownTimer.setRun(true);
//                            tvCountDownTimer.setOnRunEndListener(new TimeTextView.OnRunEndListener() {
//                                @Override
//                                public void end() {
//                                    String strTime = "<font color='#FFFFFF'>0</font><font color='#C0C0C0' >" + getResources().getString(R.string.activity_vote_gl_txt_06_03)
//                                            + "</font><font color='#FFFFFF'>0</font><font color='#C0C0C0'>" + getResources().getString(R.string.activity_vote_gl_txt_06_04)
//                                            + "</font><font color='#FFFFFF'>0</font><font color='#C0C0C0' >" + getResources().getString(R.string.activity_vote_gl_txt_06_05)
//                                            + "</font>";
//                                    tvCountDownTimer.setText(strTime);
//                                }
//                            });
//                            tvCountDownTimer.run();

//                            tvCountDownTimer.setTimes(mVoteZlInfo.getEndTime() / 1000);
                            viewCountDown.setVisibility(View.VISIBLE);
                            tvCountDownTimer.setVisibility(View.GONE);
                            viewCountDown.setTimes(mVoteZlInfo.getEndTime() / 1_000);
                            ivVoteStatusBg.setImageResource(R.mipmap.icon_vote_zl_toupiao);
                            voteSupport.setVoteEnable(true);
                            voteUnSupport.setVoteEnable(true);
                            break;
                        case "2":
                            viewCountDown.setVisibility(View.GONE);
                            tvCountDownTimer.setVisibility(View.VISIBLE);
                            tvVoteStatus.setText(getResources().getString(R.string.activity_vote_gl_txt_10_02));
                            tvCountDownTimer.setAlpha(0.6f);
                            ivVoteStatus.setImageResource(R.mipmap.icon_vote_zl_announce);
                            issueDetailContainer.setBackgroundResource(R.drawable.shape_card_dark_bg);
                            ivVoteStatusBg.setImageResource(R.mipmap.icon_vote_zl_jiexiao);
                            tvCountDownTimer.setText(DateUtilSL.dateToStrymdhm2(DateUtilSL.getDateByLong(mVoteZlInfo.getEndTime(), 2)));
                            break;
                        case "3":
                            viewCountDown.setVisibility(View.GONE);
                            tvCountDownTimer.setVisibility(View.VISIBLE);
                            tvVoteStatus.setText(getResources().getString(R.string.activity_vote_gl_txt_10_03));
                            tvCountDownTimer.setAlpha(0.6f);
                            ivVoteStatus.setImageResource(R.mipmap.icon_vote_zl_pass);
                            issueDetailContainer.setBackgroundResource(R.drawable.shape_card_light_bg);
                            ivVoteStatusBg.setImageResource(R.mipmap.icon_vote_zl_tongguo);
                            tvCountDownTimer.setText(DateUtilSL.dateToStrymdhm2(DateUtilSL.getDateByLong(mVoteZlInfo.getEndTime(), 2)));
                            break;
                        case "4":
                            viewCountDown.setVisibility(View.GONE);
                            tvCountDownTimer.setVisibility(View.VISIBLE);
                            tvVoteStatus.setText(getResources().getString(R.string.activity_vote_gl_txt_10_04));
                            tvCountDownTimer.setAlpha(0.6f);
                            ivVoteStatus.setImageResource(R.mipmap.icon_vote_zl_vote_down);
                            issueDetailContainer.setBackgroundResource(R.drawable.shape_card_light_bg);
                            ivVoteStatusBg.setImageResource(R.mipmap.icon_vote_zl_foujue);
                            tvCountDownTimer.setText(DateUtilSL.dateToStrymdhm2(DateUtilSL.getDateByLong(mVoteZlInfo.getEndTime(), 2)));
                            break;
                        case "5":
                            viewCountDown.setVisibility(View.GONE);
                            tvCountDownTimer.setVisibility(View.VISIBLE);
                            tvVoteStatus.setText(getResources().getString(R.string.activity_vote_gl_txt_10_05));
                            tvTimePro.setText(getResources().getString(R.string.activity_vote_gl_txt_06_08));
                            tvCountDownTimer.setAlpha(0.6f);
                            ivVoteStatus.setImageResource(R.mipmap.icon_vote_zl_cancellation);
                            issueDetailContainer.setBackgroundResource(R.drawable.shape_card_light_bg);
                            ivVoteStatusBg.setImageResource(R.mipmap.icon_vote_zl_zuofei);
                            mFrameLayout.setVisibility(View.VISIBLE);
                            //议题作废时间:
                            mTxtZfDes.setText(getResources().getString(R.string.activity_vote_gl_txt_21));
                            tvCountDownTimer.setText(DateUtilSL.dateToStrymdhm2(DateUtilSL.getDateByLong(mVoteZlInfo.getEndTime(), 2)));
                            break;
                    }

                    int mLangusge = ChangeLanguageUtil.languageType(VoteAlDetailsActivity.this);
                    String title = mVoteZlInfo.getNameZh();
                    des = mVoteZlInfo.getDescZh();
                    value1N = mVoteZlInfo.getValue1Zh();
                    value2N = mVoteZlInfo.getValue2Zh();
                    if (mLangusge != 1) {//en
                        title = mVoteZlInfo.getNameEn();
                        des = mVoteZlInfo.getDescEn();
                        value1N = mVoteZlInfo.getValue1En();
                        value2N = mVoteZlInfo.getValue2En();
                    }
                    mTxtVoteTitle.setText(title);
                    mTxtVoteDes.setText(des);
                    tvIssueStartTime.setText(getResources().getString(R.string.activity_vote_gl_txt_06_06)
                            + DateUtilSL.dateToStrymdhm2(DateUtilSL.getDateByLong(mVoteZlInfo.getBeginTime(), 2)));
                    BigDecimal allNum = Convert.fromWei(new BigDecimal(mVoteZlInfo.getTotalNum()), Convert.Unit.ETHER);
                    tvVoteTotal.setText(getResources().getString(R.string.activity_vote_gl_txt_06_07)
                            + SlinUtil.NumberFormat0(VoteAlDetailsActivity.this, allNum));

                    voteSupport.setSupportStatus(true, value1N);
                    voteUnSupport.setSupportStatus(false, value2N);

                    BigDecimal value1 = Convert.fromWei(new BigDecimal(mVoteZlInfo.getValue1Num()), Convert.Unit.ETHER);
                    voteSupport.setPercent("(" + DateUtilSL.formatSPercent(mVoteZlInfo.getValue1Rate()) + ")");
                    voteSupport.setVoteNum("0.".startsWith(SlinUtil.NumberFormat0(VoteAlDetailsActivity.this, value1)) ? "0" : SlinUtil.NumberFormat0(VoteAlDetailsActivity.this, value1));
                    BigDecimal value2 = Convert.fromWei(new BigDecimal(mVoteZlInfo.getValue2Num()), Convert.Unit.ETHER);
                    voteUnSupport.setPercent("(" + DateUtilSL.formatSPercent(mVoteZlInfo.getValue2Rate()) + ")");
                    voteUnSupport.setVoteNum("0.".startsWith(SlinUtil.NumberFormat0(VoteAlDetailsActivity.this, value2)) ? "0" : SlinUtil.NumberFormat0(VoteAlDetailsActivity.this, value2));
                    BigDecimal SupportNum = Convert.fromWei(new BigDecimal(mVoteZlInfo.getPeragreeNum()), Convert.Unit.ETHER);
                    voteSupport.setVotedNum(getResources().getString(R.string.activity_vote_gl_txt_10_06_01) + SlinUtil.NumberFormat0(VoteAlDetailsActivity.this, SupportNum));
                    BigDecimal UnSupportNum = Convert.fromWei(new BigDecimal(mVoteZlInfo.getPerdisagreeNum()), Convert.Unit.ETHER);
                    voteUnSupport.setVotedNum(getResources().getString(R.string.activity_vote_gl_txt_10_06_01) + SlinUtil.NumberFormat0(VoteAlDetailsActivity.this, UnSupportNum));

                    voteSupport.setPercentProgressBarStyle(PercentProgressBar.Style.ONLY_SHOW_SUPPORT);
                    voteUnSupport.setPercentProgressBarStyle(PercentProgressBar.Style.ONLY_SHOW_UN_SUPPORT);

                    voteSupport.setPercentProgressBarSupportProgress(DateUtilSL.formatPercent(mVoteZlInfo.getValue1Rate()));
                    voteUnSupport.setPercentProgressBarUnSupportProgress(DateUtilSL.formatPercent(mVoteZlInfo.getValue2Rate()));

                    mRefreshLayout.refreshComplete();
                    dismissProgressDialog();
                    isLoading = false;
                }
            }

            @Override
            public void onError(String error) {
                isFisrt = false;
                dismissProgressDialog();
                mRefreshLayout.refreshComplete();
                isLoading = false;
                DappApplication.getInstance().showToast(error);
            }
        });

        voteSupport.setOnVoteClickListener(new VoteResultView.OnVoteClickListener() {
            @Override
            public void onVote(boolean isSupport) {
                showPopupWindow(isSupport);
            }
        });


        voteUnSupport.setOnVoteClickListener(new VoteResultView.OnVoteClickListener() {
            @Override
            public void onVote(boolean isSupport) {
                //判断是否为冷钱包
                showPopupWindow(isSupport);
            }
        });
    }

    private MyPtrFrameLayout.OnRefreshListener mRefreshListener = new MyPtrFrameLayout.OnRefreshListener() {
        @Override
        public void refresh(PtrFrameLayout frame) {
            if (!isLoading) {
                initData();
            }
        }
    };

    private void showPopupWindow(final boolean isSupport) {
        View view = LayoutInflater.from(this).inflate(R.layout.vote_dialog_layout, null, false);
        final PopupWindow popupWindow = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setAnimationStyle(R.style.VoteDialogAnim);
        popupWindow.setTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable());
        popupWindow.showAtLocation(llRoot, Gravity.BOTTOM, 0, 0);

        ImageView ivVoteStatus = view.findViewById(R.id.iv_support_status);
        TextView tvVoteStatus = view.findViewById(R.id.tv_support_status);
        final EditText etVoteNum = view.findViewById(R.id.et_vote_num);
        BigDecimal floorNum = Convert.fromWei(new BigDecimal(mVoteZlInfo.getFloorNum()), Convert.Unit.ETHER);
        etVoteNum.setHint(getResources().getString(R.string.activity_vote_gl_txt_05, "" + floorNum.toString()));
        TextView tvAllVote = view.findViewById(R.id.tv_all_vote);
        TextView tvVotedNum = view.findViewById(R.id.tv_voted_num);
        TextView tvUsedNum = view.findViewById(R.id.tv_used_num);
        Button btnVoteSure = view.findViewById(R.id.btn_vote_sure);

        int mLangusge = ChangeLanguageUtil.languageType(VoteAlDetailsActivity.this);
        BigDecimal votedNum = new BigDecimal(0);
        if (isSupport) {//支持
            votedNum = Convert.fromWei(new BigDecimal(mVoteZlInfo.getPeragreeNum()), Convert.Unit.ETHER);
            ivVoteStatus.setBackgroundResource(R.drawable.shape_support_point);
            if (mLangusge == 1) {//zh
                tvVoteStatus.setText(mVoteZlInfo.getValue1Zh());
            } else {
                tvVoteStatus.setText(mVoteZlInfo.getValue1En());
            }
        } else {
            votedNum = Convert.fromWei(new BigDecimal(mVoteZlInfo.getPerdisagreeNum()), Convert.Unit.ETHER);
            ivVoteStatus.setBackgroundResource(R.drawable.shape_unsupport_point);
            if (mLangusge == 1) {//zh
                tvVoteStatus.setText(mVoteZlInfo.getValue2Zh());
            } else {
                tvVoteStatus.setText(mVoteZlInfo.getValue2En());
            }
        }
        //已投
        tvVotedNum.setText(getResources().getString(R.string.activity_vote_gl_txt_10_06)
                + SlinUtil.NumberFormat0(VoteAlDetailsActivity.this, votedNum));
        //可用票数:50,000,000
        BigDecimal canNum = Convert.fromWei(new BigDecimal(mVoteZlInfo.getAviliableNum()), Convert.Unit.ETHER);
        BigDecimal mAllpoll = SlinUtil.ValueScale(canNum, 0);
        tvUsedNum.setText(getResources().getString(R.string.activity_vote_gl_txt_10_07) + SlinUtil.NumberFormat0(VoteAlDetailsActivity.this, mAllpoll));

        tvAllVote.setOnClickListener(new View.OnClickListener() {//全部
            @Override
            public void onClick(View v) {
                etVoteNum.setText("" + mAllpoll);
                etVoteNum.setSelection(mAllpoll.toString().length());
            }
        });

        btnVoteSure.setOnClickListener(new View.OnClickListener() {//投票
            @Override
            public void onClick(View v) {
                String poll = etVoteNum.getText().toString();
                if (StrUtil.isEmpty(poll)) {
                    DappApplication.getInstance().showToast(getResources().getString(R.string.activity_vote_txt_17));
                    return;
                }
                if (poll.equals("0")) {
                    DappApplication.getInstance().showToast(getResources().getString(R.string.activity_vote_txt_18));
                    return;
                }

                BigDecimal floorNum = Convert.fromWei(new BigDecimal(mVoteZlInfo.getFloorNum()), Convert.Unit.ETHER);
                if (floorNum.toBigInteger().compareTo(new BigInteger(poll)) > 0) {
                    DappApplication.getInstance().showToast(getResources().getString(R.string.activity_vote_gl_txt_23));
                    return;
                }

                if (mAllpoll.toBigInteger().compareTo(new BigInteger(poll)) < 0) {
                    DappApplication.getInstance().showToast(getResources().getString(R.string.activity_vote_gl_txt_16));
                    return;
                }

                if (mWalletType == 0) {
                    // 热钱包
                    //弹出密码框
                    popupWindow.dismiss();
                    popupVoteSureDialog(isSupport ? 1 : 0, poll);
                } else {
                    popupWindow.dismiss();
                    new GetHpbFeeRequest(mDefultAddress).doRequest(VoteAlDetailsActivity.this, new CommonResultListener(VoteAlDetailsActivity.this) {
                        @Override
                        public void onSuccess(JSONArray jsonArray) {
                            super.onSuccess(jsonArray);
                            MapEthBean mapEthInfo = JSON.parseObject(jsonArray.get(2).toString(), MapEthBean.class);
                            //交易中指定一个gas的单元价格
                            mGasPrice = new BigInteger(mapEthInfo.getGasPrice());
                            //最大交易次数
                            mGasLimit = new BigInteger(mapEthInfo.getGasLimit());
                            mNonce = mapEthInfo.getNonce();

                            // 冷钱包
                            com.zhaoxi.Open_source_Android.common.bean.VoteZLBean voteZLBean = new com.zhaoxi.Open_source_Android.common.bean.VoteZLBean();
                            String content = des.length() > 10 ? des.substring(0, 9) : des;
                            voteZLBean.setContent(content);
                            voteZLBean.setSuportStatus(value1N);
                            voteZLBean.setContractAddress(mVoteZlInfo.getIssurContractAddress());
                            voteZLBean.setFrom(mDefultAddress);
                            voteZLBean.setGaslimt(mGasLimit.toString());
                            voteZLBean.setGasprice(mGasPrice.toString());
                            voteZLBean.setNonce(mNonce.toString());
                            voteZLBean.setPollNum(poll);
                            voteZLBean.setData(getSignData(isSupport ? 1 : 0, poll));

                            mSupportState = isSupport ? 1 : 0;
                            mPollNum = poll;
//                            String strContent = "[" + "\"2\"" + "," + JSON.toJSONString(voteZLBean) + "]";
                            String strContent = ColdWalletUtil.toJson(ColdWalletUtil.TYPE_VOTE, voteZLBean);
                            SystemLog.D("showPopupWindow", "strContent = " + strContent);
                            showQrCodeDialog(strContent);
                        }
                    });

                }
            }
        });

        // 设置背景颜色变暗
        final WindowManager.LayoutParams lp = this.getWindow().getAttributes();
        lp.alpha = 0.7f;
        this.getWindow().setAttributes(lp);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                lp.alpha = 1f;
                getWindow().setAttributes(lp);
            }
        });

        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        popupWindow.setOutsideTouchable(true);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.BUTTON_BACK) {
                    popupWindow.dismiss();
                }
                return false;
            }
        });
    }

    private void popupVoteSureDialog(int isSuport, String num) {
        mDialog = new Dialog(this);
        mDialog.setCanceledOnTouchOutside(true);
        mDialog.show();
        Window window = mDialog.getWindow();
        if (window != null) {
            window.setContentView(R.layout.vote_sure_dialog_layout);
            window.setGravity(Gravity.CENTER);
            // 设置窗口弹出时动画
            window.setWindowAnimations(R.style.VoteDialogAnim);
            // 为了让系统弹出系统软件盘，必须加上这一句，也就是清除隐藏软键盘的状态，否则软键盘无法弹出
            window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

            TextView tvVoteTotalNum = window.findViewById(R.id.tv_vote_total_num);
            final EditText etPersonPwd = window.findViewById(R.id.et_person_pwd);
            TextView tvCancel = window.findViewById(R.id.tv_cancel);
            TextView tvSure = window.findViewById(R.id.tv_sure);

            String voteNum = SlinUtil.NumberFormat0(VoteAlDetailsActivity.this, new BigDecimal(num));
            tvVoteTotalNum.setText(Html.fromHtml("<font color='#54658B'>" + getResources().getString(R.string.activity_vote_gl_txt_14, " </font><font color='#02B8F7'>"
                    + voteNum + "</font><font color='#54658B'> ") + "</font>"));

            tvCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDialog.dismiss();
                }
            });

            tvSure.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String psd = etPersonPwd.getText().toString();
                    if (StrUtil.isEmpty(psd)) {
                        DappApplication.getInstance().showToast(getResources().getString(R.string.dailog_psd_edit_hint));
                        return;
                    }
                    String curAddress = SharedPreferencesUtil.getSharePreString(VoteAlDetailsActivity.this, SharedPreferencesUtil.CHOOSE_WALLET_ADDRESS);
                    ExportWalletAsyncTask asyncTask = new ExportWalletAsyncTask(VoteAlDetailsActivity.this, curAddress, psd, 10);
                    asyncTask.setOnResultListener(new ExportWalletAsyncTask.OnResultExportListener() {
                        @Override
                        public void setOnResultListener(String result) {
                            if (result.startsWith("Failed") || result.contains("失败")) {
                                DappApplication.getInstance().showToast(getResources().getString(R.string.transfer_activity_dialog_txt_06));
                            } else {
                                showProgressDialog();
                                VoteSign(isSuport, num, curAddress, result);
                            }
                        }
                    });
                    asyncTask.execute();
                }
            });
        }
    }

    private void VoteSign(int isSuport, String pollNum, String address, String privateKey) {
        //获取gasLimit gasPrice
        new GetHpbFeeRequest(address).doRequest(VoteAlDetailsActivity.this, new CommonResultListener(VoteAlDetailsActivity.this) {
            @Override
            public void onSuccess(JSONArray jsonArray) {
                super.onSuccess(jsonArray);
                MapEthBean mapEthInfo = JSON.parseObject(jsonArray.get(2).toString(), MapEthBean.class);
                //交易中指定一个gas的单元价格
                mGasPrice = new BigInteger(mapEthInfo.getGasPrice());
                //最大交易次数
                mGasLimit = new BigInteger(mapEthInfo.getGasLimit());
                mNonce = mapEthInfo.getNonce();

                BigDecimal valueData = Convert.toWei(new BigDecimal(pollNum), Convert.Unit.ETHER);
                String signedData = TransferUtils.VoteProposalSignDataMethod(mVoteZlInfo.getIssueNo(), mNonce, mGasPrice, mGasLimit, privateKey,
                        mVoteZlInfo.getIssurContractAddress(), valueData, isSuport);
                new ProposalVoteRequest(mVoteZlInfo.getIssueNo(), address, isSuport, valueData.toString(), signedData)
                        .doRequest(VoteAlDetailsActivity.this, new CommonResultListener(VoteAlDetailsActivity.this) {
                            @Override
                            public void onSuccess(JSONArray jsonArray) {
                                mDialog.dismiss();
                                mTransationHax = jsonArray.get(2).toString();
                                dismissProgressDialog();
                                showProgress();
                            }
                        });
            }
        });
    }

    /**
     * 冷钱包获取签名数据
     *
     * @param isSupport 是否支持
     * @param pollNum   投票数量
     * @return 签名数据
     */
    private String getSignData(int isSupport, String pollNum) {
        BigDecimal valueData = Convert.toWei(new BigDecimal(pollNum), Convert.Unit.ETHER);
        return TransferUtils.getVoteProposalSignDataMethodData(mVoteZlInfo.getIssueNo(), valueData, isSupport);
    }

    /**
     * 冷钱包投票
     *
     * @param isSupport   是否支持
     * @param pollNum     投票数
     * @param address     当前钱包地址
     * @param signContent 签名后的签名内容
     */
    private void vote(int isSupport, String pollNum, String address, String signContent) {
        BigDecimal valueData = Convert.toWei(new BigDecimal(pollNum), Convert.Unit.ETHER);
        new ProposalVoteRequest(mVoteZlInfo.getIssueNo(), address, isSupport, valueData.toString(), signContent)
                .doRequest(VoteAlDetailsActivity.this, new CommonResultListener(VoteAlDetailsActivity.this) {
                    @Override
                    public void onSuccess(JSONArray jsonArray) {
                        mTransationHax = jsonArray.get(2).toString();
                        dismissProgressDialog();
                        showProgress();
                    }
                });
    }

    private void showProgress() {
        getTransactionReceipt();
        mTimer = new CountDownTimer(60 * 1000 + 500, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (!isShowDialog()) {
                    showTextProgressDialog(getResources().getString(R.string.activity_vote_gl_txt_17));
                }
            }

            @Override
            public void onFinish() {
                dismissTextProgressDialog();
                isTimeEnd = true;
            }
        };
        mTimer.start();
    }

    private void getTransactionReceipt() {
        if (!isTimeEnd && !isVoteSuccese) {
            new VoteTransationReceiptRequest(mTransationHax).doRequest(this, new CommonResultListener(this) {
                @Override
                public void onSuccess(JSONArray jsonArray) {
                    super.onSuccess(jsonArray);
                    //0x1代表成功，0x0代表失败，null就是还在pending
                    if (jsonArray.get(2) == null) {//继续转圈
                        getTransactionReceipt();
                        isVoteSuccese = false;
                    } else {
                        isVoteSuccese = true;
                        VotePersonBean VotePersonBean = JSON.parseObject(jsonArray.get(2).toString(), VotePersonBean.class);
                        String status = VotePersonBean.getStatus();
                        if ("0x1".equals(status)) {
                            DappApplication.getInstance().showToast(getResources().getString(R.string.activity_vote_txt_21));
                            initData();
                        } else {
                            DappApplication.getInstance().showToast(getResources().getString(R.string.activity_vote_txt_33));
                        }
                        dismissTextProgressDialog();
                        mTimer.cancel();
                    }
                }

                @Override
                public void onError(String error) {
                    dismissTextProgressDialog();
                    DappApplication.getInstance().showToast(error);
                }
            });
        }
    }


    private void showQrCodeDialog(String strContent) {
        View view = LayoutInflater.from(this).inflate(R.layout.view_digital_sign_qrcode_layout, null, false);
        final PopupWindow popupWindow = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setAnimationStyle(R.style.VoteDialogAnim);
        popupWindow.setTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable());
        popupWindow.showAtLocation(llRoot, Gravity.CENTER, 0, 0);

        ImageView ivDigitalSignQrCode = view.findViewById(R.id.iv_qr_code_info);
        Bitmap logo = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_qrcode_logo, null);
        CreateQrAsyncTask asyncTask = new CreateQrAsyncTask(this, ivDigitalSignQrCode, strContent, logo);
        asyncTask.setOnResultListener(new CreateQrAsyncTask.OnResultListener() {
            @Override
            public void setOnResultListener(Bitmap bitmap) {
                // 生成的bitmap
            }
        });
        asyncTask.execute();//开始执行

        Button btnNextTip = view.findViewById(R.id.btn_next_tip);
        btnNextTip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                // 下一步，跳转到扫码页面
                Intent intent = new Intent(VoteAlDetailsActivity.this, ScaningActivity.class);
                intent.putExtra(ScaningActivity.RESURE_TYPE, ScaningActivity.TYPE_VOTE_ZL);
                startActivityForResult(intent, REQUEST_CODE_SCAN);
            }
        });

        // 设置背景透明度
        final WindowManager.LayoutParams lp = this.getWindow().getAttributes();
        lp.alpha = 0.7f;
        this.getWindow().setAttributes(lp);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                lp.alpha = 1f;
                getWindow().setAttributes(lp);
            }
        });

        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.BUTTON_BACK) {
                    popupWindow.dismiss();
                }
                return false;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK && data != null) {
            String result = data.getStringExtra(SIGN_CONTENT);
            if (!TextUtils.isEmpty(result)) {
                SystemLog.D("onActivityResult", "signContent = " + result);
                vote(mSupportState, mPollNum, mDefultAddress, result);
            }
        }
    }
}
