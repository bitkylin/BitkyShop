package cc.bitky.bitkyshop.utils;


import android.support.annotation.Nullable;

import com.socks.library.KLog;

public class KyLog {

    public static String TAG = "bitky";

    public static void init(boolean isShowLog, @Nullable String tag) {
        TAG = tag;
        KLog.init(isShowLog, tag);
    }

    public static String getTAG(String msg) {
        return TAG + "_" + msg;
    }
}
