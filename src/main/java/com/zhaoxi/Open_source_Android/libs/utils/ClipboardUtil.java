package com.zhaoxi.Open_source_Android.libs.utils;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;

/**
 * @author ztt
 * @des
 * @date 2019/5/23.
 */

public class ClipboardUtil {
    /**
     * 复制到剪切板
     * @param context
     * @param str
     */
    public static void copyLable(Context context, String str) {
        //获取剪贴板管理器：
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        // 创建普通字符型ClipData
        ClipData mClipData = ClipData.newPlainText("Label", str);
        // 将ClipData内容放到系统剪贴板里。
        cm.setPrimaryClip(mClipData);
    }

    public static String GetClipboardContent(final Context context) {
        String content = "";
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        //无数据时直接返回
        if (!clipboard.hasPrimaryClip()) {
            return content;
        }
        //如果是文本信息
        if (clipboard.getPrimaryClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
            ClipData cdText = clipboard.getPrimaryClip();
            if(cdText != null && cdText.getItemCount() > 0){
                ClipData.Item item = cdText.getItemAt(0);
                if (item != null && item.getText() != null && item.getText().toString() != null && !StrUtil.isEmpty(item.getText().toString())) {
                    String str = item.getText().toString();
                    if(str.contains("hpbrp")){
                        String[] ct = str.split("hpbrp");
                        if(ct.length == 2){
                            return str;
                        }else return "";
                    }
                }
            }
        }
        return content;
    }
}
