package com.zhaoxi.Open_source_Android.ui.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Button;

import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.common.base.BaseTitleBarActivity;
import com.zhaoxi.Open_source_Android.common.bean.FlowBean;
import com.zhaoxi.Open_source_Android.common.view.AutoWrapViewGroup;
import com.zhaoxi.Open_source_Android.db.CreateDbWallet;
import com.zhaoxi.Open_source_Android.libs.tools.CommonDilogTool;
import com.zhaoxi.Open_source_Android.libs.utils.StrUtil;
import com.zhaoxi.Open_source_Android.ui.adapter.FlowViewAdapter;
import com.zhaoxi.Open_source_Android.ui.adapter.SelectFlowViewAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 备份助记词 第二步
 *
 * @author zhutt on 2018-06-12
 */
public class ExportMnemonicTwoActivity extends BaseTitleBarActivity {

    @BindView(R.id.export_mnemonic_two_flow_select)
    AutoWrapViewGroup mFlowSelect;
    @BindView(R.id.export_mnemonic_two_flow_init)
    AutoWrapViewGroup mFlowInit;
    @BindView(R.id.export_mnemonic_two_comfrim)
    Button mBtnComfrim;

    private String mMnemonic, mAddress;
    private List<FlowBean> mListData = new ArrayList<>();
    private List<FlowBean> mSelectData = new ArrayList<>();
    private FlowViewAdapter mFlowViewAdapter;
    private SelectFlowViewAdapter mSelectFlowViewAdapter;
    private CreateDbWallet mCreateDbWallet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export_mnemonic_two);
        ButterKnife.bind(this);
        mMnemonic = getIntent().getStringExtra(ExportMnemonicOneActivity.MNEMONIC_RESULT);
        mAddress = getIntent().getStringExtra(ExportMnemonicOneActivity.ADDRESS_RESULT);
        if (StrUtil.isEmpty(mMnemonic)) {
            finish();
        }
        setTitle(R.string.wallet_manager_details_txt_export_zhjici,true);
        showFreeDialog();
        initData();
    }

    private void initData() {
        mCreateDbWallet = new CreateDbWallet(this);
        //将String 转化为List
        String[] array = mMnemonic.split(" ");
        for (int i = 0; i < array.length; i++) {
            FlowBean flowBean = new FlowBean();
            flowBean.setContent(array[i]);
            flowBean.setChecked(false);
            mListData.add(flowBean);
        }

        mFlowViewAdapter = new FlowViewAdapter(this, mFlowInit, mListData,true);
        mSelectFlowViewAdapter = new SelectFlowViewAdapter(this, mFlowSelect, mSelectData);

        mFlowViewAdapter.setOnClickListener(new FlowViewAdapter.OnClickListener() {
            @Override
            public void setOnClickListener(String content) {
                FlowBean flowBean = new FlowBean();
                flowBean.setChecked(false);
                flowBean.setContent(content);
                mSelectData.add(flowBean);
                //跟新页面
                mSelectFlowViewAdapter.notifyDataChange(mSelectData);
                if(mSelectData.size() == 12){
                    mBtnComfrim.setEnabled(true);
                    mBtnComfrim.setTextColor(getResources().getColor(R.color.white));
                    mBtnComfrim.setBackgroundResource(R.drawable.draw_btn_defult_bg_03);
                }
            }

            @Override
            public void setOnCanleListener(String content) {
                int position = getPosition(mSelectData, content);
                if (position != -1) {
                    mSelectData.remove(position);
                    //更新页面
                    mSelectFlowViewAdapter.notifyDataChange(mSelectData);
                    if(mSelectData.size() < 12){
                        mBtnComfrim.setEnabled(false);
                        mBtnComfrim.setTextColor(getResources().getColor(R.color.color_2E2F47));
                        mBtnComfrim.setBackgroundResource(R.drawable.draw_btn_defult_bg_01);
                    }
                }
            }
        });

        mSelectFlowViewAdapter.setOnDeleteListener(new SelectFlowViewAdapter.OnDeleteListener() {
            @Override
            public void setOnDeleteListener(String content) {
                int position = getPosition(mSelectData, content);
                int pos = getInitPosition(mListData, content);
                if (position != -1 && pos != -1) {
                    mSelectData.remove(position);
                    mListData.get(pos).setChecked(false);
                    mFlowViewAdapter.notitfyCancleChange(mListData);
                    mSelectFlowViewAdapter.notifyDataChange(mSelectData);
                    if(mSelectData.size() < 12){
                        mBtnComfrim.setEnabled(false);
                        mBtnComfrim.setTextColor(getResources().getColor(R.color.color_2E2F47));
                        mBtnComfrim.setBackgroundResource(R.drawable.draw_btn_defult_bg_01);
                    }
                }
            }
        });
    }

    @OnClick(R.id.export_mnemonic_two_comfrim)
    public void onViewClicked() {
        //将mSelectData数字转换为String
        String[] array = new String[12];
        for (int i = 0; i < mSelectData.size(); i++) {
            array[i] = mSelectData.get(i).getContent();
        }
        StringBuilder build = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            if (i != array.length - 1) {
                build.append(array[i] + " ");
            } else build.append(array[i]);
        }

        if (mMnemonic.equals(build.toString())) {//助记词验证成功  弹出提示框
            showCreateWalletDilaog();
        } else {
            showErrorMneonic(getResources().getString(R.string.export_mnemonic_txt_tip_04));
        }
    }

    /**
     * 助记词正确提示
     */
    private void showCreateWalletDilaog() {
        CommonDilogTool dialogTool = new CommonDilogTool(this);
        dialogTool.show(null, getResources().getString(R.string.dialog_common_title_message_mnenocice_succese), null,
                getResources().getString(R.string.dailog_psd_btn_cancle), null,
                getResources().getString(R.string.dailog_psd_btn_ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //删除（修改""）助记词
                        int result = mCreateDbWallet.updateMnemonicstore(mAddress, "");
                        if (result == 0) {
                            setResult(0x090);
                            finish();
                        } else {
                            showErrorMneonic(getResources().getString(R.string.export_mnemonic_txt_tip_03));
                        }
                    }
                });
    }

    /**
     * 助记词错误提示
     *
     * @param error
     */
    private void showErrorMneonic(String error) {
        CommonDilogTool dialogTool = new CommonDilogTool(this);
        dialogTool.show(null, error, null,
                null, null,
                getResources().getString(R.string.dailog_psd_btn_ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
    }

    /**
     * 计算当前内容在数组中的位置
     *
     * @param flowBeans
     * @param content
     * @return
     */
    private int getPosition(List<FlowBean> flowBeans, String content) {
        FlowBean flowBean = null;
        for (int i = 0; i < flowBeans.size(); i++) {
            flowBean = flowBeans.get(i);
            if (flowBean.getContent().equals(content)) {
                return i;
            }
        }
        return -1;
    }

    private int getInitPosition(List<FlowBean> flowBeans, String content) {
        FlowBean flowBean = null;
        for (int i = 0; i < flowBeans.size(); i++) {
            flowBean = flowBeans.get(i);
            if (flowBean.getContent().equals(content)) {
                if(flowBean.isChecked()){//为了防止有重复的单词 需判断是否选中，只有选中才算
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * 免责声明
     */
    private void showFreeDialog() {
        CommonDilogTool dialogTool = new CommonDilogTool(this);
        dialogTool.show(getResources().getString(R.string.wallet_details_dialog_free_01),
                getResources().getString(R.string.wallet_details_dialog_free_02), null,
                null, null,
                getResources().getString(R.string.dailog_psd_btn_ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
    }
}
