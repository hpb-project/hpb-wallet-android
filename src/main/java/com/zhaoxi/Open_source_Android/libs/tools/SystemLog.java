package com.zhaoxi.Open_source_Android.libs.tools;

import android.util.Log;

import com.zhaoxi.Open_source_Android.Config;

public class SystemLog {
    public static final int LEVEL_VERBOSE = 0X001;
    public static final int LEVEL_DEBUG = 0X002;
    public static final int LEVEL_INFO = 0X003;
    public static final int LEVEL_WARNING = 0X004;
    public static final int LEVEL_ERROR = 0X005;

    public static void V(String tag, String msg) {
        log(LEVEL_VERBOSE, tag, msg);
    }

    public static void D(String tag, String msg) {
        log(LEVEL_DEBUG, tag, msg);
    }

    public static void I(String tag, String msg) {
        log(LEVEL_INFO, tag, msg);
    }

    public static void W(String tag, String msg) {
        log(LEVEL_WARNING, tag, msg);
    }

    public static void E(String tag, String msg) {
        log(LEVEL_ERROR, tag, msg);
    }

    private static void log(int logLevel, String logTag, String logMessage) {
        if (!Config.isPrduction) {
            switch (logLevel) {
                case LEVEL_VERBOSE:
                    Log.v(logTag, logMessage);
                    break;
                case LEVEL_DEBUG:
                    Log.d(logTag, logMessage);
                    break;
                case LEVEL_INFO:
                    Log.i(logTag, logMessage);
                    break;
                case LEVEL_WARNING:
                    Log.w(logTag, logMessage);
                    break;
                case LEVEL_ERROR:
                    Log.e(logTag, logMessage);
                    break;
                default:
                    // empty here
                    break;
            }
        }
    }
}
