package com.zhaoxi.Open_source_Android.ui.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gyf.immersionbar.ImmersionBar;
import com.zhaoxi.Open_source_Android.DAppConstants;
import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.base.BaseTitleBarActivity;
import com.zhaoxi.Open_source_Android.libs.utils.DateUtilSL;
import com.zhaoxi.Open_source_Android.libs.utils.SharedPreferencesUtil;
import com.zhaoxi.Open_source_Android.libs.utils.SlinUtil;
import com.zhaoxi.Open_source_Android.libs.utils.StrUtil;
import com.zhaoxi.Open_source_Android.net.bean.TranferRecordBean;
import com.zhaoxi.Open_source_Android.web3.utils.Convert;

import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class TransferRecodeDetailsActivity extends BaseTitleBarActivity {
    public static final String TRANSFER_RECODE_DETAILS = "TransferRecodeDetailsActivity.data";
    public static final String TRANSFER_RECODE_ADDRESS = "TransferRecodeDetailsActivity.address";
    public static final String TRANSFER_RECODE_RESOUCE = "TransferRecodeDetailsActivity.resouce";
    public static final String TRANSFER_RECODE_TYPE = "TransferRecodeDetailsActivity.type";
    public static final String TRANSFER_RECODE_TOKEN_SYMBOL = "TransferRecodeDetailsActivity.tokenSymbol";


    @BindView(R.id.transation_record_details_txt_money)
    TextView mTxtTransferMoney;
    @BindView(R.id.transation_record_details_txt_fromaddress)
    TextView mTxtFromAddress;
    @BindView(R.id.transation_record_details_txt_toaddress)
    TextView mTxtToAddress;
    @BindView(R.id.transation_record_details_txt_palymoney)
    TextView mTxtPalyMoney;
    @BindView(R.id.transation_record_details_txt_tansfernum)
    TextView mTxtTansferNum;
    @BindView(R.id.transation_record_details_txt_qukuai)
    TextView mTxtQukuai;
    @BindView(R.id.transation_record_details_txt_time)
    TextView mTxtTime;
    @BindView(R.id.transation_record_details_txt_resouce)
    TextView mTxtResouce;
    @BindView(R.id.transaction_record_detail_iv_status)
    ImageView mIvStatus;

    private TranferRecordBean.TransferInfo mTransferInfo;
    private String mAddress;
    private int mResouce = 0;

    @BindView(R.id.tv_token_id)
    TextView mTvTokenId;
    @BindView(R.id.iv_token_header)
    CircleImageView mIvTokenHeader;
    @BindView(R.id.tv_catch_more)
    TextView mTvCatchMore;
    @BindView(R.id.item_token_type)
    TextView mTokenType;
    @BindView(R.id.view_token_id_more)
    View viewTokenIdMore;
    @BindView(R.id.transaction_tv_fee_title)
    TextView mTransFeeTitle;
    @BindView(R.id.transaction_record_line)
    View mViewLine;
    private TextView mTvTokenId1;
    private CircleImageView mTokenHeader;
    private String mTransType;
    private String mTokenURI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_recode_details);
        ButterKnife.bind(this);
        mTransferInfo = (TranferRecordBean.TransferInfo) getIntent().getSerializableExtra(TRANSFER_RECODE_DETAILS);
        mTransType = getIntent().getStringExtra(TRANSFER_RECODE_TYPE);//交易类型
        initViews();
        initData();
    }

    private void initData() {
        mAddress = SharedPreferencesUtil.getSharePreString(this, SharedPreferencesUtil.CHOOSE_WALLET_ADDRESS);
        switch (mTransType) {
            case DAppConstants.TYPE_HRC_721:
                mTxtPalyMoney.setVisibility(View.GONE);
                mTransFeeTitle.setVisibility(View.GONE);
                mTokenType.setVisibility(View.VISIBLE);
                mTokenType.setText(mTransType);
                viewTokenIdMore.setVisibility(View.VISIBLE);
                mTvTokenId1 = viewTokenIdMore.findViewById(R.id.tv_token_id);
                mTokenHeader = viewTokenIdMore.findViewById(R.id.iv_token_header);
                mTokenURI = mTransferInfo.getTokenURI();
                if (!TextUtils.isEmpty(mTokenURI)) {
                    mTokenHeader.setVisibility(View.VISIBLE);
                    Glide.with(TransferRecodeDetailsActivity.this)
                            .load(mTokenURI)
                            .centerCrop()
                            .placeholder(R.mipmap.icon_default_header)
                            .error(R.mipmap.icon_default_header)
                            .into(mTokenHeader);
                }
                mTvTokenId1.setText(mTransferInfo.getTokenId());
                mTxtTransferMoney.setTextColor(getResources().getColor(R.color.color_black_333));
                mTxtTransferMoney.setText(mTransferInfo.getTokenSymbol());
                if (mAddress.equals(mTransferInfo.getFromAccount())) {
                    mIvStatus.setImageResource(R.mipmap.icon_transfer_out_big);
                } else {
                    mIvStatus.setImageResource(R.mipmap.icon_transfer_into_big);
                }

                setupData(mTransferInfo.getFromAccount()
                        , mTransferInfo.getToAccount()
                        , String.valueOf(mTransferInfo.getActulTxFee())
                        , mTransferInfo.getTransactionHash()
                        , String.valueOf(mTransferInfo.getBlockNumber())
                        , DateUtilSL.dateToStrymdhms(DateUtilSL.getDateByLong(mTransferInfo.getTimestap(), 0))
                );

                break;
            case DAppConstants.TYPE_HRC_20:
                mTokenType.setVisibility(View.VISIBLE);
                mTokenType.setText(mTransType);
                mTxtPalyMoney.setVisibility(View.GONE);
                mTransFeeTitle.setVisibility(View.GONE);
                if (mAddress.equals(mTransferInfo.getFromAccount())) {
                    mIvStatus.setImageResource(R.mipmap.icon_transfer_out_big);
                    mTxtTransferMoney.setTextColor(getResources().getColor(R.color.color_E86A6A));
                    mTxtTransferMoney.setText("-" + SlinUtil.NumberFormat0(this, new BigDecimal(mTransferInfo.getValue())) + " " + mTransferInfo.getTokenSymbol());
                } else {
                    mIvStatus.setImageResource(R.mipmap.icon_transfer_into_big);
                    mTxtTransferMoney.setTextColor(getResources().getColor(R.color.color_44C8A3));
                    mTxtTransferMoney.setText("+" + SlinUtil.NumberFormat0(this, new BigDecimal(mTransferInfo.getValue())) + " " + mTransferInfo.getTokenSymbol());
                }
                setupData(mTransferInfo.getFromAccount()
                        , mTransferInfo.getToAccount()
                        , String.valueOf(mTransferInfo.getActulTxFee())
                        , mTransferInfo.getTransactionHash()
                        , String.valueOf(mTransferInfo.getBlockNumber())
                        , DateUtilSL.dateToStrymdhms(DateUtilSL.getDateByLong(mTransferInfo.getTimestap(), 0))
                );
                break;
            case DAppConstants.TYPE_HPB:
                mTokenType.setVisibility(View.GONE);
                mAddress = getIntent().getStringExtra(TRANSFER_RECODE_ADDRESS);
                mResouce = getIntent().getIntExtra(TRANSFER_RECODE_RESOUCE, 0);
                if (mTransferInfo == null || StrUtil.isEmpty(mAddress)) {
                    finish();
                }

                BigDecimal money = new BigDecimal("" + mTransferInfo.getValue());
                String strMoney = SlinUtil.formatNumFromWeiT(this, money, 18) + " " + getResources().getString(R.string.wallet_manager_txt_money_unit_01);
                if (mAddress.equals(mTransferInfo.getFromAccount().toLowerCase())) {//转出
                    mTxtTransferMoney.setText("-" + strMoney);
                    mTxtTransferMoney.setTextColor(getResources().getColor(R.color.color_E86A6A));
                    mIvStatus.setImageResource(R.mipmap.icon_transfer_out_big);
                } else {
                    mTxtTransferMoney.setText("+" + strMoney);
                    mTxtTransferMoney.setTextColor(getResources().getColor(R.color.color_44C8A3));
                    mIvStatus.setImageResource(R.mipmap.icon_transfer_into_big);
                }

                setupData(mTransferInfo.getFromAccount()
                        , mTransferInfo.getToAccount()
                        , mTransferInfo.getActulTxFee()
                        , mTransferInfo.getTransactionHash()
                        , mTransferInfo.getBlockNumber()
                        , DateUtilSL.dateToStrymdhms(DateUtilSL.getDateByLong(mTransferInfo.getTimestap(), 0)));


                break;
        }
    }

    /**
     * 设置数据
     *
     * @param fromAccount 发款方
     * @param toAccount   收款方
     * @param fee         交易费用
     * @param transNum    交易号
     * @param blockNum    区块号
     * @param transTime   交易时间
     */
    private void setupData(String fromAccount, String toAccount, String fee, String transNum, String blockNum, String transTime) {
        mTxtFromAddress.setText(fromAccount);
        mTxtToAddress.setText(toAccount);
        BigDecimal txFee = Convert.fromWei(fee, Convert.Unit.ETHER);
        String strFee = txFee.toPlainString();
        if (mResouce == 1) {
            if (strFee.length() > 11) {
                mTxtPalyMoney.setText(txFee.toPlainString().substring(0, 11) + " " + getResources().getString(R.string.wallet_manager_txt_money_unit_03));
            } else {
                mTxtPalyMoney.setText(strFee + " " + getResources().getString(R.string.wallet_manager_txt_money_unit_03));
            }
        } else {
            if (strFee.length() > 10) {
                mTxtPalyMoney.setText(txFee.toPlainString().substring(0, 10) + " " + getResources().getString(R.string.wallet_manager_txt_money_unit_01));
            } else {
                mTxtPalyMoney.setText(strFee + " " + getResources().getString(R.string.wallet_manager_txt_money_unit_01));
            }
        }

        mTxtTansferNum.setText(transNum);
        mTxtQukuai.setText(blockNum);
        mTxtTime.setText(transTime);
    }

    private void initViews() {
        if (mResouce == 1) {
            mTxtResouce.setText(getResources().getString(R.string.main_map_txt_transfer_succese));
            setTitle(R.string.main_map_txt_transfer_details_title, true);
        } else {
            ImmersionBar.with(this)
                    .statusBarDarkFont(true, 0.2f)
                    .init();
            setTitleBgColor(R.color.white, true);
            if (mTransType.equals(DAppConstants.TYPE_HPB)) {
                setTitle(R.string.tansfer_recode_details_txt_title, true);
            } else {
                setTitle(R.string.activity_records_txt_02, true);
            }

        }
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @OnClick({R.id.transation_record_details_img_fromaddress, R.id.transation_record_details_img_toaddress,
            R.id.transation_record_details_img_tansfernum, R.id.transation_record_details_img_liulanqi,
            R.id.iv_token_header, R.id.transation_record_details_txt_tansfernum})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.transation_record_details_img_fromaddress:
                String fromAddress = mTxtFromAddress.getText().toString();
                if (!StrUtil.isEmpty(fromAddress)) {
                    copyLable(fromAddress);
                }
                break;
            case R.id.transation_record_details_img_toaddress:
                String toAddress = mTxtToAddress.getText().toString();
                if (!StrUtil.isEmpty(toAddress)) {
                    copyLable(toAddress);
                }
                break;
            case R.id.transation_record_details_img_tansfernum:
                String content = mTxtTansferNum.getText().toString();
                if (!StrUtil.isEmpty(content)) {
                    copyLable(content);
                }
                break;
            case R.id.transation_record_details_img_liulanqi:
                String transferHax = mTxtTansferNum.getText().toString();
                if (!StrUtil.isEmpty(transferHax)) {
                    if (mResouce == 1) {
                        goWeb(getResources().getString(R.string.tansfer_recode_details_txt_title),
                                DAppConstants.ETHER_SACN_URL + transferHax, getResources().getString(R.string.tansfer_recode_details_txt_title));
                    } else {
                        goWeb(getResources().getString(R.string.tansfer_recode_details_txt_title),
                                DAppConstants.HPB_SACN_URL + transferHax, getResources().getString(R.string.tansfer_recode_details_txt_title));
                    }
                }
                break;
            case R.id.iv_token_header:
                Intent intent = new Intent(this, ImgCatchActivity.class);
                intent.putExtra(ImgCatchActivity.IMG_URL, mTokenURI);
                Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(this, mTokenHeader, "share").toBundle();
                startActivity(intent, bundle);
                break;
            case R.id.transation_record_details_txt_tansfernum:
                String transferHax1 = mTxtTansferNum.getText().toString();
                if (!StrUtil.isEmpty(transferHax1)) {
                    if (mResouce == 1) {
                        goWeb(getResources().getString(R.string.tansfer_recode_details_txt_title),
                                DAppConstants.ETHER_SACN_URL + transferHax1, getResources().getString(R.string.tansfer_recode_details_txt_title));
                    } else {
                        goWeb(getResources().getString(R.string.tansfer_recode_details_txt_title),
                                DAppConstants.HPB_SACN_URL + transferHax1, getResources().getString(R.string.tansfer_recode_details_txt_title));
                    }
                }
                break;

//            case R.id.tv_catch_more:
//                // 查看更多
//                Intent data = new Intent(this, TokenMoreActivity.class);
//                data.putExtra(TokenMoreActivity.CONTRACT_ADDRESS, mContractAddress);
//                data.putExtra(TokenMoreActivity.TRANS_HASH, mTransInfoTransactionHash);
//                startActivity(data);
//                break;
        }
    }

    private void goWeb(String title, String url, String des) {
        Intent goto_webView = new Intent(this, NewsWebActivity.class);
        goto_webView.putExtra(NewsWebActivity.ACTIVITY_TITLE_INFO, title);
        goto_webView.putExtra(NewsWebActivity.WEBVIEW_TYPE, 1);
        goto_webView.putExtra(NewsWebActivity.ACTIVITY_DES_INFO, des);
        goto_webView.putExtra(NewsWebActivity.WEBVIEW_LOAD_URL, url);
        startActivity(goto_webView);
    }
}
