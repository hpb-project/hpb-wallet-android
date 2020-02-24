package com.zhaoxi.Open_source_Android.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.zhaoxi.Open_source_Android.DAppConstants;
import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.base.DappApplication;
import com.zhaoxi.Open_source_Android.libs.tools.SystemLog;
import com.zhaoxi.Open_source_Android.libs.utils.ChangeLanguageUtil;
import com.zhaoxi.Open_source_Android.libs.utils.SharedPreferencesUtil;
import com.zhaoxi.Open_source_Android.libs.utils.SlinUtil;
import com.zhaoxi.Open_source_Android.libs.utils.StrUtil;
import com.zhaoxi.Open_source_Android.net.bean.AssetsBean;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * create by fangz
 * create date:2019/9/5
 * create time:9:58
 */
public class AssetsAdapter extends RecyclerView.Adapter<AssetsAdapter.ItemViewHolder> {

    private List<AssetsBean> assetsBeans;
    private Context mContext;
    private String mCurrentAssetsUnit;

    public AssetsAdapter(Context context, List<AssetsBean> assetsBeans) {
        this.assetsBeans = assetsBeans;
        this.mContext = context;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_assets_list_layout, parent, false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        // 当前金额折合单位
        int mCurrentChoseUnit = SharedPreferencesUtil.getSharePreInt(mContext, SharedPreferencesUtil.CHANGE_COIN_UNIT);
        if (mCurrentChoseUnit == 0) {
            int languge = SharedPreferencesUtil.getSharePreInt(mContext, SharedPreferencesUtil.CHANGE_LANGUAGE_NAME);
            if (languge == 0) {
                String sysLanguage = Locale.getDefault().getLanguage();
                if ("zh".equals(sysLanguage)) {
                    mCurrentAssetsUnit = mContext.getResources().getString(R.string.activity_rmb_value);
                } else {
                    mCurrentAssetsUnit = mContext.getResources().getString(R.string.activity_dollar_value);
                }
            } else {
                if (languge == ChangeLanguageUtil.CHANGE_LANGUAGE_CHINA) {
                    mCurrentAssetsUnit = mContext.getResources().getString(R.string.activity_rmb_value);
                } else {
                    mCurrentAssetsUnit = mContext.getResources().getString(R.string.activity_dollar_value);
                }
            }
        } else {
            if (mCurrentChoseUnit == 1) {// 人民币
                mCurrentAssetsUnit = mContext.getResources().getString(R.string.activity_rmb_value);
            } else {// 美元
                mCurrentAssetsUnit = mContext.getResources().getString(R.string.activity_dollar_value);
            }

        }
        holder.mAssetsUnit.setText(mCurrentAssetsUnit);
        AssetsBean assetsBean = assetsBeans.get(position);
        holder.mAssetsTokenName.setText(assetsBean.getTokenSymbol());
        if (DAppConstants.TYPE_HPB.equals(assetsBean.getContractType())) {
            // 合约类型为HPB时不显示
            holder.mAssetsTokenType.setVisibility(View.GONE);
        } else {
            holder.mAssetsTokenType.setVisibility(View.VISIBLE);
            holder.mAssetsTokenType.setText(assetsBean.getContractType());

        }

        int decimals = assetsBean.getDecimals();
        SystemLog.D("decimal", "decimal = " + decimals);
        int mEyesStatus = SharedPreferencesUtil.getSharePreInt(mContext, DAppConstants.MONEY_EYES_STATUS);
        if (mEyesStatus == SwitchAddressAdapter.STATUS_SHOW) {//显示时隐藏
            holder.mAssetsTokenBalance.setText(SlinUtil.formatNumFromWeiT(mContext, new BigDecimal(assetsBean.getBalanceOfToken()), decimals));
            String cnyTotalValue = assetsBean.getCnyTotalValue();
            String usdTotalValue = assetsBean.getUsdTotalValue();
            BigDecimal mCurrentMoney;
            if (mCurrentChoseUnit == 0) {
                int languge = SharedPreferencesUtil.getSharePreInt(DappApplication.getInstance().getApplicationContext(), SharedPreferencesUtil.CHANGE_LANGUAGE_NAME);
                if (languge == 0) {
                    String sysLanguage = Locale.getDefault().getLanguage();
                    if ("zh".equals(sysLanguage)) {
                        mCurrentMoney = new BigDecimal(cnyTotalValue);
                    } else {
                        mCurrentMoney = new BigDecimal(usdTotalValue);
                    }
                } else {
                    if (languge == ChangeLanguageUtil.CHANGE_LANGUAGE_CHINA) {
                        mCurrentMoney = new BigDecimal(cnyTotalValue);
                    } else {
                        mCurrentMoney = new BigDecimal(usdTotalValue);
                    }
                }
            } else {
                if (mCurrentChoseUnit == 1) {// 人民币
                    mCurrentMoney = new BigDecimal(cnyTotalValue);
                } else {// 美元
                    mCurrentMoney = new BigDecimal(usdTotalValue);
                }

            }
            holder.mAssetsTokenValue.setText(SlinUtil.NumberFormat2(mContext, mCurrentMoney));
            if (StrUtil.isNull(assetsBean.getCnyPrice())) {
                holder.mAssetsUnit.setVisibility(View.GONE);
                holder.mAssetsTokenValue.setVisibility(View.GONE);
            } else {
                holder.mAssetsUnit.setVisibility(View.VISIBLE);
                holder.mAssetsTokenValue.setVisibility(View.VISIBLE);
            }
        } else {// 隐藏时显示
            holder.mAssetsTokenBalance.setText(DappApplication.getInstance().getApplicationContext().getResources().getString(R.string.main_home_money_eyes_close));
            holder.mAssetsTokenValue.setText(DappApplication.getInstance().getApplicationContext().getResources().getString(R.string.main_home_money_eyes_close));

            holder.mAssetsUnit.setVisibility(View.GONE);
            if (StrUtil.isNull(assetsBean.getCnyPrice())) {
                holder.mAssetsTokenValue.setVisibility(View.GONE);
            } else {
                holder.mAssetsTokenValue.setVisibility(View.VISIBLE);
            }
        }

        String tokenSymbolImageUrl = assetsBean.getTokenSymbolImageUrl();
        if (!TextUtils.isEmpty(tokenSymbolImageUrl)) {
            Glide.with(mContext).
                    load(tokenSymbolImageUrl)
                    .centerCrop()
                    .error(R.mipmap.icon_default_header)
                    .placeholder(R.mipmap.icon_default_header)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(holder.mAssetsHead);
        } else {
            holder.mAssetsHead.setImageResource(R.mipmap.icon_default_header);
        }

        holder.mAssetsListContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(assetsBean);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return assetsBeans.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_main_assets_list_head)
        CircleImageView mAssetsHead;
        @BindView(R.id.item_main_assets_list_assets_unit)
        TextView mAssetsUnit;
        @BindView(R.id.item_main_assets_list_token_name)
        TextView mAssetsTokenName;
        @BindView(R.id.item_main_assets_list_token_balance)
        TextView mAssetsTokenBalance;
        @BindView(R.id.item_main_assets_list_token_type)
        TextView mAssetsTokenType;
        @BindView(R.id.item_main_assets_list_token_value)
        TextView mAssetsTokenValue;
        @BindView(R.id.item_assets_list_container)
        LinearLayout mAssetsListContainer;

        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(AssetsBean assetsBean);
    }
}
