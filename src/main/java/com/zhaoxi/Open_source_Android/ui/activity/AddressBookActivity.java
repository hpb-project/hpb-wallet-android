package com.zhaoxi.Open_source_Android.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.base.BaseTitleBarActivity;
import com.zhaoxi.Open_source_Android.common.base.DappApplication;
import com.zhaoxi.Open_source_Android.common.bean.BandingAddressBean;
import com.zhaoxi.Open_source_Android.common.view.addressbook.CharacterParser;
import com.zhaoxi.Open_source_Android.common.view.addressbook.PinyinComparator;
import com.zhaoxi.Open_source_Android.common.view.addressbook.SideBar;
import com.zhaoxi.Open_source_Android.db.CreateDbWallet;
import com.zhaoxi.Open_source_Android.libs.utils.CollectionUtil;
import com.zhaoxi.Open_source_Android.libs.utils.SharedPreferencesUtil;
import com.zhaoxi.Open_source_Android.net.BaseBet.NetResultCallBack;
import com.zhaoxi.Open_source_Android.net.Request.PullBookRequest;
import com.zhaoxi.Open_source_Android.net.bean.BookBean;
import com.zhaoxi.Open_source_Android.ui.AddaddressActivity;
import com.zhaoxi.Open_source_Android.ui.adapter.SortAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddressBookActivity extends BaseTitleBarActivity {
    private static final int REQUEST_ADD = 0x002;
    private static final int REQUEST_CLOUD = 0x004;
    public static final String ADDRESS_BOOK_SOURCE = "AddressBookActivity.ADDRESS_BOOK_SOURCE";
    public static final String RESULT_CONTENT = "AddressBookActivity.address";

    @BindView(R.id.system_txt_openhand_status)
    TextView mTxtOpenhandStatus;
    @BindView(R.id.address_book_layout_no_data)
    TextView mLayoutNoData;
    @BindView(R.id.address_book_list_contract)
    ListView mSortListView;
    @BindView(R.id.address_book_list_top_char)
    TextView mTopChar;
    @BindView(R.id.address_book_list_top_layout)
    LinearLayout mTopLayout;
    @BindView(R.id.address_book_list_select_sidrbar_dialog)
    TextView mSelectSidrbarDialog;
    @BindView(R.id.address_book_list_sidrbar)
    SideBar mSidrbar;
    @BindView(R.id.address_book_layout_has_data)
    RelativeLayout mLayoutHasData;
    @BindView(R.id.address_book_layout_opencloud)
    RelativeLayout mLayoutOpen;

    private CharacterParser characterParser;
    private List<BandingAddressBean> mdbDateList = new ArrayList<>(); // 数据
    private List<BandingAddressBean> mSourceDateList = new ArrayList<>(); // 数据
    private SortAdapter mSortAdapter; // 排序的适配器
    private PinyinComparator pinyinComparator;
    private int lastFirstVisibleItem = -1;
    private CreateDbWallet mCreateDbWallet;
    private int mSourceType = 0;
    private int isOpenCloud = 0;//未开启

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_book);
        ButterKnife.bind(this);
        mSourceType = getIntent().getIntExtra(ADDRESS_BOOK_SOURCE, 0);
        initviews();
        getBookData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        isOpenCloud = SharedPreferencesUtil.getSharePreInt(this, SharedPreferencesUtil.CHANGE_OPENCLOUD_STATUS);
        if (isOpenCloud == 0) {
            mTxtOpenhandStatus.setText(getResources().getString(R.string.main_me_txt_address_03));
        } else {
            mTxtOpenhandStatus.setText(getResources().getString(R.string.main_me_txt_address_04));
        }
    }

    private void initviews() {
        mCreateDbWallet = new CreateDbWallet(this);
        setTitle(R.string.main_me_txt_address,true);
        showRightImgWithImgListener(R.mipmap.icon_address_book_add, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it_add = new Intent(AddressBookActivity.this, AddaddressActivity.class);
                it_add.putExtra(AddaddressActivity.ADD_SOURCE, 0);
                startActivityForResult(it_add, REQUEST_ADD);
            }
        });
        if (mSourceType == 0) {//地址簿入口
            mLayoutOpen.setVisibility(View.VISIBLE);
        } else {//转账
            mLayoutOpen.setVisibility(View.GONE);
        }

        characterParser = CharacterParser.getInstance();
        pinyinComparator = new PinyinComparator();
        mSidrbar.setTextView(mSelectSidrbarDialog);
        /**
         * 为右边添加触摸事件
         */
        mSidrbar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                int position = mSortAdapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    mSortListView.setSelection(position);
                }

            }
        });

        mSortListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                BandingAddressBean bandingAddressBean = ((BandingAddressBean) mSortAdapter.getItem(position));
                if (mSourceType == 0) {//地址簿入口
                    //进入详情
                    Intent it_add = new Intent(AddressBookActivity.this, BandAddressDetailsActivity.class);
                    it_add.putExtra(BandAddressDetailsActivity.ADD_ADDRESS_ID, bandingAddressBean.getBId());
                    startActivityForResult(it_add, REQUEST_ADD);
                } else {//选择地址 返回上一个页面
                    Intent goto_transfer = new Intent(AddressBookActivity.this, TransferActivity.class);
                    goto_transfer.putExtra(RESULT_CONTENT, bandingAddressBean.getAddressContact());
                    setResult(RESULT_OK, goto_transfer);
                    finish();
                }
            }

        });

        mSortAdapter = new SortAdapter(this, mSourceDateList);
        mSortListView.setAdapter(mSortAdapter);

        /**
         * 设置滚动监听， 实时跟新悬浮的字母的值
         */
        mSortListView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                int section = mSortAdapter.getSectionForPosition(firstVisibleItem);
                int nextSecPosition = mSortAdapter.getPositionForSection(section + 1);
                if (firstVisibleItem != lastFirstVisibleItem) {
                    ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) mTopLayout
                            .getLayoutParams();
                    params.topMargin = 0;
                    mTopLayout.setLayoutParams(params);
                    mTopChar.setText(String.valueOf((char) section));
                }
                if (nextSecPosition == firstVisibleItem + 1) {
                    View childView = view.getChildAt(0);
                    if (childView != null) {
                        int titleHeight = mTopLayout.getHeight();
                        int bottom = childView.getBottom();
                        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) mTopLayout
                                .getLayoutParams();
                        if (bottom < titleHeight) {
                            float pushedDistance = bottom - titleHeight;
                            params.topMargin = (int) pushedDistance;
                            mTopLayout.setLayoutParams(params);
                        } else {
                            if (params.topMargin != 0) {
                                params.topMargin = 0;
                                mTopLayout.setLayoutParams(params);
                            }
                        }
                    }
                }
                lastFirstVisibleItem = firstVisibleItem;
            }
        });
    }

    private void getBookData() {
        isOpenCloud = SharedPreferencesUtil.getSharePreInt(this, SharedPreferencesUtil.CHANGE_OPENCLOUD_STATUS);
        if (isOpenCloud == 0) {
            setData();
        } else {
            //网络数据
            getCloudData();
        }
    }

    private void getCloudData() {
        showProgressDialog();
        List<String> listAddress = mCreateDbWallet.queryAllAddress();
        new PullBookRequest(listAddress).doRequest(this, new NetResultCallBack() {
            @Override
            public void onSuccess(JSONArray jsonArray) {
                BookBean addressBean = JSON.parseObject(jsonArray.get(2).toString(), BookBean.class);
                int result = mCreateDbWallet.insertAllBandAddress(addressBean.getData());
                if (result == 0) {
                    setData();
                    dismissProgressDialog();
                }
            }

            @Override
            public void onError(String error) {
                dismissProgressDialog();
                if (!error.equals(getResources().getString(R.string.exception_netword_error))) {
                    DappApplication.getInstance().showToast(error);
                }
                setData();
            }
        });
    }

    private void setData() {
        mdbDateList.clear();
        mSourceDateList.clear();
        mdbDateList = mCreateDbWallet.queryAllBandAddress();
        if (mdbDateList.size() == 0) {
            mLayoutNoData.setVisibility(View.VISIBLE);
            mLayoutHasData.setVisibility(View.GONE);
        } else {
            mLayoutNoData.setVisibility(View.GONE);
            mLayoutHasData.setVisibility(View.VISIBLE);
            if (!CollectionUtil.isCollectionEmpty(mdbDateList)) {
                mSourceDateList = filledData(mdbDateList);
                Collections.sort(mSourceDateList, pinyinComparator);
                mTopChar.setText(mSourceDateList.get(0).getSortLetters());
                mSortAdapter.updateListView(mSourceDateList);
            }
        }
    }

    @OnClick(R.id.address_book_layout_opencloud)
    public void onViewClicked() {
        startActivityForResult(new Intent(this, CloudToggleActivity.class), REQUEST_CLOUD);
    }

    /**
     * 填充数据
     *
     * @param date
     * @return
     */
    private List<BandingAddressBean> filledData(List<BandingAddressBean> date) {
        List<BandingAddressBean> mSortList = new ArrayList<BandingAddressBean>();
        for (int i = 0; i < date.size(); i++) {
            BandingAddressBean bean = date.get(i);
            BandingAddressBean sortModel = new BandingAddressBean();
            String name = bean.getMark();
            sortModel.setMark(name);
            String pinyin = characterParser.getPinYinHeadChar(name);
            String sortString = "";
            if (pinyin.length() > 0) {
                sortString = pinyin.substring(0, 1).toUpperCase();
            }

            if (sortString.matches("[A-Z]")) {
                sortModel.setSortLetters(sortString.toUpperCase());
            } else {
                sortModel.setSortLetters("#");
            }
            sortModel.setBId(bean.getBId());
            sortModel.setAddressContact(bean.getAddressContact());

            mSortList.add(sortModel);
        }
        return mSortList;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ADD && resultCode == RESULT_OK) {
            getBookData();
        } else if (requestCode == REQUEST_CLOUD && resultCode == RESULT_OK) {
            getBookData();
        }
    }
}
