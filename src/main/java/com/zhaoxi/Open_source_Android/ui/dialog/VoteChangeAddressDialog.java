package com.zhaoxi.Open_source_Android.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.base.DappApplication;
import com.zhaoxi.Open_source_Android.libs.utils.StrUtil;
import com.zhaoxi.Open_source_Android.web3.utils.Numeric;

public class VoteChangeAddressDialog extends Dialog {
    public VoteChangeAddressDialog(Context context) {
        super(context);
    }

    public VoteChangeAddressDialog(Context context, int theme) {
        super(context, theme);
    }


    public interface HandlePsd {
        void getInputPsd(String psd);
    }

    public static class Builder {
        private Context mContext;
        private EditText mEditAddress, mEditPsd;
        private TextView mTxtStatusAddress, mTxtAddress;
        private LinearLayout mLayoutPsd;
        private Button mBtnVoting;
        private ImageView mImgColse;
        private String mChibiAddress = "";
        private boolean mIsColdWallet = false;

        public interface VoteDialogVoteListener {
            void onHandleCahnge(String psd, String address);

            void onHandleCahnge(String address);
        }

        public void setVoteDialogVoteListener(VoteDialogVoteListener mVoteDialogVoteListener) {
            this.mVoteDialogVoteListener = mVoteDialogVoteListener;
        }

        private VoteDialogVoteListener mVoteDialogVoteListener;

        public void setIsColdWallet(boolean isColdWallet) {
            this.mIsColdWallet = isColdWallet;
        }

        public Builder(Context context) {
            this.mContext = context;
        }

        public VoteChangeAddressDialog create(String candidateAddress) {
            mChibiAddress = candidateAddress;
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final VoteChangeAddressDialog dialog = new VoteChangeAddressDialog(mContext, R.style.Dialog);
            View layout = inflater.inflate(R.layout.view_popw_vote_changeaddress_dialog, null);
            dialog.addContentView(layout, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            mEditAddress = layout.findViewById(R.id.view_pop_vote_changeaddress_edit_address);
            mEditPsd = layout.findViewById(R.id.view_pop_vote_changeaddress_edit_psd);
            mBtnVoting = layout.findViewById(R.id.view_pop_vote_changeaddress_btn_voting);
            mImgColse = layout.findViewById(R.id.view_pop_vote_changeaddress_img_close);
            mTxtStatusAddress = layout.findViewById(R.id.view_pop_vote_changeaddress_txt_status_address);
            mTxtAddress = layout.findViewById(R.id.view_pop_vote_changeaddress_txt_address);
            mLayoutPsd = layout.findViewById(R.id.view_pop_vote_changeaddress_pad_layout);
            if (!StrUtil.isEmpty(mChibiAddress)) {
                mEditAddress.setHint(mChibiAddress);
            }

            if (mIsColdWallet) {
                mLayoutPsd.setVisibility(View.GONE);
            } else {
                mLayoutPsd.setVisibility(View.VISIBLE);
            }

            mBtnVoting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    voteSubmit();
                }
            });
            mImgColse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            mEditAddress.addTextChangedListener(new VoteConutTextCher());
            dialog.setContentView(layout);
            return dialog;
        }

        private void voteSubmit() {
            String address = mEditAddress.getText().toString();
            if (StrUtil.isEmpty(address)) {
                DappApplication.getInstance().showToast(mContext.getResources().getString(R.string.activity_vote_details_txt_05));
                return;
            }
            if (!Numeric.isValidAddress(address)) {
                DappApplication.getInstance().showToast(mContext.getResources().getString(R.string.transfer_activity_toast_02));
                return;
            }

            if((mIsColdWallet)){
                mVoteDialogVoteListener.onHandleCahnge(address);
            }else{
                String psd = mEditPsd.getText().toString();
                if (StrUtil.isEmpty(psd)) {
                    DappApplication.getInstance().showToast(mContext.getResources().getString(R.string.activity_vote_txt_20));
                    return;
                }
                mVoteDialogVoteListener.onHandleCahnge(psd, address);
            }
        }

        private class VoteConutTextCher implements TextWatcher {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString();
                if (StrUtil.isEmpty(text)) {
                    mTxtStatusAddress.setVisibility(View.GONE);
                } else {//判断是否是合法地址
                    if (!Numeric.isValidAddress(text)) {
                        mTxtStatusAddress.setVisibility(View.VISIBLE);
                    } else {
                        mTxtStatusAddress.setVisibility(View.GONE);
                    }
                }

            }
        }
    }

    @Override
    public void show() {
        super.show();
        /**
         * 设置宽度全屏，要设置在show的后面
         */
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT;
        layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;

        getWindow().getDecorView().setPadding(0, 0, 0, 0);

        getWindow().setAttributes(layoutParams);
    }


}
