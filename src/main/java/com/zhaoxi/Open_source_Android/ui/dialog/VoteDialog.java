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
import com.zhaoxi.Open_source_Android.libs.utils.SlinUtil;
import com.zhaoxi.Open_source_Android.libs.utils.StrUtil;

import java.math.BigDecimal;

public class VoteDialog extends Dialog {

    /**
     * 投票
     */
    public static final int FLAGS_VOTE = 0x10;
    /**
     * 撤销投票
     */
    public static final int FLAGS_CANCEL_VOTE = 0x11;


    public VoteDialog(Context context) {
        super(context);
    }

    public VoteDialog(Context context, int theme) {
        super(context, theme);
    }


    public interface HandlePsd {
        void getInputPsd(String psd);
    }

    public static class Builder {
        private Context mContext;
        private ImageView mImgSub, mImgAdd;
        private EditText mEditVoteNum, mEditPsd;
        private TextView mTxtNodeName, mTxtResidue;
        private Button mBtnVoting;
        private ImageView mImgColse;
        private LinearLayout mLayoutPsd;


        private BigDecimal mMaxVoteCount;
        private int flags;
        private String mName;
        private TextView mTicketNum;
        private boolean mIsColdWallet = false;

        public void setName(String name) {
            this.mName = name;
        }

        public void setIsColdWallet(boolean isColdWallet){
            this.mIsColdWallet = isColdWallet;
        }

        public void setMaxVoteCount(BigDecimal voteConut) {
            this.mMaxVoteCount = voteConut;
        }

        /**
         * 设置Dialog的显示类型
         * FLAGS_VOTE 投票
         * FLAGS_CANCEL_VOTE 撤销投票
         *
         * @param flags 类型
         */
        public void setFlags(int flags) {
            this.flags = flags;
        }

        public interface VoteDialogVoteListener {
            void onHandleVote(String psd, String poll, int flags);
            void onHandleVote(String poll, int flags);
        }

        public void setVoteDialogVoteListener(VoteDialogVoteListener mVoteDialogVoteListener) {
            this.mVoteDialogVoteListener = mVoteDialogVoteListener;
        }

        private VoteDialogVoteListener mVoteDialogVoteListener;

        public Builder(Context context) {
            this.mContext = context;
        }

        public VoteDialog create() {
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final VoteDialog dialog = new VoteDialog(mContext, R.style.Dialog);
            View layout = inflater.inflate(R.layout.view_popwindow_vote_dialog, null);
            dialog.addContentView(layout, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            mImgSub = layout.findViewById(R.id.view_pop_vote_compute_sub);
            mImgAdd = layout.findViewById(R.id.view_pop_vote_compute_add);
            mEditVoteNum = layout.findViewById(R.id.view_pop_vote_compute_content);
            mTxtNodeName = layout.findViewById(R.id.view_pop_vote_name);
            mTxtResidue = layout.findViewById(R.id.view_pop_vote_residue_count);
            mEditPsd = layout.findViewById(R.id.view_pop_vote_edit_psd);
            mBtnVoting = layout.findViewById(R.id.view_pop_vote_btn_voting);
            mImgColse = layout.findViewById(R.id.view_pop_vote_img_close);
            mTicketNum = layout.findViewById(R.id.view_ticket_num);
            mLayoutPsd = layout.findViewById(R.id.view_pop_vote_layout_psd);
            mTxtNodeName.setText(mName);

            if(mIsColdWallet){
                mLayoutPsd.setVisibility(View.GONE);
            }else{
                mLayoutPsd.setVisibility(View.VISIBLE);
            }

            if (flags == FLAGS_CANCEL_VOTE) {
                String cancelEnable = String.format(mContext.getResources().getString(R.string.activity_vote_cancel_enable_txt_16), SlinUtil.NumberFormat0(mContext, mMaxVoteCount));
                mTxtResidue.setText(cancelEnable);
                mTicketNum.setText(mContext.getResources().getString(R.string.activity_vote_txt_34));
                mBtnVoting.setText(mContext.getResources().getString(R.string.activity_vote_txt_28));
            } else {
                String str = String.format(mContext.getResources().getString(R.string.activity_vote_txt_16), SlinUtil.NumberFormat0(mContext, mMaxVoteCount));
                mTxtResidue.setText(str);
                mBtnVoting.setText(mContext.getResources().getString(R.string.activity_vote_txt_25));
                mTicketNum.setText(mContext.getResources().getString(R.string.activity_vote_txt_23));
            }

            mImgSub.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    computeSub();
                }
            });
            mImgAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    computeAdd();
                }
            });
            mBtnVoting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (flags == FLAGS_CANCEL_VOTE) {
                        // 撤销投票
                        submit(FLAGS_CANCEL_VOTE);
                    } else {
                        // 投票
                        submit(FLAGS_VOTE);
                    }

                }
            });
            mImgColse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            mEditVoteNum.addTextChangedListener(new VoteConutTextCher());
            dialog.setContentView(layout);
            return dialog;
        }

        private void computeAdd() {
            String count = mEditVoteNum.getText().toString();
            BigDecimal curConut = new BigDecimal(count.equals("0") || count.equals("") ? "0" : count);
            if (curConut.compareTo(mMaxVoteCount) < 0) {
                voteCompute(curConut.add(new BigDecimal(1)));
            }
        }

        private void computeSub() {
            String count = mEditVoteNum.getText().toString();
            BigDecimal curConut = new BigDecimal(count.equals("0") || count.equals("") ? "0" : count);
            if (curConut.compareTo(BigDecimal.ZERO) > 0) {
                voteCompute(curConut.subtract(new BigDecimal(1)));
            }
        }

        private void voteCompute(BigDecimal count) {
            String strCount = String.valueOf(count);
            mEditVoteNum.setText(strCount);
            mEditVoteNum.setSelection(strCount.length());
        }


        private void submit(int flags) {
            String poll = mEditVoteNum.getText().toString();
            if (StrUtil.isEmpty(poll)) {
                DappApplication.getInstance().showToast(mContext.getResources().getString(R.string.activity_vote_txt_17));
                return;
            }
            if (poll.equals("0")) {
                if (flags == FLAGS_CANCEL_VOTE) {
                    DappApplication.getInstance().showToast(mContext.getResources().getString(R.string.activity_cancel_vote_txt_18));
                } else {
                    DappApplication.getInstance().showToast(mContext.getResources().getString(R.string.activity_vote_txt_18));
                }
                return;
            }
            if (new BigDecimal(poll).subtract(mMaxVoteCount).compareTo(BigDecimal.ZERO) > 0) {
                if (flags == FLAGS_CANCEL_VOTE) {
                    DappApplication.getInstance().showToast(mContext.getResources().getString(R.string.activity_cancel_vote_txt_19));
                } else {
                    DappApplication.getInstance().showToast(mContext.getResources().getString(R.string.activity_vote_txt_19));
                }
                return;
            }
            if(mIsColdWallet){
                mVoteDialogVoteListener.onHandleVote(poll, flags);
            }else{
                String psd = mEditPsd.getText().toString();
                if (StrUtil.isEmpty(psd)) {
                    DappApplication.getInstance().showToast(mContext.getResources().getString(R.string.activity_vote_txt_20));
                    return;
                }
                mVoteDialogVoteListener.onHandleVote(psd, poll, flags);
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
                BigDecimal count = new BigDecimal(StrUtil.isEmpty(text) ? "0" : text);
                if (count.compareTo(BigDecimal.ZERO) == 0) {
                    mEditVoteNum.setTextColor(mContext.getResources().getColor(R.color.color_black_999));
                }

                if (count.compareTo(BigDecimal.ZERO) > 0) {
                    mEditVoteNum.setTextColor(mContext.getResources().getColor(R.color.color_black_000));
                }

//                if (count.compareTo(mMaxVoteCount) < 0) {
//                }
//
//                if (count.compareTo(mMaxVoteCount) >= 0) {
//                }
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
