package com.zhaoxi.Open_source_Android.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.gyf.immersionbar.ImmersionBar;
import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.base.BaseTitleBarActivity;
import com.zhaoxi.Open_source_Android.common.base.DappApplication;
import com.zhaoxi.Open_source_Android.common.view.refresh.MyPtrFrameLayout;
import com.zhaoxi.Open_source_Android.libs.utils.DateUtilSL;
import com.zhaoxi.Open_source_Android.libs.utils.SharedPreferencesUtil;
import com.zhaoxi.Open_source_Android.libs.utils.SlinUtil;
import com.zhaoxi.Open_source_Android.net.BaseBet.NetResultCallBack;
import com.zhaoxi.Open_source_Android.net.Request.GetNodeBonusRequest;
import com.zhaoxi.Open_source_Android.net.UrlContext;
import com.zhaoxi.Open_source_Android.net.bean.NodeDividenBean;
import com.zhaoxi.Open_source_Android.web3.utils.Convert;

import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.srain.cube.views.ptr.PtrFrameLayout;

public class NodeDividendActivity extends BaseTitleBarActivity {

    @BindView(R.id.activity_node_dividend_layout_isnot_node)
    LinearLayout mLayoutIsnotNode;
    @BindView(R.id.activity_node_dividend_layout_has_node)
    LinearLayout mLayoutHasNode;
    @BindView(R.id.activity_node_dividend_txt_node_name)
    TextView mTxtNodeName;
    @BindView(R.id.activity_node_dividend_txt_node_boe_address)
    TextView mTxtBOEAddress;
    @BindView(R.id.activity_node_dividend_txt_node_address)
    TextView mTxtCBAddress;
    @BindView(R.id.activity_node_dividend_txt_node_rank)
    TextView mTxtNodeRank;
    @BindView(R.id.activity_node_dividend_txt_node_poll)
    TextView mTxtNodePoll;
    @BindView(R.id.activity_node_dividend_txt_node_lpoll)
    TextView mTxtNodeLpoll;
    @BindView(R.id.activity_node_dividend_txt_node_earn)
    TextView mTxtNodeEarn;
    @BindView(R.id.activity_node_dividend_btn_how_to_node)
    Button mBtnHowToNode;
    @BindView(R.id.activity_node_dividend_btn_goto_send)
    Button mBtnGotoSend;
    @BindView(R.id.activity_node_dividend_btn_goto_look)
    Button mBtnGotoLook;
    @BindView(R.id.activty_node_dividend_refresh_layout)
    MyPtrFrameLayout mRefreshLayout;
    @BindView(R.id.activty_node_dividend_refresh_content)
    ScrollView mLayoutContent;

    private boolean isLoading = false, isFisrt = true; // 标注是否正在加载 防止多次加载
    private String mDefultAddress;
    private NodeDividenBean mNodeDividenBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_node_dividend);
        ButterKnife.bind(this);
        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        isFisrt = true;
        initData();
    }

    private void initViews() {
        ImmersionBar.with(this)
                .statusBarDarkFont(false, 0.2f)
                .init();
        setTitleBgColor(R.color.base_new_theme_color, false);
        setTitle(getResources().getString(R.string.activity_cion_fh_txt_01), true);

        mRefreshLayout.setLastUpdateTimeRelateObject(this);
        mRefreshLayout.setUltraPullToRefresh(mRefreshListener, mLayoutContent);
        mRefreshLayout.changeFlshBackgroud(R.color.base_new_theme_color);
    }

    private void initData() {
        isLoading = true;
        if(isFisrt)showProgressDialog();
        mDefultAddress = SharedPreferencesUtil.getSharePreString(this, SharedPreferencesUtil.CHOOSE_WALLET_ADDRESS);
        new GetNodeBonusRequest(mDefultAddress, UrlContext.Url_NodeBonus.getContext())
                .doRequest(this, new NetResultCallBack() {
                    @Override
                    public void onSuccess(JSONArray jsonArray) {
                        isFisrt = false;
                        if (jsonArray.get(2) != null) {
                            mNodeDividenBean = JSON.parseObject(jsonArray.get(2).toString(), NodeDividenBean.class);
                            if ("0".equals(mNodeDividenBean.getIsNode())) {//否
                                mBtnHowToNode.setVisibility(View.VISIBLE);
                                mLayoutIsnotNode.setVisibility(View.VISIBLE);
                                mLayoutHasNode.setVisibility(View.GONE);
                                mBtnGotoSend.setVisibility(View.GONE);
                                mBtnGotoLook.setVisibility(View.GONE);
                            } else {
                                mBtnHowToNode.setVisibility(View.GONE);
                                mLayoutIsnotNode.setVisibility(View.GONE);
                                mTxtNodeName.setText(mNodeDividenBean.getName());
                                mTxtBOEAddress.setText(mDefultAddress);
                                mTxtCBAddress.setText(mNodeDividenBean.getHolderAddress());
                                mTxtNodeRank.setText(mNodeDividenBean.getRank());
                                BigDecimal poll = Convert.fromWei(new BigDecimal(mNodeDividenBean.getNum()), Convert.Unit.ETHER);
                                mTxtNodePoll.setText(SlinUtil.NumberFormat0(NodeDividendActivity.this, SlinUtil.ValueScale(poll,0)));
                                BigDecimal Allpoll = Convert.fromWei(new BigDecimal(mNodeDividenBean.getTotalCurrentNum()), Convert.Unit.ETHER);
                                mTxtNodeLpoll.setText(SlinUtil.NumberFormat0(NodeDividendActivity.this, SlinUtil.ValueScale(Allpoll,0)));
                                mTxtNodeEarn.setText(DateUtilSL.formatAPercent(mNodeDividenBean.getBonusRate()));
                                mLayoutHasNode.setVisibility(View.VISIBLE);
                                mBtnGotoSend.setVisibility(View.VISIBLE);
                                mBtnGotoLook.setVisibility(View.VISIBLE);
                                showRightTxtWithTextListener(getResources().getString(R.string.activity_cion_fh_txt_12), new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        //分红记录
                                        startActivity(new Intent(NodeDividendActivity.this, NodeDividendRecordsActivity.class));
                                    }
                                });
                            }
                        }
                        dismissProgressDialog();
                        mRefreshLayout.refreshComplete();
                        isLoading = false;
                    }

                    @Override
                    public void onError(String error) {
                        dismissProgressDialog();
                        isFisrt = false;
                        DappApplication.getInstance().showToast(error);
                        isLoading = false;
                        mRefreshLayout.refreshComplete();
                    }
                });
    }

    @OnClick({R.id.activity_node_dividend_btn_how_to_node, R.id.activity_node_dividend_btn_goto_send, R.id.activity_node_dividend_btn_goto_look})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.activity_node_dividend_btn_how_to_node:
                Intent goto_webView = new Intent(this, CommonWebActivity.class);
                goto_webView.putExtra(CommonWebActivity.ACTIVITY_TITLE_INFO, getResources().getString(R.string.activity_cion_fh_txt_09));
                goto_webView.putExtra(CommonWebActivity.WEBVIEW_LOAD_URL, "http://www.Open_source_Android.io/node");
                startActivity(goto_webView);
                break;
            case R.id.activity_node_dividend_btn_goto_send:
                Intent it_set = new Intent(NodeDividendActivity.this, SendDevidendActivity.class);
                it_set.putExtra(SendDevidendActivity.CONTRACTADDR, mNodeDividenBean.getBonusContractAddr());
                it_set.putExtra(SendDevidendActivity.BILI, mNodeDividenBean.getBonusRate());
                it_set.putExtra(SendDevidendActivity.IS_CAN_SET, mNodeDividenBean.getIsCanSet().equals("1") ? true : false);
                startActivity(it_set);
                break;
            case R.id.activity_node_dividend_btn_goto_look:
                Intent it_de = new Intent(NodeDividendActivity.this, NodeVoteDetailsActivity.class);
                it_de.putExtra(NodeVoteDetailsActivity.DEFULT_BILE, mNodeDividenBean.getBonusRate());
                startActivity(it_de);
                break;
        }
    }

    /**
     * 下拉刷新监听事件
     */
    private MyPtrFrameLayout.OnRefreshListener mRefreshListener = new MyPtrFrameLayout.OnRefreshListener() {
        @Override
        public void refresh(PtrFrameLayout frame) {
            if (!isLoading) {
                initData();
            }
        }
    };
}
