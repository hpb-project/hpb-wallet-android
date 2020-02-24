package com.zhaoxi.Open_source_Android.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.zhaoxi.Open_source_Android.DAppConstants;
import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.base.DappApplication;
import com.zhaoxi.Open_source_Android.common.drag.ItemTouchHelperViewHolder;
import com.zhaoxi.Open_source_Android.common.view.MyCheckBox;
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
 * create time:14:37
 */
public class TokenManagerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<AssetsBean> assetsBeans;
    private Context mContext;
    private String mCurrentAssetsUnit;
    private OnItemLongClickListener onItemLongClickListener;
    private OnItemClickListener onItemClickListener;
    private OnTouchStatusListener onTouchStatusListener;
    private OnStartDragListener onStartDragListener;

    public TokenManagerAdapter(Context context, List<AssetsBean> assetsBeans) {
        this.assetsBeans = assetsBeans;
        this.mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_token_manager_list_layout, parent, false);
        return new ItemViewHolder(itemView);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
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
        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        itemViewHolder.mAssetsUnit.setText(mCurrentAssetsUnit);
        AssetsBean assetsBean = assetsBeans.get(position);
        itemViewHolder.mAssetsTokenName.setText(assetsBean.getTokenName());
        itemViewHolder.mAssetsTokenType.setText(assetsBean.getContractType());
        int decimals = assetsBean.getDecimals();

        int mEyesStatus = SharedPreferencesUtil.getSharePreInt(mContext, DAppConstants.MONEY_EYES_STATUS);
        if (mEyesStatus == SwitchAddressAdapter.STATUS_SHOW) {//显示时隐藏
            itemViewHolder.mAssetsTokenBalance.setText(SlinUtil.formatNumFromWeiT(mContext, new BigDecimal(assetsBean.getBalanceOfToken()), decimals));
            String cnyTotalValue = assetsBean.getCnyTotalValue();
            String usdTotalValue = assetsBean.getUsdTotalValue();
            BigDecimal mCurrentMoney;
            if (mCurrentChoseUnit == 0) {
                int language = SharedPreferencesUtil.getSharePreInt(DappApplication.getInstance().getApplicationContext(), SharedPreferencesUtil.CHANGE_LANGUAGE_NAME);
                if (language == 0) {
                    String sysLanguage = Locale.getDefault().getLanguage();
                    if ("zh".equals(sysLanguage)) {
                        mCurrentMoney = new BigDecimal(cnyTotalValue);
                    } else {
                        mCurrentMoney = new BigDecimal(usdTotalValue);
                    }
                } else {
                    if (language == ChangeLanguageUtil.CHANGE_LANGUAGE_CHINA) {
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

            itemViewHolder.mAssetsTokenValue.setText(SlinUtil.NumberFormat2(mContext, mCurrentMoney));
            SystemLog.D("getCnyPrice.getCnyPrice()", "assetsBean.getCnyPrice() = " + assetsBean.getCnyPrice());
            if (StrUtil.isNull(assetsBean.getCnyPrice())) {
                itemViewHolder.mAssetsUnit.setVisibility(View.GONE);
                itemViewHolder.mAssetsTokenValue.setVisibility(View.GONE);
            } else {
                itemViewHolder.mAssetsUnit.setVisibility(View.VISIBLE);
                itemViewHolder.mAssetsTokenValue.setVisibility(View.VISIBLE);
            }
        } else {// 隐藏时显示
            itemViewHolder.mAssetsUnit.setVisibility(View.GONE);
            itemViewHolder.mAssetsTokenBalance.setText(mContext.getResources().getString(R.string.main_home_money_eyes_close));
            itemViewHolder.mAssetsTokenValue.setText(mContext.getResources().getString(R.string.main_home_money_eyes_close));

            if (StrUtil.isNull(assetsBean.getCnyPrice())) {
                itemViewHolder.mAssetsTokenValue.setVisibility(View.GONE);
            } else {
                itemViewHolder.mAssetsTokenValue.setVisibility(View.VISIBLE);
            }
        }

        itemViewHolder.mAssetsTokenSimpleName.setText(assetsBean.getTokenSymbol());
        String tokenSymbolImageUrl = assetsBean.getTokenSymbolImageUrl();
        if (!TextUtils.isEmpty(tokenSymbolImageUrl)) {
            Glide.with(mContext).
                    load(tokenSymbolImageUrl)
                    .centerCrop()
                    .error(R.mipmap.icon_default_header)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .placeholder(R.mipmap.icon_default_header)
                    .into(itemViewHolder.mAssetsHead);
        } else {
            itemViewHolder.mAssetsHead.setImageResource(R.mipmap.icon_default_header);
        }

        // 默认选中
        itemViewHolder.mCheckBox.setChecked(assetsBean.isSelected());
        itemViewHolder.mTokenManagerContainer.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onItemLongClickListener != null) {
                    onItemLongClickListener.onItemLongClick(assetsBean);
                }
                return false;
            }
        });


        itemViewHolder.mTokenManagerContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = itemViewHolder.mCheckBox.isChecked();
                itemViewHolder.mCheckBox.setChecked(!checked);
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(assetsBean, !checked, itemViewHolder.getAdapterPosition());
                }
            }
        });


        itemViewHolder.mAssetsDragImg.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (onStartDragListener != null) {
                        onStartDragListener.startDrag(itemViewHolder, assetsBean);
                    }
                }
                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return assetsBeans.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {

        @BindView(R.id.item_main_assets_list_assets_unit)
        TextView mAssetsUnit;
        @BindView(R.id.item_token_manager_container)
        LinearLayout mTokenManagerContainer;
        @BindView(R.id.item_main_assets_list_head)
        CircleImageView mAssetsHead;
        @BindView(R.id.item_main_assets_list_token_name)
        TextView mAssetsTokenName;
        @BindView(R.id.item_main_assets_list_token_balance)
        TextView mAssetsTokenBalance;
        @BindView(R.id.item_main_assets_list_token_type)
        TextView mAssetsTokenType;
        @BindView(R.id.item_main_assets_list_token_value)
        TextView mAssetsTokenValue;
        @BindView(R.id.item_main_assets_list_token_simple_name)
        TextView mAssetsTokenSimpleName;
        @BindView(R.id.check_manager_check_box)
        MyCheckBox mCheckBox;
        @BindView(R.id.iv_drag_img)
        ImageView mAssetsDragImg;
        @BindView(R.id.view_under_line)
        View mAssetsUnderLine;

        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onItemSelected() {
            mTokenManagerContainer.setBackgroundResource(R.mipmap.icon_drag_bg);
            mAssetsUnderLine.setVisibility(View.GONE);
            if (onTouchStatusListener != null) {
                onTouchStatusListener.onTouch();
            }
        }

        @Override
        public void onItemClear() {
            mTokenManagerContainer.setBackgroundColor(0);
            mAssetsUnderLine.setVisibility(View.VISIBLE);
            if (onTouchStatusListener != null) {
                onTouchStatusListener.onRelease();
            }
        }
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public void setOnStartDragListener(OnStartDragListener dragListener) {
        onStartDragListener = dragListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnTouchStatusListener(OnTouchStatusListener onTouchStatuListener) {
        this.onTouchStatusListener = onTouchStatuListener;
    }

    public interface OnItemLongClickListener {
        /**
         * 长按
         *
         * @param assetsBean 当前对象
         */
        void onItemLongClick(AssetsBean assetsBean);
    }

    public interface OnItemClickListener {
        /**
         * 点击
         *
         * @param assetsBean 当前对象
         * @param isChecked 是否选中
         * @param currentPosition 当前item的Position
         */
        void onItemClick(AssetsBean assetsBean, boolean isChecked, int currentPosition);
    }

    public interface OnTouchStatusListener {
        /**
         * 触摸
         */
        void onTouch();

        /**
         * 释放
         */
        void onRelease();
    }


    public interface OnStartDragListener {
        /**
         * 快速拖拽
         *
         * @param viewHolder viewHolder
         * @param assetsBean 当前对象
         */
        void startDrag(RecyclerView.ViewHolder viewHolder, AssetsBean assetsBean);
    }

    public interface OnItemTouchCallbackListener {
        /**
         * 当某个Item被滑动删除时回调
         */
        void onSwiped(int adapterPosition);

        /**
         * 当两个Item位置互换的时候被回调(拖拽)
         *
         * @param srcPosition    拖拽的item的position
         * @param targetPosition 目的地的Item的position
         * @return 开发者处理了操作应该返回true，开发者没有处理就返回false
         */
        boolean onMove(int srcPosition, int targetPosition);
    }
}
