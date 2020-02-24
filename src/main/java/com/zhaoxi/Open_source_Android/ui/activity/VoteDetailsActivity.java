package com.zhaoxi.Open_source_Android.ui.activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.zhaoxi.Open_source_Android.DAppConstants;
import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.base.BaseTitleBarActivity;
import com.zhaoxi.Open_source_Android.common.base.DappApplication;
import com.zhaoxi.Open_source_Android.common.bean.VoteSignBean;
import com.zhaoxi.Open_source_Android.common.dialog.CommonPsdPop;
import com.zhaoxi.Open_source_Android.common.view.refresh.MyPtrFrameLayout;
import com.zhaoxi.Open_source_Android.db.CreateDbWallet;
import com.zhaoxi.Open_source_Android.libs.tools.CommonDilogTool;
import com.zhaoxi.Open_source_Android.libs.tools.CreateQrAsyncTask;
import com.zhaoxi.Open_source_Android.libs.tools.ExportWalletAsyncTask;
import com.zhaoxi.Open_source_Android.libs.tools.SystemLog;
import com.zhaoxi.Open_source_Android.libs.utils.ColdWalletUtil;
import com.zhaoxi.Open_source_Android.libs.utils.SharedPreferencesUtil;
import com.zhaoxi.Open_source_Android.libs.utils.SlinUtil;
import com.zhaoxi.Open_source_Android.libs.utils.StrUtil;
import com.zhaoxi.Open_source_Android.net.BaseBet.CommonResultListener;
import com.zhaoxi.Open_source_Android.net.Request.GetHpbFeeRequest;
import com.zhaoxi.Open_source_Android.net.Request.PersonalInfoRequest;
import com.zhaoxi.Open_source_Android.net.Request.VoteSetHolderAddrRequest;
import com.zhaoxi.Open_source_Android.net.Request.VoteTransationReceiptRequest;
import com.zhaoxi.Open_source_Android.net.Request.VoteVoteInfoRequest;
import com.zhaoxi.Open_source_Android.net.Request.VoteVoteRequest;
import com.zhaoxi.Open_source_Android.net.bean.MapEthBean;
import com.zhaoxi.Open_source_Android.net.bean.VoteBean;
import com.zhaoxi.Open_source_Android.net.bean.VotePersonBean;
import com.zhaoxi.Open_source_Android.ui.dialog.VoteChangeAddressDialog;
import com.zhaoxi.Open_source_Android.ui.dialog.VoteDialog;
import com.zhaoxi.Open_source_Android.web3.datatypes.Address;
import com.zhaoxi.Open_source_Android.web3.datatypes.Bool;
import com.zhaoxi.Open_source_Android.web3.datatypes.Function;
import com.zhaoxi.Open_source_Android.web3.datatypes.FunctionEncoder;
import com.zhaoxi.Open_source_Android.web3.utils.Convert;
import com.zhaoxi.Open_source_Android.web3.utils.TransferUtils;

import java.math.BigDecimal;
import java.math.BigInteger;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.srain.cube.views.ptr.PtrFrameLayout;

public class VoteDetailsActivity extends BaseTitleBarActivity {
    public static final String VOTE_DETAILS = "VoteDetailsActivity.VOTE_DETAILS";
    private static final int REQUEST_CODE_SCAN = 0x0001;
    private static final int REQUEST_CODE_SCAN_V = 0x0002;
    public static final String SIGN_CONTENT = "signContent";

    @BindView(R.id.activity_vote_details_des)
    TextView mTxtDes;
    @BindView(R.id.activity_vote_details_poll)
    TextView mTxtPoll;
    @BindView(R.id.activity_vote_details_poll_day)
    TextView mTxtPollDay;
    @BindView(R.id.activity_vote_dteails_voting)
    Button mBtnVote;
    @BindView(R.id.activity_vote_dteails_cancel_vote)
    Button mBtnCancelVote;
    @BindView(R.id.activity_vote_dteails_layout_wait)
    LinearLayout mLayoutWait;
    @BindView(R.id.activity_vote_dteails_voteinfo_txt_changeaddress)
    TextView mTxtChangeAddress;
    @BindView(R.id.activity_vote_dteails_txt_wait)
    TextView mTxtWait;
    @BindView(R.id.root_vote_details)
    LinearLayout mRootView;

    @BindView(R.id.refresh_vote_detail)
    MyPtrFrameLayout mRefreshVoteDetail;
    @BindView(R.id.vote_detail_container)
    RelativeLayout mVoteDetailContainer;

    private VoteBean.VoteInfo mVoteInfo;
    private BigDecimal mCurAllMoney = new BigDecimal(0);

    private VoteDialog.Builder tipDialog;
    private VoteDialog mDialog;
    private VoteChangeAddressDialog.Builder mchangeDialogBuilder;
    private VoteChangeAddressDialog mChangeDialog;
    private CountDownTimer mTimer;
    private BigInteger mGasPrice;
    private BigInteger mGasLimit;
    private BigInteger mNonce;
    private String mCurAddress, mCandidateAddress, mHolderAddr;
    private boolean isVoteSuccese = false, isTimeEnd = false;
    private boolean isApproval = false;//是否是授权状态
    private int type;
    private int voteDialogFlag = -1, mCurFlag;
    private String mTransationHax = "";
    private String voteContractAddress, mCbAddress;
    private CreateDbWallet mCreateDbWallet;
    private String mId;
    private boolean isColdWallet;
    private String mCurcbAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote_details);
        ButterKnife.bind(this);
        initView();
        mVoteInfo = (VoteBean.VoteInfo) getIntent().getSerializableExtra(VOTE_DETAILS);
        if (mVoteInfo == null) {
            finish();
        }
        setTitle(mVoteInfo.getName(), true);
        mTxtDes.setMovementMethod(ScrollingMovementMethod.getInstance());
        initData();

        initListener();
    }

    @SuppressLint("ClickableViewAccessibility")
    public void initListener() {
        mTxtDes.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_MOVE:
                        // 禁用下拉刷新
                        mRefreshVoteDetail.setDisallowPullRefresh(true);
                        break;
                    case MotionEvent.ACTION_UP:
                        // 不禁用下拉刷新
                        mRefreshVoteDetail.setDisallowPullRefresh(false);
                        break;
                }

                return false;
            }
        });
    }

    /**
     * 初始化下拉刷新
     */
    private void initView() {
        mRefreshVoteDetail.setLastUpdateTimeRelateObject(this);
        mRefreshVoteDetail.setUltraPullToRefreshWithDisallow(new MyPtrFrameLayout.OnRefreshListener() {
            @Override
            public void refresh(PtrFrameLayout frame) {
                getVoteInfo();
            }
        }, mVoteDetailContainer);
        // 不禁用下拉刷新
        mRefreshVoteDetail.setDisallowPullRefresh(false);
        mRefreshVoteDetail.changeBackgroud();
    }

    private void initData() {
        mCreateDbWallet = new CreateDbWallet(this);
        voteContractAddress = SharedPreferencesUtil.getSharePreString(this, DAppConstants.VOTE_CONTRACT_ADDRESS);
        //查询候选人信息
        mCurAddress = SharedPreferencesUtil.getSharePreString(this, SharedPreferencesUtil.CHOOSE_WALLET_ADDRESS);
        mCandidateAddress = mVoteInfo.getCoinbase();//候选人地址
        mTxtDes.setText(mVoteInfo.getDescription());
        BigDecimal money = Convert.fromWei(mVoteInfo.getCount(), Convert.Unit.ETHER);
        mTxtPoll.setText("" + SlinUtil.NumberFormat0(this, SlinUtil.ValueScale(money, 0)));
        mId = mVoteInfo.getId() == null ? mVoteInfo.getVoterId() : mVoteInfo.getId();
        //判断当前钱包是否是冷钱包
        isColdWallet = mCreateDbWallet.isColdWallet(this, mCurAddress);
        getVoteInfo();
    }

    private void getVoteInfo() {
        new VoteVoteInfoRequest(mId, mCurAddress).doRequest(this, new CommonResultListener(this) {
            @Override
            public void onSuccess(JSONArray jsonArray) {
                mRefreshVoteDetail.refreshComplete();
                VotePersonBean VotePersonBean = JSON.parseObject(jsonArray.get(2).toString(), VotePersonBean.class);
                if (StrUtil.isEmpty(mCandidateAddress)) {
                    mCandidateAddress = VotePersonBean.getCoinbase();// 候选人地址
                }

                // 描述信息
                mTxtDes.setText(VotePersonBean.getDescription());
                // 当前投票数
                BigDecimal money = Convert.fromWei(VotePersonBean.getCount(), Convert.Unit.ETHER);
                mTxtPoll.setText("" + SlinUtil.NumberFormat0(VoteDetailsActivity.this, SlinUtil.ValueScale(money, 0)));


                mTxtPollDay.setText(VotePersonBean.getAddress());
                String isRunUpStage = VotePersonBean.getIsRunUpStage();
                if ("0".equals(isRunUpStage)) {//0-竞选结束
                    if ("1".equals(VotePersonBean.getFlag())) {//1-容许修改持币地址
                        isApproval = true;
                        mTxtChangeAddress.setVisibility(View.VISIBLE);
                        mHolderAddr = VotePersonBean.getHolderAddr();
                        mTxtChangeAddress.setText(getResources().getString(R.string.activity_vote_details_txt_02));
                    } else if ("2".equals(VotePersonBean.getFlag())) {
                        isApproval = false;
                        mHolderAddr = VotePersonBean.getHolderAddr();
                        mTxtChangeAddress.setVisibility(View.VISIBLE);
                        mTxtChangeAddress.setText(getResources().getString(R.string.activity_vote_details_txt_06));
                    } else {
                        mTxtChangeAddress.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onError(String error) {
                super.onError(error);
                mRefreshVoteDetail.refreshComplete();
            }
        });
    }

    /**
     * 显示投票数量设置对话框
     *
     * @param name     节点名称
     * @param maxCount 最大数量
     * @param flags    是否是投票
     */
    private void showVotePollDialog(String name, BigDecimal maxCount, int flags) {
        boolean isColdWallet = mCreateDbWallet.isColdWallet(VoteDetailsActivity.this, mCurAddress);
        tipDialog = new VoteDialog.Builder(this);
        tipDialog.setVoteDialogVoteListener(new VoteDialog.Builder.VoteDialogVoteListener() {
            @Override
            public void onHandleVote(String psd, String poll, int flags) {
                showComfirmDilaog(psd, poll, flags);
            }

            @Override
            public void onHandleVote(String poll, int flags) {
                showColdConfDialog(poll, flags);
            }
        });

        tipDialog.setName(name);
        tipDialog.setIsColdWallet(isColdWallet);
        tipDialog.setMaxVoteCount(maxCount);
        tipDialog.setFlags(flags);
        mDialog = tipDialog.create();
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setCancelable(false);
        mDialog.show();

        WindowManager.LayoutParams params = mDialog.getWindow().getAttributes();
        params.height = (int) (this.getWindowManager().getDefaultDisplay().getHeight() * 0.8);
        params.width = (int) (this.getWindowManager().getDefaultDisplay().getWidth() * 1);
        mDialog.getWindow().setAttributes(params);
    }

    private void showProgress() {
        getTransactionReceipt();
        mTimer = new CountDownTimer(60 * 1000 + 500, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (mLayoutWait.getVisibility() != View.VISIBLE) {
                    if (type == 1) {//修改地址
                        mTxtWait.setText(getResources().getString(R.string.activity_vote_details_txt_08));
                    } else if (type == 2) {//授权
                        mTxtWait.setText(getResources().getString(R.string.activity_vote_details_txt_07));
                    } else {
                        if (voteDialogFlag == VoteDialog.FLAGS_CANCEL_VOTE) {// 撤票中...
                            mTxtWait.setText(getResources().getString(R.string.activity_cancel_vote_txt_32));
                        } else {// 投票中...
                            mTxtWait.setText(getResources().getString(R.string.activity_vote_txt_32));
                        }
                    }

                    mLayoutWait.setVisibility(View.VISIBLE);
                }
                mBtnVote.setEnabled(false);
            }

            @Override
            public void onFinish() {
                if (mLayoutWait.getVisibility() != View.GONE) {
                    mLayoutWait.setVisibility(View.GONE);
                }
                isTimeEnd = true;
                mBtnVote.setEnabled(true);
            }
        };
        mTimer.start();
    }

    // 根据Hash查询成功Or失败状态
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
                            if (type == 0) {
                                if (voteDialogFlag == VoteDialog.FLAGS_CANCEL_VOTE) {
                                    DappApplication.getInstance().showToast(getResources().getString(R.string.activity_cancel_vote_txt_21));
                                } else {
                                    DappApplication.getInstance().showToast(getResources().getString(R.string.activity_vote_txt_21));

                                }
                            } else if (type == 1) {//弹出框
                                showSetAddrDialog(mCbAddress, mVoteInfo.getName());
                            } else {
                                getVoteInfo();
                                DappApplication.getInstance().showToast(getResources().getString(R.string.activity_vote_details_txt_09_01));
                            }
                        } else {
                            if (type == 0) {
                                if (voteDialogFlag == VoteDialog.FLAGS_CANCEL_VOTE) {
                                    DappApplication.getInstance().showToast(getResources().getString(R.string.activity_cancel_vote_txt_33));
                                } else {
                                    DappApplication.getInstance().showToast(getResources().getString(R.string.activity_vote_txt_33));
                                }
                            } else if (type == 1) {
                                DappApplication.getInstance().showToast(getResources().getString(R.string.activity_vote_details_txt_09_02));
                            } else {
                                DappApplication.getInstance().showToast(getResources().getString(R.string.activity_vote_details_txt_10_02));
                            }
                        }
                        if (mLayoutWait.getVisibility() != View.GONE) {
                            mLayoutWait.setVisibility(View.GONE);
                        }
                        mBtnVote.setEnabled(true);
                        mTimer.cancel();
                    }
                }
            });
        }
    }

    @OnClick({R.id.activity_vote_dteails_voting, R.id.activity_vote_dteails_cancel_vote, R.id.activity_vote_dteails_voteinfo_txt_changeaddress})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.activity_vote_dteails_voteinfo_txt_changeaddress:
                changeAddress();
                break;
            case R.id.activity_vote_dteails_voting:
                voteBtn();
                break;
            case R.id.activity_vote_dteails_cancel_vote:
                // 撤销投票
                cancelVote();
                break;
        }
    }

    private void changeAddress() {
        if (isApproval) {
            mchangeDialogBuilder = new VoteChangeAddressDialog.Builder(this);
            mchangeDialogBuilder.setVoteDialogVoteListener(new VoteChangeAddressDialog.Builder.VoteDialogVoteListener() {
                @Override
                public void onHandleCahnge(String psd, String address) {
                    if (mCandidateAddress.equals(mCurAddress) && mCurAddress.equals(address)) {
                        isApproval = false;
                    } else {
                        if (mHolderAddr.equals(address)) {
                            DappApplication.getInstance().showToast(getResources().getString(R.string.activity_vote_details_txt_11));
                            return;
                        }
                    }
                    showChangeAddress(psd, address);
                }

                @Override
                public void onHandleCahnge(String address) {
                    showChangeAddress("", address);
                }
            });
            mchangeDialogBuilder.setIsColdWallet(isColdWallet);
            mChangeDialog = mchangeDialogBuilder.create(mHolderAddr);
            mChangeDialog.setCanceledOnTouchOutside(false);
            mChangeDialog.setCancelable(false);
            mChangeDialog.show();

            WindowManager.LayoutParams params = mChangeDialog.getWindow().getAttributes();
            params.height = (int) (this.getWindowManager().getDefaultDisplay().getHeight() * 0.8);
            params.width = (int) (this.getWindowManager().getDefaultDisplay().getWidth() * 1);
            mChangeDialog.getWindow().setAttributes(params);
        } else {//只输入密码
            boolean isColdWallet = mCreateDbWallet.isColdWallet(VoteDetailsActivity.this, mCurAddress);
            if (isColdWallet) {//弹出需签名信息二维码
                handleClodWallet(mHolderAddr);
            } else {
                CommonPsdPop commonPsdPop = new CommonPsdPop(this, mRootView);
                commonPsdPop.setHandlePsd(new CommonPsdPop.HandlePsd() {
                    @Override
                    public void getInputPsd(String psd) {
                        //获取私钥
                        ExportWalletAsyncTask asyncTask = new ExportWalletAsyncTask(VoteDetailsActivity.this, mCurAddress, psd, 10);
                        asyncTask.setOnResultListener(new ExportWalletAsyncTask.OnResultExportListener() {
                            @Override
                            public void setOnResultListener(String result) {
                                if (result.startsWith("Failed") || result.contains("失败")) {
                                    DappApplication.getInstance().showToast(getResources().getString(R.string.transfer_activity_dialog_txt_06));
                                } else {
                                    showProgressDialog();
                                    changeAddrSign(mHolderAddr, result);
                                }
                            }
                        });
                        asyncTask.execute();
                    }
                });
                commonPsdPop.show(getResources().getString(R.string.transfer_activity_dialog_txt_07));
            }
        }
    }

    /**
     * 撤销投票
     */
    private void cancelVote() {
        showProgressDialog();
        mCurAddress = SharedPreferencesUtil.getSharePreString(this, SharedPreferencesUtil.CHOOSE_WALLET_ADDRESS);

        new PersonalInfoRequest(mCurAddress, mCandidateAddress).doRequest(this, new CommonResultListener(this) {
            @Override
            public void onSuccess(JSONArray jsonArray) {
                super.onSuccess(jsonArray);
                String status = (String) jsonArray.get(0);
                if ("000000".equals(status)) {//代表处理成功
                    dismissProgressDialog();
                    VotePersonBean mapEthInfo = JSON.parseObject(jsonArray.get(2).toString(), VotePersonBean.class);
                    BigDecimal numForNode = new BigDecimal(mapEthInfo.getNumForNode());
                    BigDecimal mNumForNode = Convert.fromWei(numForNode, Convert.Unit.ETHER);
                    //获取最多可撤票数并截取不保存小数
                    BigDecimal poll = SlinUtil.ValueScale(mNumForNode, 0);
                    showVotePollDialog(mVoteInfo.getName(), poll, VoteDialog.FLAGS_CANCEL_VOTE);
                }

            }

            @Override
            public void onError(String error) {
                super.onError(error);
                dismissProgressDialog();
            }
        });
    }

    private void voteBtn() {
        showProgressDialog();
        //获取当前默认地址
        mCurAddress = SharedPreferencesUtil.getSharePreString(this, SharedPreferencesUtil.CHOOSE_WALLET_ADDRESS);

        // mCurAddress:当前默认地址
        // mCandidateAddress：候选人地址
        new PersonalInfoRequest(mCurAddress, mCandidateAddress).doRequest(this, new CommonResultListener(this) {
            @Override
            public void onSuccess(JSONArray jsonArray) {
                super.onSuccess(jsonArray);
                String status = (String) jsonArray.get(0);
                if ("000000".equals(status)) {//代表处理成功
                    dismissProgressDialog();
                    VotePersonBean mapEthInfo = JSON.parseObject(jsonArray.get(2).toString(), VotePersonBean.class);
                    BigDecimal money = new BigDecimal(mapEthInfo.getAviliable());
                    mCurAllMoney = Convert.fromWei(money, Convert.Unit.ETHER);
                    //获取当前剩余票数 并截取不保存小数
                    BigDecimal poll = SlinUtil.ValueScale(mCurAllMoney, 0);
                    showVotePollDialog(mVoteInfo.getName(), poll, VoteDialog.FLAGS_VOTE);
                }
            }
        });
    }

    private void showColdConfDialog(String poll, int flags) {
        CommonDilogTool dialogTool = new CommonDilogTool(this);
        String ruleDes = DAppConstants.getVoteAgainRule(this);
        dialogTool.show(null, ruleDes, null,
                getResources().getString(R.string.dailog_psd_btn_cancle), null,
                getResources().getString(R.string.dailog_psd_btn_ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        isTimeEnd = false;
                        isVoteSuccese = false;
                        mCurFlag = flags;
                        showProgressDialog();
                        new GetHpbFeeRequest(mCurAddress).doRequest(VoteDetailsActivity.this, new CommonResultListener(VoteDetailsActivity.this) {
                            @Override
                            public void onSuccess(JSONArray jsonArray) {
                                super.onSuccess(jsonArray);
                                MapEthBean mapEthInfo = JSON.parseObject(jsonArray.get(2).toString(), MapEthBean.class);
                                //交易中指定一个gas的单元价格
                                mGasPrice = new BigInteger(mapEthInfo.getGasPrice());
                                //最大交易次数  2500000
                                mGasLimit = new BigInteger("2500000");
                                mNonce = mapEthInfo.getNonce();
                                String methodName;
                                VoteSignBean voteSignBean = new VoteSignBean();
                                String content = "";
                                if (flags == VoteDialog.FLAGS_CANCEL_VOTE) {
                                    methodName = "cancelVoteForCandidate";// 撤销投票
                                    voteSignBean.setSuportStatus(getResources().getString(R.string.activity_vote_txt_28));
                                    content = getResources().getString(R.string.activity_cold_sign_txt_02, mVoteInfo.getName());
                                } else {
                                    methodName = "vote";// 投票
                                    voteSignBean.setSuportStatus(getResources().getString(R.string.activity_vote_txt_13));
                                    content = getResources().getString(R.string.activity_cold_sign_txt_03, mVoteInfo.getName());
                                }

                                BigDecimal valueData = Convert.toWei(poll, Convert.Unit.ETHER);
                                String data = TransferUtils.getVoteData(methodName, mCandidateAddress, mCurAddress, valueData);
                                voteSignBean.setFrom(mCurAddress);
                                voteSignBean.setNonce(mNonce.toString());
                                voteSignBean.setGaslimt(mGasLimit.toString());
                                voteSignBean.setGasprice(mGasPrice.toString());
                                voteSignBean.setPollNum(poll);
                                voteSignBean.setContent(content);
                                voteSignBean.setContractAddress(voteContractAddress);
                                voteSignBean.setData(data);

                                String strContent = ColdWalletUtil.toJson(ColdWalletUtil.TYPE_VOTE_V, voteSignBean);
                                if (mDialog != null && mDialog.isShowing()) mDialog.dismiss();
                                dialogTool.dismiss();
                                showQrCodeDialog(strContent, 1);
                            }
                        });
                    }
                });
    }

    private void showComfirmDilaog(String psd, String poll, int flags) {
        CommonDilogTool dialogTool = new CommonDilogTool(this);
        String ruleDes = DAppConstants.getVoteAgainRule(this);
        dialogTool.show(null, ruleDes, null,
                getResources().getString(R.string.dailog_psd_btn_cancle), null,
                getResources().getString(R.string.dailog_psd_btn_ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        isTimeEnd = false;
                        isVoteSuccese = false;
                        ExportWalletAsyncTask asyncTask = new ExportWalletAsyncTask(VoteDetailsActivity.this, mCurAddress, psd, 10);
                        asyncTask.setOnResultListener(new ExportWalletAsyncTask.OnResultExportListener() {
                            @Override
                            public void setOnResultListener(String result) {
                                if (result.startsWith("Failed") || result.contains("失败")) {
                                    DappApplication.getInstance().showToast(getResources().getString(R.string.transfer_activity_dialog_txt_06));
                                } else {
                                    showProgressDialog();
                                    //乘以18
                                    BigDecimal valueData = Convert.toWei(poll, Convert.Unit.ETHER);
                                    //获取nonce, mGasPrice, mGasLimit
                                    //获取gasLimit gasPrice
                                    new GetHpbFeeRequest(mCurAddress).doRequest(VoteDetailsActivity.this, new CommonResultListener(VoteDetailsActivity.this) {
                                        @Override
                                        public void onSuccess(JSONArray jsonArray) {
                                            super.onSuccess(jsonArray);
                                            MapEthBean mapEthInfo = JSON.parseObject(jsonArray.get(2).toString(), MapEthBean.class);
                                            //交易中指定一个gas的单元价格
                                            mGasPrice = new BigInteger(mapEthInfo.getGasPrice());
                                            //最大交易次数  2500000
                                            mGasLimit = new BigInteger("2500000");
                                            mNonce = mapEthInfo.getNonce();
                                            String methodName;
                                            if (flags == VoteDialog.FLAGS_CANCEL_VOTE) {
                                                methodName = "cancelVoteForCandidate";// 撤销投票
                                            } else {
                                                methodName = "vote";// 投票
                                            }
                                            String signedData = TransferUtils.tokenVote(methodName, mNonce, mGasPrice, mGasLimit, result,
                                                    voteContractAddress, mCandidateAddress, mCurAddress, valueData);
                                            new VoteVoteRequest(signedData).doRequest(VoteDetailsActivity.this, new CommonResultListener(VoteDetailsActivity.this) {
                                                @Override
                                                public void onSuccess(JSONArray jsonArray) {
                                                    super.onSuccess(jsonArray);
                                                    mTransationHax = jsonArray.get(2).toString();
                                                    dismissProgressDialog();
                                                    voteDialogFlag = flags;
                                                    showProgress();
                                                }
                                            });
                                        }
                                    });
                                }
                            }
                        });
                        asyncTask.execute();
                        dialog.dismiss();
                        mDialog.dismiss();
                    }
                });

    }

    private void handleClodWallet(String chibiAddr) {
        showProgressDialog();
        new GetHpbFeeRequest(mCurAddress).doRequest(VoteDetailsActivity.this, new CommonResultListener(VoteDetailsActivity.this) {
            @Override
            public void onSuccess(JSONArray jsonArray) {
                super.onSuccess(jsonArray);
                dismissProgressDialog();
                MapEthBean mapEthInfo = JSON.parseObject(jsonArray.get(2).toString(), MapEthBean.class);
                //交易中指定一个gas的单元价格
                mGasPrice = new BigInteger(mapEthInfo.getGasPrice());
                mGasLimit = new BigInteger(mapEthInfo.getGasLimit());
                mNonce = mapEthInfo.getNonce();

                String name = "setHolderAddr";
                String curAddr = "";
                if (isApproval) {
                    type = 1;
                    name = "setApproval";
                } else {
                    type = 2;
                    curAddr = mCurAddress;
                }
                mCurcbAddress = chibiAddr;

                Function function = null;
                if ("setApproval".equals(name)) {
                    Address tAddress = new Address(chibiAddr);//持币地址
                    function = TransferUtils.setApproval(name, tAddress, new Bool(true));
                } else {
                    Address _coinBase = new Address(mVoteInfo.getCoinbase());//coinBaseBOE地址
                    Address _holderAddr = new Address(curAddr);//持币地址
                    function = TransferUtils.setHolderAddr(name, _coinBase, _holderAddr);
                }

                String data = FunctionEncoder.encode(function);
                VoteSignBean votePersonBean = new VoteSignBean();
                votePersonBean.setFrom(mCurAddress);
                votePersonBean.setNonce(mNonce.toString());
                votePersonBean.setGaslimt(mGasLimit.toString());
                votePersonBean.setGasprice(mGasPrice.toString());
                votePersonBean.setContent(getResources().getString(R.string.activity_cold_sign_txt_01, mVoteInfo.getName()));
                votePersonBean.setContractAddress(voteContractAddress);
                votePersonBean.setData(data);

                String strContent = ColdWalletUtil.toJson(ColdWalletUtil.TYPE_AUTHOR, votePersonBean);
                if (mChangeDialog != null && mChangeDialog.isShowing()) mChangeDialog.dismiss();
                SystemLog.D("showPopupWindow", "strContent = " + strContent);
                showQrCodeDialog(strContent, 2);
            }
        });
    }

    private void showChangeAddress(String psd, String adddress) {
        boolean isColdWallet = mCreateDbWallet.isColdWallet(VoteDetailsActivity.this, mCurAddress);
        if (isColdWallet) {//弹出需签名信息二维码
            handleClodWallet(adddress);
        } else {
            ExportWalletAsyncTask asyncTask = new ExportWalletAsyncTask(VoteDetailsActivity.this, mCurAddress, psd, 10);
            asyncTask.setOnResultListener(new ExportWalletAsyncTask.OnResultExportListener() {
                @Override
                public void setOnResultListener(String result) {
                    if (result.startsWith("Failed") || result.contains("失败")) {
                        DappApplication.getInstance().showToast(getResources().getString(R.string.transfer_activity_dialog_txt_06));
                    } else {
                        showProgressDialog();
                        changeAddrSign(adddress, result);
                    }
                }
            });
            asyncTask.execute();
        }
    }

    private void changeAddrSign(String chibiAddress, String privateKey) {
        //获取gasLimit gasPrice
        new GetHpbFeeRequest(mCurAddress).doRequest(VoteDetailsActivity.this, new CommonResultListener(VoteDetailsActivity.this) {
            @Override
            public void onSuccess(JSONArray jsonArray) {
                super.onSuccess(jsonArray);
                MapEthBean mapEthInfo = JSON.parseObject(jsonArray.get(2).toString(), MapEthBean.class);
                //交易中指定一个gas的单元价格
                mGasPrice = new BigInteger(mapEthInfo.getGasPrice());
                //最大交易次数
                mGasLimit = new BigInteger(mapEthInfo.getGasLimit());
                mNonce = mapEthInfo.getNonce();

                String name = "setHolderAddr";
                String curAddr = "";
                if (isApproval) {
                    type = 1;
                    name = "setApproval";
                } else {
                    type = 2;
                    curAddr = mCurAddress;
                }
                mCurcbAddress = chibiAddress;

                String signedData = TransferUtils.ChangeAddrSignDataMethod(name, chibiAddress, mVoteInfo.getCoinbase(), mNonce, mGasPrice, mGasLimit, privateKey, voteContractAddress, curAddr);
                dismissProgressDialog();
                new VoteSetHolderAddrRequest(signedData).doRequest(VoteDetailsActivity.this, new CommonResultListener(VoteDetailsActivity.this) {
                    @Override
                    public void onSuccess(JSONArray jsonArray) {
                        if (mChangeDialog != null && mChangeDialog.isShowing()) {
                            mChangeDialog.dismiss();
                        }
                        mCbAddress = chibiAddress;
                        mTransationHax = jsonArray.get(2).toString();
                        dismissProgressDialog();

                        showProgress();
                    }
                });
            }
        });
    }

    /**
     * 修改持币地址成功 弹出框
     */
    private void showSetAddrDialog(String cbAddr, String nodeName) {
        CommonDilogTool dialogTool = new CommonDilogTool(this);
        dialogTool.show(getResources().getString(R.string.activity_vote_details_txt_10_03),
                getResources().getString(R.string.activity_vote_details_txt_10_01, cbAddr, nodeName), null,
                null, null,
                getResources().getString(R.string.dailog_psd_btn_ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getVoteInfo();
                        dialog.dismiss();
                    }
                });
    }

    private void showQrCodeDialog(String strContent, int showType) {
        View view = LayoutInflater.from(this).inflate(R.layout.view_digital_sign_qrcode_layout, null, false);
        final PopupWindow popupWindow = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setAnimationStyle(R.style.VoteDialogAnim);
        popupWindow.setTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable());
        popupWindow.showAtLocation(mRootView, Gravity.CENTER, 0, 0);

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
                if (showType == 2) {
                    Intent intent = new Intent(VoteDetailsActivity.this, ScaningActivity.class);
                    intent.putExtra(ScaningActivity.RESURE_TYPE, ScaningActivity.TYPE_VOTE);
                    startActivityForResult(intent, REQUEST_CODE_SCAN);
                } else if (showType == 1) {
                    Intent intent = new Intent(VoteDetailsActivity.this, ScaningActivity.class);
                    intent.putExtra(ScaningActivity.RESURE_TYPE, ScaningActivity.TYPE_VOTE_V);
                    startActivityForResult(intent, REQUEST_CODE_SCAN_V);
                }
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
    protected void onDestroy() {
        super.onDestroy();
        if (mTimer != null) {
            mTimer.cancel();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK && data != null) {
            String result = data.getStringExtra(SIGN_CONTENT);
            if (!TextUtils.isEmpty(result)) {
                new VoteSetHolderAddrRequest(result).doRequest(VoteDetailsActivity.this, new CommonResultListener(VoteDetailsActivity.this) {
                    @Override
                    public void onSuccess(JSONArray jsonArray) {
                        if (mChangeDialog != null && mChangeDialog.isShowing()) {
                            mChangeDialog.dismiss();
                        }
                        mCbAddress = mCurcbAddress;
                        mTransationHax = jsonArray.get(2).toString();
                        dismissProgressDialog();
                        showProgress();
                    }
                });
            }
        } else if (requestCode == REQUEST_CODE_SCAN_V && resultCode == RESULT_OK && data != null) {
            String result1 = data.getStringExtra(SIGN_CONTENT);
            if (!TextUtils.isEmpty(result1)) {
                new VoteVoteRequest(result1).doRequest(VoteDetailsActivity.this, new CommonResultListener(VoteDetailsActivity.this) {
                    @Override
                    public void onSuccess(JSONArray jsonArray) {
                        super.onSuccess(jsonArray);
                        mTransationHax = jsonArray.get(2).toString();
                        dismissProgressDialog();
                        voteDialogFlag = mCurFlag;
                        showProgress();
                    }
                });
            }
        }
    }
}
