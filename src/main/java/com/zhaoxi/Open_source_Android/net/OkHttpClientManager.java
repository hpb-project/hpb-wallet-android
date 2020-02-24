package com.zhaoxi.Open_source_Android.net;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.zhaoxi.Open_source_Android.dapp.R;
import com.zhaoxi.Open_source_Android.libs.tools.SystemLog;
import com.zhaoxi.Open_source_Android.libs.utils.CollectionUtil;
import com.zhaoxi.Open_source_Android.net.BaseBet.DefultContans;
import com.zhaoxi.Open_source_Android.net.BaseBet.IOkHttpClient;
import com.zhaoxi.Open_source_Android.net.BaseBet.NetResultCallBack;
import com.zhaoxi.Open_source_Android.web3.utils.Strings;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by 51973 on 2018/5/9.
 */

public class OkHttpClientManager implements IOkHttpClient {
    private static final String TAG = OkHttpClientManager.class.getSimpleName();
    private static volatile OkHttpClientManager mInstance;
    private OkHttpClient mOkHttpClient;

    private Handler mHandler;
    private Map<String, NetResultCallBack> mCallBackMap;
    private Context mContext;

    private OkHttpClientManager() {
//        mOkHttpClient = new OkHttpClient();
        // 超时时间设置长一些保证数据能够有充足的加载时间，默认时间10秒
        mOkHttpClient = new OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS).readTimeout(30,TimeUnit.SECONDS).build();
        mCallBackMap = new HashMap<String, NetResultCallBack>();
        mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == DefultContans.HANDLER_MSG_WHAT) {
                    String key = msg.getData().getString(DefultContans.NET_CALLBACK_KEY);
                    int type = msg.getData().getInt(DefultContans.NET_TYPE_KEY);
                    NetResultCallBack callBack = mCallBackMap.get(key);
                    mCallBackMap.remove(key);
                    if (type == DefultContans.NET_SUCCESE) {//请求成功
                        String response = (String) msg.obj;
                        if (Strings.isEmpty(response)) {//Json error
                            callBack.onError(mContext.getResources().getString(R.string.exception_json_error_02));
                            return;
                        }
                        JSONArray infoArray = null;
                        try {
                            //将数据转成JSONArray
                            infoArray = JSONArray.parseArray(response);
                            SystemLog.D(TAG, "[doRequest]" + infoArray.toJSONString());
                            if (infoArray.size() > 0) {
                                if ("000000".equals(infoArray.get(0))) {
                                    callBack.onSuccess(infoArray);
                                } else {
                                    String error = infoArray.get(1).toString();
                                    if ("40101".equals(error)) {
                                        error = mContext.getResources().getString(R.string.service_data_state_01);
                                    } else if ("10104".equals(error)) {
                                        error = mContext.getResources().getString(R.string.service_data_state_02);
                                    } else if ("10105".equals(error)) {
                                        error = mContext.getResources().getString(R.string.service_data_state_03);
                                    } else if ("10106".equals(error)) {
                                        error = mContext.getResources().getString(R.string.service_data_state_04);
                                    } else if ("10101".equals(error)) {
                                        error = mContext.getResources().getString(R.string.service_data_state_05);
                                    } else if ("insufficient funds for gas * price + value".equals(error)) {
                                        error = mContext.getResources().getString(R.string.service_data_state_06);
                                    } else if ("nonce too low".equals(error)) {
                                        error = mContext.getResources().getString(R.string.service_data_state_07);
                                    } else if ("replacement transaction underpriced".equals(error)) {
                                        error = mContext.getResources().getString(R.string.service_data_state_08);
                                    }
                                    callBack.onError(error);
                                }
                            } else {
                                callBack.onError(mContext.getResources().getString(R.string.exception_json_error_02));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            String error = mContext.getResources().getString(R.string.exception_json_error_02);
                            callBack.onError(error);
                        }
                        msg.obj = null;//为了安全，释放掉
                    } else {
                        String error = "filed";
                        if (msg.getData() != null) {
                            error = msg.getData().getString(DefultContans.NET_ERROR_KEY);
                            SystemLog.E("error", error);
                            if (error.contains("SocketTimeoutException")) {
                                error = mContext.getResources().getString(R.string.exception_json_error_01);
                            } else if (error.contains("DataErrorException")) {
                                error = mContext.getResources().getString(R.string.exception_json_error_02);
                            } else if (error.contains("NetworkErrorException")) {
                                error = mContext.getResources().getString(R.string.exception_netword_error);
                            } else {
                                error = mContext.getResources().getString(R.string.exception_json_error_02);
                            }
                            callBack.onError(error);
                        }
                    }
                }
            }
        };
    }

    public static OkHttpClientManager getInstance() {
        if (mInstance == null) {
            synchronized (OkHttpClientManager.class) {
                if (mInstance == null)
                    mInstance = new OkHttpClientManager();
            }
        }
        return mInstance;
    }

    @Override
    public void httpGet(String url, NetResultCallBack callBack) {
        final Request request = new Request.Builder().url(url).build();
        buildPostRequest(request, callBack);
    }

    @Override
    public void httpPostJson(Context context, String url, List<String> params, NetResultCallBack callBack) {
        mContext = context;
        String json = JSON.toJSONString(params);
        SystemLog.D(TAG, "[params]" + json);
        RequestBody body = RequestBody.create(DefultContans.JSON_TYPE, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        buildPostRequest(request, callBack);
    }

    @Override
    public void httpPostPairs(Context context, String url, Map<String, Object> params, List<String> files, NetResultCallBack callBack) {
        mContext = context;
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        //判断是否有参数
        if (params != null) {
            for (String key : params.keySet()) {
                builder.addFormDataPart(key, params.get(key).toString());
            }
        }
        //判断是否有文件
        if (!CollectionUtil.isCollectionEmpty(files)) {
            for (String path : files) {
                File file = new File(path);
                builder.addFormDataPart("files", file.getName(), RequestBody.create(null, file));
            }
        }

        RequestBody body = builder.build();
        Request request = new Request.Builder().url(url).post(body).build();
        buildPostRequest(request, callBack);
    }

    private void buildPostRequest(Request request, final NetResultCallBack callBack) {
        mCallBackMap.put(callBack.toString(), callBack);
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                if (callBack != null) {
                    Bundle data = new Bundle();
                    Message msg = mHandler.obtainMessage();
                    String error = e.toString();
                    data.putString(DefultContans.NET_ERROR_KEY, error);
                    data.putInt(DefultContans.NET_TYPE_KEY, DefultContans.NET_ERROR);
                    data.putString(DefultContans.NET_CALLBACK_KEY, callBack.toString());

                    msg.setData(data);
                    msg.what = DefultContans.HANDLER_MSG_WHAT;
                    msg.sendToTarget();
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String str = response.body().string();
                if (callBack != null) {
                    Bundle data = new Bundle();
                    Message msg = mHandler.obtainMessage();
                    data.putInt(DefultContans.NET_TYPE_KEY, DefultContans.NET_SUCCESE);
                    data.putString(DefultContans.NET_CALLBACK_KEY, callBack.toString());
                    msg.setData(data);
                    msg.obj = str;
                    msg.what = DefultContans.HANDLER_MSG_WHAT;
                    msg.sendToTarget();
                }
            }
        });
    }

}
