package com.zhaoxi.Open_source_Android.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.base.DappApplication;
import com.zhaoxi.Open_source_Android.common.view.CircleImageView;
import com.zhaoxi.Open_source_Android.libs.utils.ShareUtil;
import com.zhaoxi.Open_source_Android.libs.utils.SlinUtil;
import com.zhaoxi.Open_source_Android.libs.utils.StrUtil;
import com.zhaoxi.Open_source_Android.net.BaseBet.NetResultCallBack;
import com.zhaoxi.Open_source_Android.net.Request.TotalEthAccountRequest;
import com.zhaoxi.Open_source_Android.net.bean.MapEthBean;
import com.zhaoxi.Open_source_Android.ui.activity.MapTransferActivity;
import com.zhaoxi.Open_source_Android.ui.activity.TransationMapRecordActivity;
import com.zhaoxi.Open_source_Android.ui.activity.TransationRecordActivity;
import com.zhaoxi.Open_source_Android.web3.bean.WalletBean;

import java.math.BigDecimal;
import java.util.List;

/**
 * des:
 * Created by ztt on 2018/6/7.
 */
public class MainmapManagerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<WalletBean> mWalletData;

    public MainmapManagerAdapter(Context context, List<WalletBean> walletData) {
        this.mContext = context;
        this.mWalletData = walletData;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_mainmap_manager, viewGroup, false);
        return new WalletHodler(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        WalletBean walletBean = mWalletData.get(position);
        WalletHodler walletHodler = (WalletHodler) holder;
        Bitmap bitmap = ShareUtil.getLoacalBitmap(mContext,walletBean.getImgPath());
        walletHodler.mImgHader.setImageBitmap(bitmap);

        walletHodler.mTxtName.setText(walletBean.getWalletBName());
        walletHodler.mTxtAddress.setText(StrUtil.addressFilter(walletBean.getAddress()));

        walletHodler.mLayoutMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (walletBean.isEnable()) {
                    //映射
                    Intent it_map = new Intent(mContext, MapTransferActivity.class);
                    it_map.putExtra(MapTransferActivity.RESOUCE_ADDRESS, walletBean.getAddress());
                    mContext.startActivity(it_map);
                }
            }
        });
        walletHodler.mLayoutLook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //查询
                Intent it_recode = new Intent(mContext, TransationMapRecordActivity.class);
                it_recode.putExtra(TransationRecordActivity.RESOUCE_ADDRESS, walletBean.getAddress());
                mContext.startActivity(it_recode);
            }
        });

        /**
         * 获取相应钱包的金额
         */
        new TotalEthAccountRequest(walletBean.getAddress()).doRequest(mContext,new NetResultCallBack() {
            @Override
            public void onSuccess(JSONArray jsonArray) {
                String status = (String) jsonArray.get(0);
                if ("000000".equals(status)) {//代表处理成功
                    MapEthBean bannerInfo = JSON.parseObject(jsonArray.get(2).toString(), MapEthBean.class);
                    BigDecimal hpbMoney = new BigDecimal("" + bannerInfo.getHpbToken());
                    BigDecimal ethMoney = bannerInfo.getEthBalance();
                    if (ethMoney.compareTo(new BigDecimal(0)) == 0 || hpbMoney.compareTo(new BigDecimal(0)) == 0) {
                        walletHodler.mTxtMaptxt.setTextColor(mContext.getResources().getColor(R.color.color_black_999));
                        walletHodler.mImgRight.setBackgroundResource(R.mipmap.icon_main_map_map_gray);
                        walletBean.setEnable(false);
                    } else {
                        walletHodler.mTxtMaptxt.setTextColor(mContext.getResources().getColor(R.color.color_F5FF30));
                        walletHodler.mImgRight.setBackgroundResource(R.mipmap.icon_main_map_map);
                        walletBean.setEnable(true);
                    }
                    walletHodler.mTxtEthMoney.setText("" + SlinUtil.formatNumFromWeiS(mContext,ethMoney) + " ETH");
                    walletHodler.mTxtMoney.setText("" + SlinUtil.formatNumFromWeiS(mContext,hpbMoney));
                }
                walletBean.setmRunning(true);
            }

            @Override
            public void onError(String error) {
                walletBean.setmRunning(true);
                DappApplication.getInstance().showToast(error);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mWalletData.size();
    }

    private class WalletHodler extends RecyclerView.ViewHolder {
        private CircleImageView mImgHader;
        private TextView mTxtName, mTxtAddress, mTxtMoney, mTxtMaptxt, mTxtEthMoney;
        private LinearLayout mLayoutLook, mLayoutMap;
        private ImageView mImgRight;

        public WalletHodler(View itemView) {
            super(itemView);
            mImgHader = itemView.findViewById(R.id.item_mainmap_manager_header_img);
            mTxtName = itemView.findViewById(R.id.item_mainmap_manager_name);
            mTxtAddress = itemView.findViewById(R.id.item_mainmap_manager_address);
            mTxtMoney = itemView.findViewById(R.id.item_mainmap_manager_money);
            mLayoutLook = itemView.findViewById(R.id.item_mainmap_manager_look);
            mLayoutMap = itemView.findViewById(R.id.item_mainmap_manager_map);
            mTxtMaptxt = itemView.findViewById(R.id.item_mainmap_manager_map_txt);
            mTxtEthMoney = itemView.findViewById(R.id.item_mainmap_manager_eth_money);
            mImgRight = itemView.findViewById(R.id.item_mainmap_manager_img_right);
        }
    }
}
