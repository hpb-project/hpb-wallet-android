package com.zhaoxi.Open_source_Android.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.libs.utils.StrUtil;

public class SensorEndDialog extends Dialog {
    public SensorEndDialog(Context context) {
        super(context);
    }

    public SensorEndDialog(Context context, int theme) {
        super(context, theme);
    }


    public interface HandlePsd {
        void getInputPsd(String psd);
    }


    public static class Builder {
        private Context mContext;
        private TextView mTxtStatus, mBtnOk;
        private String mKey;

        public void setType(String key) {
            this.mKey = key;
        }

        public interface SensorEndListener {
            void onHandleSensor();

            void onNoHandleSensor();
        }

        public void setSensorEndListener(SensorEndListener sensorEndListener) {
            this.mSensorEndListener = sensorEndListener;
        }

        private SensorEndListener mSensorEndListener;

        public Builder(Context context) {
            this.mContext = context;
        }

        public SensorEndDialog create() {
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final SensorEndDialog dialog = new SensorEndDialog(mContext, R.style.Dialog);
            View layout = inflater.inflate(R.layout.view_sensor_end_dialog, null);
            dialog.addContentView(layout, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            mTxtStatus = layout.findViewById(R.id.dialog_sensor_end_txt_status);
            mBtnOk = layout.findViewById(R.id.dialog_sensor_end_btn_ok);
            // 显示是否摇到红包的图片
            ImageView mIvRedPackShow = layout.findViewById(R.id.iv_main_read_pack_show);
            // 关闭红包弹窗按钮
            ImageView mIvRedPackClose = layout.findViewById(R.id.iv_main_red_pack_close);

            if (!StrUtil.isEmpty(mKey)) {
                mTxtStatus.setText(mContext.getResources().getString(R.string.activity_sensor_txt_01));
                mBtnOk.setText(mContext.getResources().getString(R.string.activity_sensor_txt_02));
                // 摇到红包了，显示摇到红包图片
                mIvRedPackShow.setImageResource(R.mipmap.icon_shake_to_red_pack);
                mBtnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mSensorEndListener.onHandleSensor();
                        dialog.dismiss();
                    }
                });
            } else {
                mTxtStatus.setText(mContext.getResources().getString(R.string.activity_sensor_txt_03));
                mBtnOk.setText(mContext.getResources().getString(R.string.activity_sensor_txt_04));
                // 没有摇到红包，显示未摇到红包图片
                mIvRedPackShow.setImageResource(R.mipmap.icon_sensor_top);
                mBtnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mSensorEndListener.onNoHandleSensor();
                        dialog.dismiss();
                    }
                });
            }

            // 关闭红包弹窗
            mIvRedPackClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            dialog.setContentView(layout);
            return dialog;
        }
    }
}
