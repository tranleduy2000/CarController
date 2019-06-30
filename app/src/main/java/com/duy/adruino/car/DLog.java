package com.duy.adruino.car;

import android.util.Log;

public class DLog {
    public static final boolean DEBUG = BuildConfig.DEBUG;

    public static void d(String tag, String s) {
        Log.d(tag, s);
    }
}
