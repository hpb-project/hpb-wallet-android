package com.zhaoxi.Open_source_Android.ui.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.zhaoxi.Open_source_Android.DAppConstants;
import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.base.BaseFragment;
import com.zhaoxi.Open_source_Android.common.base.DappApplication;
import com.zhaoxi.Open_source_Android.common.dialog.CommonCopyDialog;
import com.zhaoxi.Open_source_Android.common.view.recycler.LoadMoreRecyclerAdapter;
import com.zhaoxi.Open_source_Android.common.view.recycler.LoadMoreRecyclerView;
import com.zhaoxi.Open_source_Android.common.view.refresh.MyPtrFrameLayout;
import com.zhaoxi.Open_source_Android.db.CreateDbWallet;
import com.zhaoxi.Open_source_Android.db.DBManager;
import com.zhaoxi.Open_source_Android.db.HistoryCache;
import com.zhaoxi.Open_source_Android.libs.tools.CommonDilogTool;
import com.zhaoxi.Open_source_Android.libs.tools.MainChangePagerNewsListener;
import com.zhaoxi.Open_source_Android.libs.utils.ChangeLanguageUtil;
import com.zhaoxi.Open_source_Android.libs.utils.CollectionUtil;
import com.zhaoxi.Open_source_Android.libs.utils.SharedPreferencesUtil;
import com.zhaoxi.Open_source_Android.libs.utils.StrUtil;
import com.zhaoxi.Open_source_Android.net.BaseBet.CommonResultListener;
import com.zhaoxi.Open_source_Android.net.BaseBet.NetResultCallBack;
import com.zhaoxi.Open_source_Android.net.Request.GetBannerNewsRequest;
import com.zhaoxi.Open_source_Android.net.Request.GetNewsRequest;
import com.zhaoxi.Open_source_Android.net.Request.VoteStateRequest;
import com.zhaoxi.Open_source_Android.net.UrlContext;
import com.zhaoxi.Open_source_Android.net.bean.BannerInfo;
import com.zhaoxi.Open_source_Android.net.bean.NewsBean;
import com.zhaoxi.Open_source_Android.net.bean.VotePersonBean;
import com.zhaoxi.Open_source_Android.ui.activity.CreateWalletActivity;
import com.zhaoxi.Open_source_Android.ui.activity.DappsWebActivity;
import com.zhaoxi.Open_source_Android.ui.activity.DigitalSignActivity;
import com.zhaoxi.Open_source_Android.ui.activity.MainActivity;
import com.zhaoxi.Open_source_Android.ui.activity.MainmapOneActivity;
import com.zhaoxi.Open_source_Android.ui.activity.NewsWebActivity;
import com.zhaoxi.Open_source_Android.ui.activity.NodeDividendActivity;
import com.zhaoxi.Open_source_Android.ui.activity.SendRedPacketsActivity;
import com.zhaoxi.Open_source_Android.ui.activity.VoteALActivity;
import com.zhaoxi.Open_source_Android.ui.activity.VoteActivity;
import com.zhaoxi.Open_source_Android.ui.activity.VoteWaitActivity;
import com.zhaoxi.Open_source_Android.ui.activity.WalletDetailsActivity;
import com.zhaoxi.Open_source_Android.ui.adapter.NewsAdapter;
import com.zhaoxi.Open_source_Android.web3.bean.WalletBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import in.srain.cube.views.ptr.PtrFrameLayout;

import static android.app.Activity.RESULT_OK;

public class MainNewsFragment extends BaseFragment {
    @BindView(R.id.recycler_with_refresh_list_content_news)
    LoadMoreRecyclerView mRecyclerView;
    @BindView(R.id.recycler_with_refresh_ptr_frame_layout_news)
    MyPtrFrameLayout mRefreshPtrFrameLayout;

    private LoadMoreRecyclerAdapter mListAdapter;

    Unbinder unbinder;
    private List<BannerInfo.BaseInfo> mBannerInfos = new ArrayList<>();
    private MainActivity mActivity;

    private boolean isLoading = false; // 标注是否正在加载 防止多次加载
    private int mCurrentPage = 1;
    private int mTotalPages;

    private List<NewsBean.BaseNews> mListData = new ArrayList<>();
    private List<NewsBean.BaseNews> mListDapps = new ArrayList<>();
    private NewsAdapter mNewsAdapter;
    private boolean isFirstBanner = true, isFirstNews = true, isFirstDapps = true;
    private int mReadPosition;
    private CreateDbWallet mCreateDbWallet;
    private WalletBean mWalletBean;
    private int mWalletType;
    private String mDefultAddress = "", mWalletMnc = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_main_find_news, container, false);
        unbinder = ButterKnife.bind(this, contentView);
        mActivity = (MainActivity) getActivity();
        mCreateDbWallet = new CreateDbWallet(mActivity);
        initViews();
        mCurrentPage = 1;
        mActivity.setChangeNewsViewPager(new MainChangePagerNewsListener() {
            @Override
            public void changeNewsPager() {
                mDefultAddress = SharedPreferencesUtil.getSharePreString(mActivity, SharedPreferencesUtil.CHOOSE_WALLET_ADDRESS);
                if (!StrUtil.isEmpty(mDefultAddress)) {
                    mWalletBean = mCreateDbWallet.queryWallet(mActivity, mDefultAddress);
                    mWalletType = mWalletBean.getIsClodWallet();
                    mWalletMnc = mWalletBean.getMnemonic();
                }
            }
        });
        initListData(1);
        return contentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mDefultAddress = SharedPreferencesUtil.getSharePreString(mActivity, SharedPreferencesUtil.CHOOSE_WALLET_ADDRESS);
        if (!StrUtil.isEmpty(mDefultAddress)) {
            mWalletBean = mCreateDbWallet.queryWallet(mActivity, mDefultAddress);
            mWalletType = mWalletBean.getIsClodWallet();
            mWalletMnc = mWalletBean.getMnemonic();
        }
    }

    private void initViews() {
        mDefultAddress = SharedPreferencesUtil.getSharePreString(mActivity, SharedPreferencesUtil.CHOOSE_WALLET_ADDRESS);
        if (!StrUtil.isEmpty(mDefultAddress)) {
            mWalletBean = mCreateDbWallet.queryWallet(mActivity, mDefultAddress);
            mWalletType = mWalletBean.getIsClodWallet();
            mWalletMnc = mWalletBean.getMnemonic();
        }

        mRefreshPtrFrameLayout.setLastUpdateTimeRelateObject(this);
        mRefreshPtrFrameLayout.setUltraPullToRefresh(mRefreshListener, mRecyclerView);
        mRefreshPtrFrameLayout.changeWhiteBackgroud();

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mNewsAdapter = new NewsAdapter(mActivity, mBannerInfos, mListData, mListDapps);
        mListAdapter = new LoadMoreRecyclerAdapter(mActivity, mNewsAdapter, false);
        mNewsAdapter.setOnReadListenner(new NewsAdapter.OnReadListenner() {
            @Override
            public void OnReadListenner(int position, String title, String des, String url) {
                mReadPosition = position;
                Intent goto_webView = new Intent(mActivity, NewsWebActivity.class);
                goto_webView.putExtra(NewsWebActivity.ACTIVITY_TITLE_INFO, title);
                goto_webView.putExtra(NewsWebActivity.ACTIVITY_DES_INFO, des);
                goto_webView.putExtra(NewsWebActivity.WEBVIEW_LOAD_URL, url);
                startActivityForResult(goto_webView, 0x231);
            }

            @Override
            public void OnDappsClickListenner(int id, boolean isAuthorize, String titleCn, String titleEn, String url, boolean isIntentOut) {
                //判断是否有钱包
                String defultAddress = SharedPreferencesUtil.getSharePreString(mActivity, SharedPreferencesUtil.CHOOSE_WALLET_ADDRESS);
                if (StrUtil.isEmpty(defultAddress)) {
                    showCreateWalletDilaog(0, 0, mActivity.getResources().getString(R.string.dialog_common_title_message_nowallet),
                            mActivity.getResources().getString(R.string.dialog_common_title_message_go_create_wallet), "", "", "", false);
                    return;
                }
                if (isAuthorize) {//弹出授权对话框
                    boolean isAuth = mCreateDbWallet.queryDappsAuth(defultAddress, String.valueOf(id));
                    if (!isAuth) {
                        showCreateWalletDilaog(id, 1, mActivity.getResources().getString(R.string.main_find_dapps_txt_02),
                                mActivity.getResources().getString(R.string.main_find_dapps_txt_03), titleCn, titleEn, url, isIntentOut);
                    } else {
                        gowebH5(titleCn, titleEn, url, isIntentOut);
                    }
                } else {
                    gowebH5(titleCn, titleEn, url, isIntentOut);
                }

            }

            @Override
            public void onRedBagListenner() {
                if (StrUtil.isEmpty(mDefultAddress)) {
                    showCreateWalletDilaog();
                    return;
                }
                //判断是否需要判断助记词 是，有助记词就需要弹出备份助记词对话框
                if (!StrUtil.isEmpty(mWalletMnc)) {
                    showGoCopyDialog();
                    return;
                }

                if (mWalletType == 0) {
                    startActivity(new Intent(mActivity, SendRedPacketsActivity.class));
                } else
                    DappApplication.getInstance().showToast(getResources().getString(R.string.main_send_redpackets_txt_01));
            }

            @Override
            public void onVoteListenner() {
                mDefultAddress = SharedPreferencesUtil.getSharePreString(mActivity, SharedPreferencesUtil.CHOOSE_WALLET_ADDRESS);
                if (StrUtil.isEmpty(mDefultAddress)) {
                    showCreateWalletDilaog();
                    return;
                }
                //需要判断是否开启投票功能
                checkOpenVote();
            }

            @Override
            public void onMaplistenner() {
                startActivity(new Intent(mActivity, MainmapOneActivity.class));
            }

            @Override
            public void onVoteGllistenner() {//投票治理
                if (StrUtil.isEmpty(mDefultAddress)) {
                    showCreateWalletDilaog();
                    return;
                }
                //判断是否需要判断助记词 是，有助记词就需要弹出备份助记词对话框
//                if (!StrUtil.isEmpty(mWalletMnc)){
//                    showGoCopyDialog();
//                    return;
//                }
                startActivity(new Intent(mActivity, VoteALActivity.class));
            }

            @Override
            public void onCionFhlistenner() {//节点分红
                if (StrUtil.isEmpty(mDefultAddress)) {
                    showCreateWalletDilaog();
                    return;
                }
                //判断是否需要判断助记词 是，有助记词就需要弹出备份助记词对话框
                if (!StrUtil.isEmpty(mWalletMnc)) {
                    showGoCopyDialog();
                    return;
                }

                startActivity(new Intent(mActivity, NodeDividendActivity.class));
            }

            /**
             * 数字签名
             */
            @Override
            public void onDigitalSignListener() {
                // 数字签名
                if (StrUtil.isEmpty(mDefultAddress)) {
                    showCreateWalletDilaog();
                    return;
                }

                startActivity(new Intent(mActivity, DigitalSignActivity.class));

            }
        });
        mRecyclerView.setAdapter(mListAdapter);
        // 设置滑到底部加载更多
        mRecyclerView.setListener(mLoadMoreListener);
    }

    /**
     * 创建钱包对话框
     */
    private void showCreateWalletDilaog() {
        CommonDilogTool dialogTool = new CommonDilogTool(mActivity);
        dialogTool.show(null, mActivity.getResources().getString(R.string.dialog_common_title_message_nowallet), null,
                mActivity.getResources().getString(R.string.dailog_psd_btn_cancle), null,
                mActivity.getResources().getString(R.string.dialog_common_title_message_go_create_wallet), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(mActivity, CreateWalletActivity.class);
                        startActivity(intent);
                        dialog.dismiss();
                    }
                });
    }

    /**
     * 提醒备份助记词对话框
     */
    private void showGoCopyDialog() {
        CommonCopyDialog.Builder builder = new CommonCopyDialog.Builder(mActivity);
        builder.setPositiveButton(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent goto_details = new Intent(mActivity, WalletDetailsActivity.class);
                goto_details.putExtra(WalletDetailsActivity.WALLET_TITLE, mWalletBean.getWalletBName());
                goto_details.putExtra(WalletDetailsActivity.WALLET_ADDRESS, mWalletBean.getAddress());
                goto_details.putExtra(WalletDetailsActivity.WALLET_SHOW_DAILOG, true);
                mActivity.startActivity(goto_details);
            }
        });
        CommonCopyDialog dialog = builder.create();
        dialog.show();

        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = (int) (mActivity.getWindowManager().getDefaultDisplay().getWidth() * 0.8);
        dialog.getWindow().setAttributes(params);
    }

    private void checkOpenVote() {
        new VoteStateRequest().doRequest(mActivity, new CommonResultListener(mActivity) {
            @Override
            public void onSuccess(JSONArray jsonArray) {
                super.onSuccess(jsonArray);
                if (isAdded()) {
                    String status = (String) jsonArray.get(0);
                    if ("000000".equals(status)) {
                        VotePersonBean votePersonBean = JSON.parseObject(jsonArray.get(2).toString(), VotePersonBean.class);
                        SharedPreferencesUtil.setSharePreString(mActivity, DAppConstants.VOTE_CONTRACT_ADDRESS, votePersonBean.getVoteContractAddress());
                        SharedPreferencesUtil.setSharePreString(mActivity, DAppConstants.VOTE_RULE_AGAIN_ZH, votePersonBean.getVoteRule());
                        SharedPreferencesUtil.setSharePreString(mActivity, DAppConstants.VOTE_RULE_AGAIN_EN, votePersonBean.getVoteRuleEN());
                        String state = votePersonBean.getVoteState();
                        if ("0".equals(state)) {//未开启
                            startActivity(new Intent(mActivity, VoteWaitActivity.class));
                        } else {
                            startActivity(new Intent(mActivity, VoteActivity.class));
                        }
                    }
                }
            }
        });
    }


    private void gowebH5(String titleCn, String titleEn, String url, boolean isIntentOut) {
        Intent goto_webView = new Intent(mActivity, DappsWebActivity.class);
        int type = ChangeLanguageUtil.languageType(mActivity);
        String title = titleCn;
        if (type != 1) {
            title = titleEn;
        }
        goto_webView.putExtra(DappsWebActivity.ACTIVITY_TITLE_INFO, title);
        goto_webView.putExtra(DappsWebActivity.WEBVIEW_LOAD_URL, url);
        goto_webView.putExtra(DappsWebActivity.WEBVIEW_INTENT_OUT, isIntentOut);
        startActivity(goto_webView);
    }

    /**
     * 初始化数据
     *
     * @param start
     */
    private void initListData(int start) {
        initBannerDatas();
        initNewsDatas(start);
        initDappsDatas();
    }

    private void initDappsDatas() {
        String newsDatas = DBManager.queryHistory(HistoryCache.DappsList);
        if (!StrUtil.isEmpty(newsDatas) && isFirstDapps) {
            NewsBean newsInfo = JSON.parseObject(newsDatas, NewsBean.class);
            mListDapps.clear();
            mListDapps.addAll(newsInfo.getList());
            mListAdapter.notifyDataSetChanged();
        }
        new GetNewsRequest(mActivity, UrlContext.Url_getDapps.getContext(), 0)
                .doRequest(mActivity, new CommonResultListener(mActivity) {
                    @Override
                    public void onSuccess(JSONArray jsonArray) {
                        if (isAdded()) {
                            mListDapps.clear();
                            isFirstDapps = false;
                            NewsBean newsInfo = JSON.parseObject(jsonArray.get(2).toString(), NewsBean.class);
                            mListDapps.addAll(newsInfo.getList());
                            mListAdapter.notifyDataSetChanged();
                            DBManager.insertHistory(HistoryCache.DappsList, jsonArray.get(2).toString());
                        }
                    }
                });
    }

    /**
     * 获取banner
     */
    private void initBannerDatas() {
        String bannerDatas = DBManager.queryHistory(HistoryCache.BannerList);
        if (!StrUtil.isEmpty(bannerDatas) && isFirstBanner) {
            if (isAdded()) {
                mBannerInfos.clear();
                BannerInfo bannerInfo = JSON.parseObject(bannerDatas, BannerInfo.class);
                mBannerInfos.addAll(bannerInfo.getList());
                mListAdapter.notifyDataSetChanged();
            }
        }
        new GetBannerNewsRequest(mActivity).doRequest(mActivity, new CommonResultListener(mActivity) {
            @Override
            public void onSuccess(JSONArray jsonArray) {
                if (isAdded()) {
                    isFirstBanner = false;
                    mBannerInfos.clear();
                    BannerInfo bannerInfo = JSON.parseObject(jsonArray.get(2).toString(), BannerInfo.class);
                    mBannerInfos.addAll(bannerInfo.getList());
                    mListAdapter.notifyDataSetChanged();
                    DBManager.insertHistory(HistoryCache.BannerList, jsonArray.get(2).toString());
                }
            }
        });
    }

    /**
     * 获取新闻资讯
     *
     * @param pageNum
     */
    private void initNewsDatas(int pageNum) {
        String newsDatas = DBManager.queryHistory(HistoryCache.NewsList);
        if (!StrUtil.isEmpty(newsDatas) && pageNum == 1 && isFirstNews) {
            NewsBean newsInfo = JSON.parseObject(newsDatas, NewsBean.class);
            mListData.clear();
            mRecyclerView.setSmoothPosition(0);
            mListData.addAll(newsInfo.getList());
            mListAdapter.notifyDataSetChanged();
            mRefreshPtrFrameLayout.refreshComplete();
        }
        isLoading = true;
        new GetNewsRequest(mActivity, UrlContext.Url_getArticleNews.getContext(), pageNum)
                .doRequest(mActivity, new NetResultCallBack() {
                    @Override
                    public void onSuccess(JSONArray jsonArray) {
                        if (isAdded()) {
                            isFirstNews = false;
                            NewsBean newsInfo = JSON.parseObject(jsonArray.get(2).toString(), NewsBean.class);
                            if (pageNum == 1) {
                                mListData.clear();
                                mRecyclerView.setSmoothPosition(0);
                            }
                            if (CollectionUtil.isCollectionEmpty(newsInfo.getList())) {
                                mListAdapter.setIsNeedLoadMore(false);
                                mListAdapter.notifyDataSetChanged();
                            } else {
                                mTotalPages = newsInfo.getPages();
                                mListData.addAll(newsInfo.getList());
                                mListAdapter.setIsNeedLoadMore(mCurrentPage < mTotalPages ? true : false);// 设置是否显示加载更多Item
                                mCurrentPage++;
                                mListAdapter.notifyDataSetChanged();
                                if (pageNum == 1) {
                                    DBManager.insertHistory(HistoryCache.NewsList, jsonArray.get(2).toString());
                                }
                            }
                            mRefreshPtrFrameLayout.refreshComplete();
                            isLoading = false;
                        }
                    }

                    @Override
                    public void onError(String error) {
                        if (isAdded()) {
                            mRefreshPtrFrameLayout.refreshComplete();
                            isLoading = false;
                            DappApplication.getInstance().showToast(error);
                        }
                    }
                });
    }

    /**
     * 加载更多
     */
    private LoadMoreRecyclerView.OnLoadMoreListener mLoadMoreListener = new LoadMoreRecyclerView.OnLoadMoreListener() {
        @Override
        public void loadMore() {
            // 设置滑到底部加载更多
            if (!isLoading && mCurrentPage <= mTotalPages) {
                initNewsDatas(mCurrentPage);
            }
        }
    };

    private MyPtrFrameLayout.OnRefreshListener mRefreshListener = new MyPtrFrameLayout.OnRefreshListener() {
        @Override
        public void refresh(PtrFrameLayout frame) {
            if (!isLoading) {
                mCurrentPage = 1;
                initListData(1);
            }
        }
    };

    /**
     * 创建钱包对话框
     */
    private void showCreateWalletDilaog(int id, int type, String msg, String rightStr, String titleCn, String titleEn, String url, boolean isIntentOut) {
        CommonDilogTool dialogTool = new CommonDilogTool(mActivity);
        dialogTool.show(null, msg, null,
                mActivity.getResources().getString(R.string.dailog_psd_btn_cancle), null, rightStr, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (type == 0) {
                            Intent intent = new Intent(mActivity, CreateWalletActivity.class);
                            startActivity(intent);
                            dialog.dismiss();
                        } else {
                            String defultAddress = SharedPreferencesUtil.getSharePreString(mActivity, SharedPreferencesUtil.CHOOSE_WALLET_ADDRESS);
                            int result = mCreateDbWallet.insertDappsAuth(defultAddress, String.valueOf(id));
                            if (result == 0) {
                                gowebH5(titleCn, titleEn, url, isIntentOut);
                                dialog.dismiss();
                            } else {
                                dialog.dismiss();
                            }
                        }
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0x231 && resultCode == RESULT_OK) {
            mListAdapter.notifyItemChanged(mReadPosition);
        }
    }
}
